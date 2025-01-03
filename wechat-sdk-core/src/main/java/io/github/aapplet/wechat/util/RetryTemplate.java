package io.github.aapplet.wechat.util;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public class RetryTemplate {

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
    public static <T> T submit(Supplier<T> supplier, int maxRetries, Class<? extends Exception> retryException) {
        for (int i = 0; i <= maxRetries; i++) {
            try {
                return supplier.get();
            } catch (Exception e) {
                if (retryException.isInstance(e)) {
                    log.debug("捕获到可重试异常 => {}", e.toString());
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
    public static <T> T submit(Supplier<T> supplier, Class<? extends Exception> retryException) {
        return submit(supplier, 1, retryException);
    }

}