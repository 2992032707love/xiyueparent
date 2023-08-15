package com.rts.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rts.entity.PmsProduct;
import com.rts.entity.PmsSkus;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author rts
 * @since 2022-09-29
 */
public interface PmsProductService extends IService<PmsProduct> {

    IPage<PmsProduct> list(Integer pageNo, Integer pageSize, String value);

    Boolean add(Integer brandId, Integer[] categoryIds, String description, MultipartFile[] images, String name, MultipartFile pic, BigDecimal price, String[] skuAttr, String[] skuStock, Boolean skuStocknumber, String[] spuAttr) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InternalException;

    Boolean changeStatus (Integer id, String field, Boolean val) throws NoSuchFieldException, IllegalAccessException;

    List<PmsProduct> pageGetByName(Integer pageNo, Integer pageSize, String value);

    IPage<PmsProduct> listByBusinessId(Integer pageNo, Integer pageSize, String value, String loginId);

    Boolean addition(Integer brandId, Integer[] categoryIds, String description, MultipartFile[] images, String name, MultipartFile pic, BigDecimal price, String[] skuAttr, String[] skuStock, Boolean skuStocknumber, String[] spuAttr, Integer businessId) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InternalException;

    List<PmsProduct> getProducts(String categoryName);

    IPage<PmsProduct> listGetProducts(String categoryName);


    IPage<PmsProduct> getProductsByCategoryIds(Integer[] categoryIds, Integer pageNo, Integer pageSize);

    IPage<PmsProduct> listCustom(Integer pageNo, Integer pageSize, String value);

    IPage<PmsProduct> listUsedCustom(Integer pageNo, Integer pageSize, String value);

    IPage<PmsProduct> getUsedProductsByCategoryIds(Integer[] categoryIds, Integer pageNo, Integer pageSize);
}
