package io.github.aapplet.wechat.base;

/**
 * 响应
 */
public interface WeChatResponse {

    /**
     * 微信支付响应
     */
    interface V3 extends WeChatResponse {
    }

    /**
     * 公众平台响应
     */
    interface MP extends WeChatResponse {
    }

    /**
     * 文件下载
     */
    interface Download extends WeChatResponse {
    }

    /**
     * 回调通知
     */
    interface Notify extends WeChatResponse {
    }

}