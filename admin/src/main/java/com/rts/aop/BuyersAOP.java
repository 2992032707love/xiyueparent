package com.rts.aop;

import com.alibaba.fastjson.JSONObject;
import com.rts.common.RtsEmail;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@Aspect
public class BuyersAOP implements Strategy_Email{

    @Resource
    RabbitTemplate rabbitTemplate;

    /**
     * 在买家用户修改密码后发送邮件 （修改密码）
     * @param joinPoint
     * @param res
     */
    @Override
    @AfterReturning(value = "execution(public * com.rts.service.impl.UmsUserInformationServiceImpl.changePassword(..))", returning = "res")
    public void changePassword(JoinPoint joinPoint, Object res) {
        log.info("进入增强方法！！");
        Object[] args = joinPoint.getArgs();
        String password = args[1].toString();
        String email = (String) res;
        StringBuilder text = new StringBuilder();
        text.append("尊敬的买家用户：\n")
                .append("\t邮箱为：").append(res)
                .append("的账号修改密码成功，修改后的密码为：")
                .append(password)
                .append("请不要将密码告诉他人！");
        RtsEmail rtsEmail = new RtsEmail(text.toString(), email, "系统消息");
        rabbitTemplate.convertAndSend("rtsemail", JSONObject.toJSONString(rtsEmail));
    }

    /**
     * 给重置密码后的买家用户发送邮件 （找回密码）
     * @param joinPoint
     */
    @Override
    @AfterReturning(value = "execution(public * com.rts.service.impl.UmsUserInformationServiceImpl.recoverPassword(..))")
    public void RecoverPassword(JoinPoint joinPoint) {
        log.info("进入增强方法！！");
        Object[] args = joinPoint.getArgs();
        String email = args[0].toString();
        StringBuilder text = new StringBuilder();
        text.append("尊敬的买家用户：\n")
                .append("\t邮箱为")
                .append(email)
                .append("的用户重置密码成功，重置的密码为：")
                .append("15635062626").append(", 请保管好您的密码，不要告诉他人。");
        RtsEmail rtsEmail = new RtsEmail(text.toString(), email, "系统消息");
        System.out.println("email的值为：" + email);
        rabbitTemplate.convertAndSend("rtsYZM", JSONObject.toJSONString(rtsEmail));
        log.info("发送邮件成功！！");
    }

    /**
     * 给新注册的买家用户发送邮件
     * @param joinPoint
     */
    @Override
    @AfterReturning(value = "execution(public * com.rts.service.impl.UmsUserInformationServiceImpl.add(..))")
    public void sendMail(JoinPoint joinPoint) {
        log.info("进入增强方法！");
        Object[] args = joinPoint.getArgs();
        String name = args[0].toString();
        String phone = args[1].toString();
        String email = args[2].toString();
        String password = args[4].toString();
        String address = args[5].toString();
        StringBuilder text = new StringBuilder();
        text.append("尊敬的").append(name).append(":\n")
                .append("\t系统为您创建了使用账号，登录名:")
                .append(phone).append("或者：").append(email).append("，密码：")
                .append(password).append("，默认地址为：")
                .append(address).append("。");
        RtsEmail rtsEmail = new RtsEmail(text.toString(), email, "系统消息");
        rabbitTemplate.convertAndSend("rtsemail", JSONObject.toJSONString(rtsEmail));
        log.info("发送邮件成功！");
    }

    /**
     * 给买家用户发送验证码（找回密码）
     * @param joinPoint
     * @param res
     */
    @Override
    @AfterReturning(value = "execution(public * com.rts.service.impl.UmsUserInformationServiceImpl.sendCode(..))", returning = "res")
    public void SendMailYZM(JoinPoint joinPoint, Object res) {
        log.info("进入增强方法！！");
        Object[] args = joinPoint.getArgs();
        String email = args[0].toString();
        StringBuilder text = new StringBuilder();
        text.append("尊敬的买家用户：\n")
                .append("\t发送的验证码为：")
                .append(res).append(", 10分钟内有效请勿将其告知他人。");
        RtsEmail rtsEmail = new RtsEmail(text.toString(), email, "系统消息");
        System.out.println("res的值为：" + res);
        rabbitTemplate.convertAndSend("rtsYZM", JSONObject.toJSONString(rtsEmail));
        log.info("发送邮件成功！！");
    }

    /**
     * 给买家用户发送验证码（修改密码）
     * @param joinPoint
     * @param res
     */
    @Override
    @AfterReturning(value = "execution(public * com.rts.service.impl.UmsUserInformationServiceImpl.SendCode(..))", returning = "res")
    public void SendMailYZMOne(JoinPoint joinPoint, Object res) {
        log.info("进入增强方法！！");
        Object[] args = joinPoint.getArgs();
        String email = args[0].toString();
        StringBuilder text = new StringBuilder();
        text.append("尊敬的买家用户：\n")
                .append("\t验证码为：")
                .append(res).append(", 10分钟内有效请勿将其告知他人。");
        RtsEmail rtsEmail = new RtsEmail(text.toString(), email, "系统消息");
        System.out.println("res的值为：" + res);
        rabbitTemplate.convertAndSend("rtsYZM", JSONObject.toJSONString(rtsEmail));
        log.info("发送邮件成功！！");
    }
}
