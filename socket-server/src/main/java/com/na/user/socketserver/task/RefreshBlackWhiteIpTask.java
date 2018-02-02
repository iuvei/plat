package com.na.user.socketserver.task;


import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.common.SpringContextUtil;
import com.na.user.socketserver.service.IIpBlackWhiteService;
import com.na.user.socketserver.util.DateUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 刷新黑白名单列表
 */
@Component
public class RefreshBlackWhiteIpTask implements Job {

	private Logger logger = LoggerFactory.getLogger(RefreshBlackWhiteIpTask.class);
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("启动黑白名单定时刷新任务【{}】", DateUtil.format(new Date(),DateUtil.yyyy_MM_ddHHMMss));
		IIpBlackWhiteService ipBlackWhiteService =(IIpBlackWhiteService) SpringContextUtil.getBean("ipBlackWhiteServiceImpl");
		AppCache.initBlackWhiteIpMap(ipBlackWhiteService.findAll());
	}
}
