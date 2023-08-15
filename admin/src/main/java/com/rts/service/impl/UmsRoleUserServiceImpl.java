package com.rts.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rts.entity.UmsRole;
import com.rts.entity.UmsRoleUser;
import com.rts.mapper.UmsRoleUserMapper;
import com.rts.service.UmsRoleUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色用户关联 服务实现类
 * </p>
 *
 * @author rts
 * @since 2023-03-01
 */
@Slf4j
@Service
public class UmsRoleUserServiceImpl extends ServiceImpl<UmsRoleUserMapper, UmsRoleUser> implements UmsRoleUserService {

    @Override
    @Transactional
    public Boolean saveByUser(Integer userId, Integer[] roleIds) {
        UpdateWrapper<UmsRoleUser> wrapper = new UpdateWrapper<>();
        wrapper.eq("user_id", userId);
        this.remove(wrapper);
        List<UmsRoleUser> list = new ArrayList<>();
        if (null != roleIds) {
            for (Integer roleId : roleIds) {
                list.add(new UmsRoleUser(roleId, userId));
            }
        }
        return this.saveBatch(list);
    }

    @Override
    public List<UmsRoleUser> getByUserId(Integer userId) {
        QueryWrapper<UmsRoleUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return this.list(wrapper);
    }

    @Override
    public List<UmsRoleUser> getByRoleId(Integer roleId) {
        QueryWrapper<UmsRoleUser> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", roleId);
        return this.list(wrapper);
    }

    @Override
    @Transactional
    public Boolean saveByRole(Integer roleId, Integer[] userIds) {
        UpdateWrapper<UmsRoleUser> wrapper = new UpdateWrapper<>();
        wrapper.eq("role_id", roleId);
        this.remove(wrapper);
        List<UmsRoleUser> list = new ArrayList<>();
        if (null != userIds) {
            for (Integer userid : userIds) {
                list.add(new UmsRoleUser(roleId, userid));
            }
        }
        System.out.println("roleId===> " + roleId);
        System.out.println("userIds===> " + userIds);
        return this.saveBatch(list);
    }
}
