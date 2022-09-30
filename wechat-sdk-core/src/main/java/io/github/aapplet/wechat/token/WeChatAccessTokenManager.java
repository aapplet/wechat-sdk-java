package io.github.aapplet.wechat.token;

/**
 * AccessToken管理器
 * <p>
 * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-access-token/getAccessToken.html
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