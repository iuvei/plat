package com.na.baccarat.socketserver.command.send;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.ResponseCommandEnum;
import com.na.baccarat.socketserver.command.requestpara.Card;
import com.na.baccarat.socketserver.command.sendpara.TableStatusResponse;
import com.na.baccarat.socketserver.common.enums.RoundStatusEnum;
import com.na.baccarat.socketserver.entity.BeingMiGameTable;
import com.na.baccarat.socketserver.entity.GameTable;
import com.na.baccarat.socketserver.entity.User;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.sendpara.CommandResponse;
import com.na.user.socketserver.command.sendpara.IResponse;
import com.na.user.socketserver.command.sendpara.SelectTableResponse;
import com.na.user.socketserver.common.enums.UserTypeEnum;
import com.na.user.socketserver.entity.GameTablePO;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.util.SocketUtil;
import com.na.user.socketserver.util.TimeLeft;

/**
 * 实体桌相关命令
 * 
 * @author alan
 * @date 2017年4月28日 下午5:45:19
 */
@Component
public class TableCommand {
	private static final Logger log = LoggerFactory.getLogger(TableCommand.class);
	
	@Autowired
    private SocketIOServer socketIOServer;
	
	/**
	 * 发送给该竞咪桌的其他用户
	 * @param commandResponse
	 * @param command
	 */
	 public void sendOtherUserAtBeingTable(SocketIOClient client,IResponse iResponse, BeingMiGameTable table,RequestCommandEnum requsetEnum){
	 	try {
			CommandResponse response = CommandResponse.createSuccess(requsetEnum, iResponse);
			Collection<User> userList = BaccaratCache.getUserList(table.getBesideUser());
			userList.addAll(BaccaratCache.getUserList(table.getPlayers().values()));

			Collection<SocketIOClient> clients = SocketUtil.getClientByUserList(socketIOServer, userList);
			if (clients != null) {
				clients.forEach(item -> {
					if (item != client) {
						SocketUtil.send(item, ResponseCommandEnum.OK, response);
					}
				});
			}
		}catch (Exception e){
	 		log.error(e.getMessage(),e);
		}
    }
	
	/**
	 * 发送 给所有客户端用户
	 * @param commandResponse
	 * @param command
	 */
	 public void sendAllClientPlayer(IResponse commandResponse, RequestCommandEnum command){
	 	try {
			CommandResponse response = CommandResponse.createSuccess(command, commandResponse);
			Collection<SocketIOClient> clients = socketIOServer.getAllClients();
			clients.forEach(item -> {
				UserPO user = AppCache.getUserByClient(item);
				if (null == user)
					return;
				SocketUtil.send(item, ResponseCommandEnum.OK, response);
			});
		}catch (Exception e){
	 		log.error(e.getMessage(),e);
		}
    }
	 
	
	/**
	 * 发送给该实体桌其他用户
	 * @param client
	 * @param joinRoomResponse
	 */
	public void sendAllTableOtherClient(SocketIOClient client, IResponse commandResponse, RequestCommandEnum command, Integer tableId){
		try {
			CommandResponse response = CommandResponse.createSuccess(command, commandResponse);
			Collection<SocketIOClient> clients = null;
			clients = SocketUtil.getTableOtherClientList(socketIOServer, tableId, client);
			if (clients != null) {
				clients.forEach(item -> {
					SocketUtil.send(item, ResponseCommandEnum.OK, response);
				});
			}
		}catch (Exception e){
			log.error(e.getMessage(),e);
		}
    }
	 
    /**
     * 发送给该实体桌所有用户
     * @param commandResponse
     * @param command
     */
    public void sendAllTablePlayer(IResponse commandResponse, RequestCommandEnum command, Integer tableId){
    	try {
			TimeLeft left = new TimeLeft();
			CommandResponse response = CommandResponse.createSuccess(command, commandResponse);
			Collection<SocketIOClient> clients = SocketUtil.getTableClientList(socketIOServer, tableId);

			clients.forEach(item -> {
				SocketUtil.send(item, ResponseCommandEnum.OK, response);
			});
			log.info("【sendAllTablePlayer】给{}用户发送消息花费时间：{}",clients.size(),left.end());
		}catch (Exception e){
    		log.error(e.getMessage(),e);
		}
    }
    
    /**
     * 发送给客户端桌子状态
     * @param client
     */
	public void sendTableStatus(SocketIOClient client) {
		User user = BaccaratCache.getUserByClient(client);
		if(null == user || null == user.getUserPO().getTableId())
			throw SocketException.createError("user.not.exist");
		
		sendTableStatus(client, user.getUserPO().getTableId());
	}
	
