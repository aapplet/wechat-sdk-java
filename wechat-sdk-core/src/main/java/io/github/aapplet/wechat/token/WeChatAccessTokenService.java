package io.github.aapplet.wechat.token;

import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.exception.WeChatRequestException;
import io.github.aapplet.wechat.http.WeChatHttpRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <a href="https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-access-token/getStableAccessToken.html">获取稳定版接口调用凭据</a>
 */
@Slf4j
@RequiredArgsConstructor
public class WeChatAccessTokenService implements WeChatAccessTokenManager {

    /**
     * key   = AppId
     * <br>
     * value = AccessToken实例
     */
    private static final Map<String, WeChatAccessToken> ACCESS_TOKENS = new ConcurrentHashMap<>(4);

    /**
     * 配置信息
     */
    @NonNull
    private final WeChatConfig wechatConfig;

    /**
     * 刷新AccessToken
     *
     * @param forceRefresh 强制刷新模式
     * @return AccessToken实例
     */
    WeChatAccessToken refreshAccessToken(boolean forceRefresh) {
        var httpResponse = WeChatHttpRequest.mp(wechatConfig, new WeChatAccessTokenRequest(forceRefresh));
        if (httpResponse.statusCode() != 200) {
            throw new WeChatRequestException(new String(httpResponse.body(), StandardCharsets.UTF_8));
        }
        var response = WeChatAccessTokenResponse.fromJson(httpResponse.body());
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
            synchronized (this) {
                accessToken = ACCESS_TOKENS.get(appId);
                if (accessToken == null || accessToken.isExpired()) {
                    accessToken = refreshAccessToken(false);
                }
            }
        }
        return accessToken.getAccessToken();
    }

    @Override
    public void removeAccessToken() {
        var appId = wechatConfig.getAppId();
        var accessToken = ACCESS_TOKENS.get(appId);
        // 防止强制刷新后导致最新AccessToken被删, 这里只删除创建时长大于15秒的AccessToken
        if (accessToken != null && accessToken.duration() > 15 * 1000) {
            ACCESS_TOKENS.remove(appId, accessToken);
        }
    }

    @Override
    public void forceRefresh() {
        var appId = wechatConfig.getAppId();
        var accessToken = ACCESS_TOKENS.get(appId);
        if (accessToken != null && accessToken.duration() > 30 * 1000) {
            refreshAccessToken(true);
        } else {
            log.warn("refresh access token failed, appId:{}", appId);
        }
    }

}