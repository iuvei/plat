package com.na.baccarat.socketserver.command.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.command.flow.BaccaratClassHandler;
import com.na.baccarat.socketserver.command.flow.user.BaccaratDealerAction;
import com.na.baccarat.socketserver.command.requestpara.CutCardPara;
import com.na.baccarat.socketserver.command.sendpara.CutCardResponse;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.exception.SocketException;

/**
 * 切牌
 * 
 * @author alan
 * @date 2017年4月27日 下午3:34:58
 */
@Cmd(name="发牌",paraCls=CutCardPara.class)
@Component
public class CutCardCommand implements ICommand {
	
	private Logger log = LoggerFactory.getLogger(CutCardCommand.class);
	
	@Autowired
	private BaccaratClassHandler baccaratClassHandler;
	
	@Override
	public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
		CutCardPara params = (CutCardPara) commandReqestPara;
		
		if (params == null) {
			throw SocketException.createError("param.not.allow.empty");
		}
		
		((BaccaratDealerAction)baccaratClassHandler.baccaratAction(client)).cutCard(params, client, new CutCardResponse());
		return true;
	}

}