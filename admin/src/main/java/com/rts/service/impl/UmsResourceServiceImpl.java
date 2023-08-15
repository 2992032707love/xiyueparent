package com.rts.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rts.config.MyException;
import com.rts.entity.UmsResource;
import com.rts.mapper.UmsResourceMapper;
import com.rts.service.UmsResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author rts
 * @since 2023-03-02
 */
@Service
@Slf4j
public class UmsResourceServiceImpl extends ServiceImpl<UmsResourceMapper, UmsResource> implements UmsResourceService {
    @Resource(name = "redisTemplate")
    RedisTemplate<String, Object> redisTemplate;

    @Resource
    UmsResourceMapper umsResourceMapper;

    @Override
    @Cacheable(value = "ums", key = "'resources'")
    public List<UmsResource> getAll() {
        return this.getByParentId(0);
    }

    @Override
    @CacheEvict(value = "ums", key = "'resources'")
    public Boolean add(String name, Integer type, Integer parentId, Integer level, String frontUrl, String backUrl) {
        UmsResource umsResource = new UmsResource(
                name,
                parentId,
                level,
                type,
                frontUrl,
                backUrl
        );
        return this.save(umsResource);
    }

    @Override
    @CacheEvict(value = "ums", key = "'resources'")
    public Boolean update(Integer id, String name, Integer type, String frontUrl, String backUrl) {
        UmsResource umsResource = new UmsResource(
            id,
            name,
            type,
            frontUrl,
            backUrl
        );
        return this.updateById(umsResource);
    }

    @Override
    @CacheEvict(value = "ums", key = "'resources'")
    public Boolean del(Integer id) {
        QueryWrapper<UmsResource> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        if (this.count(wrapper) > 0) {
            throw new MyException("存在未删除的下级，无法删除");
        }
        return this.removeById(id);
    }

    @Override
    public List<UmsResource> getByUserId(Integer userId) {
        return umsResourceMapper.getByUserId(userId);
    }

    private List<UmsResource> getByParentId(Integer parentId) {
        QueryWrapper<UmsResource> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", parentId);
        List<UmsResource> list = this.list(wrapper);
        for (UmsResource umsResource : list) {
            umsResource.setChildren(this.getByParentId(umsResource.getId()));
        }
        return list;
    }
}
