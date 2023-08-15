package com.rts.common;

public enum ResultCode {
    SUCCESS(200),
    FAILED(500),
    // 非法请求
    UNAUTH(401),
    FORBID(403);

    private Integer code;

    ResultCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
