package io.github.aapplet.wechat.util;

import io.github.aapplet.wechat.exception.WeChatException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.*;
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
        return getPrivateKey(WeChatFileUtil.readString(path));
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
     * 生成证书
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

    /**
     * 加载商户证书
     *
     * @param mchId 商户号
     * @param path  证书路径
     * @return 商户证书私钥
     */
    public static PrivateKey loadPKCS12(String mchId, String path) {
        final byte[] bytes = WeChatFileUtil.readAllBytes(path);
        final InputStream inputStream = new ByteArrayInputStream(bytes);
        try {
            final char[] password = mchId.toCharArray();
            final KeyStore store = KeyStore.getInstance("PKCS12");
            store.load(inputStream, password);
            final String alias = store.aliases().nextElement();
            final X509Certificate certificate = (X509Certificate) store.getCertificate(alias);
            certificate.checkValidity();
            return (PrivateKey) store.getKey(alias, password);
        } catch (CertificateExpiredException | CertificateNotYetValidException e) {
            throw new WeChatException("商户证书已过期", e);
        } catch (Exception e) {
            throw new WeChatException("无效的商户证书", e);
        }
    }

}