	/**
     * 发送多台桌子状态
     * @param client
     */
	public void sendTableStatus(SocketIOClient client, Integer tableId) {
		GameTable gameTable = BaccaratCache.getGameTableById(tableId);
		UserPO userPO = AppCache.getUserByClient(client);
		
		if(null == gameTable)
			throw SocketException.createError("table.not.exist");
		GameTablePO gameTablePO = gameTable.getGameTablePO();
		
		TableStatusResponse tableStatusResponse = new TableStatusResponse();
		tableStatusResponse.setDealerName(gameTable.getDealer() == null? null:gameTable.getDealer().getUserPO().getNickName());
		tableStatusResponse.setPlayerNumber(BaccaratCache.getTableUserList(tableId).size());
		String tableChips = BigDecimal.ZERO.compareTo(gameTablePO.getMin())==0
				? NumberFormat.getNumberInstance().format(gameTablePO.getMax())
				: NumberFormat.getNumberInstance().format(gameTablePO.getMin()) + "--" + NumberFormat.getNumberInstance().format(gameTablePO.getMax());
		tableStatusResponse.setTableChips(tableChips);
		if(gameTable.getRounds() != null){
			List<String> rounds = new ArrayList<>();
			gameTable.getRounds().forEach(item ->{
				if(null != item) {
					rounds.add(item.getRoundPO().getResult());
				}
			});
			tableStatusResponse.setRds(rounds);
		}
		if(gameTable.getRound() != null){
			tableStatusResponse.setShowTableStatus(gameTable.getShowStatus());
			tableStatusResponse.setTableBid(gameTable.getRound().getRoundPO().getBootId());
			tableStatusResponse.setTableGid(gameTable.getRound().getRoundPO().getGameId());
			tableStatusResponse.setTableNum(gameTable.getRound().getRoundPO().getRoundNumber());
			tableStatusResponse.setTableRid(gameTable.getRound().getRoundPO().getId());
			tableStatusResponse.setTableStatus(gameTable.getRound().getRoundPO().getStatus());
			tableStatusResponse.setTableType(gameTable.getGameTablePO().getType());
			
			if(gameTable.getRound().getRoundPO() != null && gameTable.getRound().getRoundPO().getStatusEnum() == RoundStatusEnum.AWAITING_RESULT){
				List<TableStatusResponse.CardInfo> cardList = new ArrayList<>(4);
				TableStatusResponse.CardInfo cardInfo;
				
				if(gameTable.getCards() != null) {
					for(int i = 0; i < gameTable.getCards().length ; i++) {
						Card card = gameTable.getCards()[i];
						if(card != null) {
							cardInfo = tableStatusResponse.new CardInfo();
							if(userPO.getUserTypeEnum()== UserTypeEnum.DEALER){
								cardInfo.number = card.getCardNum();
								cardInfo.type = card.getCardType();
								cardInfo.openStatus = card.getOpenStatus();
							} else {
								if (card.getOpenStatus()) {
									cardInfo.number = card.getCardNum();
									cardInfo.type = card.getCardType();
								}
							}
							cardInfo.index = i;
							cardList.add(cardInfo);
						}
					}
				}
				
				tableStatusResponse.setCardList(cardList);
			}
		}
		
		tableStatusResponse.setTableTid(gameTable.getGameTablePO().getId());
		tableStatusResponse.setInstantState(gameTable.getInstantState());
		tableStatusResponse.setTableName(gameTable.getGameTablePO().getName());

		long timeLeft = 0;
		if(gameTable.getRound().getRoundPO().getStatusEnum() == RoundStatusEnum.BETTING) {
			if(gameTable.getCoundDownDate()!=null) {
	            timeLeft = gameTable.getGameTablePO().getCountDownSeconds() - (System.currentTimeMillis() - gameTable.getCoundDownDate().getTime()) / 1000;
	            if(timeLeft >= 0) {
	            	tableStatusResponse.setTimeLeft((int) timeLeft);
	            }
	        }
		} else if (gameTable.getRound().getRoundPO().getStatusEnum() == RoundStatusEnum.AWAITING_RESULT) {
			if(gameTable.getCoundDownDate()!=null && gameTable.getCountDownSeconds() != null) {
	            timeLeft = gameTable.getCountDownSeconds() - (System.currentTimeMillis() - gameTable.getCoundDownDate().getTime()) / 1000;
	            if(timeLeft >= 0) {
	            	tableStatusResponse.setTimeLeft((int) timeLeft);
	            }
	        }
		}

		CommandResponse response = CommandResponse.createSuccess(RequestCommandEnum.DEALER_TABLESTATUS,tableStatusResponse);
		SocketUtil.send(client, ResponseCommandEnum.OK,response);
	}

	public void selectTableCommandResponse(SocketIOClient client,SelectTableResponse selectTableResponse){
		CommandResponse res = CommandResponse.createSuccess(RequestCommandEnum.CLIENT_SELECT_TABLE,selectTableResponse);
		SocketUtil.send(client,ResponseCommandEnum.OK,res);
	}

	/**
	 * 发送给所有多台用户
	 * @param commandResponse
	 * @param commonDealerGamepause
	 */
	public void sendAllMultipleUser(IResponse commandResponse, RequestCommandEnum commonDealerGamepause) {
		CommandResponse res = CommandResponse.createSuccess(commonDealerGamepause,commandResponse);
		Collection<SocketIOClient> clients = socketIOServer.getAllClients();
		clients.forEach( item -> {
			UserPO user = AppCache.getUserByClient(item);
			if(null == BaccaratCache.getMultipleUserMap(user.getId()))
				return;
			SocketUtil.send(item, ResponseCommandEnum.OK,res);
		});
	}
}
