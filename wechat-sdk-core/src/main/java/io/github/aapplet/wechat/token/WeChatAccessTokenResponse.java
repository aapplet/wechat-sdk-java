package io.github.aapplet.wechat.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.common.WeChatStatusCodeBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WeChatAccessTokenResponse extends WeChatStatusCodeBase {

    /**
     * 凭证
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * 凭证有效时间,单位：秒
     */
    @JsonProperty("expires_in")
    private Integer expiresIn;

}