package io.github.aapplet.wechat.notify;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.util.WeChatJacksonUtil;
import lombok.Data;

/**
 * 回调通知
 */
@Data
public class WeChatNotify {

    /**
     * 通知ID
     */
    @JsonProperty("id")
    private String id;
    /**
     * 通知创建时间
     */
    @JsonProperty("create_time")
    private String createTime;
    /**
     * 通知类型
     */
    @JsonProperty("event_type")
    private String eventType;
    /**
     * 通知数据类型
     */
    @JsonProperty("resource_type")
    private String resourceType;
    /**
     * 回调摘要
     */
    @JsonProperty("summary")
    private String summary;
    /**
     * 通知数据
     */
    @JsonProperty("resource")
    private Resource resource;

    @Data
    public static class Resource {
        /**
         * 加密算法类型
         */
        @JsonProperty("algorithm")
        private String algorithm;
        /**
         * 数据密文
         */
        @JsonProperty("ciphertext")
        private String ciphertext;
        /**
         * 附加数据
         */
        @JsonProperty("associated_data")
        private String associatedData;
        /**
         * 随机串
         */
        @JsonProperty("nonce")
        private String nonce;
        /**
         * 加密前的对象类型
         */
        @JsonProperty("original_type")
        private String originalType;
    }

    /**
     * @param body 回调通知数据
     * @return 回调通知对象
     */
    public static WeChatNotify fromJson(String body) {
        return WeChatJacksonUtil.fromJson(body, WeChatNotify.class);
    }

}