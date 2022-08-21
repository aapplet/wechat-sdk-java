package io.github.aapplet.wechat.base;

public interface WeChatAttribute<T extends WeChatResponse> {

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

    /**
     * 有授权
     */
    boolean hasAuth();

    /**
     * 有Body
     */
    boolean hasBody();

}
