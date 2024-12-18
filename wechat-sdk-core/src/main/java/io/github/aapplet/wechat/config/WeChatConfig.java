package io.github.aapplet.wechat.config;

import io.github.aapplet.wechat.base.WeChatDomain;
import io.github.aapplet.wechat.cert.WeChatCertificateManager;
import io.github.aapplet.wechat.cert.WeChatCertificateService;
import io.github.aapplet.wechat.host.WeChatDomainStorage;
import io.github.aapplet.wechat.http.WeChatHttpClient;
import io.github.aapplet.wechat.token.WeChatAccessTokenManager;
import io.github.aapplet.wechat.token.WeChatAccessTokenService;
import io.github.aapplet.wechat.util.WeChatCertificateUtil;
import io.github.aapplet.wechat.util.WeChatCryptoUtil;
import lombok.Data;

import java.net.http.HttpClient;
import java.security.PrivateKey;
import java.time.Duration;

/**
 * 配置信息
 */
@Data
@SuppressWarnings("SpellCheckingInspection")
public class WeChatConfig {

    /**
     * AccessToken管理器
     */
    private final WeChatAccessTokenManager accessTokenManager = new WeChatAccessTokenService(this);
    /**
     * 平台证书管理器
     */
    private final WeChatCertificateManager certificateManager = new WeChatCertificateService(this);
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
    private String merchantId;
    /**
     * 商户秘钥-APIv3
     */
    private String apiV3Key;
    /**
     * 服务ID
     */
    private String serviceId;
    /**
     * 证书序列号
     */
    private String serialNumber;
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
     * 平台证书颁发者
     */
    private String issuer = "Tenpay.com Root CA";
    /**
     * 认证类型
     */
    private String schema = "WECHATPAY2-SHA256-RSA2048";
    /**
     * HTTP响应超时时间,默认10秒
     */
    private Duration httpResponseTimeout = Duration.ofSeconds(10);
    /**
     * 健康检查连接超时时间, 默认10秒
     */
    private Duration healthCheckConnectTimeout = Duration.ofSeconds(10);
    /**
     * 健康检查响应超时时间, 默认10秒
     */
    private Duration healthCheckResponseTimeout = Duration.ofSeconds(10);
    /**
     * 健康检查时间间隔, 默认10秒
     */
    private Duration healthCheckDetectInterval = Duration.ofSeconds(10);
    /**
     * 公众平台域名
     */
    private WeChatDomain mpDomain = WeChatDomainStorage.MP;
    /**
     * 商户平台域名
     */
    private WeChatDomain payDomain = WeChatDomainStorage.PAY;
    /**
     * Http客户端
     * <br>
     * Http连接超时时间默认10秒, 如需修改请重新设置HttpClient
     */
    private HttpClient httpClient = WeChatHttpClient.getInstance();

    /**
     * 加载商户证书私钥
     *
     * @param filePath 私钥文件路径
     */
    public void privateKeyFromPath(String filePath) {
        this.privateKey = WeChatCertificateUtil.getPrivateKey(filePath);
    }

    /**
     * 使用商户证书私钥生成签名
     *
     * @param content 签名内容
     * @return 签名值
     */
    public String signature(String content) {
        return WeChatCryptoUtil.signature(privateKey, content);
    }

    /**
     * 使用平台证书验签名
     *
     * @param serialNumber 证书序列号
     * @param content      签名内容
     * @param ciphertext   签名值
     * @return 验签结果
     */
    public boolean verify(String serialNumber, String content, String ciphertext) {
        return WeChatCryptoUtil.verify(certificateManager.getCertificate(serialNumber), content, ciphertext);
    }

    /**
     * 使用平台证书加密敏感字段
     *
     * @param message 需要加密的消息
     * @return 加密后数据
     */
    public String encrypt(String message) {
        return WeChatCryptoUtil.encrypt(certificateManager.getCertificate(), message);
    }

    /**
     * 使用商户证书解密敏感字段
     *
     * @param ciphertext 加密后的密文
     * @return 解密后数据
     */
    public byte[] decrypt(String ciphertext) {
        return WeChatCryptoUtil.decrypt(privateKey, ciphertext);
    }

    /**
     * 解密回调报文或平台证书
     *
     * @param nonceStr       加密使用的随机串
     * @param associatedData 加密使用的附加数据
     * @param ciphertext     加密后的密文
     * @return 解密后数据
     */
    public byte[] decrypt(String nonceStr, String associatedData, String ciphertext) {
        return WeChatCryptoUtil.decrypt(apiV3Key, nonceStr, associatedData, ciphertext);
    }

}