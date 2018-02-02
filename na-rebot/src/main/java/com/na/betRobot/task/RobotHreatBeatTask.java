package com.na.betRobot.task;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

import com.na.betRobot.ServerClient;
import com.na.betRobot.robot.RobotAction;


/**
 * 机器人投注自动任务
 * 
 * @author alan
 * @date 2017年8月17日 上午11:28:53
 */
@PersistJobDataAfterExecution
public class RobotHreatBeatTask implements Job {
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		ServerClient client = (ServerClient) jobDataMap.get("client");
		RobotAction robot = (RobotAction) jobDataMap.get("robot");
		
		robot.sendHreatBeat(client);
	}

}
