package com.na.baccarat.socketserver.command.flow.user;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.requestpara.ClearCardPara;
import com.na.baccarat.socketserver.command.requestpara.CutCardPara;
import com.na.baccarat.socketserver.command.requestpara.JoinRoomPara;
import com.na.baccarat.socketserver.command.requestpara.LeaveRoomPara;
import com.na.baccarat.socketserver.command.requestpara.NewBootPara;
import com.na.baccarat.socketserver.command.requestpara.SetCardPara;
import com.na.baccarat.socketserver.command.requestpara.ShuffleEndPara;
import com.na.baccarat.socketserver.command.requestpara.ShufflePara;
import com.na.baccarat.socketserver.command.send.GoodRoadsCommand;
import com.na.baccarat.socketserver.command.send.RoomCommand;
import com.na.baccarat.socketserver.command.send.TableCommand;
import com.na.baccarat.socketserver.command.sendpara.ClearCardResponse;
import com.na.baccarat.socketserver.command.sendpara.CutCardResponse;
import com.na.baccarat.socketserver.command.sendpara.DealJoinResponse;
import com.na.baccarat.socketserver.command.sendpara.JoinRoomResponse;
import com.na.baccarat.socketserver.command.sendpara.LeaveRoomResponse;
import com.na.baccarat.socketserver.command.sendpara.NewBootResponse;
import com.na.baccarat.socketserver.command.sendpara.NewGameResponse;
import com.na.baccarat.socketserver.command.sendpara.SetCardResponse;
import com.na.baccarat.socketserver.command.sendpara.ShuffleEndResponse;
import com.na.baccarat.socketserver.command.sendpara.ShuffleResponse;
import com.na.baccarat.socketserver.command.sendpara.StartBetResponse;
import com.na.baccarat.socketserver.command.sendpara.StopBetResponse;
import com.na.baccarat.socketserver.entity.GameTable;
import com.na.baccarat.socketserver.entity.User;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.flow.user.DealerUser;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.enums.ErrorCode;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.entity.DealerUserPO;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.service.IDealerClassRecordService;
import com.na.user.socketserver.util.ConvertUtil;
import com.na.user.socketserver.util.DateUtil;

@Component
public class BaccaratDealerUser extends DealerUser implements BaccaratDealerAction {
	
	private Logger log = LoggerFactory.getLogger(BaccaratDealerUser.class);
	private Logger userLog = LoggerFactory.getLogger("user.login");
	
	@Autowired
	private RoomCommand roomCommand;
	@Autowired
	private TableCommand tableCommand;
	@Autowired
	private GoodRoadsCommand goodRoadsCommand;
	@Autowired
	private IDealerClassRecordService dealerClassRecordService;
	
	@Override
	public void join(JoinRoomPara params, SocketIOClient client, JoinRoomResponse response) {
		User loginUser = BaccaratCache.getLoginUser(ConvertUtil.toLong(client.get(SocketClientStoreEnum.USER_ID.get())));
		UserPO loginUserPO;
		if(loginUser == null) {
			loginUserPO = AppCache.getLoginUser(ConvertUtil.toLong(client.get(SocketClientStoreEnum.USER_ID.get())));
			SocketException.isNull(loginUserPO,"login.again");
			loginUser = new User(loginUserPO);
			BaccaratCache.addLoginUser(loginUser);
		} else {
			loginUserPO = loginUser.getUserPO();
		}
		
		Integer tableId = params.getTableId();
		GameTable table;
		if(tableId != null) {
			table = BaccaratCache.getGameTableById(params.getTableId());
			if(table == null) {
				throw SocketException.createError("table.not.exist");
			}
			response.setTableId(tableId);
		} else {
			throw SocketException.createError("please.select.table");
		}
		
		log.debug("荷官" + loginUserPO.getLoginName() + "加入实体桌" + table.getGameTablePO().getId() + " : " + table.getGameTablePO().getName());
		
		if(loginUserPO.getTableId() != null && tableId.compareTo(loginUserPO.getTableId()) != 0) {
			GameTable table2 = BaccaratCache.getGameTableById(loginUserPO.getTableId());
			table2.setDealer(null);
		}
		
		if(table.getDealer() != null && table.getDealer().getUserPO().getId().compareTo(loginUserPO.getId()) != 0) {
			throw SocketException.createError("table.exist.dealer", table.getDealer().getUserPO().getLoginName());
		}
		client.set(SocketClientStoreEnum.TABLE_ID.get(), tableId + "");
		//游戏状态开启
		table.setDealer(loginUser);
		loginUserPO.setTableId(tableId);
		loginUserPO.setInTable(true);
		
		//更新游戏记录
		dealerClassRecordService.dealerJoinTable(((DealerUserPO)loginUserPO).getDealerClassRecordId(), BaccaratCache.getGame().getGamePO(), table.getGameTablePO());
		
		//通知所有虚拟桌 荷官加入桌子
		DealJoinResponse dealJoinResponse = new DealJoinResponse();
		dealJoinResponse.setNickName(loginUserPO.getNickName());
		dealJoinResponse.setDealerPic(loginUserPO.getHeadPic());
		dealJoinResponse.setTableId(table.getGameTablePO().getId());
		tableCommand.sendAllTablePlayer(dealJoinResponse, RequestCommandEnum.DEALER_JOIN,table.getGameTablePO().getId());
		
		roomCommand.send(client, RequestCommandEnum.COMMON_JOIN_ROOM, response);
		//发送桌子信息
		tableCommand.sendTableStatus(client);
	}

