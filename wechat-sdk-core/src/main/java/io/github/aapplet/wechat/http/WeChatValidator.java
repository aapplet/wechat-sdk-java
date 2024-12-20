package io.github.aapplet.wechat.http;

import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.util.WeChatCryptoUtil;
import lombok.Data;

import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * <a href="https://pay.weixin.qq.com/doc/v3/merchant/4013053420">如何使用平台证书验签名</a>
 */
@Data
public class WeChatValidator {

    /**
     * 配置信息
     */
    protected final WeChatConfig wechatConfig;
    /**
     * 响应头
     */
    protected final WeChatHeaders wechatHeaders;
    /**
     * 响应数据
     */
    protected final String responseBody;

    /**
     * @param wechatConfig 配置信息
     * @param httpResponse HTTP响应
     */
    public WeChatValidator(WeChatConfig wechatConfig, HttpResponse<byte[]> httpResponse) {
        this(wechatConfig, httpResponse.headers().map(), new String(httpResponse.body(), StandardCharsets.UTF_8));
    }

    /**
     * @param wechatConfig 配置信息
     * @param headers      响应头
     * @param responseBody 响应数据
     */
    public WeChatValidator(WeChatConfig wechatConfig, Map<String, ?> headers, String responseBody) {
        this.wechatHeaders = WeChatHeaders.fromObject(headers);
        this.wechatConfig = wechatConfig;
        this.responseBody = responseBody;
    }

    /**
     * 获取证书序列号
     *
     * @return 证书序列号
     */
    public String getSerialNumber() {
        return wechatHeaders.getSerial();
    }

    /**
     * <div>应答时间戳\n</div>
     * <div>应答随机串\n</div>
     * <div>应答报文主体\n</div>
     *
     * @return 验签内容
     */
    private String signatureContent() {
        return wechatHeaders.getTimestamp() + "\n" + wechatHeaders.getNonce() + "\n" + responseBody + "\n";
    }

    /**
     * 使用平台证书验签名
     *
     * @return 签名验证结果
     */
    public boolean verify() {
        return verify(wechatConfig.getCertificateManager().getCertificate(wechatHeaders.getSerial()));
    }

    /**
     * 使用平台证书验签名
     *
     * @param certificate 平台证书公钥
     * @return 验签结果
     */
    public boolean verify(X509Certificate certificate) {
        return WeChatCryptoUtil.verify(certificate, signatureContent(), wechatHeaders.getSignature());
    }

}