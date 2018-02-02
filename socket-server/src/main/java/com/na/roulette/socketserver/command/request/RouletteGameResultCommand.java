package com.na.roulette.socketserver.command.request;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.send.RoomCommand;
import com.na.baccarat.socketserver.command.sendpara.UserMessageResponse;
import com.na.baccarat.socketserver.common.enums.AccountRecordTypeEnum;
import com.na.baccarat.socketserver.common.enums.RoundStatusEnum;
import com.na.baccarat.socketserver.entity.AccountRecord;
import com.na.baccarat.socketserver.entity.BetOrder;
import com.na.baccarat.socketserver.entity.TradeItem;
import com.na.baccarat.socketserver.util.SnowflakeIdWorker;
import com.na.remote.IPlatformUserRemote;
import com.na.roulette.socketserver.cache.RouletteCache;
import com.na.roulette.socketserver.command.RouletteRequestCommandEnum;
import com.na.roulette.socketserver.command.requestpara.GameResultPara;
import com.na.roulette.socketserver.command.requestpara.LeaveTablePara;
import com.na.roulette.socketserver.command.send.RouletteTableCommand;
import com.na.roulette.socketserver.command.sendpara.RouletteGameResultResponse;
import com.na.roulette.socketserver.command.sendpara.RouletteUserBetResultResponse;
import com.na.roulette.socketserver.command.sendpara.RouletteUserBetResultResponse.Result;
import com.na.roulette.socketserver.common.enums.RouletteBetOrderStatusEnum;
import com.na.roulette.socketserver.common.enums.RouletteBetOrderWinLostStatusEnum;
import com.na.roulette.socketserver.common.enums.RouletteGameTableInstantStateEnum;
import com.na.roulette.socketserver.entity.RouletteGameTable;
import com.na.roulette.socketserver.entity.RouletteRound;
import com.na.roulette.socketserver.entity.RouletteUser;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.common.event.DisConnectionEvent;
import com.na.user.socketserver.entity.LiveUserPO;
import com.na.user.socketserver.entity.RoundPO;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.service.IAccountRecordService;
import com.na.user.socketserver.service.IBetOrderService;
import com.na.user.socketserver.service.IRoundService;
import com.na.user.socketserver.service.ITradeItemService;
import com.na.user.socketserver.service.IUserService;
import com.na.user.socketserver.util.ActiveMQUtil;
import com.na.user.socketserver.util.BeanUtil;
import com.na.user.socketserver.util.ConvertUtil;
import com.na.user.socketserver.util.SocketUtil;

/**
 * 轮盘结算
 * 
 * @author Administrator
 *
 */
@Cmd(paraCls = GameResultPara.class, name = "轮盘结算")
@Component
public class RouletteGameResultCommand implements ICommand {

	private Logger log = LoggerFactory
			.getLogger(RouletteGameResultCommand.class);
	
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private IBetOrderService betOrderService;
	@Autowired
	private SocketIOServer socketIOServer;
	@Autowired
	private RouletteTableCommand tableCommand;
	@Autowired
	private IRoundService roundService;
	@Autowired
	private IUserService userService;
	@Autowired
	private ITradeItemService tradeItemService;
	@Autowired
	private RoomCommand roomCommand;
	@Autowired
	private RouletteLeaveTableCommand leaveTableCommand;
	@Autowired
	private IAccountRecordService accountRecordService;
	@Autowired
	private IPlatformUserRemote platformUserRemote;

	@Resource(name = "accountRecordSnowflakeIdWorker")
	private SnowflakeIdWorker accountRecordSnowflakeIdWorker;
	
	@Autowired
	private ActiveMQUtil activeMQUtil;
	
