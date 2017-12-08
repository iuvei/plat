package com.gameportal.web.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取客户端IP
 * @author Administrator
 * @version 1.0.0
 */
public class ClientIP {
	
	private static ClientIP instance;

	public static ClientIP getInstance()
	{
		if(instance!=null){
			return instance;
		}else{
			makeInstance();
			return instance;
		}
	}
	
	public static synchronized void makeInstance()
	{
		if(instance==null)
		{
			instance = new ClientIP();
		}
	}

	/**
	 * 如:X-Forwarded-For:192.168.1.110, 192.168.1.120, 192.168.1.130, 192.168.1.100
	 * 用户真实IP为:192.168.1.110
	 * @param request
	 * @return
	 */
	public String getIpAddr(HttpServletRequest request) {
	       String ip = request.getHeader("x-forwarded-for");
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	          ip = request.getHeader("Proxy-Client-IP");
	       }
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	          ip = request.getHeader("WL-Proxy-Client-IP");
	       }
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	          ip = request.getRemoteAddr();
	       }
	       if(ip.indexOf(",") !=-1){ //多级反向代理，截取有效IP
	    	   ip =ip.split(",")[0];
	       }
	       return ip;
	}
	
	/**
	 * 获取URL包括参数
	 * @param request
	 * @return
	 */
	public String getURL(HttpServletRequest request){
		 String  realPath  =  request.getScheme()+"://"+request.getServerName()+request.getContextPath()+request.getServletPath();  
	     String params = request.getQueryString();
	     if(null != params && !"".equals(params)){
	    	 realPath = realPath+"?"+params;
	     }
		return realPath;
	}
	
	
	public String getDomain(HttpServletRequest request){
		return request.getServerName();
	}
}
