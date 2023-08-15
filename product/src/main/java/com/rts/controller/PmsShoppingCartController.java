package com.rts.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rts.common.ResultJson;
import com.rts.mapper.PmsShoppingCartMapper;
import com.rts.service.PmsShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * <p>
 * 购物车表 前端控制器
 * </p>
 *
 * @author rts
 * @since 2023-04-03
 */
@Slf4j
@RestController
@RequestMapping("/pmsShoppingCart")
public class PmsShoppingCartController {
    @Resource
    PmsShoppingCartService pmsShoppingCartService;
    @Resource
    PmsShoppingCartMapper pmsShoppingCartMapper;

    /**
     * 根据买家用户id分页查询该用户购物车的所有信息
     * @param pageNo
     * @param pageSize
     * @param loginId
     * @return
     */
    @GetMapping("/list")
    ResultJson list(Integer pageNo, Integer pageSize, String loginId) {
        System.out.println("这里是list的：" + pageNo);
        System.out.println("这里是list的：" + pageSize);
        System.out.println("这里是list的：" + loginId);
        return ResultJson.success(pmsShoppingCartService.listByCustomId(pageNo,pageSize,loginId));
    }

    /**
     * 根据登录id查询该买家用户购物车里的有多少件商品
     * @param loginId
     * @return
     */
    @GetMapping ("/selectCount")
    ResultJson selectCount(String loginId) {
        return ResultJson.success(pmsShoppingCartService.selectCount(loginId));
    }

    /**
     * 添加和更新商品信息到购物车
     * @param productId
     * @param skusId
     * @param prc
     * @param productSize
     * @param loginId
     * @return
     */
    @PostMapping("/add")
    ResultJson add (Integer productId, Integer skusId, BigDecimal prc, Integer productSize, String loginId) {
        Integer customId = Integer.valueOf(loginId);
        return ResultJson.success(pmsShoppingCartService.add(customId, productId, skusId, productSize, prc));
    }

    /**
     * 根据主键和买家用户id删除对应的商品信息
     * @param id
     * @param loginId
     * @return
     */
    @PostMapping("/delete")
    ResultJson delete (Integer id, String loginId) {
        Integer customId = Integer.valueOf(loginId);
        return ResultJson.success(pmsShoppingCartService.delete(id,customId));
    }

    @GetMapping("/listByCustomId")
    ResultJson listByCustomId (Integer pageNo, Integer pageSize, String loginId){
        Integer customId = Integer.valueOf(loginId);
        return ResultJson.success(pmsShoppingCartMapper.listByCustomId(customId, new Page<>(pageNo,pageSize)));
    }
}
