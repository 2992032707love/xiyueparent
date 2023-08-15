package com.rts.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rts.entity.UmsUser;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author rts
 * @since 2022-09-17
 */
public interface UmsUserService extends IService<UmsUser> {
    List<UmsUser> getActive();

    IPage<UmsUser> list(int pageNo, int pageSize, String value);

    Boolean add(String name, String phone, String email, MultipartFile flie, String password) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException;

    Boolean update(Integer id, String name, String phone, String email, MultipartFile flie) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InternalException;

    Map<String, Object> login(String username, String password) throws Exception;

    String sendCode(String username);

    Boolean recoverPassword(String username, String code);

    UmsUser getAll(String loginId);

    Boolean updateByLoginId(Integer id, String name, String phone, String email, MultipartFile icon) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InternalException;

    String SendCode(String email, Integer id);

    Boolean Verify(String email, String code);

    String changePassword(String newPassword, String password, Integer id);
}
