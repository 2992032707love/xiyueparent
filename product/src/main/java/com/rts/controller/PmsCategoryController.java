package com.rts.controller;


import com.rts.common.ResultJson;
import com.rts.service.PmsCategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 分类表 前端控制器
 * </p>
 *
 * @author rts
 * @since 2022-09-28
 */
@RestController
@RequestMapping("/pmsCategory")
public class PmsCategoryController {
    @Resource
    PmsCategoryService pmsCategoryService;

    @GetMapping("/list")
    ResultJson list() {
        // mybatis-plus给我生成的list() 用来查所有数据
        return ResultJson.success(pmsCategoryService.get());
    }
    @GetMapping("/getAll")
    ResultJson getAll () {
        return ResultJson.success(pmsCategoryService.getAll());
    }
    @PostMapping("/add")
    ResultJson add(String name, Integer parentId, Integer level) {
        return ResultJson.success(pmsCategoryService.add(name, parentId, level), "添加分类成功");
    }
    @PostMapping("/update")
    ResultJson update(Integer id, String name) {
        return ResultJson.success(pmsCategoryService.update(id, name), "修改分类成功");
    }
    @GetMapping("/getone")
    ResultJson getOne(Integer id) {
        return ResultJson.success(pmsCategoryService.getById(id));
    }

    @PostMapping("/delete")
    ResultJson delete(Integer id, Boolean active){
        return ResultJson.success(pmsCategoryService.delete(id, active), active ? "恢复分类成功" : "删除分类成功");
    }
}
