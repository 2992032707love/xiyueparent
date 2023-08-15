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

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartProductSkus implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 购物车表主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 买家用户id
     */
    private Integer customId;

    /**
     * 商品id
     */
    private Integer productId;

    /**
     * 商品sku属性id
     */
    private Integer skusId;

    /**
     * 商品数量
     */
    private Integer productSize;

    /**
     * 商品总金额(元)
     */
    private BigDecimal productYuan;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品大图
     */
    private String pic;

    /**
     * sku属性
     */
    @JSONField(name = "items")
    private String item;
}
