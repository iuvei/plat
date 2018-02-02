package com.na.roulette.socketserver.listener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.common.enums.RoundStatusEnum;
import com.na.baccarat.socketserver.entity.BetOrder;
import com.na.baccarat.socketserver.entity.TradeItem;
import com.na.roulette.socketserver.common.enums.RouletteBetOrderStatusEnum;
import com.na.roulette.socketserver.common.enums.RouletteBetOrderWinLostStatusEnum;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.common.enums.GameEnum;
import com.na.user.socketserver.common.event.AbnormalGameResultEvent;
import com.na.user.socketserver.entity.GameTablePO;
import com.na.user.socketserver.entity.RoundPO;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.service.IBetOrderService;
import com.na.user.socketserver.service.IGameTableService;
import com.na.user.socketserver.service.IRoundExtService;
import com.na.user.socketserver.service.IRoundService;
import com.na.user.socketserver.service.ITradeItemService;
import com.na.user.socketserver.service.IUserService;

/**
 * 轮盘异常桌台结算
 * 
 * @author alan
 * @date 2017年6月9日 下午2:11:15
 */
@Component
public class RouletteAbnormalGameResultListener implements ApplicationListener<AbnormalGameResultEvent> {
	
	private final static Logger log = LoggerFactory.getLogger(RouletteAbnormalGameResultListener.class);
	
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
	private ITradeItemService tradeItemService;
	
	@Override
	public void onApplicationEvent(AbnormalGameResultEvent event) {
		Integer roundId = event.getSource();
		log.debug("异常局结算实体桌:" + roundId);
		
		RoundPO roundPO = roundService.getRound(roundId);
		
		if(roundId != 0 && roundPO != null ) {
			
			if(GameEnum.ROULETTE != AppCache.getGame(roundPO.getGameId()).getGameEnum() ) {
				return ;
			}
			
			if(roundPO.getStatusEnum() == RoundStatusEnum.FINISH) {
				throw SocketException.createError("该桌已经结算");
			}
			
			roundPO.setStatusEnum(RoundStatusEnum.FINISH);
			
			//根据卡牌计算游戏结果
			roundPO.setResult(roundPO.getResult());
			roundPO.setEndTime(new Date());
			
			GameTablePO gameTablePO = gameTableService.findGametableByTid(roundPO.getGameTableId());
			
			settlementTable(roundPO);
			
			roundService.update(roundPO);
			
			//发布
//            redisTemplate.convertAndSend("AbnormalGameResultSuccess", "success");
			
			log.info("【异常桌台】  " + gameTablePO.getName() + " 局ID： " + roundPO.getId() +" 结算完成");
		} else {
			throw SocketException.createError("该局不存在 roundId: " + roundId);
		}
    }
    
    private void settlementTable(RoundPO roundPO) {
    	//获取所有订单
		List<BetOrder> berOrderList = betOrderService.getBetOrderByTable(roundPO.getGameTableId(), roundPO);
		
		Set<UserPO> userPOList = new HashSet<>();
		
		berOrderList.parallelStream().forEach( order -> {
			settlement(order, roundPO.getResult(), userPOList);
		});
		
		if(berOrderList != null && berOrderList.size() > 0) {
			//批量更新订单信息
			betOrderService.batchUpdate(berOrderList);
			//执行批量更新任务
			userService.batchUpdateBalance(new ArrayList<UserPO>(userPOList), "GameResult");
		}
    }
    
    /**
	 * 处理输赢情况
	 */
	private void settlement(BetOrder betOrder, String gameResult, Set<UserPO> userPOList) {
		UserPO userPO = userService.getUserById(betOrder.getUserId());
		
		TradeItem tradeItem = tradeItemService.getTradeItemById(betOrder.getTradeItemId());
		
		BigDecimal resultAmount = null;

		RouletteBetOrderWinLostStatusEnum winLostStatus = null;

		BigDecimal rate = null;
		
		rate = new BigDecimal(1).multiply(tradeItem.getRate());
		
		String[] tradeItemNums = tradeItem.getAddition().split(",");
		boolean index = false;
		for (String tradeItemNum : tradeItemNums) {
			if (tradeItemNum.equals(gameResult)) {
				resultAmount = betOrder.getAmount().multiply(rate);
				winLostStatus = RouletteBetOrderWinLostStatusEnum.WIN;
				index = true;
			}
		}
		if (!index) {
			resultAmount = new BigDecimal(0).subtract(betOrder.getAmount());
			winLostStatus = RouletteBetOrderWinLostStatusEnum.LOST;
		}


		betOrder.setRoundResult(gameResult);
		betOrder.setStatus(RouletteBetOrderStatusEnum.SETTLE.get());
		betOrder.setWinLostStatus(winLostStatus.get());
		betOrder.setWinLostAmount(resultAmount);
		betOrder.setSettleRate(rate);
		betOrder.setSettleTime(new Date());
		betOrder.setValidAmount(betOrder.getAmount());

//		betOrderService.update(betOrder);
		// 更新用户账户
//		userService.updateUserBalance(userPO.getUid(), resultAmount, resultAmount.add(betOrder.getAmount()));

		userPO.setBalance(userPO.getBalance().add(resultAmount));

//		userExtService.updateUserExtBalance(userPO.getUid(), resultAmount, betOrder.getAmount(), resultAmount.add(betOrder.getAmount()));
		
		userPOList.add(userPO);
	}
}
