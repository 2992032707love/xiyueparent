package com.rts.test;

import com.mysql.cj.util.TimeUtil;
import com.rts.ProductApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductApp.class)
public class RedisTest {
    @Resource(name = "redisTemplate")
    RedisTemplate<String, Object> redisTemplate;
    @Test
    public void handler () {
//        redisTemplate.opsForValue().set("a","111111");
//        System.out.println(redisTemplate.hasKey("pms::category"));
//        System.out.println(redisTemplate.delete("a"));
//        redisTemplate.opsForHash().put("a","name","任怡旭");
//        redisTemplate.opsForHash().put("a","age",34);
//        26:05
        redisTemplate.expire("a",60, TimeUnit.SECONDS);
    }
}
