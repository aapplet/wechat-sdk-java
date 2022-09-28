package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.attribute.AbstractAttribute;
import io.github.aapplet.wechat.attribute.WeChatPaymentAttribute;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.domain.WeChatPayScore;
import io.github.aapplet.wechat.exception.WeChatParamsException;
import io.github.aapplet.wechat.response.WeChatPayScoreCompleteResponse;
import io.github.aapplet.wechat.util.WeChatJsonUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 完结支付分订单API
 * <p>
 * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter6_1_18.shtml
 */
@Data
@Accessors(chain = true)
public class WeChatPayScoreCompleteRequest implements WeChatRequest.V3<WeChatPayScoreCompleteResponse> {

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
    public WeChatAttribute<WeChatPayScoreCompleteResponse> getAttribute(WeChatConfig weChatConfig) {
        if (appId == null) {
            appId = weChatConfig.getAppId();
        }
        if (serviceId == null) {
            serviceId = weChatConfig.getServiceId();
        }
        if (outOrderNo == null) {
            throw new WeChatParamsException("商户服务订单号不存在");
        }
        AbstractAttribute<WeChatPayScoreCompleteResponse> attribute = new WeChatPaymentAttribute<>();
        attribute.setMethod("POST");
        attribute.setRequestPath("/v3/payscore/serviceorder/" + outOrderNo + "/complete");
        attribute.setRequestBody(WeChatJsonUtil.toJson(this));
        attribute.setResponseClass(WeChatPayScoreCompleteResponse.class);
        return attribute;
    }

}