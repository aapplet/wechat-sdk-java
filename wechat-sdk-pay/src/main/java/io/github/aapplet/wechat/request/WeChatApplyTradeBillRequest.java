package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.attribute.AbstractAttribute;
import io.github.aapplet.wechat.attribute.WeChatPaymentAttribute;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.exception.WeChatParamsException;
import io.github.aapplet.wechat.response.WeChatApplyBillResponse;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.StringJoiner;

/**
 * 申请交易账单API
 * <p>
 * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_6.shtml
 */
@Data
@Accessors(chain = true)
public class WeChatApplyTradeBillRequest implements WeChatRequest.V3<WeChatApplyBillResponse> {

    /**
     * 账单日期
     */
    @JsonProperty("bill_date")
    private String billDate;
    /**
     * 账单类型
     */
    @JsonProperty("bill_type")
    private String billType;
    /**
     * 压缩类型
     */
    @JsonProperty("tar_type")
    private String tarType;

    String getParameters() {
        StringJoiner join = new StringJoiner("&");
        if (billDate == null) {
            throw new WeChatParamsException("账单日期不存在");
        } else {
            join.add("bill_date=" + billDate);
        }
        if (billType != null) {
            join.add("bill_type=" + billType);
        }
        if (tarType != null) {
            join.add("tar_type=" + tarType);
        }
        return join.toString();
    }

    @Override
    public WeChatAttribute<WeChatApplyBillResponse> getAttribute(WeChatConfig weChatConfig) {
        AbstractAttribute<WeChatApplyBillResponse> attribute = new WeChatPaymentAttribute<>();
        attribute.setMethod("GET");
        attribute.setRequestPath("/v3/bill/tradebill");
        attribute.setParameters(this.getParameters());
        attribute.setResponseClass(WeChatApplyBillResponse.class);
        return attribute;
    }

}