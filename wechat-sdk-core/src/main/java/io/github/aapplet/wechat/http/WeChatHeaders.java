package io.github.aapplet.wechat.http;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.exception.WeChatValidationException;
import io.github.aapplet.wechat.util.WeChatJacksonUtil;
import lombok.Data;

@Data
@SuppressWarnings("SpellCheckingInspection")
public class WeChatHeaders {

    /**
     * 请求ID
     */
    @JsonProperty("request-id")
    private String requestId;
    /**
     * 序列号
     */
    @JsonProperty("wechatpay-serial")
    private String serial;
    /**
     * 随机串
     */
    @JsonProperty("wechatpay-nonce")
    private String nonce;
    /**
     * 时间戳
     */
    @JsonProperty("wechatpay-timestamp")
    private String timestamp;
    /**
     * 签名值
     */
    @JsonProperty("wechatpay-signature")
    private String signature;
    /**
     * 签名类型
     */
    @JsonProperty("wechatpay-signature-type")
    private String signatureType;

    /**
     * Headers Check
     *
     * @return WeChat Headers
     */
    private WeChatHeaders check() {
        if (requestId == null) {
            throw new WeChatValidationException("request-id为空, 签名验证失败");
        }
        if (serial == null) {
            throw new WeChatValidationException("wechatpay-serial为空, 签名验证失败");
        }
        if (nonce == null) {
            throw new WeChatValidationException("wechatpay-nonce为空, 签名验证失败");
        }
        if (timestamp == null) {
            throw new WeChatValidationException("wechatpay-timestamp为空, 签名验证失败");
        }
        if (signature == null) {
            throw new WeChatValidationException("wechatpay-signature为空, 签名验证失败");
        }
        return this;
    }

    /**
     * @param value HTTP Headers
     * @return WeChat Headers
     */
    public static WeChatHeaders fromObject(Object value) {
        return WeChatJacksonUtil.fromObject(value, WeChatHeaders.class).check();
    }

}