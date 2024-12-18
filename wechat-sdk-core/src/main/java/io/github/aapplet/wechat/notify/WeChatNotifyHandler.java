package io.github.aapplet.wechat.notify;

import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.exception.WeChatValidationException;
import io.github.aapplet.wechat.http.WeChatValidator;
import io.github.aapplet.wechat.util.WeChatJsonUtil;

import java.util.Map;

/**
 * 回调通知处理
 */
public class WeChatNotifyHandler extends WeChatValidator {

    /**
     * @param wechatConfig 配置信息
     * @param headers      响应头
     * @param responseBody 响应数据
     */
    public WeChatNotifyHandler(WeChatConfig wechatConfig, Map<String, ?> headers, String responseBody) {
        super(wechatConfig, headers, responseBody);
    }

    /**
     * 解密后验签并转换成对象
     *
     * @param valueType 通知类型
     * @param <T>       通知Class
     * @return 通知对象
     */
    public <T extends WeChatResponse.Notify> T transform(Class<T> valueType) {
        final WeChatNotify notification = WeChatNotify.fromJson(responseBody);
        final WeChatNotify.Resource resource = notification.getResource();
        final String nonce = resource.getNonce();
        final String ciphertext = resource.getCiphertext();
        final String associatedData = resource.getAssociatedData();
        final byte[] decrypt = wechatConfig.decrypt(nonce, associatedData, ciphertext);
        if (this.verify()) {
            return WeChatJsonUtil.fromJson(decrypt, valueType);
        } else {
            throw new WeChatValidationException("回调签名错误, 签名验证失败");
        }
    }

}