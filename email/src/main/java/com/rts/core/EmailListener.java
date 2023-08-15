package com.rts.core;

import com.alibaba.fastjson.JSONObject;
import com.rts.common.RtsEmail;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

// 放入ioc容器
@Component
public class EmailListener {
    // 取配置文件里面的值
    @Value("${spring.mail.username}")
    String from;
    @Resource
    JavaMailSender javaMailSender;
    // 监听哪个rabbitmq
    @RabbitListener(queues = "rtsemail")
    public void email (Message message) {
        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        RtsEmail rtsEmail = JSONObject.parseObject(body, RtsEmail.class);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(rtsEmail.getTo());
        mailMessage.setSubject(rtsEmail.getSubject());
        mailMessage.setText(rtsEmail.getTextl());
        javaMailSender.send(mailMessage);
//        System.out.println(body);
    }
    @RabbitListener(queues = "rtsYZM")
    public void yzm (Message message) {
        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        RtsEmail rtsEmail = JSONObject.parseObject(body, RtsEmail.class);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(rtsEmail.getTo());
        mailMessage.setSubject(rtsEmail.getSubject());
        mailMessage.setText(rtsEmail.getTextl());
        javaMailSender.send(mailMessage);
    }
}
