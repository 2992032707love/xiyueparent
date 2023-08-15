package com.rts.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 分类表
 * </p>
 *
 * @author rts
 * @since 2022-09-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class PmsCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    public PmsCategory(String name, Integer parentId, Integer level) {
        this.name = name;
        this.parentId = parentId;
        this.level = level;
    }

    public PmsCategory(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public PmsCategory(Integer id, Boolean active) {
        this.id = id;
        this.active = active;
    }

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 上级id
     */
    private Integer parentId;

    /**
     * 层级
     */
    private Integer level;

    /**
     * 状态
     */
    private Boolean active;
    /*
    * 子分类
    * */
    @TableField(exist = false)
    private List<PmsCategory> children = new ArrayList<>();
}
