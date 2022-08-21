package io.github.aapplet.wechat.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class WeChatUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 序列化属性时忽略空值
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
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
            System.out.println(e.getMessage());
            throw new RuntimeException("对象转Json异常", e);
        }
    }

    /**
     * Json转对象
     */
    public static <T> T fromJson(String value, Class<T> valueType) {
        try {
            return objectMapper.readValue(value, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Json转对象异常", e);
        }
    }

    /**
     * 获取私钥
     */
    public static PrivateKey getPrivateKey(String privateKey) {
        privateKey = privateKey
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        try {
            return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("无效的密钥", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持RSA", e);
        }
    }

    /**
     * 加载私钥
     */
    public static PrivateKey loadPrivateKey(String path) {
        // 加载资源
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
        try {
            if (resource == null) {
                // 外部加载
                return getPrivateKey(Files.readString(Path.of(path).toAbsolutePath(), StandardCharsets.UTF_8));
            } else {
                // 内部加载
                return getPrivateKey(Files.readString(Path.of(resource.toURI()), StandardCharsets.UTF_8));
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("加载私钥失败,请检查路径是否正确", e);
        }
    }

    /**
     * 签名
     */
    public static String signature(String content, PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(signature.sign());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持SHA256withRSA", e);
        } catch (SignatureException e) {
            throw new RuntimeException("签名失败", e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("无效的密钥", e);
        }
    }

}
