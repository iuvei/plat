package com.na.manager.common;

import org.quartz.*;
import org.quartz.spi.JobFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * Quartz 定时器配置
 * 
 * @author nick
 * @date 2017年4月27日 上午11:54:33
 */
@Configuration
@EnableScheduling
public class QuartzConfig {
	
	@Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
		QuartzJobFactory jobFactory = new QuartzJobFactory();
       jobFactory.setApplicationContext(applicationContext);
       return jobFactory;
    }
	
	@Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory){
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(jobFactory);
//        factory.setQuartzProperties(quartzProperties());
        return schedulerFactoryBean; 
    }
	
	/**
	 * 添加一次性任务
	 * @param date 开始时间
	 */
	public static void addJob(SchedulerFactoryBean schedulerFactoryBean, Class <? extends Job> jobClass,
			String jobName, Date date, Map<String, Object> dataMap) {
		if(date == null) {
			return ;
		}
		
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		
		SimpleTrigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(jobName)
				.startAt(date)
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(0)).build();
		
		JobDetail job;
		if(dataMap != null) {
			job = JobBuilder.newJob(jobClass).withIdentity(jobName).setJobData(new JobDataMap(dataMap)).build();
		} else {
			job = JobBuilder.newJob(jobClass).withIdentity(jobName).build();
		}
		
	    try {
			scheduler.scheduleJob(job, trigger);
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 添加任务
	 */
	public static void addJob(SchedulerFactoryBean schedulerFactoryBean,Class <? extends Job> jobClass,
			String jobName, String cronPattern, Map<String, Object> dataMap) {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		
		JobDetail job;
		if(dataMap != null) {
			job = JobBuilder.newJob(jobClass).withIdentity(jobName).setJobData(new JobDataMap(dataMap)).build();
		} else {
			job = JobBuilder.newJob(jobClass).withIdentity(jobName).build();
		}
		
		Trigger trigger;
		if(!StringUtils.isEmpty(cronPattern)) {
			trigger = TriggerBuilder.newTrigger().withIdentity(jobName)
					.withSchedule(CronScheduleBuilder.cronSchedule(cronPattern)).build();
		} else {
			trigger = TriggerBuilder.newTrigger().withIdentity(jobName).build();
		}
		
	    try {
			scheduler.scheduleJob(job, trigger);
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 移除任务
	 * @param schedulerFactoryBean
	 * @param jobName
	 */
	public static void removeJob(SchedulerFactoryBean schedulerFactoryBean,String jobName) {  
        try {
        	TriggerKey triggerKey = new TriggerKey(jobName);
            Scheduler sched = schedulerFactoryBean.getScheduler();  
            // 停止触发器
            sched.pauseTrigger(triggerKey);  
            // 移除触发器
            sched.unscheduleJob(triggerKey);  
            // 删除任务  
            sched.deleteJob(new JobKey(jobName));
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }
	
	
}
