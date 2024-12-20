package io.github.aapplet.wechat.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.util.WeChatJacksonUtil;
import lombok.Data;

/**
 * <a href="https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-access-token/getStableAccessToken.html">获取稳定版接口调用凭据</a>
 */
@Data
public class WeChatAccessTokenResponse implements WeChatResponse.MP {

    /**
     * 获取到的凭证
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * 凭证有效时间，单位：秒。目前是7200秒之内的值。
     */
    @JsonProperty("expires_in")
    private Integer expiresIn;

    /**
     * @param bytes AccessToken响应数据
     * @return AccessToken响应实例
     */
    public static WeChatAccessTokenResponse fromJson(byte[] bytes) {
        return WeChatJacksonUtil.fromJson(bytes, WeChatAccessTokenResponse.class);
    }

}