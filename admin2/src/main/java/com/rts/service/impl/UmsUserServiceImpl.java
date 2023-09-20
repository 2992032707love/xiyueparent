package com.rts.service.impl;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rts.config.MyException;
import com.rts.entity.UmsResource;
import com.rts.entity.UmsUser;
import com.rts.mapper.UmsUserMapper;
import com.rts.service.FileService;
import com.rts.service.UmsResourceService;
import com.rts.service.UmsUserService;
import com.rts.until.SendCode;
import io.minio.errors.*;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author rts
 * @since 2022-09-17
 */
@Service
@Slf4j
@Getter
@Data
public class UmsUserServiceImpl extends ServiceImpl<UmsUserMapper, UmsUser> implements UmsUserService {
    public String YZM;
    @Resource
    BCryptPasswordEncoder passwordEncoder;
    @Resource
    FileService fileService;
    @Resource
    RedisTemplate<String, Object> redisTemplate;
    @Resource
    UmsResourceService umsResourceService;
    @Override
    public List<UmsUser> getActive() {
        QueryWrapper<UmsUser> wrapper = new QueryWrapper<>();
        wrapper.eq("active", 1);
        return this.list(wrapper);
    }

    //    @Value("${upload.url}")
//    String url;
//    @Value("${upload.username}")
//    String username;
//    @Value("${upload.password}")
//    String password;
//    String bucket = "icon";
    @Override
    public IPage<UmsUser> list(int pageNo, int pageSize, String value) {
//        System.out.println("查询用户");
        QueryWrapper wrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(value)) {
            // 写数据库表的字段
            wrapper.like("name",value);
            System.out.println("这里是UmsUserServiceImpl的wrapper: " +  wrapper);
        }
        wrapper.orderByDesc("id");
        System.out.println("这里是return前的wrapper" + wrapper);
        return this.page(new Page<>(pageNo, pageSize), wrapper);
    }

    @Override
    public Boolean add(String name, String phone, String email, MultipartFile file, String password) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
