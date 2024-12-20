package io.github.aapplet.wechat.base;

import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.response.WeChatDownload;

/**
 * 客户端
 */
public interface WeChatClient {

    /**
     * 获取配置
     *
     * @return 配置信息
     */
    WeChatConfig getWechatConfig();

    /**
     * 微信支付V3请求
     *
     * @param request 请求参数
     * @param <R>     响应类型
     * @return HTTP响应
     */
    <R extends WeChatResponse.V3> R execute(WeChatRequest.V3<R> request);

    /**
     * 公众平台请求
     *
     * @param request 请求参数
     * @param <R>     响应类型
     * @return HTTP响应
     */
    <R extends WeChatResponse.MP> R execute(WeChatRequest.MP<R> request);

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