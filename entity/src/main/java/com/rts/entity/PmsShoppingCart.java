package com.rts.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 购物车表
 * </p>
 *
 * @author rts
 * @since 2023-04-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class PmsShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    public PmsShoppingCart(Integer customId, Integer productId, Integer skusId, Integer productSize, BigDecimal productYuan) {
        this.customId = customId;
        this.productId = productId;
        this.skusId = skusId;
        this.productSize = productSize;
        this.productYuan = productYuan;
    }

    public PmsShoppingCart(Integer productSize, BigDecimal productYuan) {
        this.productSize = productSize;
        this.productYuan = productYuan;
    }

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
     * sku属性id
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

}
