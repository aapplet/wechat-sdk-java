package io.github.aapplet.wechat.attribute;

import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.host.WeChatHostStorage;

/**
 * 微信支付属性
 *
 * @param <T>响应类型
 */
public final class WeChatPaymentAttribute<T extends WeChatResponse> extends AbstractAttribute<T> {

    public WeChatPaymentAttribute() {
        super(WeChatHostStorage.PAY);
    }

}