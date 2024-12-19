package io.github.aapplet.wechat.cert;

import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.exception.WeChatRequestException;
import io.github.aapplet.wechat.exception.WeChatResponseException;
import io.github.aapplet.wechat.exception.WeChatValidationException;
import io.github.aapplet.wechat.http.WeChatHttpRequest;
import io.github.aapplet.wechat.http.WeChatValidator;
import io.github.aapplet.wechat.response.WeChatStatusCode;
import io.github.aapplet.wechat.util.WeChatCertUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 平台证书管理器
 */
@RequiredArgsConstructor
public class WeChatCertificateService implements WeChatCertificateManager {

    /**
     * 存储平台证书的映射表
     * <br>
     * key   = 证书序列号 or 商户号
     * <br>
     * value = 平台证书
     */
    private static final Map<String, X509Certificate> CERTIFICATES = new ConcurrentHashMap<>(4);

    /**
     * 配置信息
     */
    @NonNull
    private final WeChatConfig wechatConfig;

    @Override
    public X509Certificate getCertificate() {
        var merchantId = wechatConfig.getMerchantId();
        var certificate = CERTIFICATES.get(merchantId);
        var expireDate = new Date(System.currentTimeMillis() + (1000 * 60 * 5));
        if (certificate == null || certificate.getNotAfter().before(expireDate)) {
            synchronized (this) {
                certificate = CERTIFICATES.get(merchantId);
                if (certificate == null || certificate.getNotAfter().before(expireDate)) {
                    certificate = loadCertificate(merchantId);
                }
            }
        }
        return certificate;
    }

    @Override
    public X509Certificate getCertificate(String serialNumber) {
        return CERTIFICATES.computeIfAbsent(serialNumber, this::loadCertificate);
    }

    @Override
    public X509Certificate setCertificate(String serialNumber, X509Certificate certificate) {
        return CERTIFICATES.put(serialNumber, certificate);
    }

    /**
     * 加载平台证书
     *
     * @param serialNumber 证书序列号 or 商户号
     * @return 平台证书
     */
    private X509Certificate loadCertificate(String serialNumber) {
        // 检查证书有效性
        this.checkCertificates();
        // 发送请求获取证书数据
        var httpResponse = WeChatHttpRequest.v3(wechatConfig, new WeChatCertificateRequest());
        if (httpResponse.statusCode() != 200) {
            throw new WeChatRequestException(WeChatStatusCode.PAY.fromJson(httpResponse.body()).getMessage());
        }
        var certificateResponse = WeChatCertificateResponse.fromJson(httpResponse.body());
        if (certificateResponse == null || certificateResponse.getData() == null) {
            throw new WeChatResponseException("获取的平台证书数据异常");
        }
        // 解密证书
        var newCertificates = new HashMap<String, X509Certificate>(4);
        for (WeChatCertificateResponse.Item item : certificateResponse.getData()) {
            var encryptCertificate = item.getEncryptCertificate();
            var associatedData = encryptCertificate.getAssociatedData();
            var ciphertext = encryptCertificate.getCiphertext();
            var nonceStr = encryptCertificate.getNonce();
            var serialNo = item.getSerialNo();
            var decrypt = wechatConfig.decrypt(nonceStr, associatedData, ciphertext);
            var certificate = WeChatCertUtil.generateCertificate(decrypt);
            var issuer = certificate.getIssuerX500Principal().getName();
            if (issuer.contains(wechatConfig.getIssuer())) {
                newCertificates.put(serialNo, certificate);
            } else {
                throw new WeChatValidationException("平台证书颁发者验证失败, Unknown Issuer => " + issuer);
            }
        }
        // 验证证书签名
        var validator = new WeChatValidator(wechatConfig, httpResponse);
        var validatorCertificate = newCertificates.get(validator.getWechatHeaders().getSerial());
        if (validatorCertificate != null && validator.verify(validatorCertificate)) {
            X509Certificate latestCertificate = null;
            for (X509Certificate certificate : newCertificates.values()) {
                if (latestCertificate == null || certificate.getNotBefore().after(latestCertificate.getNotBefore())) {
                    latestCertificate = certificate;
                }
            }
            CERTIFICATES.putAll(newCertificates);
            CERTIFICATES.put(wechatConfig.getMerchantId(), latestCertificate);
        } else {
            throw new WeChatValidationException("平台证书的签名验证失败");
        }
        return CERTIFICATES.get(serialNumber);
    }

    /**
     * 检查并移除过期或未生效的平台证书
     */
    private void checkCertificates() {
        CERTIFICATES.forEach((key, certificate) -> {
            try {
                certificate.checkValidity();
            } catch (CertificateExpiredException | CertificateNotYetValidException e) {
                CERTIFICATES.remove(key, certificate);
            }
        });
    }

}