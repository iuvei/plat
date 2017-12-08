package com.gameportal.comms.http;

import com.gameportal.comms.exception.OpensnsException;


/**
 * 简单的HTTP请求处理器
 * @author brooke
 *
 */
public interface IHttpRequestHandler {
	
	// 数据编码
	public static final String ENCODING_UTF8 = "UTF-8";
	public static final String ENCODING_GBK = "GBK";
	public static final String ENCODING_GB2312 = "GB2312";
	
	public static final String POST = "POST";
	
	public static final String GET = "GET";

	/**
	 * HTTP请求
	 * @param methodUrl 请求URL地址
	 * @param params 请求参数
	 * @param method 请求类型POST和GET
	 * @param encoding 数据编码
	 * @return
	 * @throws OpensnsException
	 */
	public String request(String methodUrl, String params,String method,String encoding) throws OpensnsException;
}
