package io.github.aapplet.wechat.token;

import io.github.aapplet.wechat.DefaultWeChatClient;
import io.github.aapplet.wechat.base.WeChatClient;
import io.github.aapplet.wechat.config.WeChatConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AccessToken服务
 * <p>
 * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-access-token/getAccessToken.html
 */
public class WeChatAccessTokenService implements WeChatAccessTokenManager {

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
    public WeChatAccessTokenService(WeChatConfig weChatConfig) {
        this.weChatConfig = weChatConfig;
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
     * 获取AccessToken
     */
    @Override
    public String getAccessToken() {
        String appId = weChatConfig.getAppId();
        WeChatAccessToken accessToken = ACCESS_TOKEN_MAP.get(appId);
        if (accessToken == null || accessToken.validate()) {
            synchronized (WeChatAccessTokenManager.class) {
                accessToken = ACCESS_TOKEN_MAP.get(appId);
                if (accessToken == null || accessToken.validate()) {
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
        final WeChatAccessToken accessToken = ACCESS_TOKEN_MAP.get(appId);
        if (accessToken != null && accessToken.pastTime() > 1000 * 30) {
            ACCESS_TOKEN_MAP.remove(appId, accessToken);
        }
    }

}