package com.rts.controller;


import com.rts.common.ResultJson;
import com.rts.entity.PmsBand;
import com.rts.service.PmsBandService;
import io.minio.errors.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 * 品牌表 前端控制器
 * </p>
 *
 * @author rts
 * @since 2022-09-27
 */
@RestController
@RequestMapping("/pmsBrand")
public class PmsBandController {
    @Resource
    PmsBandService pmsBandService;
    @GetMapping("/list")
    ResultJson list(Integer pageNo, Integer pageSize, String value) {
        return ResultJson.success(pmsBandService.list(pageNo, pageSize, value));
    }
    @PostMapping("/add")
    ResultJson add(String name, MultipartFile file, String description) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InsufficientDataException, InternalException {
        return ResultJson.success(pmsBandService.add(name, file, description), "添加品牌成功");
    }
    @GetMapping("/getone")
    ResultJson getOne(Integer id) {
        return ResultJson.success(pmsBandService.getById(id));
    }
    @PostMapping("/update")
    ResultJson update(Integer id, String name, MultipartFile file, String description) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InsufficientDataException, ErrorResponseException {
        return ResultJson.success(pmsBandService.update(id,  name, file, description), "修改品牌成功");
    }
    @PostMapping("/delete")
    ResultJson delete(Integer id, Boolean active) {
        PmsBand pmsBand = new PmsBand();
        pmsBand.setId(id);
        pmsBand.setActive(active);
        return ResultJson.success(pmsBandService.delete(id, active), active ? "恢复品牌成功" : "删除品牌成功");
    }
}
