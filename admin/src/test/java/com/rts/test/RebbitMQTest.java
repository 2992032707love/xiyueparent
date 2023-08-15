package com.rts.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RebbitMQTest {
    @Resource
    RabbitTemplate rabbitTemplate;
    @Test
    public void handler() {
        rabbitTemplate.convertAndSend("rtsemail", "13294634102旭旭宝宝");
    }
}
