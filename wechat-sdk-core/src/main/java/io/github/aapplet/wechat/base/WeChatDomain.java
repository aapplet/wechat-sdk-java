package io.github.aapplet.wechat.base;

import java.util.concurrent.atomic.AtomicBoolean;

public interface WeChatDomain {

    /**
     * @return 主域名
     */
    String getPrimaryDomain();

    /**
     * @return 备用域名
     */
    String getAlternateDomain();

    /**
     * @return 健康检查地址
     */
    String getHealthCheckUrl();

    /**
     * @return 主域名状态
     */
    AtomicBoolean getDomainStatus();

}