package com.na.baccarat.socketserver.common.playrule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.na.baccarat.socketserver.common.enums.BetOrderWinLostStatusEnum;
import com.na.baccarat.socketserver.common.enums.TradeItemEnum;
import com.na.baccarat.socketserver.entity.BetOrder;
import com.na.baccarat.socketserver.entity.TradeItem;
import com.na.user.socketserver.entity.RoundExtPO;
import com.na.user.socketserver.exception.SocketException;

/**
 * 普通玩法
 * 
 * @author alan
 * @date 2017年5月3日 上午10:21:10
 */
public class NormalPlayRule extends AbstractPlayRule {
	
	private Logger log = LoggerFactory.getLogger(NormalPlayRule.class);
	
	@Override
	public void calResult(BetOrder order, RoundExtPO roundExtPO) {
		TradeItem tradeItem = order.getTradeItem();
		rate = tradeItem.getRate().subtract(new BigDecimal(1));
		if(gameReusltList.contains(tradeItem.getTradeItemEnum())) {
			if(gameReusltList.contains(TradeItemEnum.TIE)) {
				if(TradeItemEnum.BANKER_CASE == tradeItem.getTradeItemEnum()
						|| TradeItemEnum.PLAYER_CASE == tradeItem.getTradeItemEnum()) {
					resultAmount = new BigDecimal(0);
					winLostStatus = BetOrderWinLostStatusEnum.TIE;
					return ;
				}
			}
			
			resultAmount = order.getAmount().multiply(rate);
			winLostStatus = BetOrderWinLostStatusEnum.WIN;
		} else {
			if(gameReusltList.contains(TradeItemEnum.TIE)) {
				if(TradeItemEnum.BANKER == tradeItem.getTradeItemEnum()
						|| TradeItemEnum.PLAYER == tradeItem.getTradeItemEnum()) {
					resultAmount = new BigDecimal(0);
					winLostStatus = BetOrderWinLostStatusEnum.TIE;
					return ;
				}
			}
			
			if(gameReusltList.contains(TradeItemEnum.TIE)) {
				if(TradeItemEnum.BANKER_CASE == tradeItem.getTradeItemEnum()
						|| TradeItemEnum.PLAYER_CASE == tradeItem.getTradeItemEnum()) {
					resultAmount = new BigDecimal(0);
					winLostStatus = BetOrderWinLostStatusEnum.TIE;
					return ;
				}
			}
			
			resultAmount = new BigDecimal(0).subtract(order.getAmount());
			winLostStatus = BetOrderWinLostStatusEnum.LOST;
		}
		
	}
	
	@Override
	public void getResultEnum() {
		if(gameReuslt.length() < 4) {
			throw SocketException.createError("游戏结果错误");
		}
		
		List<TradeItemEnum> result = new ArrayList<>();
		String index = gameReuslt.substring(0, 1);
		if("1".equals(index)) {
			result.add(TradeItemEnum.BANKER);
		} else if ("2".equals(index)) {
			result.add(TradeItemEnum.PLAYER);
		} else if ("3".equals(index)) {
			result.add(TradeItemEnum.TIE);
		}
		
		index = gameReuslt.substring(1, 2);
		if("1".equals(index)) {
			result.add(TradeItemEnum.BANKER_CASE);
		} else if("2".equals(index)) {
			result.add(TradeItemEnum.PLAYER_CASE);
		} else if("3".equals(index)) {
			result.add(TradeItemEnum.BANKER_CASE);
			result.add(TradeItemEnum.PLAYER_CASE);
		}
		
		index = gameReuslt.substring(2, 3);
		if("1".equals(index)) {
			result.add(TradeItemEnum.BANKER_DOUBLE);
		} else if("2".equals(index)) {
			result.add(TradeItemEnum.PLAYER_DOUBLE);
		} else if("3".equals(index)) {
			result.add(TradeItemEnum.BANKER_DOUBLE);
			result.add(TradeItemEnum.PLAYER_DOUBLE);
		}
		
		index = gameReuslt.substring(3, 4);
		
		gameReusltList = result;
		log.debug("【百家乐结算】普通结果：" + result);
	}
	
}
