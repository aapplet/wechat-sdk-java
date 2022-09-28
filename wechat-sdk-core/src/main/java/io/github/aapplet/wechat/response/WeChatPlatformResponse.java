package io.github.aapplet.wechat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.util.WeChatJsonUtil;
import lombok.Data;

/**
 * 公众平台状态码
 */
@Data
public class WeChatPlatformResponse implements WeChatResponse.MP {

    /**
     * 失败时返回错误码
     */
    @JsonProperty("errcode")
    private Integer errCode;
    /**
     * 失败时返回错误信息
     */
    @JsonProperty("errmsg")
    private String errMsg;

    /**
     * Json转对象
     */
    public static WeChatPlatformResponse fromJson(byte[] bytes) {
        return WeChatJsonUtil.fromJson(bytes, WeChatPlatformResponse.class);
    }

}