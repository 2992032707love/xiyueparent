package com.rts.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * sku表
 * </p>
 *
 * @author rts
 * @since 2022-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class PmsSkus implements Serializable {

    private static final long serialVersionUID = 1L;

    public PmsSkus(Integer id, BigDecimal price, Integer stock, BigDecimal oldPrice) {
        this.id = id;
        this.price = price;
        this.stock = stock;
        this.oldPrice = oldPrice;
    }

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * sku属性
     */
    @JSONField(name = "items")
    private String item;

    /**
     * 商品售价
     */
    private BigDecimal price;

    /**
     * 库存数
     */
    private Integer stock;

    /**
     * 商品id
     */
    private Integer productId;

    /**
     * 原价
     */
    private BigDecimal oldPrice;


}
