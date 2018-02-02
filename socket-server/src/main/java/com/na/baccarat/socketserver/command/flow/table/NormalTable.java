package com.na.baccarat.socketserver.command.flow.table;

import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.request.LeaveRoomCommand;
import com.na.baccarat.socketserver.command.requestpara.BetPara;
import com.na.baccarat.socketserver.command.requestpara.Card;
import com.na.baccarat.socketserver.command.requestpara.CutCardPara;
import com.na.baccarat.socketserver.command.requestpara.GameResultPara;
import com.na.baccarat.socketserver.command.requestpara.JoinRoomPara;
import com.na.baccarat.socketserver.command.requestpara.LeaveRoomPara;
import com.na.baccarat.socketserver.command.requestpara.SelectRoomPara;
import com.na.baccarat.socketserver.command.requestpara.SetCardPara;
import com.na.baccarat.socketserver.command.send.RoomCommand;
import com.na.baccarat.socketserver.command.send.TableCommand;
import com.na.baccarat.socketserver.command.sendpara.BetResponse;
import com.na.baccarat.socketserver.command.sendpara.CutCardResponse;
import com.na.baccarat.socketserver.command.sendpara.GameResultResponse;
import com.na.baccarat.socketserver.command.sendpara.JoinRoomResponse;
import com.na.baccarat.socketserver.command.sendpara.LeaveRoomResponse;
import com.na.baccarat.socketserver.command.sendpara.SelectRoomResponse;
import com.na.baccarat.socketserver.command.sendpara.SendDealerResponse;
import com.na.baccarat.socketserver.command.sendpara.SetCardResponse;
import com.na.baccarat.socketserver.command.sendpara.StopBetResponse;
import com.na.baccarat.socketserver.command.sendpara.UserBetResultResponse;
import com.na.baccarat.socketserver.command.sendpara.UserCardResponse;
import com.na.baccarat.socketserver.command.sendpara.UserMessageResponse;
import com.na.baccarat.socketserver.common.enums.AccountRecordTypeEnum;
import com.na.baccarat.socketserver.common.enums.BetOrderBetTypeEnum;
import com.na.baccarat.socketserver.common.enums.BetOrderSourceEnum;
import com.na.baccarat.socketserver.common.enums.BetOrderStatusEnum;
import com.na.baccarat.socketserver.common.enums.BetOrderTableTypeEnum;
import com.na.baccarat.socketserver.common.enums.BetOrderWinLostStatusEnum;
import com.na.baccarat.socketserver.common.enums.GameTableInstantStateEnum;
import com.na.baccarat.socketserver.common.enums.PlayEnum;
import com.na.baccarat.socketserver.common.enums.RoundStatusEnum;
import com.na.baccarat.socketserver.common.enums.TradeItemEnum;
import com.na.baccarat.socketserver.common.playrule.AbstractPlayRule;
import com.na.baccarat.socketserver.entity.AccountRecord;
import com.na.baccarat.socketserver.entity.BetOrder;
import com.na.baccarat.socketserver.entity.GameTable;
import com.na.baccarat.socketserver.entity.Play;
import com.na.baccarat.socketserver.entity.Round;
import com.na.baccarat.socketserver.entity.RoundExt;
import com.na.baccarat.socketserver.entity.TradeItem;
import com.na.baccarat.socketserver.entity.User;
import com.na.baccarat.socketserver.entity.UserBet;
import com.na.baccarat.socketserver.entity.VirtualGameTable;
import com.na.baccarat.socketserver.task.MiCardCountDownTask;
import com.na.baccarat.socketserver.util.SnowflakeIdWorker;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.common.enums.VirtualGameTableType;
import com.na.user.socketserver.config.QuartzConfig;
import com.na.user.socketserver.config.SocketIoConfig;
import com.na.user.socketserver.entity.GamePO;
import com.na.user.socketserver.entity.LiveUserPO;
import com.na.user.socketserver.entity.RoundExtPO;
import com.na.user.socketserver.entity.RoundPO;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.entity.VirtualGameTablePO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.service.IAccountRecordService;
import com.na.user.socketserver.service.IBetOrderService;
import com.na.user.socketserver.service.IRoundExtService;
import com.na.user.socketserver.service.IRoundService;
import com.na.user.socketserver.service.IUserService;
import com.na.user.socketserver.util.BeanUtil;
import com.na.user.socketserver.util.ConvertUtil;
import com.na.user.socketserver.util.DateUtil;
import com.na.user.socketserver.util.SocketUtil;
import com.na.user.socketserver.util.TimeLeft;

import io.netty.util.concurrent.DefaultThreadFactory;

import static java.util.stream.Collectors.toList;

@Component
public class NormalTable extends Table {

	private Logger log = LoggerFactory.getLogger(NormalTable.class);
	
	@Autowired
	private SocketIOServer socketIOServer;
	
	@Autowired
	private IRoundExtService roundExtService;
	
	@Autowired
	private RoomCommand roomCommand;
	
	@Autowired
	private IBetOrderService betOrderService;
	
	@Autowired
	private SocketIoConfig socketIoConfig;
	
	@Autowired
	private IAccountRecordService accountRecordService;
	
	@Autowired
	private LeaveRoomCommand leaveRoomCommand;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IRoundService roundService;
	
	@Autowired
	private TableCommand tableCommand;
	
	@Resource(name = "accountRecordSnowflakeIdWorker")
    private SnowflakeIdWorker accountRecordSnowflakeIdWorker;
	
	private ExecutorService normalTableThreadPool;
	/**
	 * 发送结果延时线程池。
	 */
	private ScheduledThreadPoolExecutor sendGameResultExecutor = (ScheduledThreadPoolExecutor)Executors.newScheduledThreadPool(1);
	
	@PostConstruct
	public void init() {
		normalTableThreadPool = Executors.newFixedThreadPool(socketIoConfig.getNormalTableThreadPool(),new DefaultThreadFactory("normalTableSettle"));;
	}
	
	public void join(JoinRoomPara params, SocketIOClient client,JoinRoomResponse response) {
		User loginUser = BaccaratCache.getLoginUser(ConvertUtil.toLong(client.get(SocketClientStoreEnum.USER_ID.get())));
		UserPO loginUserPO = loginUser.getUserPO();
		
		Integer tableId = params.getTableId();
		GameTable table = BaccaratCache.getGameTableById(params.getTableId());
		
		baccaratClassHandler.roomAction(params.getVirtualTableId()).join(params, client, response);
		
		//用于不下注踢出用户  第三局警告  第五局强制踢出
		loginUser.setChipId(params.getChipId());
		loginUser.setNotBetRoundNumber(new Integer(0));
		loginUserPO.setTableId(tableId);
		
		//加入房间成功
		getResponseData(response, table);
		response.setNickName(loginUserPO.getNickName());
		response.setChipId(params.getChipId());
		response.setTableType(table.getGameTablePO().getType());
	}
	
	/**
	 * 组装用户返回数据
	 */
	public void getResponseData(JoinRoomResponse response, GameTable table) {
		//获取玩法列表
		List<JoinRoomResponse.PlayInfo>  playInfoList = new ArrayList<>();
		JoinRoomResponse.PlayInfo playInfo = null;
		List<JoinRoomResponse.TradeItemInfo> tradeList = null;
		JoinRoomResponse.TradeItemInfo itemInfo = null;
		List<Play> playList = new ArrayList<>(table.getPlayList());
		for(Play play : playList) {
			playInfo = response.new PlayInfo();
			tradeList = new ArrayList<>();
			for(TradeItem item :play.getTradeList()) {
				itemInfo = response.new TradeItemInfo();
				itemInfo.id = item.getId();
				itemInfo.key = item.getKey();
				itemInfo.name = item.getName();
				itemInfo.rate = item.getRate().subtract(new BigDecimal(1)).doubleValue();
				tradeList.add(itemInfo);
			}
			playInfo.tradeList = tradeList;
			playInfo.id = play.getId();
			playInfo.name = play.getName();
			playInfoList.add(playInfo);
		}
		response.setPlayList(playInfoList);
		
		//获取该桌详细的路子信息
		if(table.getRounds() != null) {
			List<JoinRoomResponse.RoundInfo> roundList = new ArrayList<>();
			JoinRoomResponse.RoundInfo roundInfo;
			List<JoinRoomResponse.CardInfo> cardList;
			JoinRoomResponse.CardInfo cardInfo;
			Iterator<Round> it = table.getRounds().iterator();
			while(it.hasNext()) {
				Round round = it.next();
				if(round == null) {
					continue;
				}
				roundInfo = response.new RoundInfo();
				cardList = new ArrayList<>();
				RoundExtPO roundExtPO = roundExtService.getRoundExt(round.getRoundPO().getId());
				if(roundExtPO == null) {
					throw SocketException.createError("data.error");
				}
				cardInfo = response.new CardInfo();
				cardInfo.index = 0;
				cardInfo.number = roundExtPO.getPlayerCard1Number();
				cardInfo.type = roundExtPO.getPlayerCard1Mode();
				cardList.add(cardInfo);
				
				cardInfo = response.new CardInfo();
				cardInfo.index = 1;
				cardInfo.number = roundExtPO.getPlayerCard2Number();
				cardInfo.type = roundExtPO.getPlayerCard2Mode();
				cardList.add(cardInfo);
				
				cardInfo = response.new CardInfo();
				cardInfo.index = 2;
				cardInfo.number = roundExtPO.getBankCard1Number();
				cardInfo.type = roundExtPO.getBankCard1Mode();
				cardList.add(cardInfo);
				
				cardInfo = response.new CardInfo();
				cardInfo.index = 3;
				cardInfo.number = roundExtPO.getBankCard2Number();
				cardInfo.type = roundExtPO.getBankCard2Mode();
				cardList.add(cardInfo);
				
				if(roundExtPO.getPlayerCard3Number() != null) {
					cardInfo = response.new CardInfo();
					cardInfo.index = 4;
					cardInfo.number = roundExtPO.getPlayerCard3Number();
					cardInfo.type = roundExtPO.getPlayerCard3Mode();
					cardList.add(cardInfo);
				}
				
				if(roundExtPO.getBankCard3Number() != null) {
					cardInfo = response.new CardInfo();
					cardInfo.index = 5;
					cardInfo.number = roundExtPO.getBankCard3Number();
					cardInfo.type = roundExtPO.getBankCard3Mode();
					cardList.add(cardInfo);
				}
				
				roundInfo.cardList = cardList;
				roundInfo.result = round.getRoundPO().getResult();
				roundList.add(roundInfo);
				
			}

			response.setRoundList(roundList);
		}
	}
	
