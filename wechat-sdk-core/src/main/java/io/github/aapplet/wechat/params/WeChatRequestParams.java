package io.github.aapplet.wechat.params;

import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatDomain;
import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.host.WeChatDomainAllocator;
import io.github.aapplet.wechat.host.WeChatDomainManager;
import lombok.Data;

/**
 * 请求属性，定义了与微信请求相关的属性和方法。
 *
 * @param <R> 代表响应类型的泛型参数，必须是 WeChatResponse 的子类型。
 */
@Data
public class WeChatRequestParams<R extends WeChatResponse> implements WeChatAttribute<R> {

    /**
     * 域名分配器，用于动态分配请求的域名。
     */
    private final WeChatDomainAllocator domainAllocator;

    /**
     * @param wechatDomain 域名信息
     */
    public WeChatRequestParams(WeChatDomain wechatDomain) {
        this.domainAllocator = new WeChatDomainManager(wechatDomain);
    }

    /**
     * 请求方法的字符串表示
     */
    private String method;
    /**
     * 请求路径, 紧接域名之后的部分
     */
    private String requestPath;
    /**
     * 查询参数, 路径问号后的内容
     */
    private String parameters;
    /**
     * 请求体内容
     */
    private String requestBody;
    /**
     * 响应类的Class对象
     */
    private Class<R> responseClass;

    @Override
    public String getPathParams() {
        return parameters == null ? requestPath : requestPath + "?" + parameters;
    }

    @Override
    public String getRequestURL() {
        return domainAllocator.getAvailableDomain() + getPathParams();
    }

}