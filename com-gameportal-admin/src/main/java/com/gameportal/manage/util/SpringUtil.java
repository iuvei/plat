package com.gameportal.manage.util;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringUtil implements ApplicationContextAware {
	// private static ServletContext servletContext = null;
	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}

	public static Object getBean(ServletContext servletContext, String beanName) {
		Object bean = null;
		WebApplicationContext wac = WebApplicationContextUtils
				.getRequiredWebApplicationContext(servletContext);
		if (wac != null)
			bean = wac.getBean(beanName);
		return bean;
	}

	public static <T> T getBean(Class<T> clazz) {
		if(applicationContext!=null)
			return applicationContext.getBean(clazz);
		return null;
	}

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		SpringUtil.applicationContext = context;
	}
}

