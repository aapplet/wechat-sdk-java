package io.github.aapplet.wechat.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.response.WeChatRefundResponse;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 微信退款
 */
public class WeChatRefund {

    /**
     * 退款金额
     */
    @Data
    @Accessors(chain = true)
    public static class RefundAmount {
        /**
         * 退款金额
         */
        @JsonProperty("refund")
        private Integer refund;
        /**
         * 退款出资账户及金额
         */
        @JsonProperty("from")
        private List<From> from;
        /**
         * 原订单金额
         */
        @JsonProperty("total")
        private Integer total;
        /**
         * 退款币种
         */
        @JsonProperty("currency")
        private String currency;
        /**
         * 手续费退款金额
         */
        @JsonProperty("refund_fee")
        private Integer refundFee;
    }

    /**
     * 退款金额明细
     */
    @Data
    @Accessors(chain = true)
    public static class RefundDetail {
        /**
         * 订单金额
         */
        @JsonProperty("total")
        private Integer total;
        /**
         * 退款金额
         */
        @JsonProperty("refund")
        private Integer refund;
        /**
         * 退款出资账户及金额
         * <p>
         * from {@link WeChatRefundResponse}
         */
        @JsonProperty("from")
        private List<WeChatRefund.From> from;
        /**
         * 用户支付金额
         */
        @JsonProperty("payer_total")
        private Integer payerTotal;
        /**
         * 用户退款金额
         */
        @JsonProperty("payer_refund")
        private Integer payerRefund;
        /**
         * 应结退款金额
         * <p>
         * from {@link WeChatRefundResponse}
         */
        @JsonProperty("settlement_refund")
        private Integer settlementRefund;
        /**
         * 应结订单金额
         * <p>
         * from {@link WeChatRefundResponse}
         */
        @JsonProperty("settlement_total")
        private Integer settlementTotal;
        /**
         * 优惠退款金额
         * <p>
         * from {@link WeChatRefundResponse}
         */
        @JsonProperty("discount_refund")
        private Integer discountRefund;
        /**
         * 退款币种
         * <p>
         * from {@link WeChatRefundResponse}
         */
        @JsonProperty("currency")
        private String currency;
        /**
         * 手续费退款金额
         * <p>
         * from {@link WeChatRefundResponse}
         */
        @JsonProperty("refund_fee")
        private Integer refundFee;
    }

    /**
     * 退款出资账户及金额
     */
    @Data
    @Accessors(chain = true)
    public static class From {
        /**
         * 出资账户类型
         */
        @JsonProperty("account")
        private String account;
        /**
         * 出资金额
         */
        @JsonProperty("amount")
        private Integer amount;
    }

    /**
     * 优惠退款信息
     */
    @Data
    @Accessors(chain = true)
    public static class PromotionDetail {
        /**
         * 券ID
         */
        @JsonProperty("promotion_id")
        private String promotionId;
        /**
         * 优惠范围
         */
        @JsonProperty("scope")
        private String scope;
        /**
         * 优惠类型
         */
        @JsonProperty("type")
        private String type;
        /**
         * 优惠券面额
         */
        @JsonProperty("amount")
        private Integer amount;
        /**
         * 优惠退款金额
         */
        @JsonProperty("refund_amount")
        private Integer refundAmount;
        /**
         * 商品列表
         */
        @JsonProperty("goods_detail")
        private GoodsDetail goodsDetail;
    }

    /**
     * 退款商品
     */
    @Data
    @Accessors(chain = true)
    public static class GoodsDetail {
        /**
         * 商户侧商品编码
         */
        @JsonProperty("merchant_goods_id")
        private String merchantGoodsId;
        /**
         * 微信支付商品编码
         */
        @JsonProperty("wechatpay_goods_id")
        private String wechatPayGoodsId;
        /**
         * 商品名称
         */
        @JsonProperty("goods_name")
        private String goodsName;
        /**
         * 商品单价
         */
        @JsonProperty("unit_price")
        private Integer unitPrice;
        /**
         * 商品退款金额
         */
        @JsonProperty("refund_amount")
        private Integer refundAmount;
        /**
         * 商品退货数量
         */
        @JsonProperty("refund_quantity")
        private Integer refundQuantity;
    }

}
