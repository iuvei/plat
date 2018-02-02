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
import com.na.baccarat.socketserver.listener.event.MultiDisConnectionEvent;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.request.LogoutCommand;
import com.na.user.socketserver.common.event.DisConnectionEvent;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.service.IBetOrderService;

@Component
public class ClearUesrDataTask implements Job {
	
	private Logger log = LoggerFactory.getLogger(ClearUesrDataTask.class);
	
	@Autowired
	private LogoutCommand logoutCommand;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private IBetOrderService betOrderService;
	
	/**
	 * 清除用户数据
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		AppCache.getDisConnectClientMap().forEach( (key,value) -> {
			UserPO userPO = AppCache.getUserByClient(value);
			if(userPO != null) {
				//判断是否有未结算订单
				Integer unSettleCount = betOrderService.findUnSettleCount(userPO.getId());
				if(unSettleCount > 0) {
					log.debug("【任务】清除离线用户数据 : [" + userPO.getId() + "] " + userPO.getLoginName() + "有未结算订单 " + unSettleCount + "笔, 无法清除用户数据.");
					return ;
				}
				
				log.debug("【任务】清除离线用户数据 : [" + userPO.getId() + "] " + userPO.getLoginName() + ",InTable：" + userPO.isInTable() + ",Table:" + userPO.getTableId());
				if(userPO.isInTable()) {
					if(userPO.getTableId() != null) {
						applicationContext.publishEvent(new DisConnectionEvent(userPO.getTableId()));
					} else {
						//多台断线重连清除机制
						applicationContext.publishEvent(new MultiDisConnectionEvent(userPO.getId()));
					}
				} else {
					SocketIOClient client = AppCache.removeDisConnectClient(key);
					AppCache.removeDisConnectUser(key);
					logoutCommand.exec(client, null);
				}
				
			}
		});
	}

}
