package com.na.baccarat.socketserver.command.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.command.flow.table.BeingMiTable;
import com.na.baccarat.socketserver.command.requestpara.MiCardOverPara;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.exception.SocketException;

/**
 * 咪牌结束(竞咪桌使用)
 * 
 * @author alan
 * @date 2017年5月5日 上午8:52:57
 */
@Cmd(paraCls = MiCardOverPara.class, name = "咪牌结束命令")
@Component
public class MiCardOverCommand implements ICommand {
	
	@Autowired
	private BeingMiTable beingMiTable;
	
	@Override
	public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
		MiCardOverPara params = (MiCardOverPara) commandReqestPara;
		if (params == null) {
			throw SocketException.createError("param.not.allow.empty");
		}
		
		beingMiTable.miCardOver(params, client);
		return true;
	}
	

}
