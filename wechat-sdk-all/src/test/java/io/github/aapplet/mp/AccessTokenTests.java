package io.github.aapplet.mp;

import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.token.WeChatAccessTokenManager;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class AccessTokenTests {

    private final WeChatConfig wechatConfig = WeChatConfig.load("config.json");

    /**
     * 经过测试强制刷新20秒内重复请求无效, 20秒后刷新成功
     */
    @Test
    @SneakyThrows
    void forceRefresh() {
        WeChatAccessTokenManager accessTokenManager = wechatConfig.getAccessTokenManager();
        System.out.println(accessTokenManager.getAccessToken());
        System.out.println(accessTokenManager.getAccessToken());
        System.out.println(accessTokenManager.getAccessToken());
        accessTokenManager.forceRefresh();
        System.out.println(accessTokenManager.getAccessToken());
        System.out.println(accessTokenManager.getAccessToken());
        System.out.println(accessTokenManager.getAccessToken());
        // TimeUnit.SECONDS.sleep(30);
        accessTokenManager.forceRefresh();
        System.out.println(accessTokenManager.getAccessToken());
        System.out.println(accessTokenManager.getAccessToken());
        System.out.println(accessTokenManager.getAccessToken());
    }

}