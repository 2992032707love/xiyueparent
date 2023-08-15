package com.rts.controller;


import com.rts.common.ResultJson;
import com.rts.service.PmsRefundInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 退款单表 前端控制器
 * </p>
 *
 * @author rts
 * @since 2023-03-15
 */
@Slf4j
@RestController
@RequestMapping("/pmsRefundInfo")
public class PmsRefundInfoController {

    @Resource
    PmsRefundInfoService pmsRefundInfoService;

    /**
     * 根据商户订单编号查询
     * @param orderNo
     * @param loginId
     * @return
     */
    @GetMapping("/getone")
    ResultJson getOne(String orderNo, String loginId) {
        Integer businessId = Integer.valueOf(loginId);
        return ResultJson.success(pmsRefundInfoService.getone(orderNo, businessId));
    }

}
