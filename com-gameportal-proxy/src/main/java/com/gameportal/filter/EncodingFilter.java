package com.gameportal.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gameportal.comms.StringUtils;
import com.gameportal.comms.http.utils.IPAddress;
import com.gameportal.comms.http.utils.IPRange;

/**
 * 字符编码类
 * 以及IP限制访问
 * @author Administrator
 * @version 1.0.0
 * @data 20140401
 */
@SuppressWarnings("all")
public class EncodingFilter implements Filter{

	/* 日志 */
	private static final Logger logger = LoggerFactory.getLogger(EncodingFilter.class);
	
	/*运行访问IP列表*/
	public static final String  PARAM_NAME_ALLOW            = "allow";
	
	/*限制访问IP列表*/
    public static final String  PARAM_NAME_DENY             = "deny";
    
    /*不过滤URL地址*/
    public static final String PARAM_NAME_NOURL 			= "filterurl";
    
    /*编码字符集*/
    public static final String CONFIG_ENCODING				= "encoding";
    
    /*编码类型*/
    public static final String CONFIG_CONTENTTYPE			= "contentType";
    
    /*是否启用IP控制*/
    public static final String  PARAM_NAME_RESET_ENABLE     = "resetEnable";
    private final static String TEMPLATE_PAGE_RESOURCE_PATH = "/template.html";
    private boolean 			statService;
    public String               templatePage;
    
	/**
	 * 允许访问IP地址列表
	 */
	private List<IPRange>       allowList                   = new ArrayList<IPRange>();
	
	/**
	 * 禁止访问IP地址列表
	 */
    private List<IPRange>       denyList                    = new ArrayList<IPRange>();
    
    /**
     * 不过滤URL地址列表
     */
    private List<String> 		nourlList					= new ArrayList<String>();
    
	private FilterConfig filterConfig;
	private String encoding = null;
	private String contentType = null;

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		templatePage = TEMPLATE_PAGE_RESOURCE_PATH;
		
		//读取字符编码
		try {
			String param = this.filterConfig.getInitParameter(CONFIG_ENCODING);
			if(param != null && param.trim().length() != 0){
				encoding = param.trim();
			}
		} catch (Exception e) {
			String msg = "initParameter config error, encoding : " + filterConfig.getInitParameter(CONFIG_ENCODING);
			logger.error(msg);
		}
		
		//读取编码类型
		try {
			String param = this.filterConfig.getInitParameter(CONFIG_CONTENTTYPE);
			if(param != null && param.trim().length() != 0){
				contentType = param.trim();
			}
		} catch (Exception e) {
			String msg = "initParameter config error, contentType : " + filterConfig.getInitParameter(CONFIG_ENCODING);
			logger.error(msg);
		}
		
		//读取是否启用状态
		try {
			 String param = this.filterConfig.getInitParameter(PARAM_NAME_RESET_ENABLE);
			 if (param != null && param.trim().length() != 0) {
				 param = param.trim();
				 boolean resetEnable = Boolean.parseBoolean(param);
				 statService = resetEnable;
			 }
			 
		}catch (Exception e) {
			String msg = "initParameter config error, resetEnable : " + this.filterConfig.getInitParameter(PARAM_NAME_RESET_ENABLE);
			logger.error(msg);
		}
		
		//读取可以访问的IP地址
		try {
			String param = this.filterConfig.getInitParameter(PARAM_NAME_ALLOW);
			if (param != null && param.trim().length() != 0) {
                param = param.trim();
                String[] items = param.split(",");
                for (String item : items) {
                    if (item == null || item.length() == 0) {
                        continue;
                    }

                    IPRange ipRange = new IPRange(item);
                    allowList.add(ipRange);
                }
			}
		} catch (Exception e) {
			String msg = "initParameter config error, allow : " + this.filterConfig.getInitParameter(PARAM_NAME_ALLOW);
			logger.error(msg);
		}
		
