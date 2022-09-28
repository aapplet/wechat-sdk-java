package io.github.aapplet.wechat.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.attribute.AbstractAttribute;
import io.github.aapplet.wechat.attribute.WeChatPlatformAttribute;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * access_token是公众号的全局唯一接口调用凭据,公众号调用各接口时都需使用access_token。开发者需要进行妥善保存
 * <p>
 * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-access-token/getAccessToken.html
 */
@Data
@Accessors(chain = true)
public class WeChatAccessTokenRequest implements WeChatRequest.MP<WeChatAccessTokenResponse> {

    /**
     * AppID
     */
    @JsonProperty("appid")
    private String appId;

    /**
     * AppSecret
     */
    @JsonProperty("secret")
    private String secret;

    /**
     * 填写 client_credential
     */
    @JsonProperty("grant_type")
    private String grantType;

    @Override
    public WeChatAttribute<WeChatAccessTokenResponse> getAttribute(WeChatConfig weChatConfig) {
        if (appId == null) {
            appId = weChatConfig.getAppId();
        }
        if (secret == null) {
            secret = weChatConfig.getAppSecret();
        }
        if (grantType == null) {
            grantType = "client_credential";
        }
        AbstractAttribute<WeChatAccessTokenResponse> attribute = new WeChatPlatformAttribute<>();
        attribute.setMethod("GET");
        attribute.setRequestPath("/cgi-bin/token");
        attribute.setParameters("appid=" + appId + "&secret=" + secret + "&grant_type=" + grantType);
        attribute.setResponseClass(WeChatAccessTokenResponse.class);
        return attribute;
    }

}