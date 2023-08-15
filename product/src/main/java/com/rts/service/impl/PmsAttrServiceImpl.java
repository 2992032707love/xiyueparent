package com.rts.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rts.entity.PmsAttr;
import com.rts.entity.PmsCategory;
import com.rts.mapper.PmsAttrMapper;
import com.rts.service.PmsAttrService;
import com.rts.service.PmsCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品属性表 服务实现类
 * </p>
 *
 * @author rts
 * @since 2022-09-28
 */
@Service
public class PmsAttrServiceImpl extends ServiceImpl<PmsAttrMapper, PmsAttr> implements PmsAttrService {
    @Resource
    PmsCategoryService pmsCategoryService;
    @Override
    public List<PmsAttr> list(Integer categoryId, Integer type) {
        List<PmsAttr> dataList = new ArrayList<>();
        this.getParentList(categoryId, type, dataList);
        QueryWrapper<PmsAttr> wrapper = new QueryWrapper<>();
        wrapper.eq("category_id", categoryId)
                .eq("type", type);
        dataList.addAll(this.list(wrapper));
        return dataList;
    }

    private void  getParentList(Integer categoryId, Integer type, List<PmsAttr> dataList) {
        PmsCategory pmsCategory = pmsCategoryService.getById(categoryId);
        PmsCategory parent = pmsCategoryService.getById(pmsCategory.getParentId());
        if (parent != null) {
            this.getParentList(parent.getId(), type, dataList);
            QueryWrapper<PmsAttr> wrapper = new QueryWrapper<>();
            wrapper.eq("category_id", parent.getId())
                    .eq("type", type);
            List<PmsAttr> list = this.list(wrapper);
            // 一个list里面再添加一个list用addAll()
            dataList.addAll(list);
        }
    }
    @Override
    public boolean add(String name, Integer categoryId, Integer inputType, String inputList, Integer type) {
        PmsAttr pmsAttr = new PmsAttr(name, categoryId, inputType, inputList, type);
        return this.save(pmsAttr);
    }
    @Override
    public boolean update(Integer id, String name, Integer inputType, String inputList) {
        PmsAttr pmsAttr = new PmsAttr(id, name, inputType, inputList);
        return this.updateById(pmsAttr);
    }

    @Override
    public List<PmsAttr> getActiveByCategoryIds(Long[] categoryIds) {
        QueryWrapper<PmsAttr> wrapper = new QueryWrapper<>();
        wrapper.in("category_id", categoryIds);
        return this.list(wrapper);
    }
}
