package com.na.user.socketserver.config;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.spi.JobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.StringUtils;

/**
 * Quartz 定时器配置
 * 
 * @author nick
 * @date 2017年4月27日 上午11:54:33
 */
@Configuration
@EnableScheduling
public class QuartzConfig {
	
	private static Logger log = LoggerFactory.getLogger(QuartzConfig.class);
	
	@Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
		QuartzJobFactory jobFactory = new QuartzJobFactory();
       jobFactory.setApplicationContext(applicationContext);
       return jobFactory;
    }
	
	@Bean(value="schedulerFactoryBean2",name="schedulerFactoryBean2")
    public SchedulerFactoryBean schedulerFactoryBean2(JobFactory jobFactory){
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(jobFactory);
        
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        try {
			propertiesFactoryBean.afterPropertiesSet();
		} catch (IOException e1) {
			log.error(e1.getMessage(),e1);
		}
        
        try {
			schedulerFactoryBean.setQuartzProperties(propertiesFactoryBean.getObject());
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
        return schedulerFactoryBean; 
    }
	
	@Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory){
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(jobFactory);
        
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        try {
			propertiesFactoryBean.afterPropertiesSet();
		} catch (IOException e1) {
			log.error(e1.getMessage(),e1);
		}
        
        try {
			schedulerFactoryBean.setQuartzProperties(propertiesFactoryBean.getObject());
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
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
			log.info("添加自动任务:" + jobName);
		} catch (SchedulerException e) {
			log.error(e.getMessage(),e);
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
			log.info("添加自动任务:" + jobName);
		} catch (SchedulerException e) {
			log.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 移除任务
	 * @param schedulerFactoryBean
	 * @param jobName
	 * @param triggerName
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
            log.info("删除自动任务:" + jobName);
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }
	
	
}
