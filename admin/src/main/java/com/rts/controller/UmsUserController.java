package com.rts.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.rts.common.JwtUtil;
import com.rts.common.ResultJson;
import com.rts.core.SentinelUtill;
import com.rts.entity.UmsUser;
import com.rts.service.UmsUserService;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Objects;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author rts
 * @since 2022-09-17
 */
@Slf4j
@RestController
@RequestMapping("/umsUser")
public class UmsUserController {
    @Resource
    UmsUserService umsUserService;
//    @Value("${upload.url}")
//    String url;
//    @Value("${upload.username}")
//    String username;
//    @Value("${upload.password}")
//    String password;
//    @Resource
//    UmsUser umsUser;

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @param value
     * @return
     */
    @GetMapping("/list")                 // 哪个类(class对象)的哪个方法(以字符串的方式写这个方法的名字)兜底
    @SentinelResource(value = "user_list", blockHandlerClass = {SentinelUtill.class}, blockHandler = "handlerException")
    ResultJson list (Integer pageNo , Integer pageSize, String value, String loginId) {
//        String token = request.getHeader("token");
//        UmsUser user = JwtUtil.decode(token);
//        System.out.println(user.getId());
//        TimeUnit.SECONDS.sleep(3);
//        System.out.println(umsUser);
//        int i = 100 / 0;
//        System.out.println(url + "   " + username + "    " + password);
        log.info("loginId为 ====> " + loginId);
        return ResultJson.success(umsUserService.list(pageNo, pageSize, value));
    }

    /**
     * 添加用户
     * @param name
     * @param phone
     * @param email
     * @param file
     * @param password
     * @return
     * @throws IOException
     * @throws InvalidResponseException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws ServerException
     * @throws ErrorResponseException
     * @throws XmlParserException
     * @throws InsufficientDataException
     * @throws InternalException
     */
    @PostMapping("/add")
    ResultJson add (String name, String phone, String email, MultipartFile file, String password) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InsufficientDataException, InternalException {
//        TimeUnit.SECONDS.sleep(3);umsUserService.add(name, phone, email, icon, password)
//        // 文件名
//        System.out.println(file.getOriginalFilename());
//        // 文件大小
//        System.out.println(file.getSize());
//        // 选完之后传文件
//        System.out.println(file.getContentType());
        return ResultJson.success(umsUserService.add(name, phone, email, file, password),"添加用户成功");
    }

    /**
     * 根据用户id查询用户
     * @param id
     * @return
     */
    @GetMapping("/getone")
    ResultJson getOne(Integer id) {
        return ResultJson.success((umsUserService.getById(id)));
    }

    /**
     * 修改用户信息
     * @param id
     * @param name
     * @param phone
     * @param email
     * @param file
     * @return
     * @throws IOException
     * @throws InvalidResponseException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws ServerException
     * @throws InternalException
     * @throws XmlParserException
     * @throws InsufficientDataException
     * @throws ErrorResponseException
     */
    @PostMapping ("/update")
    ResultJson update(Integer id, String name, String phone, String email, MultipartFile file) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InsufficientDataException, ErrorResponseException {
        return ResultJson.success(umsUserService.update(id, name, phone, email, file), "修改用户成功");
    }

    /**
     * 删除用户
     * @param id
     * @param active
     * @return
     */
    @PostMapping("/delete")
    ResultJson delete(Integer id, Boolean active) {
        UmsUser umsUser = new UmsUser();
        umsUser.setId(id);
        umsUser.setActive(active);
        return  ResultJson.success(umsUserService.updateById(umsUser), active ? "恢复用户成功" : "删除用户成功");
    }

    /**
     * 登录
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    ResultJson login(String username, String password) throws Exception {
        log.info("调用login()登录 ===== >");
//        log.info("---"  + "JwtUtil.getToken(umsUserService.login(username, password))的值为====>" + JwtUtil.getToken(umsUserService.login(username, password)));
        log.info("---"  + "umsUserService.login(username, password)的值为====>" + umsUserService.login(username, password));
        return ResultJson.success(umsUserService.login(username, password));
    }

    /**
     * 找回密码判断
     * @param username
     * @return
     */
    @PostMapping("/sendCode")
    ResultJson sendCode(String username){
        System.out.println("username为：" + username);
        log.info("调用sendCode()验证邮箱是否正确，正确后生成验证码并且发送到邮箱 ====>");
        return ResultJson.yzsuccess(umsUserService.sendCode(username), "邮箱正确用户存在，发送验证码成功，请到邮箱查看验证码!");
    }

