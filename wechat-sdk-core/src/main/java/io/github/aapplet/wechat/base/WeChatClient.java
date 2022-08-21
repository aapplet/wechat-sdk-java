package io.github.aapplet.wechat.base;

public interface WeChatClient {

    <T extends WeChatResponse> T execute(WeChatRequest<T> request);

}
