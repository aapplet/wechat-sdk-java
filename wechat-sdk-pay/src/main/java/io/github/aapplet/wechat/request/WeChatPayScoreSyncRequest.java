package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.attribute.AbstractAttribute;
import io.github.aapplet.wechat.attribute.WeChatPaymentAttribute;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.domain.WeChatPayScore;
import io.github.aapplet.wechat.exception.WeChatParamsException;
import io.github.aapplet.wechat.response.WeChatPayScoreSyncResponse;
import io.github.aapplet.wechat.util.WeChatJsonUtil;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 同步服务订单信息API
 * <p>
 * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter6_1_20.shtml
 */
@Data
@Accessors(chain = true)
public class WeChatPayScoreSyncRequest implements WeChatRequest.V3<WeChatPayScoreSyncResponse> {

    /**
     * 商户服务订单号
     */
    private String outOrderNo;
    /**
     * 应用ID
     */
    @JsonProperty("appid")
    private String appId;
    /**
     * 服务ID
     */
    @JsonProperty("service_id")
    private String serviceId;
    /**
     * 场景类型
     */
    @JsonProperty("type")
    private String type;
    /**
     * 内容信息详情
     */
    @JsonProperty("detail")
    private WeChatPayScore.SyncDetail detail;

    @Override
    public WeChatAttribute<WeChatPayScoreSyncResponse> getAttribute(WeChatConfig wechatConfig) {
        if (appId == null) {
            appId = wechatConfig.getAppId();
        }
        if (serviceId == null) {
            serviceId = wechatConfig.getServiceId();
        }
        if (outOrderNo == null) {
            throw new WeChatParamsException("商户服务订单号不存在");
        }
        if (type == null) {
            throw new WeChatParamsException("场景类型不存在");
        }
        AbstractAttribute<WeChatPayScoreSyncResponse> attribute = new WeChatPaymentAttribute<>();
        attribute.setMethod("POST");
        attribute.setRequestPath("/v3/payscore/serviceorder/" + outOrderNo + "/sync");
        attribute.setRequestBody(WeChatJsonUtil.toJson(this));
        attribute.setResponseClass(WeChatPayScoreSyncResponse.class);
        return attribute;
    }

}