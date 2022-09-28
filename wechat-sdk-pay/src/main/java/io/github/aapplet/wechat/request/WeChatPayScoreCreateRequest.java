package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.attribute.AbstractAttribute;
import io.github.aapplet.wechat.attribute.WeChatPayAttribute;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.domain.WeChatPayScore;
import io.github.aapplet.wechat.response.WeChatPayScoreCreateResponse;
import io.github.aapplet.wechat.util.WeChatJsonUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 创建支付分订单API
 * <p>
 * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter6_1_14.shtml
 */
@Data
@Accessors(chain = true)
public class WeChatPayScoreCreateRequest implements WeChatRequest.V3<WeChatPayScoreCreateResponse> {

    /**
     * 商户服务订单号
     */
    @JsonProperty("out_order_no")
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
     * 服务信息
     */
    @JsonProperty("service_introduction")
    private String serviceIntroduction;
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
     * 订单风险金
     */
    @JsonProperty("risk_fund")
    private WeChatPayScore.RiskFund riskFund;
    /**
     * 商户数据包
     */
    @JsonProperty("attach")
    private String attach;
    /**
     * 商户回调地址
     */
    @JsonProperty("notify_url")
    private String notifyUrl;
    /**
     * 用户标识
     */
    @JsonProperty("openid")
    private String openId;
    /**
     * 是否需要用户确认
     */
    @JsonProperty("need_user_confirm")
    private Boolean needUserConfirm;

    @Override
    public WeChatAttribute<WeChatPayScoreCreateResponse> getAttribute(WeChatConfig weChatConfig) {
        if (appId == null) {
            appId = weChatConfig.getAppId();
        }
        if (serviceId == null) {
            serviceId = weChatConfig.getServiceId();
        }
        if (serviceId == null) {
            notifyUrl = weChatConfig.getPayScoreNotifyUrl();
        }
        AbstractAttribute<WeChatPayScoreCreateResponse> attribute = new WeChatPayAttribute<>();
        attribute.setMethod("POST");
        attribute.setRequestPath("/v3/payscore/serviceorder");
        attribute.setRequestBody(WeChatJsonUtil.toJson(this));
        attribute.setResponseClass(WeChatPayScoreCreateResponse.class);
        return attribute;
    }

}