package io.github.aapplet.wechat.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

public class WeChatPromotion {

    /**
     * 优惠功能
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
        @JsonProperty("coupon_id")
        private String couponId;
        /**
         * 优惠名称
         */
        @JsonProperty("name")
        private String name;
        /**
         * 优惠范围
         * <li>GLOBAL：全场代金券-以订单整体可优惠的金额为优惠门槛的代金券</li>
         * <li>SINGLE：单品优惠-以订单中具体某个单品的总金额为优惠门槛的代金券</li>
         */
        @JsonProperty("scope")
        private String scope;
        /**
         * 优惠类型
         * <li>CASH：预充值-带有结算资金的代金券，会随订单结算给订单收款商户</li>
         * <li>NOCASH：免充值-不带有结算资金的代金券，无资金结算给订单收款商户</li>
         */
        @JsonProperty("type")
        private String type;
        /**
         * 优惠券面额
         */
        @JsonProperty("amount")
        private Integer amount;
        /**
         * 活动ID
         */
        @JsonProperty("stock_id")
        private String stockId;
        /**
         * 微信出资
         */
        @JsonProperty("wechatpay_contribute")
        private Integer wechatPayContribute;
        /**
         * 商户出资
         */
        @JsonProperty("merchant_contribute")
        private Integer merchantContribute;
        /**
         * 其他出资
         */
        @JsonProperty("other_contribute")
        private Integer otherContribute;
        /**
         * 优惠币种
         */
        @JsonProperty("currency")
        private String currency;
        /**
         * 单品列表
         */
        @JsonProperty("goods_detail")
        private List<GoodsDetail> goodsDetails;
    }

    /**
     * 单品列表
     */
    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoodsDetail {
        /**
         * 商品编码
         */
        @JsonProperty("goods_id")
        private String goodsId;
        /**
         * 商品数量
         */
        @JsonProperty("quantity")
        private Integer quantity;
        /**
         * 商品价格
         */
        @JsonProperty("unit_price")
        private Integer unitPrice;
        /**
         * 商品优惠金额
         */
        @JsonProperty("discount_amount")
        private Integer discountAmount;
        /**
         * 商品备注
         */
        @JsonProperty("goods_remark")
        private String goodsRemark;
    }

}