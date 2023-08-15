package com.rts.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rts.entity.PmsProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author rts
 * @since 2022-09-29
 */
public interface PmsProductMapper extends BaseMapper<PmsProduct> {
    List<PmsProduct> pageGetByName(@Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize, @Param("value") String value);

    IPage<PmsProduct> getByCustomId(@Param("customId") Integer customId, IPage<PmsProduct> page);
}
