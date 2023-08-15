package com.rts.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 买家用户表
 * </p>
 *
 * @author rts
 * @since 2023-03-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class UmsUserInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    public UmsUserInformation(String name, String phone, String email, String icon, String password, String address) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.icon = icon;
        this.password = password;
        this.address = address;
    }

    public UmsUserInformation(String name, String phone, String email, String icon, String password, String address, String addressId, String addressValue) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.icon = icon;
        this.password = password;
        this.address = address;
        this.addressId = addressId;
        this.addressValue = addressValue;
    }

    public UmsUserInformation(Integer id, String name, String phone, String email, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public UmsUserInformation(Integer id, String name, String phone, String email, String address, String addressId, String addressValue) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.addressId = addressId;
        this.addressValue = addressValue;
    }

    public UmsUserInformation(Integer id, String password) {
        this.id = id;
        this.password = password;
    }

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 姓名
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
     * 地址
     */
    private String address;

    /**
     * 状态
     */
    private Boolean isdefault;

    /**
     * 状态
     */
    private Boolean active;

    /**
     * 省市区id
     */
    private String addressId;

    /**
     * 详细地址
     */
    private String addressValue;
}
