package com.na.user.socketserver.listener.redis;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.send.RoomCommand;
import com.na.baccarat.socketserver.command.sendpara.UserMessageResponse;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.common.enums.UserStatusEnum;
import com.na.user.socketserver.common.enums.UserTypeEnum;
import com.na.user.socketserver.entity.LiveUserPO;
import com.na.user.socketserver.entity.UserPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class AccountManageListener {
	
	@Autowired
    private SocketIOServer socketIOServer;
	
	@Autowired
	private RoomCommand roomCommand;
	
	public void onMessage(Object message){
		JSONObject redisMap = (JSONObject) message;
		Integer uid = (Integer) redisMap.get("uid");
		if(uid == null) return;
		
		Integer status = (Integer) redisMap.get("status");
		Integer isbet = (Integer) redisMap.get("isbet");

		if(status != null){
			if(status != UserStatusEnum.NORMAL.get() && status != UserStatusEnum.LOCKED.get()) return;
		}
		if(isbet != null){
			if(isbet != 1 && isbet != 2) return;
		}
		
		UserMessageResponse userMessageResponse = new UserMessageResponse();
		Collection<SocketIOClient> allClients = socketIOServer.getAllClients();
		if(status != null && isbet == null){
	        for (SocketIOClient client : allClients) {
	        	UserPO user = AppCache.getUserByClient(client);
	        	if(user != null && user.getUserTypeEnum() == UserTypeEnum.REAL){
	        		if(((LiveUserPO)user).getParentPath().contains("/"+uid+"/") || user.getId().longValue() == uid.intValue()){
						user.setUserStatus(status);
						if(UserStatusEnum.get(status) == UserStatusEnum.LOCKED)	userMessageResponse.setContent("您的账户已冻结!");
						if(UserStatusEnum.get(status) == UserStatusEnum.NORMAL)	userMessageResponse.setContent("您的账户已恢复!");
						if(userMessageResponse.getContent()!=null)	roomCommand.send(client, RequestCommandEnum.CLIENT_USER_MESSAGE, userMessageResponse);
					}

	        	}
			}
	        return;
		}
		
		if(status == null && isbet != null){
	        for (SocketIOClient client : allClients) {
	        	UserPO user = AppCache.getUserByClient(client);
	        	if(user != null && user.getUserTypeEnum() == UserTypeEnum.REAL ){
	        		if(((LiveUserPO)user).getParentPath().contains("/"+uid+"/") || user.getId().longValue() == uid.intValue()){
						((LiveUserPO)user).setIsBet(isbet);
						if(isbet == 2)	userMessageResponse.setContent("您的账户已禁止投注!");
						if(isbet == 1)	userMessageResponse.setContent("您的账户已恢复投注!");
						if(userMessageResponse.getContent()!=null)	roomCommand.send(client, RequestCommandEnum.CLIENT_USER_MESSAGE, userMessageResponse);
					}

	        	}
			}
		}
	}
}
