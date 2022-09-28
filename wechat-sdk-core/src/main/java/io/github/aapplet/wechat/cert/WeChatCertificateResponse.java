package io.github.aapplet.wechat.cert;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.util.WeChatJsonUtil;
import lombok.Data;

import java.util.List;

@Data
public class WeChatCertificateResponse implements WeChatResponse.V3 {

    /**
     * 证书列表
     */
    @JsonProperty("data")
    private List<WeChatCertificate> certificates;

    /**
     * 证书信息
     */
    @Data
    public static class WeChatCertificate {

        /**
         * 序列号
         */
        @JsonProperty("serial_no")
        private String serialNo;
        /**
         * 有效时间
         */
        @JsonProperty("effective_time")
        private String effectiveTime;
        /**
         * 过期时间
         */
        @JsonProperty("expire_time")
        private String expireTime;
        /**
         * 加密证书
         */
        @JsonProperty("encrypt_certificate")
        private EncryptCertificate encryptCertificate;
    }

    /**
     * 加密证书
     */
    @Data
    public static class EncryptCertificate {
        /**
         * 加密算法
         */
        @JsonProperty("algorithm")
        private String algorithm;
        /**
         * 加密使用的随机串初始化向量
         */
        @JsonProperty("nonce")
        private String nonce;
        /**
         * 附加数据包
         */
        @JsonProperty("associated_data")
        private String associatedData;
        /**
         * Base64编码后的密文
         */
        @JsonProperty("ciphertext")
        private String ciphertext;
    }

    /**
     * Json转对象
     */
    public static WeChatCertificateResponse fromJson(byte[] bytes) {
        return WeChatJsonUtil.fromJson(bytes, WeChatCertificateResponse.class);
    }

}