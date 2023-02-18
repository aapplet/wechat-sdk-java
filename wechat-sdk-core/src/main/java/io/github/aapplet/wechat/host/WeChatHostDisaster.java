package io.github.aapplet.wechat.host;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 跨城容灾
 */
public class WeChatHostDisaster {

    /**
     * 域名信息
     */
    private final WeChatHostStorage hostStorage;

    /**
     * 容灾状态
     */
    private final AtomicBoolean disasterStatus;

    /**
     * @param hostStorage 域名信息
     */
    protected WeChatHostDisaster(WeChatHostStorage hostStorage) {
        this.hostStorage = hostStorage;
        this.disasterStatus = new AtomicBoolean(false);
    }

    /**
     * 容灾检测
     */
    protected void disaster() {
        // 主域名为可用状态且容灾检测未启动则开启容灾检测,探测结束前只会启动一次
        if (hostStorage.isAvailable() && disasterStatus.compareAndSet(false, true)) {
            // 设置主域名不可用
            hostStorage.setAvailable(false);
            // 单线程定时任务线程池
            final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            final HttpClient httpClient = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofMillis(connectTimeout))
                    .build();
            final HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(hostStorage.getMaster()))
                    .timeout(Duration.ofMillis(responseTimeout))
                    .build();
            // 启动定时探测
            executor.scheduleWithFixedDelay(() -> {
                // 请求主域名是否畅通
                httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.discarding()).thenAccept(response -> {
                    // ***关闭线程池***
                    executor.shutdown();
                    // 探测结束,允许下次探测
                    disasterStatus.set(false);
                    // 设置主域名可用
                    hostStorage.setAvailable(true);
                });
            }, 0, detectInterval, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 容灾检测连接超时时间,默认1秒
     */
    private static int connectTimeout = 1000;
    /**
     * 容灾检测响应超时时间,默认1秒
     */
    private static int responseTimeout = 1000;
    /**
     * 容灾检测时间间隔,默认15秒
     */
    private static int detectInterval = 1000 * 15;

    /**
     * @param connectTimeout  容灾检测连接超时时间
     * @param responseTimeout 容灾检测响应超时时间
     * @param detectInterval  容灾检测时间间隔
     */
    public static void setTimeout(int connectTimeout, int responseTimeout, int detectInterval) {
        WeChatHostDisaster.connectTimeout = connectTimeout;
        WeChatHostDisaster.responseTimeout = responseTimeout;
        WeChatHostDisaster.detectInterval = detectInterval;
    }

}
