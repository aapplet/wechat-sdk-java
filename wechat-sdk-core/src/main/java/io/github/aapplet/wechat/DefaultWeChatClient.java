package io.github.aapplet.wechat;

import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatClient;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.constant.WeChatConstant;
import io.github.aapplet.wechat.exception.WeChatExpiredException;
import io.github.aapplet.wechat.exception.WeChatResponseException;
import io.github.aapplet.wechat.exception.WeChatValidationException;
import io.github.aapplet.wechat.http.WeChatHttpRequest;
import io.github.aapplet.wechat.response.WeChatDownload;
import io.github.aapplet.wechat.response.WeChatNoContentResponse;
import io.github.aapplet.wechat.response.WeChatPaymentResponse;
import io.github.aapplet.wechat.response.WeChatPlatformResponse;
import io.github.aapplet.wechat.util.RetryTemplate;
import io.github.aapplet.wechat.util.WeChatJsonUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.net.http.HttpResponse;

/**
 * 默认客户端
 */
@RequiredArgsConstructor
public final class DefaultWeChatClient implements WeChatClient {

    /**
     * 配置信息
     */
    @Getter
    private final WeChatConfig weChatConfig;

    /**
     * 微信支付V3请求
     *
     * @param request 请求参数
     * @param <T>     响应类型
     * @return HTTP响应
     */
    @Override
    public <T extends WeChatResponse.V3> T execute(WeChatRequest.V3<T> request) {
        final WeChatAttribute<T> attribute = request.getAttribute(weChatConfig);
        final HttpResponse<byte[]> httpResponse = WeChatHttpRequest.v3(weChatConfig, attribute);
        final WeChatValidator validator = new WeChatValidator(weChatConfig, httpResponse);
        if (!validator.verify()) {
            throw new WeChatValidationException("响应签名错误,验签失败");
        }
        if (httpResponse.statusCode() == 200) {
            return WeChatJsonUtil.fromJson(httpResponse.body(), attribute.getResponseClass());
        }
        if (httpResponse.statusCode() == 204) {
            return attribute.getResponseClass().cast(new WeChatNoContentResponse());
        }
        throw new WeChatResponseException(WeChatPaymentResponse.fromJson(httpResponse.body()));
    }

    /**
     * 公众平台请求
     *
     * @param request 请求参数
     * @param <T>     响应类型
     * @return HTTP响应
     */
    @Override
    public <T extends WeChatResponse.MP> T execute(WeChatRequest.MP<T> request) {
        return RetryTemplate.submit(() -> {
            final WeChatAttribute<T> attribute = request.getAttribute(weChatConfig);
            final HttpResponse<byte[]> httpResponse = WeChatHttpRequest.mp(weChatConfig, attribute);
            final T result = WeChatJsonUtil.fromJson(httpResponse.body(), attribute.getResponseClass());
            final Integer errCode = result.getErrCode();
            if (errCode == null || errCode == 0) {
                return result;
            }
            if (errCode == 40001 || errCode == 42001) {
                weChatConfig.getAccessTokenManager().removeAccessToken();
                throw new WeChatExpiredException(result.getErrMsg());
            }
            throw new WeChatResponseException(result.getErrMsg());
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
        final HttpResponse<byte[]> httpResponse = WeChatHttpRequest.v3(weChatConfig, download);
        if (httpResponse.statusCode() == 200) {
            return new WeChatDownload(httpResponse.body());
        }
        throw new WeChatResponseException(WeChatPaymentResponse.fromJson(httpResponse.body()));
    }

    /**
     * 公众平台文件下载
     *
     * @param download 下载参数
     * @return bytes处理
     */
    @Override
    public WeChatDownload execute(WeChatRequest.MPDownload<WeChatDownload> download) {
        final HttpResponse<byte[]> httpResponse = WeChatHttpRequest.mp(weChatConfig, download);
        httpResponse.headers().allValues(WeChatConstant.CONTENT_TYPE).forEach(header -> {
            if (header.contains(WeChatConstant.APPLICATION_JSON)) {
                throw new WeChatResponseException(WeChatPlatformResponse.fromJson(httpResponse.body()));
            }
        });
        return new WeChatDownload(httpResponse.body());
    }

}