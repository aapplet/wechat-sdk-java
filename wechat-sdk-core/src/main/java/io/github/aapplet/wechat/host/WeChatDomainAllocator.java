package io.github.aapplet.wechat.host;

import io.github.aapplet.wechat.config.WeChatConfig;

/**
 * 域名分配器
 */
public interface WeChatDomainAllocator {

    /**
     * 获取可用域名
     *
     * @return 域名
     */
    String getAvailableDomain();

    /**
     * 请求重试
     *
     * @return 是否重试
     */
    boolean requestRetry();

    /**
     * 主域名健康检查
     *
     * @param wechatConfig 配置信息
     */
    void healthCheck(WeChatConfig wechatConfig);

}