package io.github.aapplet.wechat.base;

import io.github.aapplet.wechat.host.WeChatHost;

/**
 * 请求属性
 *
 * @param <T> 响应类型
 */
public interface WeChatAttribute<T extends WeChatResponse> {

    /**
     * 域名信息
     */
    WeChatHost getWechatHost();

    /**
     * 请求方法
     */
    String getMethod();

    /**
     * 请求资源(路径+参数)
     */
    String getRequestURI();

    /**
     * 请求URL(域名+路径+参数)
     */
    String getRequestURL();

    /**
     * 请求体
     */
    String getRequestBody();

    /**
     * 响应class
     */
    Class<T> getResponseClass();

}