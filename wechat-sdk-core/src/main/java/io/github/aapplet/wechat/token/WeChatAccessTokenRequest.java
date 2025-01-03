package io.github.aapplet.wechat.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.request.WeChatAttributeImpl;
import io.github.aapplet.wechat.util.WeChatJacksonUtil;

/**
 * <a href="https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-access-token/getStableAccessToken.html">获取稳定版接口调用凭据</a>
 */
public class WeChatAccessTokenRequest implements WeChatRequest.MP<WeChatAccessTokenResponse> {

    /**
     * @param forceRefresh 强制刷新模式
     */
    public WeChatAccessTokenRequest(boolean forceRefresh) {
        this.forceRefresh = forceRefresh;
    }

    /**
     * AppID
     */
    @JsonProperty("appid")
    private String appId;
    /**
     * AppSecret
     */
    @JsonProperty("secret")
    private String appSecret;
    /**
     * 填写 client_credential
     */
    @JsonProperty("grant_type")
    private String grantType;
    /**
     * 强制刷新模式
     */
    @JsonProperty("force_refresh")
    private Boolean forceRefresh;

    @Override
    public WeChatAttribute<WeChatAccessTokenResponse> getAttribute(WeChatConfig wechatConfig) {
        if (appId == null) {
            appId = wechatConfig.getAppId();
        }
        if (appSecret == null) {
            appSecret = wechatConfig.getAppSecret();
        }
        if (grantType == null) {
            grantType = "client_credential";
        }
        if (forceRefresh == null) {
            forceRefresh = false;
        }
        var attribute = new WeChatAttributeImpl<WeChatAccessTokenResponse>(wechatConfig.getMpDomain());
        attribute.setMethod("POST");
        attribute.setRequestPath("/cgi-bin/stable_token");
        attribute.setRequestBody(WeChatJacksonUtil.toJson(this));
        attribute.setResponseClass(WeChatAccessTokenResponse.class);
        return attribute;
    }

}