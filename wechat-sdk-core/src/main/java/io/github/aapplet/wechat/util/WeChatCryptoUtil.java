package io.github.aapplet.wechat.util;

import io.github.aapplet.wechat.exception.WeChatException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Base64;

/**
 * WeChatCryptoUtil 类提供了与微信加密解密相关的工具方法。
 * 该类主要用于处理微信平台中涉及的数据加密和解密操作。
 */
public class WeChatCryptoUtil {

    /**
     * 生成APIv3签名。
     * <p>
     * 参考文档: <a href="https://pay.weixin.qq.com/doc/v3/merchant/4012365342">APIv3如何签名和验签</a>
     * </p>
     *
     * @param privateKey 商户证书私钥，用于签名
     * @param content    需要签名的内容
     * @return 签名值，Base64编码
     * @throws WeChatException 如果签名过程中出现错误，如算法不支持、私钥无效等
     */
    public static String signature(PrivateKey privateKey, String content) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(signature.sign());
        } catch (NoSuchAlgorithmException e) {
            throw new WeChatException("当前Java环境不支持SHA256withRSA", e);
        } catch (InvalidKeyException e) {
            throw new WeChatException("无效的商户私钥", e);
        } catch (SignatureException e) {
            throw new WeChatException("签名失败", e);
        }
    }

    /**
     * 验证平台证书的签名。
     * <p>
     * 参考文档: <a href="https://pay.weixin.qq.com/doc/v3/merchant/4013053420">如何使用平台证书验签名</a>
     * </p>
     *
     * @param certificate 平台证书公钥，用于验签
     * @param content     需要验签的内容
     * @param ciphertext  签名值，Base64编码
     * @return 验签结果，true表示验证通过，false表示验证失败
     * @throws WeChatException 如果验签过程中出现错误，如算法不支持、公钥无效等
     */
    public static boolean verify(Certificate certificate, String content, String ciphertext) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(certificate);
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            return signature.verify(Base64.getDecoder().decode(ciphertext));
        } catch (NoSuchAlgorithmException e) {
            throw new WeChatException("当前Java环境不支持SHA256withRSA", e);
        } catch (InvalidKeyException e) {
            throw new WeChatException("无效的平台公钥", e);
        } catch (SignatureException e) {
            throw new WeChatException("签名验证失败", e);
        }
    }

    /**
     * 使用平台证书公钥加密敏感字段。
     * <p>
     * 参考文档: <a href="https://pay.weixin.qq.com/doc/v3/merchant/4013053264">如何使用平台证书加密敏感字段</a>
     * </p>
     *
     * @param certificate 平台证书公钥，用于加密
     * @param message     需要加密的明文消息
     * @return 加密后的密文，Base64编码
     * @throws WeChatException 如果加密过程中出现错误，如算法不支持、证书无效等
     */
    public static String encrypt(X509Certificate certificate, String message) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, certificate.getPublicKey());
            return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            throw new WeChatException("当前Java环境不支持RSA v1.5/OAEP", e);
        } catch (InvalidKeyException e) {
            throw new WeChatException("无效的证书", e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new WeChatException("加密原串的长度不能超过214字节", e);
        }
    }

    /**
     * 使用商户私钥解密敏感字段。
     * <p>
     * 参考文档: <a href="https://pay.weixin.qq.com/doc/v3/merchant/4013053265">如何使用API证书解密敏感字段</a>
     * </p>
     *
     * @param privateKey 商户证书私钥，用于解密
     * @param ciphertext 加密后的密文，Base64编码
     * @return 解密后的明文数据
     * @throws WeChatException 如果解密过程中出现错误，如算法不支持、私钥无效等
     */
    public static byte[] decrypt(PrivateKey privateKey, String ciphertext) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(Base64.getDecoder().decode(ciphertext));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new WeChatException("当前Java环境不支持RSA v1.5/OAEP", e);
        } catch (InvalidKeyException e) {
            throw new WeChatException("无效的私钥", e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new WeChatException("解密失败", e);
        }
    }

    /**
     * 使用APIv3密钥解密回调报文和平台证书。
     * <p>
     * 参考文档: <a href="https://pay.weixin.qq.com/doc/v3/merchant/4012071382">如何解密回调报文和平台证书</a>
     * </p>
     *
     * @param aesKey         APIv3密钥，用于加密和解密的对称密钥
     * @param nonce          加密使用的随机串
     * @param associatedData 加密使用的附加数据
     * @param ciphertext     加密后的密文，Base64编码
     * @return 解密后的明文数据
     * @throws WeChatException 如果解密过程中出现错误，如算法不支持、密钥无效等
     */
    public static byte[] decrypt(String aesKey, String nonce, String associatedData, String ciphertext) {
        var secretKeySpec = new SecretKeySpec(aesKey.getBytes(StandardCharsets.UTF_8), "AES");
        var gcmParameterSpec = new GCMParameterSpec(128, nonce.getBytes(StandardCharsets.UTF_8));
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);
            cipher.updateAAD(associatedData.getBytes(StandardCharsets.UTF_8));
            return cipher.doFinal(Base64.getDecoder().decode(ciphertext));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new WeChatException("当前Java环境不支持AES-GCM", e);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new WeChatException("无效的密钥", e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new WeChatException("解密失败", e);
        }
    }

}