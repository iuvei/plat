package com.gameportal.web.listener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoaderListener;

import com.gameportal.web.cache.PortalGameCache;
import com.gameportal.web.mail.model.MailServer;
import com.gameportal.web.util.XstreamUtil;

public class GameListener extends ContextLoaderListener {

	private static Logger logger = Logger.getLogger(GameListener.class);
	

	@Override
	public void contextInitialized(ServletContextEvent event) {
		// 加载邮箱服务器信息。
		initEmailServer(event);
		//初始化mg token
//		IGamePlatformService gamePlatformService = (IGamePlatformService)SpringUtils.getBean("gamePlatformServiceImpl");
//		gamePlatformService.refreshMgToken();
	}

	/**
	 * 初始化邮箱服务器。
	 */
	private void initEmailServer(ServletContextEvent event) {
		InputStream is = null;
		ByteArrayOutputStream out = null;
		try {
			String path = Thread.currentThread().getContextClassLoader().getResource("../email-server.xml").getPath();
			is = new FileInputStream(new File(path));
			out = new ByteArrayOutputStream();
			int i = 1;
			while ((i = is.read()) != -1) {
				out.write(i);
			}
			MailServer mailServer = XstreamUtil.toBean(new String(out.toByteArray(),"utf-8"), MailServer.class);
			PortalGameCache.mailerverList.addAll(mailServer.getServers());
			logger.info("读取邮箱服务器信息成功。");
			out.close();
			is.close();
		} catch (Exception e) {
			logger.error("读取邮件服务器信息失败。",e);
		}
	}
}
