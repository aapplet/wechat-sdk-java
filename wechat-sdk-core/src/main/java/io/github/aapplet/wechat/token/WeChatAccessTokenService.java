package io.github.aapplet.wechat.token;

import io.github.aapplet.wechat.DefaultWeChatClient;
import io.github.aapplet.wechat.base.WeChatClient;
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
    private final WeChatConfig weChatConfig;

    /**
     * key   = appId
     * value = AccessToken
     */
    private static final Map<String, WeChatAccessToken> ACCESS_TOKENS = new ConcurrentHashMap<>(4);

    /**
     * 刷新AccessToken
     *
     * @return AccessToken
     */
    private WeChatAccessToken refreshAccessToken() {
        final WeChatClient weChatClient = new DefaultWeChatClient(weChatConfig);
        final WeChatAccessTokenResponse response = weChatClient.execute(new WeChatAccessTokenRequest());
        final long currentTimeMillis = System.currentTimeMillis();
        WeChatAccessToken accessToken = new WeChatAccessToken();
        accessToken.setAccessToken(response.getAccessToken());
        accessToken.setExpiresIn(response.getExpiresIn());
        accessToken.setCreateTimestamp(currentTimeMillis);
        accessToken.setExpireTimestamp(currentTimeMillis + (response.getExpiresIn() - 60 * 3) * 1000);
        ACCESS_TOKENS.put(weChatConfig.getAppId(), accessToken);
        return accessToken;
    }

    /**
     * 获取AccessToken
     */
    @Override
    public String getAccessToken() {
        String appId = weChatConfig.getAppId();
        WeChatAccessToken accessToken = ACCESS_TOKENS.get(appId);
        if (accessToken == null || accessToken.isExpired()) {
            synchronized (WeChatAccessTokenManager.class) {
                accessToken = ACCESS_TOKENS.get(appId);
                if (accessToken == null || accessToken.isExpired()) {
                    accessToken = refreshAccessToken();
                }
            }
        }
        return accessToken.getAccessToken();
    }

    /**
     * 删除AccessToken
     * <p>
     * 只删除创建时间大于三十秒的凭证
     */
    @Override
    public void removeAccessToken() {
        final String appId = weChatConfig.getAppId();
        final WeChatAccessToken accessToken = ACCESS_TOKENS.get(appId);
        if (accessToken != null && accessToken.pastTime() > 30 * 1000) {
            ACCESS_TOKENS.remove(appId, accessToken);
        }
    }

}