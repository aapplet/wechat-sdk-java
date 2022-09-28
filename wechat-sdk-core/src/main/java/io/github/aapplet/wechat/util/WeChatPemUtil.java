package io.github.aapplet.wechat.util;

import io.github.aapplet.wechat.exception.WeChatException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
 * 证书相关
 * <p>
 * https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay7_0.shtml
 */
public class WeChatPemUtil {

    /**
     * 加载商户证书私钥
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
            throw new WeChatException("加载私钥失败,请检查路径是否正确", e);
        }
    }

    /**
     * 获取商户证书私钥
     */
    public static PrivateKey getPrivateKey(String privateKey) {
        privateKey = privateKey
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        try {
            return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        } catch (NoSuchAlgorithmException e) {
            throw new WeChatException("当前Java环境不支持RSA", e);
        } catch (InvalidKeySpecException e) {
            throw new WeChatException("无效的商户私钥", e);
        }
    }

    /**
     * 获取平台证书
     */
    public static X509Certificate getCertificate(byte[] bytes) {
        try {
            InputStream inputStream = new ByteArrayInputStream(bytes);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return (X509Certificate) cf.generateCertificate(inputStream);
        } catch (CertificateException e) {
            throw new WeChatException("无效的平台证书", e);
        }
    }

}