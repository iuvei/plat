package com.gameportal.manage.betlog.util;



import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.WebUtils;

import com.gameportal.manage.util.SpringUtil;



/**
 * @author zhoujx
 * 
 */
public class SpringContextLoaderListener extends ContextLoaderListener{
	public void contextInitialized(final ServletContextEvent event) {
 		final ServletContext context = event.getServletContext();
		AppContext.setServletContext(context);
 		WebUtils.setWebAppRootSystemProperty(context);
		super.contextInitialized(event);
		final ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
		AppContext.setApplicationContext(ctx);
		new SpringUtil().setApplicationContext(ctx);
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		super.contextDestroyed(event);
	}
}

