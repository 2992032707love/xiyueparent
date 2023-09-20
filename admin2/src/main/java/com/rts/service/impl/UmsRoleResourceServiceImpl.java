package com.rts.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rts.entity.UmsRoleResource;
import com.rts.mapper.UmsRoleResourceMapper;
import com.rts.service.UmsRoleResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色关联资源  服务实现类
 * </p>
 *
 * @author rts
 * @since 2023-03-03
 */
@Service
@Slf4j
public class UmsRoleResourceServiceImpl extends ServiceImpl<UmsRoleResourceMapper, UmsRoleResource> implements UmsRoleResourceService {

    @Override
    @Transactional
    public Boolean save(Integer roleId, Integer[] menus, Integer[] btns) {
        UpdateWrapper<UmsRoleResource> wrapper = new UpdateWrapper<>();
        wrapper.eq("role_id", roleId);
        this.remove(wrapper);
        List<UmsRoleResource> list = new ArrayList<>();
        if (null != menus) {
            for (Integer resourceId : menus) {
                list.add(new UmsRoleResource(roleId, resourceId, 1));
            }
        }
        if (null != btns) {
            for (Integer resourceId : btns) {
                list.add(new UmsRoleResource(roleId, resourceId, 0));
            }
        }
        return this.saveBatch(list);
    }

    @Override
    public List<UmsRoleResource> getBtnsByRoleId(Integer roleId) {
        QueryWrapper<UmsRoleResource> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", roleId)
                .eq("resource_type", 0);
        return this.list(wrapper);
    }
}
