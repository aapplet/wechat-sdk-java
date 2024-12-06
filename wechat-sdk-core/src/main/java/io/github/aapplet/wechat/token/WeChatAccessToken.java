package io.github.aapplet.wechat.token;

import lombok.Data;

/**
 * <a href="https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-access-token/getStableAccessToken.html">获取稳定版接口调用凭据</a>
 */
@Data
public class WeChatAccessToken {

    /**
     * 凭证
     */
    private String accessToken;
    /**
     * 凭证创建时间戳
     */
    private Long createTimestamp;
    /**
     * 凭证过期时间戳
     */
    private Long expireTimestamp;

    /**
     * @return 凭证已创建时长
     */
    public long duration() {
        return System.currentTimeMillis() - createTimestamp;
    }

    /**
     * @return 检查凭证是否过期
     */
    public boolean isExpired() {
        return System.currentTimeMillis() > expireTimestamp;
    }

}