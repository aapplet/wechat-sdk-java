package io.github.aapplet.wechat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.domain.WeChatPayment;
import io.github.aapplet.wechat.domain.WeChatPromotion;
import lombok.Data;

import java.util.List;

@Data
public class WeChatPaymentQueryResponse implements WeChatResponse.V3 {

    /**
     * 应用ID
     */
    @JsonProperty("appid")
    private String appId;
    /**
     * 直连商户号
     */
    @JsonProperty("mchid")
    private String mchId;
    /**
     * 商户订单号
     */
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    /**
     * 微信支付订单号
     */
    @JsonProperty("transaction_id")
    private String transactionId;
    /**
     * 交易类型
     */
    @JsonProperty("trade_type")
    private String tradeType;
    /**
     * 交易状态
     */
    @JsonProperty("trade_state")
    private String tradeState;
    /**
     * 交易状态描述
     */
    @JsonProperty("trade_state_desc")
    private String tradeStateDesc;
    /**
     * 付款银行
     */
    @JsonProperty("bank_type")
    private String bankType;
    /**
     * 附加数据
     */
    @JsonProperty("attach")
    private String attach;
    /**
     * 支付完成时间
     */
    @JsonProperty("success_time")
    private String successTime;
    /**
     * 支付者
     */
    @JsonProperty("payer")
    private WeChatPayment.Payer payer;
    /**
     * 订单金额
     */
    @JsonProperty("amount")
    private WeChatPayment.Amount amount;
    /**
     * 场景信息
     */
    @JsonProperty("scene_info")
    private WeChatPayment.SceneInfo sceneInfo;
    /**
     * 优惠功能
     */
    @JsonProperty("promotion_detail")
    private List<WeChatPromotion.PromotionDetail> promotionDetails;

}