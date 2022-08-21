package io.github.aapplet.wechat;

import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatResponse;
import lombok.Data;

@Data
public final class WeChatMediaPlatformAttribute<T extends WeChatResponse> implements WeChatAttribute<T> {

    private String method;
    private String domain;
    private String requestPath;
    private String parameters;
    private String requestBody;
    private Class<T> responseClass;

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getRequestURI() {
        if (parameters.isEmpty()) {
            return requestPath;
        } else {
            return requestPath + "?" + parameters;
        }
    }

    @Override
    public String getRequestURL() {
        return domain + getRequestURI();
    }

    @Override
    public String getRequestBody() {
        return requestBody;
    }

    @Override
    public Class<T> getResponseClass() {
        return responseClass;
    }

    @Override
    public boolean hasAuth() {
        return false;
    }

    @Override
    public boolean hasBody() {
        return !requestBody.isEmpty();
    }

    public WeChatMediaPlatformAttribute<T> init() {
        if (requestPath == null) {
            requestPath = "";
        }
        if (parameters == null) {
            parameters = "";
        }
        if (requestBody == null) {
            requestBody = "";
        }
        return this;
    }

}