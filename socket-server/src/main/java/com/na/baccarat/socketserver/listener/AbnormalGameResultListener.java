package com.na.baccarat.socketserver.listener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.sendpara.SendDealerResponse;
import com.na.baccarat.socketserver.command.sendpara.UserMessageResponse;
import com.na.baccarat.socketserver.common.enums.BetOrderStatusEnum;
import com.na.baccarat.socketserver.common.enums.PlayEnum;
import com.na.baccarat.socketserver.common.enums.RoundStatusEnum;
import com.na.baccarat.socketserver.common.playrule.AbstractPlayRule;
import com.na.baccarat.socketserver.common.playrule.B27PlayRule;
import com.na.baccarat.socketserver.common.playrule.FreeCommissionPlayRule;
import com.na.baccarat.socketserver.common.playrule.NormalPlayRule;
import com.na.baccarat.socketserver.entity.BetOrder;
import com.na.baccarat.socketserver.entity.GameTable;
import com.na.baccarat.socketserver.entity.User;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.send.UserCommand;
import com.na.user.socketserver.common.enums.GameEnum;
import com.na.user.socketserver.common.event.AbnormalGameResultEvent;
import com.na.user.socketserver.entity.GameTablePO;
import com.na.user.socketserver.entity.RoundExtPO;
import com.na.user.socketserver.entity.RoundPO;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.service.IBetOrderService;
import com.na.user.socketserver.service.IGameTableService;
import com.na.user.socketserver.service.IRoundExtService;
import com.na.user.socketserver.service.IRoundService;
import com.na.user.socketserver.service.IUserService;
import com.na.user.socketserver.util.BeanUtil;
import com.na.user.socketserver.util.SocketUtil;

/**
 * 异常桌台结算
 * 
 * @author alan
 * @date 2017年6月9日 下午2:11:15
 */
@Component
public class AbnormalGameResultListener implements ApplicationListener<AbnormalGameResultEvent> {
	
	private final static Logger log = LoggerFactory.getLogger(AbnormalGameResultListener.class);
	
	@Autowired
	private SocketIOServer socketIOServer;
	
	@Autowired
	private IRoundService roundService;
	
	@Autowired
	private IBetOrderService betOrderService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IRoundExtService roundExtService;
	
	@Autowired
	private IGameTableService gameTableService;
	
	@Autowired
    private RedisTemplate<String,String> redisTemplate;
	
	@Autowired
    private UserCommand userCommand;
	
	@Override
	public void onApplicationEvent(AbnormalGameResultEvent event) {
		Integer roundId = event.getSource();
		log.info("异常局结算实体桌:" + roundId);
		
		RoundPO roundPO = roundService.getRound(roundId);
		
		if(roundId != 0 && roundPO != null ) {
			
			if(GameEnum.BACCARAT != AppCache.getGame(roundPO.getGameId()).getGameEnum() ) {
				return ;
			}
			
			if(roundPO.getStatusEnum() == RoundStatusEnum.FINISH) {
				throw SocketException.createError("该局已经结算 :" + roundId);
			}
			
			RoundExtPO roundExtPO = roundExtService.getRoundExt(roundId);
			
			roundPO.setStatusEnum(RoundStatusEnum.FINISH);
			
			//根据卡牌计算游戏结果
			Map<PlayEnum,AbstractPlayRule> resultMap = genResult(roundExtPO);
			final String gameResult = resultMap.get(PlayEnum.NORMAL).getGameReuslt();
			roundPO.setResult(gameResult);
			roundPO.setEndTime(new Date());
			
			final String showResult = getShowResult(gameResult, roundPO, roundExtPO);
		 	
			GameTablePO gameTablePO = gameTableService.findGametableByTid(roundPO.getGameTableId());
			
			settlementNormalTable(gameTablePO, roundPO, roundExtPO, resultMap, showResult);
			
			roundExtService.update(roundExtPO);
			roundService.update(roundPO);
			
			//通知荷官端  异常桌台处理完毕
			GameTable gameTable = BaccaratCache.getGameTableById(gameTablePO.getId());
			if(gameTable != null) {
				User dealerUser = gameTable.getDealer();
				if(dealerUser != null) {
					SocketIOClient dealerClient = SocketUtil.getClientByUser(socketIOServer, dealerUser.getUserPO());
					if(dealerClient != null) {
						SendDealerResponse sendDealerResponse = new SendDealerResponse();
						sendDealerResponse.setCommand("recoverTable");
						userCommand.send(dealerClient, RequestCommandEnum.SEND_DEALER_MESSAGE, sendDealerResponse);
					} else {
						log.warn("【异常桌台】  " + gameTablePO.getName() + " 局ID： " + roundPO.getId() +" 荷官不存在");
					}
				} else {
					log.warn("【异常桌台】  " + gameTablePO.getName() + " 局ID： " + roundPO.getId() +" 荷官不存在");
				}
			}
			
			log.info("【异常桌台】  " + gameTablePO.getName() + " 局ID： " + roundPO.getId() +" 结算完成");
		} else {
			throw SocketException.createError("该局不存在 roundId: " + roundId);
		}
    }
    
