package com.na.user.socketserver.command.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.command.requestpara.HeartBeatPara;
import com.na.user.socketserver.command.send.UserCommand;
import com.na.user.socketserver.command.sendpara.HeartBeatResponse;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.exception.SocketException;

/**
 * 心跳指令
 */
@Component
@Cmd(paraCls = HeartBeatPara.class, name = "心跳")
public class HeartBeatCommand implements ICommand {
	
	@Autowired
	private UserCommand userCommand;
	
	@Override
	public boolean exec(SocketIOClient client,
			CommandReqestPara commandReqestPara) {
		
		HeartBeatPara para = (HeartBeatPara) commandReqestPara;
		if (client == null)
			throw SocketException.createError("login.again");
		
		HeartBeatResponse response = new HeartBeatResponse();
		response.setEndTimeStamp(System.currentTimeMillis() - para.getStartTimeStamp());
//		response.setSn(para.getSn());
		response.setStartTimeStamp(para.getStartTimeStamp());
//		response.setData(para.getData());
		userCommand.send(client, RequestCommandEnum.HEART_BEAT, response);
		return true;
	}

}
