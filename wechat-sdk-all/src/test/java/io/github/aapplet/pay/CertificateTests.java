package io.github.aapplet.pay;

import io.github.aapplet.wechat.cert.WeChatCertificateManager;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.util.WeChatCertUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class CertificateTests {

    private final WeChatConfig wechatConfig = WeChatConfig.load("config.json");

    @Test
    @SneakyThrows
    void load() {
        wechatConfig.loadPrivateKeyFromPemFile("privateKey.pem");
        WeChatCertificateManager certificateManager = wechatConfig.getCertificateManager();
        WeChatCertUtil.saveCertificateToPemFile(certificateManager.getCertificate(), "cert/certificate.pem");
    }

}