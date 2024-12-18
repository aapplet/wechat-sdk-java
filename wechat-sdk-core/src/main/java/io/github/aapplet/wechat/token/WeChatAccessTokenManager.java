package io.github.aapplet.wechat.token;

/**
 * <a href="https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-access-token/getStableAccessToken.html">获取稳定版接口调用凭据</a>
 */
public interface WeChatAccessTokenManager {

    /**
     * 获取AccessToken
     *
     * @return AccessToken
     */
    String getAccessToken();

    /**
     * 移除过期AccessToken
     */
    void removeAccessToken();

}