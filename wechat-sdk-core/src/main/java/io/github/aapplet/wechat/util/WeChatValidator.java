package io.github.aapplet.wechat.util;

import io.github.aapplet.wechat.cert.WeChatCertificateManager;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.exception.WeChatException;
import io.github.aapplet.wechat.http.WeChatHeaders;
import lombok.Getter;
import lombok.ToString;

import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * 签名验证
 * <p>
 * https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay4_1.shtml
 */
@Getter
@ToString
public class WeChatValidator {

    protected final WeChatConfig wechatConfig;
    protected final WeChatHeaders wechatHeaders;
    protected final String body;

    /**
     * @param wechatConfig 配置信息
     * @param httpResponse HTTP响应
     */
    public WeChatValidator(WeChatConfig wechatConfig, HttpResponse<byte[]> httpResponse) {
        this(wechatConfig, httpResponse.headers().map(), new String(httpResponse.body(), StandardCharsets.UTF_8));
    }

    /**
     * @param wechatConfig 配置信息
     * @param headers      请求头
     * @param body         请求内容
     */
    public WeChatValidator(WeChatConfig wechatConfig, Map<String, ?> headers, String body) {
        if (wechatConfig == null) {
            throw new WeChatException("wechatConfig为空,验签失败");
        }
        if (headers == null) {
            throw new WeChatException("headers为空,验签失败");
        }
        if (body == null) {
            throw new WeChatException("body为空,验签失败");
        }
        this.wechatConfig = wechatConfig;
        this.wechatHeaders = WeChatHeaders.fromObject(headers);
        this.body = body;
    }

    /**
     * 签名验证
     *
     * @param certificate 平台证书
     */
    public boolean verify(Certificate certificate) {
        final String content = wechatHeaders.getTimestamp() + "\n" + wechatHeaders.getNonce() + "\n" + body + "\n";
        return WeChatShaUtil.verify(certificate, content, wechatHeaders.getSignature());
    }

    /**
     * 签名验证失败
     */
    public boolean verify() {
        WeChatCertificateManager certificateManager = wechatConfig.getCertificateManager();
        X509Certificate certificate = certificateManager.getCertificate(wechatHeaders.getSerial());
        return verify(certificate);
    }

}