package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.attribute.WeChatPlatformAttribute;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.common.WeChatStatusCodeBase;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.util.WeChatJsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <a href="https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/openApi-mgnt/clearQuota.html">重置API调用次数</a>
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class AppletClearQuotaRequest implements WeChatRequest.MP<WeChatStatusCodeBase> {

    /**
     * @param appId 要被清空的账号的appid
     */
    public AppletClearQuotaRequest(String appId) {
        this.appId = appId;
    }

    /**
     * 接口调用凭证，该参数为 URL 参数，非 Body 参数
     */
    @JsonIgnore
    private String accessToken;

    /**
     * 要被清空的账号的appid
     */
    @JsonProperty("appid")
    private String appId;

    @Override
    public WeChatAttribute<WeChatStatusCodeBase> getAttribute(WeChatConfig wechatConfig) {
        if (accessToken == null) {
            accessToken = wechatConfig.getAccessTokenManager().getAccessToken();
        }
        var attribute = new WeChatPlatformAttribute<WeChatStatusCodeBase>();
        attribute.setMethod("POST");
        attribute.setRequestPath("/cgi-bin/clear_quota");
        attribute.setParameters("access_token=" + accessToken);
        attribute.setRequestBody(WeChatJsonUtil.toJson(this));
        attribute.setResponseClass(WeChatStatusCodeBase.class);
        return attribute;
    }

}