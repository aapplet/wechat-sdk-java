package io.github.aapplet.wechat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.domain.WeChatPayScore;
import lombok.Data;

import java.util.List;

@Data
public class WeChatPayScoreModifyResponse implements WeChatResponse.V3 {

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
     * <li>CREATED:商户已创建服务订单</li>
     * <li>DOING:服务订单进行中</li>
     * <li>DONE:服务订单完成(终态)</li>
     * <li>REVOKED:商户取消服务订单(终态)</li>
     * <li>EXPIRED:服务订单已失效，"商户已创建服务订单"状态超过30天未变动，则订单失效(终态)</li>
     */
    @JsonProperty("state")
    private String state;
    /**
     * 订单状态说明
     * <li>USER_CONFIRM：用户已确认状态，表示用户成功确认订单后所处状态。</li>
     * <li>MCH_COMPLETE：商户已完结状态，指商户调用完结接口成功后至扣款成功前的状态。</li>
     */
    @JsonProperty("state_description")
    private String stateDescription;
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
     * 服务风险金
     */
    @JsonProperty("risk_fund")
    private WeChatPayScore.RiskFund riskFund;
    /**
     * 商户收款总金额
     */
    @JsonProperty("total_amount")
    private Integer totalAmount;
    /**
     * 是否需要收款
     */
    @JsonProperty("need_collection")
    private Boolean needCollection;
    /**
     * 收款信息
     */
    @JsonProperty("collection")
    private WeChatPayScore.Collection collection;
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
     * 用户标识
     */
    @JsonProperty("openid")
    private String openId;
    /**
     * 微信支付服务订单号
     */
    @JsonProperty("order_id")
    private String orderId;

}
