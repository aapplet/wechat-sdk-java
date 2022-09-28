package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.attribute.AbstractAttribute;
import io.github.aapplet.wechat.attribute.WeChatPaymentAttribute;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.domain.WeChatRefund;
import io.github.aapplet.wechat.response.WeChatRefundResponse;
import io.github.aapplet.wechat.util.WeChatJsonUtil;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 申请退款API
 * <p>
 * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_9.shtml
 */
@Data
@Accessors(chain = true)
public class WeChatRefundRequest implements WeChatRequest.V3<WeChatRefundResponse> {

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
     * 商户退款单号
     */
    @JsonProperty("out_refund_no")
    private String outRefundNo;
    /**
     * 退款原因
     */
    @JsonProperty("reason")
    private String reason;
    /**
     * 退款结果回调url
     */
    @JsonProperty("notify_url")
    private String notifyUrl;
    /**
     * 退款资金来源
     */
    @JsonProperty("funds_account")
    private String fundsAccount;
    /**
     * 金额信息
     */
    @JsonProperty("amount")
    private WeChatRefund.RefundAmount amount;
    /**
     * 退款商品
     */
    @JsonProperty("goods_detail")
    private WeChatRefund.GoodsDetail goodsDetail;

    @Override
    public WeChatAttribute<WeChatRefundResponse> getAttribute(WeChatConfig weChatConfig) {
        if (notifyUrl == null) {
            notifyUrl = weChatConfig.getRefundNotifyUrl();
        }
        AbstractAttribute<WeChatRefundResponse> attribute = new WeChatPaymentAttribute<>();
        attribute.setMethod("POST");
        attribute.setRequestPath("/v3/refund/domestic/refunds");
        attribute.setRequestBody(WeChatJsonUtil.toJson(this));
        attribute.setResponseClass(WeChatRefundResponse.class);
        return attribute;
    }

}