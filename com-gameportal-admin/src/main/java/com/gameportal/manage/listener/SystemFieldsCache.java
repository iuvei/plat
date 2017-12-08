package com.gameportal.manage.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoaderListener;

import com.gameportal.manage.system.service.ISystemService;
import com.gameportal.manage.util.SpringUtil;
/**
 * 系统数据字段缓存。
 * @author Administrator
 *
 */
public class SystemFieldsCache extends ContextLoaderListener{
	
	private static Logger logger = Logger.getLogger(SystemFieldsCache.class);

	public static Map<String, String> fields = new HashMap<String, String>();
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		// 加载数据字典数据。
		ISystemService systemService =(ISystemService)SpringUtil.getBean("systemServiceImpl");
		fields.putAll(systemService.qeuryAllFields());
		logger.info("系统数据字典缓存完毕。");
	}
}
