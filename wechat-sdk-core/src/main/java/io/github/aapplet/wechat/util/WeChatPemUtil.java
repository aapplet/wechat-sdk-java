package io.github.aapplet.wechat.util;

import io.github.aapplet.wechat.exception.WeChatException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class WeChatPemUtil {

    /**
     * 加载商户证书私钥
     *
     * @param path 私钥路径
     * @return RSA私钥
     */
    public static PrivateKey loadPrivateKey(String path) {
        return getPrivateKey(new String(WeChatResourceUtil.readAllBytes(path), StandardCharsets.UTF_8));
    }

    /**
     * 加载商户证书私钥
     *
     * @param privateKey 私钥字符串
     * @return RSA私钥
     */
    public static PrivateKey getPrivateKey(String privateKey) {
        privateKey = privateKey.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").replaceAll("\\s+", "");
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        } catch (NoSuchAlgorithmException e) {
            throw new WeChatException("当前Java环境不支持RSA", e);
        } catch (InvalidKeySpecException e) {
            throw new WeChatException("无效的商户私钥", e);
        }
    }

    /**
     * 生成证书
     *
     * @param bytes 证书数据
     * @return 证书
     */
    public static X509Certificate getCertificate(byte[] bytes) {
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return (X509Certificate) cf.generateCertificate(inputStream);
        } catch (IOException | CertificateException e) {
            throw new WeChatException("无效的证书", e);
        }
    }

    /**
     * 加载商户证书
     *
     * @param password    商户号
     * @param pkcs12Bytes 证书数据
     * @return 证书
     */
    public static KeyPair loadPKCS12(char[] password, byte[] pkcs12Bytes) {
        try (InputStream inputStream = new ByteArrayInputStream(pkcs12Bytes)) {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(inputStream, password);
            var alias = keyStore.aliases().nextElement();
            var certificate = keyStore.getCertificate(alias);
            var publicKey = certificate.getPublicKey();
            var privateKey = (PrivateKey) keyStore.getKey(alias, password);
            return new KeyPair(publicKey, privateKey);
        } catch (KeyStoreException | NoSuchAlgorithmException e) {
            throw new WeChatException("当前Java环境不支持PKCS12", e);
        } catch (UnrecoverableKeyException | CertificateException e) {
            throw new WeChatException("无效的商户证书", e);
        } catch (IOException e) {
            throw new WeChatException("商户号与商户证书不匹配", e);
        }
    }

}