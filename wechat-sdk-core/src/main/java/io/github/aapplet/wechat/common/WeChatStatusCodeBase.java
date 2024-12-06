package io.github.aapplet.wechat.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatResponse;
import lombok.Data;

/**
 * 公众平台状态码
 */
@Data
public class WeChatStatusCodeBase implements WeChatResponse.MP {

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

}