package com.rts.aop;


import org.aspectj.lang.JoinPoint;


public interface Strategy_Email {
    //修改密码后发送邮件（修改密码）
    public void changePassword(JoinPoint joinPoint, Object res);

    //重置密码后发送邮件（找回密码）
    public void RecoverPassword(JoinPoint joinPoint);

    //给新创建的??用户发送邮件
    public void sendMail(JoinPoint joinPoint);

    //给??用户发送验证码（找回密码）
    public void SendMailYZM(JoinPoint joinPoint, Object res);

    //给??用户发送验证码（修改密码）
    public void SendMailYZMOne(JoinPoint joinPoint, Object res);
}
