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

    /**
     * 强制刷新AccessToken
     * <ul>
     *      <li>强制刷新20秒内重复请求无效, 这里限制30秒访问一次</li>
     *      <li>强制刷新成功后上一个AccessToken有效期最多5分钟</li>
     *      <li>使当前AccessToken完全失效, 强制刷新2次即可</li>
     * </ul>
     */
    void forceRefresh();

}