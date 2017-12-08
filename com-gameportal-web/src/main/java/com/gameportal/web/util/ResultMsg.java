package com.gameportal.web.util;

/**
 * 消息接收类
 *
 * @author menu
 *
 */
public class ResultMsg {

    private String code;//1 表示成功
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return "ResultMsg [code=" + code + ", message=" + message + "]";
    }

}