    /**
     * 重置密码
     * @param username
     * @param code
     * @return
     */
    @PostMapping("/recoverPassword")
    ResultJson recoverPassword(String username, String code) {
        System.out.println("username为：" + username + "， 验证码为：" + code);
        log.info("调用recoverPassword()验证验证码是否正确，正确之后重置密码为：15635062626  =====>");
        return ResultJson.success(umsUserService.recoverPassword(username, code), "找回成功，密码重置为：15635062626");
    }

    /**
     * 通过登录id查询该用户所有信息
     * @param loginId
     * @return
     */
    @GetMapping("/getOneByloginId")
    ResultJson getOneByloginId(String loginId) {
        log.info("登录id为：",loginId);
        log.info("管理员调用getOneByloginId()，根据用户id查询该用户所有信息");
        return ResultJson.success(umsUserService.getAll(loginId), "");
    }

    /**
     * 根据登录id查询要修改的用户的信息
     * @param loginId
     * @return
     */
    @GetMapping("/getoneByloginId")
    ResultJson getoneByloginId(String loginId) {
        return ResultJson.success(umsUserService.getById(loginId));
    }

    /**
     * 更新管理员个人信息
     * @param name
     * @param phone
     * @param email
     * @param icon
     * @param loginId
     * @return
     */
    @PostMapping("/updateByLoginId")
    ResultJson updateByLoginId(String name, String phone, String email, MultipartFile icon, String loginId) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InsufficientDataException, ErrorResponseException {
        Integer id = Integer.valueOf(loginId);
        log.info("进入管理员更新自己信息的updateByLoginId()方法调用updateByLoginId()接口！！！");
        return ResultJson.success(umsUserService.updateByLoginId(id, name, phone, email, icon), "修改个人信息成功！");
    }

    /**
     * 管理员修改密码判断邮箱是否输入正确（根据用户id）
     * @param email
     * @param loginId
     * @return
     */
    @PostMapping("/SendCode")
    ResultJson SendCode(String email, String loginId) {
        log.info("管理员通过邮箱修改密码!");
        System.out.println("email为：" + email + "，loginId为：" + loginId);
        log.info("（管理员）调用SendCode()验证邮箱是否正确，正确后生成验证码并且发送到邮箱(通过邮箱修改密码) ====>");
        Integer id = Integer.valueOf(loginId);
        return ResultJson.yzsuccess(umsUserService.SendCode(email, id), "邮箱正确，发送验证码成功，请到邮箱查看验证码!");
    }

    /**
     * 管理员修改密码 判断验证码是否正确
     * @param email
     * @param code
     * @return
     */
    @PostMapping("/Verify")
    ResultJson Verify(String email, String code) {
        System.out.println("email为：" + email + "， 验证码为：" + code);
        log.info("管理员调用Verify()验证验证码是否正确  =====>");
        return ResultJson.success(umsUserService.Verify(email, code),"验证码输入正确！");
    }

    /**
     * 管理员修改密码
     * @param newPassword
     * @param password
     * @param loginId
     * @return
     */
    @PostMapping("/changePassword")
    ResultJson changePassword(String newPassword, String password, String loginId) {
        System.out.println("newPassword为：" + newPassword + "， password为：" + password + "登录id为：" + loginId);
        log.info("卖家用户调用changePassword()修改密码！");
        Integer id = Integer.valueOf(loginId);
        if (!Objects.equals(newPassword, password)) {
            return ResultJson.success("两次密码输入内容不同，请重新输入！");
        } else {
            return ResultJson.success(umsUserService.changePassword(newPassword, password, id), "修改密码成功，请重新登录！");
        }
    }
}
