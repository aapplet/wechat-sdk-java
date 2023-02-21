package io.github.aapplet.wechat.exception;

import io.github.aapplet.wechat.response.WeChatPaymentResponse;
import io.github.aapplet.wechat.response.WeChatStatusCodeResponse;

/**
 * 请求异常
 */
public class WeChatResponseException extends WeChatException {

    public WeChatResponseException(String message) {
        super(message);
    }

    public WeChatResponseException(WeChatPaymentResponse response) {
        super(response.getMessage());
    }

    public WeChatResponseException(WeChatStatusCodeResponse response) {
        super(response.getErrMsg());
    }

}
