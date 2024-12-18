package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.exception.WeChatParamsException;
import io.github.aapplet.wechat.params.WeChatRequestParams;
import io.github.aapplet.wechat.response.WeChatPayScoreQueryResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.StringJoiner;

/**
 * <a href="https://pay.weixin.qq.com/doc/v3/merchant/4012587902">查询支付分订单</a>
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class WeChatPayScoreQueryRequest implements WeChatRequest.V3<WeChatPayScoreQueryResponse> {

    /**
     * 应用ID
     */
    @JsonProperty("appid")
    private String appId;
    /**
     * 服务ID
     */
    @JsonProperty("service_id")
    private String serviceId;
    /**
     * 商户服务订单号
     * <br>
     * 调用支付分查单接口时out_order_no字段和query_id字段必填一个(不允许都填写或都不填写)。
     */
    @JsonProperty("out_order_no")
    private String outOrderNo;
    /**
     * 单据查询ID
     * <br>
     * 调用支付分查单接口时out_order_no字段和query_id字段必填一个(不允许都填写或都不填写)。
     */
    @JsonProperty("query_id")
    private String queryId;

    @JsonIgnore
    String getParameters() {
        StringJoiner join = new StringJoiner("&");
        join.add("appid=" + appId);
        join.add("service_id=" + serviceId);
        if (outOrderNo == null) {
            join.add("query_id=" + queryId);
        } else {
            join.add("out_order_no=" + outOrderNo);
        }
        return join.toString();
    }

    @Override
    public WeChatAttribute<WeChatPayScoreQueryResponse> getAttribute(WeChatConfig wechatConfig) {
        if (outOrderNo == null && queryId == null) {
            throw new WeChatParamsException("查询ID和商户服务订单号不存在");
        }
        if (appId == null) {
            appId = wechatConfig.getAppId();
        }
        if (serviceId == null) {
            serviceId = wechatConfig.getServiceId();
        }
        var attribute = new WeChatRequestParams<WeChatPayScoreQueryResponse>(wechatConfig.getPayDomain());
        attribute.setMethod("GET");
        attribute.setRequestPath("/v3/payscore/serviceorder");
        attribute.setParameters(this.getParameters());
        attribute.setResponseClass(WeChatPayScoreQueryResponse.class);
        return attribute;
    }

}