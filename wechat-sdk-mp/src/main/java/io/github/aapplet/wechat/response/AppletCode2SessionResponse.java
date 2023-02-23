package io.github.aapplet.wechat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.common.WeChatStatusCodeBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AppletCode2SessionResponse extends WeChatStatusCodeBase {

    /**
     * 用户唯一标识
     */
    @JsonProperty("openid")
    private String openId;
    /**
     * 用户在公众平台的唯一标识符,若当前小程序已绑定到微信公众平台帐号下会返回
     */
    @JsonProperty("unionid")
    private String unionId;
    /**
     * 会话密钥
     */
    @JsonProperty("session_key")
    private String sessionKey;

}