	@Override
	public void leave(LeaveRoomPara params, SocketIOClient client,
			LeaveRoomResponse response) {
		User loginUser = BaccaratCache.getLoginUser(ConvertUtil.toLong(client.get(SocketClientStoreEnum.USER_ID.get())));
		UserPO userPO = loginUser.getUserPO();
		
		GameTable table = BaccaratCache.getGameTableById(userPO.getTableId());
		Round round = table.getRound();
		
		if(params != null && !params.isReconnect()) {
			if(RoundStatusEnum.BETTING == round.getRoundPO().getStatusEnum() || 
					RoundStatusEnum.AWAITING_RESULT == round.getRoundPO().getStatusEnum()) {
				if(!params.isQuickChangeRoom()) {
					Integer betCount = betOrderService.getBetOrderByRoundCount(userPO.getId(), round.getRoundPO().getId());
					if(betCount > 0) {
						throw SocketException.createError("user.already.bet");
					}
				}
			}
		}
		
		boolean isRemove = false;
		//普通百家乐
		VirtualGameTable virtualTable = BaccaratCache.getVirtualTableById(loginUser.getVirtualTableId());
		
		//虚拟桌处理
		if(virtualTable != null) {
			if(BetOrderSourceEnum.SEAT == loginUser.getSource()) {
				BaccaratCache.leaveVirtualTableSeat(userPO.getTableId(), loginUser.getVirtualTableId(), loginUser.getSeat(), userPO.getId());
				isRemove = true;
				response.setSeat(loginUser.getSeat());
				response.setUserId(userPO.getId());
				loginUser.setSeat(null);
			} else if(BetOrderSourceEnum.SIDENOTE == loginUser.getSource()) {
				virtualTable.getBesideUser().remove(userPO.getId());
				isRemove = true;
			}
			
			if(isRemove) {
				BaccaratCache.freshVirtualTable(virtualTable);
				client.del(SocketClientStoreEnum.VIRTUAL_TABLE_ID.get());
				roomCommand.sendOtherUser(client, response, virtualTable.getVirtualGameTablePO().getId(),RequestCommandEnum.COMMON_OTHER_LEAVE_ROOM);
			}
		} else {
			throw SocketException.createError("virtual.table.update.failure");
		}
	}
	
	@Override
	public void gameResult(GameResultPara params, SocketIOClient client,
			GameResultResponse response) {
		TimeLeft timeLeft = new TimeLeft();

		GameTable table = BaccaratCache.getGameTableById(ConvertUtil.toInt(client.get(SocketClientStoreEnum.TABLE_ID.get())));
		Round round = table.getRound();
		
		if(table.getCards() == null || (table.getCards() != null && table.getCards().length < 4)) {
			throw SocketException.createError("scan.card.not.over");
		}
		
		//根据卡牌计算游戏结果
		Map<PlayEnum,AbstractPlayRule> resultMap = genResult(table.getRoundExt());
		final String gameResult = resultMap.get(PlayEnum.NORMAL).getGameReuslt();

		List<TradeItemEnum> allTradeItemEnum = new ArrayList<>();
		for(AbstractPlayRule rule : resultMap.values()) {
			allTradeItemEnum.addAll(rule.getGameReusltList());
		}

		List<Integer> filterTradeItemIds =  new ArrayList<>();
		for(Play play : table.getPlayList()) {
			filterTradeItemIds.addAll(play.getTradeList().stream()
					.filter( item -> allTradeItemEnum.contains(item.getTradeItemEnum()))
					.map(TradeItem::getId)
					.collect(toList()));
		}

		round.getRoundPO().setResult(gameResult);
		round.getRoundPO().setEndTime(new Date());
		
		final String showResult = getShowResult(gameResult, round, table.getRoundExt());
		
		//虚拟桌列表
		List<Integer> vList = BaccaratCache.getVirtualIdByTableId(table.getGameTablePO().getId());
		Set<Integer> vSet = new HashSet<>();
		vSet.addAll(vList);
		
		//查询订单
		final List<BetOrder> berOrderList = betOrderService.getBetOrderByTable(table.getGameTablePO().getId(), table.getRound().getRoundPO());
		
		//1.处理座位下注
		Map<Integer,List<BetOrder>> betOrderMap = new HashMap<>();
		for(BetOrder betOrder : berOrderList) {
			if(betOrder.getVirtualgametableId() != null) {
				if(betOrderMap.containsKey(betOrder.getVirtualgametableId())) {
					betOrderMap.get(betOrder.getVirtualgametableId()).add(betOrder);
				} else {
					List<BetOrder> betOrderList = new ArrayList<>();
					betOrderList.add(betOrder);
					betOrderMap.put(betOrder.getVirtualgametableId(), betOrderList);
				}
			}
		}
		
		vSet.addAll(betOrderMap.keySet());
		
		final CountDownLatch countDownLatch = new CountDownLatch(vSet.size() + 2);
		for(final Integer vtableId : vSet) {
			normalTableThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						Map<Long, List<UserBetResultResponse.ResultInfo>> userItemMoney = new HashMap<>();
						List<BetOrder> betList = betOrderMap.get(vtableId);
						List<BetOrder> seatBetList = null;
						List<BetOrder> sideBetList = null;
						if(betList != null && !betList.isEmpty()) {
							seatBetList = betList.stream().filter( item -> 
							BetOrderSourceEnum.SEAT == item.getBetOrderSource()).collect(Collectors.toList());
							sideBetList = betList.stream().filter( item -> 
							BetOrderSourceEnum.SIDENOTE == item.getBetOrderSource()).collect(Collectors.toList());
						} else {
							betList = null;
						}
						
						threadSettlement(vtableId, seatBetList,
								showResult, resultMap, table, BetOrderSourceEnum.SEAT, 
								userItemMoney, filterTradeItemIds);
						
						threadSettlementSidenote(vtableId, sideBetList,
						showResult, resultMap,
						table, BetOrderSourceEnum.SIDENOTE,
						userItemMoney, filterTradeItemIds);
						
					} catch (Throwable t) {
						log.error(t.getMessage(),t);
					} finally {
						countDownLatch.countDown();
						log.debug("虚拟桌结算完毕" + vtableId);
					}
				}
			});
		}
		//XXX 
