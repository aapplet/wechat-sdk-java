package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.attribute.AbstractAttribute;
import io.github.aapplet.wechat.attribute.WeChatPaymentAttribute;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.exception.WeChatParamsException;
import io.github.aapplet.wechat.response.WeChatRefundResponse;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 查询单笔退款API
 * <p>
 * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_10.shtml
 */
@Data
@Accessors(chain = true)
public class WeChatRefundQueryRequest implements WeChatRequest.V3<WeChatRefundResponse> {

    /**
     * 商户退款单号
     */
    @JsonProperty("out_refund_no")
    private String outRefundNo;

    @Override
    public WeChatAttribute<WeChatRefundResponse> getAttribute(WeChatConfig wechatConfig) {
        if (outRefundNo == null || outRefundNo.isBlank()) {
            throw new WeChatParamsException("商户退款单号不存在");
        }
        AbstractAttribute<WeChatRefundResponse> attribute = new WeChatPaymentAttribute<>();
        attribute.setMethod("POST");
        attribute.setRequestPath("/v3/refund/domestic/refunds/" + outRefundNo);
        attribute.setResponseClass(WeChatRefundResponse.class);
        return attribute;
    }

}