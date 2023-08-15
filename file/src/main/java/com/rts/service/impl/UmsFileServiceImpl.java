package com.rts.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rts.entity.UmsFile;
import com.rts.mapper.UmsFileMapper;
import com.rts.service.UmsFileService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文件表 服务实现类
 * </p>
 *
 * @author rts
 * @since 2022-09-27
 */
@Service
public class UmsFileServiceImpl extends ServiceImpl<UmsFileMapper, UmsFile> implements UmsFileService {

    @Override
    public UmsFile get(String md5, long size, String extension) {
        // 条件查询用QueryWrapper对象
        QueryWrapper<UmsFile> wrapper = new QueryWrapper<>();
        // eq 精确匹配 like 模糊匹配 lt 小于(数字) gt 大于 ne 不等于 le 小于等于 ge 大于等于
        // 默认用and连接
        wrapper.eq("md5", md5)
                .eq("size", size)
                .eq("extension", extension);
            // mybatis-plus 生成的getOne 根据条件查某一个
        return this.getOne(wrapper);
    }
}
