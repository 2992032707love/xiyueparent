package com.rts.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rts.entity.PmsOrderInfo;
import com.rts.entity.PmsProduct;
import com.rts.entity.PmsSkus;
import com.rts.entity.UmsUserInformation;
import com.rts.enums.OrderStatus;
import com.rts.mapper.PmsOrderInfoMapper;
import com.rts.mapper.PmsProductMapper;
import com.rts.mapper.PmsSkusMapper;
import com.rts.mapper.UmsUserInformationMapper;
import com.rts.service.PmsOrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rts.util.OrderNoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * <p>
 * 订单信息表 服务实现类
 * </p>
 *
 * @author rts
 * @since 2023-03-09
 */
@Service
@Slf4j
public class PmsOrderInfoServiceImpl extends ServiceImpl<PmsOrderInfoMapper, PmsOrderInfo> implements PmsOrderInfoService {

    @Resource
    private PmsProductMapper pmsProductMapper;

    @Resource
    private PmsSkusMapper pmsSkusMapper;

    @Resource
    private UmsUserInformationMapper umsUserInformationMapper;

    /**
     * 创建订单
     * @param productId
     * @param skusId
     * @param prc
     * @param productSize
     * @param loginId
     * @param paymentType
     * @return
     */
    @Override
    public PmsOrderInfo createOrderByProductId(Integer productId, Integer skusId, BigDecimal prc, Integer productSize, String loginId, String paymentType) {
        log.info("进入创建订单");
        // 查找已存在但未支付的订单
        PmsOrderInfo pmsOrderInfo = this.getNoPayOrderByProductIdLoginId(productId, loginId, paymentType);
        if ( pmsOrderInfo != null) {
            return pmsOrderInfo;
        }
        Integer customId = Integer.valueOf(loginId);
        // 获取收货人信息
        UmsUserInformation umsUserInformation = umsUserInformationMapper.selectById(customId);
        // 获取商品信息
        PmsProduct pmsProduct = pmsProductMapper.selectById(productId);
        // 获取商品sku属性
        PmsSkus pmsSkus = pmsSkusMapper.selectById(skusId);
        // 生成订单
        pmsOrderInfo = new PmsOrderInfo();
        pmsOrderInfo.setTitle(pmsProduct.getName());
        pmsOrderInfo.setOrderNo(OrderNoUtils.getPmsOrderNo()); // 订单号
        pmsOrderInfo.setProductId(productId);
        pmsOrderInfo.setUserId(Integer.parseInt(loginId)); // 设置用户id
        pmsOrderInfo.setTablename(pmsProduct.getTableName());
        pmsOrderInfo.setProductSize(productSize);
        pmsOrderInfo.setTotalYuan(prc.multiply(new BigDecimal(productSize.toString()))); // 元
        pmsOrderInfo.setOrderStatus(OrderStatus.NOTPAY.getType()); // 默认订单状态为未支付
        pmsOrderInfo.setPaymentType(paymentType);
        pmsOrderInfo.setSkusItem(pmsSkus.getItem());// 设置商品sku属性
        pmsOrderInfo.setSkusId(skusId); // 设置商品skuid
        pmsOrderInfo.setBusinessId(pmsProduct.getBusinessId()); // 设置商家id
        pmsOrderInfo.setAddress(umsUserInformation.getAddress()); // 设置收货地址
        pmsOrderInfo.setName(umsUserInformation.getName()); // 设置收货人姓名
        pmsOrderInfo.setPhone(umsUserInformation.getPhone()); // 设置收货人手机号
        baseMapper.insert(pmsOrderInfo);
        System.out.println("订单信息(pmsOrderInfo)为:" + pmsOrderInfo);
        return pmsOrderInfo;
    }

