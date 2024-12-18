package io.github.aapplet.wechat.base;

import io.github.aapplet.wechat.config.WeChatConfig;

/**
 * 请求
 *
 * @param <R> 响应类型
 */
public interface WeChatRequest<R extends WeChatResponse> {

    /**
     * 获取请求属性
     *
     * @param wechatConfig 配置信息
     * @return 请求属性
     */
    WeChatAttribute<R> getAttribute(WeChatConfig wechatConfig);

    /**
     * 微信支付请求
     *
     * @param <R> 响应类型
     */
    interface V3<R extends WeChatResponse.V3> extends WeChatRequest<R> {
    }

    /**
     * 公众平台请求
     *
     * @param <R> 响应类型
     */
    interface MP<R extends WeChatResponse.MP> extends WeChatRequest<R> {
    }

    /**
     * 微信支付文件下载
     *
     * @param <R> 响应类型
     */
    interface V3Download<R extends WeChatResponse.Download> extends WeChatRequest<R> {
    }

    /**
     * 公众平台文件下载
     *
     * @param <R> 响应类型
     */
    interface MPDownload<R extends WeChatResponse.Download> extends WeChatRequest<R> {
    }

}