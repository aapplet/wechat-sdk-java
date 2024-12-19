package io.github.aapplet.wechat.exception;

import io.github.aapplet.wechat.response.WeChatStatusCode;

public class WeChatResponseException extends WeChatException {

    public WeChatResponseException(String message) {
        super(message);
    }

    public WeChatResponseException(WeChatStatusCode.PAY response) {
        super(response.getMessage());
    }

    public WeChatResponseException(WeChatStatusCode.MP response) {
        super(response.getMessage());
    }

}
