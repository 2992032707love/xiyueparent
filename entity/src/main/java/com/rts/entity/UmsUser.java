package com.rts.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author rts
 * @since 2022-09-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class UmsUser implements Serializable {

    private static final long serialVersionUID = 1L;

    public UmsUser(String name, String phone, String email, String icon, String password) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.icon = icon;
        this.password = password;
    }

    public UmsUser(Integer id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public UmsUser(Integer id, String password) {
        this.id = id;
        this.password = password;
    }

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像
     */
    private String icon;

    /**
     * 用户密码
     */
    @JsonIgnore
    private String password;

    /**
     * 状态
     */
    private Boolean active;


}
