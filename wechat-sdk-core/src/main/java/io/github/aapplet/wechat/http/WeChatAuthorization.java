package io.github.aapplet.wechat.http;

import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.util.WeChatRandomUtil;

/**
 * <a href="https://pay.weixin.qq.com/doc/v3/merchant/4012365334">请求参数里带Path参数（路径参数），如何计算签名</a>
 * <br>
 * <a href="https://pay.weixin.qq.com/doc/v3/merchant/4012365336">请求参数里带Body参数(包体参数），如何计算签名</a>
 * <br>
 * <a href="https://pay.weixin.qq.com/doc/v3/merchant/4012365337">请求参数里有Query（查询参数），如何计算签名</a>
 * <br>
 * <a href="https://pay.weixin.qq.com/doc/v3/merchant/4012365335">图片上传接口，如何计算签名</a>
 */
public class WeChatAuthorization {

    /**
     * @param wechatConfig    配置信息
     * @param wechatAttribute 请求属性
     * @return 签名值
     */
    @SuppressWarnings("SpellCheckingInspection")
    public static String sign(WeChatConfig wechatConfig, WeChatAttribute<?> wechatAttribute) {
        String nonce = WeChatRandomUtil.random32();
        String method = wechatAttribute.getMethod();
        String pathParams = wechatAttribute.getPathParams();
        String requestBody = wechatAttribute.getRequestBody();
        String bodyContent = requestBody == null ? "" : requestBody;
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String content = method + "\n" + pathParams + "\n" + timestamp + "\n" + nonce + "\n" + bodyContent + "\n";
        String signature = wechatConfig.signature(content);
        String serialNo = wechatConfig.getPrivateKeyId();
        String schema = wechatConfig.getSchema();
        String mchId = wechatConfig.getMerchantId();
        return schema + " mchid=\"" + mchId + "\",serial_no=\"" + serialNo + "\",nonce_str=\"" + nonce + "\",timestamp=\"" + timestamp + "\",signature=\"" + signature + "\"";
    }

}