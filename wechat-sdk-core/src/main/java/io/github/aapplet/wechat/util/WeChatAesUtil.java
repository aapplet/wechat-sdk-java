package io.github.aapplet.wechat.util;

import io.github.aapplet.wechat.exception.WeChatException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 证书和回调报文解密
 * <p>
 * https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay4_2.shtml
 */
public class WeChatAesUtil {

    /**
     * 证书和回调报文解密
     *
     * @param mchKey         商户秘钥
     * @param nonceStr       加密使用的随机串初始化向量
     * @param associatedData 附加数据包
     * @param ciphertext     Base64编码后的密文
     * @return 解密后信息
     */
    public static byte[] decrypt(String mchKey, String nonceStr, String associatedData, String ciphertext) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(mchKey.getBytes(StandardCharsets.UTF_8), "AES");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, nonceStr.getBytes(StandardCharsets.UTF_8));
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);
            cipher.updateAAD(associatedData.getBytes(StandardCharsets.UTF_8));
            return cipher.doFinal(Base64.getDecoder().decode(ciphertext));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new WeChatException("当前Java环境不支持AES-GCM", e);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new WeChatException("无效的APIv3密钥", e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new WeChatException("证书或回调报文解密失败", e);
        }
    }

}