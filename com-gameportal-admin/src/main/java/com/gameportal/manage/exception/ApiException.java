package com.gameportal.manage.exception;

/**
 * API异常
 * @author Administrator
 *
 */
public class ApiException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2667147866720317803L;
	
	/**
	 * 错误CODE
	 */
	private String errorCode;
	
	public ApiException(){
		super();
	}
	
	/**
	 * 构造异常
	 * 
	 * @param code 异常状态码
	 * @param msg 异常讯息
	 */
	public ApiException(String message) {
		super(message);
	}
	
	/**
	 * 构造异常
	 * 
	 * @param code 异常状态码
	 * @param msg 异常讯息
	 */
	public ApiException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
	
	public ApiException(String errorCode,Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}
	
	public ApiException(String message,String errorCode,Throwable cause){
		super(message, cause);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
}
