package com.na.baccarat.socketserver.command.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.command.flow.BaccaratClassHandler;
import com.na.baccarat.socketserver.command.flow.user.BaccaratDealerAction;
import com.na.baccarat.socketserver.command.requestpara.NewBootPara;
import com.na.baccarat.socketserver.command.sendpara.NewBootResponse;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.annotation.Cmd;

/**
 * 新一靴
 * 
 * @author alan
 * @date 2017年7月8日 上午11:40:20
 */
@Component
@Cmd(paraCls = NewBootPara.class, name = "新一靴")
public class NewBootCommand implements ICommand {
	
	@Autowired
	private BaccaratClassHandler baccaratClassHandler;

	@Override
	public boolean exec(SocketIOClient client,
			CommandReqestPara commandReqestPara) {
		NewBootPara newBootPara = (NewBootPara) commandReqestPara;
		
		NewBootResponse response = new NewBootResponse();
		
		((BaccaratDealerAction)baccaratClassHandler.baccaratAction(client)).newBoot(newBootPara, client, response);
		return true;
	}

}
