package com.rts.controller;


import com.rts.common.ResultJson;
import com.rts.entity.PmsProductOrder;
import com.rts.service.PmsProductOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 商品订单表 前端控制器
 * </p>
 *
 * @author rts
 * @since 2023-04-12
 */
@Slf4j
@RestController
@RequestMapping("/pmsProductOrder")
public class PmsProductOrderController {

    @Resource
    PmsProductOrderService pmsProductOrderService;

    /**
     * 分页查询商品订单表的信息
     * @param pageNo
     * @param pageSize
     * @param value
     * @param loginId
     * @return
     */
    @GetMapping("/list")
    ResultJson listByBusinessId(Integer pageNo, Integer pageSize, String value, String loginId) {
        log.info("loginId为 ====> " + loginId);
        Integer businessId = Integer.valueOf(loginId);
        return ResultJson.success(pmsProductOrderService.listByBusinessId(pageNo, pageSize, value, businessId));
    }

    /**
     * 更新订单状态
     * @param id
     * @param loginId
     * @param orderStatus
     * @return
     */
    @PostMapping("/updateOrderStatus")
    ResultJson updateOrderStatus(Integer id, String loginId, String orderStatus) {
        System.out.println( "这里是orderStatus：" + orderStatus);
        System.out.println( "这里是id：" + id);
        System.out.println( "这里是loginId：" + loginId);
        return ResultJson.success(pmsProductOrderService.updateOrderStatus(id, loginId, orderStatus));
    }

    /**
     * 买家用户查看商品订单状态
     * @param orderNo
     * @param loginId
     * @return
     */
    @PostMapping("/getOne")
    ResultJson getOne(String orderNo, String loginId) {
        System.out.println("这里是orderNo：" + orderNo);
        System.out.println("这里是loginId：" + loginId);
        Integer customId = Integer.valueOf(loginId);
        return ResultJson.success(pmsProductOrderService.getone(orderNo, customId));
    }

    /**
     * 买家用户确认收货
     * @param id
     * @param orderStatus
     * @param orderNo
     * @return
     */
    @PostMapping("/update")
    ResultJson updateOrderStatusByid(Integer id, String orderStatus, String orderNo, String loginId) {
        Integer customId = Integer.valueOf(loginId);
        return ResultJson.success(pmsProductOrderService.updateOrderStatusByid(id, orderNo, orderStatus, customId));
    }
}
