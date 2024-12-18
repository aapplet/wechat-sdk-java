package io.github.aapplet.wechat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatResponse;
import lombok.Data;

@Data
public class AppletPhoneNumberResponse implements WeChatResponse.MP {

    /**
     * 用户手机号信息
     */
    @JsonProperty("phone_info")
    private PhoneInfo phoneInfo;

    @Data
    public static class PhoneInfo {
        /**
         * 用户绑定的手机号（国外手机号会有区号）
         */
        @JsonProperty("phoneNumber")
        private String phoneNumber;
        /**
         * 没有区号的手机号
         */
        @JsonProperty("purePhoneNumber")
        private String purePhoneNumber;
        /**
         * 区号
         */
        @JsonProperty("countryCode")
        private String countryCode;
        /**
         * 数据水印
         */
        @JsonProperty("watermark")
        private Watermark watermark;
    }

    @Data
    public static class Watermark {
        /**
         * 用户获取手机号操作的时间戳
         */
        @JsonProperty("timestamp")
        private Long timestamp;
        /**
         * 小程序appid
         */
        @JsonProperty("appid")
        private String appId;
    }

}