package com.rts.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rts.entity.PmsAttr;

import java.util.List;

/**
 * <p>
 * 商品属性表 服务类
 * </p>
 *
 * @author rts
 * @since 2022-09-28
 */
public interface PmsAttrService extends IService<PmsAttr> {
    List<PmsAttr> list(Integer categoryId, Integer type);
    boolean add (String name, Integer categoryId, Integer inputType, String inputList, Integer type);
    boolean update (Integer id, String name, Integer inputType, String inputList);
    List<PmsAttr> getActiveByCategoryIds (Long[] categoryIds);
}
