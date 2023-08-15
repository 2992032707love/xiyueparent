package com.rts.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResultJson {
    private Integer code;
    private Object content;
    private String message;
    private Map<String, Object> data = new HashMap<>();

    private ResultJson(Integer code, Object content, String message) {
        this.code = code;
        this.content = content;
        this.message = message;
    }
    private ResultJson(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    public static ResultJson getInstance(ResultCode resultCode,Object content,String message){
        return new ResultJson(resultCode.getCode(),content,message);
    }
    public static ResultJson getyzsuccess(ResultCode resultCode, String message){
        return new ResultJson(resultCode.getCode(),message);
    }
    public static ResultJson success(Object content,String message){
        return getInstance(ResultCode.SUCCESS, content,message);
    }
    public static ResultJson yzsuccess(Object content,String message){
        return getyzsuccess(ResultCode.SUCCESS, message);
    }
    public static ResultJson success(Object content){
        return success(content,null);
    }
    public static ResultJson success(String message) {
        return success(null, message);
    }
    public static ResultJson failed(Object content,String message){
        return getInstance(ResultCode.FAILED,content,message);
    }
    public static ResultJson failed(String message){
        return failed(null ,message);
    }
    public static ResultJson unauth(String message) {
        return getInstance(ResultCode.UNAUTH, null, message);
    }
    public static ResultJson forBid() {
        return getInstance(ResultCode.FORBID, null, "该用户无此操作权限");
    }

    public static ResultJson ok() {
        ResultJson resultJson = new ResultJson();
        resultJson.setCode(200);
        resultJson.setMessage("成功");
        return resultJson;
    }

    public static ResultJson error() {
        ResultJson resultJson = new ResultJson();
        resultJson.setCode(-1);
        resultJson.setMessage("失败");
        return resultJson;
    }

    public  ResultJson data(String key, Object value) {
        this.data.put(key, value);
        return this;

    }
}
