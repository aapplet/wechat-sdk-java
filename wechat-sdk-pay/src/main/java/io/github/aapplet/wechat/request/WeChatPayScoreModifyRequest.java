package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.attribute.AbstractAttribute;
import io.github.aapplet.wechat.attribute.WeChatPayAttribute;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.domain.WeChatPayScore;
import io.github.aapplet.wechat.exception.WeChatParamsException;
import io.github.aapplet.wechat.response.WeChatPayScoreModifyResponse;
import io.github.aapplet.wechat.util.WeChatJsonUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 修改订单金额API
 * <p>
 * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter6_1_17.shtml
 */
@Data
@Accessors(chain = true)
public class WeChatPayScoreModifyRequest implements WeChatRequest.V3<WeChatPayScoreModifyResponse> {

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
     * 后付费项目
     */
    @JsonProperty("post_payments")
    private List<WeChatPayScore.PostPayment> postPayments;
    /**
     * 后付费商户优惠
     */
    @JsonProperty("post_discounts")
    private List<WeChatPayScore.PostDiscount> postDiscounts;
    /**
     * 总金额
     */
    @JsonProperty("total_amount")
    private Integer totalAmount;
    /**
     * 修改原因
     */
    @JsonProperty("reason")
    private String reason;

    @Override
    public WeChatAttribute<WeChatPayScoreModifyResponse> getAttribute(WeChatConfig weChatConfig) {
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
            throw new WeChatParamsException("修改原因不存在");
        }
        AbstractAttribute<WeChatPayScoreModifyResponse> attribute = new WeChatPayAttribute<>();
        attribute.setMethod("POST");
        attribute.setRequestPath("/v3/payscore/serviceorder/" + outOrderNo + "/modify");
        attribute.setRequestBody(WeChatJsonUtil.toJson(this));
        attribute.setResponseClass(WeChatPayScoreModifyResponse.class);
        return attribute;
    }

}
