package com.na.baccarat.socketserver.command.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.flow.BaccaratClassHandler;
import com.na.baccarat.socketserver.command.flow.user.BaccaratDealerAction;
import com.na.baccarat.socketserver.command.requestpara.SetCardPara;
import com.na.baccarat.socketserver.command.send.RoomCommand;
import com.na.baccarat.socketserver.command.sendpara.SetCardResponse;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.exception.SocketException;

/**
 * 发牌
 * 
 * @author alan
 * @date 2017年4月27日 下午3:34:58
 */
@Cmd(name="发牌",paraCls=SetCardPara.class)
@Component
public class SetCardCommand implements ICommand {
	
	private Logger log = LoggerFactory.getLogger(SetCardCommand.class);
	
	@Autowired
	private RoomCommand roomCommand;
	
	@Autowired
	private BaccaratClassHandler baccaratClassHandler;
	
	@Override
	public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
		SetCardPara params = (SetCardPara) commandReqestPara;
		
		if (params == null) {
			throw SocketException.createError("param.not.allow.empty");
		}
		
		SetCardResponse setCardResponse = new SetCardResponse();
		setCardResponse.setIndex(params.getIndex());
		
		((BaccaratDealerAction)baccaratClassHandler.baccaratAction(client)).setCard(params, client, setCardResponse);
		
		roomCommand.send(client, RequestCommandEnum.DEALER_SET_CARD, setCardResponse);
		return true;
	}

}