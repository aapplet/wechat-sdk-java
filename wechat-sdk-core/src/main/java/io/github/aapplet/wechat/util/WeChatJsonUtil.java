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
        // 将单值数组强制转换为相应的值类型
        objectMapper.enable(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS);
        // 序列化没有属性不抛出异常
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 反序列化未知属性不抛出异常
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 禁用所有访问器,只序列化@JsonProperty注解标记的属性
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
    }

    /**
     * 对象转Json
     */
    public static String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new WeChatException("对象转Json异常", e);
        }
    }

    /**
     * Json转对象
     */
    public static <T> T fromJson(byte[] bytes, Class<T> valueType) {
        try {
            return objectMapper.readValue(bytes, valueType);
        } catch (IOException e) {
            throw new WeChatException("Json转对象异常", e);
        }
    }

    /**
     * Json转对象
     */
    public static <T> T fromJson(String body, Class<T> valueType) {
        try {
            return objectMapper.readValue(body, valueType);
        } catch (IOException e) {
            throw new WeChatException("Json转对象异常", e);
        }
    }

    /**
     * 对象转换
     */
    public static <T> T fromObject(Object value, Class<T> valueType) {
        try {
            return objectMapper.convertValue(value, valueType);
        } catch (IllegalArgumentException e) {
            throw new WeChatException("Json转对象异常", e);
        }
    }

}