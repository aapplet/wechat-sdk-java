package io.github.aapplet.wechat.exception;

public class WeChatException extends RuntimeException {

    public WeChatException(String message) {
        super(message);
    }

    public WeChatException(String message, Throwable cause) {
        super(message, cause);
    }

}