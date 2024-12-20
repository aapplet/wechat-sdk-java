package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.response.AppletCode2SessionResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html">小程序登录</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppletCode2SessionRequest implements WeChatRequest.MP<AppletCode2SessionResponse> {

    /**
     * @param jsCode 登录时获取的 code，可通过wx.login获取
     */
    public AppletCode2SessionRequest(String jsCode) {
        this.jsCode = jsCode;
    }

    /**
     * AppId
     */
    @JsonProperty("appid")
    private String appId;
    /**
     * AppSecret
     */
    @JsonProperty("secret")
    private String secret;
    /**
     * 登录时获取的 code，可通过wx.login获取
     */
    @JsonProperty("js_code")
    private String jsCode;
    /**
     * 授权类型，此处只需填写 authorization_code
     */
    @JsonProperty("grant_type")
    private String grantType;

    @Override
    public WeChatAttribute<AppletCode2SessionResponse> getAttribute(WeChatConfig wechatConfig) {
        if (appId == null) {
            appId = wechatConfig.getAppId();
        }
        if (secret == null) {
            secret = wechatConfig.getAppSecret();
        }
        if (grantType == null) {
            grantType = "authorization_code";
        }
        var attribute = new WeChatAttributeImpl<AppletCode2SessionResponse>(wechatConfig.getMpDomain());
        attribute.setMethod("GET");
        attribute.setRequestPath("/sns/jscode2session");
        attribute.setParameters("appid=" + appId + "&secret=" + secret + "&js_code=" + jsCode + "&grant_type=" + grantType);
        attribute.setResponseClass(AppletCode2SessionResponse.class);
        return attribute;
    }

}