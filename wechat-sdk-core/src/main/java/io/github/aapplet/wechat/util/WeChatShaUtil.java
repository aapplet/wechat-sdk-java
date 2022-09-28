package io.github.aapplet.wechat.util;

import io.github.aapplet.wechat.exception.WeChatException;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.util.Base64;

/**
 * 签名生成和验证
 */
public class WeChatShaUtil {

    /**
     * 签名生成
     * <p>
     * https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay4_0.shtml
     *
     * @param privateKey 商户私钥
     * @param content    签名内容
     * @return 签名
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
     * 签名验证
     * <p>
     * https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay4_1.shtml
     *
     * @param certificate 平台证书
     * @param content     验证内容
     * @param ciphertext  签名
     * @return 是否通过校验
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
            throw new WeChatException("无效的平台证书", e);
        } catch (SignatureException e) {
            throw new WeChatException("签名验证异常", e);
        }
    }

}