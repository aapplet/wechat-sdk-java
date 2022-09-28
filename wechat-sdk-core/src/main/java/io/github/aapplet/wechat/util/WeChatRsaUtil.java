package io.github.aapplet.wechat.util;

import io.github.aapplet.wechat.exception.WeChatException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Base64;

/**
 * 敏感信息加解密
 * <p>
 * https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay4_3.shtml
 */
public class WeChatRsaUtil {

    /**
     * 使用平台公钥,对敏感信息进行加密
     *
     * @param certificate 平台证书
     * @param message     敏感信息
     * @return 密文
     */
    public static String encrypt(X509Certificate certificate, String message) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, certificate.getPublicKey());
            return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            throw new WeChatException("当前Java环境不支持RSA v1.5/OAEP", e);
        } catch (InvalidKeyException e) {
            throw new WeChatException("无效的平台公钥", e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new WeChatException("敏感信息加密失败,加密原串的长度不能超过214字节", e);
        }
    }

    /**
     * 使用商户私钥,对敏感信息的密文进行解密
     *
     * @param privateKey 商户私钥
     * @param ciphertext 密文
     * @return 敏感信息
     */
    public static String decrypt(PrivateKey privateKey, String ciphertext) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(ciphertext)), StandardCharsets.UTF_8);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            throw new WeChatException("当前Java环境不支持RSA v1.5/OAEP", e);
        } catch (InvalidKeyException e) {
            throw new WeChatException("无效的商户私钥", e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new WeChatException("敏感信息解密失败", e);
        }
    }

}