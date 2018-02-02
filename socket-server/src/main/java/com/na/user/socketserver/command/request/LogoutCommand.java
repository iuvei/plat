package com.na.user.socketserver.command.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.flow.UserClassHandler;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.SocketException;

/**
 * 退出指令。 Created by sunny on 2017/4/27 0027.
 */
@Component
@Cmd(paraCls = CommandReqestPara.class, name = "用户退出")
public class LogoutCommand implements ICommand {
	
	@Autowired
	private UserClassHandler userClassHandler;

	@Override
	public boolean exec(SocketIOClient client,
			CommandReqestPara commandReqestPara) {
		if (client == null)
			throw SocketException.createError("login.again");
		
		UserPO userPO = AppCache.getUserByClient(client);
		userClassHandler.user(userPO).logout(commandReqestPara, client);
		
		return true;
	}

}
