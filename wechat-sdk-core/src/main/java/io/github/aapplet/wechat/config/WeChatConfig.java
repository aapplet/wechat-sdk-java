package io.github.aapplet.wechat.config;

import io.github.aapplet.wechat.cert.WeChatCertificateManager;
import io.github.aapplet.wechat.cert.WeChatCertificateService;
import io.github.aapplet.wechat.token.WeChatAccessTokenManager;
import io.github.aapplet.wechat.token.WeChatAccessTokenService;
import io.github.aapplet.wechat.util.WeChatAesUtil;
import io.github.aapplet.wechat.util.WeChatPemUtil;
import io.github.aapplet.wechat.util.WeChatShaUtil;
import lombok.Data;

import java.security.PrivateKey;

/**
 * 配置信息
 */
@Data
public class WeChatConfig {
    /**
     * 是否开启debug
     */
    private boolean debug;
    /**
     * 公众平台应用ID
     */
    private String appId;
    /**
     * 公众平台应用秘钥
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
     * 服务ID
     */
    private String serviceId;
    /**
     * 支付回调地址
     */
    private String payNotifyUrl;
    /**
     * 退款回调地址
     */
    private String refundNotifyUrl;
    /**
     * 支付分回调地址
     */
    private String payScoreNotifyUrl;
    /**
     * 证书私钥
     */
    private PrivateKey privateKey;
    /**
     * http连接超时时间,默认5秒
     */
    private int httpConnectTimeout = 1000 * 5;
    /**
     * HTTP响应超时时间,默认10秒
     */
    private int httpResponseTimeout = 1000 * 10;
    /**
     * 认证类型
     */
    private String schema = "WECHATPAY2-SHA256-RSA2048";
    /**
     * 平台证书管理器
     */
    private WeChatCertificateManager certificateManager = new WeChatCertificateService(this);
    /**
     * AccessToken管理器
     */
    private WeChatAccessTokenManager accessTokenManager = new WeChatAccessTokenService(this);

    /**
     * 加载私钥
     */
    public void loadPrivateKey(String path) {
        this.privateKey = WeChatPemUtil.loadPrivateKey(path);
    }

    /**
     * 签名
     */
    public String signature(String content) {
        return WeChatShaUtil.signature(privateKey, content);
    }

    /**
     * 证书和回调报文解密
     */
    public byte[] decrypt(String nonceStr, String associatedData, String ciphertext) {
        return WeChatAesUtil.decrypt(mchKey, nonceStr, associatedData, ciphertext);
    }

}