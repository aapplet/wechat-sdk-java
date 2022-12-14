package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.attribute.AbstractAttribute;
import io.github.aapplet.wechat.attribute.WeChatPaymentAttribute;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.domain.WeChatPayment;
import io.github.aapplet.wechat.response.WeChatPaymentJsapiResponse;
import io.github.aapplet.wechat.util.WeChatJsonUtil;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * JSAPI下单
 * <p>
 * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_1.shtml
 */
@Data
@Accessors(chain = true)
public class WeChatPaymentJsapiRequest implements WeChatRequest.V3<WeChatPaymentJsapiResponse> {

    /**
     * 应用ID
     */
    @JsonProperty("appid")
    private String appId;
    /**
     * 直连商户号
     */
    @JsonProperty("mchid")
    private String mchId;
    /**
     * 商品描述
     */
    @JsonProperty("description")
    private String description;
    /**
     * 商户订单号
     */
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    /**
     * 交易结束时间
     */
    @JsonProperty("time_expire")
    private String timeExpire;
    /**
     * 附加数据
     */
    @JsonProperty("attach")
    private String attach;
    /**
     * 通知地址
     */
    @JsonProperty("notify_url")
    private String notifyUrl;
    /**
     * 订单优惠标记
     */
    @JsonProperty("goods_tag")
    private String goodsTag;
    /**
     * 电子发票入口开放标识
     */
    @JsonProperty("support_fapiao")
    private Boolean supportInvoice;
    /**
     * 订单金额
     */
    @JsonProperty("amount")
    private WeChatPayment.Amount amount;
    /**
     * 支付者
     */
    @JsonProperty("payer")
    private WeChatPayment.Payer payer;
    /**
     * 优惠功能
     */
    @JsonProperty("detail")
    private WeChatPayment.Detail detail;
    /**
     * 场景信息
     */
    @JsonProperty("scene_info")
    private WeChatPayment.SceneInfo sceneInfo;
    /**
     * 结算信息
     */
    @JsonProperty("settle_info")
    private WeChatPayment.SettleInfo settleInfo;

    @Override
    public WeChatAttribute<WeChatPaymentJsapiResponse> getAttribute(WeChatConfig weChatConfig) {
        if (appId == null) {
            appId = weChatConfig.getAppId();
        }
        if (mchId == null) {
            mchId = weChatConfig.getMchId();
        }
        if (notifyUrl == null) {
            notifyUrl = weChatConfig.getPayNotifyUrl();
        }
        AbstractAttribute<WeChatPaymentJsapiResponse> attribute = new WeChatPaymentAttribute<>();
        attribute.setMethod("POST");
        attribute.setRequestPath("/v3/pay/transactions/jsapi");
        attribute.setRequestBody(WeChatJsonUtil.toJson(this));
        attribute.setResponseClass(WeChatPaymentJsapiResponse.class);
        return attribute;
    }

}