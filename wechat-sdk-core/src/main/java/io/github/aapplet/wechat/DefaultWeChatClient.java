package io.github.aapplet.wechat;

import io.github.aapplet.wechat.base.WeChatClient;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.constant.WeChatConstant;
import io.github.aapplet.wechat.exception.WeChatExpiredException;
import io.github.aapplet.wechat.exception.WeChatResponseException;
import io.github.aapplet.wechat.exception.WeChatValidationException;
import io.github.aapplet.wechat.http.WeChatHttpRequest;
import io.github.aapplet.wechat.http.WeChatValidator;
import io.github.aapplet.wechat.response.WeChatDownload;
import io.github.aapplet.wechat.response.WeChatNoContent;
import io.github.aapplet.wechat.response.WeChatStatusCode;
import io.github.aapplet.wechat.util.RetryTemplate;
import io.github.aapplet.wechat.util.WeChatJacksonUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * 默认客户端
 */
@Getter
@RequiredArgsConstructor
public final class DefaultWeChatClient implements WeChatClient {

    /**
     * 配置信息
     */
    @NonNull
    private final WeChatConfig wechatConfig;

    /**
     * 微信支付V3请求
     *
     * @param request 请求参数
     * @param <R>     响应类型
     * @return HTTP响应
     */
    @Override
    public <R extends WeChatResponse.V3> R execute(WeChatRequest.V3<R> request) {
        var attribute = request.getAttribute(wechatConfig);
        var httpResponse = WeChatHttpRequest.v3(wechatConfig, attribute);
        var validator = new WeChatValidator(wechatConfig, httpResponse);
        if (!validator.verify()) {
            throw new WeChatValidationException("响应签名错误, 验签失败");
        }
        if (httpResponse.statusCode() == 200) {
            return WeChatJacksonUtil.fromJson(httpResponse.body(), attribute.getResponseClass());
        }
        if (httpResponse.statusCode() == 204) {
            return attribute.getResponseClass().cast(new WeChatNoContent());
        }
        throw new WeChatResponseException(WeChatStatusCode.PAY.fromJson(httpResponse.body()));
    }

    /**
     * 公众平台请求
     *
     * @param request 请求参数
     * @param <R>     响应类型
     * @return HTTP响应
     */
    @Override
    public <R extends WeChatResponse.MP> R execute(WeChatRequest.MP<R> request) {
        return RetryTemplate.submit(() -> {
            var attribute = request.getAttribute(wechatConfig);
            var httpResponse = WeChatHttpRequest.mp(wechatConfig, attribute);
            var statusCode = WeChatStatusCode.MP.fromJson(httpResponse.body());
            if (statusCode.ok() && httpResponse.statusCode() == 200) {
                return WeChatJacksonUtil.fromJson(httpResponse.body(), attribute.getResponseClass());
            }
            if (statusCode.getCode() == 42001) {
                wechatConfig.getAccessTokenManager().removeAccessToken();
                throw new WeChatExpiredException(WeChatJacksonUtil.toJson(statusCode));
            }
            throw new WeChatResponseException(WeChatJacksonUtil.toJson(statusCode));
        }, WeChatExpiredException.class);
    }

    /**
     * 微信支付V3文件下载
     *
     * @param download 下载参数
     * @return bytes处理
     */
    @Override
    public WeChatDownload execute(WeChatRequest.V3Download<WeChatDownload> download) {
        var httpResponse = WeChatHttpRequest.v3(wechatConfig, download);
        if (httpResponse.statusCode() == 200) {
            return new WeChatDownload(httpResponse.body());
        }
        throw new WeChatResponseException(WeChatStatusCode.PAY.fromJson(httpResponse.body()));
    }

    /**
     * 公众平台文件下载
     *
     * @param download 下载参数
     * @return bytes处理
     */
    @Override
    public WeChatDownload execute(WeChatRequest.MPDownload<WeChatDownload> download) {
        var httpResponse = WeChatHttpRequest.mp(wechatConfig, download);
        httpResponse.headers().allValues(WeChatConstant.CONTENT_TYPE).forEach(header -> {
            if (header.contains(WeChatConstant.APPLICATION_JSON)) {
                throw new WeChatResponseException(WeChatStatusCode.MP.fromJson(httpResponse.body()));
            }
        });
        return new WeChatDownload(httpResponse.body());
    }

}