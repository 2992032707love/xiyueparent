package com.rts.controller;


import com.rts.common.ResultJson;
import com.rts.entity.PmsOrderInfo;
import com.rts.enums.OrderStatus;
import com.rts.service.PmsOrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 订单信息表 前端控制器
 * </p>
 *
 * @author rts
 * @since 2023-03-09
 */
//@CrossOrigin //开放前端的跨域访问
@Api(tags = "商品订单管理")
@RestController
@RequestMapping("/pmsOrderInfo")
@Slf4j
public class PmsOrderInfoController {

    @Resource
    private PmsOrderInfoService pmsOrderInfoService;

    @ApiOperation("订单列表")
    @GetMapping("/list")
    public ResultJson list(Integer pageNo, Integer pageSize, String value, String loginId) {
        log.info("loginId为 ====> " + loginId);
        System.out.println(pmsOrderInfoService.list());
//        List<PmsOrderInfo> list = pmsOrderInfoService.listOrderByCreateTimeDesc();
        return ResultJson.success(pmsOrderInfoService.list(pageNo, pageSize, value, loginId));
    }

    /**
     * 查询本地订单状态
     * @param orderNo
     * @return
     */
    @ApiOperation("查询本地订单状态")
    @GetMapping("/query-order-status/{orderNo}")
    public ResultJson queryOrderStatus(@PathVariable String orderNo){

//        String orderStatus = pmsOrderInfoService.getOrderStatus(orderNo);
//        if(OrderStatus.SUCCESS.getType().equals(orderStatus)){
//            return ResultJson.ok().setMessage("支付成功"); //支付成功
//        }

        return ResultJson.ok().setCode(101).setMessage("支付中......");
    }

}
