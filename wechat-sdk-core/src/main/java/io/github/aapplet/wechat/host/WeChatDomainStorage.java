package io.github.aapplet.wechat.host;

import io.github.aapplet.wechat.base.WeChatDomain;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public enum WeChatDomainStorage implements WeChatDomain {

    /**
     * 公众平台域名配置
     */
    MP("http://127.0.0.1", "https://api2.weixin.qq.com", "https://api.weixin.qq.com/sns/jscode2session"),
    /**
     * 商户平台域名配置
     */
    PAY("https://api.mch.weixin.qq.com", "https://api2.mch.weixin.qq.com", "https://api.mch.weixin.qq.com/v3/certificates");

    /**
     * 主域名
     */
    private final String primaryDomain;
    /**
     * 备用域名
     */
    private final String alternateDomain;
    /**
     * 健康检查地址
     */
    private final String healthCheckUrl;
    /**
     * 主域名状态
     */
    private final AtomicBoolean domainStatus = new AtomicBoolean(true);

    /**
     * 初始化域名相关配置信息
     *
     * @param primaryDomain   主域名
     * @param alternateDomain 备用域名
     * @param healthCheckUrl  健康检查地址
     */
    WeChatDomainStorage(String primaryDomain, String alternateDomain, String healthCheckUrl) {
        this.primaryDomain = primaryDomain;
        this.alternateDomain = alternateDomain;
        this.healthCheckUrl = healthCheckUrl;
    }

}