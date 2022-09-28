package io.github.aapplet.wechat.attribute;

import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.host.WeChatHostStorage;

/**
 * 公众平台属性
 *
 * @param <T> 响应类型
 */
public final class WeChatPlatformAttribute<T extends WeChatResponse> extends AbstractAttribute<T> {

    public WeChatPlatformAttribute() {
        super(WeChatHostStorage.MP);
    }

}