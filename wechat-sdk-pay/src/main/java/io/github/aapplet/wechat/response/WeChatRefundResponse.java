package io.github.aapplet.wechat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.domain.WeChatRefund;
import lombok.Data;

import java.util.List;

@Data
public class WeChatRefundResponse implements WeChatResponse.V3 {

    /**
     * 微信支付退款号
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
     * <li>ORIGINAL: 原路退款</li>
     * <li>BALANCE: 退回到余额</li>
     * <li>OTHER_BALANCE: 原账户异常退到其他余额账户</li>
     * <li>OTHER_BANKCARD: 原银行卡异常退到其他银行卡</li>
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
     * <li>SUCCESS: 退款成功</li>
     * <li>CLOSED: 退款关闭</li>
     * <li>PROCESSING: 退款处理中</li>
     * <li>ABNORMAL: 退款异常，需商户平台手动处理，参考交易退款方法介绍。</li>
     */
    @JsonProperty("status")
    private String status;
    /**
     * 资金账户
     * <li>UNSETTLED: 未结算资金</li>
     * <li>AVAILABLE: 可用余额</li>
     * <li>UNAVAILABLE: 不可用余额</li>
     * <li>OPERATION: 运营户</li>
     * <li>BASIC: 基本账户（含可用余额和不可用余额）</li>
     * <li>ECNY_BASIC: 数字人民币基本账户</li>
     */
    @JsonProperty("funds_account")
    private String fundsAccount;
    /**
     * 金额信息
     */
    @JsonProperty("amount")
    private WeChatRefund.Amount amount;
    /**
     * 优惠退款信息
     */
    @JsonProperty("promotion_detail")
    private List<WeChatRefund.PromotionDetail> promotionDetails;

}