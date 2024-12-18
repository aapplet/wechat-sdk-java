package io.github.aapplet.wechat.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.util.WeChatJsonUtil;
import lombok.Data;

/**
 * 微信状态码
 */
public class WeChatStatusCode {

    @Data
    public static class MP implements WeChatResponse.MP {
        /**
         * 错误码
         */
        @JsonProperty("errcode")
        private Integer code;
        /**
         * 错误信息
         */
        @JsonProperty("errmsg")
        private String message;

        /**
         * @return 请求是否成功
         */
        public boolean ok() {
            return code == null || code == 0;
        }

        /**
         * @param bytes 业务错误码数据
         * @return 业务错误码实例
         */
        public static WeChatStatusCode.MP fromJson(byte[] bytes) {
            return WeChatJsonUtil.fromJson(bytes, WeChatStatusCode.MP.class);
        }
    }

    @Data
    public static class PAY implements WeChatResponse.V3 {
        /**
         * 错误码
         */
        @JsonProperty("code")
        private String code;
        /**
         * 错误信息
         */
        @JsonProperty("message")
        private String message;

        /**
         * @param bytes 业务错误码数据
         * @return 业务错误码实例
         */
        public static WeChatStatusCode.PAY fromJson(byte[] bytes) {
            return WeChatJsonUtil.fromJson(bytes, WeChatStatusCode.PAY.class);
        }
    }

}