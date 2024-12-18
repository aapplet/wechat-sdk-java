package io.github.aapplet.wechat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatResponse;
import lombok.Data;

@Data
public class WeChatPayScoreCancelResponse implements WeChatResponse.V3 {

    /**
     * 应用ID
     */
    @JsonProperty("appid")
    private String appId;
    /**
     * 商户号
     */
    @JsonProperty("mchid")
    private String mchId;
    /**
     * 商户订单号
     */
    @JsonProperty("out_order_no")
    private String outOrderNo;
    /**
     * 服务ID
     */
    @JsonProperty("service_id")
    private String serviceId;
    /**
     * 微信支付服务订单号
     */
    @JsonProperty("order_id")
    private String orderId;

}