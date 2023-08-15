package com.rts.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 品牌表
 * </p>
 *
 * @author rts
 * @since 2022-09-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class PmsBand implements Serializable {

    private static final long serialVersionUID = 1L;

    public PmsBand(Integer id, Boolean active) {
        this.id = id;
        this.active = active;
    }

    public PmsBand(String name, String logo, String description) {
        this.name = name;
        this.logo = logo;
        this.description = description;
    }

    public PmsBand(Integer id, String name, String logo, String description) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.description = description;
    }

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 品牌名
     */
    private String name;

    /**
     * 品牌logo
     */
    private String logo;

    /**
     * 品牌故事
     */
    private String description;

    /**
     * 状态
     */
    private Boolean active;


}
