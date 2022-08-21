package io.github.aapplet.wechat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatResponse;
import lombok.Data;

@Data
public class AppletAccessTokenResponse implements WeChatResponse {

    /**
     * 凭证
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * 凭证有效时间，单位：秒。
     */
    @JsonProperty("expires_in")
    private Integer expiresIn;

}