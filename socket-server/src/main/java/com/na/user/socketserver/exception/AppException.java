package com.na.user.socketserver.exception;

import com.na.user.socketserver.common.enums.SocketExceptionEnum;

/**
 * 通用异常类。
 * Created by sunny on 2017/4/27 0027.
 */
public class AppException extends RuntimeException{
    private SocketExceptionEnum val;
    private String msg;

    public static AppException createError(String msg){
        AppException exception = new AppException();
        exception.val = SocketExceptionEnum.ERROR;
        exception.msg = msg;
        return exception;
    }

    public SocketExceptionEnum getVal() {
        return val;
    }

    public void setVal(SocketExceptionEnum val) {
        this.val = val;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