	@Override
	public void leave(LeaveRoomPara params, SocketIOClient client,
			LeaveRoomResponse response) {
		com.na.baccarat.socketserver.entity.User loginUser = BaccaratCache.getLoginUser(ConvertUtil.toLong(client.get(SocketClientStoreEnum.USER_ID.get())));
		if(loginUser == null) {
			throw SocketException.createError(ErrorCode.RELOGIN, "user.not.exist");
		}
		DealerUserPO loginUserPO = (DealerUserPO) loginUser.getUserPO();
		
		Integer tableId = ConvertUtil.toInt(client.get(SocketClientStoreEnum.TABLE_ID.get()));
		GameTable gameTable = BaccaratCache.getGameTableById(tableId);
		if (loginUserPO.getId().compareTo(gameTable.getDealer().getUserPO().getId()) == 0) {
			//将实体桌的荷官置为空
//			gameTable.setInstantStateEnum(GameTableInstantStateEnum.INACTIVE);
//	    	gameTable.getGameTablePO().setStatus(0);
	    	gameTable.setDealer(null);
	    	
			BaccaratCache.removeUser(loginUserPO.getId());
			
	    	//游戏状态在投注中处理-todo
	    	response.setLoginId(loginUserPO.getLoginId());
	    	response.setUserType(loginUserPO.getUserType());
	    	response.setTableName(gameTable.getGameTablePO().getName());
	    	response.setTableId(tableId);
			
	    	tableCommand.sendAllTablePlayer(response, RequestCommandEnum.DEALER_LEVEL_TABLE, tableId);
		} else {
			throw SocketException.createError("dealer.not.equals");
		}
	}
	
	@Override
	public void shuffle(ShufflePara params, SocketIOClient client, ShuffleResponse response) {
		com.na.baccarat.socketserver.entity.User user = BaccaratCache.getUserByClient(client);
		if(user == null) {
			throw SocketException.createError("user.not.exist");
		}
		GameTable table = BaccaratCache.getGameTableById(user.getUserPO().getTableId());
		if(table != null) {
			baccaratClassHandler.table(table).shuffle(params, client, response);
		} else {
			throw SocketException.createError("dealer.not.join.table");
		}
	}
	
	@Override
	public void shuffleEnd(ShuffleEndPara params, SocketIOClient client,
			ShuffleEndResponse response) {
		com.na.baccarat.socketserver.entity.User user = BaccaratCache.getUserByClient(client);
		if(user == null) {
			throw SocketException.createError("user.not.exist");
		}
		GameTable table = BaccaratCache.getGameTableById(user.getUserPO().getTableId());
		if(table != null) {
			baccaratClassHandler.table(table).shuffleEnd(params, client, response);
		} else {
			throw SocketException.createError("dealer.not.join.table");
		}
	}
	
	@Override
	public void newBoot(NewBootPara params, SocketIOClient client, NewBootResponse response) {
		com.na.baccarat.socketserver.entity.User user = BaccaratCache.getUserByClient(client);
		UserPO userPO = user.getUserPO();
		if(userPO.getTableId() != null ) {
			GameTable gameTable = BaccaratCache.getGameTableById(user.getUserPO().getTableId());
			
			baccaratClassHandler.table(gameTable).newBoot(params, client, response);
			
			tableCommand.sendTableStatus(client);
			tableCommand.sendAllTablePlayer(response, RequestCommandEnum.COMMON_NEW_BOOT, userPO.getTableId());
		} else {
			throw SocketException.createError("dealer.not.in.table");
		}
	}
	
