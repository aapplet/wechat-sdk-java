package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.attribute.AbstractAttribute;
import io.github.aapplet.wechat.attribute.WeChatPlatformAttribute;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.response.AppletApiQuotaResponse;
import io.github.aapplet.wechat.token.WeChatAccessTokenManager;
import io.github.aapplet.wechat.util.WeChatJsonUtil;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 查询 API 调用额度
 * <p>
 * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/openApi-mgnt/getApiQuota.html
 */
@Data
@Accessors(chain = true)
public class AppletApiQuotaRequest implements WeChatRequest.MP<AppletApiQuotaResponse> {

    /**
     * 接口调用凭证,该参数为 URL 参数,非 Body 参数
     */
    private String accessToken;

    /**
     * api的请求地址,例如"/cgi-bin/message/custom/send";不要前缀“https://api.weixin.qq.com” ,也不要漏了"/",否则都会76003的报错
     */
    @JsonProperty("cgi_path")
    private String cgiPath;

    @Override
    public WeChatAttribute<AppletApiQuotaResponse> getAttribute(WeChatConfig weChatConfig) {
        if (accessToken == null) {
            this.accessToken = WeChatAccessTokenManager.getAccessToken(weChatConfig);
        }
        AbstractAttribute<AppletApiQuotaResponse> attribute = new WeChatPlatformAttribute<>();
        attribute.setMethod("POST");
        attribute.setRequestPath("/cgi-bin/openapi/quota/get");
        attribute.setParameters("access_token=" + accessToken);
        attribute.setRequestBody(WeChatJsonUtil.toJson(this));
        attribute.setResponseClass(AppletApiQuotaResponse.class);
        return attribute;
    }

}