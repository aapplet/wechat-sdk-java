package io.github.aapplet.wechat.base;

import io.github.aapplet.wechat.config.WeChatConfig;

/**
 * 请求
 *
 * @param <T> 响应类型
 */
public interface WeChatRequest<T extends WeChatResponse> {

    /**
     * 获取属性
     *
     * @param wechatConfig 配置信息
     * @return 请求属性
     */
    WeChatAttribute<T> getAttribute(WeChatConfig wechatConfig);

    /**
     * 微信支付V3请求
     *
     * @param <T> 响应类型
     */
    interface V3<T extends WeChatResponse.V3> extends WeChatRequest<T> {
    }

    /**
     * 公众平台请求
     *
     * @param <T> 响应类型
     */
    interface MP<T extends WeChatResponse.MP> extends WeChatRequest<T> {
    }

    /**
     * 微信支付V3文件下载
     *
     * @param <T> 响应类型
     */
    interface V3Download<T extends WeChatResponse.Download> extends WeChatRequest<T> {
    }

    /**
     * 公众平台文件下载
     *
     * @param <T> 响应类型
     */
    interface MPDownload<T extends WeChatResponse.Download> extends WeChatRequest<T> {
    }

}