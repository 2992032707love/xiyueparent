package com.rts.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 商品订单表
 * </p>
 *
 * @author rts
 * @since 2023-04-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class PmsProductOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    public PmsProductOrder(Integer id, String orderStatus) {
        this.id = id;
        this.orderStatus = orderStatus;
    }

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商家用户id
     */
    private Integer businessId;

    /**
     * 买家用户id
     */
    private Integer customId;

    /**
     * 买家订单编号
     */
    private String orderNo;

    /**
     * 商品表名
     */
    private String tablename;

    /**
     * 订单标题
     */
    private String title;

    /**
     * 订单金额(元)
     */
    private BigDecimal totalYuan;

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
     * 收货人姓名
     */
    private String name;

    /**
     * 收货人手机号
     */
    private String phone;

    /**
     * 收货地址
     */
    private String address;

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
}
