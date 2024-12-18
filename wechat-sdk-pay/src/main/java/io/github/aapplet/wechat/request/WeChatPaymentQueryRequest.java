package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.exception.WeChatParamsException;
import io.github.aapplet.wechat.params.WeChatRequestParams;
import io.github.aapplet.wechat.response.WeChatPaymentQueryResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 查询订单API
 * <p>
 * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_2.shtml
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class WeChatPaymentQueryRequest implements WeChatRequest.V3<WeChatPaymentQueryResponse> {

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
     * 请求路径
     */
    String getRequestPath() {
        // 微信支付订单号查询
        if (transactionId != null) {
            return "/v3/pay/transactions/id/" + transactionId;
        }
        // 商户订单号查询
        if (outTradeNo != null) {
            return "/v3/pay/transactions/out-trade-no/" + outTradeNo;
        }
        throw new WeChatParamsException("商户订单号和微信支付订单号不存在");
    }

    @Override
    public WeChatAttribute<WeChatPaymentQueryResponse> getAttribute(WeChatConfig wechatConfig) {
        if (mchId == null) {
            mchId = wechatConfig.getMerchantId();
        }
        var attribute = new WeChatRequestParams<WeChatPaymentQueryResponse>(wechatConfig.getPayDomain());
        attribute.setMethod("GET");
        attribute.setRequestPath(this.getRequestPath());
        attribute.setParameters("mchid=" + mchId);
        attribute.setResponseClass(WeChatPaymentQueryResponse.class);
        return attribute;
    }

}