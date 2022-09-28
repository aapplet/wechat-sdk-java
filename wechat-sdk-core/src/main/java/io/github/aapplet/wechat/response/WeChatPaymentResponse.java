package io.github.aapplet.wechat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.util.WeChatJsonUtil;
import lombok.Data;

/**
 * 微信支付状态码
 */
@Data
public class WeChatPaymentResponse implements WeChatResponse.V3 {

    /**
     * 失败时返回错误码
     */
    @JsonProperty("code")
    private String code;
    /**
     * 失败时返回错误信息
     */
    @JsonProperty("message")
    private String message;

    /**
     * Json转对象
     */
    public static WeChatPaymentResponse fromJson(byte[] bytes) {
        return WeChatJsonUtil.fromJson(bytes, WeChatPaymentResponse.class);
    }

}