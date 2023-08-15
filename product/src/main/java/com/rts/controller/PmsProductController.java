package com.rts.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rts.common.ResultJson;
import com.rts.entity.PmsProduct;
import com.rts.entity.PmsSkus;
import com.rts.mapper.PmsProductMapper;
import com.rts.service.*;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.nutz.lang.random.R;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author rts
 * @since 2022-09-29
 */
@Slf4j
@RestController
@RequestMapping("/pmsProduct")
public class PmsProductController {
    @Resource
    PmsProductMapper pmsProductMapper;
    @Resource
    PmsBandService pmsBandService;
    @Resource
    PmsCategoryService pmsCategoryService;
    @Resource
    PmsAttrService pmsAttrService;
    @Resource
    PmsProductService pmsProductService;
    @Resource
    PmsSkusService pmsSkusService;

    /**
     * 查询所有商品（管理员）
     * @param pageNo
     * @param pageSize
     * @param value
     * @return
     */
    @GetMapping("/list")
    ResultJson list (Integer pageNo, Integer pageSize, String value) {
        System.out.println("这里是controller的" + pageNo);
        System.out.println("这里是controller的" + pageSize);
        System.out.println("这里是controller的" + value);
        return ResultJson.success(pmsProductService.list(pageNo, pageSize, value));
//        log.info("这里是Controller里的value:" +value);
//        pageNo = (pageNo - 1) * pageSize;
//        System.out.println("pageNo" + pageNo);
//        return ResultJson.success(pmsProductService.pageGetByName(pageNo, pageSize, value));
    }

    /**
     *
     * @return
     */
    @GetMapping("/getInit")
    ResultJson getInit () {
        Map<String, List> map = new HashMap<>();
        map.put("brands", pmsBandService.getAll());
        map.put("categories", pmsCategoryService.getActive());
//        System.out.println(map);
        return ResultJson.success(map);
    }

    /**
     *
     * @param categoryIds
     * @return
     */
    @GetMapping("/getAttr")
    ResultJson getAttr(Long[] categoryIds) {
        for (Long categoryId : categoryIds) {
            System.out.println(categoryId);
        }
        return ResultJson.success(pmsAttrService.getActiveByCategoryIds(categoryIds));
    }

    /**
     * 添加商品(管理员)
     * @param brandId
     * @param categoryIds
     * @param description
     * @param images
     * @param name
     * @param price
     * @param pic
     * @param skuAttr
     * @param skuStock
     * @param spuAttr
     * @return
     * @throws IOException
     * @throws InvalidKeyException
     * @throws InvalidResponseException
     * @throws InsufficientDataException
     * @throws NoSuchAlgorithmException
     * @throws ServerException
     * @throws ErrorResponseException
     * @throws XmlParserException
     * @throws InternalException
     */
    @PostMapping("/add")
    @Transactional
    ResultJson add(Integer brandId, Integer[] categoryIds, String description, MultipartFile[] images, String name, MultipartFile pic, BigDecimal price, String[] skuAttr, String[] skuStock, Boolean skuStocknumber, String[] spuAttr) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InternalException {
        System.out.println(brandId);
        for (Integer categoryId : categoryIds) {
            System.out.println(categoryId);
        }
        System.out.println(description);
        System.out.println(images.length);
        System.out.println(name);
        System.out.println(pic.getOriginalFilename());
        System.out.println(price);
        for (String str : skuAttr) {
            System.out.println(str);
        }

        for (String str : skuStock) {
            System.out.println(str);
        }
        for (String str : spuAttr) {
            System.out.println(str);
        }

//        PmsProduct pmsProduct = new PmsProduct();
//        pmsProduct.setBrandId(brandId);
//        pmsProduct.setBrandName(pmsBandService.getNameById(brandId));
//        pmsProduct.setCategoryId(Arrays.toString(categoryIds));
//        pmsProduct.setCategoryName(pmsCategoryService.getNameByIds(categoryIds));
//        pmsProduct.setDescription(description);
//        pmsProduct.setPic(fileService.upload("logo", pic));
//        pmsProduct.setName(name);
//        pmsProductService.save(pmsProduct);
//        List<String> srcList = new ArrayList<>();
//        for (MultipartFile file : images) {
//            srcList.add(fileService.upload("logo", file));
//        }
//        pmsProduct.setImages(String.join(",", srcList));
//        pmsProduct.setPrice(price);
//        pmsProduct.setSpuAttr(Arrays.toString(spuAttr));
//        pmsProduct.setSkuAttr(Arrays.toString(skuAttr));
//        PmsSkus pmsSkus = JSONObject.parseObject(str, PmsSkus.class);
//        pmsSkus.setProductId(pmsProduct.getId());
//        skus.add(pmsSkus);
//        System.out.println(skus);
        return ResultJson.success(pmsProductService.add(brandId, categoryIds, description, images, name, pic, price, skuAttr, skuStock, skuStocknumber, spuAttr), "添加商品成功");
    }

    /**
     * 根据主键查询该条内容和对应的skus表的属性
     * @param id
     * @return
     */
    @GetMapping("/getOne")
    ResultJson getOne(Integer id) {
        Map<String, Object> map = new HashMap<>();
        map.put("product", pmsProductService.getById(id));
        map.put("skus", pmsSkusService.list(id));
        return ResultJson.success(map);
    }

