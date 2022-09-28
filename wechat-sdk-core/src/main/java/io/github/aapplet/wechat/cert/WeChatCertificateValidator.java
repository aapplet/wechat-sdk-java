package io.github.aapplet.wechat.cert;

import io.github.aapplet.wechat.WeChatValidator;
import io.github.aapplet.wechat.config.WeChatConfig;

import java.net.http.HttpResponse;

/**
 * 平台证书验证器
 */
class WeChatCertificateValidator extends WeChatValidator {

    /**
     * @param weChatConfig 配置信息
     * @param httpResponse HTTP响应
     */
    WeChatCertificateValidator(WeChatConfig weChatConfig, HttpResponse<byte[]> httpResponse) {
        super(weChatConfig, httpResponse);
    }

}