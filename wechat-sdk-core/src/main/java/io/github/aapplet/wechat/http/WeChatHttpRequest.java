package io.github.aapplet.wechat.http;

import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.constant.WeChatConstant;
import io.github.aapplet.wechat.exception.WeChatHttpException;
import io.github.aapplet.wechat.host.WeChatHost;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
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
    private final HttpRequest.Builder httpRequest;

    /**
     * @param wechatConfig    配置信息
     * @param wechatAttribute 请求属性
     */
    private WeChatHttpRequest(WeChatConfig wechatConfig, WeChatAttribute<?> wechatAttribute) {
        final Duration httpConnectTimeout = Duration.ofMillis(wechatConfig.getHttpConnectTimeout());
        final Duration httpResponseTimeout = Duration.ofMillis(wechatConfig.getHttpResponseTimeout());
        this.wechatConfig = wechatConfig;
        this.wechatAttribute = wechatAttribute;
        this.httpRequest = HttpRequest.newBuilder().timeout(httpResponseTimeout);
    }

    /**
     * http headers
     *
     * @return this
     */
    private WeChatHttpRequest auth() {
        httpRequest.setHeader(WeChatConstant.ACCEPT, WeChatConstant.APPLICATION_JSON);
        httpRequest.setHeader(WeChatConstant.USER_AGENT, WeChatConstant.USER_AGENT_VALUE);
        httpRequest.setHeader(WeChatConstant.CONTENT_TYPE, WeChatConstant.APPLICATION_JSON);
        httpRequest.setHeader(WeChatConstant.AUTHORIZATION, WeChatAuthorization.sign(wechatConfig, wechatAttribute));
        return this;
    }

    /**
     * http请求体
     *
     * @return this
     */
    private WeChatHttpRequest body() {
        String requestBody = wechatAttribute.getRequestBody();
        if (requestBody == null) {
            httpRequest.method(wechatAttribute.getMethod(), HttpRequest.BodyPublishers.noBody());
        } else {
            httpRequest.method(wechatAttribute.getMethod(), HttpRequest.BodyPublishers.ofString(requestBody));
        }
        return this;
    }

    /**
     * 执行http请求
     *
     * @return HTTP响应
     */
    private HttpResponse<byte[]> execute() {
        URI uri = URI.create(wechatAttribute.getRequestURL());
        HttpRequest request = httpRequest.uri(uri).build();
        HttpClient httpClient = wechatConfig.getHttpClient();
        try {
            return logger(httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray()));
        } catch (IOException | InterruptedException e) {
            // 域名信息
            final WeChatHost wechatHost = wechatAttribute.getWechatHost();
            // 失败重试,切换另一个域名
            if (wechatHost.retry()) {
                // 容灾检测
                wechatHost.disaster();
                // 请求重试
                return execute();
            } else {
                // 主备域名均请求失败
                throw new WeChatHttpException("网络异常,请检查网络是否畅通", e);
            }
        }
    }

    /**
     * 打印日志
     *
     * @param httpResponse HTTP响应
     * @return HTTP响应
     */
    private HttpResponse<byte[]> logger(HttpResponse<byte[]> httpResponse) {
        if (wechatConfig.isDebug()) {
            final StringJoiner join = new StringJoiner("\n").add(LocalDateTime.now().toString());
            final byte[] bytes = httpResponse.body();
            final HttpRequest request = httpResponse.request();
            final String requestBody = wechatAttribute.getRequestBody();
            final String responseBody = bytes.length > 8192 ? null : new String(bytes, StandardCharsets.UTF_8);
            join.add("================================================== Start ==================================================");
            join.add(">>>>>Request-URL........：" + request.uri());
            join.add(">>>>>Request-Method.....：" + request.method());
            join.add(">>>>>Request-MchId......：" + wechatConfig.getMchId());
            join.add(">>>>>Request-AppId......：" + wechatConfig.getAppId());
            join.add(">>>>>Request-Body.......：" + requestBody);
            join.add(">>>>>Request-Headers....：" + request.headers().map());
            join.add(">>>>>Response-Headers...：" + httpResponse.headers().map());
            join.add(">>>>>Response-Body......：" + responseBody);
            join.add(">>>>>Response-Length....：" + bytes.length);
            join.add(">>>>>Response-Status....：" + httpResponse.statusCode());
            join.add("=================================================== End ===================================================");
            log.info(join.toString());
        }
        return httpResponse;
    }

    /**
     * 微信支付V3请求
     *
     * @param wechatConfig 配置信息
     * @param request      请求信息
     * @return HTTP响应
     */
    public static HttpResponse<byte[]> v3(WeChatConfig wechatConfig, WeChatRequest<?> request) {
        return v3(wechatConfig, request.getAttribute(wechatConfig));
    }

    /**
     * 微信支付V3请求
     *
     * @param wechatConfig    配置信息
     * @param wechatAttribute 请求属性
     * @return HTTP响应
     */
    public static HttpResponse<byte[]> v3(WeChatConfig wechatConfig, WeChatAttribute<?> wechatAttribute) {
        return new WeChatHttpRequest(wechatConfig, wechatAttribute).auth().body().execute();
    }

    /**
     * 公众平台请求
     *
     * @param wechatConfig 配置信息
     * @param request      请求信息
     * @return HTTP响应
     */
    public static HttpResponse<byte[]> mp(WeChatConfig wechatConfig, WeChatRequest<?> request) {
        return mp(wechatConfig, request.getAttribute(wechatConfig));
    }

    /**
     * 公众平台请求
     *
     * @param wechatConfig    配置信息
     * @param wechatAttribute 请求属性
     * @return HTTP响应
     */
    public static HttpResponse<byte[]> mp(WeChatConfig wechatConfig, WeChatAttribute<?> wechatAttribute) {
        return new WeChatHttpRequest(wechatConfig, wechatAttribute).body().execute();
    }

}