package io.github.aapplet.wechat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.request.WeChatApplyDownloadRequest;
import lombok.Data;

@Data
public class WeChatApplyBillResponse implements WeChatResponse.V3 {

    /**
     * 【哈希类型】哈希类型
     * <li>SHA1: Secure Hash Algorithm 1</li>
     */
    @JsonProperty("hash_type")
    private String hashType;
    /**
     * 【哈希值】原始账单（gzip需要解压缩）的摘要值，用于校验文件的完整性
     */
    @JsonProperty("hash_value")
    private String hashValue;
    /**
     * 【下载地址】供下一步请求账单文件的下载地址，该地址5min内有效。
     */
    @JsonProperty("download_url")
    private String downloadUrl;

    /**
     * @return 下载账单Request
     */
    public WeChatApplyDownloadRequest download() {
        return new WeChatApplyDownloadRequest().setDownloadUrl(downloadUrl);
    }

}