//		BaccaratCache.freshVirtualTableList(vtableList);
		
		//2.处理多台下注
		normalTableThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					threadSettlement(null, berOrderList.stream().filter( item -> 
					BetOrderSourceEnum.MANY_TYPE == item.getBetOrderSource()).collect(Collectors.toList()), 
					showResult, resultMap, table,
					BetOrderSourceEnum.MANY_TYPE, new HashMap<>(),
					filterTradeItemIds);
				} catch (Throwable t) {
					log.error(t.getMessage(),t);
				} finally {
					countDownLatch.countDown();
					log.debug("多台结算完毕");
				}
			}
		});
		
		//3.处理好路结算
		normalTableThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					threadSettlement(null, berOrderList.stream().filter( item -> 
					BetOrderSourceEnum.GOOD_ROAD == item.getBetOrderSource()).collect(Collectors.toList()), 
					showResult, resultMap, table, BetOrderSourceEnum.GOOD_ROAD, new HashMap<>(),
					filterTradeItemIds);
				} catch (Throwable t) {
					log.error(t.getMessage(),t);
				} finally {
					countDownLatch.countDown();
					log.debug("好路结算完毕");
				}
			}
		});
		
		try {
			countDownLatch.await();
			log.debug("所有同步线程结束");
		} catch (InterruptedException e) {
			throw SocketException.createError("settlement.abnormal");
		}
	}
	
	/**
	 * 单桌旁注结算
	 * @param berOrderList   需要处理的订单列表
	 * @param showResult
	 * @param resultMap      计算的该局结果集
	 * @param table
	 */
	private void threadSettlementSidenote(Integer vtableId, List<BetOrder> berOrderList,
			String showResult, Map<PlayEnum,AbstractPlayRule> resultMap,
			GameTable table, BetOrderSourceEnum sourceEnum,
			Map<Long, List<UserBetResultResponse.ResultInfo>> seatUserItemMoney,
		  	final List<Integer> winTradeItemIds) {
		/**
		 * 用户赢钱额度 若用户未投注  集合则不存在该用户
		 * key  用户ID   value  用户赢得钱
		 */
		Map<Long,BigDecimal> userWinMoney = new HashMap<>();
		Map<Long, List<UserBetResultResponse.ResultInfo>> userItemMoney = new HashMap<>();
		
		String gameResult = resultMap.get(PlayEnum.NORMAL).getGameReuslt();
		//投注来源
		dealOrder(berOrderList, showResult, resultMap, table,
				userItemMoney, userWinMoney);
		
		Collection<User> sendUserList = new HashSet<>();
		if(berOrderList != null && !berOrderList.isEmpty()) {
			berOrderList.forEach( item -> {
	            User loginUser = BaccaratCache.getLoginUser(item.getUserId());
	            if(loginUser != null){
	            	if(BetOrderSourceEnum.MANY_TYPE == sourceEnum) {
	            		if(BaccaratCache.containMultipleUserMap(item.getUserId())) {
	            			sendUserList.add(loginUser);
	            		}
	            	} else if(BetOrderSourceEnum.SEAT == sourceEnum || BetOrderSourceEnum.SIDENOTE == sourceEnum) {
	            		if(loginUser.getUserPO().getTableId() != null 
	                    		&& table.getGameTablePO().getId().compareTo(loginUser.getUserPO().getTableId()) == 0) {
	            			sendUserList.add(loginUser);
	                	}
	            	} else if(BetOrderSourceEnum.GOOD_ROAD == sourceEnum) {
	            		sendUserList.add(loginUser);
	            	}
	            }
			});
		}
		
		if(BetOrderSourceEnum.SIDENOTE == sourceEnum) {
			Set<User> userSet = BaccaratCache.getVirtualTableUser(table.getGameTablePO().getId(), vtableId);
			if(userSet != null && !userSet.isEmpty()) {
				sendUserList.addAll(userSet.stream().filter( item -> {
					return item.getSource() == sourceEnum;
				}).collect(Collectors.toSet()));
			}
		}
		
		for(User user : sendUserList) {
			Map<Long, List<UserBetResultResponse.ResultInfo>> resultUserItemMoney = new HashMap<>();
			resultUserItemMoney.putAll(seatUserItemMoney);
			resultUserItemMoney.put(user.getUserPO().getId(), userItemMoney.get(user.getUserPO().getId()));
			
			log.debug("【百家乐结算】 发送结果通知，桌子：" + table.getGameTablePO().getName() + "，用户数量：" + sendUserList.size());
			sendUserMessage(table, user, userWinMoney, gameResult, resultUserItemMoney, sourceEnum, winTradeItemIds);
//			addSendGameResultTask(table, user, userWinMoney, gameResult, userItemMoney, sourceEnum);
		}
	}
	
	/**
	 * 单桌结算
	 * @param berOrderList   需要处理的订单列表
	 * @param showResult
	 * @param resultMap      计算的该局结果集
	 * @param table
	 */
	private void threadSettlement(Integer vtableId, List<BetOrder> berOrderList,
			String showResult, Map<PlayEnum,AbstractPlayRule> resultMap,
			GameTable table, BetOrderSourceEnum sourceEnum,
			Map<Long, List<UserBetResultResponse.ResultInfo>> userItemMoney,
		  	final List<Integer> winTradeItemIds) {
		/**
		 * 用户赢钱额度 若用户未投注  集合则不存在该用户
		 * key  用户ID   value  用户赢得钱
		 */
		Map<Long,BigDecimal> userWinMoney = new HashMap<>();
		
		String gameResult = resultMap.get(PlayEnum.NORMAL).getGameReuslt();
		//投注来源
		dealOrder(berOrderList, showResult, resultMap, table,
				userItemMoney, userWinMoney);
		
		Collection<User> sendUserList = new HashSet<>();
		if(berOrderList != null && !berOrderList.isEmpty()) {
			berOrderList.forEach( item -> {
	            User loginUser = BaccaratCache.getLoginUser(item.getUserId());
	            if(loginUser != null){
	            	if(BetOrderSourceEnum.MANY_TYPE == sourceEnum) {
	            		if(BaccaratCache.containMultipleUserMap(item.getUserId())) {
	            			sendUserList.add(loginUser);
	            		}
	            	} else if(BetOrderSourceEnum.SEAT == sourceEnum || BetOrderSourceEnum.SIDENOTE == sourceEnum) {
	            		if(loginUser.getUserPO().getTableId() != null 
	                    		&& table.getGameTablePO().getId().compareTo(loginUser.getUserPO().getTableId()) == 0) {
	            			sendUserList.add(loginUser);
	                	}
	            	} else if(BetOrderSourceEnum.GOOD_ROAD == sourceEnum) {
	            		sendUserList.add(loginUser);
	            	}
	            }
			});
		}
		
		if(BetOrderSourceEnum.MANY_TYPE == sourceEnum) {
			sendUserList.addAll(BaccaratCache.getMultipleUserMap().values());
		}
		if(BetOrderSourceEnum.SEAT == sourceEnum) {
			Set<User> userSet = BaccaratCache.getVirtualTableUser(table.getGameTablePO().getId(), vtableId);
			if(userSet != null && !userSet.isEmpty()) {
				sendUserList.addAll(userSet.stream().filter( item -> {
					return item.getSource() == BetOrderSourceEnum.SEAT;
				}).collect(Collectors.toSet()));
			}
		}
		
		log.debug("【百家乐结算】 发送结果通知，桌子：" + table.getGameTablePO().getName() + "，用户数量：" + sendUserList.size());
		for(User user : sendUserList) {
			sendUserMessage(table, user, userWinMoney, gameResult, userItemMoney, sourceEnum, winTradeItemIds);
//			addSendGameResultTask(table, user, userWinMoney, gameResult, userItemMoney, sourceEnum);
		}
	}
	
	/**
	 * 处理订单
	 */
	private void dealOrder(List<BetOrder> berOrderList, String showResult, Map<PlayEnum,AbstractPlayRule> resultMap,
			GameTable table, Map<Long, List<UserBetResultResponse.ResultInfo>> userItemMoney, 
			Map<Long,BigDecimal> userWinMoney) {
		
		List<UserBetResultResponse.ResultInfo> itemMoney = null;
		Set<UserPO> userPOList = new HashSet<>();
		List<AccountRecord> accountRecordList = new ArrayList<>();
		
		if(berOrderList != null && berOrderList.size() > 0) {
			JSONObject json = new JSONObject();
			json.put("tableId", table.getGameTablePO().getId());
			json.put("roundId", table.getRound().getRoundPO().getId());
			String accountRecordRemark = json.toJSONString();
			
			for(BetOrder order:berOrderList) {
				if(userItemMoney.containsKey(order.getUserId())) {
					itemMoney = userItemMoney.get(order.getUserId());
				} else {
					itemMoney = new ArrayList<>();
					userItemMoney.put(order.getUserId(), itemMoney);
				}
				
				settlement(order, table, resultMap, userWinMoney, userPOList, showResult, itemMoney, accountRecordList, accountRecordRemark);
			}
			//执行批量更新任务
			userService.batchUpdateBalance(new ArrayList<UserPO>(userPOList), "GameResult");
			userPOList.clear();
			//增加账户流水
			if(accountRecordList.size() > 0) {
				accountRecordService.batchAddAcountRecord(accountRecordList);
			}
			//批量更新订单信息
			betOrderService.batchUpdate(berOrderList);
		}
	}
	
	/**
	 * 发送用户通知
	 */
	private void sendUserMessage(GameTable table, User user, 
			Map<Long,BigDecimal> userWinMoney, String gameResult, 
			Map<Long, List<UserBetResultResponse.ResultInfo>> userItemMoney, BetOrderSourceEnum sourceEnum,
		 	final List<Integer> winTradeItemIds) {
		try {
			if (user == null) return;
			BigDecimal money = userWinMoney.get(user.getUserPO().getId());
			//获取到用户对应的客户端
			SocketIOClient userClient = SocketUtil.getClientByUser(socketIOServer, user.getUserPO());
			if (money == null && BetOrderSourceEnum.SEAT == sourceEnum) {
				if (user.getNotBetRoundNumber() != null) {
					Integer notBetCount = user.getNotBetRoundNumber() + 1;

					user.setNotBetRoundNumber(notBetCount);

					if (notBetCount == 3) {
						//发出警告
						UserMessageResponse userMessageResponse = new UserMessageResponse();
						userMessageResponse.setContent("请下注,五局未下注将自动踢出房间");
						roomCommand.send(userClient, RequestCommandEnum.CLIENT_USER_MESSAGE, userMessageResponse);
					} else if (notBetCount == 5) {
						try {
							//踢出桌子
							LeaveRoomPara para = new LeaveRoomPara();
							para.setForce(true);
							leaveRoomCommand.exec(userClient, para);
						} catch (Exception e) {
							log.error("", e);
						}
					}
				}
			}
			//不是连续的5局不能提
			if (money != null && BetOrderSourceEnum.SEAT == sourceEnum) {
				user.setNotBetRoundNumber(new Integer(0));
			}

			UserBetResultResponse userBetResultResponse = new UserBetResultResponse();
			userBetResultResponse.setTableId(table.getGameTablePO().getId());
			userBetResultResponse.setRid(table.getRound().getRoundPO().getId());
			userBetResultResponse.setMoney(money);
			userBetResultResponse.setResult(gameResult);
			userBetResultResponse.setSource(sourceEnum.get());
			List<UserBetResultResponse.CardInfo> cardList = new ArrayList<>();

			UserBetResultResponse.CardInfo cardInfo = null;
			Card[] cards = table.getCards();

			for (int i = 0; i < cards.length; i++) {
				if (cards[i] != null) {
					cardInfo = userBetResultResponse.new CardInfo();
					cardInfo.index = i;
					cardInfo.type = cards[i].getCardType();
					cardInfo.number = cards[i].getCardNum();

					cardList.add(cardInfo);
				}
			}
			userBetResultResponse.setCardList(cardList);

			//交易项结果信息
			List<UserBetResultResponse.UserInfo> userInfiList = new ArrayList<>();
			
			userItemMoney.forEach((k, v) -> {
				if(user.getUserPO().getId().compareTo(k) == 0) {
					UserBetResultResponse.UserInfo userInfo = userBetResultResponse.new UserInfo();
					List<UserBetResultResponse.ResultInfo> resultList = new ArrayList<>();
					int i = 0;
					for(UserBetResultResponse.ResultInfo item : v) {
						if(item.winNumber.setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(BigDecimal.ZERO) > 0) {
							i++;
						}
						UserBetResultResponse.ResultInfo resultInfo = userBetResultResponse.new ResultInfo();
						resultInfo.id = item.id;
						resultInfo.playId = item.playId;
						resultInfo.number = item.number;
						resultInfo.winNumber = item.winNumber;
						resultList.add(resultInfo);
					}
					if(i > 0) {
						resultList.sort((UserBetResultResponse.ResultInfo h1, UserBetResultResponse.ResultInfo h2) -> h2.winNumber.compareTo(h1.winNumber));
						userInfo.resultInfo = resultList;
					}
					userInfo.userId = k;
					userInfiList.add(userInfo);
				}
			});

			userBetResultResponse.setUserInfoList(userInfiList);
			userBetResultResponse.setTotalBetMoney(calUserBetTotalMoney(userItemMoney.get(user.getUserPO().getId())));
			userBetResultResponse.setShowTableStatus(table.getShowStatus());
			userBetResultResponse.setWinTradeItems(winTradeItemIds);
			
			//发送通知
			log.debug("【百家乐结算】 发送结果通知，桌子：" + table.getGameTablePO().getName() + "，用户：" + user.getUserPO().getLoginName());
			roomCommand.send(userClient, RequestCommandEnum.COMMON_GAME_RESULT, userBetResultResponse);
		}catch (Exception e){
			log.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 结算
	 */
	private void settlement(BetOrder order,GameTable table, 
			Map<PlayEnum,AbstractPlayRule> resultMap, Map<Long,BigDecimal> userWinMoney,
			Set<UserPO> userPOList, String showResult, List<UserBetResultResponse.ResultInfo> itemMoney, 
			List<AccountRecord> accountRecordList, String accountRecordRemark) {
		Round round = table.getRound();
		RoundExt roundExt = table.getRoundExt();
		
		User user = BaccaratCache.getLoginUser(order.getUserId());
		UserPO userPO = null;
		if(user != null) {
			userPO = user.getUserPO();
		}
		

		if(userPO == null) {
			userPO = AppCache.getLoginUser(order.getUserId());
			if(userPO == null){
                userPO = userService.getUserById(order.getUserId());
                log.debug("【结算】从数据库加载用户数据  ID:" + order.getUserId());
			}
		}
		
		log.debug("【结算】 用户:" + userPO.getId() + ",当前余额为：" + userPO.getBalance());
		UserPO dbUserPO = null;
		if(userPOList.contains(userPO)) {
			for(UserPO item : userPOList) {
				if(userPO.getId().compareTo(item.getId()) == 0) {
					dbUserPO = item;
					break;
				}
			}
		}
		
		if(dbUserPO == null) {
			dbUserPO = BeanUtil.cloneTo(userPO);
			dbUserPO.setBalance(BigDecimal.ZERO);
			((LiveUserPO)dbUserPO).setWinMoney(BigDecimal.ZERO);
			userPOList.add(dbUserPO);
		}
		
		
		AbstractPlayRule rule = null;
		for(Play item: table.getPlayList()) {
			if(item.getId() == order.getPlayId()) {
				if (resultMap.containsKey(order.getPlayEnum())) {
					rule = BeanUtil.cloneTo(resultMap.get(order.getPlayEnum()));
				} else {
					throw SocketException.createError("play.not.find");
				}
				break;
			}
		}
		if(rule == null) {
			throw SocketException.createError("order.not.find.play");
		}
		
		//结算
		rule.calResult(order, roundExt.getRoundExtPO());
		
		round.getRoundPO().setResult(rule.getGameReuslt());
		
		order.setRoundResult(showResult);
		order.setStatus(BetOrderStatusEnum.SETTLE.get());
		order.setWinLostStatusEnum(rule.getWinLostStatus());
		order.setWinLostAmount(rule.getResultAmount());
		order.setSettleRate(rule.getRate().add(new BigDecimal(1)));
		order.setSettleTime(new Date());
		if(BetOrderWinLostStatusEnum.TIE == order.getWinLostStatusEnum()) {
			order.setValidAmount(BigDecimal.ZERO);
		} else if(BetOrderWinLostStatusEnum.LOST == order.getWinLostStatusEnum()) {
			order.setValidAmount(order.getAmount());
		} else {
			if(order.getAmount().compareTo(order.getWinLostAmount()) >= 0) {
				order.setValidAmount(order.getAmount());
			} else {
				order.setValidAmount(order.getWinLostAmount());
			}
		}
		
		if(BetOrderWinLostStatusEnum.WIN == order.getWinLostStatusEnum() || BetOrderWinLostStatusEnum.TIE == order.getWinLostStatusEnum()) {
			AccountRecord accountRecord = new AccountRecord();
			accountRecord.setUserId(order.getUserId());
			accountRecord.setPreBalance(userPO.getBalance());
			accountRecord.setAmount(order.getAmount().add(order.getWinLostAmount()));
			if(BetOrderWinLostStatusEnum.WIN == order.getWinLostStatusEnum()) {
				accountRecord.setType(AccountRecordTypeEnum.RETURN_BONUS.get());
			} else  if(BetOrderWinLostStatusEnum.TIE == order.getWinLostStatusEnum()) {
				accountRecord.setType(AccountRecordTypeEnum.RETURN.get());
			}
			accountRecord.setTime(new Date());
			long id = accountRecordSnowflakeIdWorker.nextId();
			accountRecord.setId(id);
			accountRecord.setSn("ZR" + id);
			accountRecord.setExecUser(order.getLoginName());
			accountRecord.setBusinessKey(order.getId() + "");
			accountRecord.setRemark(accountRecordRemark);
			accountRecordList.add(accountRecord);
		}
		//增量加余额
		userPO.setBalance(userPO.getBalance().add(order.getAmount()).add(order.getWinLostAmount()));
		dbUserPO.setBalance(dbUserPO.getBalance().add(order.getAmount().add(order.getWinLostAmount())));
		BigDecimal winMoney = ((LiveUserPO)userPO).getWinMoney();
		if(winMoney == null) {
			winMoney = new BigDecimal(0);
		}
		if(rule.getResultAmount().compareTo(new BigDecimal(0)) > 0) {
			winMoney = winMoney.add(rule.getResultAmount());
		}
		((LiveUserPO)userPO).setWinMoney(winMoney);
		((LiveUserPO)dbUserPO).setWinMoney(((LiveUserPO)dbUserPO).getWinMoney().add(rule.getResultAmount()));
		
		/**
		 * 统计交易项金额
		 */
		UserBetResultResponse.ResultInfo resultInfo = itemMoney.stream().filter(item -> {
			return item.id.compareTo(order.getTradeItemId()) == 0;
		}).findAny().orElse(null);
		if(resultInfo != null) {
			resultInfo.number = resultInfo.number.add(order.getAmount());
			resultInfo.winNumber = resultInfo.winNumber.add(order.getWinLostAmount()).add(order.getAmount());
		} else {
			UserBetResultResponse rsp = new UserBetResultResponse();
			resultInfo = rsp.new ResultInfo();
			resultInfo.id = order.getTradeItemId();
			resultInfo.playId = order.getPlayId();
			resultInfo.number = order.getAmount();
			resultInfo.winNumber = order.getWinLostAmount().add(order.getAmount());
			itemMoney.add(resultInfo);
		}
		
		/**
		 * 统计用户输赢金额
		 */
		if(userWinMoney.containsKey(userPO.getId())) {
			userWinMoney.put(userPO.getId(), order.getWinLostAmount().add(userWinMoney.get(userPO.getId())));
		} else {
			userWinMoney.put(userPO.getId(), order.getWinLostAmount());
		}
	}

	@Override
	public void stopBet(CommandReqestPara params, SocketIOClient client,
			StopBetResponse response) {
		GameTable gameTable = BaccaratCache.getGameTableById(ConvertUtil.toInt(client.get(SocketClientStoreEnum.TABLE_ID.get())));
		Integer tableId = gameTable.getGameTablePO().getId();
		RoundPO roundPO = gameTable.getRound().getRoundPO();
		if (roundPO.getStatusEnum() == RoundStatusEnum.BETTING
				|| roundPO.getStatusEnum() == RoundStatusEnum.PAUSE) {
			
			gameTable.setInstantStateEnum(GameTableInstantStateEnum.AWAITING_RESULT);
			roundPO.setStatusEnum(RoundStatusEnum.AWAITING_RESULT);
			
			if(gameTable.getGameTablePO().isMiTable()) {
				dealBankPlayer(gameTable);
			}
			//dealAgentVIPTable(gameTable);
			
			//清空个人限红和台红检查
			gameTable.setLimitRule(null);
			BaccaratCache.getTableUserList(tableId).forEach( item -> {
				item.removeLimitRule(tableId);
			});
			
			roundService.update(gameTable.getRound().getRoundPO());
			response.setTableId(tableId);
			response.setShowTableStatus(gameTable.getShowStatus());
			tableCommand.sendAllTablePlayer(response, RequestCommandEnum.COMMOM_STOP_BET, tableId);
		} else {
			throw SocketException.createError("table.status.abnormal");
		}
	}
	
	/**
	 * 处理代理VIP桌对冲投注记录
	 */
	private void dealAgentVIPTable(GameTable gameTable) {
		RoundPO roundPO = gameTable.getRound().getRoundPO();
		
		//处理代理VIP桌的对冲投注
		List<VirtualGameTable> virtualTableList = BaccaratCache.getVirtualTableByTableId(gameTable.getGameTablePO().getId());
		List<VirtualGameTable> virtualList = virtualTableList.stream().filter( item -> {
			return item.getVirtualGameTablePO().getTypeEnum() == VirtualGameTableType.AGENT_VIP;
		}).collect(Collectors.toList());
		
		if(virtualList == null || virtualList.isEmpty()) {
			return;
		}
		
		List<TradeItem> normalList = AppCache.getTradeItemListByPlayId(PlayEnum.NORMAL.get());
		
		TradeItem bankerTradeItem = normalList.stream().filter( item -> {
			return item.getTradeItemEnum() == TradeItemEnum.BANKER;
		}).findFirst().get();
		
		TradeItem playerTradeItem = normalList.stream().filter( item -> {
			return item.getTradeItemEnum() == TradeItemEnum.PLAYER;
		}).findFirst().get();
		
		//算出对冲金额和需要的投注项
		final List<BetOrder> berOrderList = betOrderService.getBetOrderByTable(gameTable.getGameTablePO().getId(), BetOrderBetTypeEnum.COMMON, gameTable.getRound().getRoundPO());
		
		if(berOrderList == null || berOrderList.isEmpty()) {
			return ;
		}
		
		List<BetOrder> betOrderList = new ArrayList<>(virtualList.size());
		
		virtualList.parallelStream().forEach( item -> {
			VirtualGameTablePO virtualGameTablePO = item.getVirtualGameTablePO();
			//获取房主
			LiveUserPO userPO = userService.getUserById(virtualGameTablePO.getOwnerId());
			
			List<BetOrder> nowBetOrderList = berOrderList.stream().filter( orderItem -> {
				if(orderItem.getVirtualgametableId() != null) {
					return virtualGameTablePO.getId().compareTo(orderItem.getVirtualgametableId()) == 0;
				}
				return false;
			}).collect(Collectors.toList());
			
			BigDecimal bankerTotal = BigDecimal.ZERO;
			BigDecimal playerTotal = BigDecimal.ZERO;
			for(BetOrder orderItem : nowBetOrderList) {
				if(orderItem.getTradeItemKeyEnum() == TradeItemEnum.BANKER) {
					bankerTotal = bankerTotal.add(orderItem.getAmount());
				} else if(orderItem.getTradeItemKeyEnum() == TradeItemEnum.PLAYER) {
					playerTotal = playerTotal.add(orderItem.getAmount());
				}
			}
			
			TradeItem tradeItem;
			if(bankerTotal.compareTo(playerTotal) > 0) {
				tradeItem = bankerTradeItem;
			} else if (bankerTotal.compareTo(playerTotal) < 0) {
				tradeItem = playerTradeItem;
			} else {
				return ;
			}
			
			BigDecimal amount = bankerTotal.subtract(playerTotal).abs();
			
			BetOrder betOrder = new BetOrder();
            betOrder.setBootsId(roundPO.getBootId());
            betOrder.setGameId(roundPO.getGameId());
            betOrder.setGameTableId(roundPO.getGameTableId());
            betOrder.setBetTime(new Date());
            betOrder.setAmount(amount);
            betOrder.setSourceEnum(BetOrderSourceEnum.SEAT);
            betOrder.setBetRate(tradeItem.getRate());
            betOrder.setBetTypeEnum(BetOrderBetTypeEnum.AGENT_VIP);
            betOrder.setBootsNum(roundPO.getBootNumber());
            betOrder.setRoundId(roundPO.getId());
            betOrder.setRoundNum(roundPO.getRoundNumber());
            betOrder.setLoginName(userPO.getLoginName());
            betOrder.setUserId(userPO.getId());
            betOrder.setParentId(userPO.getParentId());
            betOrder.setUserParentPath(userPO.getParentPath());
            betOrder.setUserPreBalance(userPO.getBalance());
            betOrder.setTableType(virtualGameTablePO.getType());
            betOrder.setTradeItemId(tradeItem.getId());
            betOrder.setTradeItemKey(tradeItem.getKey());
            betOrder.setPlayId(tradeItem.getPlayId());
            betOrder.setoPercentage(BigDecimal.ZERO);
            betOrder.setWaterPercentage(BigDecimal.ZERO);
            betOrder.setWashPercentage(userPO.getWashPercentage());
            betOrder.setStatus(BetOrderStatusEnum.CONFIRM.get());
			betOrder.setVirtualgametableId(item.getVirtualGameTablePO().getId());
			betOrder.setTableTypeEnum(BetOrderTableTypeEnum.VIP);
			
			betOrderList.add(betOrder);
			userService.updateUserBalance(userPO.getId(),BigDecimal.ZERO.subtract(amount), "Bet");
		});
		
		//批量添加订单
		if(!betOrderList.isEmpty()) {
			betOrderService.batchAddBetOrders(betOrderList);
		}
	}
	
	/**
	 * 普通虚拟桌处理本张桌子庄和闲
	 * 规则：人人可咪(同时投庄闲不可咪)
	 * @param gameTable
	 */
	private void dealBankPlayer(GameTable gameTable) {
		Set<User> gameTableUsers = BaccaratCache.getTableUserList(gameTable.getGameTablePO().getId());
		List<BetOrder> betOrders = betOrderService.getBetOrderByRoundNoTradeItem(gameTable.getRound().getRoundPO());
		Set<User> bankers = new HashSet<>();
		Set<User> players = new HashSet<>();
		for (BetOrder betOrder : betOrders) {
			if(TradeItemEnum.BANKER != betOrder.getTradeItemKeyEnum() && TradeItemEnum.PLAYER != betOrder.getTradeItemKeyEnum())
				continue;
			for (User player : gameTableUsers) {
				if(!betOrder.getUserId().equals(player.getUserPO().getId()))
					continue;
				if(TradeItemEnum.BANKER == betOrder.getTradeItemKeyEnum()){
					bankers.add(player);
					continue;
				}
				if(TradeItemEnum.PLAYER == betOrder.getTradeItemKeyEnum()){
					players.add(player);
				}
			}
		}
		
		Set<User> result = new HashSet<User>();
		result.addAll(bankers);
		bankers.removeAll(players);
		players.removeAll(result);
		gameTable.getBankPlayerMap().put(TradeItemEnum.BANKER, new HashSet<>(bankers));
		gameTable.getBankPlayerMap().put(TradeItemEnum.PLAYER, new HashSet<>(players));
		
	}

	@Override
	public void setCard(SetCardPara params, SocketIOClient client,
			SetCardResponse response) {
		GameTable table = BaccaratCache.getGameTableById(ConvertUtil.toInt(client.get(SocketClientStoreEnum.TABLE_ID.get())));
		if(table == null) {
			throw SocketException.createError("table.not.exist");
		}
		
		if(table.getRound().getRoundPO().getStatusEnum() != RoundStatusEnum.AWAITING_RESULT && table.getRound().getRoundPO().getStatusEnum() != RoundStatusEnum.PAUSE) {
			throw SocketException.createError("table.status.abnormal");
		}
		response.setRoundId(table.getRound().getRoundPO().getId());
		
		if (params.getCardNum() != null && params.getCardType() != null && params.getIndex() != null) {
			
			table.setCard(params.getCardType(), params.getCardNum(), params.getIndex());
			
			Map<TradeItemEnum,Set<User>> betPlayerMap = table.getBankPlayerMap();
			
			RoundExt roundExt = table.getRoundExt();
			RoundExtPO roundExtPO = roundExt.getRoundExtPO();
			
			int flag;
			synchronized(this) {
				flag = 0;
				if((roundExtPO.getPlayerCard1Number() != null && roundExtPO.getPlayerCard2Number() != null
						&& table.getCards()[0] != null && !table.getCards()[0].getOpenStatus() 
						&& table.getCards()[1] != null && !table.getCards()[1].getOpenStatus())
					&& (roundExtPO.getBankCard1Number() != null && roundExtPO.getBankCard2Number() != null
						&& table.getCards()[2] != null && !table.getCards()[2].getOpenStatus() 
						&& table.getCards()[3] != null && !table.getCards()[3].getOpenStatus())
						&& !roundExt.hasOpenCards(1)) {
					flag = 1;
				}
				if(roundExtPO.getPlayerCard3Number() != null
						&& table.getCards()[4] != null && !table.getCards()[4].getOpenStatus() 
						&& !roundExt.hasOpenCards(2) ) {
					flag = 2;
				}
				if(roundExtPO.getBankCard3Number() != null
						&& table.getCards()[5] != null && !table.getCards()[5].getOpenStatus() 
						&& !roundExt.hasOpenCards(3) ) {
					flag = 3;
				}
				log.debug("【开牌】标志： " + flag);
			}
			
			if(flag != 0) {
				roundExt.addOpenCard(flag);
				//二张二张咪牌
				handOutCard(flag, table, roundExtPO, client, betPlayerMap);
				//一张一张咪牌
//				handOutCardOneByOne(flag, table, roundExtPO, client, betPlayerMap);
			}
		}		
	}
	
	public void selectRoom(SelectRoomPara params, SocketIOClient client,
			SelectRoomResponse response) {
		baccaratClassHandler.roomAction(params.getTypeEnum()).selectRoom(params, client, response);
	}
	
	/**
	 * 分发卡片  让用户咪牌
	 * @param type   1表示闲前2张和庄闲前2张  2表示闲第3张  3表示庄第3张
	 * @param table
	 * @param roundExtPO
	 * @param dealerClient
	 */
	private void handOutCardOneByOne(Integer type, GameTable table, RoundExtPO roundExtPO,  SocketIOClient dealerClient , Map<TradeItemEnum,Set<User>> betPlayerMap) {
		//获取咪牌倒计时
		GamePO gamePO = AppCache.getGame(table.getGameTablePO().getGameId());
		int countDownSecond = table.getGameTablePO().getMiCountDownSeconds();
		
		List<UserCardResponse.CardInfo> cardList = new ArrayList<>();
		List<UserCardResponse.CardInfo> bankTaskCardList = new ArrayList<>();
		List<UserCardResponse.CardInfo> playTaskCardList = new ArrayList<>();
		Set<UserCardResponse.CardInfo> dealerCardList = new HashSet<>();
		Set<User> bankTaskNoticeUserList = new HashSet<>();
		Set<User> playTaskNoticeUserList = new HashSet<>();
		UserCardResponse userMiCardResponse = new UserCardResponse();
		UserCardResponse.CardInfo cardInfo;
		boolean taskFlag = false;
		int delaySecond = 0;
		switch (type) {
			case 1:
				//获取延时发送秒数
				delaySecond = ConvertUtil.toInt(gamePO.getGameConfig().get("twodelayTime"), 5);
				
				List<UserCardResponse.CardInfo> bankTaskCardList1 = new ArrayList<>();
				List<UserCardResponse.CardInfo> playTaskCardList1 = new ArrayList<>();
				List<UserCardResponse.CardInfo> bankTaskCardList2 = new ArrayList<>();
				List<UserCardResponse.CardInfo> playTaskCardList2 = new ArrayList<>();
				cardInfo = userMiCardResponse.new CardInfo();
				cardInfo.number = roundExtPO.getPlayerCard1Number();
				cardInfo.type = roundExtPO.getPlayerCard1Mode();
				cardInfo.index = 0;
				cardList.add(cardInfo);
				cardInfo = userMiCardResponse.new CardInfo();
				cardInfo.number = roundExtPO.getPlayerCard2Number();
				cardInfo.type = roundExtPO.getPlayerCard2Mode();
				cardInfo.index = 1;
				cardList.add(cardInfo);
				handOutUser(TradeItemEnum.PLAYER, table, betPlayerMap, 
						cardList, playTaskCardList, countDownSecond, 
						dealerClient, dealerCardList, playTaskNoticeUserList, delaySecond);
				
				cardInfo = userMiCardResponse.new CardInfo();
				cardInfo.number = roundExtPO.getBankCard1Number();
				cardInfo.type = roundExtPO.getBankCard1Mode();
				cardInfo.index = 2;
				cardList.add(cardInfo);
				cardInfo = userMiCardResponse.new CardInfo();
				cardInfo.number = roundExtPO.getBankCard2Number();
				cardInfo.type = roundExtPO.getBankCard2Mode();
				cardInfo.index = 3;
				cardList.add(cardInfo);
				handOutUser(TradeItemEnum.BANKER, table, betPlayerMap, 
						cardList, bankTaskCardList, countDownSecond, 
						dealerClient, dealerCardList, bankTaskNoticeUserList, delaySecond);
				
				if(dealerCardList != null && dealerCardList.size() > 0) {
					noticeDealer(dealerClient, dealerCardList, table);
				}
				
				playTaskCardList.forEach( item -> {
					if(item.index == 0) {
						playTaskCardList1.add(item);
					} else if(item.index == 1) {
						playTaskCardList2.add(item);
					}
				});
				bankTaskCardList.forEach( item -> {
					if(item.index == 2) {
						bankTaskCardList1.add(item);
					} else if(item.index == 3) {
						bankTaskCardList2.add(item);
					}
				});
				
				if(playTaskCardList != null && playTaskCardList.size() > 0 ||
						bankTaskCardList != null && bankTaskCardList.size() > 0) {
					//启动咪牌倒计时线程
					Map<String,Object> dataMap = new HashMap<>();
					dataMap.put("bankCardList", bankTaskCardList1);
					dataMap.put("playCardList", playTaskCardList1);
					dataMap.put("dealerClient", dealerClient);
					dataMap.put("bankUserList", bankTaskNoticeUserList);
					dataMap.put("playUserList", playTaskNoticeUserList);
					dataMap.put("table", table);
					
					QuartzConfig.addJob(schedulerFactoryBean, MiCardCountDownTask.class, 
							MiCardCountDownTask.class.getName() + table.getGameTablePO().getId() + 0, 
							DateUtil.nextSecondDate(null, countDownSecond), dataMap);
					
					dataMap = new HashMap<>();
					dataMap.put("bankCardList", bankTaskCardList2);
					dataMap.put("playCardList", playTaskCardList2);
					dataMap.put("dealerClient", dealerClient);
					dataMap.put("bankUserList", bankTaskNoticeUserList);
					dataMap.put("playUserList", playTaskNoticeUserList);
					dataMap.put("table", table);
					
					QuartzConfig.addJob(schedulerFactoryBean, MiCardCountDownTask.class, 
							MiCardCountDownTask.class.getName() + table.getGameTablePO().getId() + 1, 
							DateUtil.nextSecondDate(null, countDownSecond * 2), dataMap);
				}
				
				taskFlag = false;
				break;
			case 2:
				//获取延时发送秒数
				delaySecond = ConvertUtil.toInt(gamePO.getGameConfig().get("threedelayTime"), 5);
				
				cardInfo = userMiCardResponse.new CardInfo();
				cardInfo.number = roundExtPO.getPlayerCard3Number();
				cardInfo.type = roundExtPO.getPlayerCard3Mode();
				cardInfo.index = 4;
				cardList.add(cardInfo);
				handOutUser(TradeItemEnum.PLAYER, table, betPlayerMap, 
						cardList, playTaskCardList, countDownSecond, 
						dealerClient, dealerCardList, playTaskNoticeUserList, delaySecond);
				
				if(dealerCardList != null && dealerCardList.size() > 0) {
					noticeDealer(dealerClient, dealerCardList, table);
				}
				if(playTaskCardList != null && playTaskCardList.size() > 0) {
					taskFlag = true;
				}
				break;
			case 3:
				//获取延时发送秒数
				delaySecond = ConvertUtil.toInt(gamePO.getGameConfig().get("threedelayTime"), 5);
				
				cardInfo = userMiCardResponse.new CardInfo();
				cardInfo.number = roundExtPO.getBankCard3Number();
				cardInfo.type = roundExtPO.getBankCard3Mode();
				cardInfo.index = 5;
				cardList.add(cardInfo);
				handOutUser(TradeItemEnum.BANKER, table, betPlayerMap, 
						cardList, bankTaskCardList, countDownSecond, 
						dealerClient, dealerCardList, bankTaskNoticeUserList, delaySecond);
				
				if(dealerCardList != null && dealerCardList.size() > 0) {
					noticeDealer(dealerClient, dealerCardList, table);
				}
				if(bankTaskCardList != null && bankTaskCardList.size() > 0) {
					taskFlag = true;
				}
				break;
		}
		
		if(taskFlag) {
			//启动咪牌倒计时线程
			Map<String,Object> dataMap = new HashMap<>();
			dataMap.put("bankCardList", bankTaskCardList);
			dataMap.put("playCardList", playTaskCardList);
			dataMap.put("dealerClient", dealerClient);
			dataMap.put("bankUserList", bankTaskNoticeUserList);
			dataMap.put("playUserList", playTaskNoticeUserList);
			dataMap.put("table", table);
			
			QuartzConfig.addJob(schedulerFactoryBean, MiCardCountDownTask.class, 
					MiCardCountDownTask.class.getName() + table.getGameTablePO().getId() + type, 
					DateUtil.nextSecondDate(null, countDownSecond), dataMap);
		}
	}
	
	/**
	 * 分发卡片  让用户咪牌
	 * @param type   1表示闲前2张和庄闲前2张  2表示闲第3张  3表示庄第3张
	 * @param table
	 * @param roundExtPO
	 * @param dealerClient
	 */
	private void handOutCard(Integer type, GameTable table, RoundExtPO roundExtPO,  SocketIOClient dealerClient , Map<TradeItemEnum,Set<User>> betPlayerMap) {
		GamePO gamePO = AppCache.getGame(table.getGameTablePO().getGameId());
		//获取咪牌倒计时
		int countDownSecond = table.getGameTablePO().getMiCountDownSeconds();
		
		List<UserCardResponse.CardInfo> cardList = new ArrayList<>();
		List<UserCardResponse.CardInfo> bankTaskCardList = new ArrayList<>();
		List<UserCardResponse.CardInfo> playTaskCardList = new ArrayList<>();
		Set<UserCardResponse.CardInfo> dealerCardList = new HashSet<>();
		Set<User> bankTaskNoticeUserList = new HashSet<>();
		Set<User> playTaskNoticeUserList = new HashSet<>();
		UserCardResponse userMiCardResponse = new UserCardResponse();
		UserCardResponse.CardInfo cardInfo;
		boolean taskFlag = false;
		int delaySecond = 0;
		int taskSecond = countDownSecond;
		switch (type) {
			case 1:
				//获取延时发送秒数
				delaySecond = ConvertUtil.toInt(gamePO.getGameConfig().get("twodelayTime"), 5);
				
				cardInfo = userMiCardResponse.new CardInfo();
				cardInfo.number = roundExtPO.getPlayerCard1Number();
				cardInfo.type = roundExtPO.getPlayerCard1Mode();
				cardInfo.index = 0;
				cardList.add(cardInfo);
				cardInfo = userMiCardResponse.new CardInfo();
				cardInfo.number = roundExtPO.getPlayerCard2Number();
				cardInfo.type = roundExtPO.getPlayerCard2Mode();
				cardInfo.index = 1;
				cardList.add(cardInfo);
				handOutUser(TradeItemEnum.PLAYER, table, betPlayerMap, 
						new ArrayList<>(cardList), playTaskCardList, countDownSecond * 2, 
						dealerClient, dealerCardList, playTaskNoticeUserList, delaySecond);
				
				cardList = new ArrayList<>();
				cardInfo = userMiCardResponse.new CardInfo();
				cardInfo.number = roundExtPO.getBankCard1Number();
				cardInfo.type = roundExtPO.getBankCard1Mode();
				cardInfo.index = 2;
				cardList.add(cardInfo);
				cardInfo = userMiCardResponse.new CardInfo();
				cardInfo.number = roundExtPO.getBankCard2Number();
				cardInfo.type = roundExtPO.getBankCard2Mode();
				cardInfo.index = 3;
				cardList.add(cardInfo);
				handOutUser(TradeItemEnum.BANKER, table, betPlayerMap, 
						new ArrayList<>(cardList), bankTaskCardList, countDownSecond * 2, 
						dealerClient, dealerCardList, bankTaskNoticeUserList, delaySecond);
				
				if(dealerCardList != null && dealerCardList.size() > 0) {
					noticeDealer(dealerClient, dealerCardList, table);
				}
				if(playTaskCardList != null && playTaskCardList.size() > 0 ||
						bankTaskCardList != null && bankTaskCardList.size() > 0) {
					taskSecond = taskSecond * 2;
					taskFlag = true;
				}
				break;
			case 2:
				//获取延时发送秒数
				delaySecond = ConvertUtil.toInt(gamePO.getGameConfig().get("threedelayTime"), 5);
				
				cardInfo = userMiCardResponse.new CardInfo();
				cardInfo.number = roundExtPO.getPlayerCard3Number();
				cardInfo.type = roundExtPO.getPlayerCard3Mode();
				cardInfo.index = 4;
				cardList.add(cardInfo);
				handOutUser(TradeItemEnum.PLAYER, table, betPlayerMap, 
						cardList, playTaskCardList, countDownSecond, 
						dealerClient, dealerCardList, playTaskNoticeUserList, delaySecond);
				
				if(dealerCardList != null && dealerCardList.size() > 0) {
					noticeDealer(dealerClient, dealerCardList, table);
				}
				if(playTaskCardList != null && playTaskCardList.size() > 0) {
					taskFlag = true;
				}
				break;
			case 3:
				//获取延时发送秒数
				delaySecond = ConvertUtil.toInt(gamePO.getGameConfig().get("threedelayTime"), 5);
				
				cardInfo = userMiCardResponse.new CardInfo();
				cardInfo.number = roundExtPO.getBankCard3Number();
				cardInfo.type = roundExtPO.getBankCard3Mode();
				cardInfo.index = 5;
				cardList.add(cardInfo);
				handOutUser(TradeItemEnum.BANKER, table, betPlayerMap, 
						cardList, bankTaskCardList, countDownSecond, 
						dealerClient, dealerCardList, bankTaskNoticeUserList, delaySecond);
				
				if(dealerCardList != null && dealerCardList.size() > 0) {
					noticeDealer(dealerClient, dealerCardList, table);
				}
				if(bankTaskCardList != null && bankTaskCardList.size() > 0) {
					taskFlag = true;
				}
				break;
		}
		
		if(taskFlag) {
			//启动咪牌倒计时线程
			Map<String,Object> dataMap = new HashMap<>();
			dataMap.put("bankCardList", bankTaskCardList);
			dataMap.put("playCardList", playTaskCardList);
			dataMap.put("dealerClient", dealerClient);
			dataMap.put("bankUserList", bankTaskNoticeUserList);
			dataMap.put("playUserList", playTaskNoticeUserList);
			dataMap.put("table", table);
			
			QuartzConfig.addJob(schedulerFactoryBean, MiCardCountDownTask.class, 
					MiCardCountDownTask.class.getName() + table.getGameTablePO().getId() + type, 
					DateUtil.nextSecondDate(null, taskSecond), dataMap);
		}
	}
	
	/**
	 * 分发需要发送给哪些Client
	 */
	private void handOutUser(TradeItemEnum tradeItemEnum, GameTable table, Map<TradeItemEnum,Set<User>> betPlayerMap, 
			List<UserCardResponse.CardInfo> cardList, List<UserCardResponse.CardInfo> taskCardList, 
			Integer countDownSecond, SocketIOClient dealerClient, Set<UserCardResponse.CardInfo> dealerCardList,
			Collection<User> taskNoticeUserList, Integer delaySecond) {
		if(betPlayerMap == null || betPlayerMap.size() < 1) {
			dealerCardList.addAll(cardList);
		}
		Set<User> miUserList = betPlayerMap.get(tradeItemEnum);
		if(miUserList != null && miUserList.size() > 0) {
			noticeMiCardUser(table.getGameTablePO().getId(), cardList, miUserList, 
					countDownSecond, tradeItemEnum, dealerClient, delaySecond);
			table.setCountDownSeconds(countDownSecond + delaySecond);
			table.setCoundDownDate(new Date());
			taskCardList.addAll(cardList);
//			taskNoticeUserList.addAll(miUserList);
			//发送给未下注用户  开牌
			noticeNotMiCardUser(table.getGameTablePO().getId(), false, cardList, miUserList, countDownSecond, delaySecond);
			taskNoticeUserList.addAll(miUserList);
		} else {
			dealerCardList.addAll(cardList);
			noticeNotMiCardUser(table.getGameTablePO().getId(), true, cardList, new HashSet<>(), countDownSecond, delaySecond);
		}
	}
	
	/**
	 * 如果没有用户下注通知荷官开牌
	 */
	private void noticeDealer(SocketIOClient dealerClient, Set<UserCardResponse.CardInfo> cardList, GameTable table) {
		cardList.forEach(item -> {
			table.getCards()[item.index].setOpenStatus(true);
		});
		//通知荷官开牌
		SendDealerResponse sendResponse = new SendDealerResponse();
		Set<Integer> indexList = new HashSet<>();
		for(UserCardResponse.CardInfo cardInfo : cardList) {
			indexList.add(cardInfo.index);
		}
		
		sendResponse.setCommand("fanpai");
		sendResponse.setCardList(indexList);
		dealerCommand.sendMessage(dealerClient, sendResponse);
	}
	
	/**
	 * 通知不咪牌用户
	 */
	private void noticeNotMiCardUser(Integer tableId, boolean isFanPai, List<UserCardResponse.CardInfo> cardList,
			Set<User> miUserList, int countDownSecond, Integer delaySecond) {
		//通知用户开牌
		UserCardResponse userCardResponse = new UserCardResponse();
		if (isFanPai) {
			userCardResponse.setCardList(cardList);
		} else {
			List<UserCardResponse.CardInfo> tempCardList2 = BeanUtil.cloneTo(cardList);
			tempCardList2.forEach(item -> {
				item.number = null;
				item.type = null;
			});
			userCardResponse.setCardList(tempCardList2);
			userCardResponse.setCountDownSecond(countDownSecond);
		}
		userCardResponse.setIsMiCard(false);
		userCardResponse.setTableId(tableId);
		
		new Thread( new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(delaySecond * 1000);
					Set<User> allTableUserList = BaccaratCache.getTableUserList(tableId);
					Set<User> notMiUserList = allTableUserList.stream().filter( item -> {
						return !miUserList.contains(item);
					}).collect(Collectors.toSet());
					Collection<SocketIOClient> clients = SocketUtil.getClientByUserList(socketIOServer, notMiUserList);
					for(SocketIOClient item : clients) {
						roomCommand.send(item, RequestCommandEnum.CLIENT_USER_CARD, userCardResponse);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	/**
	 * 发送需要咪牌的用户
	 */
	private void noticeMiCardUser(Integer tableId,List<UserCardResponse.CardInfo> cardList,
			Set<User> userList, int countDownSecond,TradeItemEnum tradeItemEnum,
			SocketIOClient dealerClient, Integer delaySecond) {
		//通知用户咪牌
		UserCardResponse userCardResponse = new UserCardResponse();
		userCardResponse.setCardList(cardList);
		userCardResponse.setIsMiCard(true);
		userCardResponse.setCountDownSecond(countDownSecond);
		userCardResponse.setMiType(tradeItemEnum.get());
		userCardResponse.setTableId(tableId);
		//TODO 表示单台开牌
		userCardResponse.setCardType(1);
		
		new Thread( new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(delaySecond * 1000);
					Collection<SocketIOClient> clients = SocketUtil.getClientByUserList(socketIOServer, userList);
					for(SocketIOClient item : clients) {
						roomCommand.send(item, RequestCommandEnum.CLIENT_USER_CARD, userCardResponse);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		//通知荷官咪牌倒计时
		SendDealerResponse sendDealerResponse = new SendDealerResponse();
		sendDealerResponse.setCommand("miCD");
		sendDealerResponse.setMiCoundDown(countDownSecond);
		dealerCommand.sendMessage(dealerClient, sendDealerResponse);
	}

	@Override
	public void bet(BetPara params, SocketIOClient client, BetResponse response) {
		com.na.baccarat.socketserver.entity.User user = BaccaratCache.getUserByClient(client);
		LiveUserPO userPO = (LiveUserPO) user.getUserPO();
		
		Integer tableId = params.getTableId();
		GameTable gameTable = BaccaratCache.getGameTableById(tableId);
		if(gameTable == null) {
			throw SocketException.createError("table.not.exist");
		}
		
        SocketException.isTrue(gameTable.getRound().getRoundPO().getStatusEnum() != RoundStatusEnum.BETTING,"bet.already.stop");
		
		checkBetTimeout(gameTable);
		
		checkTableLimit(params, user, gameTable);
		
		RoundPO roundPO = gameTable.getRound().getRoundPO();
		
		VirtualGameTable virtualGameTable = null;
		if(BetOrderSourceEnum.SEAT == params.getSourceEnum() || 
				BetOrderSourceEnum.SIDENOTE == params.getSourceEnum()) {
			virtualGameTable = BaccaratCache.getVirtualTableById(user.getVirtualTableId());
	    	if(virtualGameTable == null) {
	    		throw SocketException.createError("table.not.exist");
	    	}
		}
    	

        BigDecimal total = BigDecimal.ZERO;
        response.setUserId(userPO.getId());
        response.setTid(params.getTableId());
        response.setSource(params.getSource());
        List<BetOrder> betOrderList = new ArrayList<>(params.getBets().size());
        BetOrder betOrder;
        for(BetPara.Bet bet : params.getBets()) {
            TradeItem tradeItem = gameTable.getTradeItemById(bet.tradeId);
            
            betOrder = new BetOrder();
            betOrder.setBetIp(((InetSocketAddress)client.getRemoteAddress()).getAddress().getHostAddress());
            betOrder.setDeviceType(1);
            betOrder.setDeviceType(userPO.getDeviceType());
            betOrder.setDeviceInfo(userPO.getDeviceInfo());
            betOrder.setBootsId(roundPO.getBootId());
            betOrder.setGameId(roundPO.getGameId());
            betOrder.setGameTableId(roundPO.getGameTableId());
            betOrder.setBetTime(new Date());
            betOrder.setAmount(bet.amount);
            betOrder.setBetRate(tradeItem.getRate());
            betOrder.setBetType(params.getBetType());
            betOrder.setBootsNum(roundPO.getBootNumber());
            betOrder.setRoundId(roundPO.getId());
            betOrder.setRoundNum(roundPO.getRoundNumber());
            betOrder.setLoginName(userPO.getLoginName());
            betOrder.setUserId(userPO.getId());
            betOrder.setParentId(userPO.getParentId());
            betOrder.setUserParentPath(userPO.getParentPath());
            betOrder.setUserPreBalance(userPO.getBalance());
            betOrder.setSource(params.getSource());
            if(params.getSourceEnum() == BetOrderSourceEnum.SEAT) {
                betOrder.setSourceValue(user.getSeat()+"");
            }
            if(virtualGameTable != null) {
            	betOrder.setTableType(virtualGameTable.getVirtualGameTablePO().getType());
            }
            
            if(virtualGameTable == null || virtualGameTable.getVirtualGameTablePO().getTypeEnum() == VirtualGameTableType.COMMON) {
            	betOrder.setTableTypeEnum(BetOrderTableTypeEnum.COMMON);
            } else if(virtualGameTable.getVirtualGameTablePO().getTypeEnum() == VirtualGameTableType.AGENT_VIP) {
            	betOrder.setTableTypeEnum(BetOrderTableTypeEnum.VIP);
            }
            betOrder.setTradeItemId(tradeItem.getId());
            betOrder.setTradeItemKey(tradeItem.getKey());
            betOrder.setPlayId(tradeItem.getPlayId());
            betOrder.setoPercentage(userPO.getIntoPercentage());
            betOrder.setWashPercentage(userPO.getWashPercentage());
            betOrder.setStatus(BetOrderStatusEnum.CONFIRM.get());
            
            if(BetOrderSourceEnum.SEAT == params.getSourceEnum() || 
    				BetOrderSourceEnum.SIDENOTE == params.getSourceEnum()) {
            	betOrder.setVirtualgametableId(virtualGameTable.getVirtualGameTablePO().getId());
    			
    			if(VirtualGameTableType.AGENT_VIP == virtualGameTable.getVirtualGameTablePO().getTypeEnum()) {
    				if(TradeItemEnum.BANKER == tradeItem.getTradeItemEnum() ||
    						TradeItemEnum.PLAYER == tradeItem.getTradeItemEnum()) {
    					betOrder.setoPercentage(virtualGameTable.getVirtualGameTablePO().getIntoPercentage());
    				} else {
    					betOrder.setoPercentage(virtualGameTable.getVirtualGameTablePO().getNoHedgePercentage());
    				}
    				betOrder.setWaterPercentage(virtualGameTable.getVirtualGameTablePO().getWaterPercentage());
    			}
            }
    		
    		betOrderList.add(betOrder);

            userPO.setBalance(userPO.getBalance().subtract(betOrder.getAmount()));

            total = total.add(betOrder.getAmount());
            
        	//用户投注细节
        	UserBet userBetDetail = new UserBet();
            userBetDetail.setUserId(userPO.getId());
            userBetDetail.setTableId(params.getTableId());
            if(virtualGameTable != null) {
            	userBetDetail.setVirtualTableId(virtualGameTable.getVirtualGameTablePO().getId());
            }
            userBetDetail.setAmount(betOrder.getAmount());
            userBetDetail.setSource(params.getSourceEnum());
            userBetDetail.setTradeId(tradeItem.getId());
            gameTable.getRound().addBeted(userBetDetail);
            
            response.addTradeItemId(betOrder.getTradeItemId());
        }
        
        //批量添加订单
        betOrderService.batchAddBetOrders(userPO,betOrderList, gameTable.getGameTablePO().getId(), roundPO.getId());
        
        userService.updateUserBalance(userPO.getId(),BigDecimal.ZERO.subtract(total), "Bet");
        
        if(BetOrderSourceEnum.SEAT == params.getSourceEnum()) {
        	BaccaratCache.freshVirtualTable(virtualGameTable);
        	sendTableBetSuccess(client, user, BaccaratCache.getUserList(BaccaratCache.getVirtualTableSeatUser(virtualGameTable.getVirtualGameTablePO().getGameTableId(), virtualGameTable.getVirtualGameTablePO().getId()).values()), params, total);
        }
	}

	@Override
	public void cutCard(CutCardPara params, SocketIOClient client,
			CutCardResponse response) {
		
	}

//	private void addSendGameResultTask(GameTable table,
//								  User user,
//								  Map<Long,BigDecimal> userWinMoney,
//								  String gameResult,
//								  Map<Long, Map<Integer, BigDecimal>> userItemMoney,
//								  BetOrderSourceEnum sourceEnum){
//		String timeStr = AppCache.getGame(GameEnum.BACCARAT).getGameConfig().getOrDefault("sendGameResultTime","2");
//		int time = Integer.valueOf(timeStr);
//		sendGameResultExecutor.schedule(
//				new SendGameResultTask(table,user,userWinMoney,gameResult,userItemMoney,sourceEnum),
//				time,
//				TimeUnit.SECONDS);
//	}

	/**
	 * 发送结果任务。
	 */
	class SendGameResultTask implements Runnable{
		private GameTable table;
		private User user;
		private Map<Long,BigDecimal> userWinMoney;
		private String gameResult;
		private Map<Long, List<UserBetResultResponse.ResultInfo>> userItemMoney;
		private BetOrderSourceEnum sourceEnum;

		public SendGameResultTask(GameTable table,
								  User user,
								  Map<Long,BigDecimal> userWinMoney,
								  String gameResult,
								  Map<Long, List<UserBetResultResponse.ResultInfo>> userItemMoney,
								  BetOrderSourceEnum sourceEnum) {
			this.table = table;
			this.user = user;
			this.userWinMoney = userWinMoney;
			this.gameResult = gameResult;
			this.userItemMoney = userItemMoney;
			this.sourceEnum = sourceEnum;
		}

		@Override
		public void run(){
//			sendUserMessage(table,user,userWinMoney,gameResult,userItemMoney,sourceEnum);
		}
	}

}
