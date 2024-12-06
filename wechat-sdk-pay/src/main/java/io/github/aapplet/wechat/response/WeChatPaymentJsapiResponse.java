package io.github.aapplet.wechat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.util.WeChatStrUtil;
import lombok.Data;

@Data
public class WeChatPaymentJsapiResponse implements WeChatResponse.V3 {

    /**
     * 预支付交易会话标识
     */
    @JsonProperty("prepay_id")
    private String prepayId;

    @Data
    public static class JsapiResult {
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
         * 随机字符串
         */
        @JsonProperty("nonceStr")
        private String nonceStr;
        /**
         * 订单详情扩展字符串
         */
        @JsonProperty("package")
        private String packageValue;
        /**
         * 签名方式
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
     * 小程序调起支付
     */
    public JsapiResult jsapi(WeChatConfig wechatConfig) {
        JsapiResult jsapiResult = new JsapiResult();
        jsapiResult.setAppId(wechatConfig.getAppId());
        jsapiResult.setTimeStamp(String.valueOf(System.currentTimeMillis() / 1000));
        jsapiResult.setNonceStr(WeChatStrUtil.random());
        jsapiResult.setPackageValue("prepay_id=" + prepayId);
        jsapiResult.setSignType("RSA");
        jsapiResult.setPaySign(wechatConfig.signature(jsapiResult.getSignContent()));
        return jsapiResult;
    }

}