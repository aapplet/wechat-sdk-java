package io.github.aapplet.wechat.response;

import io.github.aapplet.wechat.base.WeChatResponse;
import lombok.Getter;
import lombok.ToString;

/**
 * 无数据（Http状态码为204）
 */
@Getter
@ToString
public class WeChatNoContentResponse implements WeChatResponse.V3 {

    private final boolean ok = true;

}