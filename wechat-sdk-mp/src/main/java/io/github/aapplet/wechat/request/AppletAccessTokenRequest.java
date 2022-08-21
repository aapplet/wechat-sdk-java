package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.WeChatMediaPlatformAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.response.AppletAccessTokenResponse;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 获取小程序全局唯一后台接口调用凭据（access_token）。调用绝大多数后台接口时都需使用 access_token，开发者需要进行妥善保存。
 * <p>
 * https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/access-token/auth.getAccessToken.html
 */
@Data
@Accessors(chain = true)
public class AppletAccessTokenRequest implements WeChatRequest<AppletAccessTokenResponse> {

    /**
     * 小程序 appId
     */
    @JsonProperty("appid")
    private String appid;

    /**
     * 小程序 appSecret
     */
    @JsonProperty("secret")
    private String secret;

    /**
     * 填写 client_credential
     */
    @JsonProperty("grant_type")
    private String grantType;

    @Override
    public WeChatMediaPlatformAttribute<AppletAccessTokenResponse> getAttribute(WeChatConfig weChatConfig) {
        if (appid == null) {
            appid = weChatConfig.getAppId();
        }
        if (secret == null) {
            secret = weChatConfig.getAppSecret();
        }
        if (grantType == null) {
            grantType = "client_credential";
        }
        WeChatMediaPlatformAttribute<AppletAccessTokenResponse> attribute = new WeChatMediaPlatformAttribute<>();
        attribute.setMethod("GET");
        attribute.setDomain("https://api.weixin.qq.com/cgi-bin/token");
        attribute.setParameters("appid=" + appid + "&secret=" + secret + "&grant_type=" + grantType);
        attribute.setResponseClass(AppletAccessTokenResponse.class);
        return attribute.init();
    }

}