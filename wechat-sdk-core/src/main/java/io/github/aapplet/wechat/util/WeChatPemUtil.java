package io.github.aapplet.wechat.util;

import io.github.aapplet.wechat.exception.WeChatException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
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
     *
     * @param path 私钥路径
     * @return RSA私钥
     */
    public static PrivateKey loadPrivateKey(String path) {
        return getPrivateKey(WeChatFileUtil.readString(path));
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
            final InputStream inputStream = new ByteArrayInputStream(bytes);
            final CertificateFactory cf = CertificateFactory.getInstance("X.509");
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
    public static KeyPair loadPKCS12(String mchId, String path) {
        if (mchId == null || mchId.isEmpty()) {
            throw new WeChatException("商户号不能为空");
        }
        final byte[] bytes = WeChatFileUtil.readAllBytes(path);
        final InputStream inputStream = new ByteArrayInputStream(bytes);
        try {
            final char[] password = mchId.toCharArray();
            final KeyStore store = KeyStore.getInstance("PKCS12");
            store.load(inputStream, password);
            final String alias = store.aliases().nextElement();
            final X509Certificate certificate = (X509Certificate) store.getCertificate(alias);
            certificate.checkValidity();
            return new KeyPair(certificate.getPublicKey(), (PrivateKey) store.getKey(alias, password));
        } catch (KeyStoreException | NoSuchAlgorithmException e) {
            throw new WeChatException("当前Java环境不支持PKCS12", e);
        } catch (UnrecoverableKeyException | CertificateException e) {
            throw new WeChatException("无效的商户证书", e);
        } catch (IOException e) {
            throw new WeChatException("商户号与商户证书不匹配", e);
        }
    }

}