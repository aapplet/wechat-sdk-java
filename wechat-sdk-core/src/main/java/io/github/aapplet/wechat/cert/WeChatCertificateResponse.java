package io.github.aapplet.wechat.cert;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.util.WeChatJacksonUtil;
import lombok.Data;

import java.util.List;

@Data
public class WeChatCertificateResponse implements WeChatResponse.V3 {

    /**
     * 证书列表
     */
    @JsonProperty("data")
    private List<Item> data;

    /**
     * 证书信息
     */
    @Data
    public static class Item {

        /**
         * 【证书序列号】 平台证书的主键，唯一定义此资源的标识
         */
        @JsonProperty("serial_no")
        private String serialNo;
        /**
         * 【证书启用时间】 启用证书的时间，时间格式为RFC3339。每个平台证书的启用时间是固定的。
         */
        @JsonProperty("effective_time")
        private String effectiveTime;
        /**
         * 【证书弃用时间】 弃用证书的时间，时间格式为RFC3339。更换平台证书前，会提前24小时修改老证书的弃用时间，接口返回新老两个平台证书。更换完成后，接口会返回最新的平台证书。
         */
        @JsonProperty("expire_time")
        private String expireTime;
        /**
         * 【证书信息】 证书内容
         */
        @JsonProperty("encrypt_certificate")
        private EncryptCertificate encryptCertificate;
    }

    /**
     * 【证书信息】 证书内容
     */
    @Data
    public static class EncryptCertificate {
        /**
         * 【加密证书的随机串】 对应到加密算法中的IV。
         */
        @JsonProperty("nonce")
        private String nonce;
        /**
         * 【加密证书的算法】 对开启结果数据进行加密的加密算法，目前只支持AEAD_AES_256_GCM。
         */
        @JsonProperty("algorithm")
        private String algorithm;
        /**
         * 【加密后的证书内容】 使用API KEY和上述参数，可以解密出平台证书的明文。证书明文为PEM格式。（注意：更换证书时会出现PEM格式中的证书失效时间与接口返回的证书弃用时间不一致的情况）
         */
        @JsonProperty("ciphertext")
        private String ciphertext;
        /**
         * 【加密证书的附加数据】 加密证书的附加数据，固定为“certificate"。
         */
        @JsonProperty("associated_data")
        private String associatedData;
    }

    /**
     * @param bytes 证书数据
     * @return 证书数据实例
     */
    public static WeChatCertificateResponse fromJson(byte[] bytes) {
        return WeChatJacksonUtil.fromJson(bytes, WeChatCertificateResponse.class);
    }

}