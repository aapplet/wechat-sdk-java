package io.github.aapplet.wechat.config;

import com.fasterxml.jackson.annotation.JsonProperty;
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
import io.github.aapplet.wechat.util.WeChatJacksonUtil;
import io.github.aapplet.wechat.util.WeChatResourceUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpClient;
import java.security.PrivateKey;

/**
 * 配置信息
 */
@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("SpellCheckingInspection")
public class WeChatConfig {

    /**
     * AccessToken管理器
     */
    @Delegate
    private final WeChatAccessTokenManager accessTokenManager = new WeChatAccessTokenService(this);

    /**
     * 平台证书管理器
     */
    @Delegate
    private final WeChatCertificateManager certificateManager = new WeChatCertificateService(this);

    /**
     * 是否开启调试模式
     */
    @JsonProperty("debug")
    private boolean debug;

    /**
     * 微信公众平台应用ID
     */
    @JsonProperty("appId")
    private String appId;

    /**
     * 微信公众平台应用密钥
     */
    @JsonProperty("appSecret")
    private String appSecret;

    /**
     * 商户号
     */
    @JsonProperty("merchantId")
    private String merchantId;

    /**
     * 商户密钥（APIv3）
     */
    @JsonProperty("apiV3Key")
    private String apiV3Key;

    /**
     * 服务ID
     */
    @JsonProperty("serviceId")
    private String serviceId;

    /**
     * 商户私钥ID
     */
    @JsonProperty("privateKeyId")
    private String privateKeyId;

    /**
     * 商户私钥路径
     */
    @JsonProperty("privateKeyPath")
    private String privateKeyPath;

    /**
     * 微信支付公钥ID
     */
    @JsonProperty("publicKeyId")
    private String publicKeyId;

    /**
     * 微信支付公钥路径
     */
    @JsonProperty("publicKeyPath")
    private String publicKeyPath;

    /**
     * 支付回调地址
     */
    @JsonProperty("payNotifyUrl")
    private String payNotifyUrl;

    /**
     * 退款回调地址
     */
    @JsonProperty("refundNotifyUrl")
    private String refundNotifyUrl;

    /**
     * 支付分回调地址
     */
    @JsonProperty("payScoreNotifyUrl")
    private String payScoreNotifyUrl;

    /**
     * 配置文件路径
     */
    @JsonProperty("configFilePath")
    private String configFilePath;

    /**
     * 证书私钥
     */
    private PrivateKey privateKey;

    /**
     * 证书颁发者
     */
    @Builder.Default
    @JsonProperty("issuer")
    private String issuer = "Tenpay.com Root CA";

    /**
     * 认证类型
     */
    @Builder.Default
    @JsonProperty("schema")
    private String schema = "WECHATPAY2-SHA256-RSA2048";

    /**
     * HTTP响应超时时间，默认10秒
     */
    @Builder.Default
    @JsonProperty("httpResponseTimeout")
    private long httpResponseTimeout = 10 * 1000;

    /**
     * 健康检查时间间隔，默认10秒
     */
    @Builder.Default
    @JsonProperty("healthCheckDetectInterval")
    private long healthCheckDetectInterval = 10 * 1000;

    /**
     * 健康检查连接超时时间，默认10秒
     */
    @Builder.Default
    @JsonProperty("healthCheckConnectTimeout")
    private long healthCheckConnectTimeout = 10 * 1000;

    /**
     * 健康检查响应超时时间，默认10秒
     */
    @Builder.Default
    @JsonProperty("healthCheckResponseTimeout")
    private long healthCheckResponseTimeout = 10 * 1000;

    /**
     * 微信公众平台域名
     */
    @Builder.Default
    private WeChatDomain mpDomain = WeChatDomainStorage.MP;

    /**
     * 微信商户平台域名
     */
    @Builder.Default
    private WeChatDomain payDomain = WeChatDomainStorage.PAY;

    /**
     * HTTP客户端
     * <p>
     * HTTP连接超时时间默认10秒，如需修改请重新设置HttpClient
     */
    @Builder.Default
    private HttpClient httpClient = WeChatHttpClient.getInstance();

    /**
     * 从指定文件路径读取私钥并解析为 {@link PrivateKey} 对象。
     *
     * @param filePath 私钥文件路径
     */
    public void loadPrivateKeyFromPath(String filePath) {
        this.privateKey = WeChatCertUtil.loadPrivateKeyFromPath(filePath);
    }

