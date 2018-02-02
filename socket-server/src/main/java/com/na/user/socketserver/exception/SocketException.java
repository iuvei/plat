package com.na.user.socketserver.exception;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.na.user.socketserver.common.enums.ErrorCode;
import com.na.user.socketserver.common.enums.SocketExceptionEnum;

/**
 * 通用异常类。
 * Created by sunny on 2017/4/27 0027.
 */
public class SocketException extends RuntimeException {
    private SocketExceptionEnum val;
    private String msg;
    private Object[] args;

    public static SocketException createError(String msg,Object... args){
        SocketException exception = new SocketException(msg);
        exception.val = SocketExceptionEnum.ERROR;
        exception.msg = msg;
        exception.args = args;
        return exception;
    }
    
    public static SocketException createError(ErrorCode code ,String msg,Object... args){
        SocketException exception = new SocketException(msg);
        exception.val = SocketExceptionEnum.ERROR;
        exception.msg = code.get() + ":" + msg;
        exception.args = args;
        return exception;
    }
    
    public SocketException(String message) {
        super(message);
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
    
    public void setArgs(Object[] args) {
		this.args = args;
	}

	public Object[] getArgsArrays(){
        return this.args;
    }

    public String getArgs(){
        return ArrayUtils.toString(this.args);
    }

    /**
     * 判断对象是否为空，为空则抛出异常。
     * @param obj
     * @param msg
     */
    public static void isNull(Object obj, String msg) {
        if (obj == null) {
            throw createError(msg);
        }
        if (obj instanceof String){
            if( StringUtils.isBlank((String)obj) ){
                throw createError(msg);
            }
        }
    }

    /**
     * 判断对象是否不为空，为空则抛出异常。
     * @param obj
     * @param msg
     */
    public static void isNotNull(Object obj, String msg) {
        if (obj != null) {
            throw createError(msg);
        }
    }

    /**
     * 判断条件是否为真，为真则抛出异常。
     * @param isTrue
     * @param msg
     */
    public static void isTrue(boolean isTrue, String msg) {
        if (isTrue) {
            throw createError(msg);
        }
    }
}
