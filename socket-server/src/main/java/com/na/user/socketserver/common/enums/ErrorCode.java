package com.na.user.socketserver.common.enums;

/**
 * 服务器返回错误码
 * 
 * @author alan
 * @date 2017年9月26日 上午11:58:23
 */
public enum ErrorCode {
	
	/**
	 * 参数错误
	 */
	PARAM_ERROR(20001),
	/**
	 * 系统错误
	 */
	SYSTEM_ERROR(90000),
    /**
     * 重新登录
     */
    RELOGIN(90002)
    ;
    private int val;

    ErrorCode(int val){
        this.val = val;
    }

    public int get(){return val;};
}