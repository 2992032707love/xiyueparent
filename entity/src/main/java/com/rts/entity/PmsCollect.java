package com.rts.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 收藏表
 * </p>
 *
 * @author rts
 * @since 2023-04-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class PmsCollect implements Serializable {

    private static final long serialVersionUID = 1L;

    public PmsCollect(Integer customId, Integer productId) {
        this.customId = customId;
        this.productId = productId;
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


}
