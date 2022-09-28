package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.attribute.AbstractAttribute;
import io.github.aapplet.wechat.attribute.WeChatPayAttribute;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.exception.WeChatParamsException;
import io.github.aapplet.wechat.response.WeChatPayScoreCancelResponse;
import io.github.aapplet.wechat.util.WeChatJsonUtil;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 取消支付分订单API
 * <p>
 * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter6_1_16.shtml
 */
@Data
@Accessors(chain = true)
public class WeChatPayScoreCancelRequest implements WeChatRequest.V3<WeChatPayScoreCancelResponse> {

    /**
     * 商户服务订单号
     */
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
    public WeChatAttribute<WeChatPayScoreCancelResponse> getAttribute(WeChatConfig weChatConfig) {
        if (appId == null) {
            appId = weChatConfig.getAppId();
        }
        if (serviceId == null) {
            serviceId = weChatConfig.getServiceId();
        }
        if (outOrderNo == null) {
            throw new WeChatParamsException("商户服务订单号不存在");
        }
        if (reason == null) {
            throw new WeChatParamsException("取消原因不存在");
        }
        AbstractAttribute<WeChatPayScoreCancelResponse> attribute = new WeChatPayAttribute<>();
        attribute.setMethod("POST");
        attribute.setRequestPath("/v3/payscore/serviceorder/" + outOrderNo + "/cancel");
        attribute.setRequestBody(WeChatJsonUtil.toJson(this));
        attribute.setResponseClass(WeChatPayScoreCancelResponse.class);
        return attribute;
    }

}