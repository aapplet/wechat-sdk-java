package io.github.aapplet.wechat.base;

import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.response.WeChatDownload;

/**
 * 客户端
 */
public interface WeChatClient {

    /**
     * 获取配置
     */
    WeChatConfig getWeChatConfig();

    /**
     * 微信支付V3请求
     *
     * @param request 请求参数
     * @param <T>     响应类型
     * @return HTTP响应
     */
    <T extends WeChatResponse.V3> T execute(WeChatRequest.V3<T> request);

    /**
     * 公众平台请求
     *
     * @param request 请求参数
     * @param <T>     响应类型
     * @return HTTP响应
     */
    <T extends WeChatResponse.MP> T execute(WeChatRequest.MP<T> request);

    /**
     * 微信支付V3文件下载
     *
     * @param download 下载参数
     * @return bytes处理
     */
    WeChatDownload execute(WeChatRequest.V3Download<WeChatDownload> download);

    /**
     * 公众平台文件下载
     *
     * @param download 下载参数
     * @return bytes处理
     */
    WeChatDownload execute(WeChatRequest.MPDownload<WeChatDownload> download);

}
