package com.na.manager.task;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.na.manager.service.IStatisticsUserService;
import com.na.manager.util.DateUtil;

/**
 * 统计用户数据
 */
@Component
public class StatisticsUserDataTask implements Job {
	
	private static Logger log = Logger.getLogger(StatisticsUserDataTask.class);
	
	@Autowired
	private IStatisticsUserService statisticsUserService;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.debug("开始统计任务");
		
		String beginTime = DateUtil.getOneHoursAgoTime(new Date(), "yyyy-MM-dd HH:mm:ss");
		String endTime= DateUtil.string(new Date(), "yyyy-MM-dd HH:mm:ss");
		
		statisticsUserService.add(beginTime, endTime);
		log.debug("完成统计任务");
	}
}
