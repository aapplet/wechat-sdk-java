package io.github.aapplet.wechat.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.request.WeChatPaymentJsapiRequest;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 微信支付
 */
public class WeChatPayment {

    /**
     * 订单金额
     */
    @Data
    @Accessors(chain = true)
    public static class Amount {
        /**
         * 总金额
         */
        @JsonProperty("total")
        private Integer total;
        /**
         * 货币类型
         */
        @JsonProperty("currency")
        private String currency;
        /**
         * 用户支付金额
         * <p>
         * except {@link WeChatPaymentJsapiRequest}
         */
        @JsonProperty("payer_total")
        private Integer payerTotal;
        /**
         * 用户支付币种
         * <p>
         * except {@link WeChatPaymentJsapiRequest}
         */
        @JsonProperty("payer_currency")
        private String payerCurrency;
    }

    /**
     * 支付者
     */
    @Data
    @Accessors(chain = true)
    public static class Payer {
        /**
         * 用户标识
         */
        @JsonProperty("openid")
        private String openId;
    }

    /**
     * 优惠功能
     */
    @Data
    @Accessors(chain = true)
    public static class Detail {
        /**
         * 订单原价
         */
        @JsonProperty("cost_price")
        private Integer costPrice;
        /**
         * 商品小票ID
         */
        @JsonProperty("invoice_id")
        private String invoiceId;
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
         * 商品数量
         */
        @JsonProperty("quantity")
        private Integer quantity;
        /**
         * 商品单价
         */
        @JsonProperty("unit_price")
        private Integer unitPrice;
    }

    /**
     * 场景信息
     */
    @Data
    @Accessors(chain = true)
    public static class SceneInfo {
        /**
         * 商户端设备号
         */
        @JsonProperty("device_id")
        private String deviceId;
        /**
         * 用户终端IP
         * <p>
         * from {@link WeChatPaymentJsapiRequest}
         */
        @JsonProperty("payer_client_ip")
        private String payerClientIp;
        /**
         * 商户门店信息
         * <p>
         * from {@link WeChatPaymentJsapiRequest}
         */
        @JsonProperty("store_info")
        private StoreInfo storeInfo;
    }

    /**
     * 商户门店信息
     */
    @Data
    @Accessors(chain = true)
    public static class StoreInfo {
        /**
         * 门店编号
         */
        @JsonProperty("id")
        private String id;
        /**
         * 门店名称
         */
        @JsonProperty("name")
        private String name;
        /**
         * 地区编码
         */
        @JsonProperty("area_code")
        private String areaCode;
        /**
         * 详细地址
         */
        @JsonProperty("address")
        private String address;
    }

    /**
     * 结算信息
     */
    @Data
    @Accessors(chain = true)
    public static class SettleInfo {
        /**
         * 是否指定分账
         */
        @JsonProperty("profit_sharing")
        private Boolean profitSharing;
    }

}
