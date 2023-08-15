package com.rts.controller;


import com.rts.common.ResultJson;
import com.rts.service.PmsSkusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * <p>
 * sku表 前端控制器
 * </p>
 *
 * @author rts
 * @since 2022-10-08
 */
@Slf4j
@RestController
@RequestMapping("/pmsSkus")
public class PmsSkusController {
    @Resource
    PmsSkusService pmsSkusService;

    /**
     * 根据主键查询该条属性
     * @param id
     * @return
     */
    @GetMapping("/getone")
    ResultJson getone(Integer id) {
        return ResultJson.success(pmsSkusService.getById(id));
    }

    /**
     * 更新
     * @param id
     * @param oldPrice
     * @param price
     * @param stock
     * @return
     */
    @PostMapping("/update")
    ResultJson update(Integer id, BigDecimal oldPrice, BigDecimal price, Integer stock) {
        return ResultJson.success(pmsSkusService.update(id,oldPrice,price,stock),"更新成功！");
    }
}
