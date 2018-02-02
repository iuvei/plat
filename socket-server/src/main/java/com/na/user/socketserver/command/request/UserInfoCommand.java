package com.na.user.socketserver.command.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.command.send.UserCommand;
import com.na.user.socketserver.command.sendpara.UserInfoResponse;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.entity.UserPO;

/**
 * 获取个人资料
 */
@Cmd(paraCls = CommandReqestPara.class,name = "获取个人资料")
@Component
public class UserInfoCommand implements ICommand {
	
	private Logger log = LoggerFactory.getLogger(UserInfoCommand.class);
	
	@Autowired
	private UserCommand userCommand;
	
	@Override
	public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
		
		UserPO userPO = AppCache.getUserByClient(client);
		
		UserInfoResponse response = new UserInfoResponse();
		response.setNickName(userPO.getNickName());
		response.setBlance(userPO.getBalance());
		
		userCommand.send(client, RequestCommandEnum.CLIENT_USER_INFO, response);
		return true;
	}
	
}
