package io.github.aapplet.wechat.notify;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.domain.WeChatRefund;
import lombok.Data;

/**
 * 退款结果通知
 * <p>
 * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_11.shtml
 */
@Data
public class WeChatRefundNotify implements WeChatResponse.Notify {

    /**
     * 直连商户号
     */
    @JsonProperty("mchid")
    private String mchId;
    /**
     * 商户订单号
     */
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    /**
     * 微信支付订单号
     */
    @JsonProperty("transaction_id")
    private String transactionId;
    /**
     * 商户退款单号
     */
    @JsonProperty("out_refund_no")
    private String outRefundNo;
    /**
     * 微信支付退款单号
     */
    @JsonProperty("refund_id")
    private String refundId;
    /**
     * 退款状态
     */
    @JsonProperty("refund_status")
    private String refundStatus;
    /**
     * 退款成功时间
     */
    @JsonProperty("success_time")
    private String successTime;
    /**
     * 退款入账账户
     */
    @JsonProperty("user_received_account")
    private String userReceivedAccount;
    /**
     * 金额信息
     */
    @JsonProperty("amount")
    private WeChatRefund.Amount amount;

}