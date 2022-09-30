package io.github.aapplet.wechat.cert;

import java.security.cert.X509Certificate;

/**
 * 平台证书管理器
 */
public interface WeChatCertificateManager {

    /**
     * 获取最新平台证书
     *
     * @return 平台证书
     */
    X509Certificate getCertificate();

    /**
     * 获取平台证书
     *
     * @param serialNumber 证书序列号
     * @return 平台证书
     */
    X509Certificate getCertificate(String serialNumber);

}