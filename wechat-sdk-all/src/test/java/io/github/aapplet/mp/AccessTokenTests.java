package io.github.aapplet.mp;

import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.token.WeChatAccessTokenManager;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class AccessTokenTests {

    private final WeChatConfig wechatConfig = WeChatConfig.load("config.json");

    /**
     * 经过测试强制刷新20秒内重复请求无效, 20秒后刷新成功
     */
    @Test
    @SneakyThrows
    void getAccessToken() {
        WeChatAccessTokenManager accessTokenManager = wechatConfig.getAccessTokenManager();
        String accessToken1 = accessTokenManager.getAccessToken();
        System.out.println(accessTokenManager.getAccessToken());
        System.out.println(accessTokenManager.getAccessToken());
        System.out.println(accessTokenManager.getAccessToken());
        accessTokenManager.removeAccessToken();
        System.out.println(accessTokenManager.getAccessToken());
        System.out.println(accessTokenManager.getAccessToken());
        System.out.println(accessTokenManager.getAccessToken());
        // TimeUnit.SECONDS.sleep(30);
        accessTokenManager.forceRefresh();
        System.out.println(accessTokenManager.getAccessToken());
        System.out.println(accessTokenManager.getAccessToken());
        System.out.println(accessTokenManager.getAccessToken());
        String accessToken2 = accessTokenManager.getAccessToken();
        assert Objects.equals(accessToken1, accessToken2) : "AccessToken不一致";
    }

}