package com.na.user.socketserver.common.enums;

/**
 * Created by Administrator on 2017/4/27 0027.
 */
public enum AppExceptionEnum {
    /**异常*/
    ERROR(1);
    private int val;

    AppExceptionEnum(int val){
        this.val = val;
    }

    public int get(){return val;};
}