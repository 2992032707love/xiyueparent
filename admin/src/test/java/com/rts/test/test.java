package com.rts.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Slf4j
public class test {
    static int res = 0;
//    static class User{
//         final String name = "Mark";
//        final int age = 20;
//        public String getName() {return name;}
//        public int getAge() { return  age;}
//    }
//    public static void main(String[] args) {
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("user", new User());
//        map.put("arr", new int[]{1,2,3});
//        map.put("flag", true);
//        map.put("str", "LongShine");
//        map.put("year", 1996);
//        System.out.println(JSON.toJSON(map));
//        String a = "a";
//        int b = 1;
//        String k = b + a;
//        log.info(k);
//    }
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("输入字符串");
        String ar = s.next();
        int num = countS(ar);
        System.out.println("该字符串中的回文子串个数为：" + num);
    }
    public static int countS(String s) {
        for (int i = 0; i < s.length(); i++) {
            ex(s, i, i);
            ex(s, i, i + 1);
        }
        return res;
    }
    public static void ex(String s, int start, int end) {
        while (start >= 0 && end < s.length() && s.charAt(start) == s.charAt(end)) {
            start--;
            end++;
            res++;
        }
    }
}
