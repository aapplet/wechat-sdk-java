package io.github.aapplet.wechat;

import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatClient;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.exception.WeChatException;
import io.github.aapplet.wechat.util.WeChatUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

@RequiredArgsConstructor
public final class DefaultWeChatClient implements WeChatClient {

    @Getter
    private final WeChatConfig weChatConfig;

    @Override
    public <T extends WeChatResponse> T execute(WeChatRequest<T> request) {
        WeChatAttribute<T> attribute = request.getAttribute(weChatConfig);

        HttpRequest.Builder builder = HttpRequest.newBuilder();
        builder.uri(URI.create(attribute.getRequestURL()));
        builder.setHeader("Accept", "application/json");
        builder.setHeader("Content-Type", "application/json");
        if (attribute.hasAuth()) {
            builder.setHeader("Authorization", this.authorization(attribute));
        }
        if (attribute.hasBody()) {
            builder.method(attribute.getMethod(), HttpRequest.BodyPublishers.ofString(attribute.getRequestBody()));
        } else {
            builder.method(attribute.getMethod(), HttpRequest.BodyPublishers.noBody());
        }
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(builder.build(), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                return WeChatUtil.fromJson(response.body(), attribute.getResponseClass());
            } else {
                throw new WeChatException(response.body(), response.statusCode());
            }
        } catch (IOException e) {
            throw new RuntimeException("请求失败", e);
        } catch (InterruptedException e) {
            throw new RuntimeException("请求中断", e);
        }
    }

    /**
     * 授权
     */
    String authorization(WeChatAttribute<?> weChatAttribute) {
        final String method = weChatAttribute.getMethod();
        final String requestURI = weChatAttribute.getRequestURI();
        final String requestBody = weChatAttribute.getRequestBody();
        final String randomStr = UUID.randomUUID().toString();
        final String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        final String readySign = method + "\n" + requestURI + "\n" + timestamp + "\n" + randomStr + "\n" + requestBody + "\n";
        final String mchId = weChatConfig.getMchId();
        final String schema = weChatConfig.getSchema();
        final String serialNo = weChatConfig.getSerialNo();
        final String signature = weChatConfig.signature(readySign);
        return schema + " " +
               "mchid=\"" + mchId + "\"," +
               "serial_no=\"" + serialNo + "\"," +
               "nonce_str=\"" + randomStr + "\"," +
               "timestamp=\"" + timestamp + "\"," +
               "signature=\"" + signature + "\"";
    }

}
