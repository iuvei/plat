package com.na.gate.vo;

/**
 * Created by Administrator on 2017/7/28 0028.
 */
public class PlatformResponse {
    private long code;
    private String msg;
    private String url;

    public static PlatformResponse createSuccess(String url){
        PlatformResponse response = new PlatformResponse();
        response.code = 0;
        response.msg = "success";
        response.url = url;
        return  response;
    }

    public static PlatformResponse createError(String msg){
        PlatformResponse response = new PlatformResponse();
        response.code = 1;
        response.msg = msg;
        return  response;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
