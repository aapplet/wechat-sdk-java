package io.github.aapplet.wechat.base;

import io.github.aapplet.wechat.host.WeChatDomainAllocator;

/**
 * 请求属性
 *
 * @param <R> 响应类型
 */
public interface WeChatAttribute<R extends WeChatResponse> {

    /**
     * @return 域名信息
     */
    WeChatDomainAllocator getDomainAllocator();

    /**
     * @return 请求方法
     */
    String getMethod();

    /**
     * @return (请求路径 + 路径参数)
     */
    String getPathParams();

    /**
     * @return (请求域名 + 请求路径 + 路径参数)
     */
    String getRequestURL();

    /**
     * @return 包体参数
     */
    String getRequestBody();

    /**
     * @return 响应class
     */
    Class<R> getResponseClass();

}