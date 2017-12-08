package com.gameportal.filter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取webapp物理路径
 * System.getProperty("webapp.root");获取路径
 * @author YTO_CS
 *
 */
public class WebRootServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(WebRootServlet.class);
	
	private static final String CONFIG_LOCATION_PARAM = "webAppRootKey";
	
	public void init() throws ServletException {
		logger.info("将webapp物理路径写入System Properties系统属性中。");
		String webkey = getServletContext().getInitParameter(CONFIG_LOCATION_PARAM);
		if(null == webkey || "".equals(webkey)){
			throw new NullPointerException(CONFIG_LOCATION_PARAM+"值为空请在web.xml文件里面配置<context-param>标签。");
		}
		String path = getServletContext().getRealPath("/");
		System.setProperty(webkey, path);
	}
}
