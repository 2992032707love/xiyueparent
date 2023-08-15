package com.rts.service;

import com.rts.entity.PmsPaymentInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author rts
 * @since 2023-03-14
 */
public interface PmsPaymentInfoService extends IService<PmsPaymentInfo> {

    void createPmsPaymentInfoForAliPay(Map<String, String> params);
}
