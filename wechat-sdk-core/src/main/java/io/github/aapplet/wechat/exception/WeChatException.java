package io.github.aapplet.wechat.exception;

import lombok.Getter;

@Getter
public class WeChatException extends RuntimeException {

    private final String code;
    private final String message;
    private final Integer httpStatus;

    public WeChatException(String message, Integer httpStatus) {
        super(message);
        this.code = "";
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
