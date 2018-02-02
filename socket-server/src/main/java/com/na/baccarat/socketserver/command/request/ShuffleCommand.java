package com.na.baccarat.socketserver.command.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.command.flow.BaccaratClassHandler;
import com.na.baccarat.socketserver.command.flow.user.BaccaratDealerAction;
import com.na.baccarat.socketserver.command.requestpara.ShufflePara;
import com.na.baccarat.socketserver.command.sendpara.ShuffleResponse;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.exception.SocketException;

/**
 * 洗牌
 * 
 * @author alan
 * @date 2017年4月27日 下午3:34:58
 */
@Cmd(name="洗牌",paraCls=ShufflePara.class)
@Component
public class ShuffleCommand implements ICommand {
	
	private Logger log = LoggerFactory.getLogger(ShuffleCommand.class);
	
	@Autowired
	private BaccaratClassHandler baccaratClassHandler;
	
	@Override
	public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
		ShufflePara params = (ShufflePara) commandReqestPara;
		ShuffleResponse response = new ShuffleResponse();
		if (params == null) {
			throw SocketException.createError("param.not.allow.empty");
		}
		
		((BaccaratDealerAction)baccaratClassHandler.baccaratAction(client)).shuffle(params, client, response);
		return true;
	}
	
	

}