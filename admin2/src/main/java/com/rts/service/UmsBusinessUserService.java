package com.rts.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rts.entity.UmsBusinessUser;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * <p>
 * 商家用户表 服务类
 * </p>
 *
 * @author rts
 * @since 2023-03-24
 */
public interface UmsBusinessUserService extends IService<UmsBusinessUser> {

    Boolean add(String name, String phone, String email, MultipartFile icon, String password) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InternalException;

    Map<String, Object> login(String username, String password);

    String sendCode(String username);

    Boolean recoverPassword(String username, String code);

    UmsBusinessUser getAll(String userId);

    IPage<UmsBusinessUser> list(int pageNo, int pageSize, String value);

    Boolean update(Integer userId, String name, String phone, String email, MultipartFile icon) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InternalException;

    String SendCode(String email, Integer userId);

    Boolean Verify(String email, String code);

    String changePassword(String newPassword, String password, Integer userId);
}
