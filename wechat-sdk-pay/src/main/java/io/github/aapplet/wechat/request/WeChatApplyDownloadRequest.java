package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.common.WeChatDownload;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.exception.WeChatParamsException;
import io.github.aapplet.wechat.params.WeChatRequestParams;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * <a href="https://pay.weixin.qq.com/doc/v3/merchant/4012084687">下载账单</a>
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class WeChatApplyDownloadRequest implements WeChatRequest.V3Download<WeChatDownload> {

    /**
     * 账单下载地址
     */
    @JsonProperty("download_url")
    private String downloadUrl;

    /**
     * 解析域名
     */
    URL getUrl() {
        try {
            return new URL(downloadUrl);
        } catch (MalformedURLException e) {
            throw new WeChatParamsException("域名解析失败");
        }
    }

    @Override
    public WeChatAttribute<WeChatDownload> getAttribute(WeChatConfig wechatConfig) {
        final URL url = this.getUrl();
        var attribute = new WeChatRequestParams<WeChatDownload>(wechatConfig.getPayDomain());
        attribute.setMethod("GET");
        attribute.setRequestPath(url.getPath());
        attribute.setParameters(url.getQuery());
        attribute.setResponseClass(WeChatDownload.class);
        return attribute;
    }

}