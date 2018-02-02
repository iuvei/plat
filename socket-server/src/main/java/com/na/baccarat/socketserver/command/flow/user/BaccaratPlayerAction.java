package com.na.baccarat.socketserver.command.flow.user;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.command.requestpara.BetPara;
import com.na.baccarat.socketserver.command.requestpara.QuickChangeRoomPara;
import com.na.baccarat.socketserver.command.requestpara.SelectRoomPara;
import com.na.baccarat.socketserver.command.sendpara.BetResponse;
import com.na.baccarat.socketserver.command.sendpara.SelectRoomResponse;
import com.na.user.socketserver.command.flow.game.GameAction;

/**
 * 百家乐用户动作
 * 
 * @author alan
 * @date 2017年8月1日 下午3:36:06
 */
public interface BaccaratPlayerAction extends GameAction {
	
	public void bet(BetPara params, SocketIOClient client, BetResponse response);
	
	public void quickChangTable(QuickChangeRoomPara params, SocketIOClient client);
	
	public void selectRoom(SelectRoomPara params, SocketIOClient client, SelectRoomResponse response);
	
}
