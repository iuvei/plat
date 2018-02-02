package com.na.user.socketserver.exception;

import com.na.user.socketserver.common.enums.ErrorCode;
import com.na.user.socketserver.common.enums.SocketExceptionEnum;

/**
 * 通用异常类。
 * Created by sunny on 2017/4/27 0027.
 */
public class CacheContainsException extends SocketException {
	
	public static SocketException createError(String msg,Object... args){
        SocketException exception = new CacheContainsException(msg);
        exception.setVal(SocketExceptionEnum.ERROR);
        exception.setMsg(msg);
        exception.setArgs(args);
        return exception;
    }
    
    public static SocketException createError(ErrorCode code ,String msg,Object... args){
        SocketException exception = new CacheContainsException(msg);
        exception.setVal(SocketExceptionEnum.ERROR);
        exception.setMsg(code.get() + ":" + msg);
        exception.setArgs(args);
        return exception;
    }
	
    public CacheContainsException(String message) {
		super(message);
	}
}
