package io.github.aapplet.wechat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatResponse;
import lombok.Data;

@Data
public class AppletCode2SessionResponse implements WeChatResponse.MP {

    /**
     * 用户唯一标识
     */
    @JsonProperty("openid")
    private String openId;
    /**
     * 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台账号下会返回
     */
    @JsonProperty("unionid")
    private String unionId;
    /**
     * 会话密钥
     */
    @JsonProperty("session_key")
    private String sessionKey;

}