package com.rts.controller;


import com.rts.common.ResultJson;
import com.rts.service.UmsRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author rts
 * @since 2023-02-28
 */
@Slf4j
@RestController
@RequestMapping("/umsRole")
public class UmsRoleController {
    @Resource
    UmsRoleService umsRoleService;
    @GetMapping("/list")
    ResultJson list(String value) {
        System.out.println(value);
        return ResultJson.success(umsRoleService.list(value));
    }
    @PostMapping("/add")
    ResultJson add(String name) {
        return ResultJson.success(umsRoleService.add(name), "添加角色成功");
    }
    @PostMapping("/update")
    ResultJson update(Integer id, String name) {
        return ResultJson.success(umsRoleService.update(id,name), "修改角色成功");
    }
    @PostMapping("/delete")
    ResultJson del(Integer id, Boolean active) {
        return ResultJson.success(umsRoleService.del(id, active), active ? "恢复角色成功" : "删除角色成功");
    }
    @GetMapping("/getone")
    ResultJson getOne(Integer id) {
        System.out.println("umsRoleService.getById(id):  " + umsRoleService.getById(id));
        return ResultJson.success(umsRoleService.getById(id));
    }
}
