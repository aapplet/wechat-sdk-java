package io.github.aapplet.wechat.token;

import io.github.aapplet.wechat.DefaultWeChatClient;
import io.github.aapplet.wechat.config.WeChatConfig;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <a href="https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-access-token/getStableAccessToken.html">获取稳定版接口调用凭据</a>
 */
@RequiredArgsConstructor
public class WeChatAccessTokenService implements WeChatAccessTokenManager {

    /**
     * 配置信息
     */
    private final WeChatConfig wechatConfig;

    /**
     * key   = appId
     * value = AccessToken
     */
    private static final Map<String, WeChatAccessToken> ACCESS_TOKENS = new ConcurrentHashMap<>(2);

    /**
     * 刷新AccessToken
     *
     * @return AccessToken
     */
    private WeChatAccessToken refreshAccessToken() {
        var client = new DefaultWeChatClient(wechatConfig);
        var response = client.execute(new WeChatAccessTokenRequest());
        var accessToken = new WeChatAccessToken();
        var currentTimeMillis = System.currentTimeMillis();
        accessToken.setAccessToken(response.getAccessToken());
        accessToken.setCreateTimestamp(currentTimeMillis);
        accessToken.setExpireTimestamp(currentTimeMillis + (response.getExpiresIn() - 60 * 3) * 1000);
        ACCESS_TOKENS.put(wechatConfig.getAppId(), accessToken);
        return accessToken;
    }

    @Override
    public String getAccessToken() {
        var appId = wechatConfig.getAppId();
        var accessToken = ACCESS_TOKENS.get(appId);
        if (accessToken == null || accessToken.isExpired()) {
            synchronized (wechatConfig) {
                accessToken = ACCESS_TOKENS.get(appId);
                if (accessToken == null || accessToken.isExpired()) {
                    accessToken = refreshAccessToken();
                }
            }
        }
        return accessToken.getAccessToken();
    }

    @Override
    public void removeAccessToken() {
        var appId = wechatConfig.getAppId();
        var accessToken = ACCESS_TOKENS.get(appId);
        // 强制刷新20秒内重复使用无效,所以获取到的AccessToken至少能使用20秒
        if (accessToken != null && accessToken.duration() > 15 * 1000) {
            ACCESS_TOKENS.remove(appId, accessToken);
        }
    }

}