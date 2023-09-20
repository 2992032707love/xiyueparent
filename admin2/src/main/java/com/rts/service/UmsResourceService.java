package com.rts.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rts.entity.UmsResource;

import java.util.List;

/**
 * <p>
 * 资源表 服务类
 * </p>
 *
 * @author rts
 * @since 2023-03-02
 */
public interface UmsResourceService extends IService<UmsResource> {
    List<UmsResource> getAll();
    Boolean add(String name, Integer type, Integer parentId, Integer level, String frontUrl, String backUrl);
    Boolean update (Integer id, String name, Integer type, String frontUrl, String backUrl);
    Boolean del(Integer id);
    List<UmsResource> getByUserId(Integer userId);
}
