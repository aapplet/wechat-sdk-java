package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.domain.WeChatRefund;
import io.github.aapplet.wechat.response.WeChatRefundResponse;
import io.github.aapplet.wechat.util.WeChatJacksonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <a href="https://pay.weixin.qq.com/doc/v3/merchant/4012587971">微信支付分-申请退款</a>
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
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
    private WeChatRefund.Amount amount;
    /**
     * 退款商品
     */
    @JsonProperty("goods_detail")
    private List<WeChatRefund.GoodsDetail> goodsDetails;

    @Override
    public WeChatAttribute<WeChatRefundResponse> getAttribute(WeChatConfig wechatConfig) {
        if (notifyUrl == null) {
            notifyUrl = wechatConfig.getRefundNotifyUrl();
        }
        var attribute = new WeChatAttributeImpl<WeChatRefundResponse>(wechatConfig.getPayDomain());
        attribute.setMethod("POST");
        attribute.setRequestPath("/v3/refund/domestic/refunds");
        attribute.setRequestBody(WeChatJacksonUtil.toJson(this));
        attribute.setResponseClass(WeChatRefundResponse.class);
        return attribute;
    }

}