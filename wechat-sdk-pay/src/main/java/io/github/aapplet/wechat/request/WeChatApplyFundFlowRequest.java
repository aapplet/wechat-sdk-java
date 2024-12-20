package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.exception.WeChatParamsException;
import io.github.aapplet.wechat.response.WeChatApplyBillResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.StringJoiner;

/**
 * <a href="https://pay.weixin.qq.com/doc/v3/merchant/4012556745">申请资金账单</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeChatApplyFundFlowRequest implements WeChatRequest.V3<WeChatApplyBillResponse> {

    /**
     * 【账单日期】格式yyyy-MM-DD，仅支持三个月内的账单下载申请。
     */
    @JsonProperty("bill_date")
    private String billDate;
    /**
     * 【资金账户类型】不填默认是BASIC
     * <li>BASIC: 基本账户</li>
     * <li>OPERATION: 运营账户</li>
     * <li>FEES: 手续费账户</li>
     */
    @JsonProperty("account_type")
    private String accountType;
    /**
     * 【压缩格式】不填则以不压缩的方式返回数据流
     * <li>GZIP: GZIP格式压缩，返回格式为.gzip的压缩包账单</li>
     */
    @JsonProperty("tar_type")
    private String tarType;

    @JsonIgnore
    String getParameters() {
        StringJoiner join = new StringJoiner("&");
        if (billDate == null) {
            throw new WeChatParamsException("账单日期不存在");
        } else {
            join.add("bill_date=" + billDate);
        }
        if (accountType != null) {
            join.add("account_type=" + accountType);
        }
        if (tarType != null) {
            join.add("tar_type=" + tarType);
        }
        return join.toString();
    }

    @Override
    public WeChatAttribute<WeChatApplyBillResponse> getAttribute(WeChatConfig wechatConfig) {
        var attribute = new WeChatAttributeImpl<WeChatApplyBillResponse>(wechatConfig.getPayDomain());
        attribute.setMethod("GET");
        attribute.setRequestPath("/v3/bill/fundflowbill");
        attribute.setParameters(this.getParameters());
        attribute.setResponseClass(WeChatApplyBillResponse.class);
        return attribute;
    }

}