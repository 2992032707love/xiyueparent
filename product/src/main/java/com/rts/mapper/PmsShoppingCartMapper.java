package com.rts.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rts.entity.PmsShoppingCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 购物车表 Mapper 接口
 * </p>
 *
 * @author rts
 * @since 2023-04-03
 */
public interface PmsShoppingCartMapper extends BaseMapper<PmsShoppingCart> {
    IPage<PmsShoppingCart> listByCustomId(@Param("customId") Integer customId, IPage<PmsShoppingCart> page);
}
