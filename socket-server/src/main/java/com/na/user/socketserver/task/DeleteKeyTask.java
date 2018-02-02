package com.na.user.socketserver.task;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.user.socketserver.constant.Constant;

/**
 * 咪牌倒计时任务
 * 
 * @author alan
 * @date 2017年5月5日 下午3:12:04
 */
@Component
public class DeleteKeyTask implements Job {
	
	private Logger log = LoggerFactory.getLogger(DeleteKeyTask.class);
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		JobDataMap dataMap = arg0.getJobDetail().getJobDataMap();
		
		SocketIOClient client = (SocketIOClient) dataMap.get("client");
		client.del(Constant.SECRET_OLD_KEY);
		log.debug("删除旧秘钥");
	}

}
