package com.rts.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rts.config.MyException;
import com.rts.entity.PmsCategory;
import com.rts.mapper.PmsCategoryMapper;
import com.rts.service.PmsCategoryService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 分类表 服务实现类
 * </p>
 *
 * @author rts
 * @since 2022-09-28
 */
@Service
public class PmsCategoryServiceImpl extends ServiceImpl<PmsCategoryMapper, PmsCategory> implements PmsCategoryService {

//    @Resource(name = "redisTemplate")
//    RedisTemplate<String, Object> redisTemplate;

//    @Override
//    @Cacheable(value = "pms", key = "'category'")
//    public List<PmsCategory> get() {
//        final String key = "pms::category";
//        if (redisTemplate.hasKey(key)) {
//            String str = (redisTemplate.opsForValue().get(key)).toString();
//            List<PmsCategory> pmsCategories = JSONObject.parseArray(str, PmsCategory.class);
//            return pmsCategories;
//        }
//        List<PmsCategory> list = this.getByParentId(0, null);
//        redisTemplate.opsForValue().set(key, list, 30, TimeUnit.DAYS);
//        return this.getByParentId(0, null);
//    }


    @Override
    @Cacheable(value = "pms", key = "'category'")
    public List<PmsCategory> get() {
        return this.getByParentId(0, null);
    }

    @Override
    public List<PmsCategory> getActive() {
        return this.getByParentId(0, true);
    }

    @Override
    public List<PmsCategory> getActiveByIds(Long[] ids) {
        return this.listByIds(Arrays.asList(ids));
    }

//    private  List<PmsCategory> getByParentId(Integer parentId, Boolean active) {
//        QueryWrapper<PmsCategory> wrapper = new QueryWrapper<>();
//        wrapper.eq("parent_id", parentId);
//        if (active != null) {
//            wrapper.eq("active", active);
//        }
//        List<PmsCategory> list = this.list(wrapper);
//        for (PmsCategory pmsCategory : list) {
//           pmsCategory.setChildren(this.getByParentId(pmsCategory.getId(), active));
//        }
//        return list;
//    }
    private List<PmsCategory> getByParentId(Integer parentId, Boolean active) {
        QueryWrapper<PmsCategory> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", parentId);
        if (active != null) {
            wrapper.eq("active", active);
        }
        List<PmsCategory> list = this.list(wrapper);
        for(PmsCategory pmsCategory : list) {
            pmsCategory.setChildren( this.getByParentId(pmsCategory.getId(), active));
        }
        return list;
    }
    @Override
    @Caching(evict = { @CacheEvict(value = "pms", key = "'category'"),@CacheEvict(value = "pmscus", key = "'catCustom'")})
//    @CacheEvict(value = "pms", key = "'category'")
    public boolean add(String name, Integer parentId, Integer level) {
        PmsCategory pmsCategory = new PmsCategory(name, parentId, level);
        return this.save(pmsCategory);
    }

    @Override
    @Caching(evict = { @CacheEvict(value = "pms", key = "'category'"),@CacheEvict(value = "pmscus", key = "'catCustom'")})
//    @CacheEvict(value = "pms", key = "'category'")
    public boolean update(Integer id, String name) {
        QueryWrapper<PmsCategory> wrapper = new QueryWrapper<>();
        PmsCategory pmsCategory = new PmsCategory(id, name);
        return this.updateById(pmsCategory);
    }

    @Override
    @Caching(evict = { @CacheEvict(value = "pms", key = "'category'"),@CacheEvict(value = "pmscus", key = "'catCustom'")})
//    @CacheEvict(value = "pms", key = "'category'")
    public boolean delete(Integer id, Boolean active) {
        QueryWrapper<PmsCategory> wrapper = new QueryWrapper<>();
        if (!active) {
            wrapper.eq("parent_id", id)
                    .eq("active", 1);
            if (this.count(wrapper) > 0) {
                throw  new MyException("存在未删除的下级，无法删除当前分类");
            }
        } else {
            PmsCategory category = this.getById(id);
            if (category.getParentId() != 0) {
                PmsCategory parent = this.getById(category.getParentId());
                if (!parent.getActive()) {
                    throw new MyException("必须先恢复上级分类");
                }
            }
//            this.getOne(wrapper);
        }
        PmsCategory pmsCategory = new PmsCategory(id, active);
        return this.updateById(pmsCategory);
    }

    @Override
    public String getNameByIds(Integer[] categoryIds) {
        List<String> list = new ArrayList<>();
        for (Integer id : categoryIds) {
            list.add(this.getById(id).getName());
        }
        return String.join("/", list);
    }

    /**
     * 查询所有有效分类
     * @return
     */
    @Override
    @Cacheable(value = "pmscus", key = "'catCustom'")
    public List<PmsCategory> getAll() {
        return this.getByParentId(0, true);
    }
}
