package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.exception.WeChatParamsException;
import io.github.aapplet.wechat.params.WeChatRequestParams;
import io.github.aapplet.wechat.response.WeChatRefundResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <a href="https://pay.weixin.qq.com/doc/v3/merchant/4012587973">微信支付分-查询退款</a>
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class WeChatRefundQueryRequest implements WeChatRequest.V3<WeChatRefundResponse> {

    /**
     * 商户退款单号
     */
    @JsonIgnore
    private String outRefundNo;

    @Override
    public WeChatAttribute<WeChatRefundResponse> getAttribute(WeChatConfig wechatConfig) {
        if (outRefundNo == null) {
            throw new WeChatParamsException("商户退款单号不存在");
        }
        var attribute = new WeChatRequestParams<WeChatRefundResponse>(wechatConfig.getPayDomain());
        attribute.setMethod("GET");
        attribute.setRequestPath("/v3/refund/domestic/refunds/" + outRefundNo);
        attribute.setResponseClass(WeChatRefundResponse.class);
        return attribute;
    }

}