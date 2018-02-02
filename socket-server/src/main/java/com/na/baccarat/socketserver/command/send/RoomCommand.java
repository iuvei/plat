package com.na.baccarat.socketserver.command.send;

import java.util.Collection;

import com.alibaba.fastjson.JSONObject;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.ResponseCommandEnum;
import com.na.baccarat.socketserver.entity.User;
import com.na.user.socketserver.command.sendpara.CommandResponse;
import com.na.user.socketserver.command.sendpara.IResponse;
import com.na.user.socketserver.util.SocketUtil;

/**
 * 加入房间相关命令
 * 
 * @author alan
 * @date 2017年4月28日 下午5:45:19
 */
@Component
public class RoomCommand {
    private Logger log = LoggerFactory.getLogger(RoomCommand.class);
	@Autowired
    private SocketIOServer socketIOServer;

	/**
	 * 给一个用户集合发送消息
	 * @param userCollection
	 * @param requestEnum
	 * @param response
	 */
    public void send(Collection<User> userCollection, RequestCommandEnum requestEnum, IResponse response) {
        CommandResponse res = CommandResponse.createSuccess(requestEnum, response);
        Collection<SocketIOClient> clients = SocketUtil.getClientByUserList(socketIOServer, userCollection);
        if(log.isInfoEnabled()) {
            StringBuilder userIds = new StringBuilder();
            clients.forEach(item -> {
                userIds.append(item.get(SocketClientStoreEnum.USER_ID.get()) + ",");
            });
            log.info("【批量发送】：命令：{},内容：{},接收方为：{}", res.getType(), JSONObject.toJSONString(res), userIds);
        }

        clients.stream().forEach(item->{
            SocketUtil.send(item,ResponseCommandEnum.OK,res);
        });
    }
    
    /**
	 * 发送单个消息
	 * @param client
	 * @param joinRoomResponse
	 */
    public void send(SocketIOClient client,RequestCommandEnum commandEnum, IResponse iresponse){
        CommandResponse response = CommandResponse.createSuccess(commandEnum,iresponse);

        SocketUtil.send(client, ResponseCommandEnum.OK,response);
    }
    
    /**
     * 发送给该虚拟桌其他用户
     * @param client
     * @param joinRoomResponse
     */
    public void sendOtherUser(SocketIOClient client,IResponse iResponse, Integer virtualTableId,RequestCommandEnum requsetEnum){
        CommandResponse response = CommandResponse.createSuccess(requsetEnum, iResponse);
        Collection<SocketIOClient> clients = SocketUtil.getOtherVirtualTableClients(socketIOServer, client, virtualTableId);
        if(clients != null) {
        	clients.forEach( item -> {
            	SocketUtil.send(item, ResponseCommandEnum.OK,response);
            });
        }
    }
}
