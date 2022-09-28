package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.attribute.AbstractAttribute;
import io.github.aapplet.wechat.attribute.WeChatPlatformAttribute;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.response.AppletCode2SessionResponse;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 小程序登录
 * <p>
 * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html
 */
@Data
@Accessors(chain = true)
public class AppletCode2SessionRequest implements WeChatRequest.MP<AppletCode2SessionResponse> {

    /**
     * 小程序 appId
     */
    @JsonProperty("appid")
    private String appId;
    /**
     * 小程序 appSecret
     */
    @JsonProperty("secret")
    private String secret;
    /**
     * 登录时获取的 code,可通过wx.login获取
     */
    @JsonProperty("js_code")
    private String jsCode;
    /**
     * 授权类型,此处只需填写 authorization_code
     */
    @JsonProperty("grant_type")
    private String grantType;

    @Override
    public WeChatAttribute<AppletCode2SessionResponse> getAttribute(WeChatConfig weChatConfig) {
        if (appId == null) {
            appId = weChatConfig.getAppId();
        }
        if (secret == null) {
            secret = weChatConfig.getAppSecret();
        }
        if (grantType == null) {
            grantType = "authorization_code";
        }
        AbstractAttribute<AppletCode2SessionResponse> attribute = new WeChatPlatformAttribute<>();
        attribute.setMethod("GET");
        attribute.setRequestPath("/sns/jscode2session");
        attribute.setParameters("appid=" + appId + "&secret=" + secret + "&js_code=" + jsCode + "&grant_type=" + grantType);
        attribute.setResponseClass(AppletCode2SessionResponse.class);
        return attribute;
    }

}