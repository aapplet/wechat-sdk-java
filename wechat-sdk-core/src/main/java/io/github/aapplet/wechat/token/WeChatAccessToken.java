package io.github.aapplet.wechat.token;

import lombok.Data;

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
    public long pastTime() {
        return System.currentTimeMillis() - createTimestamp;
    }

    /**
     * @return 验证是否过期
     */
    public boolean validate() {
        return System.currentTimeMillis() > expireTimestamp;
    }

}