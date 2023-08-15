package com.rts.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rts.entity.UmsUserInformation;
import com.baomidou.mybatisplus.extension.service.IService;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 买家用户表 服务类
 * </p>
 *
 * @author rts
 * @since 2023-03-08
 */
public interface UmsUserInformationService extends IService<UmsUserInformation> {

    UmsUserInformation getAll(String userId);

    IPage<UmsUserInformation> list(int pageNo, int pageSize, String value);

    Boolean add(String name, String phone, String email, MultipartFile icon, String password, String address, String[] addressId, String addressValue) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InternalException;

    Map<String, Object> login(String username, String password);

    String sendCode(String username);

    Boolean recoverPassword(String username, String code);

    Boolean update(Integer userId, String name, String phone, String email, MultipartFile icon, String address, String[] addressId, String addressValue) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InternalException;

    Boolean Verify(String email, String code);

    String changePassword(String newPassword, String password, Integer userId);

    String SendCode(String email, Integer userId);
}
