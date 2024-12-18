package io.github.aapplet.wechat.util;

import java.util.function.Supplier;

/**
 * RetryTemplate 是一个通用的重试机制模板类，用于执行可能失败的操作，并在特定条件下进行重试。
 * 该类允许配置最大重试次数、每次重试之间的延迟时间以及可重试的异常类型。
 *
 * @param <T> 操作返回的类型
 */
public class RetryTemplate<T> {

    /**
     * 要执行的操作，使用 Supplier 接口表示，允许延迟执行。
     */
    private final Supplier<T> action;

    /**
     * 最大重试次数，表示在操作失败后最多可以尝试执行的次数。
     * 如果该值为 0，则表示不进行任何重试。
     */
    private final int maxRetries;

    /**
     * 每次重试之间的延迟时间（毫秒）。在每次重试之前，线程将休眠指定的时间。
     * 如果该值为 0，则表示没有延迟。
     */
    private final long delayMillis;

    /**
     * 可重试的异常类型数组。只有抛出这些类型的异常时，才会进行重试。
     * 如果该数组为空，则表示所有异常都将导致重试。
     */
    private final Class<?>[] exceptionTypes;

    /**
     * 构造函数，初始化 RetryTemplate 实例。
     *
     * @param action         要执行的操作，使用 Supplier 接口表示
     * @param maxRetries     最大重试次数，必须是非负数
     * @param delayMillis    每次重试之间的延迟时间（毫秒），必须是非负数
     * @param exceptionTypes 可重试的异常类型数组
     */
    private RetryTemplate(Supplier<T> action, int maxRetries, long delayMillis, Class<?>... exceptionTypes) {
        this.action = action;
        this.maxRetries = Math.max(0, maxRetries);
        this.delayMillis = Math.max(0, delayMillis);
        this.exceptionTypes = exceptionTypes;
    }

    /**
     * 判断抛出的异常是否是可重试的异常类型。
     *
     * @param e 抛出的异常
     * @return 如果异常是可重试的，返回 true；否则返回 false
     */
    private boolean isRetryable(Exception e) {
        for (Class<?> exceptionType : exceptionTypes) {
            if (exceptionType.isInstance(e)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 执行操作并根据需要进行重试。
     * 如果在最大重试次数内操作成功，则返回操作的结果；否则，抛出 RuntimeException。
     *
     * @return 操作成功时的返回值
     * @throws RuntimeException 如果达到最大重试次数后仍然失败，抛出此异常
     */
    public T execute() {
        for (int i = 0; i <= maxRetries; i++) {
            try {
                return action.get();
            } catch (Exception e) {
                if (!isRetryable(e)) {
                    throw e;
                }
                if (i < maxRetries && delayMillis > 0) {
                    try {
                        Thread.sleep(delayMillis);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("线程在等待重试时被中断", ex);
                    }
                }
            }
        }
        throw new RuntimeException("达到最大重试次数后仍然失败");
    }

    /**
     * 提交操作，默认最大重试次数为 1，延迟时间为 0，允许所有异常类型重试。
     *
     * @param action 要执行的操作
     * @param <T>    操作返回的类型
     * @return 操作成功时的返回值
     */
    public static <T> T submit(Supplier<T> action) {
        return new RetryTemplate<>(action, 1, 0, Exception.class).execute();
    }

    /**
     * 提交操作，默认最大重试次数为 1，延迟时间为 0，允许指定异常类型重试。
     *
     * @param action         要执行的操作
     * @param exceptionTypes 可重试的异常类型数组
     * @param <T>            操作返回的类型
     * @return 操作成功时的返回值
     */
    public static <T> T submit(Supplier<T> action, Class<?>... exceptionTypes) {
        return new RetryTemplate<>(action, 1, 0, exceptionTypes).execute();
    }

    /**
     * 提交操作，指定最大重试次数，延迟时间为 0，允许指定异常类型重试。
     *
     * @param action         要执行的操作
     * @param maxRetries     最大重试次数，必须是非负数
     * @param exceptionTypes 可重试的异常类型数组
     * @param <T>            操作返回的类型
     * @return 操作成功时的返回值
     */
    public static <T> T submit(Supplier<T> action, int maxRetries, Class<?>... exceptionTypes) {
        return new RetryTemplate<>(action, maxRetries, 0, exceptionTypes).execute();
    }

    /**
     * 提交操作，指定最大重试次数和延迟时间，允许指定异常类型重试。
     *
     * @param action         要执行的操作
     * @param maxRetries     最大重试次数，必须是非负数
     * @param delayMillis    每次重试之间的延迟时间（毫秒），必须是非负数
     * @param exceptionTypes 可重试的异常类型数组
     * @param <T>            操作返回的类型
     * @return 操作成功时的返回值
     */
    public static <T> T submit(Supplier<T> action, int maxRetries, long delayMillis, Class<?>... exceptionTypes) {
        return new RetryTemplate<>(action, maxRetries, delayMillis, exceptionTypes).execute();
    }

}