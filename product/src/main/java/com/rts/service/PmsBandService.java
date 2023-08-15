package com.rts.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rts.entity.PmsBand;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * <p>
 * 品牌表 服务类
 * </p>
 *
 * @author rts
 * @since 2022-09-27
 */
public interface PmsBandService extends IService<PmsBand> {
    List<PmsBand> getAll();
    IPage<PmsBand> list(int pageNo, int pageSize, String value);
    Boolean add(String name, MultipartFile flie, String description) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException;
    Boolean update(Integer id, String name, MultipartFile file, String description) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InternalException;
    Boolean delete(Integer id, Boolean active);
    String getNameById(Integer id);
}
