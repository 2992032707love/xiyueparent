//package com.rts.aop;
//
//import com.alibaba.fastjson.JSONObject;
//import com.rts.common.RtsEmail;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.*;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
////import org.springframework.mail.SimpleMailMessage;
////import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
//@Component
//@Aspect
//@Slf4j
//public class AdminAop {
//    @Resource
//    RabbitTemplate rabbitTemplate;
////    @Resource
////    JavaMailSender javaMailSender;
//    // 前置通知(Before) 哪个方法之前发邮件 后置通知(After)
////    @Before(value = "execution(public * com.rts.service.impl.UmsUserServiceImpl.list(..))")
////    @After(value = "execution(public * com.rts.service.impl.UmsUserServiceImpl.list(..))")
////    @AfterReturning(value = "execution(public * com.rts.service.impl.UmsUserServiceImpl.list(..))")
////    @Around(value = "execution(public * com.rts.service.impl.UmsUserServiceImpl.list(..))")
//
//    /**
//     * 给新创建的管理员用户发送邮件
//     * @param joinPoint
//     * @throws Throwable
//     */
//    @AfterReturning(value = "execution(public * com.rts.service.impl.UmsUserServiceImpl.add(..))")
//    public void sendMail(JoinPoint joinPoint) throws Throwable {
////         把切点植入进来(目标方法的对象)
////        System.out.println("发邮件前");
////         调用目标方法
////        IPage result = (IPage) joinPoint.proceed();
////        System.out.println("发邮件");
//        Object[] args = joinPoint.getArgs();
//        String name = args[0].toString();
//        String phone = args[1].toString();
//        String email = args[2].toString();
//        String password = args[4].toString();
////        SimpleMailMessage mailMessage = new SimpleMailMessage();
////        mailMessage.setSubject("系统消息");
////        mailMessage.setFrom("2992032707@qq.com");
////        mailMessage.setTo(email);
//        StringBuilder text = new StringBuilder();
//        text.append("尊敬的").append(name).append(":\n")
//                .append("\t系统为您创建了使用账号，登录名:")
//                .append(phone).append(" 或者：").append(email).append(", 密码：")
//                .append(password).append("。");
////        mailMessage.setText(text.toString());
//        RtsEmail rtsEmail = new RtsEmail(text.toString(), email, "系统消息");
////        javaMailSender.send(mailMessage);
//        rabbitTemplate.convertAndSend("rtsemail", JSONObject.toJSONString(rtsEmail));
//    }
//
//    /**
//     * 给管理员用户发送验证码（找回密码）
//     * @param joinPoint
//     * @param res
//     */
//    @AfterReturning(value = "execution(public * com.rts.service.impl.UmsUserServiceImpl.sendCode(..))", returning = "res")
//    public void SendMailYZM(JoinPoint joinPoint, Object res) {
//        log.info("进入增强方法!!");
//        Object[] args = joinPoint.getArgs();
//        String email = args[0].toString();
//        StringBuilder text = new StringBuilder();
//        text.append("尊敬的用户：\n")
//                .append("\t发送的验证码为：")
//                .append(res).append(", 10分钟内有效请勿将其告知他人。");
//        RtsEmail rtsEmail = new RtsEmail(text.toString(), email, "系统消息");
//        System.out.println("res的值为：" + res);
//        rabbitTemplate.convertAndSend("rtsYZM", JSONObject.toJSONString(rtsEmail));
//        log.info("发送邮件成功！！");
//    }
//
//    /**
//     * 给管理员用户重置密码后发送邮件（找回密码）
//     * @param joinPoint
//     */
//    @AfterReturning(value = "execution(public * com.rts.service.impl.UmsUserServiceImpl.recoverPassword(..))")
//    public void RecoverPassword(JoinPoint joinPoint) {
//        log.info("进入增强方法!!");
//        Object[] args = joinPoint.getArgs();
//        String email = args[0].toString();
//        StringBuilder text = new StringBuilder();
//        text.append("尊敬的用户：\n")
//                .append("\t邮箱为")
//                .append(email)
//                .append("的用户重置密码成功，重置的密码为：")
//                .append("15635062626").append(", 请保管好您的密码，不要告诉他人。");
//        RtsEmail rtsEmail = new RtsEmail(text.toString(), email, "系统消息");
//        System.out.println("email的值为：" + email);
//        rabbitTemplate.convertAndSend("rtsYZM", JSONObject.toJSONString(rtsEmail));
//        log.info("发送邮件成功！！");
//    }
//
//    /**
//     * 给管理员发送验证码（修改密码）
//     * @param joinPoint
//     * @param res
//     */
//    @AfterReturning(value = "execution(public * com.rts.service.impl.UmsUserServiceImpl.SendCode(..))", returning = "res")
//    public void SendMailYZMOne(JoinPoint joinPoint, Object res) {
//        log.info("进入增强方法！！");
//        Object[] args = joinPoint.getArgs();
//        String email = args[0].toString();
//        StringBuilder text = new StringBuilder();
//        text.append("尊敬的用户：\n")
//                .append("\t验证码为：")
//                .append(res).append(", 10分钟内有效请勿将其告知他人。");
//        RtsEmail rtsEmail = new RtsEmail(text.toString(), email, "系统消息");
//        System.out.println("res的值为：" + res);
//        rabbitTemplate.convertAndSend("rtsYZM", JSONObject.toJSONString(rtsEmail));
//        log.info("发送邮件成功！！");
//    }
//
//    /**
//     * 在管理员修改密码后发送邮件 （修改密码）
//     * @param joinPoint
//     * @param res
//     */
//    @AfterReturning(value = "execution(public * com.rts.service.impl.UmsUserServiceImpl.changePassword(..))", returning = "res")
//    public void changePassword(JoinPoint joinPoint, Object res) {
//        log.info("进入增强方法！！");
//        Object[] args = joinPoint.getArgs();
//        String password = args[1].toString();
//        String email = (String) res;
//        StringBuilder text = new StringBuilder();
//        text.append("尊敬的用户：\n")
//                .append("\t邮箱为：").append(res)
//                .append("的账号修改密码成功，修改后的密码为：")
//                .append(password)
//                .append("请不要将密码告诉他人！");
//        RtsEmail rtsEmail = new RtsEmail(text.toString(), email, "系统消息");
//        rabbitTemplate.convertAndSend("rtsemail", JSONObject.toJSONString(rtsEmail));
//    }
//
//    /**
//     * 给新注册的买家用户发送邮件
//     * @param joinPoint
//     */
//    @AfterReturning(value = "execution(public * com.rts.service.impl.UmsUserInformationServiceImpl.add(..))")
//    public void sendMailTwo(JoinPoint joinPoint) {
//        log.info("进入增强方法！");
//        Object[] args = joinPoint.getArgs();
//        String name = args[0].toString();
//        String phone = args[1].toString();
//        String email = args[2].toString();
//        String password = args[4].toString();
//        String address = args[5].toString();
//        StringBuilder text = new StringBuilder();
//        text.append("尊敬的").append(name).append(":\n")
//                .append("\t系统为您创建了使用账号，登录名:")
//                .append(phone).append("或者：").append(email).append("，密码：")
//                .append(password).append("，默认地址为：")
//                .append(address).append("。");
//        RtsEmail rtsEmail = new RtsEmail(text.toString(), email, "系统消息");
//        rabbitTemplate.convertAndSend("rtsemail", JSONObject.toJSONString(rtsEmail));
//        log.info("发送邮件成功！");
//    }
//
//    /**
//     * 给买家用户发送验证码（找回密码）
//     * @param joinPoint
//     * @param res
//     */
//    @AfterReturning(value = "execution(public * com.rts.service.impl.UmsUserInformationServiceImpl.sendCode(..))", returning = "res")
//    public void SendMailYZMBuyers(JoinPoint joinPoint, Object res) {
//        log.info("进入增强方法！！");
//        Object[] args = joinPoint.getArgs();
//        String email = args[0].toString();
//        StringBuilder text = new StringBuilder();
//        text.append("尊敬的买家用户：\n")
//                .append("\t发送的验证码为：")
//                .append(res).append(", 10分钟内有效请勿将其告知他人。");
//        RtsEmail rtsEmail = new RtsEmail(text.toString(), email, "系统消息");
//        System.out.println("res的值为：" + res);
//        rabbitTemplate.convertAndSend("rtsYZM", JSONObject.toJSONString(rtsEmail));
//        log.info("发送邮件成功！！");
//    }
//
//    /**
//     * 给重置密码后的买家用户发送邮件 （找回密码）
//     * @param joinPoint
//     */
//    @AfterReturning(value = "execution(public * com.rts.service.impl.UmsUserInformationServiceImpl.recoverPassword(..))")
//    public void RecoverPasswordBuyers(JoinPoint joinPoint) {
//        log.info("进入增强方法！！");
//        Object[] args = joinPoint.getArgs();
//        String email = args[0].toString();
//        StringBuilder text = new StringBuilder();
//        text.append("尊敬的买家用户：\n")
//                .append("\t邮箱为")
//                .append(email)
//                .append("的用户重置密码成功，重置的密码为：")
//                .append("15635062626").append(", 请保管好您的密码，不要告诉他人。");
//        RtsEmail rtsEmail = new RtsEmail(text.toString(), email, "系统消息");
//        System.out.println("email的值为：" + email);
//        rabbitTemplate.convertAndSend("rtsYZM", JSONObject.toJSONString(rtsEmail));
//        log.info("发送邮件成功！！");
//    }
//
//    /**
//     * 给买家用户发送验证码（修改密码）
//     * @param joinPoint
//     * @param res
//     */
//    @AfterReturning(value = "execution(public * com.rts.service.impl.UmsUserInformationServiceImpl.SendCode(..))", returning = "res")
//    public void SendMailYZMbuyerstwo(JoinPoint joinPoint, Object res) {
//        log.info("进入增强方法！！");
//        Object[] args = joinPoint.getArgs();
//        String email = args[0].toString();
//        StringBuilder text = new StringBuilder();
//        text.append("尊敬的买家用户：\n")
//                .append("\t验证码为：")
//                .append(res).append(", 10分钟内有效请勿将其告知他人。");
//        RtsEmail rtsEmail = new RtsEmail(text.toString(), email, "系统消息");
//        System.out.println("res的值为：" + res);
//        rabbitTemplate.convertAndSend("rtsYZM", JSONObject.toJSONString(rtsEmail));
//        log.info("发送邮件成功！！");
//    }
//
//    /**
//     * 在买家用户修改密码后发送邮件 （修改密码）
//     * @param joinPoint
//     * @param res
//     */
//    @AfterReturning(value = "execution(public * com.rts.service.impl.UmsUserInformationServiceImpl.changePassword(..))", returning = "res")
//    public void changePasswordBuyers(JoinPoint joinPoint, Object res) {
//        log.info("进入增强方法！！");
//        Object[] args = joinPoint.getArgs();
//        String password = args[1].toString();
//        String email = (String) res;
//        StringBuilder text = new StringBuilder();
//        text.append("尊敬的买家用户：\n")
//                .append("\t邮箱为：").append(res)
//                .append("的账号修改密码成功，修改后的密码为：")
//                .append(password)
//                .append("请不要将密码告诉他人！");
//        RtsEmail rtsEmail = new RtsEmail(text.toString(), email, "系统消息");
//        rabbitTemplate.convertAndSend("rtsemail", JSONObject.toJSONString(rtsEmail));
//    }
//
//    /**
//     * 给新注册的商家用户发送邮件
//     * @param joinPoint
//     */
//    @AfterReturning(value = "execution(public * com.rts.service.impl.UmsBusinessUserServiceImpl.add(..))")
//    public void sendMailThree(JoinPoint joinPoint) {
//        log.info("进入增强方法！");
//        Object[] args = joinPoint.getArgs();
//        String name = args[0].toString();
//        String phone = args[1].toString();
//        String email = args[2].toString();
//        String password = args[4].toString();
//        StringBuilder text = new StringBuilder();
//        text.append("尊敬的").append(name).append("：\n")
//                .append("\t系统为您创建了使用账号，登录名：")
//                .append(phone).append("或者：").append(email).append("，密码：")
//                .append(password).append("。");
//        RtsEmail rtsEmail = new RtsEmail(text.toString(), email, "系统消息");
//        rabbitTemplate.convertAndSend("rtsemail", JSONObject.toJSONString(rtsEmail));
//        log.info("发送邮件成功！");
//    }
//
//    /**
//     * 给商家用户发送验证码（找回密码）
//     * @param joinPoint
//     * @param res
//     */
//    @AfterReturning(value = "execution(public * com.rts.service.impl.UmsBusinessUserServiceImpl.sendCode(..))", returning = "res")
//    public void SendMailYZMBusiness(JoinPoint joinPoint, Object res) {
//        log.info("进入增强方法！！");
//        Object[] args = joinPoint.getArgs();
//        String email = args[0].toString();
//        StringBuilder text = new StringBuilder();
//        text.append("尊敬的商家用户：\n")
//                .append("\t发送的验证码为：")
//                .append(res).append(", 10分钟内有效请勿将其告知他人。");
//        RtsEmail rtsEmail = new RtsEmail(text.toString(), email, "系统消息");
//        System.out.println("res的值为：" + res);
//        rabbitTemplate.convertAndSend("rtsYZM", JSONObject.toJSONString(rtsEmail));
//        log.info("发送邮件成功！！");
//    }
//
//    /**
//     * 给重置密码后的商家用户发送邮件 （找回密码）
//     * @param joinPoint
//     */
//    @AfterReturning(value = "execution(public * com.rts.service.impl.UmsBusinessUserServiceImpl.recoverPassword(..))")
//    public void RecoverPasswordBusiness(JoinPoint joinPoint) {
//        log.info("进入增强方法！！");
//        Object[] args = joinPoint.getArgs();
//        String email = args[0].toString();
//        StringBuilder text = new StringBuilder();
//        text.append("尊敬的商家用户：\n")
//                .append("\t邮箱为")
//                .append(email)
//                .append("的用户重置密码成功，重置的密码为：")
//                .append("15635062626").append(", 请保管好您的密码，不要告诉他人。");
//        RtsEmail rtsEmail = new RtsEmail(text.toString(), email, "系统消息");
//        System.out.println("email的值为：" + email);
//        rabbitTemplate.convertAndSend("rtsYZM", JSONObject.toJSONString(rtsEmail));
//        log.info("发送邮件成功！！");
//    }
//
//    /**
//     * 给商家用户发送验证码（修改密码）
//     * @param joinPoint
//     * @param res
//     */
//    @AfterReturning(value = "execution(public * com.rts.service.impl.UmsBusinessUserServiceImpl.SendCode(..))", returning = "res")
//    public void SendMailYZMBusinessThree(JoinPoint joinPoint, Object res) {
//        log.info("进入增强方法！！");
//        Object[] args = joinPoint.getArgs();
//        String email = args[0].toString();
//        StringBuilder text = new StringBuilder();
//        text.append("尊敬的商家用户：\n")
//                .append("\t验证码为：")
//                .append(res).append(", 10分钟内有效请勿将其告知他人。");
//        RtsEmail rtsEmail = new RtsEmail(text.toString(), email, "系统消息");
//        System.out.println("res的值为：" + res);
//        rabbitTemplate.convertAndSend("rtsYZM", JSONObject.toJSONString(rtsEmail));
//        log.info("发送邮件成功！！");
//    }
//
//    /**
//     * 在商家用户修改密码后发送邮件 （修改密码）
//     * @param joinPoint
//     * @param res
//     */
//    @AfterReturning(value = "execution(public * com.rts.service.impl.UmsBusinessUserServiceImpl.changePassword(..))", returning = "res")
//    public void changePasswordBusiness(JoinPoint joinPoint, Object res) {
//        log.info("进入增强方法！！");
//        Object[] args = joinPoint.getArgs();
//        String password = args[1].toString();
//        String email = (String) res;
//        StringBuilder text = new StringBuilder();
//        text.append("尊敬的商家用户：\n")
//                .append("\t邮箱为：").append(res)
//                .append("的账号修改密码成功，修改后的密码为：")
//                .append(password)
//                .append("请不要将密码告诉他人！");
//        RtsEmail rtsEmail = new RtsEmail(text.toString(), email, "系统消息");
//        rabbitTemplate.convertAndSend("rtsemail", JSONObject.toJSONString(rtsEmail));
//    }
//}
