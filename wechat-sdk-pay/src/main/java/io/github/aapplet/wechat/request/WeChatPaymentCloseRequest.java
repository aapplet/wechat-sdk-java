package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.attribute.AbstractAttribute;
import io.github.aapplet.wechat.attribute.WeChatPaymentAttribute;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.common.WeChatNoContentResponse;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.exception.WeChatParamsException;
import io.github.aapplet.wechat.util.WeChatJsonUtil;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 关闭订单API
 * <p>
 * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_3.shtml
 */
@Data
@Accessors(chain = true)
public class WeChatPaymentCloseRequest implements WeChatRequest.V3<WeChatNoContentResponse> {

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
    public WeChatAttribute<WeChatNoContentResponse> getAttribute(WeChatConfig weChatConfig) {
        if (mchId == null) {
            mchId = weChatConfig.getMchId();
        }
        if (outTradeNo == null) {
            throw new WeChatParamsException("商户订单号不存在");
        }
        AbstractAttribute<WeChatNoContentResponse> attribute = new WeChatPaymentAttribute<>();
        attribute.setMethod("POST");
        attribute.setRequestPath("/v3/pay/transactions/out-trade-no/" + outTradeNo + "/close");
        attribute.setRequestBody(WeChatJsonUtil.toJson(this));
        attribute.setResponseClass(WeChatNoContentResponse.class);
        return attribute;
    }

}