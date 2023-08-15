package com.rts.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rts.entity.PmsCategory;

import java.util.List;

/**
 * <p>
 * 分类表 服务类
 * </p>
 *
 * @author rts
 * @since 2022-09-28
 */
public interface PmsCategoryService extends IService<PmsCategory> {
    List<PmsCategory> get();

    List<PmsCategory> getActive();

    List<PmsCategory> getActiveByIds(Long[] ids);

    boolean add(String name, Integer parentId, Integer level);

    boolean update(Integer id, String name);

    boolean delete(Integer id, Boolean active);

    String getNameByIds(Integer[] categoryId);

    List<PmsCategory> getAll();
}
