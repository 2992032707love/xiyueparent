package com.rts.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rts.entity.PmsProductOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rts.enums.ProductOrderStatus;

import java.math.BigDecimal;

/**
 * <p>
 * 商品订单表 服务类
 * </p>
 *
 * @author rts
 * @since 2023-04-12
 */
public interface PmsProductOrderService extends IService<PmsProductOrder> {

    IPage<PmsProductOrder> listByBusinessId(int pageNo, int pageSize, String value, Integer businessId);

    String updateOrderStatus(Integer id, String loginId, String orderStatus);

    Boolean add(Integer businessId, Integer customId, String orderNo, String tablename, String title, BigDecimal totalYuan, Integer productSize, String skusItem, Integer skusId, String name, String phone, String address, String orderStatus, String paymentType);

    PmsProductOrder listByOrderNo(String orderNo, Integer customId);

    PmsProductOrder getone(String orderNo, Integer customId);

    Boolean updateOrderStatusByid(Integer id, String orderNo, String orderStatus, Integer customId);

    Boolean updateRefund(String orderNo, ProductOrderStatus cancelRefundSuccess);
}
