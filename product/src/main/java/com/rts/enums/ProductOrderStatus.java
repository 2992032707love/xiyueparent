package com.rts.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProductOrderStatus {

    /**
     * 支付成功，待发货
     */
    SUCCESS_WAIt_FOR_SHIPMENT("用户已支付成功，待发货"),

    /**
     * 已发货，运输中
     */
    SHIPPED_IN_TRANSIT("已发货，运输中"),

    /**
     * 已送达，待收货
     */
    DELIVERED_READY_TO_BE_RECEIVED("已送达，待收货"),

    /**
     * 确认收货
     */
    CONFIRM_RECEIPT("确认收货"),

    /**
     * 用户以取消该订单且已退款
     */
    CANCEL_REFUND_SUCCESS("用户以取消该订单且已退款"),

    /**
     * 退款异常
     */
    REFUND_ABNORMAL("退款异常");

    /**
     * 类型
     */
    private final String type;
}
