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
 * json工具类
 */
public class WeChatJsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 序列化属性时忽略空值
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 序列化空对象不抛出异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 反序列化将单值数组强制转换为相应的值类型, [value] => value
        objectMapper.configure(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS, true);
        // 反序列化未知属性不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 只序列化@JsonProperty注解标记的属性
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
    }

    /**
     * @param value 对象
     * @return 对象转Json
     */
    public static String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new WeChatException("对象转Json异常", e);
        }
    }

    /**
     * @param body      Json字符串
     * @param valueType 对象class
     * @param <T>       类型
     * @return Json转对象
     */
    public static <T> T fromJson(String body, Class<T> valueType) {
        try {
            return objectMapper.readValue(body, valueType);
        } catch (IOException e) {
            throw new WeChatException("Json转对象异常", e);
        }
    }

    /**
     * @param bytes     Json Bytes
     * @param valueType 对象class
     * @param <T>       类型
     * @return Json转对象
     */
    public static <T> T fromJson(byte[] bytes, Class<T> valueType) {
        try {
            return objectMapper.readValue(bytes, valueType);
        } catch (IOException e) {
            throw new WeChatException("Json转对象异常", e);
        }
    }

    /**
     * @param value     对象转指定类型
     * @param valueType 对象class
     * @param <T>       类型
     * @return 对象转换
     */
    public static <T> T fromObject(Object value, Class<T> valueType) {
        try {
            return objectMapper.convertValue(value, valueType);
        } catch (IllegalArgumentException e) {
            throw new WeChatException("Json转对象异常", e);
        }
    }

}