package com.rts.enums.zfbpay;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
//  远程服务器的支付订单的状态
public enum AliPayTradeState {
    /**
     * 支付成功
     */
    SUCCESS("TRADE_SUCCESS"),

    /**
     * 未支付
     */
    NOTPAY("WAIT_BUYER_PAY"),

    /**
     * 交易超时已关闭
     */
    CLOSED("TRADE_CLOSED"),

    /**
     * 转入退款
     */
    REFUND("REFUND"),

    /**
     * 退款成功
     */
    REFUND_SUCCESS("REFUND_SUCCESS"),

    /**
     * 退款失败
     */
    REFUND_ERROR("REFUND_ERROR");

    /**
     * 类型
     */
    private final String type;
}
