package com.rts.service.impl;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rts.config.MyException;
import com.rts.entity.UmsUserInformation;
import com.rts.mapper.UmsUserInformationMapper;
import com.rts.service.FileService;
import com.rts.service.UmsUserInformationService;
import com.rts.until.SendCode;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 买家用户表 服务实现类
 * </p>
 *
 * @author rts
 * @since 2023-03-08
 */
@Slf4j
@Service
public class UmsUserInformationServiceImpl extends ServiceImpl<UmsUserInformationMapper, UmsUserInformation> implements UmsUserInformationService {
    public String YZM;
    @Resource
    FileService fileService;
    @Resource
    BCryptPasswordEncoder passwordEncoder;
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    /**
     * 根据用户id查询买家用户个人信息
     * @param userId
     * @return
     */
    @Override
    public UmsUserInformation getAll(String userId) {
        QueryWrapper<UmsUserInformation> wrapper = new QueryWrapper<>();
        wrapper.eq("id", userId);
        UmsUserInformation umsUserInformation = this.getOne(wrapper);
        return umsUserInformation;
    }

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @param value
     * @return
     */
    @Override
    public IPage<UmsUserInformation> list(int pageNo, int pageSize, String value) {
        QueryWrapper wrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(value)) {
            // 写数据库表的字段
            wrapper.like("name", value);
        }
        wrapper.orderByDesc("id");
        return this.page(new Page<>(pageNo, pageSize), wrapper);
    }

    /**
     * 注册买家用户
     * @param name
     * @param phone
     * @param email
     * @param icon
     * @param password
     * @param address
     * @param addressId
     * @param addressValue
     * @return
     * @throws IOException
     * @throws InvalidKeyException
     * @throws InvalidResponseException
     * @throws InsufficientDataException
     * @throws NoSuchAlgorithmException
     * @throws ServerException
     * @throws ErrorResponseException
     * @throws XmlParserException
     * @throws InternalException
     */
    @Override
    @Transactional
    public Boolean add(String name, String phone, String email, MultipartFile icon, String password, String address, String[] addressId, String addressValue) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InternalException {

        QueryWrapper<UmsUserInformation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        UmsUserInformation userInformation = this.getOne(queryWrapper);
        log.info("userInformation为：",userInformation);
        if (userInformation == null) {
            QueryWrapper<UmsUserInformation> wrapper = new QueryWrapper<>();
            wrapper.eq("email", email);
            UmsUserInformation userInformation1 = this.getOne(wrapper);
            log.info("userInformation1为：",userInformation1);
            if (userInformation1 == null) {
                for (int i = 0; i < addressId.length; i++) {
                    System.out.println( "这里是addressId：" + addressId[i]);
                }
                UmsUserInformation umsUserInformation = new UmsUserInformation(
                        name,
                        phone,
                        email,
                        fileService.upload("icon", icon),
                        passwordEncoder.encode(password),
                        address,
                        Arrays.toString(addressId),
                        addressValue
                );
                return this.save(umsUserInformation);
            } else {
                throw new MyException("邮箱重复，请重新输入");
            }
        } else {
            throw new MyException("手机号重复，请重新输入");
        }
    }

    /**
     * 登录验证
     * @param username
     * @param password
     * @return
     */
    @Override
    public Map<String, Object> login(String username, String password) {
        log.info("进入login()进行买家用户登录!");
        QueryWrapper<UmsUserInformation> wrapper = new QueryWrapper<>();
        // eq 精确匹配
        wrapper.eq("phone", username)
                .or().eq("email", username);
        UmsUserInformation umsUserInformation = this.getOne(wrapper);
        // 手机号、邮箱不对或 密码不对     密码要和加密之后的比对 第一个参数为没加密的密码，第二个是加了密的
        if (null == umsUserInformation || !(passwordEncoder.matches(password, umsUserInformation.getPassword()))){
            System.out.println("--- umsUserInformation： " + umsUserInformation + "-1--");
            System.out.println("--- password是： " + password + "-1--");
            if (null != umsUserInformation) {
                System.out.println("--- umsUserInformation.getPassword()是： " + umsUserInformation.getPassword() + "-1--");
                System.out.println("--- passwordEncoder.matches(password, umsUserInformation.getPassword())是： " + passwordEncoder.matches(password, umsUserInformation.getPassword()) + "-1--");
                System.out.println("--- !(passwordEncoder.matches(password, umsUserInformation.getPassword()))是： " + !(passwordEncoder.matches(password, umsUserInformation.getPassword())) + "-1--");
            }
            throw new MyException("用户名或密码错误");
        }
        if (!umsUserInformation.getActive()) {
            System.out.println("--- umsUserInformation： " + umsUserInformation + "-2--");
            System.out.println("--- password是： " + password + "-2--");
            System.out.println("--- umsUserInformation.getPassword()是： " + umsUserInformation.getPassword() + "-2--");
            System.out.println("--- passwordEncoder.matches(password, umsUserInformation.getPassword())是： " + passwordEncoder.matches(password, umsUserInformation.getPassword()) + "-2--");
            System.out.println("--- !(passwordEncoder.matches(password, umsUserInformation.getPassword()))是： " + !(passwordEncoder.matches(password, umsUserInformation.getPassword())) + "-2--");
            throw new MyException("该用户已经失效，无法登录");
        }
        log.info("--- umsUserInformation： " + umsUserInformation + "---");
        log.info("--- password是： " + password + "---");
        log.info("--- umsUserInformation.getPassword()是： " + umsUserInformation.getPassword() + "---");
        log.info("--- passwordEncoder.matches(password, umsUserInformation.getPassword())是： " + passwordEncoder.matches(password, umsUserInformation.getPassword()) + "---");
        log.info("--- !(passwordEncoder.matches(password, umsUserInformation.getPassword()))是： " + !(passwordEncoder.matches(password, umsUserInformation.getPassword())) + "---");
        String id = NanoIdUtils.randomNanoId();
        redisTemplate.opsForHash().put(id, "BuyersloginId", umsUserInformation.getId());
        redisTemplate.opsForHash().put(id, "BuyersloginName", umsUserInformation.getName());
        redisTemplate.expire(id, 30, TimeUnit.MINUTES);
        Map<String, Object> result = new HashMap<>();
        result.put("token", id);
        log.info("token的值为： --- " + id + "   ---" );
        return result;
    }

    /**
     * 买家用户找回密码 判断邮箱是否正确，用户是否失效，都对之后生成验证码
     * @param username
     * @return
     */
    @Override
    public String sendCode(String username) {
        log.info("买家用户进入sendCode()验证邮箱是否正确........",  "username为：" + username);
        QueryWrapper<UmsUserInformation> wrapper = new QueryWrapper<>();
        // eq 精确匹配
        wrapper.eq("email", username);
        UmsUserInformation umsUserInformation = this.getOne(wrapper);
        log.info("查到的umsUser数据为：",umsUserInformation);
        if (umsUserInformation == null) {
            log.error("邮箱错误，请重新输入!!!", username);
            throw new MyException("邮箱错误，请重新输入!");
        }
        log.info("邮箱存在，验证用户是否失效");
        if (!umsUserInformation.getActive()) {
            log.error("该用户已经失效，请重新申请账号或者恢复账号!");
            throw new MyException("该用户已经失效，请重新申请账号或者恢复账号!");
        }
        log.info("邮箱存在，用户没有失效,生成验证码");
        YZM = SendCode.getSendCode();
        System.out.println("YZM的值为：" + YZM);
        return YZM;
    }

    /**
     * 买家用户找回密码 验证验证码是否正确，正确后重置密码
     * @param username
     * @param code
     * @return
     */
    @Override
    public Boolean recoverPassword(String username, String code) {
        log.info("买家进入recoverPassword()验证验证码是否正确，正确后重置密码！", "   邮箱为：" + username + "， 验证码为：" + code);
        Integer CODE = Integer.valueOf(code);
        System.out.println("CODE为：" + CODE);
        if (YZM == null) {
            log.error("没有先发送验证码！请先发送验证码再点击找回!");
            throw new MyException("没有先发送验证码！请先发送验证码再点击找回!");
        }
        System.out.println("YZM为：" + YZM);
        Integer OLDCODE = Integer.valueOf(YZM);
        System.out.println("OLDCODE的值为：" + OLDCODE);
        System.out.println("1   " + (CODE == OLDCODE));
        System.out.println("2   " + CODE.equals(OLDCODE));
        System.out.println("3   " + Objects.equals(CODE, OLDCODE));
        log.info("判断验证码是否相等!");
        if (!Objects.equals(CODE, OLDCODE)) {
            log.error("验证码输入错误，请重新输入！");
            throw new MyException("验证码输入错误，请重新输入！");
        }
        log.info("验证码输入正确！清空验证码并且开始重置密码！");
        System.out.println("YZM清空前：" + YZM);
        YZM = null;
        System.out.println("YZM清空后：" + YZM);
        QueryWrapper<UmsUserInformation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", username);
        UmsUserInformation umsUserInformation = new UmsUserInformation();
        umsUserInformation.setPassword(passwordEncoder.encode("15635062626"));
        log.info("重置密码成功！");
        return this.update(umsUserInformation, queryWrapper);
    }

    /**
     * 更新买家用户信息
     * @param userId
     * @param name
     * @param phone
     * @param email
     * @param icon
     * @param address
     * @param addressId
     * @param addressValue
     * @return
     * @throws IOException
     * @throws InvalidKeyException
     * @throws InvalidResponseException
     * @throws InsufficientDataException
     * @throws NoSuchAlgorithmException
     * @throws ServerException
     * @throws ErrorResponseException
     * @throws XmlParserException
     * @throws InternalException
     */
    @Override
    public Boolean update(Integer userId, String name, String phone, String email, MultipartFile icon, String address, String[] addressId, String addressValue) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InternalException {
        log.info("进入update()");
        QueryWrapper<UmsUserInformation> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", phone);
        UmsUserInformation ums= this.getOne(wrapper);
        System.out.println("这里是ums：" + ums);
        if (ums != null) {
            System.out.println("ums.getId():"+ ums.getId() + " userId:" + userId + "ums.getPhone():" + ums.getPhone() + "phone:" + phone);
            System.out.println("1  " + Objects.equals(ums.getId(), userId));
            System.out.println("2  " + (ums.getId() == userId));
            System.out.println("3  " + (ums.getId()).equals(userId));
            System.out.println("1  " + ((ums == null) || (Objects.equals(ums.getPhone(), phone) && Objects.equals(ums.getId(), userId))));
        }
        if (((ums == null) || (Objects.equals(ums.getPhone(), phone) && Objects.equals(ums.getId(), userId)))){
            QueryWrapper<UmsUserInformation> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("email", email);
            UmsUserInformation ums1 = this.getOne(wrapper1);
            if (((ums1 == null) || (Objects.equals(ums1.getEmail(), email) && Objects.equals(ums1.getId(), userId)))) {
                for (int i = 0; i < addressId.length; i++) {
                    System.out.println( "这里是addressId：" + addressId[i]);
                }
                UmsUserInformation umsUserInformation = new UmsUserInformation(userId, name, phone, email, address, Arrays.toString(addressId), addressValue);
                if (icon != null && icon.getSize() > 0) {
                    umsUserInformation.setIcon(fileService.upload("icon", icon));
                }
                return updateById(umsUserInformation);
            } else {
                throw new MyException("邮箱重复，请重新输入!");
            }
        } else {
            throw new MyException("手机号重复，请重新输入!");
        }
    }

    /**
     * 买家用户通过邮箱修改密码 判断验证码是否正确
     * @param email
     * @param code
     * @return
     */
    @Override
    public Boolean Verify(String email, String code) {
        log.info("买家进入Verify()修改密码判断验证码是否正确", "   邮箱为：" + email + "， 验证码为：" + code);
        Integer CODE = Integer.valueOf(code);
        System.out.println("CODE为：" + CODE);
        if (YZM == null) {
            log.error("没有先发送验证码！请先发送验证码再点击找回!");
            throw new MyException("没有先发送验证码！请先发送验证码再点击找回!");
        }
        System.out.println("YZM为：" + YZM);
        Integer OLDCODE = Integer.valueOf(YZM);
        System.out.println("OLDCODE的值为：" + OLDCODE);
        System.out.println("1   " + (CODE == OLDCODE));
        System.out.println("2   " + CODE.equals(OLDCODE));
        System.out.println("3   " + Objects.equals(CODE, OLDCODE));
        log.info("判断验证码是否相等!");
        if (!Objects.equals(CODE, OLDCODE)) {
            log.error("验证码输入错误，请重新输入！");
            throw new MyException("验证码输入错误，请重新输入！");
        }
        log.info("验证码输入正确！清空验证码并且开始更改密码！");
        System.out.println("YZM清空前：" + YZM);
        YZM = null;
        System.out.println("YZM清空后：" + YZM);
        return true;
    }

    /**
     * 买家用户通过邮箱修改密码
     * @param newPassword
     * @param password
     * @param userId
     * @return
     */
    @Override
    public String changePassword(String newPassword, String password, Integer userId) {
        log.info("进入changePassword()买家修改密码！");
        QueryWrapper<UmsUserInformation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        UmsUserInformation ums = this.getOne(queryWrapper);
        if (passwordEncoder.matches(password, ums.getPassword())) {
            log.error("新密码和近期密码相同请重新输入密码！！");
            throw new MyException("新密码和近期密码相同请重新输入密码！！");
        }
        UmsUserInformation umsUserInformation = new UmsUserInformation(userId, passwordEncoder.encode(password));
        updateById(umsUserInformation);
        return ums.getEmail();
    }

    /**
     * 买家用户修改密码判断邮箱是否输入正确（根据用户id）
     * @param email
     * @param userId
     * @return
     */
    @Override
    public String SendCode(String email, Integer userId) {
        log.info("(买家用户修改密码)进入SendCode()方法判断邮箱是否输入正确，正确后生成验证码");
        QueryWrapper<UmsUserInformation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        UmsUserInformation ums = this.getOne(queryWrapper);
        if (!Objects.equals(ums.getEmail(), email)){
            log.error("邮箱输入错误，请重新输入邮箱！");
            throw new MyException("邮箱输入错误，请重新输入邮箱！");
        }
        log.info("邮箱输入正确！，用户没有失效,生成验证码");
        YZM = SendCode.getSendCode();
        System.out.println("YZM的值为：" + YZM);
        return YZM;
    }

}
