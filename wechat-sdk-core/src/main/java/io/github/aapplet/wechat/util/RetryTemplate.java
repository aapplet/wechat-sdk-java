package io.github.aapplet.wechat.util;

import java.util.function.Supplier;

/**
 * 异常重试模板
 */
public class RetryTemplate<T> {

    /**
     * 异常类型
     */
    private final Class<?> throwableClass;
    /**
     * 函数方法
     */
    private final Supplier<T> supplier;
    /**
     * 重试次数
     */
    private int retryCount;

    /**
     * @param supplier   函数方法
     * @param retryCount 重试次数
     */
    public RetryTemplate(Supplier<T> supplier, int retryCount) {
        this(supplier, retryCount, Exception.class);
    }

    /**
     * @param supplier       函数方法
     * @param retryCount     重试次数
     * @param throwableClass 异常类型
     */
    public RetryTemplate(Supplier<T> supplier, int retryCount, Class<?> throwableClass) {
        this.supplier = supplier;
        this.retryCount = retryCount;
        this.throwableClass = throwableClass;
    }

    /**
     * @return 执行函数方法, 异常后重试
     */
    public T execute() {
        try {
            return supplier.get();
        } catch (Throwable e) {
            if (throwableClass.isInstance(e) && retryCount-- > 0) {
                return execute();
            } else {
                throw e;
            }
        }
    }

    /**
     * 提交函数方法,异常重试一次
     *
     * @param supplier 函数方法
     * @param <T>      返回类型
     * @return 返回值
     */
    public static <T> T submit(Supplier<T> supplier) {
        return submit(supplier, 1);
    }

    /**
     * 提交函数方法,异常重试一次
     *
     * @param supplier       函数方法
     * @param throwableClass 异常类型
     * @param <T>            返回类型
     * @return 返回值
     */
    public static <T> T submit(Supplier<T> supplier, Class<?> throwableClass) {
        return submit(supplier, 1, throwableClass);
    }

    /**
     * 提交函数方法,异常重试指定次数
     *
     * @param supplier   函数方法
     * @param retryCount 重试次数
     * @param <T>        返回类型
     * @return 返回值
     */
    public static <T> T submit(Supplier<T> supplier, int retryCount) {
        return new RetryTemplate<>(supplier, retryCount).execute();
    }

    /**
     * 提交函数方法,异常重试指定次数
     *
     * @param supplier       函数方法
     * @param retryCount     重试次数
     * @param throwableClass 异常类型
     * @param <T>            返回类型
     * @return 返回值
     */
    public static <T> T submit(Supplier<T> supplier, int retryCount, Class<?> throwableClass) {
        return new RetryTemplate<>(supplier, retryCount, throwableClass).execute();
    }

}
