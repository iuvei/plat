package com.na.user.socketserver.command;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;

/**
 * 命令接口
 */
public interface ICommand {

	/**
	 * 执行命令。
	 * @param client
	 * @param commandReqestPara
	 * @return
	 */
	public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara);

}
