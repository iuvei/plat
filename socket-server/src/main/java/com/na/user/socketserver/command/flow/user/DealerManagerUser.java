package com.na.user.socketserver.command.flow.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.command.requestpara.LoginInfoPara;
import com.na.user.socketserver.command.sendpara.LoginInfoResponse;

@Component
public class DealerManagerUser extends User {
	
	private Logger log = LoggerFactory.getLogger(DealerManagerUser.class);

	@Override
	public void login(LoginInfoPara params, SocketIOClient client, LoginInfoResponse response) {
		
	}

	@Override
	public void reconnect(SocketIOClient client) {
		
	}

	@Override
	public void logout(CommandReqestPara commandReqestPara,
			SocketIOClient client) {
		
	}

	
}
