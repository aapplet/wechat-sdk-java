package io.github.aapplet.wechat.cert;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.util.WeChatJacksonUtil;
import lombok.Data;

import java.util.List;

@Data
public class WeChatCertificateResponse implements WeChatResponse.V3 {

    /**
     * 证书数据
     */
    @JsonProperty("data")
    private List<Item> data;

    /**
     * 平台证书的详情
     */
    @Data
    public static class Item {
        /**
         * 证书序列号
         */
        @JsonProperty("serial_no")
        private String serialNo;
        /**
         * 证书启用时间
         */
        @JsonProperty("effective_time")
        private String effectiveTime;
        /**
         * 证书弃用时间
         */
        @JsonProperty("expire_time")
        private String expireTime;
        /**
         * 证书信息
         */
        @JsonProperty("encrypt_certificate")
        private EncryptCertificate encryptCertificate;
    }

    /**
     * 证书信息
     */
    @Data
    public static class EncryptCertificate {
        /**
         * 加密证书的算法
         */
        @JsonProperty("algorithm")
        private String algorithm;
        /**
         * 加密证书的随机串
         */
        @JsonProperty("nonce")
        private String nonce;
        /**
         * 加密证书的附加数据
         */
        @JsonProperty("associated_data")
        private String associatedData;
        /**
         * 加密后的证书内容
         */
        @JsonProperty("ciphertext")
        private String ciphertext;
    }

    /**
     * @param bytes 证书JSON数据
     * @return 证书响应实例
     */
    public static WeChatCertificateResponse fromJson(byte[] bytes) {
        return WeChatJacksonUtil.fromJson(bytes, WeChatCertificateResponse.class);
    }

}