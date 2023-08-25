package com.rts.test;

import io.lettuce.core.output.ScanOutput;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;

@Slf4j
public class SendCode {
//    /**
//     * 获取验证码
//     */
//    public static String getSendCode() {
//        return getYZMcode();
//    }
//    public static String getYZMcode() {
//        log.info("验证码生成！！！！");
//        // 定义一个随机生成数技术，用来生成随机数
//        Random yzm = new Random();
//        // 2，用String常用API-charAit生成验证码 //定义一个String变量存放需要的数据，一共58位
//        String yzm1 = "1234567890abcdefghijklmnopqrstuvwxwzABCDEFGHIJKLMNOPQRSTUVWXYZ";
//        String yzm3 = "";// 定义一个空的Atring变量用来接收生成的验证码
//        for (int i = 0; i < 5; i++) {
//            int a = yzm.nextInt(58);//随机生成0-57之间的数，提供索引位置
//            yzm3+=yzm1.charAt(a);//用get 和提供的索引找到相应位置的数据给变量
//            System.out.println(yzm3+=yzm1.charAt(a));
//        }
//        log.info("验证码为： ", yzm3);
//        System.out.println(yzm3);
//        String code = yzm3;
//        return code;
//    }
//
//    public static void main(String[] args) {
//        Random yzm = new Random();                          //定义一个随机生成数技术，用来生成随机数
////2，用String常用API-charAit生成验证码
//        String yzm1 = "1234567890abcdefghijklmnopqrstuvwxwzABCDEFGHIJKLMNOPQRSTUVWXYZ";//定义一个String变量存放需要的数据，一共58位
//        String yzm3 = "";//定义一个空的Atring变量用来接收生成的验证码
//        for (int i = 0; i < 5; i++) {
//            int a = yzm.nextInt(58);//随机生成0-57之间的数，提供索引位置
//            yzm3+=yzm1.charAt(a);//用get 和提供的索引找到相应位置的数据给变量
//
//        }
//        System.out.println("用String常用API-charAit生成的验证码为:"+yzm3);
//        if (getYZMcode() == null) {
//            System.out.println(false);
//        } else {
//            System.out.println(true);
//            System.out.println(getSendCode());
//        }
//        System.out.println("    asdad    " + getSendCode());
//    }
//    /**
//     * 获取验证码
//     */
//    public static String getSendCode() {
//        return getYZMcode();
//    }
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
//
//    public static String next() {
//        StringBuilder builder = new StringBuilder();
//        Random random = new Random();
//        for (int i = 0; i < 6; i++) {
//            builder.append(random.nextInt(10));
//        }
//        return builder.toString();
//    }
//    public static void main(String[] args) {
//       String code = next();
//        System.out.println(code+"   asddd    ");
//    }

    public static String YZM = "456415";

    public static void main(String[] args) {
        String code = "456415";
        Integer CODE = Integer.valueOf(code);
        System.out.println("CODE为：" + CODE);
        System.out.println("YZM为：" + YZM);
        Integer OLDCODE = Integer.valueOf(YZM);
        System.out.println("OLDCODE的值为：" + OLDCODE);
        System.out.println("1 "+ CODE.equals(OLDCODE));
        System.out.println("2" + (CODE == OLDCODE));
        System.out.println("3" + Objects.equals(CODE, OLDCODE));
        System.out.println("4" + code.equals(OLDCODE));
        System.out.println(code.getClass().getName());
        System.out.println(OLDCODE.getClass().getName());
        String sendCode = getSendCode();
        System.out.println("验证码为：" + sendCode + "YZM为：" + YZM);
        YZM = sendCode;
        System.out.println("验证码为：" + YZM);
        System.out.println("push test!");
        System.out.println("pull test!");
    }
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
}
