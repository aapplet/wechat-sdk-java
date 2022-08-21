package io.github.aapplet.wechat.config;

import io.github.aapplet.wechat.util.WeChatUtil;
import lombok.Data;

import java.security.PrivateKey;

@Data
public class WeChatConfig {
    /**
     * 开放平台上创建的应用的ID
     */
    private String appId;
    /**
     * 开放平台上创建的应用的秘钥
     */
    private String appSecret;
    /**
     * 商户号
     */
    private String mchId;
    /**
     * 商户秘钥
     */
    private String mchKey;
    /**
     * 证书序列号
     */
    private String serialNo;
    /**
     * 证书私钥
     */
    private PrivateKey privateKey;
    /**
     * 微信支付回调地址
     */
    private String notifyUrl;
    /**
     * 认证类型
     */
    private String schema = "WECHATPAY2-SHA256-RSA2048";

    /**
     * 设置私钥
     */
    public void setPrivateKey(String privateKey) {
        this.privateKey = WeChatUtil.getPrivateKey(privateKey);
    }

    /**
     * 加载私钥
     */
    public void loadPrivateKey(String path) {
        this.privateKey = WeChatUtil.loadPrivateKey(path);
    }

    /**
     * 签名
     */
    public String signature(String content) {
        return WeChatUtil.signature(content, privateKey);
    }
}
