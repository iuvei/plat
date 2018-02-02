package com.na.baccarat.socketserver.command.send;

import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.ResponseCommandEnum;
import com.na.baccarat.socketserver.command.sendpara.SendDealerResponse;
import com.na.user.socketserver.command.sendpara.CommandResponse;
import com.na.user.socketserver.util.SocketUtil;

/**
 * 发出给荷官用户相关指令集合。
 * Created by sunny on 2017/4/28 0028.
 */
@Component
public class DealerCommand {

    /**
     * 发送荷官消息
     */
    public void sendMessage(SocketIOClient client, SendDealerResponse sendResponse) {
    	CommandResponse response = CommandResponse.createSuccess(RequestCommandEnum.SEND_DEALER_MESSAGE,sendResponse);
    	SocketUtil.send(client, ResponseCommandEnum.OK, response);
    }
    
}
