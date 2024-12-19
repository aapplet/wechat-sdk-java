package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.domain.WeChatPayment;
import io.github.aapplet.wechat.response.WeChatPaymentJsapiResponse;
import io.github.aapplet.wechat.util.WeChatJacksonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <a href="https://pay.weixin.qq.com/doc/v3/merchant/4012791856">JSAPI下单</a>
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class WeChatPaymentJsapiRequest implements WeChatRequest.V3<WeChatPaymentJsapiResponse> {

    /**
     * 【公众账号ID】
     */
    @JsonProperty("appid")
    private String appId;
    /**
     * 【商户号】
     */
    @JsonProperty("mchid")
    private String mchId;
    /**
     * 【商品描述】
     */
    @JsonProperty("description")
    private String description;
    /**
     * 【商户订单号】
     */
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    /**
     * 【支付结束时间】
     */
    @JsonProperty("time_expire")
    private String timeExpire;
    /**
     * 【商户数据包】
     */
    @JsonProperty("attach")
    private String attach;
    /**
     * 【商户回调地址】
     */
    @JsonProperty("notify_url")
    private String notifyUrl;
    /**
     * 【订单优惠标记】
     */
    @JsonProperty("goods_tag")
    private String goodsTag;
    /**
     * 【电子发票入口开放标识】
     */
    @JsonProperty("support_fapiao")
    private Boolean supportInvoice;
    /**
     * 【订单金额】
     */
    @JsonProperty("amount")
    private WeChatPayment.Amount amount;
    /**
     * 【支付者】
     */
    @JsonProperty("payer")
    private WeChatPayment.Payer payer;
    /**
     * 【优惠功能】
     */
    @JsonProperty("detail")
    private WeChatPayment.Detail detail;
    /**
     * 【场景信息】
     */
    @JsonProperty("scene_info")
    private WeChatPayment.SceneInfo sceneInfo;
    /**
     * 【结算信息】
     */
    @JsonProperty("settle_info")
    private WeChatPayment.SettleInfo settleInfo;

    @Override
    public WeChatAttribute<WeChatPaymentJsapiResponse> getAttribute(WeChatConfig wechatConfig) {
        if (appId == null) {
            appId = wechatConfig.getAppId();
        }
        if (mchId == null) {
            mchId = wechatConfig.getMerchantId();
        }
        if (notifyUrl == null) {
            notifyUrl = wechatConfig.getPayNotifyUrl();
        }
        var attribute = new WeChatAttributeImpl<WeChatPaymentJsapiResponse>(wechatConfig.getPayDomain());
        attribute.setMethod("POST");
        attribute.setRequestPath("/v3/pay/transactions/jsapi");
        attribute.setRequestBody(WeChatJacksonUtil.toJson(this));
        attribute.setResponseClass(WeChatPaymentJsapiResponse.class);
        return attribute;
    }

}