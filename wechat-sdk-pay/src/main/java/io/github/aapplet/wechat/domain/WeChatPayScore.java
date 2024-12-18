package io.github.aapplet.wechat.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 微信支付分
 */
public class WeChatPayScore {

    /**
     * 后付费项目
     */
    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostPayment {
        /**
         * 后付费项目名称
         */
        @JsonProperty("name")
        private String name;
        /**
         * 后付费项目的数量
         */
        @JsonProperty("count")
        private Integer count;
        /**
         * 后付费项目金额
         */
        @JsonProperty("amount")
        private Integer amount;
        /**
         * 后付费项目说明
         */
        @JsonProperty("description")
        private String description;
    }

    /**
     * 后付费商户优惠
     */
    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostDiscount {
        /**
         * 优惠名称
         */
        @JsonProperty("name")
        private String name;
        /**
         * 优惠数量
         */
        @JsonProperty("count")
        private Integer count;
        /**
         * 优惠金额
         */
        @JsonProperty("amount")
        private Integer amount;
        /**
         * 优惠说明
         */
        @JsonProperty("description")
        private String description;
    }

    /**
     * 服务风险金
     */
    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RiskFund {
        /**
         * 风险名称
         */
        @JsonProperty("name")
        private String name;
        /**
         * 风险金额
         */
        @JsonProperty("amount")
        private Integer amount;
        /**
         * 风险说明
         */
        @JsonProperty("description")
        private String description;
    }

    /**
     * 服务时间段
     */
    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeRange {
        /**
         * 服务开始时间
         */
        @JsonProperty("start_time")
        private String startTime;
        /**
         * 服务结束时间
         */
        @JsonProperty("end_time")
        private String endTime;
        /**
         * 服务开始时间备注
         */
        @JsonProperty("start_time_remark")
        private String startTimeRemark;
        /**
         * 服务结束时间备注
         */
        @JsonProperty("end_time_remark")
        private String endTimeRemark;
    }

    /**
     * 服务位置
     */
    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location {
        /**
         * 服务开始地点
         */
        @JsonProperty("start_location")
        private String startLocation;
        /**
         * 服务结束地点
         */
        @JsonProperty("end_location")
        private String endLocation;
    }

    /**
     * 收款信息
     */
    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Collection {
        /**
         * 收款状态
         * <li>USER_PAYING：待支付</li>
         * <li>USER_PAID：已支付</li>
         */
        @JsonProperty("state")
        private String state;
        /**
         * 总收款金额
         */
        @JsonProperty("total_amount")
        private Integer totalAmount;
        /**
         * 待收金额
         */
        @JsonProperty("paying_amount")
        private Integer payingAmount;
        /**
         * 已收金额
         */
        @JsonProperty("paid_amount")
        private Integer paidAmount;
        /**
         * 收款明细列表
         */
        @JsonProperty("details")
        private List<CollectionDetail> details;
    }

    /**
     * 收款明细列表
     */
    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CollectionDetail {
        /**
         * 收款序号
         */
        @JsonProperty("seq")
        private Integer seq;
        /**
         * 单笔收款金额
         */
        @JsonProperty("amount")
        private Integer amount;
        /**
         * 收款成功渠道
         */
        @JsonProperty("paid_type")
        private String paidType;
        /**
         * 收款成功时间
         */
        @JsonProperty("paid_time")
        private String paidTime;
        /**
         * 微信支付交易单号
         */
        @JsonProperty("transaction_id")
        private String transactionId;
        /**
         * 优惠功能
         */
        @JsonProperty("promotion_detail")
        private List<WeChatPromotion.PromotionDetail> promotionDetails;
    }

}