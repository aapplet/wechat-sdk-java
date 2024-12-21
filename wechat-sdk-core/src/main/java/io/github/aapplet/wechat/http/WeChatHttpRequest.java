package io.github.aapplet.wechat.http;

import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.constant.WeChatConstant;
import io.github.aapplet.wechat.exception.WeChatHttpException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.StringJoiner;

@Slf4j
public class WeChatHttpRequest {

    /**
     * 配置信息
     */
    private final WeChatConfig wechatConfig;
    /**
     * 请求属性
     */
    private final WeChatAttribute<?> wechatAttribute;
    /**
     * http请求构建器
     */
    private final HttpRequest.Builder httpRequestBuilder;

    /**
     * @param wechatConfig    配置信息
     * @param wechatAttribute 请求属性
     */
    private WeChatHttpRequest(WeChatConfig wechatConfig, WeChatAttribute<?> wechatAttribute) {
        this.wechatConfig = wechatConfig;
        this.wechatAttribute = wechatAttribute;
        this.httpRequestBuilder = HttpRequest.newBuilder().timeout(Duration.ofMillis(wechatConfig.getHttpResponseTimeout()));
    }

    /**
     * http headers
     *
     * @return this
     */
    private WeChatHttpRequest headers() {
        httpRequestBuilder.setHeader(WeChatConstant.ACCEPT, WeChatConstant.APPLICATION_JSON);
        httpRequestBuilder.setHeader(WeChatConstant.USER_AGENT, WeChatConstant.USER_AGENT_VALUE);
        httpRequestBuilder.setHeader(WeChatConstant.CONTENT_TYPE, WeChatConstant.APPLICATION_JSON);
        httpRequestBuilder.setHeader(WeChatConstant.AUTHORIZATION, WeChatAuthorization.sign(wechatConfig, wechatAttribute));
        return this;
    }

    /**
     * http body
     *
     * @return this
     */
    private WeChatHttpRequest body() {
        String requestBody = wechatAttribute.getRequestBody();
        if (requestBody == null) {
            httpRequestBuilder.method(wechatAttribute.getMethod(), HttpRequest.BodyPublishers.noBody());
        } else {
            httpRequestBuilder.method(wechatAttribute.getMethod(), HttpRequest.BodyPublishers.ofString(requestBody));
        }
        return this;
    }

    /**
     * Http Request
     *
     * @return Http Response
     */
    private HttpResponse<byte[]> execute() {
        var httpClient = wechatConfig.getHttpClient();
        var httpRequest = httpRequestBuilder.uri(URI.create(wechatAttribute.getRequestURL())).build();
        try {
            return logger(httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofByteArray()));
        } catch (IOException e) {
            var domainAllocator = wechatAttribute.getDomainAllocator();
            if (domainAllocator.requestRetry()) {
                domainAllocator.healthCheck(wechatConfig);
                if (wechatConfig.isDebug()) {
                    StringJoiner join = new StringJoiner("\n").add(LocalDateTime.now().toString());
                    join.add("================================================== 主域名请求失败 ==================================================");
                    join.add(">>>>>Response-URL.........：" + httpRequest.uri());
                    join.add(">>>>>Response-Exception...：" + e);
                    join.add("====================================================== End ======================================================");
                    join.add("");
                    log.info(join.toString());
                }
                return execute();
            } else {
                throw new WeChatHttpException("主备域名均请求失败, 请检查网络是否畅通");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new WeChatHttpException("Thread was interrupted while sending request to URL: " + httpRequest.uri());
        }
    }

    /**
     * 日志记录器
     *
     * @param httpResponse Http Response
     * @return Http Response
     */
    private HttpResponse<byte[]> logger(HttpResponse<byte[]> httpResponse) {
        if (wechatConfig.isDebug()) {
            var bytes = httpResponse.body();
            var httpRequest = httpResponse.request();
            var requestBody = wechatAttribute.getRequestBody();
            var responseBody = new String(bytes, 0, Math.min(4096, bytes.length), StandardCharsets.UTF_8);
            var join = new StringJoiner("\n").add(LocalDateTime.now().toString());
            join.add("================================================== Start ==================================================");
            join.add(">>>>>Request-URL........：" + httpRequest.uri());
            join.add(">>>>>Request-Method.....：" + httpRequest.method());
            join.add(">>>>>Request-MchId......：" + wechatConfig.getMerchantId());
            join.add(">>>>>Request-AppId......：" + wechatConfig.getAppId());
            join.add(">>>>>Request-Body.......：" + requestBody);
            join.add(">>>>>Request-Headers....：" + httpRequest.headers().map());
            join.add(">>>>>Response-Headers...：" + httpResponse.headers().map());
            join.add(">>>>>Response-Status....：" + httpResponse.statusCode());
            join.add(">>>>>Response-Length....：" + bytes.length);
            join.add(">>>>>Response-Body......：" + responseBody);
            join.add("=================================================== End ===================================================");
            join.add("");
            log.info(join.toString());
        }
        return httpResponse;
    }

    /**
     * 微信支付V3请求
     *
     * @param wechatConfig  配置信息
     * @param wechatRequest 请求信息
     * @return Http Response
     */
    public static HttpResponse<byte[]> v3(WeChatConfig wechatConfig, WeChatRequest<?> wechatRequest) {
        return v3(wechatConfig, wechatRequest.getAttribute(wechatConfig));
    }

    /**
     * 微信支付V3请求
     *
     * @param wechatConfig    配置信息
     * @param wechatAttribute 请求属性
     * @return Http Response
     */
    public static HttpResponse<byte[]> v3(WeChatConfig wechatConfig, WeChatAttribute<?> wechatAttribute) {
        return new WeChatHttpRequest(wechatConfig, wechatAttribute).headers().body().execute();
    }

    /**
     * 公众平台请求
     *
     * @param wechatConfig  配置信息
     * @param wechatRequest 请求信息
     * @return Http Response
     */
    public static HttpResponse<byte[]> mp(WeChatConfig wechatConfig, WeChatRequest<?> wechatRequest) {
        return mp(wechatConfig, wechatRequest.getAttribute(wechatConfig));
    }

    /**
     * 公众平台请求
     *
     * @param wechatConfig    配置信息
     * @param wechatAttribute 请求属性
     * @return Http Response
     */
    public static HttpResponse<byte[]> mp(WeChatConfig wechatConfig, WeChatAttribute<?> wechatAttribute) {
        return new WeChatHttpRequest(wechatConfig, wechatAttribute).body().execute();
    }

}