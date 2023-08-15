package com.rts.service;

import com.rts.entity.UmsRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author rts
 * @since 2023-02-28
 */
public interface UmsRoleService extends IService<UmsRole> {
    List<UmsRole> getActive();
    List<UmsRole> list(String value);
    Boolean add(String name);
    Boolean update(Integer id, String name);
    Boolean del(Integer id, Boolean active);
}
