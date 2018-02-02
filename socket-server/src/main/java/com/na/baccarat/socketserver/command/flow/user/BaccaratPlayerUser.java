package com.na.baccarat.socketserver.command.flow.user;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.flow.table.NormalTable;
import com.na.baccarat.socketserver.command.request.TradeItemCommand;
import com.na.baccarat.socketserver.command.requestpara.BetPara;
import com.na.baccarat.socketserver.command.requestpara.JoinRoomPara;
import com.na.baccarat.socketserver.command.requestpara.LeaveRoomPara;
import com.na.baccarat.socketserver.command.requestpara.QuickChangeRoomPara;
import com.na.baccarat.socketserver.command.requestpara.SelectRoomPara;
import com.na.baccarat.socketserver.command.requestpara.TradeItemPara;
import com.na.baccarat.socketserver.command.send.RoomCommand;
import com.na.baccarat.socketserver.command.send.TableCommand;
import com.na.baccarat.socketserver.command.sendpara.BetResponse;
import com.na.baccarat.socketserver.command.sendpara.JoinRoomResponse;
import com.na.baccarat.socketserver.command.sendpara.LeaveRoomResponse;
import com.na.baccarat.socketserver.command.sendpara.SelectRoomResponse;
import com.na.baccarat.socketserver.common.enums.BetOrderSourceEnum;
import com.na.baccarat.socketserver.common.enums.TradeItemEnum;
import com.na.baccarat.socketserver.common.limitrule.CheckLimitRule;
import com.na.baccarat.socketserver.entity.GameTable;
import com.na.baccarat.socketserver.entity.TradeItem;
import com.na.baccarat.socketserver.entity.User;
import com.na.baccarat.socketserver.entity.UserBet;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.flow.user.PlayerUser;
import com.na.user.socketserver.common.enums.ErrorCode;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.common.enums.UserIsBetEnum;
import com.na.user.socketserver.common.enums.UserStatusEnum;
import com.na.user.socketserver.common.enums.UserTypeEnum;
import com.na.user.socketserver.entity.LiveUserPO;
import com.na.user.socketserver.entity.UserChipsPO;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.CacheContainsException;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.service.IBetOrderService;
import com.na.user.socketserver.util.ConvertUtil;


@Component
public class BaccaratPlayerUser extends PlayerUser implements BaccaratPlayerAction {
	
	private Logger log = LoggerFactory.getLogger(BaccaratPlayerUser.class);
	
	@Autowired
	private RoomCommand roomCommand;
	
	@Autowired
	private TableCommand tableCommand;
	
	@Autowired
	private IBetOrderService betOrderService;
	
	@Autowired
	private NormalTable normalTable;
	
	@Autowired
	private TradeItemCommand tradeItemCommand;
	
	@Override
	public void bet(BetPara params, SocketIOClient client, BetResponse response) {
		com.na.baccarat.socketserver.entity.User user = BaccaratCache.getUserByClient(client);
        if(user == null) {
        	UserPO userPO = AppCache.getUserByClient(client);
        	if(userPO == null){
        		throw SocketException.createError("user.not.exist");
        	}
        	user = new com.na.baccarat.socketserver.entity.User();
        	user.setUserPO(userPO);
        }
        LiveUserPO userPO = (LiveUserPO) user.getUserPO();
        
        SocketException.isTrue(
        		userPO.getUserStatusEnum() == UserStatusEnum.LOCKED
                || userPO.getUserStatusEnum() == UserStatusEnum.FREEZE
                || ((LiveUserPO)userPO).getIsBetEnum() == UserIsBetEnum.LOCKED
                ,"user.not.allow.bet");
        SocketException.isTrue(
        		userPO.getUserTypeEnum()!=UserTypeEnum.REAL,
                "non.member.not.allow.bet"
        );
        
        checkUserChip(params, user);

        checkUserEnoughBalance(params, user);
        //检查个人最大赢额和最高余额
        checkPersonMaxMoney(params, user);
        
        Integer tableId = params.getTableId();
        GameTable table;
		if(tableId != null && tableId.compareTo(new Integer(0)) != 0) {
			table = BaccaratCache.getGameTableById(tableId);
			if(table == null) {
				throw SocketException.createError("table.not.exist");
			}
		} else {
			throw SocketException.createError("please.select.table");
		}
		
		baccaratClassHandler.table(table).bet(params, client, response);
        
		roomCommand.send(client, RequestCommandEnum.CLIENT_BET, response);
	}
	
