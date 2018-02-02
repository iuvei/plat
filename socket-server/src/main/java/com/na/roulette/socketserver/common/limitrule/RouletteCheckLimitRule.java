package com.na.roulette.socketserver.common.limitrule;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.na.baccarat.socketserver.entity.TradeItem;
import com.na.roulette.socketserver.command.requestpara.BetPara.Bet;
import com.na.roulette.socketserver.entity.RouletteUser;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.entity.LiveUserPO;
import com.na.user.socketserver.entity.UserChipsPO;
import com.na.user.socketserver.exception.SocketException;



/**
 * 计算最高赢额算法
 * 个人限红,台红需要使用
 * @author alan
 * @date 2017年6月14日 下午4:20:40
 */
public class RouletteCheckLimitRule {

	private enum LimitItem {
		ONE,
		TWO,
		STR,
		THR,
		ANG,
		FOR,
		LIN,
		FIG,
		COL,
//		RED_BLA,
		RED,
		BLACK,
//		ODD_EVE,
		ODD,
		EVE,
//		SMA_BIG,
		SMALL,
		BIG,
	}

	//每个交易项输赢钱
	private Map<Integer, BigDecimal> tradeItemMap;

	private Map<Integer, BigDecimal> totalResult;
	//相对输赢项的钱
//	private Map<LimitItem, BigDecimal> absoluteTotalResult;

	private Map<Integer, BigDecimal> result;

//	private Map<LimitItem, BigDecimal> absoluteResult;

	public RouletteCheckLimitRule() {
		totalResult = new HashMap<>();
//		absoluteTotalResult = new HashMap<>();
	}

	/**
	 * 计算用户限红
	 *  @param betList
	 * @param userChipsPO
     * @param user
     */
	public void countUserChips(List<Bet> betList, UserChipsPO userChipsPO, RouletteUser user) {
		countMaxWinMoney(betList, new BigDecimal(userChipsPO.getMin()), new BigDecimal(userChipsPO.getMax()),user);
	}

	public void countMaxWinMoney(List<Bet> betList, BigDecimal limitMin, BigDecimal limitMax, RouletteUser user) {
		result = new HashMap<>();
		tradeItemMap = new HashMap<>();
//		absoluteResult = new HashMap<>();

		betList.parallelStream().forEach(item -> {
			TradeItem tradeItem = AppCache.getTradeItem(item.tradeId);
			//计算赢额
//			tradeItemMap.put(tradeItem, this.count(tradeItem, item.amount));
			
			tradeItemMap.put(item.tradeId, this.count(tradeItem, item.amount));
		});

		TradeItem tradeItem = AppCache.getTradeItem(betList.get(0).tradeId);
		List<TradeItem> tradeItemList = AppCache.getTradeItemListByPlayId(tradeItem.getPlayId());

		tradeItemList.forEach(item -> {
			isAllowBet(item, limitMin, limitMax);
		});

		checkPersonMaxWinMoney(user);

		result.forEach((key, value) -> {
			if (totalResult.containsKey(key)) {
				totalResult.put(key, totalResult.get(key).add(value));
			} else {
				totalResult.put(key, value);
			}
		});

//		absoluteResult.forEach((key, value) -> {
//			if (absoluteTotalResult.containsKey(key)) {
//				absoluteTotalResult.put(key, absoluteTotalResult.get(key).add(value));
//			} else {
//				absoluteTotalResult.put(key, value);
//			}
//		});

//		totalResult.forEach((key, value) -> {
//			System.out.println("当前下注总计:" + key + ",投注额:" + value);
//		});

	}

