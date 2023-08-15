package com.rts.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rts.entity.PmsOrderInfo;
import com.rts.entity.PmsRefundInfo;
import com.rts.mapper.PmsRefundInfoMapper;
import com.rts.service.PmsOrderInfoService;
import com.rts.service.PmsRefundInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rts.util.OrderNoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 退款单表 服务实现类
 * </p>
 *
 * @author rts
 * @since 2023-03-15
 */
@Slf4j
@Service
public class PmsRefundInfoServiceImpl extends ServiceImpl<PmsRefundInfoMapper, PmsRefundInfo> implements PmsRefundInfoService {

    @Resource
    private PmsOrderInfoService pmsOrderInfoService;
    /**
     * 根据订单号创建退款订单
     * @param orderNo
     * @param reason
     * @param userId
     * @return
     */
    @Override
    public PmsRefundInfo createRefundByOrderNoForAliPay(String orderNo, String reason, Integer userId) {

        log.info("根据订单号创建退款订单", orderNo + "----" + reason + "----" + userId);
        // 根据订单号获取订单信息
        PmsOrderInfo pmsOrderInfo = pmsOrderInfoService.getOrderByOrderNo(orderNo);

        // 根据订单号生成退款订单
        PmsRefundInfo pmsRefundInfo = new PmsRefundInfo();
        pmsRefundInfo.setOrderNo(orderNo); // 订单编号
        pmsRefundInfo.setRefundNo(OrderNoUtils.getPmsRefundNo()); // 退款单编号

        pmsRefundInfo.setTotalYuan(pmsOrderInfo.getTotalYuan());// 原订单金额(元)
        pmsRefundInfo.setRefund(pmsOrderInfo.getTotalYuan());// 退款金额(元)
        pmsRefundInfo.setReason(reason);// 退款原因
        pmsRefundInfo.setUserId(pmsOrderInfo.getUserId());// 用户id
        pmsRefundInfo.setBusinessId(pmsOrderInfo.getBusinessId());// 商家用户id

        // 保存退款订单
        baseMapper.insert(pmsRefundInfo);
        log.info("根据订单号创建退款订单结束,创建的退款订单为：", pmsRefundInfo);
        return pmsRefundInfo;
    }

    @Override
    public void updateRefundForAliPay(String refundNo, String content, String refundStatus) {

        log.info("根据退款单编号修改退款单 ===>", refundNo + " --- " + content + " 退款状态:" + refundStatus);
        // 根据退款单编号修改退款单
        QueryWrapper<PmsRefundInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("refund_no", refundNo);

        // 设置要修改的字段
        PmsRefundInfo pmsRefundInfo = new PmsRefundInfo();
        pmsRefundInfo.setRefundStatus(refundStatus);// 退款状态
        pmsRefundInfo.setContentReturn(content);// 将全部响应结果存入数据库的content字段

        // 更新退款单
        baseMapper.update(pmsRefundInfo, queryWrapper);
        log.info("更新退款单完成!");
    }

    /**
     * 根据商户订单编号查询
     * @param orderNo
     * @param businessId
     * @return
     */
    @Override
    public PmsRefundInfo getone(String orderNo, Integer businessId) {
        QueryWrapper<PmsRefundInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        queryWrapper.eq("business_id", businessId);

        PmsRefundInfo one = this.getOne(queryWrapper);
        return one;
    }
}
