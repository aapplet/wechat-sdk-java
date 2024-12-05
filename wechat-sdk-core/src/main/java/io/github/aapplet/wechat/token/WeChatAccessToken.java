package io.github.aapplet.wechat.token;

import lombok.Data;

@Data
public class WeChatAccessToken {

    /**
     * 凭证
     */
    private String accessToken;
    /**
     * 凭证有效时间，单位：秒。目前是7200秒之内的值。
     */
    private Integer expiresIn;
    /**
     * 凭证创建时间戳
     */
    private Long createTimestamp;
    /**
     * 凭证过期时间戳
     */
    private Long expireTimestamp;

    /**
     * @return 检查凭证是否过期
     */
    public boolean isExpired() {
        return System.currentTimeMillis() > expireTimestamp;
    }

    /**
     * @return 凭证已创建时长
     */
    public long pastTime() {
        return System.currentTimeMillis() - createTimestamp;
    }

}