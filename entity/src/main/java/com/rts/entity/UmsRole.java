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
 * 角色表
 * </p>
 *
 * @author rts
 * @since 2023-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class UmsRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    public UmsRole(Integer id, Boolean active) {
        this.id = id;
        this.active = active;
    }

    public UmsRole(String name) {
        this.name = name;
    }

    public UmsRole(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * 角色名称
     */
    private String name;

    /**
     * 状态
     */
    private Boolean active;


}
