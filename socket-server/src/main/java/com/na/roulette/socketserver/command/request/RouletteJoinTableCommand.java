package com.na.roulette.socketserver.command.request;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.entity.Play;
import com.na.baccarat.socketserver.entity.TradeItem;
import com.na.roulette.socketserver.cache.RouletteCache;
import com.na.roulette.socketserver.command.RouletteRequestCommandEnum;
import com.na.roulette.socketserver.command.requestpara.JoinTablePara;
import com.na.roulette.socketserver.command.send.RouletteTableCommand;
import com.na.roulette.socketserver.command.sendpara.RouletteDealJoinResponse;
import com.na.roulette.socketserver.command.sendpara.RouletteJoinRoomResponse;
import com.na.roulette.socketserver.common.enums.RouletteGameTableInstantStateEnum;
import com.na.roulette.socketserver.entity.RouletteGameTable;
import com.na.roulette.socketserver.entity.RouletteRound;
import com.na.roulette.socketserver.entity.RouletteUser;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.common.enums.DealerUserTypeEnum;
import com.na.user.socketserver.common.enums.GameEnum;
import com.na.user.socketserver.common.enums.LiveUserTypeEnum;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.common.enums.UserTypeEnum;
import com.na.user.socketserver.entity.DealerUserPO;
import com.na.user.socketserver.entity.LiveUserPO;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.service.IDealerClassRecordService;
import com.na.user.socketserver.util.ConvertUtil;


/**
 * 轮盘加入桌子
 * 
 * @author alan
 * @date 2017年5月12日 下午12:10:08
 */
@Cmd(paraCls = JoinTablePara.class,name = "轮盘加入桌子")
@Component
public class RouletteJoinTableCommand implements ICommand {
	
	@Autowired
	private RouletteTableCommand rouletteTableCommand;
	@Autowired
	private IDealerClassRecordService dealerClassRecordService;
	
	@Override
	public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
		JoinTablePara params = (JoinTablePara) commandReqestPara;
		RouletteJoinRoomResponse response = new RouletteJoinRoomResponse();
		
		if (params == null) {
			throw SocketException.createError("param.not.allow.empty");
		}
		
		response.setTableId(params.getTableId());
		response.setChipId(params.getChipId());

		RouletteGameTable gameTable = RouletteCache.getGameTableById(params.getTableId());
		if(gameTable == null) {
			throw SocketException.createError("table.not.exist");
		}
		
		RouletteUser loginUser = RouletteCache.getLoginUser(ConvertUtil.toLong(client.get(SocketClientStoreEnum.USER_ID.get())));
		UserPO loginUserPO;
		if(loginUser == null) {
			loginUserPO = AppCache.getLoginUser(ConvertUtil.toLong(client.get(SocketClientStoreEnum.USER_ID.get())));
			loginUser = new RouletteUser(loginUserPO);
			RouletteCache.addLoginUser(loginUser);
		}
		loginUserPO = loginUser.getUserPO();
		
		//FIXME 方便测试  已注掉
//		if(loginUserPO.getTableId() != null || loginUser.isMultipleTableUser()) {
//			throw SocketException.createError("已在座位中,请先退出");
//		}
		
