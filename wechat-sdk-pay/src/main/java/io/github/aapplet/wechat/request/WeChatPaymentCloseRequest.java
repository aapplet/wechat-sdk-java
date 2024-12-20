package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.exception.WeChatParamsException;
import io.github.aapplet.wechat.response.WeChatNoContent;
import io.github.aapplet.wechat.util.WeChatJacksonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 关闭订单API
 * <p>
 * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_3.shtml
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeChatPaymentCloseRequest implements WeChatRequest.V3<WeChatNoContent> {

    /**
     * 直连商户号
     */
    @JsonProperty("mchid")
    private String mchId;
    /**
     * 商户订单号
     */
    private String outTradeNo;

    @Override
    public WeChatAttribute<WeChatNoContent> getAttribute(WeChatConfig wechatConfig) {
        if (mchId == null) {
            mchId = wechatConfig.getMerchantId();
        }
        if (outTradeNo == null) {
            throw new WeChatParamsException("商户订单号不存在");
        }
        var attribute = new WeChatAttributeImpl<WeChatNoContent>(wechatConfig.getPayDomain());
        attribute.setMethod("POST");
        attribute.setRequestPath("/v3/pay/transactions/out-trade-no/" + outTradeNo + "/close");
        attribute.setRequestBody(WeChatJacksonUtil.toJson(this));
        attribute.setResponseClass(WeChatNoContent.class);
        return attribute;
    }

}