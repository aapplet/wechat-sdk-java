package io.github.aapplet.wechat.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.aapplet.wechat.exception.WeChatException;

import java.io.IOException;

/**
 * Json工具类，用于处理JSON字符串与Java对象之间的转换。
 */
public class WeChatJacksonUtil {

    // 静态初始化的ObjectMapper实例，用于处理JSON相关的操作
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 序列化属性时忽略空值，即属性值为null时不输出到JSON字符串中
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 序列化空对象时不抛出异常，即使对象没有任何属性也能正常序列化
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 反序列化时将单值数组强制转换为相应的值类型，例如 JSON 字符串 "[123]" 将转换为 int 类型的 123
        objectMapper.configure(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS, true);
        // 反序列化时未知属性不抛出异常，即使JSON字符串中包含Java对象未定义的属性也能正常反序列化
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 只序列化使用@JsonProperty注解标记的属性，其他属性将被忽略
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
    }

    /**
     * 将对象转换为JSON字符串。
     *
     * @param value 要转换的对象
     * @return JSON字符串
     */
    public static String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new WeChatException("对象转Json异常", e);
        }
    }

    /**
     * 将对象转换为JSON字符串并格式化。
     *
     * @param value 要转换的对象
     * @return JSON字符串
     */
    public static String toPrettyJson(Object value) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new WeChatException("对象转Json异常", e);
        }
    }

    /**
     * 将JSON字符串反序列化为指定对象类型。
     *
     * @param json      JSON字符串
     * @param valueType 目标对象类
     * @param <T>       目标对象类型
     * @return 反序列化后的对象
     */
    public static <T> T fromJson(String json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (IOException e) {
            throw new WeChatException("Json转对象异常", e);
        }
    }

    /**
     * 将JSON字节数组反序列化为指定对象类型。
     *
     * @param bytes     JSON字节数组
     * @param valueType 目标对象类
     * @param <T>       目标对象类型
     * @return 反序列化后的对象
     */
    public static <T> T fromJson(byte[] bytes, Class<T> valueType) {
        try {
            return objectMapper.readValue(bytes, valueType);
        } catch (IOException e) {
            throw new WeChatException("Json转对象异常", e);
        }
    }

    /**
     * 将一个对象转换为另一个指定类型的对象。
     *
     * @param value     要转换的对象
     * @param valueType 目标对象类
     * @param <T>       目标对象类型
     * @return 转换后的对象
     */
    public static <T> T fromObject(Object value, Class<T> valueType) {
        try {
            return objectMapper.convertValue(value, valueType);
        } catch (IllegalArgumentException e) {
            throw new WeChatException("Json转对象异常", e);
        }
    }

}