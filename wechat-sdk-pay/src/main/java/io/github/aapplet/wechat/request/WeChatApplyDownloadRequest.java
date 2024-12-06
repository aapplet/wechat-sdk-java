package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.attribute.AbstractAttribute;
import io.github.aapplet.wechat.attribute.WeChatPaymentAttribute;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.common.WeChatDownload;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.exception.WeChatParamsException;
import lombok.Data;
import lombok.experimental.Accessors;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * 下载账单API
 * <p>
 * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_8.shtml
 */
@Data
@Accessors(chain = true)
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
        AbstractAttribute<WeChatDownload> attribute = new WeChatPaymentAttribute<>();
        attribute.setMethod("GET");
        attribute.setRequestPath(url.getPath());
        attribute.setParameters(url.getQuery());
        attribute.setResponseClass(WeChatDownload.class);
        return attribute;
    }

}