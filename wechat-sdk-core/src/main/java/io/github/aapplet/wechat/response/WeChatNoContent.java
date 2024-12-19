package io.github.aapplet.wechat.response;

import io.github.aapplet.wechat.base.WeChatResponse;
import lombok.Data;

/**
 * 无数据（Http状态码为204）
 */
@Data
public class WeChatNoContent implements WeChatResponse.V3 {

    private final boolean ok = true;

}