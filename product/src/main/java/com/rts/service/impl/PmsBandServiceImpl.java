package com.rts.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rts.entity.PmsBand;
import com.rts.mapper.PmsBandMapper;
import com.rts.service.FileService;
import com.rts.service.PmsBandService;
import io.minio.errors.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * <p>
 * 品牌表 服务实现类
 * </p>
 *
 * @author rts
 * @since 2022-09-27
 */
@Service
public class PmsBandServiceImpl extends ServiceImpl<PmsBandMapper, PmsBand> implements PmsBandService {
    @Resource
    FileService fileService;
    String bucket = "logo";

    @Override
    @CacheEvict(value = "pms", key = "'brands'")
    public List<PmsBand> getAll() {
        QueryWrapper<PmsBand> wrapper = new QueryWrapper<>();
        wrapper.eq("active", 1);
        return this.list(wrapper);
    }

    @Override
    public IPage<PmsBand> list(int pageNo, int pageSize, String value) {
        QueryWrapper<PmsBand> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(value)) {
            wrapper.like("name", value);
        }
        return this.page(new Page<>(pageNo, pageSize), wrapper);
    }

    @Override
    @CacheEvict(value = "pms", key = "'brands'")
    public Boolean add(String name, MultipartFile flie, String description) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        PmsBand pmsBand = new PmsBand(
                name,
                fileService.upload(bucket, flie),
                description
        );
        return this.save(pmsBand);
    }

    @Override
    @CacheEvict(value = "pms", key = "'brands'")
    public Boolean update(Integer id, String name, MultipartFile file, String description) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InternalException {
        PmsBand pmsBand = new PmsBand(
                id,
                name,
                null,
                description
        );
        if (file !=null && file.getSize() > 0) {
            pmsBand.setLogo(fileService.upload(bucket, file));
        }
        return this.updateById(pmsBand);
    }

    @Override
    @CacheEvict(value = "pms", key = "'brands'")
    public Boolean delete(Integer id, Boolean active) {
        PmsBand pmsBand = new PmsBand(id, active);
        return this.updateById(pmsBand);
    }

    @Override
    public String getNameById(Integer id) {
        return this.getById(id).getName();
    }

}
