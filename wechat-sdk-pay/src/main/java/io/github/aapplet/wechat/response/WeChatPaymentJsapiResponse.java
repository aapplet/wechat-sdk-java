package io.github.aapplet.wechat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.util.WeChatRandomUtil;
import lombok.Data;

/**
 * <a href="https://pay.weixin.qq.com/doc/v3/merchant/4012265791">小程序调起支付</a>
 */
@Data
public class WeChatPaymentJsapiResponse implements WeChatResponse.V3 {

    /**
     * 预支付交易会话标识
     */
    @JsonProperty("prepay_id")
    private String prepayId;

    @Data
    public static class RequestPayment {
        /**
         * 小程序ID
         */
        @JsonProperty("appId")
        private String appId;
        /**
         * 时间戳
         */
        @JsonProperty("timeStamp")
        private String timeStamp;
        /**
         * 随机字符串，不长于32位
         */
        @JsonProperty("nonceStr")
        private String nonceStr;
        /**
         * 小程序下单接口返回的prepay_id参数值，提交格式如：prepay_id=***
         */
        @JsonProperty("package")
        private String packageValue;
        /**
         * 签名类型，默认为RSA，仅支持RSA
         */
        @JsonProperty("signType")
        private String signType;
        /**
         * 签名
         */
        @JsonProperty("paySign")
        private String paySign;

        /**
         * 签名内容
         */
        private String getSignContent() {
            return appId + "\n" + timeStamp + "\n" + nonceStr + "\n" + packageValue + "\n";
        }
    }

    /**
     * 小程序调起支付所需参数
     *
     * @param wechatConfig 配置信息
     * @return 小程序调起支付参数
     */
    public RequestPayment jsapi(WeChatConfig wechatConfig) {
        RequestPayment requestPayment = new RequestPayment();
        requestPayment.setAppId(wechatConfig.getAppId());
        requestPayment.setTimeStamp(String.valueOf(System.currentTimeMillis() / 1000));
        requestPayment.setNonceStr(WeChatRandomUtil.random32());
        requestPayment.setPackageValue("prepay_id=" + prepayId);
        requestPayment.setSignType("RSA");
        requestPayment.setPaySign(wechatConfig.signature(requestPayment.getSignContent()));
        return requestPayment;
    }

}