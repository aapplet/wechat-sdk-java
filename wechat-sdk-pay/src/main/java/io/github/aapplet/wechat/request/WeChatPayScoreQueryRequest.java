package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.attribute.AbstractAttribute;
import io.github.aapplet.wechat.attribute.WeChatPaymentAttribute;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.exception.WeChatParamsException;
import io.github.aapplet.wechat.response.WeChatPayScoreQueryResponse;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.StringJoiner;

/**
 * 查询支付分订单API
 * <p>
 * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter6_1_15.shtml
 */
@Data
@Accessors(chain = true)
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
     * 回跳查询ID
     */
    @JsonProperty("query_id")
    private String queryId;

    /**
     * 商户服务订单号
     */
    @JsonProperty("out_order_no")
    private String outOrderNo;

    String getParameters() {
        StringJoiner join = new StringJoiner("&");
        join.add("appid=" + appId);
        join.add("service_id=" + serviceId);
        if (queryId != null) {
            join.add("query_id=" + queryId);
        } else {
            join.add("out_order_no=" + outOrderNo);
        }
        return join.toString();
    }

    @Override
    public WeChatAttribute<WeChatPayScoreQueryResponse> getAttribute(WeChatConfig wechatConfig) {
        if (appId == null) {
            appId = wechatConfig.getAppId();
        }
        if (serviceId == null) {
            serviceId = wechatConfig.getServiceId();
        }
        if (queryId == null && outOrderNo == null) {
            throw new WeChatParamsException("查询ID和商户服务订单号不存在");
        }
        AbstractAttribute<WeChatPayScoreQueryResponse> attribute = new WeChatPaymentAttribute<>();
        attribute.setMethod("GET");
        attribute.setRequestPath("/v3/payscore/serviceorder");
        attribute.setParameters(this.getParameters());
        attribute.setResponseClass(WeChatPayScoreQueryResponse.class);
        return attribute;
    }

}