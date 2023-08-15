package com.rts.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rts.entity.PmsShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * 购物车表 服务类
 * </p>
 *
 * @author rts
 * @since 2023-04-03
 */
public interface PmsShoppingCartService extends IService<PmsShoppingCart> {

    IPage<PmsShoppingCart> listByCustomId(Integer pageNo, Integer pageSize, String loginId);

    int selectCount(String loginId);

    String add(Integer customId, Integer productId, Integer skusId, Integer productSize, BigDecimal prc);

    String delete(Integer id, Integer customId);
}
