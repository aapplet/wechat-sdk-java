package io.github.aapplet.wechat.config;

import io.github.aapplet.wechat.base.WeChatDomain;
import io.github.aapplet.wechat.cert.WeChatCertificateManager;
import io.github.aapplet.wechat.cert.WeChatCertificateService;
import io.github.aapplet.wechat.exception.WeChatException;
import io.github.aapplet.wechat.host.WeChatDomainStorage;
import io.github.aapplet.wechat.http.WeChatHttpClient;
import io.github.aapplet.wechat.token.WeChatAccessTokenManager;
import io.github.aapplet.wechat.token.WeChatAccessTokenService;
import io.github.aapplet.wechat.util.WeChatCertUtil;
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
     * 是否开启调试模式
     */
    private boolean debug;

    /**
     * 微信公众平台应用ID
     */
    private String appId;

    /**
     * 微信公众平台应用密钥
     */
    private String appSecret;

    /**
     * 商户号
     */
    private String merchantId;

    /**
     * 商户密钥（APIv3）
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
     * 证书颁发者
     */
    private String issuer = "Tenpay.com Root CA";

    /**
     * 认证类型
     */
    private String schema = "WECHATPAY2-SHA256-RSA2048";

    /**
     * HTTP响应超时时间，默认10秒
     */
    private Duration httpResponseTimeout = Duration.ofSeconds(10);

    /**
     * 健康检查连接超时时间，默认10秒
     */
    private Duration healthCheckConnectTimeout = Duration.ofSeconds(10);

    /**
     * 健康检查响应超时时间，默认10秒
     */
    private Duration healthCheckResponseTimeout = Duration.ofSeconds(10);

    /**
     * 健康检查时间间隔，默认10秒
     */
    private Duration healthCheckDetectInterval = Duration.ofSeconds(10);

    /**
     * 微信公众平台域名
     */
    private WeChatDomain mpDomain = WeChatDomainStorage.MP;

    /**
     * 微信商户平台域名
     */
    private WeChatDomain payDomain = WeChatDomainStorage.PAY;

    /**
     * HTTP客户端
     * <p>
     * HTTP连接超时时间默认10秒，如需修改请重新设置HttpClient
     */
    private HttpClient httpClient = WeChatHttpClient.getInstance();

    /**
     * AccessToken管理器
     */
    private WeChatAccessTokenManager accessTokenManager;

    /**
     * 证书管理器
     */
    private WeChatCertificateManager certificateManager;

    /**
     * 获取AccessToken管理器
     *
     * @return AccessToken管理器
     */
    public WeChatAccessTokenManager getAccessTokenManager() {
        if (accessTokenManager == null) {
            return new WeChatAccessTokenService(this);
        }
        return accessTokenManager;
    }

    /**
     * 获取证书管理器
     *
     * @return 证书管理器
     */
    public WeChatCertificateManager getCertificateManager() {
        if (certificateManager == null) {
            return new WeChatCertificateService(this);
        }
        return certificateManager;
    }

    /**
     * 从指定文件路径读取私钥并解析为 {@link PrivateKey} 对象。
     *
     * @param filePath 私钥文件路径
     */
    public void loadPrivateKeyFromPemFile(String filePath) {
        this.privateKey = WeChatCertUtil.loadPrivateKeyFromPemFile(filePath);
    }

    /**
     * 从指定的文件路径加载 X.509 证书并保存到证书管理器
     *
     * @param filePath     证书文件路径，文件内容应为 PEM 格式。
     * @param serialNumber 证书序列号
     */
    public void loadCertificateFromPemFile(String filePath, String serialNumber) {
        getCertificateManager().setCertificate(serialNumber, WeChatCertUtil.loadCertificateFromPemFile(filePath));
    }

    /**
     * 使用商户证书私钥生成签名
     *
     * @param content 需要签名的内容
     * @return 签名值，Base64编码
     * @throws WeChatException 如果签名过程中出现错误，如算法不支持、私钥无效等
     */
    public String signature(String content) {
        return WeChatCryptoUtil.signature(privateKey, content);
    }

    /**
     * 使用平台证书公钥验证签名
     *
     * @param serialNumber 证书序列号
     * @param content      需要验签的内容
     * @param ciphertext   签名值，Base64编码
     * @return 验签结果，true表示验证通过，false表示验证失败
     * @throws WeChatException 如果验签过程中出现错误，如算法不支持、公钥无效等
     */
    public boolean verify(String serialNumber, String content, String ciphertext) {
        return WeChatCryptoUtil.verify(getCertificateManager().getCertificate(serialNumber), content, ciphertext);
    }

    /**
     * 使用平台证书公钥加密敏感字段
     *
     * @param message 需要加密的明文消息
     * @return 加密后的密文，Base64编码
     * @throws WeChatException 如果加密过程中出现错误，如算法不支持、证书无效等
     */
    public String encrypt(String message) {
        return WeChatCryptoUtil.encrypt(getCertificateManager().getCertificate(), message);
    }

    /**
     * 使用商户证书私钥解密敏感字段
     *
     * @param ciphertext 加密后的密文，Base64编码
     * @return 解密后的明文数据
     * @throws WeChatException 如果解密过程中出现错误，如算法不支持、私钥无效等
     */
    public byte[] decrypt(String ciphertext) {
        return WeChatCryptoUtil.decrypt(privateKey, ciphertext);
    }

    /**
     * 使用APIv3密钥解密回调报文和平台证书
     *
     * @param nonceStr       加密使用的随机串
     * @param associatedData 加密使用的附加数据
     * @param ciphertext     加密后的密文，Base64编码
     * @return 解密后的明文数据
     * @throws WeChatException 如果解密过程中出现错误，如算法不支持、密钥无效等
     */
    public byte[] decrypt(String nonceStr, String associatedData, String ciphertext) {
        return WeChatCryptoUtil.decrypt(apiV3Key, nonceStr, associatedData, ciphertext);
    }

}