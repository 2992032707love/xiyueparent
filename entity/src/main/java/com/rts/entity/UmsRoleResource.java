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
 * 角色关联资源 
 * </p>
 *
 * @author rts
 * @since 2023-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class UmsRoleResource implements Serializable {

    private static final long serialVersionUID = 1L;

    public UmsRoleResource(Integer roleId, Integer resourceId, Integer resourceType) {
        this.roleId = roleId;
        this.resourceId = resourceId;
        this.resourceType = resourceType;
    }

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 资源id
     */
    private Integer resourceId;

    /**
     * 资源类别
     */
    private Integer resourceType;


}