    /**
	 * 桌结算
	 */
	private void settlementNormalTable(GameTablePO gameTablePO, RoundPO roundPO, RoundExtPO roundExtPO,
			Map<PlayEnum,AbstractPlayRule> resultMap, String showResult) {
		//获取所有订单
		List<BetOrder> berOrderList = betOrderService.getBetOrderByTable(roundPO.getGameTableId(), roundPO);
		
		/**
		 * 用户赢钱额度 若用户未投注  集合则不存在该用户
		 * key  用户ID   value  用户赢得钱
		 */
		Map<Long,BigDecimal> userWinMoney = new HashMap<>();
		Set<UserPO> userPOList = new HashSet<>();
		
		berOrderList.parallelStream().forEach( order -> {
			settlement(order, roundPO, roundExtPO, resultMap, userPOList, showResult, userWinMoney);
		});
		
		if(berOrderList != null && berOrderList.size() > 0) {
			//批量更新订单信息
			betOrderService.batchUpdate(berOrderList);
			//执行批量更新任务
			userService.batchUpdateBalance(new ArrayList<UserPO>(userPOList), "GameResult");
		}
		
		//通知用户结算
		for(Entry<Long,BigDecimal> entry : userWinMoney.entrySet()) {
			UserPO userPO = AppCache.getLoginUser(entry.getKey());
			SocketIOClient client = SocketUtil.getClientByUser(socketIOServer, userPO);
			if(client != null) {
				String content = "您在百家乐 " + gameTablePO.getName() + "桌, " + roundPO.getId() + "局,";
				if(entry.getValue().compareTo(BigDecimal.ZERO) >= 0) {
					content += "赢" + entry.getValue();
				} else if(entry.getValue().compareTo(BigDecimal.ZERO) < 0) {
					content += "输" + entry.getValue();
				}
				UserMessageResponse userMessageResponse = new UserMessageResponse();
				userMessageResponse.setContent(content);
				userCommand.send(client, RequestCommandEnum.CLIENT_USER_MESSAGE, userMessageResponse);
			}
		}
	}

	/**
	 * 结算
	 */
	private void settlement(BetOrder order, RoundPO roundPO, RoundExtPO roundExtPO,
			Map<PlayEnum,AbstractPlayRule> resultMap, Set<UserPO> userPOList, 
			String showResult, Map<Long,BigDecimal> userWinMoney) {
		
		if(BetOrderStatusEnum.SETTLE == order.getStatusEnum() || BetOrderStatusEnum.CANCEL == order.getStatusEnum()) {
			log.warn("该订单已经处理 ID: " + order.getId());
			return;
		}
		
		UserPO userPO = userService.getUserById(order.getUserId());
		
		AbstractPlayRule rule = null;
		if (resultMap.containsKey(order.getPlayEnum())) {
			rule = BeanUtil.cloneTo(resultMap.get(order.getPlayEnum()));
		} else {
			throw SocketException.createError("找不到对应玩法");
		}
		if(rule == null) {
			throw SocketException.createError("该订单找不到对应玩法");
		}
		
		//结算
		rule.calResult(order, roundExtPO);
		
		roundPO.setResult(rule.getGameReuslt());
		
		order.setRoundResult(showResult);
		order.setStatus(BetOrderStatusEnum.SETTLE.get());
		order.setWinLostStatus(rule.getWinLostStatus().get());
		order.setWinLostAmount(rule.getResultAmount());
		order.setSettleRate(rule.getRate().add(new BigDecimal(1)));
		order.setSettleTime(new Date());
		order.setValidAmount(order.getAmount());
		
		userPO.setBalance(userPO.getBalance().add(rule.getResultAmount()).add(order.getAmount()));
		
		
		userPOList.add(userPO);
		
		/**
		 * 统计用户输赢金额
		 */
		if(userWinMoney.containsKey(userPO.getId())) {
			userWinMoney.put(userPO.getId(), rule.getResultAmount().add(userWinMoney.get(userPO.getId())));
		} else {
			userWinMoney.put(userPO.getId(), rule.getResultAmount());
		}
	}
	
