package com.rts.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rts.entity.UmsRoleResource;

import java.util.List;

/**
 * <p>
 * 角色关联资源  服务类
 * </p>
 *
 * @author rts
 * @since 2023-03-03
 */
public interface UmsRoleResourceService extends IService<UmsRoleResource> {
    Boolean save (Integer roleId, Integer[] menus, Integer[] btns);
    List<UmsRoleResource> getBtnsByRoleId (Integer roleId);
}
