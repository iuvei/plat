package com.gameportal.comms.http.utils;

import java.io.Serializable;

/**
 * 定义错误码。
 * 
 * @Version 1.0.0
 * @since jdk1.5
 */
public class ErrorCode implements Serializable{
	
	// 序列化UID
	private static final long serialVersionUID = -1679458253208555786L;

	/**
	 * 必填参数为空。
	 */
	public final static int PARAMETER_EMPTY = 2001;
	
	/**
	 * 必填参数无效。
	 */
	public final static int PARAMETER_INVALID = 2002;
	
	/**
	 * 服务器响应数据无效。
	 */
	public final static int RESPONSE_DATA_INVALID = 2003;
	
	/**
	 * 网络错误。
	 */
	public final static int NETWORK_ERROR = 3000;
}