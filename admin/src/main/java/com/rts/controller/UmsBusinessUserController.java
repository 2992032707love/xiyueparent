package com.rts.controller;


import com.rts.common.ResultJson;
import com.rts.entity.UmsBusinessUser;
import com.rts.service.UmsBusinessUserService;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * <p>
 * 商家用户表 前端控制器
 * </p>
 *
 * @author rts
 * @since 2023-03-24
 */
@Slf4j
@RestController
@RequestMapping("/umsBusinessUser")
public class UmsBusinessUserController {
    @Resource
    UmsBusinessUserService umsBusinessUserService;

    /**
     * 商家用户注册
     * @param name
     * @param phone
     * @param email
     * @param icon
     * @param password
     * @return
     */
    @PostMapping("/add")
    ResultJson add (String name, String phone, String email, MultipartFile icon, String password) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InsufficientDataException, ErrorResponseException {
        log.info("进入商家用户添加方法",
                "用户姓名为：" + name +
                        "，手机号为：" + phone +
                        "，电子邮箱为：" + email +
                        "，用户头像的地址为：" + icon +
                        "，密码为：" + password);
        return ResultJson.success(umsBusinessUserService.add(name,phone,email,icon,password), "注册成功，信息已发送至邮箱，请查看！");
    }

    /**
     * 商家用户登录
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    ResultJson login(String username, String password) {
        log.info("调用login()商家用户登录 ===== >");
        log.info("---"  + "umsBusinessUserService.login(username, password)的值为====>" + umsBusinessUserService.login(username, password));
        return ResultJson.success(umsBusinessUserService.login(username, password), "登录成功！");
    }

    /**
     * 商家用户找回密码判断
     * @param username
     * @return
     */
    @PostMapping("/sendCode")
    ResultJson sendCode(String username){
        log.info("商家用户找回密码!");
        System.out.println("username为：" + username);
        log.info("（商家用户）调用sendCode()验证邮箱是否正确，正确后生成验证码并且发送到邮箱 ====>");
        return ResultJson.yzsuccess(umsBusinessUserService.sendCode(username), "邮箱正确用户存在，发送验证码成功，请到邮箱查看验证码!");
    }

    /**
     * 商家用户重置密码
     * @param username
     * @param code
     * @return
     */
    @PostMapping("/recoverPassword")
    ResultJson recoverPassword(String username, String code) {
        System.out.println("username为：" + username + "， 验证码为：" + code);
        log.info("买家调用recoverPassword()验证验证码是否正确，正确之后重置密码为：15635062626  =====>");
        return ResultJson.success(umsBusinessUserService.recoverPassword(username, code), "找回成功，密码重置为：15635062626");
    }

    /**
     * 根据登录id查询该商家用户的个人信息
     * @param loginId
     * @return
     */
    @GetMapping("/list")
    ResultJson list(String loginId) {
        log.info("商家用户调用list()，根据用户id查询该用户所有信息");
        return ResultJson.success(umsBusinessUserService.getAll(loginId));
    }

    /**
     * 分页查询 显示卖家用户和按照输入内容显示该卖家用户的信息
     * @param pageNo
     * @param pageSize
     * @param value
     * @param loginId
     * @return
     */
    @GetMapping("/getAll")
    ResultJson getAll(Integer pageNo, Integer pageSize, String value, String loginId) {
        log.info("loginId为 ====> " + loginId);
        System.out.println(umsBusinessUserService.list());
        return ResultJson.success(umsBusinessUserService.list(pageNo, pageSize, value));
    }

    /**
     * 逻辑删除商家用户
     * @param id
     * @param active
     * @return
     */
    @PostMapping("/delete")
    ResultJson del(Integer id, Boolean active) {
        UmsBusinessUser umsBusinessUser = new UmsBusinessUser();
        umsBusinessUser.setId(id);
        umsBusinessUser.setActive(active);
        return ResultJson.success(umsBusinessUserService.updateById(umsBusinessUser), active ? "恢复用户成功" : "删除用户成功");
    }

    /**
     * 根据登录id查询要修改的商家用户的信息
     * @param loginId
     * @return
     */
    @GetMapping("/getone")
    ResultJson getOne(String loginId) {
        return ResultJson.success(umsBusinessUserService.getById(loginId));
    }

    /**
     * 更新商家用户的个人信息
     * @param name
     * @param phone
     * @param email
     * @param icon
     * @param loginId
     * @return
     */
    @PostMapping("/update")
    ResultJson update(String name, String phone, String email, MultipartFile icon, String loginId) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InsufficientDataException, ErrorResponseException {
        Integer userId = Integer.valueOf(loginId);
        log.info("进入商家更新信息的update()方法调用update()接口！！！");
        return ResultJson.success(umsBusinessUserService.update(userId, name, phone, email, icon), "修改个人信息成功!");
    }

    /**
     * 商家用户通过邮箱修改密码判断
     * @param email
     * @param loginId
     * @return
     */
    @PostMapping("/SendCode")
    ResultJson SendCode(String email, String loginId) {
        log.info("商家用户通过邮箱修改密码!");
        System.out.println("email为：" + email + "，loginId为：" + loginId);
        log.info("（商家用户）调用SendCode()验证邮箱是否正确，正确后生成验证码并且发送到邮箱(通过邮箱修改密码) ====>");
        Integer userId = Integer.valueOf(loginId);
        return ResultJson.yzsuccess(umsBusinessUserService.SendCode(email, userId), "邮箱正确，发送验证码成功，请到邮箱查看验证码!");
    }

    /**
     * 商家用户修改密码 判断验证码是否正确
     * @param email
     * @param code
     * @return
     */
    @PostMapping("/Verify")
    ResultJson Verify(String email, String code) {
        System.out.println("email为：" + email + "， 验证码为：" + code);
        log.info("商家用户调用Verify()验证验证码是否正确  =====>");
        return ResultJson.success(umsBusinessUserService.Verify(email, code), "验证码输入正确！");
    }

    /**
     * 卖家用户修改密码
     * @param newPassword
     * @param password
     * @param loginId
     * @return
     */
    @PostMapping("/changePassword")
    ResultJson changePassword(String newPassword, String password, String loginId) {
        System.out.println("newPassword为：" + newPassword + "， password为：" + password + "登录id为：" + loginId);
        log.info("卖家用户调用changePassword()修改密码！");
        Integer userId = Integer.valueOf(loginId);
        if (!Objects.equals(newPassword, password)) {
            return ResultJson.success("两次密码输入内容不同，请重新输入！");
        } else {
            return ResultJson.success(umsBusinessUserService.changePassword(newPassword, password, userId), "修改密码成功，请重新登录！");
        }
    }
}