//
//        StringBuilder builder = new StringBuilder();
//        // 获取文件名
//        builder.append(NanoIdUtils.randomNanoId())
//                .append(".")
//                // 获取扩展名
//                .append(FilenameUtils.getExtension(file.getOriginalFilename()));
//        // minio的客户端
//        MinioClient client = MinioClient.builder()
//                //地址 上传和下载的是9000 客户端的是9001
//                .endpoint(url)
//                //用户名和密码
//                .credentials(username,password)
//                .build();//这样就创建了一个客户端
//        PutObjectArgs args = PutObjectArgs.builder()
//                // 哪个桶子
//                .bucket(bucket)
//                // 文件的类型
//                .contentType(file.getContentType())
//                // 自己写一个文件名
//                .object(builder.toString())
//                // 传字节流
//                .stream(file.getInputStream(), file.getSize(), 0)
//                .build();
//        client.putObject(args);
        QueryWrapper<UmsUser> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", phone);
        System.out.println("wraper:-------------------  " + wrapper + "  -----------------");
        UmsUser ums = this.getOne(wrapper);
        System.out.println("ums:---------------" + ums + "--------------");
        if (ums == null){
            QueryWrapper<UmsUser> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("email", email);
            System.out.println("wraper1:-------------------  " + wrapper1 + "  -----------------");
            UmsUser ums1 = this.getOne(wrapper1);
            System.out.println("ums1:---------------" + ums1 + "--------------");
            if (ums1 == null) {
                UmsUser umsUser = new UmsUser(
                        name,
                        phone,
                        email,
                        // 返回路径
                        fileService.upload("icon", file),
                        passwordEncoder.encode(password)
                );
                return this.save(umsUser);
            } else {
                throw new MyException("邮箱重复，请重新输入");
            }
        } else {
            throw new MyException("手机号重复，请重新输入");
        }

    }

    /**
     * 更新管理员用户
     * @param id
     * @param name
     * @param phone
     * @param email
     * @param file
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
    public Boolean update(Integer id, String name, String phone, String email, MultipartFile file) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InternalException {
        log.info("进入update()");
        QueryWrapper<UmsUser> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", phone);
        UmsUser ums = this.getOne(wrapper);
        System.out.println("这里是ums：" + ums);
        System.out.println("ums.getId():"+ ums.getId() + " userId:" + id + "ums.getPhone():" + ums.getPhone() + "phone:" + phone);
        System.out.println("1  " + Objects.equals(ums.getId(), id));
        System.out.println("2  " + (ums.getId() == id));
        System.out.println("3  " + (ums.getId()).equals(id));
        System.out.println("1  " + ((Objects.equals(ums.getPhone(), phone) && Objects.equals(ums.getId(), id)) || (ums == null)));
        if (((Objects.equals(ums.getPhone(), phone) && Objects.equals(ums.getId(), id)) || (ums == null))){
            QueryWrapper<UmsUser> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("email", email);
            UmsUser ums1 = this.getOne(wrapper1);
            if (((Objects.equals(ums1.getEmail(), email) && Objects.equals(ums1.getId(), id)) || (ums1 == null))) {
                UmsUser umsUser = new UmsUser(id, name, phone, email);
                if (file != null && file.getSize() > 0) {
                    umsUser.setIcon(fileService.upload("icon",file));
                }
                return updateById(umsUser);
            } else {
                throw new MyException("邮箱重复，请重新输入!");
            }
        } else {
            throw new MyException("手机号重复，请重新输入!");
        }
    }

    /**
     * 登录验证
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> login(String username, String password) throws Exception {
        QueryWrapper<UmsUser> wrapper = new QueryWrapper<>();
        // eq 精确匹配
        wrapper.eq("phone", username)
                .or().eq("email", username);
        UmsUser umsUser = this.getOne(wrapper);
        // 手机号、邮箱不对或 密码不对     密码要和加密之后的比对 第一个参数为没加密的密码，第二个是加了密的
        if (null == umsUser || !(passwordEncoder.matches(password, umsUser.getPassword()))) {
            System.out.println("--- umsUser是： " + umsUser + "-1--");
            System.out.println("--- password是： " + password + "-1--");
            if (null != umsUser) {
                System.out.println("--- umsUser.getPassword()是： " + umsUser.getPassword() + "-1--");
                System.out.println("--- passwordEncoder.matches(password, umsUser.getPassword())是： " + passwordEncoder.matches(password, umsUser.getPassword()) + "-1--");
                System.out.println("--- !(passwordEncoder.matches(password, umsUser.getPassword()))是： " + !(passwordEncoder.matches(password, umsUser.getPassword())) + "-1--");
            }
            throw new MyException("用户名或密码错误");
        }
        if (!umsUser.getActive()) {
            System.out.println("--- umsUser是： " + umsUser + "-2--");
            System.out.println("--- password是： " + password + "-2--");
            System.out.println("--- umsUser.getPassword()是： " + umsUser.getPassword() + "-2--");
            System.out.println("--- passwordEncoder.matches(password, umsUser.getPassword())是： " + passwordEncoder.matches(password, umsUser.getPassword()) + "-2--");
            System.out.println("--- !(passwordEncoder.matches(password, umsUser.getPassword()))是： " + !(passwordEncoder.matches(password, umsUser.getPassword())) + "-2--");
            throw new MyException("该用户已经失效，无法登录");
        }
        log.info("--- umsUser是： " + umsUser + "---");
        log.info("--- password是： " + password + "---");
        log.info("--- umsUser.getPassword()是： " + umsUser.getPassword() + "---");
        log.info("--- passwordEncoder.matches(password, umsUser.getPassword())是： " + passwordEncoder.matches(password, umsUser.getPassword()) + "---");
        log.info("--- !(passwordEncoder.matches(password, umsUser.getPassword()))是： " + !(passwordEncoder.matches(password, umsUser.getPassword())) + "---");
        List<UmsResource> source = umsResourceService.getByUserId(umsUser.getId());
        Map<String, Object> map = this.splitResource(source);
        System.out.println("   后端权限地址为：   " + map.get("backUrl") + "    ----");
        String id = NanoIdUtils.randomNanoId();
        redisTemplate.opsForHash().put(id, "loginId", umsUser.getId());
        redisTemplate.opsForHash().put(id, "loginName", umsUser.getName());
        redisTemplate.opsForHash().put(id, "Urls", map.get("backUrl"));
        redisTemplate.expire(id, 30, TimeUnit.MINUTES);
        Map<String, Object> result = new HashMap<>();
        result.put("token", id);
        result.put("menu", map.get("menu"));
        log.info("token的值为： --- " + id + "   ---" );
        System.out.println("   前端权限地址为：   " + map.get("menu") + "    ----");
        return result;
    }

    /**
     * 判断邮箱是否正确，用户是否失效，都对之后生成验证码
     * @param username
     * @return
     */
    @Override
    public String sendCode(String username) {
        log.info("进入sendCode()验证邮箱是否正确........",  "username为：" + username);
        QueryWrapper<UmsUser> wrapper = new QueryWrapper<>();
        // eq 精确匹配
        wrapper.eq("email", username);
        UmsUser umsUser = this.getOne(wrapper);
        log.info("查到的umsUser数据为：",umsUser);
        if (umsUser == null) {
            log.error("邮箱错误，请重新输入!!!", username);
            throw new MyException("邮箱错误，请重新输入!");
        }
        log.info("邮箱存在，验证用户是否失效");
        if (!umsUser.getActive()) {
            log.error("该用户已经失效，请重新申请账号或者恢复账号!");
            throw new MyException("该用户已经失效，请重新申请账号或者恢复账号!");
        }
        log.info("邮箱存在，用户没有失效,生成验证码");
        YZM = SendCode.getSendCode();
        System.out.println("YZM的值为：" + YZM);
        return YZM;
    }

    /**
     * 验证验证码是否正确，正确后重置密码
     * @param username
     * @param code
     * @return
     */
    @Override
    public Boolean recoverPassword(String username, String code) {
        log.info("进入recoverPassword()验证验证码是否正确，正确后重置密码！", "   邮箱为：" + username + "， 验证码为：" + code);
        Integer CODE = Integer.valueOf(code);
        System.out.println("CODE为：" + CODE);
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
        log.info("验证码输入正确！清空验证码并开始重置密码！");
        YZM = null;
        log.info("YZM为：", YZM);
        QueryWrapper<UmsUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", username);
        UmsUser umsUser = new UmsUser();
        umsUser.setPassword(passwordEncoder.encode("15635062626"));
        log.info("重置密码成功！");
        return this.update(umsUser, queryWrapper);
    }

    /**
     * 通过登录id查询该用户所有信息
     * @param loginId
     * @return
     */
    @Override
    public UmsUser getAll(String loginId) {
        QueryWrapper<UmsUser> queryWrapper = new QueryWrapper<>();
        Integer id = Integer.valueOf(loginId);
        queryWrapper.eq("id", id);
        UmsUser umsUser = this.getOne(queryWrapper);
        return umsUser;
    }

    /**
     * 更新管理员个人信息
     * @param id
     * @param name
     * @param phone
     * @param email
     * @param icon
     * @return
     */
    @Override
    public Boolean updateByLoginId(Integer id, String name, String phone, String email, MultipartFile icon) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InternalException {

        log.info("进入updateByLoginId()");
        QueryWrapper<UmsUser> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", phone);
        UmsUser ums= this.getOne(wrapper);
        System.out.println("这里是ums：" + ums);
//        if (ums != null) {
//            System.out.println("ums.getId():"+ ums.getId() + " id:" + id + "ums.getPhone():" + ums.getPhone() + "phone:" + phone);
//            System.out.println("1  " + Objects.equals(ums.getId(), userId));
//            System.out.println("2  " + (ums.getId() == userId));
//            System.out.println("3  " + (ums.getId()).equals(userId));
//            System.out.println("1  " + ((ums == null) || (Objects.equals(ums.getPhone(), phone) && Objects.equals(ums.getId(), userId))));
//        }
        if (((ums == null) || (Objects.equals(ums.getPhone(), phone) && Objects.equals(ums.getId(), id)))){
            QueryWrapper<UmsUser> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("email", email);
            UmsUser ums1 = this.getOne(wrapper1);
            if (((ums1 == null) || (Objects.equals(ums1.getEmail(), email) && Objects.equals(ums1.getId(), id)))) {
                UmsUser umsUser = new UmsUser(id, name, phone, email);
                if (icon != null && icon.getSize() > 0) {
                    umsUser.setIcon(fileService.upload("icon", icon));
                }
                return updateById(umsUser);
            } else {
                throw new MyException("邮箱重复，请重新输入!");
            }
        } else {
            throw new MyException("手机号重复，请重新输入!");
        }
    }

    /**
     * 管理员修改密码判断邮箱是否输入正确（根据用户id）
     * @param email
     * @param id
     * @return
     */
    @Override
    public String SendCode(String email, Integer id) {
        log.info("(管理员修改密码)进入SendCode()方法判断邮箱是否输入正确，正确后生成验证码");
        QueryWrapper<UmsUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        UmsUser ums = this.getOne(queryWrapper);
        if (!Objects.equals(ums.getEmail(), email)){
            log.error("邮箱输入错误，请重新输入邮箱！");
            throw new MyException("邮箱输入错误，请重新输入邮箱！");
        }
        log.info("邮箱输入正确！，用户没有失效,生成验证码");
        YZM = SendCode.getSendCode();
        System.out.println("YZM的值为：" + YZM);
        return YZM;
    }

    /**
     * 管理员通过邮箱修改密码 判断验证码是否正确
     * @param email
     * @param code
     * @return
     */
    @Override
    public Boolean Verify(String email, String code) {
        log.info("管理员进入Verify()修改密码判断验证码是否正确", "   邮箱为：" + email + "， 验证码为：" + code);
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
     * 管理员修改密码
     * @param newPassword
     * @param password
     * @param id
     * @return
     */
    @Override
    public String changePassword(String newPassword, String password, Integer id) {
        log.info("进入changePassword()管理员修改密码！");
        QueryWrapper<UmsUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        UmsUser ums = this.getOne(queryWrapper);
        if (passwordEncoder.matches(password, ums.getPassword())) {
            log.error("新密码和近期密码相同请重新输入密码！！");
            throw new MyException("新密码和近期密码相同请重新输入密码！！");
        }
        UmsUser umsUser = new UmsUser(id, passwordEncoder.encode(password));
        updateById(umsUser);
        return ums.getEmail();
    }

    /**
     * 分离权限
     * @param source
     * @return
     */
    private Map<String, Object> splitResource(List<UmsResource> source) {
        List<UmsResource> menu = new ArrayList<>();
        Set<String> backUrls = new HashSet<>();
        for (UmsResource umsResource : source) {
            if (umsResource.getType() == 0) {
                backUrls.add(umsResource.getBackUrl());
                continue;
            }
            if (umsResource.getLevel() == 1) {
                menu.add(umsResource);
                continue;
            }
            for (UmsResource parent : source) {
                if (parent.getId().intValue() == umsResource.getParentId().intValue()) {
                    parent.getChildren().add(umsResource);
                    break;
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("menu", menu);
        map.put("backUrl", backUrls);
        return map;
    }
}