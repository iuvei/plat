package com.na.user.socketserver.command.flow.game;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.command.requestpara.JoinRoomPara;
import com.na.baccarat.socketserver.command.requestpara.LeaveRoomPara;
import com.na.baccarat.socketserver.command.sendpara.JoinRoomResponse;
import com.na.baccarat.socketserver.command.sendpara.LeaveRoomResponse;

public interface GameAction {
	
	public void join(JoinRoomPara params, SocketIOClient client, JoinRoomResponse response);
	
	public void leave(LeaveRoomPara params, SocketIOClient client, LeaveRoomResponse response);

}
