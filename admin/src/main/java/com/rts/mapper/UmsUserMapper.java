package com.rts.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rts.entity.UmsUser;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author rts
 * @since 2022-09-17
 */
public interface UmsUserMapper extends BaseMapper<UmsUser> {
    List<UmsUser> queryAll(UmsUser umsUser);
    UmsUser getUser(Integer id);

}
