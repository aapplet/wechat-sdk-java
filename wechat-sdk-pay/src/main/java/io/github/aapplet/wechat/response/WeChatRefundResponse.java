package io.github.aapplet.wechat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.domain.WeChatRefund;
import lombok.Data;

@Data
public class WeChatRefundResponse implements WeChatResponse.V3 {

    /**
     * 微信支付退款单号
     */
    @JsonProperty("refund_id")
    private String refundId;
    /**
     * 商户退款单号
     */
    @JsonProperty("out_refund_no")
    private String outRefundNo;
    /**
     * 微信支付订单号
     */
    @JsonProperty("transaction_id")
    private String transactionId;
    /**
     * 商户订单号
     */
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    /**
     * 退款渠道
     */
    @JsonProperty("channel")
    private String channel;
    /**
     * 退款入账账户
     */
    @JsonProperty("user_received_account")
    private String userReceivedAccount;
    /**
     * 退款成功时间
     */
    @JsonProperty("success_time")
    private String successTime;
    /**
     * 退款创建时间
     */
    @JsonProperty("create_time")
    private String createTime;
    /**
     * 退款状态
     */
    @JsonProperty("status")
    private String status;
    /**
     * 资金账户
     */
    @JsonProperty("funds_account")
    private String fundsAccount;
    /**
     * 金额信息
     */
    @JsonProperty("amount")
    private WeChatRefund.RefundDetail amount;
    /**
     * 优惠退款信息
     */
    @JsonProperty("promotion_detail")
    private WeChatRefund.PromotionDetail promotionDetail;

}