package io.github.aapplet.wechat.host;

/**
 * 域名分配,失败重试,容灾检测
 */
public class WeChatHostAttribute implements WeChatHost {

    /**
     * 重试状态
     */
    private boolean retry = false;
    /**
     * 主域名状态
     */
    private final boolean available;
    /**
     * 域名信息
     */
    private final WeChatHostStorage hostStorage;

    /**
     * 初始化
     *
     * @param hostStorage 域名信息
     */
    public WeChatHostAttribute(WeChatHostStorage hostStorage) {
        this.available = hostStorage.isAvailable();
        this.hostStorage = hostStorage;
    }

    /**
     * 获取域名
     *
     * @return 域名
     */
    @Override
    public String getHost() {
        return (available ^ retry) ? hostStorage.getMaster() : hostStorage.getSlave();
    }

    /**
     * 请求失败重试
     *
     * @return 是否重试
     */
    @Override
    public boolean retry() {
        return !retry && (retry = true);
    }

    /**
     * 容灾检测
     */
    @Override
    public void disaster() {
        hostStorage.getDisaster().disaster();
    }

}
