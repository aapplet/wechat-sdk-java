package io.github.aapplet.wechat.http;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.exception.WeChatValidationException;
import io.github.aapplet.wechat.util.WeChatJsonUtil;
import lombok.Data;

@Data
@SuppressWarnings("SpellCheckingInspection")
public class WeChatHeaders {

    /**
     * 应答请求ID
     */
    @JsonProperty("request-id")
    private String requestId;
    /**
     * 应答证书序列号
     */
    @JsonProperty("wechatpay-serial")
    private String serial;
    /**
     * 应答随机串
     */
    @JsonProperty("wechatpay-nonce")
    private String nonce;
    /**
     * 应答时间戳
     */
    @JsonProperty("wechatpay-timestamp")
    private String timestamp;
    /**
     * 应答签名串
     */
    @JsonProperty("wechatpay-signature")
    private String signature;
    /**
     * 应答签名类型
     */
    @JsonProperty("wechatpay-signature-type")
    private String signatureType;

    /**
     * 参数校验
     */
    private WeChatHeaders validate() {
        if (serial == null || serial.isEmpty()) {
            throw new WeChatValidationException("wechatpay-serial为空,验签失败");
        }
        if (nonce == null || nonce.isEmpty()) {
            throw new WeChatValidationException("wechatpay-nonce为空,验签失败");
        }
        if (timestamp == null || timestamp.isEmpty()) {
            throw new WeChatValidationException("wechatpay-timestamp为空,验签失败");
        }
        if (signature == null || signature.isEmpty()) {
            throw new WeChatValidationException("wechatpay-signature为空,验签失败");
        }
        return this;
    }

    /**
     * 对象转换
     */
    public static WeChatHeaders fromObject(Object value) {
        return WeChatJsonUtil.fromObject(value, WeChatHeaders.class).validate();
    }

}