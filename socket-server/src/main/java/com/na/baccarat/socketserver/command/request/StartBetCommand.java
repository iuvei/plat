package com.na.baccarat.socketserver.command.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.command.flow.BaccaratClassHandler;
import com.na.baccarat.socketserver.command.flow.user.BaccaratDealerAction;
import com.na.baccarat.socketserver.command.sendpara.StartBetResponse;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.annotation.Cmd;


/**
 * 开始下注
 * 
 * @author alan
 * @date 2017年5月2日 下午12:21:23
 */
@Cmd(name="开始下注",paraCls=CommandReqestPara.class)
@Component
public class StartBetCommand implements ICommand {

	@Autowired
	private BaccaratClassHandler baccaratClassHandler;

	@Override
	public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
		
		StartBetResponse response = new StartBetResponse();
		
		((BaccaratDealerAction)baccaratClassHandler.baccaratAction(client)).startBet(commandReqestPara, client, response);
		return true;
	}
}
