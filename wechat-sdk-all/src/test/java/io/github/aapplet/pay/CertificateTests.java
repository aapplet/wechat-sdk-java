package io.github.aapplet.pay;

import io.github.aapplet.wechat.cert.WeChatCertificateManager;
import io.github.aapplet.wechat.config.WeChatConfig;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.security.cert.X509Certificate;

public class CertificateTests {

    private final WeChatConfig wechatConfig = WeChatConfig.load("config.json");

    @Test
    @SneakyThrows
    void load() {
        wechatConfig.loadPrivateKeyFromPath("privateKey.pem");
        WeChatCertificateManager certificateManager = wechatConfig.getCertificateManager();
        String serialNumber = certificateManager.getCertificate().getSerialNumber().toString(16).toUpperCase();
        X509Certificate certificate1 = certificateManager.getCertificate(serialNumber);
        X509Certificate certificate2 = certificateManager.getCertificate(wechatConfig.getMerchantId());
        System.out.println(certificate1.getIssuerX500Principal().getName());
        System.out.println(certificate1.getNotBefore());
        System.out.println(certificate1.getNotAfter());
        System.out.println(certificate2.getIssuerX500Principal().getName());
        System.out.println(certificate2.getNotBefore());
        System.out.println(certificate2.getNotAfter());
        System.out.println(certificate1.equals(certificate2));
    }

}