package io.github.aapplet.wechat.params;

import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatDomain;
import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.host.WeChatDomainAllocator;
import io.github.aapplet.wechat.host.WeChatDomainManager;
import lombok.Data;

/**
 * 请求属性
 *
 * @param <R> 响应类型
 */
@Data
public class WeChatRequestParams<R extends WeChatResponse> implements WeChatAttribute<R> {

    /**
     * 域名分配
     */
    private final WeChatDomainAllocator domainAllocator;

    /**
     * @param wechatDomain 域名信息
     */
    public WeChatRequestParams(WeChatDomain wechatDomain) {
        this.domainAllocator = new WeChatDomainManager(wechatDomain);
    }

    /**
     * 请求方法
     */
    private String method;
    /**
     * 请求路径
     */
    private String requestPath;
    /**
     * 路径参数
     */
    private String parameters;
    /**
     * 包体参数
     */
    private String requestBody;
    /**
     * 响应class
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