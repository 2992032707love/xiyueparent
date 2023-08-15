package com.rts.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rts.config.MyException;
import com.rts.entity.PmsShoppingCart;
import com.rts.mapper.PmsShoppingCartMapper;
import com.rts.service.PmsShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * 购物车表 服务实现类
 * </p>
 *
 * @author rts
 * @since 2023-04-03
 */
@Service
public class PmsShoppingCartServiceImpl extends ServiceImpl<PmsShoppingCartMapper, PmsShoppingCart> implements PmsShoppingCartService {

    /**
     * 根据买家用户id分页查询该用户购物车的所有信息
     * @param pageNo
     * @param pageSize
     * @param loginId
     * @return
     */
    @Override
    public IPage<PmsShoppingCart> listByCustomId(Integer pageNo, Integer pageSize, String loginId) {

        System.out.println("这里是list的：" + pageNo);
        System.out.println("这里是list的：" + pageSize);
        System.out.println("这里是list的：" + loginId);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("custom_id", loginId);
        wrapper.orderByDesc("id");
        return this.page(new Page<>(pageNo, pageSize), wrapper);
    }

    /**
     * 根据登录id查询该买家用户购物车里的有多少件商品
     * @param loginId
     * @return
     */
    @Override
    public int selectCount(String loginId) {
        QueryWrapper<PmsShoppingCart> wrapper = new QueryWrapper<>();
        wrapper.eq("custom_id", loginId);
        return baseMapper.selectCount(wrapper);
    }

    /**
     * 添加商品到购物车
     * @param customId
     * @param productId
     * @param skusId
     * @param productSize
     * @param prc
     * @return
     */
    @Override
    public String add(Integer customId, Integer productId, Integer skusId, Integer productSize, BigDecimal prc) {
        QueryWrapper<PmsShoppingCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("custom_id", customId);
        queryWrapper.eq("product_id", productId);
        queryWrapper.eq("skus_id", skusId);
        PmsShoppingCart pms = this.getOne(queryWrapper);
        if (pms == null) {
            PmsShoppingCart pmsShoppingCart = new PmsShoppingCart(customId,productId,skusId,productSize,prc);
            this.save(pmsShoppingCart);
            return "添加购物车成功！";
        } else {
            PmsShoppingCart pmsShoppingCart = new PmsShoppingCart(productSize,prc);
            baseMapper.update(pmsShoppingCart, queryWrapper);
            return "购物车中已经有该款商品，更新商品信息成功！";
        }
    }

    /**
     * 根据主键和买家用户id删除该用户的购物车中对应的商品信息
     * @param id
     * @param customId
     * @return
     */
    @Override
    public String delete(Integer id, Integer customId) {
        QueryWrapper<PmsShoppingCart> wrapper = new QueryWrapper<>();
        wrapper.eq("custom_id", customId);
        wrapper.eq("id", id);
        PmsShoppingCart pmsShoppingCart = this.getOne(wrapper);
        if (pmsShoppingCart != null) {
            this.removeById(id);
            return "删除成功！";
        } else {
            return "删除失败!";
        }
    }


}