	@Override
	public boolean exec(SocketIOClient client,
			CommandReqestPara commandReqestPara) {
		GameResultPara params = (GameResultPara) commandReqestPara;

		if (params == null)
			throw SocketException.createError("param.not.allow.empty");

		Integer tableId = ConvertUtil.toInt(client
				.get(SocketClientStoreEnum.TABLE_ID.get()));
		RouletteGameTable table = RouletteCache.getGameTableById(tableId);

		if (null == table)
			throw SocketException.createError("table.not.exist");

		RouletteRound round = table.getRound();
		RoundPO roundPO = round.getRoundPO();

		if (roundPO.getStatusEnum() != RoundStatusEnum.AWAITING_RESULT)
			throw SocketException.createError("table.status.abnormal");

		roundPO.setStatusEnum(RoundStatusEnum.FINISH);
		table.setInstantStateEnum(RouletteGameTableInstantStateEnum.FINISH);

		final String gameResult = params.getResult();
		if (StringUtils.isBlank(gameResult))
			throw SocketException.createError("param.not.allow.empty");

		roundPO.setResult(gameResult);

		// 获取该桌子的投注记录
		List<BetOrder> betOrders = betOrderService.getBetOrderByRound(roundPO);

		// 用户赢钱额度 若用户未投注 集合则不存在该用户 key 用户ID value 用户赢得钱
		Map<Long, BigDecimal> userWinMoney = new HashMap<>();
		// 每项交易的细节
		Map<Long, Map<Integer, RouletteUserBetResultResponse.Result>> userDetailWinMoney = new HashMap<>();
		//用户余额变动细节
		Map<Long, UserPO> updateUserList = new HashMap<>();
		//用户资金流水
		List<AccountRecord> accountRecordList = new LinkedList();
		// 处理每个投注单
		
		JSONObject json = new JSONObject();
		json.put("tableId", table.getGameTablePO().getId());
		json.put("roundId", table.getRound().getRoundPO().getId());
		String accountRecordRemark = json.toJSONString();
		
		for (BetOrder betOrder : betOrders) {
			settlement(betOrder, table, gameResult, userWinMoney,
					userDetailWinMoney, updateUserList,accountRecordList, accountRecordRemark);
		}
		if(updateUserList.size()>0 ){
			userService.batchUpdateBalance(new ArrayList<UserPO>(updateUserList
					.values()), "GameResult");
		}
		if(betOrders.size() > 0){
			betOrderService.batchUpdate(betOrders);
		}
		if(accountRecordList.size() > 0){
			accountRecordService.batchAddAcountRecord(accountRecordList);
		}

		for (RouletteUser rouletteUser : table.getPlayers().values()) {
			BigDecimal money = userWinMoney.get(rouletteUser.getUserPO()
					.getId());
			SocketIOClient betOrderClent = SocketUtil.getClientByUser(
					socketIOServer, rouletteUser.getUserPO());
			
			if (money == null) {
				if (rouletteUser.getNotBetRoundNumber() != null) {
					Integer notBetCount = rouletteUser.getNotBetRoundNumber() + 1;

					rouletteUser.setNotBetRoundNumber(notBetCount);

					if (notBetCount == 3) {
						//发出警告
						UserMessageResponse userMessageResponse = new UserMessageResponse();
						userMessageResponse.setContent("请下注,五局未下注将自动踢出房间");
						roomCommand.send(betOrderClent, RequestCommandEnum.CLIENT_USER_MESSAGE, userMessageResponse);
					} else if (notBetCount == 5) {
						try {
							//踢出桌子
							LeaveTablePara param = new LeaveTablePara();
							param.setForce(true);
							leaveTableCommand.exec(betOrderClent, param);
						} catch (Exception e) {
							log.error("", e);
						}
					}
				}
			}
			//不是连续的5局不能提
			if (money != null) {
				rouletteUser.setNotBetRoundNumber(new Integer(0));
			}
			
			RouletteUserBetResultResponse userBetResultResponse = new RouletteUserBetResultResponse();
			userBetResultResponse.setMoney(money);
			userBetResultResponse.setResult(roundPO.getResult());
			userBetResultResponse.setShowTableStatus(table.getShowStatus());
			userBetResultResponse
					.setBetDetail(getValueListByMap(userDetailWinMoney
							.get(rouletteUser.getUserPO().getId())));

			tableCommand.send(betOrderClent,
					RouletteRequestCommandEnum.COMMON_GAME_RESULT,
					userBetResultResponse);
		}

		roundService.update(roundPO);
		// 该局数据归入路子
		table.getRounds().add(round);
		try {
			platformUserRemote.sendRound(roundPO.getId());
			activeMQUtil.send(roundPO.getId(), null);
		} catch (RuntimeException e) {
			log.warn("", e);
		}
		
		//清除用户数据
		table.getPlayers().values().forEach( item -> {
			item.setTradeItemBetMoneyMap(new ConcurrentHashMap<>());
		});
		// 清除本局用户数据
		RouletteCache.removeUserBetDetail(tableId, roundPO.getId());
		//结算完毕  清除离线用户
		applicationContext.publishEvent(new DisConnectionEvent(table.getGameTablePO().getId()));
		
		RouletteGameResultResponse gameResultResponse = new RouletteGameResultResponse();
		gameResultResponse.setResult(gameResult);
		tableCommand.send(client,
				RouletteRequestCommandEnum.COMMON_GAME_RESULT,
				gameResultResponse);
		log.info(table.getGameTablePO().getName() + " 局ID： " + roundPO.getId()
				+ " 结算完成");
		userDetailWinMoney = null;
		userWinMoney = null;
		return true;
	}

