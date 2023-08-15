package com.rts.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rts.entity.PmsSkus;
import com.rts.mapper.PmsSkusMapper;
import com.rts.service.PmsSkusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * sku表 服务实现类
 * </p>
 *
 * @author rts
 * @since 2022-10-08
 */
@Slf4j
@Service
public class PmsSkusServiceImpl extends ServiceImpl<PmsSkusMapper, PmsSkus> implements PmsSkusService {

    @Override
    public List<PmsSkus> list(Integer productId) {
        QueryWrapper<PmsSkus> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id", productId);
        return this.list(wrapper);
    }

    /**
     * 更新该商品sku属性表中的库存数
     * @param skusId
     * @param productSize
     */
    @Override
    public void updateStockBySkusId(Integer skusId, Integer productSize) {

        log.info("更新商品sku表中对应商品的库存数 ===> {减少}", productSize);
        QueryWrapper<PmsSkus> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", skusId);
        PmsSkus pmsSkus = baseMapper.selectOne(queryWrapper);
        System.out.println("该sku属性为：" + pmsSkus);
        Integer OldStock = pmsSkus.getStock();
        System.out.println("OldStock为：" + OldStock);
        Integer STOCK = 0;
        System.out.println("STOCK的值为：" + STOCK);
        System.out.println("(OldStock - productSize)为：" + (OldStock - productSize));
        STOCK = (OldStock - productSize);
        System.out.println("STOCK的值为：" + STOCK);
        pmsSkus.setStock(STOCK);

        baseMapper.update(pmsSkus, queryWrapper);
        log.info("更新成功(-)!");
    }

    /**
     * 更新该商品sku属性表中的库存数
     * @param skusId
     * @param productSize
     */
    @Override
    public void updateStockBySkusIdAdd(Integer skusId, Integer productSize) {

        log.info("更新商品sku表中对应商品的库存数 ===> {增加}", productSize);
        QueryWrapper<PmsSkus> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", skusId);
        PmsSkus pmsSkus = baseMapper.selectOne(queryWrapper);
        System.out.println("该sku属性为：" + pmsSkus);
        Integer OldStock = pmsSkus.getStock();
        System.out.println("OldStock为：" + OldStock);
        Integer STOCK = 0;
        System.out.println("STOCK的值为：" + STOCK);
        System.out.println("(OldStock - productSize)为：" + (OldStock + productSize));
        STOCK = (OldStock + productSize);
        System.out.println("STOCK的值为：" + STOCK);
        pmsSkus.setStock(STOCK);

        baseMapper.update(pmsSkus, queryWrapper);
        log.info("更新成功(+)!");
    }

    /**
     * 更新商品sku信息
     * @param id
     * @param oldPrice
     * @param price
     * @param stock
     * @return
     */
    @Override
    public Boolean update(Integer id, BigDecimal oldPrice, BigDecimal price, Integer stock) {
        log.info("进入update()更新商品sku信息");
        PmsSkus pmsSkus = new PmsSkus(id,price,stock,oldPrice);
        return updateById(pmsSkus);
    }
}
