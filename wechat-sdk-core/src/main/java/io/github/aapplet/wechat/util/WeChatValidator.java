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

    protected final WeChatConfig weChatConfig;
    protected final WeChatHeaders weChatHeaders;
    protected final String body;

    /**
     * @param weChatConfig 配置信息
     * @param httpResponse HTTP响应
     */
    public WeChatValidator(WeChatConfig weChatConfig, HttpResponse<byte[]> httpResponse) {
        this(weChatConfig, httpResponse.headers().map(), new String(httpResponse.body(), StandardCharsets.UTF_8));
    }

    /**
     * @param weChatConfig 配置信息
     * @param headers      请求头
     * @param body         请求内容
     */
    public WeChatValidator(WeChatConfig weChatConfig, Map<String, ?> headers, String body) {
        if (weChatConfig == null) {
            throw new WeChatException("weChatConfig为空,验签失败");
        }
        if (headers == null) {
            throw new WeChatException("headers为空,验签失败");
        }
        if (body == null) {
            throw new WeChatException("body为空,验签失败");
        }
        this.weChatConfig = weChatConfig;
        this.weChatHeaders = WeChatHeaders.fromObject(headers);
        this.body = body;
    }

    /**
     * 签名验证
     *
     * @param certificate 平台证书
     */
    public boolean verify(Certificate certificate) {
        final String content = weChatHeaders.getTimestamp() + "\n" + weChatHeaders.getNonce() + "\n" + body + "\n";
        return WeChatShaUtil.verify(certificate, content, weChatHeaders.getSignature());
    }

    /**
     * 签名验证失败
     */
    public boolean verify() {
        WeChatCertificateManager certificateManager = weChatConfig.getCertificateManager();
        X509Certificate certificate = certificateManager.getCertificate(weChatHeaders.getSerial());
        return verify(certificate);
    }

}