package com.rts.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rts.entity.UmsRoleUser;

import java.util.List;

/**
 * <p>
 * 角色用户关联 服务类
 * </p>
 *
 * @author rts
 * @since 2023-03-01
 */
public interface UmsRoleUserService extends IService<UmsRoleUser> {
    Boolean saveByUser (Integer userId, Integer[] roleIds);
    List<UmsRoleUser> getByUserId(Integer userId);
    List<UmsRoleUser> getByRoleId(Integer roleId);
    Boolean saveByRole (Integer roleId, Integer[] userIds);
}
