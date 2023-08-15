package com.rts.service.impl;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.rts.entity.UmsFile;
import com.rts.service.FileService;
import com.rts.service.UmsFileService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class FileServiceImpl implements FileService {
    @Value("${upload.url}")
    String url;
    @Value("${upload.username}")
    String username;
    @Value("${upload.password}")
    String password;
    @Resource
    UmsFileService umsFileService;
    @Override
    public String upload(String bucket, MultipartFile file) throws IOException, ServerException, InsufficientDataException, InternalException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, XmlParserException, ErrorResponseException {
       // 获取文件的md5 大小 后缀名
        String md5 = DigestUtils.md5DigestAsHex(file.getInputStream());
        long size = file.getSize();
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        // 通过md5 大小 后缀名查询数据库中是否存在一条记录
        UmsFile umsFile = umsFileService.get(md5, size, extension);
        if (umsFile != null) {
            return umsFile.getPath();
        }
        // 如果没查出来 需要上传
        StringBuilder builder = new StringBuilder();
        // 获取文件名
        builder.append(NanoIdUtils.randomNanoId())
                .append(".")
                // 获取扩展名
                .append(extension);
        // 定义返回的路径
        String path =  bucket + "/" + builder.toString();
        // 根据地址 用户名 密码 minio的客户端
        MinioClient client = MinioClient.builder()
                //地址 上传和下载的是9000 客户端的是9001
                .endpoint(url)
                //用户名和密码
                .credentials(username,password)
                .build();//这样就创建了一个客户端
        // 设置上传参数 桶子名 类型 文件名 字节流等等
        PutObjectArgs args = PutObjectArgs.builder()
                // 哪个桶子
                .bucket(bucket)
                // 文件的类型
                .contentType(file.getContentType())
                // 自己写一个文件名
                .object(builder.toString())
                // 传字节流
                .stream(file.getInputStream(), size, 0)
                .build();
        // 向数据库中保存已上传文件的信息
        umsFile = new UmsFile(md5, size, extension, path);
        umsFileService.save(umsFile);
        // 上传文件
        client.putObject(args);
        return path;
    }
}
