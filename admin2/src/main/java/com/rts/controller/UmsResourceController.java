package com.rts.controller;


import com.rts.common.ResultJson;
import com.rts.service.UmsResourceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 资源表 前端控制器
 * </p>
 *
 * @author rts
 * @since 2023-03-02
 */
@RestController
@RequestMapping("/umsResource")
public class UmsResourceController {
    @Resource
    UmsResourceService umsResourceService;
    @GetMapping("/list")
    ResultJson list () {
        return ResultJson.success(umsResourceService.getAll());
    }
    @PostMapping("/add")
    ResultJson add(String name, Integer type, Integer parentId, Integer level, String frontUrl, String backUrl) {
        return ResultJson.success(umsResourceService.add(name, type, parentId, level, frontUrl, backUrl), "添加资源成功");
    }
    @GetMapping("/getone")
    ResultJson getById (Integer id) {
        return ResultJson.success(umsResourceService.getById(id));
    }
    @PostMapping("/update")
    ResultJson update(Integer id, String name, Integer type, String frontUrl, String backUrl) {
        return ResultJson.success(umsResourceService.update(id, name, type, frontUrl, backUrl), "修改资源成功");
    }
    @PostMapping("/delete")
    ResultJson del(Integer id) {
        return ResultJson.success(umsResourceService.del(id), "删除资源成功");
    }
}