	private void isAllowBet(TradeItem tradeItem, BigDecimal limitMin, BigDecimal limitMax) {
//		LimitItem limitItem = null;
//		switch (tradeItem.getRouletteTradeItemEnum()) {
//			case ANGLE:
//				limitItem = LimitItem.ANG;
//				break;
//			case COLUMN:
//				limitItem = LimitItem.COL;
//				break;
//			case DIRECT:
//				limitItem = LimitItem.ONE;
//				break;
//			case DOZEN:
//				limitItem = LimitItem.FIG;
//				break;
//			case FOUR:
//				limitItem = LimitItem.FOR;
//				break;
//			case LINE:
//				limitItem = LimitItem.LIN;
//				break;
//			case POINT:
//				limitItem = LimitItem.TWO;
//				break;
//			case STREET:
//				limitItem = LimitItem.STR;
//				break;
//			case THREE:
//				limitItem = LimitItem.THR;
//				break;
//			case BIG:
//				limitItem = LimitItem.BIG;
//				break;
//			case SMALL:
//				limitItem = LimitItem.SMALL;
//				break;
//			case ODD:
//				limitItem = LimitItem.ODD;
//				break;
//			case EVEN:
//				limitItem = LimitItem.EVE;
//				break;
//			case BLACK:
//				limitItem = LimitItem.BLACK;
//				break;
//			case RED:
//				limitItem = LimitItem.RED;
//				break;
//			default:
//				break;
//		}
//		if (limitItem == null) return;

		BigDecimal betWinMoney = tradeItemMap.containsKey(tradeItem.getId()) ? tradeItemMap.get(tradeItem.getId()) : new BigDecimal(0);

		BigDecimal finalMoney = totalResult.containsKey(tradeItem.getId()) ? totalResult.get(tradeItem.getId()) : new BigDecimal(0);
		BigDecimal resultFinalMoney = result.containsKey(tradeItem.getId()) ? result.get(tradeItem.getId()) : new BigDecimal(0);

		BigDecimal nowFinalMoney = betWinMoney.add(resultFinalMoney).abs();

		BigDecimal totailFinalMoney = finalMoney.add(nowFinalMoney).abs();


		if (totailFinalMoney.compareTo(new BigDecimal(0)) > 0 && limitMin.compareTo(totailFinalMoney) > 0) {
			throw SocketException.createError("bet.lower.min");
		}

		if (limitMax.compareTo(totailFinalMoney) < 0) {
			throw SocketException.createError("bet.higher.max");
		}
		//totalResult.put(limitItem, finalMoney);
		result.put(tradeItem.getId(), nowFinalMoney);
	}

//	private BigDecimal getItem(TradeItem item) {
//
//		if (item == null) {
//			return new BigDecimal(0);
//		}
//		return tradeItemMap.containsKey(item) ? tradeItemMap.get(item) : new BigDecimal(0);
//	}

	/**
	 * 计算赢额
	 */
	private BigDecimal count(TradeItem item, BigDecimal betNumber) {
		return betNumber.multiply(item.getRate().subtract(new BigDecimal(1)));
	}

	//5.检测最高赢额和最高余额
	private void checkPersonMaxWinMoney(RouletteUser user) {
		BigDecimal personBiggestBalance = ((LiveUserPO) (user.getUserPO())).getBiggestBalance();
		BigDecimal personMaxWinMoney = ((LiveUserPO) (user.getUserPO())).getBiggestWinMoney();
		BigDecimal personWinMoney = ((LiveUserPO) (user.getUserPO())).getWinMoney();
		if (personBiggestBalance == null) {
			personBiggestBalance = BigDecimal.ZERO;
		}
		if (personWinMoney == null) personWinMoney = BigDecimal.ZERO;

		BigDecimal sumTotalResults = totalResult.size() > 0? totalResult.values().stream().reduce(BigDecimal::add).get():BigDecimal.ZERO;
		BigDecimal sumResults =  result.values().stream().reduce(BigDecimal::add).get();
		BigDecimal roundWinMoney = sumResults.add(sumTotalResults);

		personWinMoney = personWinMoney.add(roundWinMoney);
		if (personMaxWinMoney != null && personMaxWinMoney.compareTo(BigDecimal.ZERO) != 0 && personMaxWinMoney.compareTo(personWinMoney) < 0) {
			throw SocketException.createError("maxwinmoney.beyond");
		}
		//最高余额
		BigDecimal personBalance = user.getUserPO().getBalance().add(roundWinMoney);
		if (personBiggestBalance.compareTo(BigDecimal.ZERO) != 0 && personBiggestBalance.compareTo(personBalance) < 0) {
			throw SocketException.createError("maxbalance.beyond");
		}
	}

}