package com.rts.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rts.entity.PmsSkus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * sku表 服务类
 * </p>
 *
 * @author rts
 * @since 2022-10-08
 */
public interface PmsSkusService extends IService<PmsSkus> {

    List<PmsSkus> list (Integer productId);

    void updateStockBySkusId(Integer skusId, Integer productSize);

    void updateStockBySkusIdAdd(Integer skusId, Integer productSize);

    Boolean update(Integer id, BigDecimal oldPrice, BigDecimal price, Integer stock);
}
