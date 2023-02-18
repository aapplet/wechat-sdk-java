package io.github.aapplet.wechat.http;

import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.constant.WeChatConstant;
import io.github.aapplet.wechat.exception.WeChatHttpException;
import io.github.aapplet.wechat.host.WeChatHost;
import io.github.aapplet.wechat.util.WeChatStrUtil;
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
    private final WeChatConfig weChatConfig;
    /**
     * 请求属性
     */
    private final WeChatAttribute<?> weChatAttribute;
    /**
     * http客户端
     */
    private final HttpClient httpClient;
    /**
     * http请求构建器
     */
    private final HttpRequest.Builder httpRequest;

    /**
     * @param weChatConfig    配置信息
     * @param weChatAttribute 请求属性
     */
    private WeChatHttpRequest(WeChatConfig weChatConfig, WeChatAttribute<?> weChatAttribute) {
        final Duration httpConnectTimeout = Duration.ofMillis(weChatConfig.getHttpConnectTimeout());
        final Duration httpResponseTimeout = Duration.ofMillis(weChatConfig.getHttpResponseTimeout());
        this.weChatConfig = weChatConfig;
        this.weChatAttribute = weChatAttribute;
        this.httpClient = HttpClient.newBuilder().connectTimeout(httpConnectTimeout).build();
        this.httpRequest = HttpRequest.newBuilder().timeout(httpResponseTimeout);
    }

    /**
     * http headers
     *
     * @return this
     */
    private WeChatHttpRequest auth() {
        httpRequest.setHeader(WeChatConstant.ACCEPT, WeChatConstant.APPLICATION_JSON);
        httpRequest.setHeader(WeChatConstant.CONTENT_TYPE, WeChatConstant.APPLICATION_JSON);
        httpRequest.setHeader(WeChatConstant.AUTHORIZATION, this.authorization());
        return this;
    }

    /**
     * http请求体
     *
     * @return this
     */
    private WeChatHttpRequest body() {
        String requestBody = weChatAttribute.getRequestBody();
        if (requestBody == null) {
            httpRequest.method(weChatAttribute.getMethod(), HttpRequest.BodyPublishers.noBody());
        } else {
            httpRequest.method(weChatAttribute.getMethod(), HttpRequest.BodyPublishers.ofString(requestBody));
        }
        return this;
    }

    /**
     * 执行http请求
     *
     * @return HTTP响应
     */
    private HttpResponse<byte[]> execute() {
        final URI uri = URI.create(weChatAttribute.getRequestURL());
        final HttpRequest request = httpRequest.uri(uri).build();
        try {
            return logger(httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray()));
        } catch (IOException | InterruptedException e) {
            // 域名信息
            final WeChatHost weChatHost = weChatAttribute.getWeChatHost();
            // 失败重试,切换另一个域名
            if (weChatHost.retry()) {
                // 容灾检测
                weChatHost.disaster();
                // 请求重试
                return execute();
            } else {
                // 主备域名均请求失败
                throw new WeChatHttpException("网络异常,请检查网络是否畅通", e);
            }
        }
    }

    /**
     * API-V3 Authorization
     * <p>
     * https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay4_0.shtml
     */
    private String authorization() {
        final String nonceStr = WeChatStrUtil.random();
        final String method = weChatAttribute.getMethod();
        final String requestURI = weChatAttribute.getRequestURI();
        final String requestBody = weChatAttribute.getRequestBody();
        final String body = requestBody == null ? "" : requestBody;
        final String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        final String message = method + "\n" + requestURI + "\n" + timestamp + "\n" + nonceStr + "\n" + body + "\n";
        final String mchId = weChatConfig.getMchId();
        final String schema = weChatConfig.getSchema();
        final String serialNo = weChatConfig.getSerialNo();
        final String signature = weChatConfig.signature(message);
        return schema + " mchid=\"" + mchId +
                "\",serial_no=\"" + serialNo +
                "\",nonce_str=\"" + nonceStr +
                "\",timestamp=\"" + timestamp +
                "\",signature=\"" + signature + "\"";
    }

    /**
     * 打印日志
     *
     * @param httpResponse HTTP响应
     * @return HTTP响应
     */
    private HttpResponse<byte[]> logger(HttpResponse<byte[]> httpResponse) {
        if (log.isDebugEnabled()) {
            final StringJoiner join = new StringJoiner("\n").add(LocalDateTime.now().toString());
            final byte[] bytes = httpResponse.body();
            final HttpRequest request = httpResponse.request();
            final String requestBody = weChatAttribute.getRequestBody();
            final String responseBody = bytes.length > 8192 ? null : new String(bytes, StandardCharsets.UTF_8);
            join.add("================================================== Start ==================================================");
            join.add(">>>>>Request-URL........：" + request.uri());
            join.add(">>>>>Request-Method.....：" + request.method());
            join.add(">>>>>Request-MchId......：" + weChatConfig.getMchId());
            join.add(">>>>>Request-AppId......：" + weChatConfig.getAppId());
            join.add(">>>>>Request-Body.......：" + requestBody);
            join.add(">>>>>Request-Headers....：" + request.headers().map());
            join.add(">>>>>Response-Headers...：" + httpResponse.headers().map());
            join.add(">>>>>Response-Body......：" + responseBody);
            join.add(">>>>>Response-Length....：" + bytes.length);
            join.add(">>>>>Response-Status....：" + httpResponse.statusCode());
            join.add("=================================================== End ===================================================");
            log.debug(join.toString());
        }
        return httpResponse;
    }

    /**
     * 微信支付V3请求
     *
     * @param weChatConfig 配置信息
     * @param request      请求信息
     * @return HTTP响应
     */
    public static HttpResponse<byte[]> v3(WeChatConfig weChatConfig, WeChatRequest<?> request) {
        return v3(weChatConfig, request.getAttribute(weChatConfig));
    }

    /**
     * 微信支付V3请求
     *
     * @param weChatConfig    配置信息
     * @param weChatAttribute 请求属性
     * @return HTTP响应
     */
    public static HttpResponse<byte[]> v3(WeChatConfig weChatConfig, WeChatAttribute<?> weChatAttribute) {
        return new WeChatHttpRequest(weChatConfig, weChatAttribute).auth().body().execute();
    }

    /**
     * 公众平台请求
     *
     * @param weChatConfig 配置信息
     * @param request      请求信息
     * @return HTTP响应
     */
    public static HttpResponse<byte[]> mp(WeChatConfig weChatConfig, WeChatRequest<?> request) {
        return mp(weChatConfig, request.getAttribute(weChatConfig));
    }

    /**
     * 公众平台请求
     *
     * @param weChatConfig    配置信息
     * @param weChatAttribute 请求属性
     * @return HTTP响应
     */
    public static HttpResponse<byte[]> mp(WeChatConfig weChatConfig, WeChatAttribute<?> weChatAttribute) {
        return new WeChatHttpRequest(weChatConfig, weChatAttribute).body().execute();
    }

}