package io.github.aapplet.wechat.base;

import io.github.aapplet.wechat.host.WeChatDomainAllocator;

/**
 * 请求属性接口，定义了与微信请求相关的属性和方法。
 *
 * @param <R> 代表响应类型的泛型参数，必须是 WeChatResponse 的子类型。
 */
public interface WeChatAttribute<R extends WeChatResponse> {

    /**
     * 获取域名分配器，用于动态分配请求的域名。
     *
     * @return WeChatDomainAllocator 对象，用于域名的选择与分配。
     */
    WeChatDomainAllocator getDomainAllocator();

    /**
     * 获取请求的方法类型，如 GET、POST 等。
     *
     * @return 请求方法的字符串表示。
     */
    String getMethod();

    /**
     * 获取请求路径和查询参数的组合字符串。
     * 请求路径是 URL 中紧接域名之后的部分，查询参数则是路径问号后的内容。
     *
     * @return 请求路径与查询参数的组合字符串。
     */
    String getPathParams();

    /**
     * 获取完整的请求 URL，包括请求域名、请求路径和查询参数。
     *
     * @return 完整的请求 URL。
     */
    String getRequestURL();

    /**
     * 获取请求体内容，通常用于 POST、PUT 等方法的请求数据。
     *
     * @return 请求体的字符串表示。
     */
    String getRequestBody();

    /**
     * 获取响应类的 Class 对象。
     *
     * @return 响应类的 Class 对象。
     */
    Class<R> getResponseClass();

}