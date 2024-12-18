package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.params.WeChatRequestParams;
import io.github.aapplet.wechat.response.AppletApiQuotaResponse;
import io.github.aapplet.wechat.util.WeChatJsonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <a href="https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/openApi-mgnt/getApiQuota.html">查询API调用额度</a>
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class AppletApiQuotaRequest implements WeChatRequest.MP<AppletApiQuotaResponse> {

    /**
     * @param cgiPath api的请求地址
     */
    public AppletApiQuotaRequest(String cgiPath) {
        this.cgiPath = cgiPath;
    }

    /**
     * 接口调用凭证，该参数为 URL 参数，非 Body 参数
     */
    @JsonIgnore
    private String accessToken;

    /**
     * api的请求地址
     */
    @JsonProperty("cgi_path")
    private String cgiPath;

    @Override
    public WeChatAttribute<AppletApiQuotaResponse> getAttribute(WeChatConfig wechatConfig) {
        if (accessToken == null) {
            accessToken = wechatConfig.getAccessTokenManager().getAccessToken();
        }
        var attribute = new WeChatRequestParams<AppletApiQuotaResponse>(wechatConfig.getMpDomain());
        attribute.setMethod("POST");
        attribute.setRequestPath("/cgi-bin/openapi/quota/get");
        attribute.setParameters("access_token=" + accessToken);
        attribute.setRequestBody(WeChatJsonUtil.toJson(this));
        attribute.setResponseClass(AppletApiQuotaResponse.class);
        return attribute;
    }

}