package com.rts.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rts.entity.PmsOrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rts.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 订单信息表 服务类
 * </p>
 *
 * @author rts
 * @since 2023-03-09
 */
public interface PmsOrderInfoService extends IService<PmsOrderInfo> {

    PmsOrderInfo createOrderByProductId(Integer productId, Integer skusId, BigDecimal prc, Integer productSize, String loginId, String paymentType);

    List<PmsOrderInfo> listOrderByCreateTimeDesc();

    PmsOrderInfo getOrderByOrderNo(String orderNo);

    String getOrderStatus(String orderNo);

    void updateStatusByOrderNo(String orderNo, OrderStatus orderStatus);

    IPage<PmsOrderInfo> list(int pageNo, int pageSize, String value, String loginId);

    List<PmsOrderInfo> getNoPayOrderByDuration(int minutes, String paymentType);

    void updateStatusByOrderNoUserId(String orderNo, Integer userId, OrderStatus orderStatus);
}
