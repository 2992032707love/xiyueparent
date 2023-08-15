package com.rts.service;

import com.rts.entity.PmsRefundInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 退款单表 服务类
 * </p>
 *
 * @author rts
 * @since 2023-03-15
 */
public interface PmsRefundInfoService extends IService<PmsRefundInfo> {

    PmsRefundInfo createRefundByOrderNoForAliPay(String orderNo, String reason, Integer userId);

    void updateRefundForAliPay(String refundNo, String content, String refundStatus);

    PmsRefundInfo getone(String orderNo, Integer businessId);
}
