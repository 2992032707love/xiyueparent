package com.rts.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rts.entity.UmsRole;
import com.rts.mapper.UmsRoleMapper;
import com.rts.service.UmsRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author rts
 * @since 2023-02-28
 */
@Service
public class UmsRoleServiceImpl extends ServiceImpl<UmsRoleMapper, UmsRole> implements UmsRoleService {

    @Override
    public List<UmsRole> getActive() {
        QueryWrapper<UmsRole> wrapper = new QueryWrapper<>();
        wrapper.eq("active", 1);
        return this.list(wrapper);
    }

    @Override
    public List<UmsRole> list(String value) {
        QueryWrapper<UmsRole> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(value)) {
            wrapper.like("name", value);
        }
        return this.list(wrapper);
    }

    @Override
    public Boolean add(String name) {
        UmsRole umsRole = new UmsRole(name);
        return this.save(umsRole);
    }

    @Override
    public Boolean update(Integer id, String name) {
        UmsRole umsRole = new UmsRole(id,name);
        return this.updateById(umsRole);
    }

    @Override
    public Boolean del(Integer id, Boolean active) {
        UmsRole umsRole = new UmsRole(id,active);
        return this.updateById(umsRole);
    }
}
