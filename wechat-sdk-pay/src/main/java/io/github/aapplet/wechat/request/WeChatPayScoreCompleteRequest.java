package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.domain.WeChatPayScore;
import io.github.aapplet.wechat.exception.WeChatParamsException;
import io.github.aapplet.wechat.params.WeChatRequestParams;
import io.github.aapplet.wechat.response.WeChatPayScoreCompleteResponse;
import io.github.aapplet.wechat.util.WeChatJsonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <a href="https://pay.weixin.qq.com/doc/v3/merchant/4012587955">完结支付分订单</a>
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class WeChatPayScoreCompleteRequest implements WeChatRequest.V3<WeChatPayScoreCompleteResponse> {

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
     * 服务时间段
     */
    @JsonProperty("time_range")
    private WeChatPayScore.TimeRange timeRange;
    /**
     * 服务位置
     */
    @JsonProperty("location")
    private WeChatPayScore.Location location;
    /**
     * 微信支付服务分账标记
     */
    @JsonProperty("profit_sharing")
    private Boolean profitSharing;
    /**
     * 订单优惠标记
     */
    @JsonProperty("goods_tag")
    private String goodsTag;

    @Override
    public WeChatAttribute<WeChatPayScoreCompleteResponse> getAttribute(WeChatConfig wechatConfig) {
        if (outOrderNo == null) {
            throw new WeChatParamsException("商户服务订单号不存在");
        }
        if (appId == null) {
            appId = wechatConfig.getAppId();
        }
        if (serviceId == null) {
            serviceId = wechatConfig.getServiceId();
        }
        var attribute = new WeChatRequestParams<WeChatPayScoreCompleteResponse>(wechatConfig.getPayDomain());
        attribute.setMethod("POST");
        attribute.setRequestPath("/v3/payscore/serviceorder/" + outOrderNo + "/complete");
        attribute.setRequestBody(WeChatJsonUtil.toJson(this));
        attribute.setResponseClass(WeChatPayScoreCompleteResponse.class);
        return attribute;
    }

}