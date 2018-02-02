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

/**
 * B27玩法
 * 
 * @author alan
 * @date 2017年5月3日 上午10:21:10
 */
public class B27PlayRule extends NormalPlayRule {
	
	private Logger log = LoggerFactory.getLogger(B27PlayRule.class);

	@Override
	public void calResult(BetOrder order, RoundExtPO roundExtPO) {
		super.calResult(order, roundExtPO);
		
		TradeItem tradeItem = order.getTradeItem();
		
		if (gameReusltList.contains(TradeItemEnum.B27) && TradeItemEnum.BANKER == tradeItem.getTradeItemEnum()) {
			if("2".equals(gameReuslt.substring(3, 4))) {
				rate = new BigDecimal(1).multiply(new BigDecimal(2));
				resultAmount = order.getAmount().multiply(rate);
				winLostStatus = BetOrderWinLostStatusEnum.WIN;
			} else if ("7".equals(gameReuslt.substring(3, 4))) {
				rate = new BigDecimal(1).multiply(new BigDecimal(0.5));
				resultAmount = order.getAmount().multiply(rate);
				winLostStatus = BetOrderWinLostStatusEnum.WIN;
			}
			return ;
		}
		
	}

	@Override
	public void getResultEnum() {
		super.getResultEnum();
		
		List<TradeItemEnum> result = new ArrayList<>();
		String index = gameReuslt.substring(3, 4);
		if(("2".equals(index) || "7".equals(index)) && "1".equals(gameReuslt.substring(0, 1))) {
			result.add(TradeItemEnum.B27);
		}
		gameReusltList.addAll(result);
		log.debug("【百家乐结算】B27结果：" + result);
	}


}
