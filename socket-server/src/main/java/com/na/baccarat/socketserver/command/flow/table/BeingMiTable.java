package com.na.baccarat.socketserver.command.flow.table;

import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
import com.na.baccarat.socketserver.command.requestpara.MiCardOverPara;
import com.na.baccarat.socketserver.command.requestpara.SetCardPara;
import com.na.baccarat.socketserver.command.send.RoomCommand;
import com.na.baccarat.socketserver.command.send.TableCommand;
import com.na.baccarat.socketserver.command.sendpara.BetResponse;
import com.na.baccarat.socketserver.command.sendpara.CutCardResponse;
import com.na.baccarat.socketserver.command.sendpara.GameResultResponse;
import com.na.baccarat.socketserver.command.sendpara.JoinRoomResponse;
import com.na.baccarat.socketserver.command.sendpara.LeaveRoomResponse;
import com.na.baccarat.socketserver.command.sendpara.OtherJoinRoomResponse;
import com.na.baccarat.socketserver.command.sendpara.SendDealerResponse;
import com.na.baccarat.socketserver.command.sendpara.SetCardResponse;
import com.na.baccarat.socketserver.command.sendpara.StopBetResponse;
import com.na.baccarat.socketserver.command.sendpara.UserBetResultResponse;
import com.na.baccarat.socketserver.command.sendpara.UserCardResponse;
import com.na.baccarat.socketserver.command.sendpara.UserMessageResponse;
import com.na.baccarat.socketserver.common.enums.AccountRecordTypeEnum;
import com.na.baccarat.socketserver.common.enums.BetOrderSourceEnum;
import com.na.baccarat.socketserver.common.enums.BetOrderStatusEnum;
import com.na.baccarat.socketserver.common.enums.BetOrderWinLostStatusEnum;
import com.na.baccarat.socketserver.common.enums.GameTableInstantStateEnum;
import com.na.baccarat.socketserver.common.enums.PlayEnum;
import com.na.baccarat.socketserver.common.enums.RoundStatusEnum;
import com.na.baccarat.socketserver.common.enums.TradeItemEnum;
import com.na.baccarat.socketserver.common.playrule.AbstractPlayRule;
import com.na.baccarat.socketserver.entity.AccountRecord;
import com.na.baccarat.socketserver.entity.BeingMiGameTable;
import com.na.baccarat.socketserver.entity.BetOrder;
import com.na.baccarat.socketserver.entity.GameTable;
import com.na.baccarat.socketserver.entity.Play;
import com.na.baccarat.socketserver.entity.Round;
import com.na.baccarat.socketserver.entity.RoundExt;
import com.na.baccarat.socketserver.entity.TradeItem;
import com.na.baccarat.socketserver.entity.User;
import com.na.baccarat.socketserver.entity.UserBet;
import com.na.baccarat.socketserver.task.MiCardCountDownTask;
import com.na.baccarat.socketserver.util.SnowflakeIdWorker;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.config.QuartzConfig;
import com.na.user.socketserver.config.SocketIoConfig;
import com.na.user.socketserver.entity.GamePO;
import com.na.user.socketserver.entity.LiveUserPO;
import com.na.user.socketserver.entity.RoundExtPO;
import com.na.user.socketserver.entity.RoundPO;
import com.na.user.socketserver.entity.UserPO;
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

@Component
public class BeingMiTable extends Table {
	
	private static Logger log = LoggerFactory.getLogger(BeingMiTable.class);
	
	@Autowired
	private SocketIOServer socketIOServer;
	
	@Autowired
	private TableCommand tableCommand;
	
	@Autowired
	private IRoundExtService roundExtService;
	
	@Autowired
	private IAccountRecordService accountRecordService;
	
	@Autowired
	private LeaveRoomCommand leaveRoomCommand;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IRoundService roundService;
	
	@Resource(name = "accountRecordSnowflakeIdWorker")
    private SnowflakeIdWorker accountRecordSnowflakeIdWorker;
	
	@Autowired
	private IBetOrderService betOrderService;
	
	@Autowired
	private SocketIoConfig socketIoConfig;
	
	@Autowired
	private RoomCommand roomCommand;
	
	private ExecutorService beingMiTableThreadPool;
	
	@PostConstruct
	public void init(){
		beingMiTableThreadPool = Executors.newFixedThreadPool(socketIoConfig.getBeingMiTableThreadPool(),new DefaultThreadFactory("beingMiTableSettle"));
	}
	
