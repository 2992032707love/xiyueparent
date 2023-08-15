package com.rts.test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class TestThree {
    public static void main(String[] args) {
        HashMap<String,String> hashMap = new HashMap();
        hashMap.put("null","旭旭宝宝");
        Map<String, List> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add("李博文");
        list.add("许刚");
        list.add("李政耀");
        list.add("范瑞新");
        map.put("2",list);
        map.put("xf",list);
        System.out.println(hashMap);
        System.out.println(map);
        Collection<List> values = map.values();
        boolean b = Objects.equals(map.get("2"), map.get("xf"));
        System.out.println(values);
        System.out.println(b);
        ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>();

        try {
            concurrentHashMap.put("null", "null");
            concurrentHashMap.put("null", "null");
        }catch (Exception exception){
            exception.printStackTrace();
        }
        System.out.println(concurrentHashMap);
    }
}