	@Override
	public void join(JoinRoomPara params, SocketIOClient client, JoinRoomResponse response) {
		
		UserPO loginUserPO = AppCache.getUserByClient(client);
		if(loginUserPO==null) {
			throw SocketException.createError(ErrorCode.RELOGIN, "login.again");
		}
		User loginUser = new User(loginUserPO);
		BaccaratCache.addLoginUser(loginUser);
		LiveUserPO liveUserPO = (LiveUserPO) loginUser.getUserPO();
		
		if(loginUserPO.isInTable()) {
			throw CacheContainsException.createError("user.exist.in.table");
		}
		//根据不同的桌子 走不同的路径
		if(BetOrderSourceEnum.MANY_TYPE == params.getSourceEnum()) {
			BaccaratCache.getMultipleUserMap().remove(liveUserPO.getId());
			BaccaratCache.getMultipleUserMap().put(liveUserPO.getId(), loginUser);
			liveUserPO.setInTable(true);
//			loginUser.setChipId(params.getChipId());
			loginUser.setSource(BetOrderSourceEnum.MANY_TYPE);
			response.setSourceEnum(BetOrderSourceEnum.MANY_TYPE);
			response.setTableType(9);
			
			List<UserBet> userBetInfos = null;
			for(GameTable table : BaccaratCache.getMultipleTableList()) {
				userBetInfos = table.getRound().getUserBetedInfos().stream().filter( item -> {
					return item.getSource() == BetOrderSourceEnum.SEAT || item.getUserId().compareTo(liveUserPO.getId()) == 0;
				}).collect(Collectors.toList());
			}
			
			getResponseData(response, userBetInfos);
			roomCommand.send(client, RequestCommandEnum.COMMON_JOIN_ROOM, response);
			
			//返回交易项
			TradeItemPara tradeItemPara = new TradeItemPara();
			tradeItemPara.setPlayId(1);
			tradeItemCommand.exec(client, tradeItemPara);
			
			//发送多台桌子信息
			BaccaratCache.getMultipleTableList().forEach( item -> {
				tableCommand.sendTableStatus(client, item.getGameTablePO().getId());
			});
		} else {
			Integer tableId = params.getTableId();
			GameTable table;
			if(tableId != null && tableId.compareTo(new Integer(0)) != 0) {
				table = BaccaratCache.getGameTableById(tableId);
				if(table == null) {
					throw SocketException.createError("please.select.table");
				}
				client.set(SocketClientStoreEnum.TABLE_ID.get(), tableId + "");
				response.setTableId(tableId);
			} else {
				throw SocketException.createError("please.select.table");
			}
			
			baccaratClassHandler.table(table).join(params, client, response);
			liveUserPO.setInTable(true);
			response.setQuickChange(params.isQuickChangeRoom());
//			response.getSeatList().forEach( value -> {
//    			if(loginUserPO.getId().compareTo(value.userId) != 0) {
//    				value.nickName = StringUtil.hideNickName(value.nickName);
//    			}
//    		});
			
			roomCommand.send(client, RequestCommandEnum.COMMON_JOIN_ROOM, response);
			//发送桌子信息
			tableCommand.sendTableStatus(client);
		}
	}

