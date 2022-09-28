package io.github.aapplet.wechat.notify;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.domain.WeChatPayScore;
import lombok.Data;

import java.util.List;

/**
 * 微信支付分支付成功通知
 * <p>
 * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter6_1_22.shtml
 */
@Data
public class WeChatPayScoreNotify implements WeChatResponse.Notify {

    /**
     * 应用ID
     */
    @JsonProperty("appid")
    private String appId;
    /**
     * 商户号
     */
    @JsonProperty("mchid")
    private String mchId;
    /**
     * 服务ID
     */
    @JsonProperty("service_id")
    private String serviceId;
    /**
     * 商户服务订单号
     */
    @JsonProperty("out_order_no")
    private String outOrderNo;
    /**
     * 服务信息
     */
    @JsonProperty("service_introduction")
    private String serviceIntroduction;
    /**
     * 服务订单状态
     */
    @JsonProperty("state")
    private String state;
    /**
     * 订单状态说明
     */
    @JsonProperty("state_description")
    private String stateDescription;
    /**
     * 商户收款总金额
     */
    @JsonProperty("total_amount")
    private String totalAmount;
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
     * 订单风险金
     */
    @JsonProperty("risk_fund")
    private WeChatPayScore.RiskFund riskFund;
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
     * 微信支付服务订单号
     */
    @JsonProperty("order_id")
    private String orderId;
    /**
     * 是否需要收款
     */
    @JsonProperty("need_collection")
    private String need_collection;
    /**
     * 收款信息
     */
    @JsonProperty("collection")
    private WeChatPayScore.Collection collection;
    /**
     * 用户标识
     */
    @JsonProperty("openid")
    private String openId;

}