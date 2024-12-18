package io.github.aapplet.wechat.cert;

import java.security.cert.X509Certificate;

/**
 * 平台证书管理器
 */
public interface WeChatCertificateManager {

    /**
     * 获取证书
     *
     * @return X.509证书实例
     */
    X509Certificate getCertificate();

    /**
     * 根据证书序列号获取证书
     *
     * @param serialNumber 证书序列号
     * @return X.509证书实例
     */
    X509Certificate getCertificate(String serialNumber);

}