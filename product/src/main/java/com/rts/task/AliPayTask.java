package com.rts.task;

import com.rts.entity.PmsOrderInfo;
import com.rts.entity.UmsUser;
import com.rts.enums.PayType;
import com.rts.service.AliPayService;
import com.rts.service.PmsOrderInfoService;
import com.rts.service.impl.UmsUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class AliPayTask {
    @Resource
    private PmsOrderInfoService pmsOrderInfoService;

    @Resource
    private AliPayService aliPayService;

    /**
     * 查询未支付订单
     * 从第0秒开始每隔30秒执行1次，查询创建超过5分钟，并且未支付的订单
     */
    @Scheduled(cron = "0/60 * * * * ?")
    public void orderConfirm(){
        log.info("orderConfirm 被执行......");

        // 1 代表的是查询创建超过一分钟且未支付的订单 返回的是订单列表
        List<PmsOrderInfo> pmsOrderInfos = pmsOrderInfoService.getNoPayOrderByDuration(5, PayType.ALIPAY.getType());

        // 判断支付宝端这个订单是什么状态
        for (PmsOrderInfo pmsOrderInfo : pmsOrderInfos) {
            Integer userId = pmsOrderInfo.getUserId();
            String orderNo = pmsOrderInfo.getOrderNo();
            log.warn("超时订单 ===> {}" + orderNo + " 用户id为 ===> {}" + userId);

//            // 拿到登录用户的id
//            UmsUserServiceImpl umsUserServiceImpl = new UmsUserServiceImpl();
//            System.out.println("拿到的登录用户的id为：" + umsUserServiceImpl.getLogin());

            // 核实订单状态：调用支付宝查单接口
            aliPayService.checkOrderStatus(orderNo);
        }

        log.info("orderConfirm 执行完毕......");
    }
}