	@Override
	public void join(JoinRoomPara params, SocketIOClient client, JoinRoomResponse response) {
		Long userId = ConvertUtil.toLong(client.get(SocketClientStoreEnum.USER_ID.get()));
		User loginUser = BaccaratCache.getLoginUser(userId);
		
		Integer tableId = params.getTableId();
		BeingMiGameTable table = (BeingMiGameTable) BaccaratCache.getGameTableById(tableId);
		
		List<UserBet> userBetInfos = table.getRound().getUserBetedInfos().stream().filter( item -> {
			return item.getSource() == BetOrderSourceEnum.SEAT || item.getUserId().compareTo(userId) == 0;
		}).collect(Collectors.toList());
		
		//旁注进入
		if(BetOrderSourceEnum.SIDENOTE == params.getSourceEnum()) {
			loginUser.setSource(BetOrderSourceEnum.SIDENOTE);
			loginUser.setChipId(params.getChipId());
			loginUser.getUserPO().setTableId(tableId);
			if(!table.getBesideUser().contains(loginUser.getUserPO().getId())) {
				table.getBesideUser().add(loginUser.getUserPO().getId());
			}
			
			getResponseData(response, table.getPlayers(), userBetInfos, table, userId);
			
			//加入房间成功
			response.setSourceEnum(BetOrderSourceEnum.SIDENOTE);
			response.setChipId(params.getChipId());
			response.setNickName(loginUser.getUserPO().getNickName());
		} else if(BetOrderSourceEnum.SEAT == params.getSourceEnum()) {
			//进入座位
			Integer seatNum = randomSeat(table.getPlayers(), loginUser,table.getGameTablePO().getId());
			loginUser.setSeat(seatNum);
			loginUser.setNotBetRoundNumber(new Integer(0));
			loginUser.setSource(BetOrderSourceEnum.SEAT);
			loginUser.setChipId(params.getChipId());
			loginUser.getUserPO().setTableId(tableId);
			
			table.getPlayers().put(seatNum, loginUser.getUserPO().getId());
			
			getResponseData(response, table.getPlayers(), userBetInfos, table, userId);
			
			//加入房间成功
			response.setChipId(params.getChipId());
			response.setSeatNum(seatNum);
			response.setSourceEnum(BetOrderSourceEnum.SEAT);
			response.setNickName(loginUser.getUserPO().getNickName());
			response.setTableType(table.getGameTablePO().getType());
			
			//通知该桌其他用户有人加入房间
			Collection<SocketIOClient> clients = SocketUtil.getTableOtherClientList(socketIOServer, tableId, client);
			if(clients != null) {
	        	clients.forEach( item -> {
	        		Long itemUserId = ConvertUtil.toLong(item.get(SocketClientStoreEnum.USER_ID.get()));
	        		OtherJoinRoomResponse otherJoinRoomResponse = new OtherJoinRoomResponse();
	        		otherJoinRoomResponse.setTableId(response.getTableId());
	        		otherJoinRoomResponse.setVirtualTableId(response.getVirtualTableId());
//	        		List<JoinRoomResponse.UserInfo> userInfoList = BeanUtil.cloneTo(response.getSeatList());
//	        		userInfoList.forEach( value -> {
//	        			if(itemUserId.compareTo(value.userId) != 0) {
//	        				value.nickName = StringUtil.hideNickName(value.nickName);
//	        			}
//	        		});
	        		otherJoinRoomResponse.setSeatList(response.getSeatList());
	        		roomCommand.send(item, RequestCommandEnum.COMMON_OTHER_JOIN_ROOM, otherJoinRoomResponse);
	            });
	        }
			
		} else {
			throw SocketException.createError("data.error");
		}
	}
	
