package com.na.roulette.socketserver.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.roulette.socketserver.cache.RouletteCache;
import com.na.roulette.socketserver.command.request.RouletteJoinTableCommand;
import com.na.roulette.socketserver.command.request.RouletteLeaveTableCommand;
import com.na.roulette.socketserver.command.send.RouletteTableCommand;
import com.na.roulette.socketserver.command.sendpara.RouletteJoinRoomResponse;
import com.na.roulette.socketserver.entity.RouletteGameTable;
import com.na.roulette.socketserver.entity.RouletteUser;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.common.enums.GameEnum;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.common.event.ReconnectEvent;
import com.na.user.socketserver.entity.UserPO;

/**
 * 重连监听器
 * 
 * @author alan
 * @date 2017年5月24日 下午12:03:21
 */
@Component("rouletteReconnectEventListener")
public class ReconnectEventListener implements ApplicationListener<ReconnectEvent>{
	
    private final static Logger log = LoggerFactory.getLogger(ReconnectEventListener.class);

    @Autowired
    private SocketIOServer socketIOServer;
    
    @Autowired
    private RouletteJoinTableCommand rouletteJoinTableCommand;
    
    @Autowired
	private RouletteLeaveTableCommand rouletteLeaveTableCommand;
    
    @Autowired
	private RouletteTableCommand rouletteTableCommand;

    @Override
    public void onApplicationEvent(ReconnectEvent reconnectEvent) {
    	SocketIOClient client = reconnectEvent.getSource();
        if(client == null) {
        	log.error("客户端已经断开");
            return ;
        }
        
        if(GameEnum.ROULETTE != GameEnum.get(client.get(SocketClientStoreEnum.GAME_CODE.get()))) {
            return ;
        }
    	
        UserPO userPO = AppCache.getUserByClient(client);
        if(userPO == null){
        	log.error("没找到用户");
            return ;
        }
        
        //用户掉线重连机制
	    RouletteUser leaveSeatUser = RouletteCache.getLoginUser(userPO.getId());
		if (leaveSeatUser!=null && leaveSeatUser.getUserPO().isInTable()) {
			UserPO leaveSeatUserPO = leaveSeatUser.getUserPO();
			if(leaveSeatUserPO == null || leaveSeatUserPO.getTableId() == null){
				return;
			}
			RouletteGameTable gameTable = RouletteCache.getGameTableById(leaveSeatUserPO.getTableId());
			RouletteJoinRoomResponse response = new RouletteJoinRoomResponse();
			response.setNickName(leaveSeatUserPO.getNickName());
			response.setTableId(leaveSeatUserPO.getTableId());
			response.setChipId(leaveSeatUser.getChipId());
			rouletteJoinTableCommand.getResponseData(response, gameTable, leaveSeatUser.getTradeItemBetMoneyMap());
			
			rouletteTableCommand.join(client, response);
			rouletteTableCommand.sendTableStatus(client);
		}

    }
}
