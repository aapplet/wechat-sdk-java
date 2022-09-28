package io.github.aapplet.wechat.cert;

import io.github.aapplet.wechat.attribute.AbstractAttribute;
import io.github.aapplet.wechat.attribute.WeChatPayAttribute;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;

/**
 * 获取平台证书
 * <p>
 * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/wechatpay5_1.shtml
 */
public class WeChatCertificateRequest implements WeChatRequest.V3<WeChatCertificateResponse> {

    @Override
    public WeChatAttribute<WeChatCertificateResponse> getAttribute(WeChatConfig weChatConfig) {
        AbstractAttribute<WeChatCertificateResponse> attribute = new WeChatPayAttribute<>();
        attribute.setMethod("GET");
        attribute.setRequestPath("/v3/certificates");
        attribute.setResponseClass(WeChatCertificateResponse.class);
        return attribute;
    }

}