	/**
	 * 组装用户返回数据
	 */
	public void getResponseData(JoinRoomResponse response,Map<Integer, Long> userMap, List<UserBet> userBetInfos, GameTable table, Long userId) {
		//获取该桌用户信息
		List<JoinRoomResponse.UserInfo> seatList = new ArrayList<>();
		Set<User> userList = BaccaratCache.getUserList(userMap.values());
		userList.forEach( value -> {
			JoinRoomResponse.UserInfo userInfo = response.new UserInfo();
			userInfo.userId = value.getUserPO().getId();
			userInfo.seat = value.getSeat();
			userInfo.countryCode = value.getCountryCode();
			userInfo.nickName = value.getUserPO().getNickName();
			userInfo.balance = value.getUserPO().getBalance().doubleValue();
			userInfo.userPicture = value.getUserPO().getHeadPic();
			seatList.add(userInfo);
		});
		response.setSeatList(seatList);
		
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
			for(Round round : table.getRounds()) {
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
		boolean isRemove = false;
		
		BeingMiGameTable table = (BeingMiGameTable) BaccaratCache.getGameTableById(userPO.getTableId());
		Round round = table.getRound();
		
		if(params != null && !params.isReconnect()) {
			if(RoundStatusEnum.BETTING == round.getRoundPO().getStatusEnum() || 
					RoundStatusEnum.AWAITING_RESULT == round.getRoundPO().getStatusEnum()) {
				Integer betCount = betOrderService.getBetOrderByRoundCount(userPO.getId(), round.getRoundPO().getId());
				if(betCount > 0) {
					throw SocketException.createError("user.already.bet");
				}
			}
		}
		
		//判断是否为竟咪桌
		for (Map.Entry<Integer, Long> entry : table.getPlayers().entrySet()) {
			if(entry.getValue().compareTo(userPO.getId()) == 0){
				table.getPlayers().remove(entry.getKey());
				isRemove = true;
				break;
			}
		}
		
		if(isRemove) {
			response.setSeat(loginUser.getSeat());
			response.setUserId(userPO.getId());
			loginUser.setSeat(null);
			tableCommand.sendOtherUserAtBeingTable(client, response, table, RequestCommandEnum.COMMON_OTHER_LEAVE_ROOM);
		}
		
		if(!isRemove && table.getBesideUser().contains(userPO.getId())) {
			table.getBesideUser().remove(userPO.getId());
			isRemove = true;
		}
	}

	@Override
	public void gameResult(GameResultPara params, SocketIOClient client,
			GameResultResponse response) {
		BeingMiGameTable table = (BeingMiGameTable) BaccaratCache.getGameTableById(ConvertUtil.toInt(client.get(SocketClientStoreEnum.TABLE_ID.get())));
		Round round = table.getRound();
		
		//根据卡牌计算游戏结果
		Map<PlayEnum,AbstractPlayRule> resultMap = genResult(table.getRoundExt());
		final String gameResult = resultMap.get(PlayEnum.NORMAL).getGameReuslt();
		round.getRoundPO().setResult(gameResult);
		round.getRoundPO().setEndTime(new Date());
		
		final String showResult = getShowResult(gameResult, round, table.getRoundExt());
		
		//获取该实体桌对应所有虚拟桌对应的用户下注记录
		final CountDownLatch countDownLatch = new CountDownLatch(4);
		//查询订单
		List<BetOrder> berOrderList = betOrderService.getBetOrderByTable(table.getGameTablePO().getId(), table.getRound().getRoundPO());
		Map<BetOrderSourceEnum, Object> result = new ConcurrentHashMap<>();
		//处理座位上的订单
		beingMiTableThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					threadSettlement(berOrderList.stream().filter( item -> 
					BetOrderSourceEnum.SEAT == item.getBetOrderSource()).collect(Collectors.toList()),
					showResult, resultMap, table, BaccaratCache.getUserList(table.getPlayers().values()), 
					BetOrderSourceEnum.SEAT, result);
				} catch(Throwable t) {
					log.error(t.getMessage(),t);
				} finally {
					countDownLatch.countDown();
					log.debug("座位结算完毕");
				}
			}
		});
		
		//处理旁注的订单
		beingMiTableThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					threadSettlement(berOrderList.stream().filter( item ->
					BetOrderSourceEnum.SIDENOTE == item.getBetOrderSource()).collect(Collectors.toList()),
					showResult, resultMap, table, BaccaratCache.getUserList(table.getBesideUser()),
					BetOrderSourceEnum.SIDENOTE, result);
				} catch(Throwable t) {
					log.error(t.getMessage(),t);
				} finally {
					countDownLatch.countDown();
					log.debug("旁注结算完毕");
				}
			}
		});
		
		//2.处理多台下注
		beingMiTableThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					threadSettlement(berOrderList.stream().filter( item -> 
					BetOrderSourceEnum.MANY_TYPE == item.getBetOrderSource()).collect(Collectors.toList()), 
					showResult, resultMap, table, BaccaratCache.getMultipleUserMap().values(), 
					BetOrderSourceEnum.MANY_TYPE, result);
				} catch(Throwable t) {
					log.error(t.getMessage(),t);
				} finally {
					countDownLatch.countDown();
					log.debug("多台结算完毕");
				}
			}
		});
		
		//好路结算处理
		beingMiTableThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					threadSettlement(berOrderList.stream().filter(item -> 
					BetOrderSourceEnum.GOOD_ROAD == item.getBetOrderSource()).collect(Collectors.toList()), 
					showResult, resultMap, table, new ArrayList<>(), 
					BetOrderSourceEnum.GOOD_ROAD, result);
				} catch(Throwable t) {
					log.error(t.getMessage(),t);
				} finally {
					countDownLatch.countDown();
					log.debug("好路结算完毕");
				}
			}
		});
		
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			throw SocketException.createError("settlement.abnormal");
		} finally {
			for(Entry<BetOrderSourceEnum,Object> enrty : result.entrySet()) {
				log.info("发送: 【花费】" + enrty.getKey().name());
				Map<String, Object> sourceMap = (Map<String, Object>) enrty.getValue();
				Collection<User> sendUserList = (Collection<User>) sourceMap.get("sendUserList");
				Map<Long,BigDecimal> userWinMoney = (Map<Long, BigDecimal>) sourceMap.get("userWinMoney");
				Map<Long, List<UserBetResultResponse.ResultInfo>> userItemMoney = (Map<Long, List<UserBetResultResponse.ResultInfo>>) sourceMap.get("userItemMoney");
				
				//发送消息
				TimeLeft left = new TimeLeft();
				sendUserList.parallelStream().forEach(user->{
					try {
						Map<Long, List<UserBetResultResponse.ResultInfo>> resultUserItemMoney = new HashMap<>();
						if (enrty.getKey() == BetOrderSourceEnum.SIDENOTE && userItemMoney.containsKey(user.getUserPO().getId())) {
							Map<String, Object> seatSourceMap = (Map<String, Object>) result.get(BetOrderSourceEnum.SEAT);
							if(seatSourceMap != null) {
								resultUserItemMoney.putAll((Map<Long, List<UserBetResultResponse.ResultInfo>>)seatSourceMap.get("userItemMoney"));
							}
							resultUserItemMoney.put(user.getUserPO().getId(), userItemMoney.get(user.getUserPO().getId()));
						} else {
							resultUserItemMoney = userItemMoney;
						}
						sendUserMessage(table, user, userWinMoney, gameResult, resultUserItemMoney, enrty.getKey());
					}catch (Exception e){
						log.error(e.getMessage(),e);
					}
				});
				log.info("【结算】给{}个用户发送消息花费时间{}",sendUserList.size(),left.end());
			}
		}
	}
	
	/**
	 * 单桌结算
	 * @param berOrderList   需要处理的订单列表
	 * @param showResult
	 * @param resultMap      计算的该局结果集
	 * @param table
	 * @param userList       用户集合
	 * @param sourceEnum
	 * @param result
	 */
	private void threadSettlement(List<BetOrder> berOrderList,
			String showResult, Map<PlayEnum,AbstractPlayRule> resultMap,
			GameTable table, Collection<User> userList, BetOrderSourceEnum sourceEnum, 
			Map<BetOrderSourceEnum, Object> result) {
		TimeLeft left = new TimeLeft();
		try {
			/**
			 * 用户赢钱额度 若用户未投注  集合则不存在该用户
			 * key  用户ID   value  用户赢得钱
			 */
			Map<Long, BigDecimal> userWinMoney = new HashMap<>();
			Map<Long, List<UserBetResultResponse.ResultInfo>> userItemMoney = new HashMap<>();

			String gameResult = resultMap.get(PlayEnum.NORMAL).getGameReuslt();
			//投注来源
			dealOrder(berOrderList, showResult, resultMap, table, userList,
					userItemMoney, userWinMoney);

			Collection<User> sendUserList = new HashSet<>();
			if (userList == null) {
				berOrderList.forEach(item -> {
					User loginUser = BaccaratCache.getLoginUser(item.getUserId());
					if (loginUser != null) {
						sendUserList.add(loginUser);
					}
				});
			} else {
				sendUserList.addAll(userList);
			}
			Map<String, Object> sourceMap = new HashMap<>();
			sourceMap.put("sendUserList", sendUserList);
			sourceMap.put("userWinMoney", userWinMoney);
			sourceMap.put("userItemMoney", userItemMoney);
			result.put(sourceEnum, sourceMap);
			log.info("发送: 【花费】" + sourceEnum + ",人数：" + sendUserList.size());
		} catch(Exception e) {
			log.error("", e);
			throw SocketException.createError("table.status.abnormal");
		} finally {
			log.info("【{}】对{}个用户的{}订单结算，花费时间：{}",sourceEnum.name(),userList.size(),berOrderList.size(),left.end());
		}
	}
	
	/**
	 * 处理订单
	 */
	private void dealOrder(List<BetOrder> berOrderList, String showResult, Map<PlayEnum,AbstractPlayRule> resultMap,
			GameTable table, Collection<User> userList,  Map<Long, List<UserBetResultResponse.ResultInfo>> userItemMoney, 
			Map<Long,BigDecimal> userWinMoney) {
		
		List<UserBetResultResponse.ResultInfo> itemMoney = null;
		Set<UserPO> userPOList = new HashSet<>();
		List<AccountRecord> accountRecordList = new ArrayList<>();
		
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
			
			settlement(order, table, userList, resultMap, userWinMoney, userPOList, showResult, itemMoney, accountRecordList, accountRecordRemark);
		}
		if(berOrderList != null && berOrderList.size() > 0) {
			//增加账户流水
			if(accountRecordList.size() > 0) {
				accountRecordService.batchAddAcountRecord(accountRecordList);
			}
			//批量更新订单信息
			betOrderService.batchUpdate(berOrderList);
			//执行批量更新任务
			userService.batchUpdateBalance(new ArrayList<UserPO>(userPOList), "GameResult");
		}
	}
	
	/**
	 * 发送用户通知
	 */
	private void sendUserMessage(GameTable table, User user, 
			Map<Long,BigDecimal> userWinMoney, String gameResult, 
			Map<Long, List<UserBetResultResponse.ResultInfo>> userItemMoney, BetOrderSourceEnum sourceEnum) {
	    if(user == null) {
	    	return ;
	    }
		BigDecimal money = userWinMoney.get(user.getUserPO().getId());
		//获取到用户对应的客户端
		SocketIOClient userClient = SocketUtil.getClientByUser(socketIOServer, user.getUserPO());
		if(userClient!=null && money == null && BetOrderSourceEnum.SEAT == sourceEnum) {
			if(user.getNotBetRoundNumber() != null) {
				Integer notBetCount = user.getNotBetRoundNumber() + 1;
				
				user.setNotBetRoundNumber(notBetCount);
				
				if(notBetCount == 3) {
					//发出警告
					UserMessageResponse userMessageResponse = new UserMessageResponse();
					userMessageResponse.setContent("请下注,五局未下注将自动踢出房间");
					roomCommand.send(userClient, RequestCommandEnum.CLIENT_USER_MESSAGE, userMessageResponse);
				} else if (notBetCount == 5) {
					User temp = BaccaratCache.getUserByClient(userClient);
					if(temp!=null) {
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
		}
		log.debug("发送游戏结果通知" + user.getUserPO().getLoginName());
		//不是连续的5局不能提
		if(money != null && BetOrderSourceEnum.SEAT == sourceEnum){
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
		
		for(int i = 0 ; i < cards.length ; i++) {
			if(cards[i] != null) {
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
		
		//发送通知
		roomCommand.send(userClient, RequestCommandEnum.COMMON_GAME_RESULT, userBetResultResponse);
	}
	
	/**
	 * 结算
	 */
	private void settlement(BetOrder order,GameTable table, Collection<User> userList, 
			Map<PlayEnum,AbstractPlayRule> resultMap, Map<Long,BigDecimal> userWinMoney,
			Set<UserPO> userPOList, String showResult, List<UserBetResultResponse.ResultInfo> itemMoney, 
			List<AccountRecord> accountRecordList, String accountRecordRemark) {
		Round round = table.getRound();
		RoundExt roundExt = table.getRoundExt();
		
		UserPO userPO = null;
		if(userList != null && userList.size() > 0){
			for(User player : userList) {
				if(order.getUserId().compareTo(player.getUserPO().getId()) == 0) {
					User loginUser = BaccaratCache.getLoginUser(player.getUserPO().getId());
					userPO = loginUser==null ? null : loginUser.getUserPO();
					break;
				}
			}
		}

		if(userPO == null) {
			userPO = AppCache.getLoginUser(order.getUserId());
			if(userPO == null){
                userPO = userService.getUserById(order.getUserId());
                log.debug("【结算】从数据库加载用户数据  ID:" + order.getUserId());
			}
			User temp = new User();
			temp.setUserPO(userPO);
			userList.add(temp);
		}
		log.debug("【结算】 用户:" + userPO.getId() + ",当前余额为：" + userPO.getBalance());
		
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
		
		userPO.setBalance(userPO.getBalance().add(order.getAmount()).add(order.getWinLostAmount()));
		BigDecimal winMoney = ((LiveUserPO)userPO).getWinMoney();
		if(winMoney == null) {
			winMoney = new BigDecimal(0);
		}
		if(rule.getResultAmount().compareTo(new BigDecimal(0)) > 0) {
			winMoney = winMoney.add(rule.getResultAmount());
		}
		((LiveUserPO)userPO).setWinMoney(winMoney);
		
		userPOList.add(userPO);
		
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
			userWinMoney.put(userPO.getId(), rule.getResultAmount().add(userWinMoney.get(userPO.getId())));
		} else {
			userWinMoney.put(userPO.getId(), rule.getResultAmount());
		}
	}

	@Override
	public void stopBet(CommandReqestPara params, SocketIOClient client, StopBetResponse response) {
		GameTable gameTable = BaccaratCache.getGameTableById(ConvertUtil.toInt(client.get(SocketClientStoreEnum.TABLE_ID.get())));
		Integer tableId = gameTable.getGameTablePO().getId();
		RoundPO roundPO = gameTable.getRound().getRoundPO();
		if (roundPO.getStatusEnum() == RoundStatusEnum.BETTING
				|| roundPO.getStatusEnum() == RoundStatusEnum.PAUSE) {
			
			gameTable.setInstantStateEnum(GameTableInstantStateEnum.AWAITING_RESULT);
			gameTable.getRound().getRoundPO().setStatusEnum(RoundStatusEnum.AWAITING_RESULT);
			
			if(gameTable.getGameTablePO().isMiTable()) {
				findMiUserAtBeingMiTable(gameTable.getGameTablePO().getId());
			}
			Map<String,Long> zhuYouGao = new HashMap<>();
			if(gameTable.getBankPlayerMap() != null && gameTable.getBankPlayerMap(TradeItemEnum.BANKER)!=null && gameTable.getBankPlayerMap(TradeItemEnum.BANKER).size() > 0){
				zhuYouGao.put(TradeItemEnum.BANKER.get(), gameTable.getBankPlayerMap(TradeItemEnum.BANKER).stream().filter(item -> 
				item != null&&item.getUserPO() != null
				).findFirst().get().getUserPO().getId());
			}
			if(gameTable.getBankPlayerMap() != null && gameTable.getBankPlayerMap(TradeItemEnum.PLAYER)!=null && gameTable.getBankPlayerMap(TradeItemEnum.PLAYER).size() > 0){
				zhuYouGao.put(TradeItemEnum.PLAYER.get(), gameTable.getBankPlayerMap(TradeItemEnum.PLAYER).stream().filter(item -> 
				item != null&&item.getUserPO() != null
				).findFirst().get().getUserPO().getId());
			}
			response.setZhuYouGao(zhuYouGao.size()>0?zhuYouGao:null);
			
			//清空个人限红和台红检查
			gameTable.setLimitRule(null);
			BaccaratCache.getTableUserList(tableId).forEach( item -> {
				item.removeLimitRule(tableId);
			});
			
			roundService.update(gameTable.getRound().getRoundPO());
			response.setTableId(tableId);
			response.setShowTableStatus(gameTable.getShowStatus());
			tableCommand.sendAllTablePlayer(response, RequestCommandEnum.COMMOM_STOP_BET, tableId);
		}
	}
	
	/**
	 * 竞咪桌找到需要咪牌的用户
	 * 规则：分别找到庄闲投注额最大的用户进行咪牌
	 */
	private void findMiUserAtBeingMiTable(Integer tableId) {
		BeingMiGameTable gameTable = (BeingMiGameTable) BaccaratCache.getGameTableById(tableId);
		gameTable.getBankPlayerMap().put(TradeItemEnum.BANKER , new HashSet<User>());
		gameTable.getBankPlayerMap().put(TradeItemEnum.PLAYER , new HashSet<User>());
		List<BetOrder> betOrders = betOrderService.getBetOrderByRoundNoTradeItem(gameTable.getRound().getRoundPO());
		BigDecimal bankerMax = new BigDecimal(0);
		BigDecimal playerMax = new BigDecimal(0);
		List<BetOrder> userOrderList;
		for(final User user : BaccaratCache.getUserList(gameTable.getPlayers().values())) {
			userOrderList = betOrders.stream().filter( item -> {
				return TradeItemEnum.BANKER == item.getTradeItemKeyEnum() 
						&& item.getUserId().compareTo(user.getUserPO().getId()) == 0;
			}).collect(Collectors.toList());
			
			BigDecimal banker = new BigDecimal(0);
			for (BetOrder item : userOrderList) {
				if(item == null){
					continue;
				}
				banker = banker.add(item.getAmount());
			}
			
			if(banker.compareTo(bankerMax) > 0) {
				bankerMax = BeanUtil.cloneTo(banker);
				gameTable.setBankPlayerMap(TradeItemEnum.BANKER, user);
				continue;
			}
			
			userOrderList = betOrders.stream().filter( item -> {
				return TradeItemEnum.PLAYER == item.getTradeItemKeyEnum()
						&& item.getUserId().compareTo(user.getUserPO().getId()) == 0;
			}).collect(Collectors.toList());
			
			BigDecimal player = new BigDecimal(0);
			for (BetOrder item : userOrderList) {
				if(item == null){
					continue;
				}
				player = player.add(item.getAmount());
			}
			
			if(player.compareTo(playerMax) > 0) {
				playerMax = BeanUtil.cloneTo(player);
				gameTable.setBankPlayerMap(TradeItemEnum.PLAYER, user);
			}
		}
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
			
			Map<TradeItemEnum,Set<User>> betPlayerMap = table.getBankPlayerMap();
			RoundExt roundExt = table.getRoundExt();
			RoundExtPO roundExtPO = roundExt.getRoundExtPO();
			
			int flag;
			synchronized(this) {
				table.setCard(params.getCardType(), params.getCardNum(), params.getIndex());
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
				handOutCard(flag, table, roundExtPO, client, betPlayerMap);
			}
		}
	}
	
	public void miCardOver(MiCardOverPara params, SocketIOClient client) {
		log.debug("用户咪牌" + params.getIndex());
		BeingMiGameTable table = (BeingMiGameTable) BaccaratCache.getGameTableById(ConvertUtil.toInt(client.get(SocketClientStoreEnum.TABLE_ID.get())));
		if(table == null) {
			throw SocketException.createError("table.not.exist");
		}
		
		Integer type = 0;
		User user = BaccaratCache.getUserByClient(client);
		List<UserCardResponse.CardInfo> cardList = new ArrayList<>();
		UserCardResponse userMiCardResponse = new UserCardResponse();
		
		for(Integer item : params.getIndex()) {
			if(table.getCards()[item].getOpenStatus()) {
				throw SocketException.createError("already.open.card");
			} else {
				table.getCards()[item].setOpenStatus(true);
				UserCardResponse.CardInfo cardInfo = userMiCardResponse.new CardInfo();
				cardInfo.number = table.getCards()[item].getCardNum();
				cardInfo.type = table.getCards()[item].getCardType();
				cardInfo.index = item;
				cardList.add(cardInfo);
				if(item.compareTo(4) == 0) {
					QuartzConfig.removeJob(schedulerFactoryBean, MiCardCountDownTask.class.getName() + table.getGameTablePO().getId() + 2);
				} else if (item.compareTo(5) == 0) {
					QuartzConfig.removeJob(schedulerFactoryBean, MiCardCountDownTask.class.getName() + table.getGameTablePO().getId() + 3);
				} else {
					type = 1;
				}
			}
		}
		
		//移除倒计时任务
//		QuartzConfig.removeJob(schedulerFactoryBean, MiCardCountDownTask.class.getName() + table.getGameTablePO().getId() + type);
		
		Set<User> userList = BaccaratCache.getUserList(table.getBesideUser());
		
		userList.addAll(BaccaratCache.getUserList(table.getPlayers().values()).stream().filter( item -> {
			return item.getUserPO().getId() != user.getUserPO().getId();
		}).collect(Collectors.toList()));
		
		//通知该桌用户开牌
		noticeNotMiCardUser(table.getGameTablePO().getId(), true, cardList, userList, 0);
		
		//通知荷官开牌
		SendDealerResponse sendResponse = new SendDealerResponse();
		sendResponse.setCommand("fanpai");
		sendResponse.setCardList(params.getIndex());
		SocketIOClient dealerClient = SocketUtil.getClientByUser(socketIOServer, table.getDealer().getUserPO());
		dealerCommand.sendMessage(dealerClient, sendResponse);
	}
	
	/**
	 * 分发卡片  让用户咪牌
	 * @param type   1表示闲前2张和庄闲前2张  2表示闲第3张  3表示庄第3张
	 * @param table
	 * @param roundExtPO
	 * @param dealerClient
	 * @param betPlayerMap
	 */
	private void handOutCard(Integer type, GameTable table, RoundExtPO roundExtPO,  SocketIOClient dealerClient , Map<TradeItemEnum,Set<User>> betPlayerMap) {
		//获取咪牌倒计时
		GamePO gamePO = AppCache.getGame(table.getGameTablePO().getGameId());
		int countDownSecond = table.getGameTablePO().getMiCountDownSeconds();
		
		List<UserCardResponse.CardInfo> cardList = new ArrayList<>();
		List<UserCardResponse.CardInfo> bankTaskCardList = new ArrayList<>();
		List<UserCardResponse.CardInfo> playTaskCardList = new ArrayList<>();
		List<UserCardResponse.CardInfo> dealerCardList = new ArrayList<>();
		Set<User> bankTaskNoticeUserList = new HashSet<>();
		Set<User> playTaskNoticeUserList = new HashSet<>();
		UserCardResponse userMiCardResponse = new UserCardResponse();
		UserCardResponse.CardInfo cardInfo;
		boolean taskFlag = false;
		switch (type) {
			case 1:
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
						dealerClient, dealerCardList, playTaskNoticeUserList);
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
						dealerClient, dealerCardList, bankTaskNoticeUserList);
				if(dealerCardList != null && dealerCardList.size() > 0) {
					noticeDealer(dealerClient, dealerCardList, table);
				}
				if(playTaskCardList != null && playTaskCardList.size() > 0 ||
						bankTaskCardList != null && bankTaskCardList.size() > 0) {
					taskFlag = true;
				}
				break;
			case 2:
				cardInfo = userMiCardResponse.new CardInfo();
				cardInfo.number = roundExtPO.getPlayerCard3Number();
				cardInfo.type = roundExtPO.getPlayerCard3Mode();
				cardInfo.index = 4;
				cardList.add(cardInfo);
				handOutUser(TradeItemEnum.PLAYER, table, betPlayerMap, 
						cardList, playTaskCardList, countDownSecond, 
						dealerClient, dealerCardList, playTaskNoticeUserList);
				
				if(dealerCardList != null && dealerCardList.size() > 0) {
					noticeDealer(dealerClient, dealerCardList, table);
				}
				if(playTaskCardList != null && playTaskCardList.size() > 0) {
					taskFlag = true;
				}
				break;
			case 3:
				cardInfo = userMiCardResponse.new CardInfo();
				cardInfo.number = roundExtPO.getBankCard3Number();
				cardInfo.type = roundExtPO.getBankCard3Mode();
				cardInfo.index = 5;
				cardList.add(cardInfo);
				handOutUser(TradeItemEnum.BANKER, table, betPlayerMap, 
						cardList, bankTaskCardList, countDownSecond, 
						dealerClient, dealerCardList, bankTaskNoticeUserList);
				
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
	 * 分发需要发送给哪些Client
	 */
	private void handOutUser(TradeItemEnum tradeItemEnum, GameTable table, Map<TradeItemEnum,Set<User>> betPlayerMap, 
			List<UserCardResponse.CardInfo> cardList, List<UserCardResponse.CardInfo> taskCardList, 
			Integer countDownSecond, SocketIOClient dealerClient, List<UserCardResponse.CardInfo> dealerCardList,
			Set<User> taskNoticeUserList) {
		if(betPlayerMap == null || betPlayerMap.size() < 1) {
			dealerCardList.addAll(cardList);
		}
		Set<User> miUserList = betPlayerMap.get(tradeItemEnum);
		Set<User> allTableUserList = BaccaratCache.getTableUserList(table.getGameTablePO().getId());
		Set<User> notMiUserList = null;
		if(miUserList != null && miUserList.size() > 0) {
			noticeMiCardUser(table.getGameTablePO().getId(), cardList, miUserList, 
					countDownSecond, tradeItemEnum, dealerClient);
			taskCardList.addAll(cardList);
//			taskNoticeUserList.addAll(miUserList);
			//发送给未下注用户
			notMiUserList = allTableUserList.stream().filter( item -> {
				return !miUserList.contains(item);
			}).collect(Collectors.toSet());
			
			noticeNotMiCardUser(table.getGameTablePO().getId(), false, cardList, notMiUserList, countDownSecond);
			taskNoticeUserList.addAll(notMiUserList);
		} else {
			dealerCardList.addAll(cardList);
			notMiUserList = allTableUserList;
			//发送给未下注用户  开牌
			noticeNotMiCardUser(table.getGameTablePO().getId(), true, cardList, notMiUserList, countDownSecond);
		}
		
		cardList.clear();
	}
	
	/**
	 * 如果没有用户下注通知荷官开牌
	 */
	private void noticeDealer(SocketIOClient dealerClient, List<UserCardResponse.CardInfo> cardList, GameTable table) {
		log.debug("通知荷官开牌");
		cardList.forEach(item -> {
			table.getCards()[item.index].setOpenStatus(true);
		});
		//通知荷官开牌
		SendDealerResponse sendResponse = new SendDealerResponse();
		List<Integer> indexList = new ArrayList<>();
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
			Set<User> userList, int countDownSecond) {
		try {
			//通知用户开牌
			UserCardResponse userCardResponse = new UserCardResponse();
			if (isFanPai) {
				userCardResponse.setCardList(cardList);
			} else {
				List<UserCardResponse.CardInfo> tempCardList = BeanUtil.cloneTo(cardList);
				tempCardList.forEach(item -> {
					item.number = null;
					item.type = null;
				});
				userCardResponse.setCardList(tempCardList);
				userCardResponse.setCountDownSecond(countDownSecond);
			}
			userCardResponse.setIsMiCard(false);
			userCardResponse.setTableId(tableId);

			Collection<SocketIOClient> clients = SocketUtil.getClientByUserList(socketIOServer, userList);
			for (SocketIOClient item : clients) {
				roomCommand.send(item, RequestCommandEnum.CLIENT_USER_CARD, userCardResponse);
			}
		}catch (Exception e){
			log.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 发送需要咪牌的用户
	 */
	private void noticeMiCardUser(Integer tableId, List<UserCardResponse.CardInfo> cardList,
			Set<User> userList, int countDownSecond,TradeItemEnum tradeItemEnum,
			SocketIOClient dealerClient) {
		//通知用户咪牌
		UserCardResponse userCardResponse = new UserCardResponse();
		userCardResponse.setCardList(cardList);
		userCardResponse.setIsMiCard(true);
		userCardResponse.setCountDownSecond(countDownSecond);
		userCardResponse.setMiType(tradeItemEnum.get());
		userCardResponse.setTableId(tableId);
		//TODO 表示单台开牌
		userCardResponse.setCardType(1);

		try {
			Collection<SocketIOClient> clients = SocketUtil.getClientByUserList(socketIOServer, userList);
			for (SocketIOClient item : clients) {
				roomCommand.send(item, RequestCommandEnum.CLIENT_USER_CARD, userCardResponse);
			}
		}catch (Exception e){
			log.error(e.getMessage(),e);
		}
		
		//通知荷官咪牌倒计时
		SendDealerResponse sendDealerResponse = new SendDealerResponse();
		sendDealerResponse.setCommand("miCD");
		sendDealerResponse.setMiCoundDown(countDownSecond);
		dealerCommand.sendMessage(dealerClient, sendDealerResponse);
		
	}

	@Override
	public void bet(BetPara params, SocketIOClient client, BetResponse response) {
		com.na.baccarat.socketserver.entity.User user = BaccaratCache.getUserByClient(client);
		if(user == null) {
			throw SocketException.createError("user.not.exist");
		}
		LiveUserPO userPO = (LiveUserPO) user.getUserPO();
		
		Integer tableId = params.getTableId();
		BeingMiGameTable gameTable = (BeingMiGameTable) BaccaratCache.getGameTableById(tableId);
		if(gameTable == null) {
			throw SocketException.createError("table.not.exist");
		}
		
        SocketException.isTrue(gameTable.getRound().getRoundPO().getStatusEnum() != RoundStatusEnum.BETTING,"bet.already.stop");
		
		checkBetTimeout(gameTable);
		
		checkTableLimit(params, user, gameTable);
		
		RoundPO roundPO = gameTable.getRound().getRoundPO();

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
            if(params.getSourceEnum() == BetOrderSourceEnum.SEAT || params.getSourceEnum() == BetOrderSourceEnum.SIDENOTE) {
            	betOrder.setSource(user.getSource().get());
            } else {
            	betOrder.setSource(params.getSource());
            }
            if(params.getSourceEnum() == BetOrderSourceEnum.SEAT) {
                betOrder.setSourceValue(user.getSeat()+"");
            }

            betOrder.setTradeItemId(tradeItem.getId());
            betOrder.setTradeItemKey(tradeItem.getKey());
            betOrder.setPlayId(tradeItem.getPlayId());
            betOrder.setoPercentage(userPO.getIntoPercentage());
            betOrder.setWashPercentage(userPO.getWashPercentage());
            betOrder.setStatus(BetOrderStatusEnum.CONFIRM.get());
            
    		betOrderList.add(betOrder);

            userPO.setBalance(userPO.getBalance().subtract(betOrder.getAmount()));

            total = total.add(betOrder.getAmount());
            
//            if(BetOrderSourceEnum.SEAT == params.getSourceEnum()) {
//            	gameTable.setTradeItemBetMoney(userPO.getId(), betOrder.getTradeItemId(),betOrder.getAmount());
//            }
            
        	//用户投注细节
            UserBet userBetDetail = new UserBet();
            userBetDetail.setAmount(betOrder.getAmount());
            userBetDetail.setUserId(userPO.getId());
            userBetDetail.setTableId(params.getTableId());
            userBetDetail.setSource(betOrder.getSourceEnum());
            userBetDetail.setTradeId(tradeItem.getId());
            gameTable.getRound().addBeted(userBetDetail);
            
            response.addTradeItemId(betOrder.getTradeItemId());
        }
        
        //批量添加订单
        betOrderService.batchAddBetOrders(userPO,betOrderList, gameTable.getGameTablePO().getId(), roundPO.getId());
        
        userService.updateUserBalance(userPO.getId(),BigDecimal.ZERO.subtract(total), "Bet");

        Set<User> userCollection = null;
        if(params.getSourceEnum() == BetOrderSourceEnum.SEAT) {
            userCollection = BaccaratCache.getUserList(gameTable.getBesideUser());
            userCollection.addAll(BaccaratCache.getUserList(gameTable.getPlayers().values()));
        }else{
            List<Long> ids = new ArrayList<>();
            ids.add(Long.valueOf(client.get(SocketClientStoreEnum.USER_ID.get())));
            userCollection = BaccaratCache.getUserList(ids);
        }
        sendTableBetSuccess(client, user, userCollection, params, total);
	}

	@Override
	public void cutCard(CutCardPara params, SocketIOClient client,
			CutCardResponse response) {
		response.setCutPoint(params.getCutPoint());
	}

}
