package com.rts.service;

import com.rts.entity.PmsCollect;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 收藏表 服务类
 * </p>
 *
 * @author rts
 * @since 2023-04-06
 */
public interface PmsCollectService extends IService<PmsCollect> {

    Boolean add(Integer productId, Integer customId);

    Boolean del(Integer productId, Integer customId);
}
