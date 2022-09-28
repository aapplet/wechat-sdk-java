package io.github.aapplet.wechat.attribute;

import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.host.WeChatHost;
import io.github.aapplet.wechat.host.WeChatHostAllocate;
import io.github.aapplet.wechat.host.WeChatHostStorage;
import lombok.Data;
import lombok.Getter;

/**
 * 请求属性
 *
 * @param <T> 响应类型
 */
@Data
public abstract class AbstractAttribute<T extends WeChatResponse> implements WeChatAttribute<T> {

    /**
     * 域名分配
     */
    @Getter
    private final WeChatHost weChatHost;

    /**
     * @param hostStorage 域名信息
     */
    public AbstractAttribute(WeChatHostStorage hostStorage) {
        this.weChatHost = new WeChatHostAllocate(hostStorage);
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
     * 请求参数
     */
    private String parameters;
    /**
     * 请求体
     */
    private String requestBody;
    /**
     * 响应class
     */
    private Class<T> responseClass;

    /**
     * 请求资源(路径+参数)
     */
    @Override
    public String getRequestURI() {
        return parameters == null ? requestPath : requestPath + "?" + parameters;
    }

    /**
     * 请求URL(域名+路径+参数)
     */
    @Override
    public String getRequestURL() {
        return weChatHost.getHost() + getRequestURI();
    }

}
