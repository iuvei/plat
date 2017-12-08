package com.gameportal.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class URLFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpPesponse = (HttpServletResponse)response;
		//String  path  =  httpRequest.getContextPath()+httpRequest.getServletPath();
		String requestedWith = httpRequest.getHeader("x-requested-with");
		if(!StringUtils.isNotBlank(requestedWith)){
			String referer = httpRequest.getHeader("Referer");//获取请求来源
			if(StringUtils.isNotBlank(referer)){
				//非Ajax请求
				String refererParams = "";
				if(referer.indexOf("?") > 0){//验证源地址有参数
					refererParams = referer.substring(referer.indexOf("?"), referer.length());
				}
				if(StringUtils.isNotBlank(refererParams)){
					String  realPath  =  httpRequest.getScheme()+"://"+httpRequest.getServerName()+httpRequest.getContextPath()+httpRequest.getServletPath();  
				   String params = httpRequest.getQueryString();
				   if(!StringUtils.isNotBlank(params)){
					    realPath +=refererParams;
						httpPesponse.sendRedirect(realPath);
						return;
				   }
				   
				}
			}
		}
		filterChain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
