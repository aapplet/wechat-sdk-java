package io.github.aapplet.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

@Slf4j
public class RetryTests {

    /**
     * 尝试执行指定的操作，并在捕获到指定类型的异常时进行重试。
     *
     * @param <T>            泛型类型，表示供应商返回的类型
     * @param supplier       提供操作结果的 Supplier
     * @param maxRetries     最大重试次数
     * @param retryException 需要重试的异常类型
     * @return 操作成功时返回的结果
     * @throws RuntimeException 如果达到最大重试次数后操作仍未成功，则抛出此运行时异常。
     */
    public static <T> T retry(Supplier<T> supplier, int maxRetries, Class<? extends Exception> retryException) {
        for (int i = 0; i <= maxRetries; i++) {
            try {
                log.debug("第 {} 次运行", i + 1);
                return supplier.get();
            } catch (Exception e) {
                if (retryException.isInstance(e)) {
                    log.debug("第 {} 次重试，捕获到可重试异常: {}", i + 1, e.getMessage());
                } else {
                    throw e;
                }
            }
        }
        throw new RuntimeException("已达到最大重试次数，操作仍未成功");
    }

    /**
     * 尝试执行指定的操作，并在捕获到指定类型的异常时进行一次重试。
     *
     * <p>这是一个重载方法，默认最大重试次数为 1。</p>
     *
     * @param <T>            泛型类型，表示供应商返回的类型
     * @param supplier       提供操作结果的 Supplier
     * @param retryException 需要重试的异常类型
     * @return 操作成功时返回的结果
     * @throws RuntimeException 如果达到最大重试次数后操作仍未成功，则抛出此运行时异常。
     */
    public static <T> T retry(Supplier<T> supplier, Class<? extends Exception> retryException) {
        return retry(supplier, 1, retryException);
    }

    @Test
    void simulate() {
        Supplier<String> sampleOperation = () -> {
            int nextInt = ThreadLocalRandom.current().nextInt(100);
            if (nextInt < 50) {
                throw new RuntimeException("模拟操作失败");
            }
            return "操作成功";
        };
        try {
            String result = retry(sampleOperation, RuntimeException.class);
            System.out.println("最终结果：" + result);
        } catch (Exception e) {
            System.out.println("最终失败，原因：" + e.getMessage());
        }
    }

}
