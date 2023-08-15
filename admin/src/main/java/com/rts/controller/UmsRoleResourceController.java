package com.rts.controller;


import com.rts.common.ResultJson;
import com.rts.service.UmsResourceService;
import com.rts.service.UmsRoleResourceService;
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
 * 角色关联资源  前端控制器
 * </p>
 *
 * @author rts
 * @since 2023-03-03
 */
@RestController
@RequestMapping("/umsRoleResource")
public class UmsRoleResourceController {
    @Resource
    UmsResourceService umsResourceService;
    @Resource
    UmsRoleResourceService umsRoleResourceService;
    @GetMapping("/getData")
    ResultJson getData (Integer roleId) {
        Map<String, List> map = new HashMap<>();
        map.put("resources", umsResourceService.getAll());
        map.put("values", umsRoleResourceService.getBtnsByRoleId(roleId));
        return ResultJson.success(map);
    }
    @PostMapping("/save")
    ResultJson save (Integer roleId, Integer[] menus, Integer[] btns) {
        return ResultJson.success(umsRoleResourceService.save(roleId, menus, btns), "关联成功");
    }
}
