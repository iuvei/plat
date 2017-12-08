package com.gameportal.manage.betlog.util;



import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;

public class AppContext {

	/**
	 * Spring ApplicationContext
	 */
	private static ApplicationContext applicationContext;

	private static ServletContext servletContext;

	public static void setApplicationContext(
			ApplicationContext applicationContext) {
		AppContext.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return AppContext.applicationContext;
	}

	public static ServletContext getServletContext() {
		return servletContext;
	}

	public static void setServletContext(ServletContext servletContext) {
		AppContext.servletContext = servletContext;
	}

}
