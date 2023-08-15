package com.rts.service;

import java.math.BigDecimal;
import java.util.Map;

public interface AliPayService {

    String tradeCreate(Integer productId, Integer skusId, BigDecimal prc, Integer productSize, String loginId);

    void processOrder(Map<String, String> params);

    String queryOrder(String orderNo);

    void checkOrderStatus(String orderNo);

    void cancelOrder(String orderNo, String loginId);

    void refund(String orderNo, String reason, Integer userId);

    String queryRefund(String orderNo, Integer userId);
}
