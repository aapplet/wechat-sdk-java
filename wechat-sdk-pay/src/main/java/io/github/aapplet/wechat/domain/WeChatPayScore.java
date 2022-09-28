package io.github.aapplet.wechat.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.request.WeChatPayScoreCompleteRequest;
import io.github.aapplet.wechat.request.WeChatPayScoreCreateRequest;
import io.github.aapplet.wechat.response.WeChatPayScoreModifyResponse;
import lombok.Data;
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
    @Accessors(chain = true)
    public static class PostPayment {
        /**
         * 付费项目名称
         */
        @JsonProperty("name")
        private String name;
        /**
         * 付费数量
         */
        @JsonProperty("count")
        private Integer count;
        /**
         * 金额
         */
        @JsonProperty("amount")
        private Integer amount;
        /**
         * 计费说明
         */
        @JsonProperty("description")
        private String description;
    }

    /**
     * 后付费商户优惠
     */
    @Data
    @Accessors(chain = true)
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
         * <p>
         * except {@link WeChatPayScoreCreateRequest}
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
     * 订单风险金
     */
    @Data
    @Accessors(chain = true)
    public static class RiskFund {
        /**
         * 风险金名称
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
    @Accessors(chain = true)
    public static class TimeRange {
        /**
         * 服务开始时间
         */
        @JsonProperty("start_time")
        private String startTime;
        /**
         * 服务开始时间备注
         */
        @JsonProperty("start_time_remark")
        private String startTimeRemark;
        /**
         * 预计服务结束时间
         */
        @JsonProperty("end_time")
        private String endTime;
        /**
         * 预计服务结束时间备注
         */
        @JsonProperty("end_time_remark")
        private String endTimeRemark;
    }

    /**
     * 服务位置
     */
    @Data
    @Accessors(chain = true)
    public static class Location {
        /**
         * 服务开始地点
         * <p>
         * except {@link WeChatPayScoreCompleteRequest}
         */
        @JsonProperty("start_location")
        private String startLocation;
        /**
         * 预计服务结束位置
         */
        @JsonProperty("end_location")
        private String endLocation;
    }

    /**
     * 收款信息
     */
    @Data
    @Accessors(chain = true)
    public static class Collection {
        /**
         * 收款状态
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
    @Accessors(chain = true)
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
         * <p>
         * except {@link WeChatPayScoreModifyResponse}
         */
        @JsonProperty("promotion_detail")
        private List<WeChatPromotion.PromotionDetail> promotionDetail;
    }

    /**
     * 内容信息详情
     */
    @Data
    @Accessors(chain = true)
    public static class SyncDetail {
        /**
         * 收款成功时间
         */
        @JsonProperty("paid_time")
        private String paidTime;
    }

}