package com.rts.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rts.entity.PmsBand;
import com.rts.entity.PmsProduct;
import com.rts.entity.PmsSkus;
import com.rts.mapper.PmsProductMapper;
import com.rts.service.*;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author rts
 * @since 2022-09-29
 */
@Slf4j
@Service
public class PmsProductServiceImpl extends ServiceImpl<PmsProductMapper, PmsProduct> implements PmsProductService {
    @Resource
    PmsBandService pmsBandService;
    @Resource
    PmsCategoryService pmsCategoryService;
    @Resource
    FileService fileService;
    @Resource
    PmsSkusService pmsSkusService;
    @Resource
    PmsProductMapper pmsProductMapper;

    /**
     * 查询所有商品（管理员）
     * @param pageNo
     * @param pageSize
     * @param value
     * @return
     */
    @Override
    public IPage<PmsProduct> list(Integer pageNo, Integer pageSize, String value) {

        System.out.println("这里是PmsProductServiceImpl的" + pageNo);
        System.out.println("这里是PmsProductServiceImpl的" + pageSize);
        System.out.println("这里是PmsProductServiceImpl的" + value);
        QueryWrapper wrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(value)) {
            QueryWrapper wrapper1 = new QueryWrapper();
            wrapper.like("name", value);
            System.out.println("这里是PmsProductServiceImpl的wrapper: " +  wrapper);
            wrapper1.eq("is_publish", 1);
        }
        wrapper.orderByDesc("id");
        return this.page(new Page<>(pageNo, pageSize), wrapper);
    }

    /**
     * 添加商品（管理员）
     * @param brandId
     * @param categoryIds
     * @param description
     * @param images
     * @param name
     * @param pic
     * @param price
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
    @Override
    @Transactional  // 事务管理 要有一步报错 都退回到执行之前
    public Boolean add(Integer brandId, Integer[] categoryIds, String description, MultipartFile[] images, String name, MultipartFile pic, BigDecimal price, String[] skuAttr, String[] skuStock, Boolean skuStocknumber, String[] spuAttr) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InternalException {
        PmsProduct pmsProduct = new PmsProduct();
        pmsProduct.setBrandId(brandId);
        pmsProduct.setBrandName(pmsBandService.getNameById(brandId));
        pmsProduct.setCategoryId(Arrays.toString(categoryIds));
        pmsProduct.setCategoryName(pmsCategoryService.getNameByIds(categoryIds));
        pmsProduct.setDescription(description);
        pmsProduct.setPic(fileService.upload("logo", pic));
        pmsProduct.setName(name);
        List<String> srcList = new ArrayList<>();
        for (MultipartFile file : images) {
            System.out.println("这里是 file   " + file);
            srcList.add(fileService.upload("logo", file));
        }
        pmsProduct.setImages(String.join(",", srcList));
        pmsProduct.setPrice(price);
        pmsProduct.setSpuAttr(Arrays.toString(spuAttr));
        pmsProduct.setSkuAttr(Arrays.toString(skuAttr));
        System.out.println(pmsProduct);
        this.save(pmsProduct);
        System.out.println( "这是skuStock   " + skuStock);
//        for (String str : skuAttr) {
//            System.out.println(str);
//        }
        List<PmsSkus> list = new ArrayList<>();
        if (skuStocknumber) {
            System.out.println(skuStock.length);
            PmsSkus pmsSkus = JSONObject.parseObject(skuStock[0], PmsSkus.class);
            System.out.println("这里是pmsSkus: ==>"+pmsSkus);
            pmsSkus.setProductId(pmsProduct.getId());
            list.add(pmsSkus);
            System.out.println(list);
        } else {
            for (String str : skuStock) {
                // 反序列化
//                System.out.println(skuStock.length);
                System.out.println("这里是str    " + str);
                PmsSkus pmsSkus = JSONObject.parseObject(str, PmsSkus.class);;
                System.out.println("这里是pmsSkus    " + pmsSkus);
                pmsSkus.setProductId(pmsProduct.getId());
                list.add(pmsSkus);
            }
            System.out.println(list);
        }
        return pmsSkusService.saveBatch(list);
    }

    /**
     * 修改状态
     * @param id
     * @param field
     * @param val
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Override
    @Transactional
    public Boolean changeStatus(Integer id, String field, Boolean val) throws NoSuchFieldException, IllegalAccessException {
        PmsProduct pmsProduct = new PmsProduct();
        pmsProduct.setId(id);
        Class clz = PmsProduct.class;
//        clz.getField();只能获取到public修饰的属性
        // 用啥修饰的都可以获取到
        Field f = clz.getDeclaredField(field);
        f.setAccessible(true);
        // 给某个对象的某个属性赋值
        f.set(pmsProduct, val);
        return this.updateById(pmsProduct);
    }

    /**
     * 根据商品名搜索对应商品
     * @param pageNo
     * @param pageSize
     * @param value
     * @return
     */
    @Override
    public List<PmsProduct> pageGetByName(Integer pageNo, Integer pageSize, String value) {
        log.info("value为:" + value);
        System.out.println(value == null);
        return pmsProductMapper.pageGetByName(pageNo,pageSize, value);

    }

    /**
     * 根据商家用户id分页查询该用户所有的商品
     * @param pageNo
     * @param pageSize
     * @param value
     * @param loginId
     * @return
     */
    @Override
    public IPage<PmsProduct> listByBusinessId(Integer pageNo, Integer pageSize, String value, String loginId) {

        System.out.println("这里是listByBusinessId的：" + pageNo);
        System.out.println("这里是listByBusinessId的：" + pageSize);
        System.out.println("这里是listByBusinessId的：" + value);
        System.out.println("这里是listByBusinessId的：" + loginId);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("business_id", loginId);
        if (StringUtils.isNotBlank(value)) {
            wrapper.like("name", value);
        }
        wrapper.orderByDesc("active");
//        wrapper.orderByDesc("is_new");
//        wrapper.orderByDesc("is_used");
//        wrapper.orderByDesc("is_discounted");
        wrapper.orderByDesc("id");
        return this.page(new Page<>(pageNo, pageSize), wrapper);
    }

    /**
     * 商家用户添加商品
     * @param brandId
     * @param categoryIds
     * @param description
     * @param images
     * @param name
     * @param pic
     * @param price
     * @param skuAttr
     * @param skuStock
     * @param spuAttr
     * @param businessId
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
    @Override
    @Transactional
    public Boolean addition(Integer brandId, Integer[] categoryIds, String description, MultipartFile[] images, String name, MultipartFile pic, BigDecimal price, String[] skuAttr, String[] skuStock, Boolean skuStocknumber, String[] spuAttr, Integer businessId) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InternalException {
        PmsProduct pmsProduct = new PmsProduct();
        pmsProduct.setBrandId(brandId);
        pmsProduct.setBrandName(pmsBandService.getNameById(brandId));
        pmsProduct.setCategoryId(Arrays.toString(categoryIds));
        pmsProduct.setCategoryName(pmsCategoryService.getNameByIds(categoryIds));
        pmsProduct.setDescription(description);
        pmsProduct.setPic(fileService.upload("logo", pic));
        pmsProduct.setName(name);
        pmsProduct.setBusinessId(businessId);
        List<String> srcList = new ArrayList<>();
        for (MultipartFile file : images) {
            System.out.println("这里是 file   " + file);
            srcList.add(fileService.upload("logo", file));
        }
        pmsProduct.setImages(String.join(",", srcList));
        pmsProduct.setPrice(price);
        pmsProduct.setSpuAttr(Arrays.toString(spuAttr));
        pmsProduct.setSkuAttr(Arrays.toString(skuAttr));
        System.out.println(pmsProduct);
        this.save(pmsProduct);
        System.out.println( "这是skuStock   " + skuStock);
//        for (String str : skuAttr) {
//            System.out.println(str);
//        }
        List<PmsSkus> list = new ArrayList<>();
        if (skuStocknumber) {
            System.out.println(skuStock.length);
            PmsSkus pmsSkus = JSONObject.parseObject(skuStock[0], PmsSkus.class);
            System.out.println("这里是pmsSkus: ==>"+pmsSkus);
            pmsSkus.setProductId(pmsProduct.getId());
            list.add(pmsSkus);
            System.out.println(list);
        } else {
            for (String str : skuStock) {
                // 反序列化
//                System.out.println(skuStock.length);
            System.out.println("这里是str    " + str);
                PmsSkus pmsSkus = JSONObject.parseObject(str, PmsSkus.class);;
            System.out.println("这里是pmsSkus    " + pmsSkus);
                pmsSkus.setProductId(pmsProduct.getId());
                list.add(pmsSkus);
            }
            System.out.println(list);
        }
        return pmsSkusService.saveBatch(list);
    }

    /**
     * 通过分类名称查询所有有效且上架的商品
     * @param categoryName
     * @return
     */
    @Override
    public List<PmsProduct> getProducts(String categoryName) {
        QueryWrapper<PmsProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("category_name", categoryName);
        wrapper.eq("active", 1);
        wrapper.eq("is_publish", 1);
        return baseMapper.selectList(wrapper);
    }

    /**
     * 通过分类名称分页查询7个有效且上架的商品
     * @param categoryName
     * @return
     */
    @Override
    public IPage<PmsProduct> listGetProducts(String categoryName) {
        QueryWrapper<PmsProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("category_name", categoryName);
        wrapper.eq("active", 1);
        wrapper.eq("is_publish", 1);
        wrapper.orderByDesc("id");

        return this.page(new Page<>(1, 7), wrapper);
    }

    /**
     * 通过分类id分页查询有效企且上架的商品
     * @param categoryIds
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public IPage<PmsProduct> getProductsByCategoryIds(Integer[] categoryIds, Integer pageNo, Integer pageSize) {
        QueryWrapper<PmsProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", Arrays.toString(categoryIds));
        queryWrapper.eq("active", 1);
        queryWrapper.eq("is_publish", 1);
        queryWrapper.orderByDesc("id");
        return this.page(new Page<>(pageNo, pageSize), queryWrapper);
    }

    /**
     * （买家用户浏览商品）分页查询所有有效商品
     * @param pageNo
     * @param pageSize
     * @param value
     * @return
     */
    @Override
    public IPage<PmsProduct> listCustom(Integer pageNo, Integer pageSize, String value) {
        QueryWrapper<PmsProduct> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(value)) {
            QueryWrapper wrapper1 = new QueryWrapper();
            queryWrapper.like("name", value);
        }
        queryWrapper.eq("active", 1);
        queryWrapper.eq("is_publish", 1);
        queryWrapper.orderByDesc("is_discounted");
        queryWrapper.orderByDesc("is_new");
        queryWrapper.orderByDesc("id");
        return this.page(new Page<>(pageNo, pageSize), queryWrapper);
    }

    /**
     * （买家用户浏览商品）分页查询所有有效二手商品
     * @param pageNo
     * @param pageSize
     * @param value
     * @return
     */
    @Override
    public IPage<PmsProduct> listUsedCustom(Integer pageNo, Integer pageSize, String value) {
        QueryWrapper<PmsProduct> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(value)) {
            queryWrapper.like("name", value);
        }
        queryWrapper.eq("active", 1);
        queryWrapper.eq("is_publish", 1);
        queryWrapper.eq("is_used", 1);
        queryWrapper.orderByDesc("is_discounted");
        queryWrapper.orderByDesc("is_new");
        queryWrapper.orderByDesc("id");
        return this.page(new Page<>(pageNo, pageSize), queryWrapper);
    }

    /**
     * 通过分类id分页查询有效企且上架的二手商品
     * @param categoryIds
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public IPage<PmsProduct> getUsedProductsByCategoryIds(Integer[] categoryIds, Integer pageNo, Integer pageSize) {
        QueryWrapper<PmsProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", Arrays.toString(categoryIds));
        queryWrapper.eq("active", 1);
        queryWrapper.eq("is_publish", 1);
        queryWrapper.eq("is_used", 1);
        queryWrapper.orderByDesc("id");
        return this.page(new Page<>(pageNo, pageSize), queryWrapper);
    }
}
