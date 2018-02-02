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
 * 免拥玩法
 * 
 * @author alan
 * @date 2017年5月3日 上午10:21:10
 */
public class FreeCommissionPlayRule extends NormalPlayRule {
	
	private Logger log = LoggerFactory.getLogger(FreeCommissionPlayRule.class);

	@Override
	public void calResult(BetOrder order, RoundExtPO roundExtPO) {
		super.calResult(order, roundExtPO);
		
		TradeItem tradeItem = order.getTradeItem();
		
		if (gameReusltList.contains(TradeItemEnum.SUPER_SIX) && TradeItemEnum.BANKER == tradeItem.getTradeItemEnum() ) {
			rate = rate.divide(new BigDecimal(2));
			resultAmount = order.getAmount().multiply(rate);
			winLostStatus = BetOrderWinLostStatusEnum.WIN;
			return ;
		}
		
	}

	@Override
	public void getResultEnum() {
		super.getResultEnum();
		
		List<TradeItemEnum> result = new ArrayList<>();
		String index = gameReuslt.substring(3, 4);
		if("6".equals(index) && "1".equals(gameReuslt.substring(0, 1))) {
			result.add(TradeItemEnum.SUPER_SIX);
		}
		gameReusltList.addAll(result);
		log.debug("【百家乐结算】免拥结果：" + result);
	}


}
