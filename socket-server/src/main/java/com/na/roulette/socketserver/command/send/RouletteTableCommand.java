package com.na.roulette.socketserver.command.send;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.command.ResponseCommandEnum;
import com.na.baccarat.socketserver.common.enums.RoundStatusEnum;
import com.na.roulette.socketserver.cache.RouletteCache;
import com.na.roulette.socketserver.command.RouletteRequestCommandEnum;
import com.na.roulette.socketserver.command.sendpara.RouletteJoinRoomResponse;
import com.na.roulette.socketserver.command.sendpara.RouletteLeaveTableResponse;
import com.na.roulette.socketserver.command.sendpara.RouletteTableStatusResponse;
import com.na.roulette.socketserver.common.enums.RouletteGameTableInstantStateEnum;
import com.na.roulette.socketserver.entity.RouletteGameTable;
import com.na.roulette.socketserver.entity.RouletteUser;
import com.na.user.socketserver.command.sendpara.CommandResponse;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.util.SocketUtil;

/**
 * 轮盘桌子相关命令
 * 
 * @date 2017年5月11日 上午10:32:46
 */
@Component
public class RouletteTableCommand {
	
	@Autowired
    private SocketIOServer socketIOServer;
	
	/**
	 * 发送单个消息
	 * @param client
	 * @param roomResponse
	 */
    public void send(SocketIOClient client,RouletteRequestCommandEnum commandEnum, CommandResponse roomResponse){
        CommandResponse response = CommandResponse.createSuccess(commandEnum,roomResponse);
        SocketUtil.send(client, ResponseCommandEnum.OK,response);
    }
	
	/**
	 * 加入成功
	 * @param client
	 * @param joinRoomResponse
	 */
    public void join(SocketIOClient client,RouletteJoinRoomResponse joinRoomResponse){
        CommandResponse response = CommandResponse.createSuccess(RouletteRequestCommandEnum.COMMON_JOIN_TABLE,joinRoomResponse);
        SocketUtil.send(client, ResponseCommandEnum.OK,response);
    }
	
	/**
	 * 发送给该实体桌其他用户
	 * @param client
	 * @param commandResponse
	 */
	public void sendAllTableOtherClient(SocketIOClient client, CommandResponse commandResponse, RouletteRequestCommandEnum command, Integer tableId){
		CommandResponse response = CommandResponse.createSuccess(command,commandResponse);
        Collection<SocketIOClient> clients = null;
    	clients = SocketUtil.getTableOtherClientList(socketIOServer, tableId, client);
    	if(clients != null) {
    		clients.forEach( item -> {
            	SocketUtil.send(item, ResponseCommandEnum.OK,response);
            });
    	}
    }
	 
    /**
     * 发送给该实体桌所有用户
     * @param commandResponse
     * @param command
     */
    public void sendAllTablePlayer(CommandResponse commandResponse, RouletteRequestCommandEnum command, Integer tableId){
        CommandResponse response = CommandResponse.createSuccess(command,commandResponse);
        Collection<SocketIOClient> clients = null;
    	clients = SocketUtil.getTableClientList(socketIOServer, tableId);
    	
        clients.forEach( item -> {
        	SocketUtil.send(item, ResponseCommandEnum.OK,response);
        });
    }
    
    /**
     * 发送给该实体桌所有用户
     * @param command
	 * @param tableId
     */
    public void sendAllTablePlayer(RouletteRequestCommandEnum command,Integer tableId){
        CommandResponse response = CommandResponse.createSuccess(command,new Object());
        Collection<SocketIOClient> clients = null;
    	clients = SocketUtil.getTableClientList(socketIOServer, tableId);
    	
        clients.forEach( item -> {
        	SocketUtil.send(item, ResponseCommandEnum.OK,response);
        });
    }
    
    /**
     * 发送给客户端桌子状态
     * @param client
     */
	public void sendTableStatus(SocketIOClient client) {
		RouletteUser user = RouletteCache.getUserByClient(client);
		if(null == user || null == user.getUserPO().getTableId()) {
			throw SocketException.createError("user.is.empty");
		}
		RouletteGameTable gameTable = RouletteCache.getGameTableById(user.getUserPO().getTableId());
		if(null == gameTable) {
			throw SocketException.createError("table.is.empty");
		}
		
		RouletteTableStatusResponse tableStatusResponse = new RouletteTableStatusResponse();
		tableStatusResponse.setDealerName(gameTable.getDealer() == null ? null : gameTable.getDealer().getUserPO().getNickName());
		if(gameTable.getRound() != null && gameTable.getRound().getRoundPO() != null){
			tableStatusResponse.setTableState(gameTable.getRound().getRoundPO().getStatus());
			tableStatusResponse.setTableBid(gameTable.getRound().getRoundPO().getBootId());
			tableStatusResponse.setTableGid(gameTable.getRound().getRoundPO().getGameId());
			tableStatusResponse.setTableNum(gameTable.getRound().getRoundPO().getRoundNumber());
			tableStatusResponse.setTableRid(gameTable.getRound().getRoundPO().getId());
			tableStatusResponse.setTableChips(gameTable.getGameTablePO().getMin() + "--" + gameTable.getGameTablePO().getMax());
			tableStatusResponse.setShowTableStatus(gameTable.getShowStatus());
		}
		if(gameTable.getRounds() != null && gameTable.getRounds().size() > 0){
			gameTable.getRounds().forEach(item ->{
				tableStatusResponse.getResults().add(item.getRoundPO().getResult());	
			});
		}
		tableStatusResponse.setTableName(gameTable.getGameTablePO().getName());
		tableStatusResponse.setTableTid(gameTable.getGameTablePO().getId());
		tableStatusResponse.setInstantState(gameTable.getInstantState() == null ? RouletteGameTableInstantStateEnum.CLOSED.get():gameTable.getInstantState());
		long countDownSeconds = 0L;
		if(gameTable.getCoundDownDate() != null && gameTable.getRound().getRoundPO().getStatusEnum() == RoundStatusEnum.BETTING ){
			countDownSeconds = gameTable.getGameTablePO().getCountDownSeconds() - (System.currentTimeMillis() - gameTable.getCoundDownDate().getTime()) / 1000;
			if(countDownSeconds < 0) {
				countDownSeconds = 0L;
			}
		}
		tableStatusResponse.setTableTime(countDownSeconds);

		CommandResponse response = CommandResponse.createSuccess(RouletteRequestCommandEnum.DEALER_TABLESTATUS,tableStatusResponse);
		SocketUtil.send(client, ResponseCommandEnum.OK,response);
	}
	
	
	/**
	 * 离开房间
	 * @param client
	 * @param leaveTableResponse
	 */
    public void leave(SocketIOClient client,RouletteLeaveTableResponse leaveTableResponse){
        CommandResponse response = CommandResponse.createSuccess(RouletteRequestCommandEnum.COMMON_LEAVE_TABLE,leaveTableResponse);
        SocketUtil.send(client, ResponseCommandEnum.OK,response);
    }
    
    
    
}
