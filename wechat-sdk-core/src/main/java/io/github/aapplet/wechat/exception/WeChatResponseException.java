package io.github.aapplet.wechat.exception;

import io.github.aapplet.wechat.common.WeChatPaymentResponse;
import io.github.aapplet.wechat.common.WeChatStatusCodeBase;

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

    public WeChatResponseException(WeChatStatusCodeBase response) {
        super(response.getErrMsg());
    }

}
