package io.github.aapplet.wechat.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.request.WeChatPaymentJsapiRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 微信支付
 */
public class WeChatPayment {

    /**
     * 【订单金额】
     */
    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Amount {
        /**
         * 【总金额】
         * {@link WeChatPaymentJsapiRequest}
         */
        @JsonProperty("total")
        private Integer total;
        /**
         * 【支付币种】
         * {@link WeChatPaymentJsapiRequest}
         */
        @JsonProperty("currency")
        private String currency;
        /**
         * 用户支付金额
         */
        @JsonProperty("payer_total")
        private Integer payerTotal;
        /**
         * 用户支付币种
         */
        @JsonProperty("payer_currency")
        private String payerCurrency;
    }

    /**
     * 【支付者】
     */
    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payer {
        /**
         * 【用户平台标识】
         * {@link WeChatPaymentJsapiRequest}
         */
        @JsonProperty("openid")
        private String openId;
    }

    /**
     * 【优惠功能】
     */
    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detail {
        /**
         * 【订单原价】
         */
        @JsonProperty("cost_price")
        private Integer costPrice;
        /**
         * 【商品小票ID】
         */
        @JsonProperty("invoice_id")
        private String invoiceId;
        /**
         * 【单品列表】
         */
        @JsonProperty("goods_detail")
        private List<GoodsDetail> goodsDetail;
    }

    /**
     * 【单品列表】
     */
    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoodsDetail {
        /**
         * 【商户侧商品编码】
         */
        @JsonProperty("merchant_goods_id")
        private String merchantGoodsId;
        /**
         * 【微信支付商品编码】
         */
        @JsonProperty("wechatpay_goods_id")
        private String wechatPayGoodsId;
        /**
         * 【商品名称】
         */
        @JsonProperty("goods_name")
        private String goodsName;
        /**
         * 【商品数量】
         */
        @JsonProperty("quantity")
        private Integer quantity;
        /**
         * 【商品单价】
         */
        @JsonProperty("unit_price")
        private Integer unitPrice;
    }

    /**
     * 【场景信息】
     */
    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SceneInfo {
        /**
         * 【用户终端IP】
         */
        @JsonProperty("payer_client_ip")
        private String payerClientIp;
        /**
         * 【商户端设备号】
         */
        @JsonProperty("device_id")
        private String deviceId;
        /**
         * 【商户门店信息】
         */
        @JsonProperty("store_info")
        private StoreInfo storeInfo;
    }

    /**
     * 【商户门店信息】
     */
    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoreInfo {
        /**
         * 【门店编号】
         */
        @JsonProperty("id")
        private String id;
        /**
         * 【门店名称】
         */
        @JsonProperty("name")
        private String name;
        /**
         * 【地区编码】
         */
        @JsonProperty("area_code")
        private String areaCode;
        /**
         * 【详细地址】
         */
        @JsonProperty("address")
        private String address;
    }

    /**
     * 【结算信息】
     */
    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SettleInfo {
        /**
         * 【分账标识】
         */
        @JsonProperty("profit_sharing")
        private Boolean profitSharing;
    }

}