    /**
     * 更新上架 新品 热卖 优惠 二手状态
     * @param id
     * @param field
     * @param val
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @PostMapping("/changeStatus")
    @Transactional
    ResultJson changeStatus (Integer id, String field, Boolean val) throws NoSuchFieldException, IllegalAccessException {
        return ResultJson.success(pmsProductService.changeStatus(id, field, val), "更新状态成功");
    }

    /**
     * 逻辑删除商品
     * @param id
     * @param active
     * @return
     */
    @PostMapping("/delete")
    ResultJson delete(Integer id, Boolean active) {
        PmsProduct pmsProduct = new PmsProduct();
        pmsProduct.setId(id);
        pmsProduct.setActive(active);
        return ResultJson.success(pmsProductService.updateById(pmsProduct), "删除商品成功！");
    }

    /**
     * 根据商家用户id分页查询该用户所有的商品
     * @param pageNo
     * @param pageSize
     * @param value
     * @param loginId
     * @return
     */
    @GetMapping("/listByBusinessId")
    ResultJson listByBusinessId(Integer pageNo, Integer pageSize, String value, String loginId) {
        System.out.println("这里是listByBusinessId的：" + pageNo);
        System.out.println("这里是listByBusinessId的：" + pageSize);
        System.out.println("这里是listByBusinessId的：" + value);
        System.out.println("这里是listByBusinessId的：" + loginId);
        return ResultJson.success(pmsProductService.listByBusinessId(pageNo, pageSize, value, loginId));
    }

    /**
     * 添加商品（商家）
     * @param brandId
     * @param categoryIds
     * @param description
     * @param images
     * @param name
     * @param price
     * @param pic
     * @param skuAttr
     * @param skuStock
     * @param spuAttr
     * @param loginId
     * @return
     * @throws IOException
     * @throws InvalidResponseException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws ServerException
     * @throws InternalException
     * @throws XmlParserException
     * @throws InsufficientDataException
     * @throws ErrorResponseException
     */
    @PostMapping("/addition")
    @Transactional
    ResultJson addition(Integer brandId, Integer[] categoryIds, String description, MultipartFile[] images, String name, MultipartFile pic, BigDecimal price,  String[] skuAttr, String[] skuStock, Boolean skuStocknumber, String[] spuAttr, String loginId) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InsufficientDataException, ErrorResponseException {
        Integer businessId = Integer.valueOf(loginId);
        return ResultJson.success(pmsProductService.addition(brandId, categoryIds, description, images, name, pic, price, skuAttr, skuStock, skuStocknumber, spuAttr, businessId), "添加商品成功");
    }

    /**
     * 通过分类名称查询所有有效且上架的商品
     * @param categoryName
     * @return
     */
    @PostMapping("/getProducts")
    ResultJson getProducts(String categoryName) {
        return ResultJson.success(pmsProductService.getProducts(categoryName));
    }

    /**
     * 通过分类名称分页查询7个有效且上架的商品
     * @param categoryName
     * @return
     */
    @PostMapping("/listgetProducts")
    ResultJson listGetProducts(String categoryName) {
        return ResultJson.success(pmsProductService.listGetProducts(categoryName));
    }

    /**
     * 根据登录id分页查询该买家用户所收藏的商品的所有信息
     */
    @PostMapping("/getByCustomId")
    ResultJson getByCustomId(String loginId, Integer pageNo, Integer pageSize) {

        Integer customId = Integer.valueOf(loginId);
        return ResultJson.success(pmsProductMapper.getByCustomId(customId, new Page<>(pageNo, pageSize)));
    }

    /**
     * 通过分类id分页查询有效企且上架的商品
     * @param categoryIds
     * @param pageNo
     * @param pageSize
     * @return
     */
    @PostMapping("/getProductsByCategoryIds")
    ResultJson getProductsByCategoryIds(Integer[] categoryIds,Integer pageNo, Integer pageSize) {
        return ResultJson.success(pmsProductService.getProductsByCategoryIds(categoryIds, pageNo, pageSize));
    }

    /**
     * （买家用户浏览商品）分页查询所有有效商品
     * @param pageNo
     * @param pageSize
     * @param value
     * @return
     */
    @GetMapping("/listCustom")
    ResultJson listCustom(Integer pageNo, Integer pageSize, String value) {
        return ResultJson.success(pmsProductService.listCustom(pageNo, pageSize, value));
    }

    /**
     * （买家用户浏览商品）分页查询所有有效二手商品
     * @param pageNo
     * @param pageSize
     * @param value
     * @return
     */
    @GetMapping("/listUsedCustom")
    ResultJson listUsedCustom(Integer pageNo, Integer pageSize, String value) {
        return ResultJson.success(pmsProductService.listUsedCustom(pageNo, pageSize, value));
    }

    /**
     * 通过分类id分页查询有效企且上架的二手商品
     * @param categoryIds
     * @param pageNo
     * @param pageSize
     * @return
     */
    @PostMapping("/getUsedProductsByCategoryIds")
    ResultJson getUsedProductsByCategoryIds(Integer[] categoryIds,Integer pageNo, Integer pageSize) {
        return ResultJson.success(pmsProductService.getUsedProductsByCategoryIds(categoryIds, pageNo, pageSize));
    }
}