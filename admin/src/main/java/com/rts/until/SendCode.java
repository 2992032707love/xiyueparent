package com.rts.until;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * 验证码工具类
 */
@Slf4j
public class SendCode {
    /**
     * 获取验证码
     */
    public static String getSendCode() {
        log.info("验证码生成！！！！");
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            builder.append(random.nextInt(10));
        }
        System.out.println("验证码为："+builder.toString());
        return builder.toString();
    }
//    public static String getYZMcode() {
//        log.info("验证码生成！！！！");
//        // 定义一个随机生成数技术，用来生成随机数
//        Random code = new Random();
//        // 2，用String常用API-charAit生成验证码 //定义一个String变量存放需要的数据，一共58位
//        String yzm1 = "1234567890abcdefghijklmnopqrstuvwxwzABCDEFGHIJKLMNOPQRSTUVWXYZ";
//        String yzm3 = "";// 定义一个空的Atring变量用来接收生成的验证码
//        for (int i = 0; i < 5; i++) {
//            int a = code.nextInt(58);// 随机生成0-57之间的数，提供索引位
//            yzm3+= yzm1.charAt(a);// 用get 和提供的索引找到相应位置的数据给变量
//        }
//        log.info("验证码为： ", yzm3);
//        return yzm3;
//    }
}