		//读取禁止访问IP地址列表
		try {
			String param = this.filterConfig.getInitParameter(PARAM_NAME_DENY);
			if(param != null && param.trim().length() != 0){
				param = param.trim();
				String[] items = param.split(",");
				for(String item : items){
					if(item == null || item.length() == 0){
						continue;
					}
					IPRange ipRange = new IPRange(item);
					denyList.add(ipRange);
				}
			}
		} catch (Exception e) {
			String msg = "initParameter config error, deny : " + this.filterConfig.getInitParameter(PARAM_NAME_ALLOW);
			logger.error(msg);
		}
		
		//读取不过滤URL
		try {
			String param = this.filterConfig.getInitParameter(PARAM_NAME_NOURL);
			if(param != null && param.trim().length() != 0){
				param = param.trim();
				String[] items = param.split(",");
				for(String item : items){
					if(item == null || item.length() == 0){
						continue;
					}
					nourlList.add(item);
				}
			}
		} catch (Exception e) {
			String msg = "initParameter config error, url : " + this.filterConfig.getInitParameter(PARAM_NAME_NOURL);
			logger.error(msg);
		}
	}
	
	public boolean isPermittedRequest(HttpServletRequest request) {
        String remoteAddress = request.getRemoteAddr();
        return isPermittedRequest(remoteAddress);
    }
	
	/**
	 * 是否允许请求
	 * @param remoteAddress
	 * @return
	 */
	public boolean isPermittedRequest(String remoteAddress) {
        boolean ipV6 = remoteAddress != null && remoteAddress.indexOf(':') != -1;

        if (ipV6) {
            if (denyList.size() == 0 && allowList.size() == 0) {
                return true;
            }
        }

        IPAddress ipAddress = new IPAddress(remoteAddress);

        for (IPRange range : denyList) {
            if (range.isIPAddressInRange(ipAddress)) {
                return false;
            }
        }

        if (allowList.size() > 0) {
            for (IPRange range : allowList) {
                if (range.isIPAddressInRange(ipAddress)) {
                    return true;
                }
            }

            return false;
        }

        return true;
    }
	
	/**
	 * 验证用户访问URL是否不过滤
	 * @param remoteUrl 当前用户访问URL
	 * @return 如果不过滤URL列表中存在当前用户访问的URL则返回<code>true</code> 否则返回<code>false</code>
	 */
	public boolean isFilterUrl(String remoteUrl){
		if(nourlList.size() > 0){
			for(String item : nourlList){
				if(item.equals(remoteUrl)){
					return true;
				}
			}
		}
		return false;
	}
	
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		request.setCharacterEncoding(encoding);
		response.setContentType(contentType);
		HttpServletRequest req = (HttpServletRequest) request;
		String requestURI = req.getRequestURI();
		if(statService){
			//当前访问地址不在未过滤地址列表内
			if(!isFilterUrl(requestURI)){
				
			}
		}
		Enumeration names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String[] values = request.getParameterValues(name);
			if(values !=null && !values.equals("")){
			   for (int i = 0; i < values.length; i++) {
					 if(values[i] == null || "".equals(values[i])){
						 continue;
					 }
					 String checkBefore = values[i];
					 //验证是否是非法字符
					 String checkAfter = StringUtils.StringFilter(checkBefore);
					 //所有参数是非法字符 或者 部分参数是非法字符
					 if(checkAfter.equals("") || checkBefore.length() != checkAfter.length()){
						//非法参数记录日志
						logger.warn("路径包含非法字符:"+checkBefore);
						System.out.println("路径包含非法字符:"+checkBefore);
					 }else{
					    //get请求时，重新编码
					     if ("GET".equals(req.getMethod())) {
					    	 values[i] = new String(values[i].getBytes("ISO-8859-1"),encoding);
					     }
					 }
			   	}
			}
		}
		filterChain.doFilter(request, response);
	}

	public void destroy() {
		filterConfig = null;
		encoding = null;
	}
}