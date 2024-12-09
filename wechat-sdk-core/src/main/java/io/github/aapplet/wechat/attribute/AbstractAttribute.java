package io.github.aapplet.wechat.attribute;

import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.host.WeChatHost;
import io.github.aapplet.wechat.host.WeChatHostAttribute;
import io.github.aapplet.wechat.host.WeChatHostStorage;
import lombok.Data;

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
    private final WeChatHost wechatHost;

    /**
     * @param hostStorage 域名信息
     */
    public AbstractAttribute(WeChatHostStorage hostStorage) {
        this.wechatHost = new WeChatHostAttribute(hostStorage);
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
     * @return 请求资源(路径 + 参数)
     */
    @Override
    public String getRequestURI() {
        return parameters == null ? requestPath : requestPath + "?" + parameters;
    }

    /**
     * @return 请求URL(域名 + 路径 + 参数)
     */
    @Override
    public String getRequestURL() {
        return wechatHost.getHost() + getRequestURI();
    }

}
