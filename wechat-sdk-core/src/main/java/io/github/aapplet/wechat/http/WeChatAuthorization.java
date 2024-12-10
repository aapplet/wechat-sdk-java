package io.github.aapplet.wechat.http;

import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.util.WeChatStrUtil;

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
    public static String sign(WeChatConfig wechatConfig, WeChatAttribute<?> wechatAttribute) {
        String nonceStr = WeChatStrUtil.random32();
        String method = wechatAttribute.getMethod();
        String requestURI = wechatAttribute.getRequestURI();
        String requestBody = wechatAttribute.getRequestBody();
        String body = requestBody == null ? "" : requestBody;
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String message = method + "\n" + requestURI + "\n" + timestamp + "\n" + nonceStr + "\n" + body + "\n";
        String mchId = wechatConfig.getMchId();
        String schema = wechatConfig.getSchema();
        String serialNo = wechatConfig.getSerialNo();
        String signature = wechatConfig.signature(message);
        return schema + " mchid=\"" + mchId + "\",serial_no=\"" + serialNo + "\",nonce_str=\"" + nonceStr + "\",timestamp=\"" + timestamp + "\",signature=\"" + signature + "\"";
    }

}