package com.rts.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 资源表
 * </p>
 *
 * @author rts
 * @since 2023-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class UmsResource implements Serializable {

    private static final long serialVersionUID = 1L;

    public UmsResource(String name, Integer parentId, Integer level, Integer type, String frontUrl, String backUrl) {
        this.name = name;
        this.parentId = parentId;
        this.level = level;
        this.type = type;
        this.frontUrl = frontUrl;
        this.backUrl = backUrl;
    }

    public UmsResource(Integer id, String name, Integer type, String frontUrl, String backUrl) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.frontUrl = frontUrl;
        this.backUrl = backUrl;
    }

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 上级id
     */
    private Integer parentId;

    /**
     * 当前层级
     */
    private Integer level;

    /**
     * 1-目录 0-按钮
     */
    private Integer type;

    /**
     * 前端地址
     */
    private String frontUrl;

    /**
     * 后端地址
     */
    private String backUrl;

    /**
     * 下级资源
     */
    @TableField(exist = false)
    private List<UmsResource> children = new ArrayList<>();
}
