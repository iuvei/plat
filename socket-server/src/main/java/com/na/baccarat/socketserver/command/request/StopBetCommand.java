package com.na.baccarat.socketserver.command.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.command.flow.BaccaratClassHandler;
import com.na.baccarat.socketserver.command.flow.user.BaccaratDealerAction;
import com.na.baccarat.socketserver.command.sendpara.StopBetResponse;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.annotation.Cmd;

/**
 * 停止下注
 * 
 * @author Administrator
 *
 */
@Cmd(paraCls = CommandReqestPara.class, name = "停止下注指令")
@Component
public class StopBetCommand implements ICommand {

	private Logger log = LoggerFactory.getLogger(StopBetCommand.class);
	
	@Autowired
	private BaccaratClassHandler baccaratClassHandler;

	@Override
	public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
		StopBetResponse response = new StopBetResponse();
		
		((BaccaratDealerAction)baccaratClassHandler.baccaratAction(client)).stopBet(commandReqestPara, client, response);
		return true;
	}

}
