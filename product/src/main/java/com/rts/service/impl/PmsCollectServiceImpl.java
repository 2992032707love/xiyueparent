package com.rts.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rts.config.MyException;
import com.rts.entity.PmsCollect;
import com.rts.entity.UmsUser;
import com.rts.mapper.PmsCollectMapper;
import com.rts.service.PmsCollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 收藏表 服务实现类
 * </p>
 *
 * @author rts
 * @since 2023-04-06
 */
@Slf4j
@Service
@Getter
@Data
public class PmsCollectServiceImpl extends ServiceImpl<PmsCollectMapper, PmsCollect> implements PmsCollectService {

    /**
     * 添加收藏
     * @param productId
     * @param customId
     * @return
     */
    @Override
    @Transactional
    public Boolean add(Integer productId, Integer customId) {
        QueryWrapper<PmsCollect> wrapper = new QueryWrapper<>();
        wrapper.eq("custom_id", customId);
        wrapper.eq("product_id", productId);
        PmsCollect pmsCollect = this.getOne(wrapper);
        if (pmsCollect == null) {
            PmsCollect pmsCollect1 = new PmsCollect(customId, productId);
            return this.save(pmsCollect1);
        } else {
            throw new MyException("该商品已经添加收藏，请到我的收藏查看");
        }
    }

    /**
     * 删除收藏
     * @param productId
     * @param customId
     * @return
     */
    @Override
    @Transactional
    public Boolean del(Integer productId, Integer customId) {
        QueryWrapper<PmsCollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId);
        queryWrapper.eq("custom_id", customId);
        return this.remove(queryWrapper);
    }
}
