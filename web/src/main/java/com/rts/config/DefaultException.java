package com.rts.config;

import com.rts.common.ResultJson;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DefaultException {
    @ExceptionHandler
    ResultJson defaultExceptionHandler(Exception ex){
        // 把错误信息打印在控制台上
        ex.printStackTrace();
        if (ex instanceof MyException) {
            return ResultJson.failed(ex.getMessage());
        }
        return ResultJson.failed("系统异常，请联系管理员");
    }
}
