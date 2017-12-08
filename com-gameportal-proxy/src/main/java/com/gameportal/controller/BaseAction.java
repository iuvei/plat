package com.gameportal.controller;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.gameportal.domain.PageData;


/**
 * 控制器基类
 * @author CS
 *
 */
@Controller
public class BaseAction {

	// AbstractController 
	protected static final Logger logger = LoggerFactory.getLogger(BaseAction.class);
	
	protected static final ModelAndView SUCCESS = null;
	
	protected static final String ContentType = "text/html; charset=utf-8";
	protected static final String REDIRECT = "redirect:";
	
	/**
	 * 获得Session
	 * @return HttpSession
	 */
	public HttpSession getSession(){
		return getRequest().getSession();
	}
	
	/**
	 * 获得Session
	 * @param flag 表示为此请求创建一个新会话（如有必要）；false 表示返回 null（如果没有当前会话）
	 * @return HttpSession 与此请求关联的 HttpSession，如果 create 为 false，并且该请求没有有效会话，则返回 null
	 */
	public HttpSession getSession(boolean flag){
		return getRequest().getSession(flag);
	}
	
	/**
	 * 返回request
	 * @return HttpServletRequest
	 */
	public HttpServletRequest getRequest(){
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		
		return request;
	}
	
	
	/**
	 * 返回属性
	 * @param value 属性名称
	 * @return String
	 */
	public String getParameter(String value){
		
		return getRequest().getParameter(value);
	}
	
	public String[] getParameterValues(String name){
		return getRequest().getParameterValues(name);
	}
	
	/**
	 * 设置Request值
	 * @param key 键
	 * @param value 值
	 */
	public void setAttribute(String key ,String value){
		getRequest().setAttribute(key, value);
	}
	
	/**
	 * 设置Session值
	 * @param key 键
	 * @param value 值
	 */
	public void setAttribute(String key ,Object value){
		getSession().setAttribute(key, value);
	}
	
	/**
	 * 获得项目路径
	 * @return
	 */
	public String getPath(){
		String path = getRequest().getContextPath();
		String basePath = getRequest().getScheme()+"://"+getRequest().getServerName()+":"+getRequest().getServerPort()+path+"/";
		return basePath;
	}
	
	/**
	 * 输出脚本到页面
	 * @param script
	 */
	public void writerScript(String script){
		PrintWriter out = null;
		try {
			out = ResponseContext.getResponse().getWriter();
			out.print(script);
			out.flush();
		} catch (IOException e) {
			logger.error("输出流错误。", e);
		}finally{
			if(out != null){
				out.close();
			}
		}
	}
	
	/**
	 * 得到ModelAndView
	 */
	public ModelAndView getModelAndView(){
		return new ModelAndView();
	}
	
	/**
	 * 返回结果
	 * @param mv
	 * @param status 
	 * @param msg 返回消息
	 * @return
	 */
	public ModelAndView getResult(ModelAndView mv,String status,String msg){
		mv.addObject("status",status);
		mv.addObject("msg",msg);
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 得到PageData
	 */
	public PageData getPageData(){
		return new PageData(this.getRequest());
	}
	
	/**
	 * 如:X-Forwarded-For:192.168.1.110, 192.168.1.120, 192.168.1.130, 192.168.1.100
	 * 用户真实IP为:192.168.1.110
	 * @param request
	 * @return
	 */
	public String getIpAddr() {
		HttpServletRequest request = getRequest();
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
        return ip;
	 }
}