	/**
	 * 根据卡牌信息生成游戏结果
	 * @param roundExt
	 * @return
	 */
	private Map<PlayEnum,AbstractPlayRule> genResult(RoundExtPO roundExtPO) {
		Map<PlayEnum,AbstractPlayRule> resultMap = new HashMap<PlayEnum, AbstractPlayRule>();
		AbstractPlayRule normalPlayRule = new NormalPlayRule();
		normalPlayRule.getResultStr(roundExtPO);
		normalPlayRule.getResultEnum();
		resultMap.put(PlayEnum.NORMAL, normalPlayRule);
		
		AbstractPlayRule freeCommissionPlayRule = new FreeCommissionPlayRule();
		freeCommissionPlayRule.setGameReuslt(normalPlayRule.getGameReuslt());
		freeCommissionPlayRule.getResultEnum();
		resultMap.put(PlayEnum.FREE_COMMISSION, freeCommissionPlayRule);
		
		AbstractPlayRule b27PlayRule = new B27PlayRule();
		b27PlayRule.setGameReuslt(normalPlayRule.getGameReuslt());
		b27PlayRule.getResultEnum();
		resultMap.put(PlayEnum.B27, b27PlayRule);
		return resultMap;
	}
	
	/**
	 * 生成展示结果
	 */
	private String getShowResult(String result, RoundPO roundPO, RoundExtPO roundExtPO) {
		Map<String,Object> resultMap = new HashMap<>();
		
		char first = result.charAt(0);
		char third = result.charAt(2);
		int bpresult = -1;
		if(first == '1') {
			if(third == '1') {
				bpresult = 3;
			} else if (third == '2') {
				bpresult = 4;
			} else if (third == '3') {
				bpresult = 9;
			} else {
				bpresult = 0;
			}
		} else if (first == '2') {
			if(third == '1') {
				bpresult = 7;
			} else if (third == '2') {
				bpresult = 8;
			} else if (third == '3') {
				bpresult = 11;
			} else {
				bpresult = 1;
			}
		} else if (first == '3') {
			if(third == '1') {
				bpresult = 5;
			} else if (third == '2') {
				bpresult = 6;
			} else if (third == '3') {
				bpresult = 10;
			} else {
				bpresult = 2;
			}
		} else {
			throw SocketException.createError("展示结果错误");
		}
		
		resultMap.put("bpresult", bpresult);
		resultMap.put("betnums", roundPO.getBootNumber());
		List<Map<String,Object>> bankerCardList = new ArrayList<>();
		List<Map<String,Object>> playerCardList = new ArrayList<>();
		Map<String,Object> card = new HashMap<>();
		
		card.put("m", roundExtPO.getBankCard1Mode());
		card.put("n", roundExtPO.getBankCard1Number());
		bankerCardList.add(card);
		
		card = new HashMap<>();
		card.put("m", roundExtPO.getBankCard2Mode());
		card.put("n", roundExtPO.getBankCard2Number());
		bankerCardList.add(card);
		
		if(roundExtPO.getBankCard3Mode() != null) {
			card = new HashMap<>();
			card.put("m", roundExtPO.getBankCard3Mode());
			card.put("n", roundExtPO.getBankCard3Number());
			bankerCardList.add(card);
		}
		
		card = new HashMap<>();
		card.put("m", roundExtPO.getPlayerCard1Mode());
		card.put("n", roundExtPO.getPlayerCard1Number());
		playerCardList.add(card);
		
		card = new HashMap<>();
		card.put("m", roundExtPO.getPlayerCard2Mode());
		card.put("n", roundExtPO.getPlayerCard2Number());
		playerCardList.add(card);
		
		if(roundExtPO.getPlayerCard3Mode() != null) {
			card = new HashMap<>();
			card.put("m", roundExtPO.getPlayerCard3Mode());
			card.put("n", roundExtPO.getPlayerCard3Number());
			playerCardList.add(card);
		}
		
		resultMap.put("b", bankerCardList);
		resultMap.put("p", playerCardList);
		resultMap.put("result", result);
		return JSONObject.toJSONString(resultMap);
	}

}
