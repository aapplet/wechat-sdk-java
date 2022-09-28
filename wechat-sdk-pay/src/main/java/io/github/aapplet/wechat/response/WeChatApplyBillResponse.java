package io.github.aapplet.wechat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.request.WeChatApplyDownloadRequest;
import lombok.Data;

@Data
public class WeChatApplyBillResponse implements WeChatResponse.V3 {

    /**
     * 哈希类型
     */
    @JsonProperty("hash_type")
    private String hashType;
    /**
     * 哈希值
     */
    @JsonProperty("hash_value")
    private String hashValue;
    /**
     * 账单下载地址
     */
    @JsonProperty("download_url")
    private String downloadUrl;

    /**
     * 下载账单
     */
    public WeChatApplyDownloadRequest download() {
        return new WeChatApplyDownloadRequest().setDownloadUrl(downloadUrl);
    }

}