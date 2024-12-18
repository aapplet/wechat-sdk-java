package io.github.aapplet.wechat.http;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 全局HttpClient
 */
public class WeChatHttpClient {

    /**
     * 使用AtomicReference存储HttpClient实例
     */
    private static final AtomicReference<HttpClient> httpClientRef = new AtomicReference<>(
            HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build()
    );

    /**
     * 获取HttpClient实例
     *
     * @return HttpClient实例
     */
    public static HttpClient getInstance() {
        return httpClientRef.get();
    }

    /**
     * 设置HttpClient
     *
     * @param httpClient HttpClient实例
     */
    public static void setHttpClient(HttpClient httpClient) {
        httpClientRef.set(httpClient);
    }

}