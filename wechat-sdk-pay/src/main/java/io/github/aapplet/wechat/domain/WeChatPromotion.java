package io.github.aapplet.wechat.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

public class WeChatPromotion {

    /**
     * 优惠功能
     */
    @Data
    @Accessors(chain = true)
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
        private List<GoodsDetail> goodsDetail;
    }

    /**
     * 单品列表
     */
    @Data
    @Accessors(chain = true)
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