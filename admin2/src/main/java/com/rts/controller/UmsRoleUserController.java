package com.rts.controller;


import com.rts.common.ResultJson;
import com.rts.service.UmsRoleService;
import com.rts.service.UmsRoleUserService;
import com.rts.service.UmsUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色用户关联 前端控制器
 * </p>
 *
 * @author rts
 * @since 2023-03-01
 */
@Slf4j
@RestController
@RequestMapping("/umsRoleUser")
//@CrossOrigin
public class UmsRoleUserController {
    @Resource
    UmsRoleService umsRoleService;
    @Resource
    UmsUserService umsUserService;
    @Resource
    UmsRoleUserService umsRoleUserService;
    @GetMapping("/getDataByUser")
    ResultJson getDataByUser (Integer userId) {
        Map<String, List> map = new HashMap<>();
        map.put("roles", umsRoleService.getActive());
        map.put("values", umsRoleUserService.getByUserId(userId));
        return ResultJson.success(map);
    }
    @GetMapping("/getDataByRole")
    ResultJson getDataByRole (Integer roleId) {
        Map<String, List> map = new HashMap<>();
        map.put("users", umsUserService.getActive());
        map.put("values", umsRoleUserService.getByRoleId(roleId));
        return ResultJson.success(map);
    }
    @PostMapping("/saveByUser")
    ResultJson saveByUser(Integer userId, Integer[] roleIds) {
        return ResultJson.success(umsRoleUserService.saveByUser(userId, roleIds), "关联成功");
    }
    @PostMapping("/saveByRole")
    ResultJson saveByRole(Integer roleId, Integer[] userIds) {
        System.out.println("roleId===> " + roleId);
        System.out.println("userIds===> " + userIds);
        return ResultJson.success(umsRoleUserService.saveByRole(roleId, userIds), "关联成功");
    }
}