    /**
     * 查询订单列表，并倒序查询
     * @return
     */
    @Override
    public List<PmsOrderInfo> listOrderByCreateTimeDesc() {
        QueryWrapper<PmsOrderInfo> queryWrapper = new QueryWrapper<PmsOrderInfo>().orderByDesc("creat_time");
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 根据订单号获取订单
     * @param orderNo
     * @return
     */
    @Override
    public PmsOrderInfo getOrderByOrderNo(String orderNo) {

        QueryWrapper<PmsOrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        PmsOrderInfo pmsOrderInfo = baseMapper.selectOne(queryWrapper);
        System.out.println("根据订单号获取的订单为：" + pmsOrderInfo);
        return pmsOrderInfo;
    }

    /**
     * 根据订单号获取订单状态
     * @param orderNo
     * @return
     */
    @Override
    public String getOrderStatus(String orderNo) {

        QueryWrapper<PmsOrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        PmsOrderInfo pmsOrderInfo = baseMapper.selectOne(queryWrapper);
        if (pmsOrderInfo == null) {
            return null;
        }
        return pmsOrderInfo.getOrderStatus();
    }

    /**
     * 根据订单号更新订单状态
     * @param orderNo
     * @param orderStatus
     */
    @Override
    public void updateStatusByOrderNo(String orderNo, OrderStatus orderStatus) {

        log.info("更新订单状态 ===> {}", orderStatus.getType());

        QueryWrapper<PmsOrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);

        PmsOrderInfo pmsOrderInfo = new PmsOrderInfo();
        pmsOrderInfo.setOrderStatus(orderStatus.getType());

        baseMapper.update(pmsOrderInfo, queryWrapper);
    }

    /**
     * 分页查询订单列表
     * @param pageNo
     * @param pageSize
     * @param value
     * @return
     */
    @Override
    public IPage<PmsOrderInfo> list(int pageNo, int pageSize, String value, String loginId) {
        QueryWrapper wrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(value)) {
            // 写数据库表的字段
            wrapper.like("title", value);
        }
        wrapper.eq("user_id", loginId);
        wrapper.orderByDesc("id");
        return this.page(new Page<>(pageNo, pageSize), wrapper);
    }

    /**
     * 查询创建超过minutes分钟并且未支付的订单
     * @param minutes
     * @param paymentType
     * @return
     */
    @Override
    public List<PmsOrderInfo> getNoPayOrderByDuration(int minutes, String paymentType) {

        // 用时间日期对象创建了一个时间对象
        Instant instant = Instant.now().minus(Duration.ofMinutes(minutes));

        QueryWrapper<PmsOrderInfo> queryWrapper = new QueryWrapper<>();
        // 并且是未支付的状态
        queryWrapper.eq("order_status", OrderStatus.NOTPAY.getType());
        System.out.println("创建的时间日期对象为： " + instant);
        // 查询时间超过一分钟 le小于等于
        queryWrapper.le("creat_time", instant);
        // 且支付类型为支付宝的
        queryWrapper.eq("payment_type", paymentType);

        List<PmsOrderInfo> pmsOrderInfos = baseMapper.selectList(queryWrapper);

        // 返回数据列表
        return pmsOrderInfos;
    }

    /**
     * 根据订单号和用户id更新订单状态
     * @param orderNo
     * @param userId
     * @param orderStatus
     */
    @Override
    public void updateStatusByOrderNoUserId(String orderNo, Integer userId, OrderStatus orderStatus) {

        log.info("进入根据订单号和用户id更新订单状态接口");
        log.info("更新订单状态 ===> {}", orderStatus.getType());

        QueryWrapper<PmsOrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        queryWrapper.eq("user_id", userId);

        PmsOrderInfo pmsOrderInfo = new PmsOrderInfo();
        pmsOrderInfo.setOrderStatus(orderStatus.getType());

        baseMapper.update(pmsOrderInfo, queryWrapper);
    }

    /**
     * 根据商品id和用户id查询未支付订单
     * 防止重复创建订单对象
     * @param productId
     * @param loginId
     * @param paymentType
     * @return
     */
    private PmsOrderInfo getNoPayOrderByProductIdLoginId(Integer productId, String loginId, String paymentType) {
        QueryWrapper<PmsOrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", loginId);
        queryWrapper.eq("product_id", productId);
        queryWrapper.eq("order_status", OrderStatus.NOTPAY.getType());
        queryWrapper.eq("payment_type", paymentType);
        PmsOrderInfo pmsOrderInfo = baseMapper.selectOne(queryWrapper);
        return pmsOrderInfo;
    }
}
