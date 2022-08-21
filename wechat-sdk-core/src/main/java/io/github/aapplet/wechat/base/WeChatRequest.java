package io.github.aapplet.wechat.base;

import io.github.aapplet.wechat.config.WeChatConfig;

public interface WeChatRequest<T extends WeChatResponse> {

    WeChatAttribute<T> getAttribute(WeChatConfig weChatConfig);

}
