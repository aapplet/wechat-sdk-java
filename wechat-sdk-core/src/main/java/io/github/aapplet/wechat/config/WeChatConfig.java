package io.github.aapplet.wechat.config;

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
     * 商户秘钥,APIv3密钥
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
     * 认证类型
     */
    private String schema = "WECHATPAY2-SHA256-RSA2048";
    /**
     * http连接超时时间,默认5秒
     */
    private int httpConnectTimeout = 1000 * 5;
    /**
     * HTTP响应超时时间,默认10秒
     */
    private int httpResponseTimeout = 1000 * 10;
    /**
     * 容灾检测连接超时时间,默认1秒
     */
    private int disasterConnectTimeout = 1000;
    /**
     * 容灾检测响应超时时间,默认1秒
     */
    private int disasterResponseTimeout = 1000;
    /**
     * 容灾检测时间间隔,默认15秒
     */
    private int disasterDetectInterval = 1000 * 15;

    /**
     * 设置私钥
     */
    public void setPrivateKey(String privateKey) {
        this.privateKey = WeChatPemUtil.getPrivateKey(privateKey);
    }

    /**
     * 设置商户证书
     */
    public void loadPKCS12(String path) {
        this.privateKey = WeChatPemUtil.loadPKCS12(mchId, path);
    }

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