package com.na.user.socketserver.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.user.socketserver.common.event.ChangeKeyEvent;
import com.na.user.socketserver.constant.Constant;
import com.na.user.socketserver.util.ConvertUtil;

@Component
public class ChangeKeyMoitorTask implements Job {
	
	private Logger log = LoggerFactory.getLogger(ChangeKeyMoitorTask.class);
	
	@Autowired
	private SocketIOServer socketIOServer;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	/**
	 * 观察现场  监听客户端是否需要更换秘钥
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		//开启线程池 执行更换秘钥
		for(SocketIOClient client : socketIOServer.getAllClients()) {
			if (client.has(Constant.CHANGE_KEY_TIMETSTAMP)) {
				long oldStamp = ConvertUtil.toLong(client.get(Constant.CHANGE_KEY_TIMETSTAMP));
				if(System.currentTimeMillis() > oldStamp) {
					applicationContext.publishEvent(new ChangeKeyEvent(client));
				} else {
					continue;
				}
			}
		}
	}

}