	@Override
	public void newGame(CommandReqestPara params, SocketIOClient client,
			NewGameResponse response) {
		com.na.baccarat.socketserver.entity.User user = BaccaratCache.getUserByClient(client);
		if(user == null) {
			throw SocketException.createError("user.not.exist");
		}
		GameTable table = BaccaratCache.getGameTableById(user.getUserPO().getTableId());
		if(table != null) {
			baccaratClassHandler.table(table).newGame(params, client, response);
		} else {
			throw SocketException.createError("dealer.not.join.table");
		}
	}
	
	@Override
	public void startBet(CommandReqestPara params, SocketIOClient client,
			StartBetResponse response) {
		com.na.baccarat.socketserver.entity.User user = BaccaratCache.getUserByClient(client);
		if(user == null) {
			throw SocketException.createError("user.not.exist");
		}
		GameTable table = BaccaratCache.getGameTableById(user.getUserPO().getTableId());
		if(table != null) {
			baccaratClassHandler.table(table).startBet(params, client, response);
			goodRoadsCommand.checkGoodRoads(user.getUserPO().getTableId(), socketIOServer);
		} else {
			throw SocketException.createError("dealer.not.join.table");
		}	
	}
	
	@Override
	public void stopBet(CommandReqestPara params, SocketIOClient client,
			StopBetResponse response) {
		com.na.baccarat.socketserver.entity.User user = BaccaratCache.getUserByClient(client);
		if(user == null) {
			throw SocketException.createError("user.not.exist");
		}
		GameTable table = BaccaratCache.getGameTableById(user.getUserPO().getTableId());
		if(table != null) {
			baccaratClassHandler.table(table).stopBet(params, client, response);
		} else {
			throw SocketException.createError("dealer.not.join.table");
		}	
	}
	
	@Override
	public void setCard(SetCardPara params, SocketIOClient client,
			SetCardResponse response) {
		com.na.baccarat.socketserver.entity.User user = BaccaratCache.getUserByClient(client);
		if(user == null) {
			throw SocketException.createError("user.not.exist");
		}
		GameTable table = BaccaratCache.getGameTableById(user.getUserPO().getTableId());
		if(table != null) {
			baccaratClassHandler.table(table).setCard(params, client, response);
		} else {
			throw SocketException.createError("dealer.not.join.table");
		}	
	}

	@Override
	public void cutCard(CutCardPara params, SocketIOClient client,
			CutCardResponse response) {
		com.na.baccarat.socketserver.entity.User user = BaccaratCache.getUserByClient(client);
		if(user == null) {
			throw SocketException.createError("user.not.exist");
		}
		GameTable table = BaccaratCache.getGameTableById(user.getUserPO().getTableId());
		if(table != null) {
			baccaratClassHandler.table(table).cutCard(params, client, response);
		} else {
			throw SocketException.createError("dealer.not.join.table");
		}
		//发送给荷官切牌
		SocketIOClient dealerClient = AppCache.getDisConnectClientMap(table.getDealer().getUserPO().getId());
		roomCommand.send(dealerClient, RequestCommandEnum.CLIENT_CUT_CARD, response);
	}

	@Override
	public void clearCard(ClearCardPara params, SocketIOClient client,
			ClearCardResponse response) {
		com.na.baccarat.socketserver.entity.User user = BaccaratCache.getUserByClient(client);
		if(user == null) {
			throw SocketException.createError("user.not.exist");
		}
		GameTable table = BaccaratCache.getGameTableById(user.getUserPO().getTableId());
		table.removeCard(params.getIndex());
		response.setIndex(params.getIndex());
		
//		RoundExt roundExt = table.getRoundExt();
		
//		RoundExtPO roundExtPO = new RoundExtPO();
//		roundExtPO.setRoundId(table.getRound().getRoundPO().getId());
//		roundExt.setRoundExtPO(roundExtPO);
//		roundExt.setOpenCards(new ArrayList<>());

		//FIXME 记录操作日志
		Map<String,Object> logMap = new HashMap<>();
		logMap.put("userId", user.getUserPO().getId());
		logMap.put("info", "clearCard "+params.getIndex());
		logMap.put("time", DateUtil.format(new Date(), DateUtil.yyyy_MM_ddHHMMss));
		logMap.put("tableId", user.getUserPO().getTableId());
		userLog.info(JSONObject.toJSONString(logMap));
		
		roomCommand.send(client, RequestCommandEnum.DEALER_CLEAR_CARD, response);
	}

}
