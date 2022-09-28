package io.github.aapplet.wechat.token;

import io.github.aapplet.wechat.DefaultWeChatClient;
import io.github.aapplet.wechat.base.WeChatClient;
import io.github.aapplet.wechat.config.WeChatConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AccessToken管理器
 * <p>
 * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-access-token/getAccessToken.html
 */
public class WeChatAccessTokenManager {

    /**
     * key   = appId
     * value = AccessToken
     */
    private static final Map<String, WeChatAccessToken> ACCESS_TOKEN_MAP = new ConcurrentHashMap<>(4);
    /**
     * 配置信息
     */
    private final WeChatConfig weChatConfig;

    /**
     * @param weChatConfig 配置信息
     */
    private WeChatAccessTokenManager(WeChatConfig weChatConfig) {
        this.weChatConfig = weChatConfig;
    }

    /**
     * 获取AccessToken
     */
    private String getAccessToken() {
        String appId = weChatConfig.getAppId();
        WeChatAccessToken accessToken = ACCESS_TOKEN_MAP.get(appId);
        if (accessToken == null) {
            synchronized (WeChatAccessTokenManager.class) {
                accessToken = ACCESS_TOKEN_MAP.get(appId);
                if (accessToken == null) {
                    accessToken = refreshAccessToken();
                }
            }
        }
        if (accessToken.validate()) {
            synchronized (WeChatAccessTokenManager.class) {
                accessToken = ACCESS_TOKEN_MAP.get(appId);
                if (accessToken.validate()) {
                    accessToken = refreshAccessToken();
                }
            }
        }
        return accessToken.getAccessToken();
    }

    /**
     * 刷新AccessToken
     * <p>
     * token过期时间提前三分钟,防止时间差异
     */
    private WeChatAccessToken refreshAccessToken() {
        final WeChatClient weChatClient = new DefaultWeChatClient(weChatConfig);
        final WeChatAccessTokenResponse response = weChatClient.execute(new WeChatAccessTokenRequest());
        final long currentTimeMillis = System.currentTimeMillis();
        WeChatAccessToken accessToken = new WeChatAccessToken();
        accessToken.setAccessToken(response.getAccessToken());
        accessToken.setCreateTimestamp(currentTimeMillis);
        accessToken.setExpireTimestamp(currentTimeMillis + (response.getExpiresIn() - 60 * 3) * 1000);
        ACCESS_TOKEN_MAP.put(weChatConfig.getAppId(), accessToken);
        return accessToken;
    }

    /**
     * 删除AccessToken
     * <p>
     * 只删除创建时间大于三分钟的凭证
     */
    private void removeAccessToken() {
        final String appId = weChatConfig.getAppId();
        final WeChatAccessToken accessToken = ACCESS_TOKEN_MAP.get(appId);
        synchronized (ACCESS_TOKEN_MAP) {
            if (accessToken != null && accessToken.pastTime() > 1000 * 60 * 3) {
                ACCESS_TOKEN_MAP.remove(appId, accessToken);
            }
        }
    }

    /**
     * 获取AccessToken
     *
     * @param weChatConfig 配置信息
     * @return AccessToken
     */
    public static String getAccessToken(WeChatConfig weChatConfig) {
        return new WeChatAccessTokenManager(weChatConfig).getAccessToken();
    }

    /**
     * 删除AccessToken
     *
     * @param weChatConfig 配置信息
     */
    public static void removeAccessToken(WeChatConfig weChatConfig) {
        new WeChatAccessTokenManager(weChatConfig).removeAccessToken();
    }

}