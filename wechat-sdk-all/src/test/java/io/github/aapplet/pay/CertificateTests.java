package io.github.aapplet.pay;

import io.github.aapplet.wechat.cert.WeChatCertificateManager;
import io.github.aapplet.wechat.config.WeChatConfig;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class CertificateTests {

    private final WeChatConfig wechatConfig = WeChatConfig.load("config.json");

    @Test
    @SneakyThrows
    void load() {
        wechatConfig.loadPrivateKeyFromPath("privateKey.pem");
        WeChatCertificateManager certificateManager = wechatConfig.getCertificateManager();
        System.out.println(certificateManager.getCertificate());
    }

}