package com.na.baccarat.socketserver.command.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.command.flow.BaccaratClassHandler;
import com.na.baccarat.socketserver.command.flow.user.BaccaratDealerAction;
import com.na.baccarat.socketserver.command.requestpara.ClearCardPara;
import com.na.baccarat.socketserver.command.sendpara.ClearCardResponse;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.exception.SocketException;

/**
 * 清除牌
 * 
 * @author alan
 * @date 2017年4月27日 下午3:34:58
 */
@Cmd(name="清牌",paraCls=ClearCardPara.class)
@Component
public class ClearCardCommand implements ICommand {
	
	private Logger log = LoggerFactory.getLogger(ClearCardCommand.class);
	
	@Autowired
	private BaccaratClassHandler baccaratClassHandler;
	
	@Override
	public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
		ClearCardPara params = (ClearCardPara) commandReqestPara;
		
		if (params == null) {
			throw SocketException.createError("param.not.allow.empty");
		}
		
		if(StringUtils.isEmpty(params.getPassword())) {
			throw SocketException.createError("param.not.allow.empty");
		}
		
		((BaccaratDealerAction)baccaratClassHandler.baccaratAction(client)).clearCard(params, client, new ClearCardResponse());
		return true;
	}

}