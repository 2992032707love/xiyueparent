package com.rts.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 订单信息表
 * </p>
 *
 * @author rts
 * @since 2023-03-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PmsOrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单标题
     */
    private String title;

    /**
     * 商户订单编号
     */
    private String orderNo;

    /**
     * 商品表名
     */
    private String tablename;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 支付产品id
     */
    private Integer productId;

    /**
     * 订单金额(元)
     */
    private BigDecimal totalYuan;

    /**
     * 订单二维码链接
     */
    private String codeUrl;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 创建时间
     */
    private LocalDateTime creatTime;

    /**
     * 更新时间
     */
    private LocalDateTime updataTime;

    /**
     * 支付方式
     */
    private String paymentType;

    /**
     * 商品数量
     */
    private Integer productSize;

    /**
     * 商品sku属性
     */
    private String skusItem;

    /**
     * 商品sku属性id
     */
    private Integer skusId;

    /**
     * 商家id
     */
    private Integer businessId;

    /**
     * 收货地址
     */
    private String address;

    /**
     * 收货人姓名
     */
    private String name;

    /**
     * 收货人手机号
     */
    private String phone;
}
