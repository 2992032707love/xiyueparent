package com.rts.service.impl;

import com.google.gson.Gson;
import com.rts.entity.PmsPaymentInfo;
import com.rts.enums.PayType;
import com.rts.mapper.PmsPaymentInfoMapper;
import com.rts.service.PmsPaymentInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author rts
 * @since 2023-03-14
 */
@Slf4j
@Service
public class PmsPaymentInfoServiceImpl extends ServiceImpl<PmsPaymentInfoMapper, PmsPaymentInfo> implements PmsPaymentInfoService {

    /**
     * 记录支付日志：支付宝沙箱
     * @param params
     */
    @Override
    public void createPmsPaymentInfoForAliPay(Map<String, String> params) {

        log.info("记录支付日志");

        // 获取订单号
        String orderNo = params.get("out_trade_no");
        // 获取业务编号
        String transactionId = params.get("trade_no");
        // 获取交易状态
        String tradeStatus = params.get("trade_status");
        // 获取交易金额
        String totalAmount = params.get("total_amount");
        BigDecimal totalAmountBig = new BigDecimal(totalAmount);

        PmsPaymentInfo pmsPaymentInfo = new PmsPaymentInfo();
        // 设置订单编号
        pmsPaymentInfo.setOrderNo(orderNo);
        // 设置支付类型
        pmsPaymentInfo.setPaymentType(PayType.ALIPAY.getType());
        // 设置支付系统交易编号
        pmsPaymentInfo.setTrandsctionId(transactionId);
        // 设置交易类型
        pmsPaymentInfo.setTradeType("电脑网站支付");
        // 设置交易状态
        pmsPaymentInfo.setTradeState(tradeStatus);
        // 设置支付金额（元）
        pmsPaymentInfo.setPayerTotal(totalAmountBig);

        Gson gson = new Gson();
        // 要转换的是params,里面是个hashmap，所以把这个hashmap转换成字符串
        String json = gson.toJson(params, HashMap.class);
        pmsPaymentInfo.setContent(json);

        baseMapper.insert(pmsPaymentInfo);
    }
}
