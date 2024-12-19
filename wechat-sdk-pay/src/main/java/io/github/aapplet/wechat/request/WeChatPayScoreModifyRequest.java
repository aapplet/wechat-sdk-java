package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.domain.WeChatPayScore;
import io.github.aapplet.wechat.exception.WeChatParamsException;
import io.github.aapplet.wechat.response.WeChatPayScoreModifyResponse;
import io.github.aapplet.wechat.util.WeChatJacksonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <a href="https://pay.weixin.qq.com/doc/v3/merchant/4012587957">修改订单金额</a>
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class WeChatPayScoreModifyRequest implements WeChatRequest.V3<WeChatPayScoreModifyResponse> {

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
    public WeChatAttribute<WeChatPayScoreModifyResponse> getAttribute(WeChatConfig wechatConfig) {
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
            throw new WeChatParamsException("修改原因不存在");
        }
        var attribute = new WeChatAttributeImpl<WeChatPayScoreModifyResponse>(wechatConfig.getPayDomain());
        attribute.setMethod("POST");
        attribute.setRequestPath("/v3/payscore/serviceorder/" + outOrderNo + "/modify");
        attribute.setRequestBody(WeChatJacksonUtil.toJson(this));
        attribute.setResponseClass(WeChatPayScoreModifyResponse.class);
        return attribute;
    }

}