	/**
	 * 处理输赢情况
	 * @param betOrder
	 *            订单表
	 * @param table
 *            龙盘桌
	 * @param gameResult 游戏结果
	 * @param userWinMoney 输赢Map
	 * @param updateUserList
	 * @param accountRecordList
	 */
	private void settlement(
			BetOrder betOrder,
			RouletteGameTable table,
			String gameResult,
			Map<Long, BigDecimal> userWinMoney,
			Map<Long, Map<Integer, RouletteUserBetResultResponse.Result>> userDetailWinMoney,
			Map<Long, UserPO> updateUserList, List<AccountRecord> accountRecordList,
			String accountRecordRemark) {

		RouletteUser user = null;
		UserPO userPO = null;
		UserPO dbUserPO = null;
		for (RouletteUser player : table.getPlayers().values()) {
			if (betOrder.getUserId().equals(player.getUserPO().getId())) {
				user = player;
				userPO = user.getUserPO();
				break;
			}
		}
		if (user == null)
			userPO = userService.getUserById(betOrder.getUserId());
		
		if(updateUserList.containsKey(userPO.getId())) {
			dbUserPO = updateUserList.get(userPO.getId());
		}
		
		if(dbUserPO == null) {
			dbUserPO = BeanUtil.cloneTo(userPO);
			dbUserPO.setBalance(BigDecimal.ZERO);
			((LiveUserPO)dbUserPO).setWinMoney(BigDecimal.ZERO);
			updateUserList.put(userPO.getId(), dbUserPO);
		}
		
		// 通过交易项ID,查到相应的交易项(赔率,详情)
		List<TradeItem> byPlayId = RouletteCache.getTradeItemList(betOrder
				.getPlayId());
		if (byPlayId == null || byPlayId.size() == 0) {
			byPlayId = tradeItemService.getByPlayId(betOrder.getPlayId());
			RouletteCache.putTradeItemList(betOrder.getPlayId(), byPlayId);
		}

		BigDecimal resultAmount = null;

		RouletteBetOrderWinLostStatusEnum winLostStatus = null;

		BigDecimal rate = null;

		for (TradeItem tradeItem : byPlayId) {
			if (tradeItem.getId().intValue() != betOrder.getTradeItemId()
					.intValue())
				continue;
			rate = new BigDecimal(1).multiply(tradeItem.getRate());
			String[] tradeItemNums = tradeItem.getAddition().split(",");
			boolean index = false;
			for (String tradeItemNum : tradeItemNums) {
				if (tradeItemNum.equals(gameResult)) {
					resultAmount = betOrder.getAmount().multiply(rate.subtract(new BigDecimal(1)));
					winLostStatus = RouletteBetOrderWinLostStatusEnum.WIN;
					index = true;
				}
			}
			if (!index) {
				resultAmount = new BigDecimal(0).subtract(betOrder.getAmount());
				winLostStatus = RouletteBetOrderWinLostStatusEnum.LOST;
			}

			break;
		}

		betOrder.setRoundResult(gameResult);
		betOrder.setStatus(RouletteBetOrderStatusEnum.SETTLE.get());
		betOrder.setWinLostStatus(winLostStatus.get());
		betOrder.setWinLostAmount(resultAmount);
		betOrder.setSettleRate(rate);
		betOrder.setSettleTime(new Date());
		betOrder.setValidAmount(betOrder.getAmount());

		if(RouletteBetOrderWinLostStatusEnum.WIN == winLostStatus){
			AccountRecord accountRecord = new AccountRecord();
			accountRecord.setBusinessKey(betOrder.getId().toString());
			accountRecord.setUserId(betOrder.getUserId());
			accountRecord.setPreBalance(userPO.getBalance());
			accountRecord.setAmount(betOrder.getAmount().add(betOrder.getWinLostAmount()));
			accountRecord.setType(AccountRecordTypeEnum.RETURN_BONUS.get());
			accountRecord.setTime(new Date());
			long id = accountRecordSnowflakeIdWorker.nextId();
			accountRecord.setId(id);
			accountRecord.setSn("ZR" + id);
			accountRecord.setExecUser(betOrder.getLoginName());
			accountRecord.setRemark(accountRecordRemark);
			accountRecordList.add(accountRecord);
		}

		// 更新用户账户
		userPO.setBalance(userPO.getBalance().add(resultAmount)
				.add(betOrder.getAmount()));
		dbUserPO.setBalance(dbUserPO.getBalance().add(resultAmount)
				.add(betOrder.getAmount()));
		
		BigDecimal winMoney = ((LiveUserPO)userPO).getWinMoney();
		if(winMoney == null) {
			winMoney = new BigDecimal(0);
		}
		if(resultAmount.compareTo(new BigDecimal(0)) > 0) {
			winMoney = winMoney.add(resultAmount);
		}
		((LiveUserPO)userPO).setWinMoney(winMoney);
		((LiveUserPO)dbUserPO).setWinMoney(((LiveUserPO)dbUserPO).getWinMoney().add(winMoney));
		

		if (userWinMoney.containsKey(userPO.getId())) {
			userWinMoney.put(userPO.getId(),
					resultAmount.add(userWinMoney.get(userPO.getId())));
		} else {
			userWinMoney.put(userPO.getId(), resultAmount);
		}

		// 处理动画细节
		if (userDetailWinMoney.containsKey(userPO.getId())) {
			Map<Integer, RouletteUserBetResultResponse.Result> userDetailResultWinMoney = userDetailWinMoney
					.get(userPO.getId());
			if (userDetailResultWinMoney.containsKey(betOrder.getTradeItemId())) {
				RouletteUserBetResultResponse.Result result = userDetailResultWinMoney
						.get(betOrder.getTradeItemId());
				result.amount = result.amount.add(betOrder.getAmount());
				result.winAmount = result.winAmount.add(resultAmount).add(betOrder.getAmount());
			} else {
				RouletteUserBetResultResponse.Result userDetailResult = new RouletteUserBetResultResponse.Result();
				userDetailResult.id = betOrder.getTradeItemId();
				userDetailResult.amount = betOrder.getAmount();
				userDetailResult.winAmount = resultAmount.add(betOrder.getAmount());
				userDetailResultWinMoney.put(betOrder.getTradeItemId(),
						userDetailResult);
			}
		} else {
			Map<Integer, RouletteUserBetResultResponse.Result> userDetailResultWinMoney = new HashMap<>();
			RouletteUserBetResultResponse.Result userDetailResult = new RouletteUserBetResultResponse.Result();
			userDetailResult.id = betOrder.getTradeItemId();
			userDetailResult.amount = betOrder.getAmount();
			userDetailResult.winAmount = resultAmount.add(betOrder.getAmount());
			userDetailResultWinMoney.put(betOrder.getTradeItemId(),
					userDetailResult);
			userDetailWinMoney.put(userPO.getId(), userDetailResultWinMoney);
		}
	}

	/*
	 * 根据map返回key和value的list
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public static List<RouletteUserBetResultResponse.Result> getValueListByMap(
			Map<Integer, RouletteUserBetResultResponse.Result> map) {

		List<RouletteUserBetResultResponse.Result> list = new ArrayList<>();
		if (map == null) {
			return list;
		}
		int i = 0;
		for(Entry<Integer, RouletteUserBetResultResponse.Result> item :map.entrySet()) {
			Result value = item.getValue();
			if(value.winAmount.setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(BigDecimal.ZERO) > 0) {
				i++;
			}
			list.add(value);
		}
		if(i > 0) {
			list.sort((RouletteUserBetResultResponse.Result h1, RouletteUserBetResultResponse.Result h2) -> h2.winAmount.compareTo(h1.winAmount));
			return list;
		}
		return null;
	}

}
