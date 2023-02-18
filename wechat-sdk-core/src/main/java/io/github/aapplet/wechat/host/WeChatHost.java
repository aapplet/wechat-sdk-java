package io.github.aapplet.wechat.host;

/**
 * 域名分配,失败重试,容灾检测
 */
public interface WeChatHost {

    /**
     * 获取域名
     *
     * @return 域名
     */
    String getHost();

    /**
     * 请求失败重试
     *
     * @return 是否重试
     */
    boolean retry();

    /**
     * 容灾检测
     */
    void disaster();

}