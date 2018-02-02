package com.na.user.socketserver.command.send;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.ResponseCommandEnum;
import com.na.baccarat.socketserver.command.sendpara.UserAnnounceResponse;
import com.na.user.socketserver.command.request.LogoutCommand;
import com.na.user.socketserver.command.sendpara.CommandResponse;
import com.na.user.socketserver.command.sendpara.IResponse;
import com.na.user.socketserver.command.sendpara.LoginInfoResponse;
import com.na.user.socketserver.util.SocketUtil;

/**
 * 与登录相关操作指令。
 * Created by sunny on 2017/4/27 0027.
 */
@Component
public class UserCommand {
    @Autowired
    private SocketIOServer socketIOServer;
    @Autowired
	private LogoutCommand logoutCommand;
    
    /**
	 * 发送单个消息
	 * @param client
	 * @param joinRoomResponse
	 */
    public void send(SocketIOClient client,RequestCommandEnum commandEnum, IResponse roomResponse){
        CommandResponse response = CommandResponse.createSuccess(commandEnum,roomResponse);
        SocketUtil.send(client, ResponseCommandEnum.OK,response);
    }

    /**
     * 登录成功响应。
     * @param loginInfoResponse
     */
    public void loginSuceess(SocketIOClient client,LoginInfoResponse loginInfoResponse){
        CommandResponse response = CommandResponse.createSuccess(RequestCommandEnum.COMMON_LOGIN,loginInfoResponse);
        SocketUtil.send(client, ResponseCommandEnum.OK,response);
    }

    /**
     * 退出成功响应。
     */
    public void logoutSuccess(SocketIOClient client){
        CommandResponse response = CommandResponse.createSuccess(RequestCommandEnum.COMMON_LOGOUT,null);
        SocketUtil.send(client, ResponseCommandEnum.OK, response);
    }

    /**
     * 用户离开房间。
     * @param levelTableResponse
     */
    /*public void levelRoom(LevelTableResponse levelTableResponse){
        CommandResponse response = CommandResponse.createSuccess(RequestCommandEnum.SERVER_LEVEL_TABLE,levelTableResponse);

        Collection<SocketIOClient> clients = SocketUtil.getTableClientList(socketIOServer,levelTableResponse.getTableId());
        clients.forEach(item->{
            SocketUtil.send(item,ResponseCommandEnum.OK,response);
        });
    }*/
    
    /**
     * 向用户发送信息
     * @param client
     */
	public void sendAllTableStatusToClient(SocketIOClient client, Map<String,Object> responseMap) {
		 CommandResponse response = CommandResponse.createSuccess(RequestCommandEnum.SERVER_ALL_TABLE_STATUS,responseMap);
	     SocketUtil.send(client, ResponseCommandEnum.OK,response);
	}
	
	/**
     * 向用户发送公告信息
     * @param client
     */
	public void sendUserAnnounce(SocketIOClient client, UserAnnounceResponse userAnnounceResponse) {
		 CommandResponse response = CommandResponse.createSuccess(RequestCommandEnum.COMMON_USER_ANNOUNCE,userAnnounceResponse);
	     SocketUtil.send(client, ResponseCommandEnum.OK,response);
	}
	
}
