package io.github.aapplet.wechat.cert;

import io.github.aapplet.wechat.WeChatValidator;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.exception.WeChatException;
import io.github.aapplet.wechat.exception.WeChatResponseException;
import io.github.aapplet.wechat.exception.WeChatValidationException;
import io.github.aapplet.wechat.http.WeChatHttpRequest;
import io.github.aapplet.wechat.response.WeChatPaymentResponse;
import io.github.aapplet.wechat.util.WeChatPemUtil;

import java.net.http.HttpResponse;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * 平台证书管理器
 */
public class WeChatCertificateManager {

    /**
     * key   = 证书序列号
     * value = 平台证书
     */
    private static final Map<String, X509Certificate> CERTIFICATE_MAP = new ConcurrentHashMap<>(4);
    /**
     * key   = 商户号
     * value = 平台证书
     */
    private static final Map<String, X509Certificate> LATEST_MAP = new ConcurrentHashMap<>(4);
    /**
     * 配置信息
     */
    private final WeChatConfig weChatConfig;

    /**
     * @param weChatConfig 配置信息
     */
    private WeChatCertificateManager(WeChatConfig weChatConfig) {
        this.weChatConfig = weChatConfig;
    }

    /**
     * 获取平台证书
     *
     * @param serialNumber 证书序列号
     * @return 平台证书
     */
    private X509Certificate getCertificate(String serialNumber) {
        X509Certificate x509Certificate = CERTIFICATE_MAP.get(serialNumber);
        if (x509Certificate == null) {
            synchronized (WeChatCertificateManager.class) {
                x509Certificate = CERTIFICATE_MAP.get(serialNumber);
                if (x509Certificate == null) {
                    this.loadCertificate();
                    this.checkCertificate(CERTIFICATE_MAP);
                }
            }
            if (x509Certificate == null) {
                x509Certificate = CERTIFICATE_MAP.get(serialNumber);
            }
            if (x509Certificate == null) {
                throw new WeChatException("未知的平台证书序列号");
            }
        }
        return x509Certificate;
    }

    /**
     * 获取最新平台证书
     *
     * @return 平台证书
     */
    private X509Certificate getCertificate() {
        final String mchId = weChatConfig.getMchId();
        X509Certificate x509Certificate = LATEST_MAP.get(mchId);
        try {
            x509Certificate.checkValidity();
        } catch (NullPointerException | CertificateExpiredException | CertificateNotYetValidException e) {
            synchronized (WeChatCertificateManager.class) {
                if (x509Certificate == null || LATEST_MAP.remove(mchId, x509Certificate)) {
                    this.loadCertificate();
                    this.checkCertificate(LATEST_MAP);
                }
            }
            if ((x509Certificate = LATEST_MAP.get(mchId)) == null) {
                throw new WeChatException("平台证书加载失败");
            }
        }
        return x509Certificate;
    }

    /**
     * 加载平台证书
     */
    private void loadCertificate() {
        final HttpResponse<byte[]> httpResponse = WeChatHttpRequest.v3(weChatConfig, new WeChatCertificateRequest());
        if (httpResponse.statusCode() == 401) {
            throw new WeChatException("签名信息错误,请检查配置信息");
        }
        if (httpResponse.statusCode() != 200) {
            throw new WeChatResponseException(WeChatPaymentResponse.fromJson(httpResponse.body()));
        }
        final WeChatValidator validator = new WeChatCertificateValidator(weChatConfig, httpResponse);
        final Map<String, X509Certificate> certificates = new HashMap<>(4);
        final WeChatCertificateResponse certificateResponse = WeChatCertificateResponse.fromJson(httpResponse.body());
        for (WeChatCertificateResponse.WeChatCertificate certificate : certificateResponse.getCertificates()) {
            final WeChatCertificateResponse.EncryptCertificate encryptCertificate = certificate.getEncryptCertificate();
            final String associatedData = encryptCertificate.getAssociatedData();
            final String ciphertext = encryptCertificate.getCiphertext();
            final String nonceStr = encryptCertificate.getNonce();
            final String serialNumber = certificate.getSerialNo();
            final byte[] decrypt = weChatConfig.decrypt(nonceStr, associatedData, ciphertext);
            certificates.put(serialNumber, WeChatPemUtil.getCertificate(decrypt));
        }
        if (validator.verify(certificates.get(validator.getWeChatHeaders().getSerial()))) {
            CERTIFICATE_MAP.putAll(certificates);
            final Stream<X509Certificate> stream = certificates.values().stream();
            final Optional<X509Certificate> optional = stream.max(Comparator.comparing(X509Certificate::getNotBefore));
            optional.ifPresent(certificate -> LATEST_MAP.put(weChatConfig.getMchId(), certificate));
        } else {
            throw new WeChatValidationException("平台证书错误,验签失败");
        }
    }

    /**
     * 检查平台证书
     */
    private void checkCertificate(Map<String, X509Certificate> certificates) {
        certificates.forEach((key, certificate) -> {
            try {
                certificate.checkValidity();
            } catch (CertificateExpiredException | CertificateNotYetValidException e) {
                certificates.remove(key);
            }
        });
    }

    /**
     * 获取平台证书
     *
     * @param weChatConfig 配置信息
     * @return 平台证书
     */
    public static X509Certificate getCertificate(WeChatConfig weChatConfig) {
        return new WeChatCertificateManager(weChatConfig).getCertificate();
    }

    /**
     * 获取平台证书
     *
     * @param weChatConfig 配置信息
     * @param serialNumber 证书序列号
     * @return 平台证书
     */
    public static X509Certificate getCertificate(WeChatConfig weChatConfig, String serialNumber) {
        return new WeChatCertificateManager(weChatConfig).getCertificate(serialNumber);
    }

}