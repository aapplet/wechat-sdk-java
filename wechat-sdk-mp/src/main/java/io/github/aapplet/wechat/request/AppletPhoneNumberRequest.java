package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.response.AppletPhoneNumberResponse;
import io.github.aapplet.wechat.util.WeChatJacksonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-info/phone-number/getPhoneNumber.html">获取手机号</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppletPhoneNumberRequest implements WeChatRequest.MP<AppletPhoneNumberResponse> {

    /**
     * @param code 手机号获取凭证
     */
    public AppletPhoneNumberRequest(String code) {
        this.code = code;
    }

    /**
     * @param accessToken 接口调用凭证，该参数为 URL 参数，非 Body 参数
     * @param code        手机号获取凭证
     */
    public AppletPhoneNumberRequest(String accessToken, String code) {
        this.accessToken = accessToken;
        this.code = code;
    }

    /**
     * 接口调用凭证，该参数为 URL 参数，非 Body 参数
     */
    @JsonIgnore
    private String accessToken;

    /**
     * <a href="https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/getPhoneNumber.html">手机号获取凭证</a>
     */
    @JsonProperty("code")
    private String code;

    /**
     * 用户唯一标识
     */
    @JsonProperty("openid")
    private String openId;

    @Override
    public WeChatAttribute<AppletPhoneNumberResponse> getAttribute(WeChatConfig wechatConfig) {
        if (accessToken == null) {
            accessToken = wechatConfig.getAccessTokenManager().getAccessToken();
        }
        var attribute = new WeChatAttributeImpl<AppletPhoneNumberResponse>(wechatConfig.getMpDomain());
        attribute.setMethod("POST");
        attribute.setRequestPath("/wxa/business/getuserphonenumber");
        attribute.setParameters("access_token=" + accessToken);
        attribute.setRequestBody(WeChatJacksonUtil.toJson(this));
        attribute.setResponseClass(AppletPhoneNumberResponse.class);
        return attribute;
    }

}