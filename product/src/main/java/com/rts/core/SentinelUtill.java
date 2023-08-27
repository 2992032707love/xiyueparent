package com.rts.core;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.rts.common.ResultJson;

public class SentinelUtill {
    // 兜底方法 返回值类型、参数和原方法一样但是多了个BlockException
    public static ResultJson handlerException(Integer pageNo , Integer pageSize, String value, BlockException ex) {
        System.out.println(pageNo);
        System.out.println(pageSize);
        ex.printStackTrace();
        return ResultJson.failed("压力太大，数据库要顶不住了！");
    }
}
