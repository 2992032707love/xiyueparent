package com.rts.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rts.config.MyException;
import com.rts.entity.PmsProductOrder;
import com.rts.enums.ProductOrderStatus;
import com.rts.mapper.PmsProductOrderMapper;
import com.rts.service.PmsProductOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.net.QCodec;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * <p>
 * 商品订单表 服务实现类
 * </p>
 *
 * @author rts
 * @since 2023-04-12
 */
@Slf4j
@Service
public class PmsProductOrderServiceImpl extends ServiceImpl<PmsProductOrderMapper, PmsProductOrder> implements PmsProductOrderService {

    /**
     * 分页查询商品订单表的信息
     * @param pageNo
     * @param pageSize
     * @param value
     * @param businessId
     * @return
     */
    @Override
    public IPage<PmsProductOrder> listByBusinessId(int pageNo, int pageSize, String value, Integer businessId) {
        QueryWrapper<PmsProductOrder> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(value)) {
            queryWrapper.like("title", value);
        }
        queryWrapper.eq("business_id", businessId);
        queryWrapper.orderByDesc("id");
        return this.page(new Page<>(pageNo, pageSize), queryWrapper);
    }

    /**
     * 更新订单状态
     * @param id
     * @param loginId
     * @param orderStatus
     * @return
     */
    @Override
    public String updateOrderStatus(Integer id, String loginId, String orderStatus) {
        Integer businessId = Integer.valueOf(loginId);
        QueryWrapper<PmsProductOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        PmsProductOrder pmsProductOrder = this.getOne(queryWrapper);
        System.out.println( "这里是orderStatus：" + orderStatus);
        if (Objects.equals(businessId, pmsProductOrder.getBusinessId())) {
            switch (orderStatus) {
                case "用户已支付成功，待发货" :
                    PmsProductOrder pmsProductOrder1 = new PmsProductOrder();
                    pmsProductOrder1.setOrderStatus(ProductOrderStatus.SUCCESS_WAIt_FOR_SHIPMENT.getType());
                    this.baseMapper.update(pmsProductOrder1, queryWrapper);
                    break;
                case "已发货，运输中":
                    PmsProductOrder pmsProductOrder2 = new PmsProductOrder();
                    pmsProductOrder2.setOrderStatus(ProductOrderStatus.SHIPPED_IN_TRANSIT.getType());
                    this.baseMapper.update(pmsProductOrder2, queryWrapper);
                    break;
                case "已送达，待收货":
                    PmsProductOrder pmsProductOrder3 = new PmsProductOrder();
                    pmsProductOrder3.setOrderStatus(ProductOrderStatus.DELIVERED_READY_TO_BE_RECEIVED.getType());
                    this.baseMapper.update(pmsProductOrder3, queryWrapper);
                    break;
//                case "确认收货":
//                    PmsProductOrder pmsProductOrder4 = new PmsProductOrder();
//                    pmsProductOrder4.setOrderStatus(ProductOrderStatus.CONFIRM_RECEIPT.getType());
//                    break;
                default :
                    throw new MyException("更新失败！");
            }
            return "更新成功！";
        } else {
            throw new MyException("无法更新非自己所属的订单的状态");
        }
    }

    /**
     * 买家用户支付成功之后对应生成一条商家端的订单信息
     * @param businessId
     * @param customId
     * @param orderNo
     * @param tablename
     * @param title
     * @param totalYuan
     * @param productSize
     * @param skusItem
     * @param skusId
     * @param name
     * @param phone
     * @param address
     * @param orderStatus
     * @param paymentType
     * @return
     */
    @Override
    @Transactional
    public Boolean add(Integer businessId, Integer customId, String orderNo, String tablename, String title, BigDecimal totalYuan, Integer productSize, String skusItem, Integer skusId, String name, String phone, String address, String orderStatus, String paymentType) {
        PmsProductOrder pmsProductOrder = new PmsProductOrder();
        pmsProductOrder.setBusinessId(businessId);
        pmsProductOrder.setCustomId(customId);
        pmsProductOrder.setOrderNo(orderNo);
        pmsProductOrder.setTablename(tablename);
        pmsProductOrder.setTitle(title);
        pmsProductOrder.setTotalYuan(totalYuan);
        pmsProductOrder.setProductSize(productSize);
        pmsProductOrder.setSkusItem(skusItem);
        pmsProductOrder.setSkusId(skusId);
        pmsProductOrder.setName(name);
        pmsProductOrder.setPhone(phone);
        pmsProductOrder.setAddress(address);
        pmsProductOrder.setOrderStatus(orderStatus);
        pmsProductOrder.setPaymentType(paymentType);
        System.out.println("商品订单信息：" + pmsProductOrder);
        return this.save(pmsProductOrder);
    }

    /**
     * 根据订单号查看商品订单的订单状态
     * @param orderNo
     * @param customId
     */
    @Override
    public PmsProductOrder listByOrderNo(String orderNo, Integer customId) {
        QueryWrapper<PmsProductOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        queryWrapper.eq("custom_id", customId);
        PmsProductOrder pmsProductOrder = this.getOne(queryWrapper);
        return pmsProductOrder;
    }

    /**
     * 买家用户查看商品订单状态
     * @param orderNo
     * @param customId
     * @return
     */
    @Override
    public PmsProductOrder getone(String orderNo, Integer customId) {
        QueryWrapper<PmsProductOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no",orderNo);
        queryWrapper.eq("custom_id", customId);
        PmsProductOrder pmsProductOrder = this.getOne(queryWrapper);
        System.out.println("这里是pmsProductOrder：" + pmsProductOrder);
        return pmsProductOrder;
    }

    /**
     * 买家用户确认收货
     * @param id
     * @param orderNo
     * @param orderStatus
     * @param customId
     * @return
     */
    @Override
    public Boolean updateOrderStatusByid(Integer id, String orderNo, String orderStatus, Integer customId) {
        QueryWrapper<PmsProductOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        queryWrapper.eq("custom_id", customId);
        PmsProductOrder pmsProductOrder = this.getOne(queryWrapper);
        if (pmsProductOrder == null) {
            throw new MyException("无法进行该操作，请重新登录！");
        } else {
            if (Objects.equals(pmsProductOrder.getId(), id)) {
                PmsProductOrder pmsProductOrder1 = new PmsProductOrder(id, orderStatus);

                return this.updateById(pmsProductOrder1);
            } else {
                throw new MyException("无法进行该操作，请重新登录！！");
            }
        }
    }

    /**
     * 更新商品订单表（退款）
     * @param orderNo
     * @param cancelRefundSuccess
     * @return
     */
    @Override
    @Transactional
    public Boolean updateRefund(String orderNo, ProductOrderStatus cancelRefundSuccess) {
        log.info("更新商品订单表（退款）订单状态 ===> {}", cancelRefundSuccess.getType());
        QueryWrapper<PmsProductOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);

        PmsProductOrder pmsProductOrder = new PmsProductOrder();
        pmsProductOrder.setOrderStatus(cancelRefundSuccess.getType());

        return this.update(pmsProductOrder, queryWrapper);
    }
}
