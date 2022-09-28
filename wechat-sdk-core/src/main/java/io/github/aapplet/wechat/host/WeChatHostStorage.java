package io.github.aapplet.wechat.host;

import lombok.Getter;

@Getter
public enum WeChatHostStorage {

    /**
     * 商户平台
     */
    PAY("https://api.mch.weixin.qq.com", "https://api2.mch.weixin.qq.com"),
    /**
     * 公众平台
     */
    MP("https://api.weixin.qq.com", "https://api2.weixin.qq.com");

    /**
     * 主域名
     */
    private final String master;
    /**
     * 备用域名
     */
    private final String slave;
    /**
     * 跨城容灾
     */
    private final WeChatHostDisaster disaster;
    /**
     * 主域名状态
     */
    private volatile boolean available = true;

    /**
     * @param master 主域名
     * @param slave  备用域名
     */
    WeChatHostStorage(String master, String slave) {
        this.master = master;
        this.slave = slave;
        this.disaster = new WeChatHostDisaster(this);
    }

    /**
     * 主域名状态切换
     *
     * @param available 主域名是否可用
     */
    void setAvailable(boolean available) {
        this.available = available;
    }

}