package io.github.aapplet.wechat.cert;

import io.github.aapplet.wechat.common.WeChatStatusCode;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.exception.WeChatRequestException;
import io.github.aapplet.wechat.exception.WeChatResponseException;
import io.github.aapplet.wechat.exception.WeChatValidationException;
import io.github.aapplet.wechat.http.WeChatHttpRequest;
import io.github.aapplet.wechat.http.WeChatValidator;
import io.github.aapplet.wechat.util.WeChatPemUtil;
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
     * 配置信息
     */
    @NonNull
    private final WeChatConfig wechatConfig;

    /**
     * key   = 商户号 or 证书序列号
     * value = 平台证书
     */
    private static final Map<String, X509Certificate> CERTIFICATES = new ConcurrentHashMap<>(4);

    @Override
    public X509Certificate getCertificate() {
        var mchId = wechatConfig.getMerchantId();
        var certificate = CERTIFICATES.get(mchId);
        var expireDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60);
        if (certificate == null || certificate.getNotAfter().before(expireDate)) {
            synchronized (this) {
                certificate = CERTIFICATES.get(mchId);
                if (certificate == null || certificate.getNotAfter().before(expireDate)) {
                    certificate = loadCertificate(mchId);
                }
            }
        }
        return certificate;
    }

    @Override
    public X509Certificate getCertificate(String key) {
        return CERTIFICATES.computeIfAbsent(key, this::loadCertificate);
    }

    /**
     * 加载平台证书
     *
     * @param key 证书序列号 or 商户号
     * @return 平台证书
     */
    private X509Certificate loadCertificate(String key) {
        this.checkCertificates();
        //
        var httpResponse = WeChatHttpRequest.v3(wechatConfig, new WeChatCertificateRequest());
        if (httpResponse.statusCode() != 200) {
            throw new WeChatRequestException(WeChatStatusCode.PAY.fromJson(httpResponse.body()).getMessage());
        }
        var certificateResponse = WeChatCertificateResponse.fromJson(httpResponse.body());
        if (certificateResponse == null || certificateResponse.getData() == null) {
            throw new WeChatResponseException("获取的平台证书数据异常");
        }
        var newCertificates = new HashMap<String, X509Certificate>(4);
        for (WeChatCertificateResponse.Item item : certificateResponse.getData()) {
            var encryptCertificate = item.getEncryptCertificate();
            var associatedData = encryptCertificate.getAssociatedData();
            var ciphertext = encryptCertificate.getCiphertext();
            var nonceStr = encryptCertificate.getNonce();
            var serialNo = item.getSerialNo();
            var decrypt = wechatConfig.decrypt(nonceStr, associatedData, ciphertext);
            var certificate = WeChatPemUtil.getCertificate(decrypt);
            var issuer = certificate.getIssuerX500Principal().getName();
            if (issuer.contains(wechatConfig.getIssuer())) {
                newCertificates.put(serialNo, certificate);
            } else {
                throw new WeChatValidationException("平台证书颁发者验证失败, Unknown Issuer => " + issuer);
            }
        }
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
            throw new WeChatValidationException("平台证书, 签名验证失败");
        }
        return CERTIFICATES.get(key);
    }

    /**
     * 检查平台证书
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