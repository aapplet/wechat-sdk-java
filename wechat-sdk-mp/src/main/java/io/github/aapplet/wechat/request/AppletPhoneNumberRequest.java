package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.attribute.AbstractAttribute;
import io.github.aapplet.wechat.attribute.WeChatPlatformAttribute;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.response.AppletPhoneNumberResponse;
import io.github.aapplet.wechat.util.WeChatJsonUtil;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 获取手机号
 * <p>
 * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-info/phone-number/getPhoneNumber.html
 */
@Data
@Accessors(chain = true)
public class AppletPhoneNumberRequest implements WeChatRequest.MP<AppletPhoneNumberResponse> {

    /**
     * 接口调用凭证,该参数为 URL 参数,非 Body 参数。
     */
    private String accessToken;

    /**
     * 手机号获取凭证
     */
    @JsonProperty("code")
    private String code;


    @Override
    public WeChatAttribute<AppletPhoneNumberResponse> getAttribute(WeChatConfig weChatConfig) {
        if (accessToken == null) {
            this.accessToken = weChatConfig.getAccessTokenManager().getAccessToken();
        }
        AbstractAttribute<AppletPhoneNumberResponse> attribute = new WeChatPlatformAttribute<>();
        attribute.setMethod("POST");
        attribute.setRequestPath("/wxa/business/getuserphonenumber");
        attribute.setParameters("access_token=" + accessToken);
        attribute.setRequestBody(WeChatJsonUtil.toJson(this));
        attribute.setResponseClass(AppletPhoneNumberResponse.class);
        return attribute;
    }

}