		if(UserTypeEnum.REAL == loginUserPO.getUserTypeEnum()) {
			LiveUserPO liveUserPO = (LiveUserPO) loginUserPO;
			if(LiveUserTypeEnum.PLAYER == liveUserPO.getTypeEnum()) {
				gameTable.addPlayer(loginUser);
				
				loginUserPO.setTableId(params.getTableId());
				loginUserPO.setInTable(true);
				loginUser.setChipId(params.getChipId());
				
				getResponseData(response, gameTable, loginUser.getTradeItemBetMoneyMap());
				response.setNickName(loginUserPO.getNickName());
				
				rouletteTableCommand.sendAllTableOtherClient(client, response, RouletteRequestCommandEnum.COMMON_OTHER_JOIN_TABLE, params.getTableId());
			}
		} else if (UserTypeEnum.DEALER == loginUserPO.getUserTypeEnum()) {
			DealerUserPO dealerUserPO = (DealerUserPO) loginUserPO;
			if(DealerUserTypeEnum.DEALER == dealerUserPO.getTypeEnum()) {
//				if(gameTable.getDealer() != null) {
//					throw SocketException.createError("table.exist.dealer", gameTable.getDealer().getUserPO().getLoginName());
//				}
				
				//gameTable.getGameTablePO().setStatus(1);
				gameTable.setDealer(loginUser);
				if(gameTable.getInstantState() == null){
					gameTable.setInstantStateEnum(RouletteGameTableInstantStateEnum.INACTIVE);
				}
				loginUserPO.setTableId(params.getTableId());
				loginUserPO.setInTable(true);
				
				//更新游戏记录
				dealerClassRecordService.dealerJoinTable(((DealerUserPO)loginUserPO).getDealerClassRecordId(), RouletteCache.getGame().getGamePO(), gameTable.getGameTablePO());
				
				RouletteDealJoinResponse dealJoinResponse = new RouletteDealJoinResponse();
				dealJoinResponse.setNickName(loginUserPO.getNickName());
				dealJoinResponse.setDealerPic(loginUserPO.getHeadPic());
				
				rouletteTableCommand.sendAllTableOtherClient(client, dealJoinResponse, RouletteRequestCommandEnum.DEALER_JOIN, params.getTableId());
			} else if(DealerUserTypeEnum.DEALER == dealerUserPO.getTypeEnum()) {
				if(gameTable.getCheckers() != null) {
					throw SocketException.createError("table.exist.checker");
				}
				loginUserPO.setInTable(true);
				loginUserPO.setTableId(params.getTableId());
				gameTable.setCheckers(loginUser);
			}
		} else {
			throw SocketException.createError("user.type.error");
		}
		
		loginUserPO.setInTable(true);
		client.joinRoom(GameEnum.ROULETTE.get() + params.getTableId());
		
		client.set(SocketClientStoreEnum.TABLE_ID.get(), params.getTableId() + "");
		client.set(SocketClientStoreEnum.GAME_CODE.get(), RouletteCache.getGame().getGamePO().getGameCode());
		loginUserPO.setGameCode(RouletteCache.getGame().getGamePO().getGameCode());
		
		if(params.isQuickChangeRoom()) {
			response.setQuickChange(params.isQuickChangeRoom());
		}
		rouletteTableCommand.join(client, response);
		rouletteTableCommand.sendTableStatus(client);
		return true;
	}
	
	/**
	 * 组装用户返回数据
	 */
	public void getResponseData(RouletteJoinRoomResponse response, RouletteGameTable table, Map<Integer,BigDecimal> tradeItemBetMoneyMap) {
		//获取玩法列表
		List<RouletteJoinRoomResponse.PlayInfo>  playInfoList = new ArrayList<>();
		RouletteJoinRoomResponse.PlayInfo playInfo = null;
		List<RouletteJoinRoomResponse.TradeItemInfo> tradeList = null;
		RouletteJoinRoomResponse.TradeItemInfo itemInfo = null;
		List<Play> playList = table.getPlayList();
		for(Play play : playList) {
			playInfo = response.new PlayInfo();
			tradeList = new ArrayList<>();
			for(TradeItem item :play.getTradeList()) {
				itemInfo = response.new TradeItemInfo();
				itemInfo.id = item.getId();
				itemInfo.key = item.getKey();
				itemInfo.name = item.getName();
				itemInfo.rate = item.getRate().subtract(BigDecimal.ONE).doubleValue();
				itemInfo.number = item.getAddition();
				tradeList.add(itemInfo);
			}
			playInfo.tradeList = tradeList;
			playInfo.id = play.getId();
			playInfo.name = play.getName();
			playInfoList.add(playInfo);
		}
		
		//获取用户已下注数量
		List<RouletteJoinRoomResponse.UserBetInfo> userBetList = new ArrayList<>();
		tradeItemBetMoneyMap.forEach( (key, value) -> {
			RouletteJoinRoomResponse.UserBetInfo userBetInfo = response.new UserBetInfo();
			userBetInfo.tradeId = key;
			userBetInfo.number = value;
			userBetList.add(userBetInfo);
		});
		response.setUserBetList(userBetList);
		
		response.setPlayList(playInfoList);
		//获取该桌详细的路子信息
		if(table.getRounds() != null) {
			List<RouletteJoinRoomResponse.RoundInfo> roundList = new ArrayList<>();
			RouletteJoinRoomResponse.RoundInfo roundInfo;
			for(RouletteRound round : table.getRounds()) {
				roundInfo = response.new RoundInfo();
				roundInfo.result = round.getRoundPO().getResult();
				roundList.add(roundInfo);
			}
			response.setRoundList(roundList);
		}
	}
	
}
