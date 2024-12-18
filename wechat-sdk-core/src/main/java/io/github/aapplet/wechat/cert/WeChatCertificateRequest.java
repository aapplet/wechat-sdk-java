package io.github.aapplet.wechat.cert;

import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.params.WeChatRequestParams;

/**
 * <a href="https://pay.weixin.qq.com/doc/v3/merchant/4012551764">下载平台证书</a>
 */
public class WeChatCertificateRequest implements WeChatRequest.V3<WeChatCertificateResponse> {

    @Override
    public WeChatAttribute<WeChatCertificateResponse> getAttribute(WeChatConfig wechatConfig) {
        var attribute = new WeChatRequestParams<WeChatCertificateResponse>(wechatConfig.getPayDomain());
        attribute.setMethod("GET");
        attribute.setRequestPath("/v3/certificates");
        attribute.setResponseClass(WeChatCertificateResponse.class);
        return attribute;
    }

}