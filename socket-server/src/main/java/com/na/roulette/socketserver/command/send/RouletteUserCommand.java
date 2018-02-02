package com.na.roulette.socketserver.command.send;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.command.ResponseCommandEnum;
import com.na.roulette.socketserver.command.RouletteRequestCommandEnum;
import com.na.roulette.socketserver.command.sendpara.RouletteLevelTableResponse;
import com.na.user.socketserver.command.sendpara.CommandResponse;
import com.na.user.socketserver.util.SocketUtil;

/**
 * 发出给荷官用户相关指令集合。 Created by sunny on 2017/4/28 0028.
 */
@Component
public class RouletteUserCommand {

	@Autowired
	private SocketIOServer socketIOServer;

	/**
     * 荷官离开房间。
     * @param levelTableResponse
     */
    public void dealerLevelRoom(RouletteLevelTableResponse levelTableResponse){
        CommandResponse response = CommandResponse.createSuccess(RouletteRequestCommandEnum.DEALER_LEAVE,levelTableResponse);

        Collection<SocketIOClient> clients = SocketUtil.getTableClientList(socketIOServer,levelTableResponse.getTableId());
        clients.forEach(item->{
            SocketUtil.send(item,ResponseCommandEnum.OK,response);
        });
    }

}
