package io.github.aapplet.wechat.host;

import io.github.aapplet.wechat.base.WeChatDomain;
import io.github.aapplet.wechat.config.WeChatConfig;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.StringJoiner;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <a href="https://pay.weixin.qq.com/doc/v3/merchant/4012075113">跨城冗灾升级指引</a>
 */
@Slf4j
public class WeChatDomainManager implements WeChatDomainAllocator {

    /**
     * 域名信息
     */
    private final WeChatDomain wechatDomain;
    /**
     * 锁定状态
     */
    private final boolean lockedDomainStatus;
    /**
     * 重试状态
     */
    private boolean requestRetryStatus = false;

    /**
     * @param wechatDomain 域名信息
     */
    public WeChatDomainManager(WeChatDomain wechatDomain) {
        this.wechatDomain = wechatDomain;
        this.lockedDomainStatus = wechatDomain.getDomainStatus().get();
    }

    @Override
    public String getAvailableDomain() {
        return (lockedDomainStatus ^ requestRetryStatus) ? wechatDomain.getPrimaryDomain() : wechatDomain.getAlternateDomain();
    }

    @Override
    public boolean requestRetry() {
        return !requestRetryStatus && (requestRetryStatus = true);
    }

    @Override
    public void healthCheck(WeChatConfig wechatConfig) {
        var domainStatus = wechatDomain.getDomainStatus();
        if (domainStatus.compareAndSet(true, false)) {
            var executor = Executors.newSingleThreadScheduledExecutor();
            //
            var healthCheckUrl = wechatDomain.getHealthCheckUrl();
            var probeConnectTimeout = Duration.ofMillis(wechatConfig.getHealthCheckConnectTimeout());
            var probeResponseTimeout = Duration.ofMillis(wechatConfig.getHealthCheckResponseTimeout());
            var httpClient = HttpClient.newBuilder().executor(executor).connectTimeout(probeConnectTimeout).build();
            var httpRequest = HttpRequest.newBuilder(URI.create(healthCheckUrl)).timeout(probeResponseTimeout).build();
            // 定时探测
            executor.scheduleAtFixedRate(() -> {
                var requestTimestamp = System.currentTimeMillis();
                httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.discarding()).thenAccept(response -> {
                    var responseTimestamp = System.currentTimeMillis();
                    if (response.statusCode() < 500) {
                        // ***主域名可用***
                        domainStatus.set(true);
                        // ***关闭线程池***
                        executor.shutdownNow();
                    }
                    if (wechatConfig.isDebug()) {
                        StringJoiner join = new StringJoiner("\n").add(LocalDateTime.now().toString());
                        join.add("================================================== 主域名探测成功 ==================================================");
                        join.add(">>>>>Response-URL.......：" + response.uri());
                        join.add(">>>>>Response-Status....：" + response.statusCode());
                        join.add(">>>>>Response-Time......：" + (responseTimestamp - requestTimestamp) + "ms");
                        join.add("====================================================== End ======================================================");
                        log.info(join.toString());
                    }
                }).exceptionally(throwable -> {
                    var responseTimestamp = System.currentTimeMillis();
                    if (wechatConfig.isDebug()) {
                        StringJoiner join = new StringJoiner("\n").add(LocalDateTime.now().toString());
                        join.add("================================================== 主域名探测失败 ==================================================");
                        join.add(">>>>>Request-URL.....：" + httpRequest.uri());
                        join.add(">>>>>Exception.......：" + throwable.getMessage());
                        join.add(">>>>>Response-Time...：" + (responseTimestamp - requestTimestamp) + "ms");
                        join.add("====================================================== End ======================================================");
                        log.error(join.toString());
                    }
                    return null;
                });
            }, 0, wechatConfig.getHealthCheckDetectInterval(), TimeUnit.MILLISECONDS);
        }
    }

}