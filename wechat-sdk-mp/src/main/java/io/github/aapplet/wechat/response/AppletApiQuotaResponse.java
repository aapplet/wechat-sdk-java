package io.github.aapplet.wechat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AppletApiQuotaResponse extends WeChatStatusCodeResponse {

    /**
     * quota详情
     */
    @JsonProperty("quota")
    private Quota quota;

    @Data
    public static class Quota {
        /**
         * 当天该账号可调用该接口的次数
         */
        @JsonProperty("daily_limit")
        private String dailyLimit;
        /**
         * 当天已经调用的次数
         */
        @JsonProperty("used")
        private String used;
        /**
         * 当天剩余调用次数
         */
        @JsonProperty("remain")
        private String remain;
    }

}