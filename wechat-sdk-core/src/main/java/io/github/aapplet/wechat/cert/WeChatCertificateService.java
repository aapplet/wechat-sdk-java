package io.github.aapplet.wechat.cert;

import io.github.aapplet.wechat.common.WeChatPaymentResponse;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.exception.WeChatException;
import io.github.aapplet.wechat.exception.WeChatResponseException;
import io.github.aapplet.wechat.exception.WeChatValidationException;
import io.github.aapplet.wechat.http.WeChatHttpRequest;
import io.github.aapplet.wechat.util.WeChatPemUtil;
import io.github.aapplet.wechat.util.WeChatValidator;

import java.net.http.HttpResponse;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * 平台证书管理器
 */
public class WeChatCertificateService implements WeChatCertificateManager {

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
    public WeChatCertificateService(WeChatConfig weChatConfig) {
        this.weChatConfig = weChatConfig;
    }

    /**
     * 获取最新平台证书
     *
     * @return 平台证书
     */
    @Override
    public X509Certificate getCertificate() {
        final String mchId = weChatConfig.getMchId();
        X509Certificate certificate = LATEST_MAP.get(mchId);
        try {
            certificate.checkValidity();
        } catch (NullPointerException | CertificateExpiredException | CertificateNotYetValidException e) {
            synchronized (WeChatCertificateService.class) {
                final X509Certificate validation = LATEST_MAP.get(mchId);
                if (validation == null || Objects.equals(validation, certificate)) {
                    this.loadCertificate();
                    this.checkCertificate(LATEST_MAP);
                }
            }
            if ((certificate = LATEST_MAP.get(mchId)) == null) {
                throw new WeChatException("平台证书加载失败");
            }
        }
        return certificate;
    }

    /**
     * 获取平台证书
     *
     * @param serialNumber 证书序列号
     * @return 平台证书
     */
    @Override
    public X509Certificate getCertificate(String serialNumber) {
        X509Certificate certificate = CERTIFICATE_MAP.get(serialNumber);
        if (certificate == null) {
            synchronized (WeChatCertificateService.class) {
                if (!CERTIFICATE_MAP.containsKey(serialNumber)) {
                    this.loadCertificate();
                    this.checkCertificate(CERTIFICATE_MAP);
                }
            }
            if ((certificate = CERTIFICATE_MAP.get(serialNumber)) == null) {
                throw new WeChatException("未知的平台证书序列号");
            }
        }
        return certificate;
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
        final WeChatValidator validator = new WeChatValidator(weChatConfig, httpResponse);
        final X509Certificate validatorCertificate = certificates.get(validator.getWeChatHeaders().getSerial());
        if (validatorCertificate != null && validator.verify(validatorCertificate)) {
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

}