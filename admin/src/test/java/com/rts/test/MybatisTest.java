package com.rts.test;

import com.rts.AdminApp;
import com.rts.entity.UmsUser;
import com.rts.mapper.UmsUserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdminApp.class)
public class MybatisTest {
    @Resource
    UmsUserMapper umsUserMapper;
    @Test
    public void handler() {
        UmsUser umsUser = new UmsUser();
        umsUser.setActive(true);
        umsUser.setName("任庭圣");
        System.out.println(umsUserMapper.queryAll(umsUser));
    }
}
