package com.gameportal.controller;

import javax.servlet.http.HttpServletResponse;

/**
 * 获取ResponseContext
 * @author brooke
 *
 */
public class ResponseContext {

	//响应线程
	private static ThreadLocal<HttpServletResponse> response_threadLocal = new ThreadLocal<HttpServletResponse>();
	
	public static void setResponse(HttpServletResponse response){
		response_threadLocal.set(response);
	}
	
	public static HttpServletResponse getResponse(){
		return response_threadLocal.get();
	}
}
