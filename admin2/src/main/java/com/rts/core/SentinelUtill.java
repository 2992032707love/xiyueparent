package com.rts.core;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.rts.common.ResultJson;


public class SentinelUtill {
    // 兜底方法 返回值类型、参数和原方法一样但是多了个BlockException
    public static ResultJson handlerException(Integer pageNo , Integer pageSize, String value, String loginId, BlockException ex) {
        System.out.println(pageNo);
        System.out.println(pageSize);
        System.out.println(loginId);
        ex.printStackTrace();
        return ResultJson.failed("压力太大，顶不住了");
    }
}
