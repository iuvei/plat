package com.na.betRobot.task;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.na.betRobot.ServerClient;
import com.na.betRobot.robot.Robot;


/**
 * 机器人投注自动任务
 * 
 * @author alan
 * @date 2017年8月17日 上午11:28:53
 */
@PersistJobDataAfterExecution
public class RobotBetTask implements Job {
	
	private Logger log = LoggerFactory.getLogger(RobotBetTask.class);
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		int count = 0;
		if(!jobDataMap.containsKey("count")) {
			jobDataMap.put("count", 0);
		} else {
			count = jobDataMap.getInt("count");
			count++;
			jobDataMap.put("count", count);
		}
		
		List<Integer> betSeconds = (List<Integer>) jobDataMap.get("betSeconds");
		if(betSeconds.contains(count)) {
			Robot robot = (Robot) jobDataMap.get("robot");
			ServerClient client = (ServerClient) jobDataMap.get("client");
			robot.bet(client);
		}
	}

}
