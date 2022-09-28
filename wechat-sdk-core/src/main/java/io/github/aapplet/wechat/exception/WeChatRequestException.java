package io.github.aapplet.wechat.exception;

import io.github.aapplet.wechat.response.WeChatPaymentResponse;
import io.github.aapplet.wechat.response.WeChatPlatformResponse;

/**
 * 请求异常
 */
public class WeChatRequestException extends WeChatException {

    public WeChatRequestException(String message) {
        super(message);
    }

    public WeChatRequestException(WeChatPaymentResponse exception) {
        super(exception.getMessage());
    }

    public WeChatRequestException(WeChatPlatformResponse exception) {
        super(exception.getErrMsg());
    }

}
