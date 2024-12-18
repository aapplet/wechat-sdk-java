package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.exception.WeChatParamsException;
import io.github.aapplet.wechat.params.WeChatRequestParams;
import io.github.aapplet.wechat.response.WeChatPayScoreCancelResponse;
import io.github.aapplet.wechat.util.WeChatJsonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <a href="https://pay.weixin.qq.com/doc/v3/merchant/4012587905">取消支付分订单</a>
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class WeChatPayScoreCancelRequest implements WeChatRequest.V3<WeChatPayScoreCancelResponse> {

    /**
     * 商户服务订单号
     */
    @JsonIgnore
    private String outOrderNo;
    /**
     * 应用ID
     */
    @JsonProperty("appid")
    private String appId;
    /**
     * 服务ID
     */
    @JsonProperty("service_id")
    private String serviceId;
    /**
     * 取消原因
     */
    @JsonProperty("reason")
    private String reason;

    @Override
    public WeChatAttribute<WeChatPayScoreCancelResponse> getAttribute(WeChatConfig wechatConfig) {
        if (outOrderNo == null) {
            throw new WeChatParamsException("商户服务订单号不存在");
        }
        if (appId == null) {
            appId = wechatConfig.getAppId();
        }
        if (serviceId == null) {
            serviceId = wechatConfig.getServiceId();
        }
        if (reason == null) {
            throw new WeChatParamsException("取消原因不存在");
        }
        var attribute = new WeChatRequestParams<WeChatPayScoreCancelResponse>(wechatConfig.getPayDomain());
        attribute.setMethod("POST");
        attribute.setRequestPath("/v3/payscore/serviceorder/" + outOrderNo + "/cancel");
        attribute.setRequestBody(WeChatJsonUtil.toJson(this));
        attribute.setResponseClass(WeChatPayScoreCancelResponse.class);
        return attribute;
    }

}