	@Override
	public void leave(LeaveRoomPara params, SocketIOClient client, LeaveRoomResponse response) {
		com.na.baccarat.socketserver.entity.User loginUser = BaccaratCache.getLoginUser(ConvertUtil.toLong(client.get(SocketClientStoreEnum.USER_ID.get())));
		LiveUserPO userPO = (LiveUserPO) loginUser.getUserPO();
		
		String tableName = "";
		//多台
		if(BaccaratCache.getMultipleUserMap().containsKey(userPO.getId())) {
			BaccaratCache.removeMultipleUser(userPO.getId());
			tableName = "多台";
		} else {
			GameTable table = BaccaratCache.getGameTableById(userPO.getTableId());
			
			baccaratClassHandler.table(table).leave(params, client, response);
			
			response.setRountId(table.getRound().getRoundPO().getId());
			tableName = table.getGameTablePO().getName();
		}
		
		userPO.setTableId(null);
		userPO.setInTable(false);
		loginUser.setSource(null);
		BaccaratCache.removeUser(userPO.getId());
		
		response.setUserId(loginUser.getUserPO().getId());
		response.setLoginName(userPO.getLoginName());
		response.setUserType(userPO.getUserType());
		response.setLoginId(userPO.getLoginId());
		if(params != null) {
			response.setForce(params.isForce());
		} else {
			response.setForce(false);
		}
		
		if(params == null || ((params != null && !params.isQuickChangeRoom()) && (params != null && !params.isReconnect()))) {
			roomCommand.send(client, RequestCommandEnum.COMMON_LEAVE_ROOM, response);
		}
		log.debug("玩家" + userPO.getLoginName() + "离开房间" + tableName);
	}
	
	/**
     * 检查个人最大赢额和最高余额
     */
    private void checkPersonMaxMoney(BetPara betPara, com.na.baccarat.socketserver.entity.User user) {
    	BigDecimal personMaxWinMoney = ((LiveUserPO)(user.getUserPO())).getBiggestWinMoney();
    	BigDecimal personBiggestBalance = ((LiveUserPO)(user.getUserPO())).getBiggestBalance();
    	BigDecimal personWinMoney = ((LiveUserPO)(user.getUserPO())).getWinMoney();
    	if(personBiggestBalance == null) {
    		personBiggestBalance = BigDecimal.ZERO;
    	}
    	if(personMaxWinMoney == null) {
    		personMaxWinMoney = BigDecimal.ZERO;
    	}
    	if(personWinMoney == null) {
    		personWinMoney = BigDecimal.ZERO;
    	}
    	BigDecimal roundWinMoney = new BigDecimal(0);
    	List<BetPara.Bet> betList = betPara.getBets().stream().filter( item -> {
    		TradeItem tradeItem = AppCache.getTradeItem(item.tradeId);
			return tradeItem.getTradeItemEnum() == TradeItemEnum.BANKER || 
					tradeItem.getTradeItemEnum() == TradeItemEnum.PLAYER;
		}).collect(Collectors.toList());
    	
    	BigDecimal bpWinMoney = new BigDecimal(0);
    	if(betList != null) {
    		if(betList.size() == 1) {
    			TradeItem tradeItem = AppCache.getTradeItem(betList.get(0).tradeId);
    			bpWinMoney = bpWinMoney.add(betList.get(0).amount.multiply(tradeItem.getRate().subtract(new BigDecimal(1))));
    		} else if (betList.size() == 2) {
    			TradeItem tradeItem = AppCache.getTradeItem(betList.get(0).tradeId);
    			bpWinMoney = bpWinMoney.add(betList.get(0).amount.multiply(tradeItem.getRate().subtract(new BigDecimal(1))));
    			tradeItem = AppCache.getTradeItem(betList.get(1).tradeId);
    			bpWinMoney = bpWinMoney.subtract(betList.get(1).amount.multiply(tradeItem.getRate().subtract(new BigDecimal(1))));
    		}
    	}
    	roundWinMoney = roundWinMoney.add(bpWinMoney.abs());
    	
    	betList = betPara.getBets().stream().filter( item -> {
    		TradeItem tradeItem = AppCache.getTradeItem(item.tradeId);
			return tradeItem.getTradeItemEnum() != TradeItemEnum.BANKER && 
					tradeItem.getTradeItemEnum() != TradeItemEnum.PLAYER;
		}).collect(Collectors.toList());
    	
    	for(BetPara.Bet bet : betList) {
    		TradeItem tradeItem = AppCache.getTradeItem(bet.tradeId);
    		roundWinMoney = roundWinMoney.add(bet.amount.multiply(tradeItem.getRate().subtract(new BigDecimal(1))));
    	}
    	personWinMoney = personWinMoney.add(roundWinMoney);
    	if(personMaxWinMoney.compareTo(BigDecimal.ZERO) != 0 && personMaxWinMoney.compareTo(personWinMoney) < 0) {
    		throw SocketException.createError("maxwinmoney.beyond");
    	}
    	
    	BigDecimal personBalance = user.getUserPO().getBalance().add(roundWinMoney);
    	if(personBiggestBalance.compareTo(BigDecimal.ZERO) != 0 && personBiggestBalance.compareTo(personBalance) < 0) {
    		throw SocketException.createError("maxbalance.beyond");
    	}
    }
	
