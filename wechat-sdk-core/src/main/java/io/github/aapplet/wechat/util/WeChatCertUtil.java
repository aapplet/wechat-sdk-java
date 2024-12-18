package io.github.aapplet.wechat.util;

import io.github.aapplet.wechat.exception.WeChatException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * 微信证书工具类，用于处理商户证书的私钥和平台证书的公钥。
 */
public class WeChatCertUtil {

    /**
     * 从指定文件路径读取私钥并解析为 {@link PrivateKey} 对象。
     *
     * @param filePath 私钥文件路径
     * @return 解析后的私钥对象
     * @throws WeChatException 如果当前 Java 环境不支持 RSA 或者私钥格式无效
     */
    public static PrivateKey getPrivateKey(String filePath) {
        byte[] bytes = WeChatResourceUtil.readAllBytes(filePath);
        String privateKey = new String(bytes, StandardCharsets.UTF_8)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        } catch (NoSuchAlgorithmException e) {
            throw new WeChatException("当前Java环境不支持RSA", e);
        } catch (InvalidKeySpecException e) {
            throw new WeChatException("无效的私钥", e);
        }
    }

    /**
     * 从字节数组中解析 X.509 证书并返回 {@link X509Certificate} 对象。
     *
     * @param bytes 证书的字节数组
     * @return 解析后的 X.509 证书对象
     * @throws WeChatException 如果证书格式无效
     */
    public static X509Certificate getCertificate(byte[] bytes) {
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return (X509Certificate) cf.generateCertificate(inputStream);
        } catch (IOException | CertificateException e) {
            throw new WeChatException("无效的证书", e);
        }
    }

}