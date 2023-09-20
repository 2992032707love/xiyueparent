package com.rts.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rts.entity.UmsResource;

import java.util.List;

/**
 * <p>
 * 资源表 Mapper 接口
 * </p>
 *
 * @author rts
 * @since 2023-03-02
 */
public interface UmsResourceMapper extends BaseMapper<UmsResource> {
    List<UmsResource> getByUserId(Integer userId);
}
