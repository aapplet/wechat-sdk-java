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

public class WeChatCertificateUtil {

    /**
     * 获取商户证书私钥
     *
     * @param filePath 私钥文件路径
     * @return 证书私钥
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
            throw new WeChatException("无效的证书私钥", e);
        }
    }

    /**
     * 获取平台证书公钥
     *
     * @param bytes 证书数据
     * @return 证书公钥
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