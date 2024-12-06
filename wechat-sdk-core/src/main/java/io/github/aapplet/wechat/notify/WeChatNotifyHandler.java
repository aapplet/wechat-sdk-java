package io.github.aapplet.wechat.notify;

import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.exception.WeChatValidationException;
import io.github.aapplet.wechat.util.WeChatJsonUtil;
import io.github.aapplet.wechat.util.WeChatValidator;

import java.util.Map;

/**
 * 回调通知处理
 */
public class WeChatNotifyHandler extends WeChatValidator {

    /**
     * 回调通知签名验证
     *
     * @param wechatConfig 配置信息
     * @param headers      请求头
     * @param body         请求内容
     */
    public WeChatNotifyHandler(WeChatConfig wechatConfig, Map<String, ?> headers, String body) {
        super(wechatConfig, headers, body);
    }

    /**
     * 解密后验签并转换成对象
     *
     * @param valueType 通知类型
     * @return 通知对象
     */
    public <T extends WeChatResponse.Notify> T transform(Class<T> valueType) {
        final WeChatNotify notification = WeChatNotify.fromJson(body);
        final WeChatNotify.Resource resource = notification.getResource();
        final String nonce = resource.getNonce();
        final String ciphertext = resource.getCiphertext();
        final String associatedData = resource.getAssociatedData();
        final byte[] decrypt = wechatConfig.decrypt(nonce, associatedData, ciphertext);
        if (this.verify()) {
            return WeChatJsonUtil.fromJson(decrypt, valueType);
        } else {
            throw new WeChatValidationException("回调签名错误,验签失败");
        }
    }

}