	/**
     * 1、检查用户的余额是否足够
     * @param betPara
     * @param user
     */
    private void checkUserEnoughBalance(BetPara betPara, com.na.baccarat.socketserver.entity.User user) {
        BigDecimal betTotal = BigDecimal.ZERO;
        for(BetPara.Bet bet : betPara.getBets()){
            betTotal = betTotal.add(bet.amount);
            if(betTotal.compareTo(user.getUserPO().getBalance())>0 || user.getUserPO().getBalance().compareTo(BigDecimal.ZERO)<0){
                throw SocketException.createError("user.balance.not.enough");
            }
        }
    }
	
	/**
     * 检查用户限红。
     * @param betPara
     * @param user
     */
    private void checkUserChip(BetPara betPara, com.na.baccarat.socketserver.entity.User user) {
    	LiveUserPO liveUserPO = (LiveUserPO) user.getUserPO();
    	if(liveUserPO.getUserChipList() == null || liveUserPO.getUserChipList().size() < 1) {
    		throw SocketException.createError("user.not.chips");
    	}
        UserChipsPO chipPO = liveUserPO.getUserChipList().stream().filter(item->{
        	return item.getId().equals(betPara.getChipsCid());
        }).findFirst().orElse(null);
        if(chipPO == null) {
        	throw SocketException.createError("user.chips.error");
        }
        if(user.getLimitRule(betPara.getTableId()) == null) {
        	user.setLimitRule(betPara.getTableId(),new CheckLimitRule());
        }
        user.getLimitRule(betPara.getTableId()).countUserChips(betPara.getBets(), chipPO);
    }

	@Override
	public void quickChangTable(QuickChangeRoomPara params,
			SocketIOClient client) {
		
	}

	@Override
	public void selectRoom(SelectRoomPara params, SocketIOClient client,
			SelectRoomResponse response) {
		normalTable.selectRoom(params, client, response);
	}
	
	public void getResponseData(JoinRoomResponse response, List<UserBet> userBetInfos) {
		//获取用户已下注数量
		List<JoinRoomResponse.UserBetInfo> userBetList = new ArrayList<>();
		userBetInfos.forEach( item -> {
			JoinRoomResponse.UserBetInfo userBetInfo = userBetList.stream().filter( item2 -> {
				return item2.userId.compareTo(item.getUserId()) == 0;
			}).findFirst().orElse(null);
			List<JoinRoomResponse.UserBetDetailInfo> userBetDetailInfoList;
			
			if(userBetInfo == null) {
				userBetInfo = response.new UserBetInfo();
				userBetInfo.userId = item.getUserId();
				userBetInfo.tableId = item.getTableId();
				userBetDetailInfoList = new ArrayList<>();
				userBetInfo.betList = userBetDetailInfoList;
				userBetList.add(userBetInfo);
			} else {
				userBetDetailInfoList = userBetInfo.betList;
			}
			
			JoinRoomResponse.UserBetDetailInfo userBetDetailInfo = response.new UserBetDetailInfo();
			userBetDetailInfo.tradeId = item.getTradeId();
			userBetDetailInfo.number = item.getAmount();
			userBetDetailInfoList.add(userBetDetailInfo);
		});
		response.setUserBetList(userBetList);
	}
}
