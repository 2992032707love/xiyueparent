package com.rts.controller;


import com.rts.common.ResultJson;
import com.rts.service.PmsCollectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 收藏表 前端控制器
 * </p>
 *
 * @author rts
 * @since 2023-04-06
 */
@Slf4j
@RestController
@RequestMapping("/pmsCollect")
public class PmsCollectController {

    @Resource
    PmsCollectService pmsCollectService;

    /**
     * 添加收藏
     * @param productId
     * @param loginId
     * @return
     */
    @PostMapping("/add")
    @Transactional
    ResultJson add (Integer productId, String loginId) {
        Integer customId = Integer.valueOf(loginId);
        return ResultJson.yzsuccess(pmsCollectService.add(productId, customId), "添加收藏成功");
    }

    /**
     * 删除收藏
     * @param productId
     * @param loginId
     * @return
     */
    @PostMapping("/delete")
    @Transactional
    ResultJson del (Integer productId, String loginId) {
        Integer customId = Integer.valueOf(loginId);
        return ResultJson.success(pmsCollectService.del(productId, customId), "删除收藏成功");
    }
}
