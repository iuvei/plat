package com.na.manager.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.na.manager.cache.AppCache;
import com.na.manager.common.SpringContextUtil;
import com.na.manager.service.IIpBlackWhiteService;

/**
 * 刷新黑白名单列表
 */
@Component
public class RefreshBlackWhiteIpTask implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		IIpBlackWhiteService ipBlackWhiteService =(IIpBlackWhiteService)SpringContextUtil.getBean("ipBlackWhiteServiceImpl");
		AppCache.initBlackWhiteIpMap(ipBlackWhiteService.findAll());
	}
}