    /**
     * 从指定的文件路径加载 X.509 证书并保存到证书管理器
     *
     * @param serialNumber 证书序列号
     * @param filePath     证书文件路径，文件内容应为 PEM 格式。
     */
    public void loadPublicKeyFromPath(String serialNumber, String filePath) {
        certificateManager.setCertificate(serialNumber, WeChatCertUtil.loadPublicKeyFromPath(filePath));
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
        return WeChatCryptoUtil.verify(certificateManager.getCertificate(serialNumber), content, ciphertext);
    }

    /**
     * 使用平台证书公钥加密敏感字段
     *
     * @param message 需要加密的明文消息
     * @return 加密后的密文，Base64编码
     * @throws WeChatException 如果加密过程中出现错误，如算法不支持、证书无效等
     */
    public String encrypt(String message) {
        return WeChatCryptoUtil.encrypt(certificateManager.getCertificate(), message);
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
     * @param nonce          加密使用的随机串
     * @param associatedData 加密使用的附加数据
     * @param ciphertext     加密后的密文，Base64编码
     * @return 解密后的明文数据
     * @throws WeChatException 如果解密过程中出现错误，如算法不支持、密钥无效等
     */
    public byte[] decrypt(String nonce, String associatedData, String ciphertext) {
        return WeChatCryptoUtil.decrypt(apiV3Key, nonce, associatedData, ciphertext);
    }

    /**
     * 重新加载配置
     */
    public void reload() {
        try {
            WeChatConfig wechatConfig = WeChatConfig.load(configFilePath);
            this.debug = wechatConfig.isDebug();
            this.appId = wechatConfig.getAppId();
            this.appSecret = wechatConfig.getAppSecret();
            this.merchantId = wechatConfig.getMerchantId();
            this.apiV3Key = wechatConfig.getApiV3Key();
            this.serviceId = wechatConfig.getServiceId();
            this.privateKeyId = wechatConfig.getPrivateKeyId();
            this.publicKeyId = wechatConfig.getPublicKeyId();
            this.payNotifyUrl = wechatConfig.getPayNotifyUrl();
            this.refundNotifyUrl = wechatConfig.getRefundNotifyUrl();
            this.payScoreNotifyUrl = wechatConfig.getPayScoreNotifyUrl();
            this.configFilePath = wechatConfig.getConfigFilePath();
            this.privateKey = wechatConfig.getPrivateKey();
            this.issuer = wechatConfig.getIssuer();
            this.schema = wechatConfig.getSchema();
            this.httpResponseTimeout = wechatConfig.getHttpResponseTimeout();
            this.healthCheckDetectInterval = wechatConfig.getHealthCheckDetectInterval();
            this.healthCheckConnectTimeout = wechatConfig.getHealthCheckConnectTimeout();
            this.healthCheckResponseTimeout = wechatConfig.getHealthCheckResponseTimeout();
            log.debug("配置信息重新加载成功 => {}", WeChatJacksonUtil.toPrettyJson(wechatConfig));
        } catch (Exception e) {
            log.error("配置信息重新加载失败 => {}", e.getMessage());
        }
    }

    /**
     * 加载配置文件
     *
     * @param filePath 配置文件路径
     * @return 配置信息
     */
    public static WeChatConfig load(String filePath) {
        var configBytes = WeChatResourceUtil.readAllBytes(filePath);
        var wechatConfig = WeChatJacksonUtil.fromJson(configBytes, WeChatConfig.class);
        // 加载商户私钥
        var privateKeyFilePath = wechatConfig.getPrivateKeyPath();
        if (!(privateKeyFilePath == null || privateKeyFilePath.isBlank())) {
            wechatConfig.loadPrivateKeyFromPath(privateKeyFilePath);
            log.debug("加载商户私钥成功");
        }
        // 加载微信公钥
        var publicKeyFilePath = wechatConfig.getPublicKeyPath();
        if (!(publicKeyFilePath == null || publicKeyFilePath.isBlank())) {
            wechatConfig.loadPublicKeyFromPath(wechatConfig.getPublicKeyId(), publicKeyFilePath);
            log.debug("加载微信公钥成功");
        }
        return wechatConfig;
    }

}