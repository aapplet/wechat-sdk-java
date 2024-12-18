package io.github.aapplet.wechat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatResponse;
import lombok.Data;

@Data
public class AppletApiQuotaResponse implements WeChatResponse.MP {

    /**
     * quota详情
     */
    @JsonProperty("quota")
    private Quota quota;
    /**
     * 普通调用频率限制
     */
    @JsonProperty("rate_limit")
    private RateLimit rateLimit;
    /**
     * 代调用频率限制
     */
    @JsonProperty("component_rate_limit")
    private RateLimit componentRateLimit;

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

    @Data
    public static class RateLimit {
        /**
         * 周期内可调用数量，单位 次
         */
        @JsonProperty("call_count")
        private String callCount;
        /**
         * 更新周期，单位 秒
         */
        @JsonProperty("refresh_second")
        private String refreshSecond;
    }

}