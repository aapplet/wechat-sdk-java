package io.github.aapplet.wechat.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 微信退款
 */
public class WeChatRefund {

    /**
     * 金额信息
     */
    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Amount {
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
         */
        @JsonProperty("from")
        private List<From> from;
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
         */
        @JsonProperty("settlement_refund")
        private Integer settlementRefund;
        /**
         * 应结订单金额
         */
        @JsonProperty("settlement_total")
        private Integer settlementTotal;
        /**
         * 优惠退款金额
         */
        @JsonProperty("discount_refund")
        private Integer discountRefund;
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
     * 退款出资账户及金额
     */
    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
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
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PromotionDetail {
        /**
         * 券ID
         */
        @JsonProperty("promotion_id")
        private String promotionId;
        /**
         * 优惠范围
         * <li>GLOBAL: 全场代金券-以订单整体可优惠的金额为优惠门槛的代金券</li>
         * <li>SINGLE: 单品优惠-以订单中具体某个单品的总金额为优惠门槛的代金券</li>
         */
        @JsonProperty("scope")
        private String scope;
        /**
         * 优惠类型
         * <li>CASH: 预充值-带有结算资金的代金券，会随订单结算给订单收款商户</li>
         * <li>NOCASH: 免充值-不带有结算资金的代金券，无资金结算给订单收款商户</li>
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
        private List<GoodsDetail> goodsDetails;
    }

    /**
     * 商品列表
     */
    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoodsDetail {
        /**
         * 商户侧商品编码
         */
        @JsonProperty("merchant_goods_id")
        private String merchantGoodsId;
        /**
         * 微信侧商品编码
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
