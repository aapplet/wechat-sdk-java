package io.github.aapplet.config;

import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.util.WeChatJacksonUtil;
import org.junit.jupiter.api.Test;

public class ConfigTests {

    @Test
    void toJson() {
        WeChatConfig wechatConfig = new WeChatConfig();
        wechatConfig.setDebug(true);
        wechatConfig.setAppId(".................应用ID...........");
        wechatConfig.setAppSecret(".............应用密钥..........");
        wechatConfig.setMerchantId("............商户号............");
        wechatConfig.setApiV3Key("..............商户密钥..........");
        wechatConfig.setServiceId(".............服务ID...........");
        wechatConfig.setPrivateKeyId("..........商户私钥ID........");
        wechatConfig.setPrivateKeyPath("........商户私钥路径.......");
        wechatConfig.setPublicKeyId("...........微信支付公钥ID.....");
        wechatConfig.setPublicKeyPath(".........微信支付公钥路径....");
        wechatConfig.setPayNotifyUrl("..........支付回调地址.......");
        wechatConfig.setRefundNotifyUrl(".......退款回调地址.......");
        wechatConfig.setPayScoreNotifyUrl(".....支付分回调地址.....");
        wechatConfig.setConfigFilePath("........配置文件路径.......");
        System.out.println(WeChatJacksonUtil.toJson(wechatConfig));
    }

    @Test
    void load() {
        WeChatConfig wechatConfig = WeChatConfig.load("config.json");
        wechatConfig.reload();
        System.out.println(WeChatJacksonUtil.toJson(wechatConfig));
    }

}