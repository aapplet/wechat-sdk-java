package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.domain.WeChatPayScore;
import io.github.aapplet.wechat.exception.WeChatParamsException;
import io.github.aapplet.wechat.params.WeChatRequestParams;
import io.github.aapplet.wechat.response.WeChatPayScoreSyncResponse;
import io.github.aapplet.wechat.util.WeChatJsonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <a href="https://pay.weixin.qq.com/doc/v3/merchant/4012587962">同步订单状态</a>
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class WeChatPayScoreSyncRequest implements WeChatRequest.V3<WeChatPayScoreSyncResponse> {

    /**
     * 商户服务订单号
     */
    @JsonIgnore
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
    private WeChatPayScore.CollectionDetail detail;

    @Override
    public WeChatAttribute<WeChatPayScoreSyncResponse> getAttribute(WeChatConfig wechatConfig) {
        if (outOrderNo == null) {
            throw new WeChatParamsException("商户服务订单号不存在");
        }
        if (appId == null) {
            appId = wechatConfig.getAppId();
        }
        if (serviceId == null) {
            serviceId = wechatConfig.getServiceId();
        }
        if (type == null) {
            throw new WeChatParamsException("场景类型不存在");
        }
        if (detail == null) {
            throw new WeChatParamsException("内容信息详情不存在");
        }
        var attribute = new WeChatRequestParams<WeChatPayScoreSyncResponse>(wechatConfig.getPayDomain());
        attribute.setMethod("POST");
        attribute.setRequestPath("/v3/payscore/serviceorder/" + outOrderNo + "/sync");
        attribute.setRequestBody(WeChatJsonUtil.toJson(this));
        attribute.setResponseClass(WeChatPayScoreSyncResponse.class);
        return attribute;
    }

}