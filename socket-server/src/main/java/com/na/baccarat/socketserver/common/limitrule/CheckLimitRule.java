package com.na.baccarat.socketserver.common.limitrule;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.na.baccarat.socketserver.command.requestpara.BetPara.Bet;
import com.na.baccarat.socketserver.common.enums.TradeItemEnum;
import com.na.baccarat.socketserver.entity.TradeItem;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.entity.UserChipsPO;
import com.na.user.socketserver.exception.SocketException;

/**
 * 计算最高赢额算法
 * 个人限红,台红需要使用
 * @author alan
 * @date 2017年6月14日 下午4:20:40
 */
public class CheckLimitRule {
	
	private Logger log = LoggerFactory.getLogger(CheckLimitRule.class);
	
	private enum LimitItem {
		BP,
//		BP_PAIRS,
		BANK_PAIRS,
		PLAYER_PAIRS,
//		BP_CASE,
		BANK_CASE,
		PLAYER_CASE,
		TIE,
		SUPER_SIX,
		B27
	}
	
	private Map<TradeItem,BigDecimal> tradeItemMap;
	
	private Map<LimitItem,BigDecimal> totalResult;
	
	private Map<LimitItem,BigDecimal> absoluteTotalResult;
	
	private Map<LimitItem,BigDecimal> result;
	
	private Map<LimitItem,BigDecimal> absoluteResult;
	
	// 可重用同步锁
	private ReadWriteLock LOCK_CURRENT_LOGIN = new ReentrantReadWriteLock();
	private ReadWriteLock LOCK_CURRENT_LOGIN2 = new ReentrantReadWriteLock();
	
	public CheckLimitRule() {
		totalResult = new ConcurrentHashMap<>();
		absoluteTotalResult = new ConcurrentHashMap<>();
		result = new ConcurrentHashMap<>();
		tradeItemMap = new ConcurrentHashMap<>();
		absoluteResult = new ConcurrentHashMap<>();
	}
	
	/**
	 * 计算用户限红
	 * @param betList
	 * @param userChipsPO
	 */
	public void countUserChips(List<Bet> betList, UserChipsPO userChipsPO) {
		countMaxWinMoney(betList, new BigDecimal(userChipsPO.getMin()), new BigDecimal(userChipsPO.getMax()));
	}
	
	public void countMaxWinMoney(List<Bet> betList, BigDecimal limitMin, BigDecimal limitMax) {
		result.clear();
		tradeItemMap.clear();
		absoluteResult.clear();
		
		betList.parallelStream().forEach( item -> {
			TradeItem tradeItem = AppCache.getTradeItem(item.tradeId);
			//计算赢额
			tradeItemMap.put(tradeItem, this.count(tradeItem,item.amount));
		});
		
		TradeItem tradeItem = AppCache.getTradeItem(betList.get(0).tradeId);
		List<TradeItem> tradeItemList = AppCache.getTradeItemListByPlayId(tradeItem.getPlayId());
		
		new LimitCalcPattern() {
			@Override
			public BigDecimal calc() {
				List<TradeItem> itemColl = tradeItemList.stream().filter( item -> {
					return item.getTradeItemEnum() == TradeItemEnum.BANKER || 
							item.getTradeItemEnum() == TradeItemEnum.PLAYER;
				}).collect(Collectors.toList());
				
				if(itemColl.size() < 2) {
					return new BigDecimal(0);
				}
				
				BigDecimal onceAbsolute = absoluteTotalResult.containsKey(LimitItem.BP) ? absoluteTotalResult.get(LimitItem.BP) : new BigDecimal(0);
				BigDecimal onceAdd = getItem(itemColl.get(0)).add(getItem(itemColl.get(1)));
				onceAbsolute = onceAbsolute.add(onceAdd);
				
				if(onceAbsolute.compareTo(new BigDecimal(0)) > 0 && limitMin.compareTo(onceAbsolute) > 0) {
					throw SocketException.createError("bet.lower.min");
				}
				
				BigDecimal onceResult = totalResult.containsKey(LimitItem.BP) ? totalResult.get(LimitItem.BP) : new BigDecimal(0);
				BigDecimal onceSubtract = getItem(itemColl.get(0)).subtract(getItem(itemColl.get(1)));
				onceResult = onceResult.add(onceSubtract);
				
				if(limitMax.compareTo(onceResult.abs()) < 0) {
					throw SocketException.createError("bet.higher.max");
				}
				
				LOCK_CURRENT_LOGIN2.writeLock().lock();
				try {
					absoluteResult.put(LimitItem.BP, onceAdd);
				} finally {
					LOCK_CURRENT_LOGIN2.writeLock().unlock();
				}
				LOCK_CURRENT_LOGIN.writeLock().lock();
				try {
					result.put(LimitItem.BP, onceSubtract);
				} finally {
					LOCK_CURRENT_LOGIN.writeLock().unlock();
				}
				
				return onceSubtract;
			}
		}.calc();
		
		isAllowBet(LimitItem.BANK_PAIRS, limitMin, limitMax, new LimitCalcPattern() {
			@Override
			public BigDecimal calc() {
				List<TradeItem> itemColl = tradeItemList.stream().filter( item -> {
					return item.getTradeItemEnum() == TradeItemEnum.BANKER_DOUBLE;
				}).collect(Collectors.toList());
				
				if(itemColl.size() < 1) {
					return new BigDecimal(0);
				}
				
				return getItem(itemColl.get(0));
			}
		});
		
		isAllowBet(LimitItem.PLAYER_PAIRS, limitMin, limitMax, new LimitCalcPattern() {
			@Override
			public BigDecimal calc() {
				List<TradeItem> itemColl = tradeItemList.stream().filter( item -> {
					return item.getTradeItemEnum() == TradeItemEnum.PLAYER_DOUBLE;
				}).collect(Collectors.toList());
				
				if(itemColl.size() < 1) {
					return new BigDecimal(0);
				}
				
				return getItem(itemColl.get(0));
			}
		});
		
		
		isAllowBet(LimitItem.BANK_CASE, limitMin, limitMax, new LimitCalcPattern() {
			@Override
			public BigDecimal calc() {
				List<TradeItem> itemColl = tradeItemList.stream().filter( item -> {
					return item.getTradeItemEnum() == TradeItemEnum.BANKER_CASE;
				}).collect(Collectors.toList());
				
				if(itemColl.size() < 1) {
					return new BigDecimal(0);
				}
				
				return getItem(itemColl.get(0));
			}
		});
		
		isAllowBet(LimitItem.PLAYER_CASE, limitMin, limitMax, new LimitCalcPattern() {
			@Override
			public BigDecimal calc() {
				List<TradeItem> itemColl = tradeItemList.stream().filter( item -> {
					return item.getTradeItemEnum() == TradeItemEnum.PLAYER_CASE;
				}).collect(Collectors.toList());
				
				if(itemColl.size() < 1) {
					return new BigDecimal(0);
				}
				
				return getItem(itemColl.get(0));
			}
		});
		
		
		isAllowBet(LimitItem.TIE, limitMin, limitMax, new LimitCalcPattern() {
			@Override
			public BigDecimal calc() {
				Optional<TradeItem> itemOpt = tradeItemList.stream().filter( item -> {
					return item.getTradeItemEnum() == TradeItemEnum.TIE;
				}).findFirst();
				
				return getItem(itemOpt.orElse(null));
			}
		});
		
		isAllowBet(LimitItem.SUPER_SIX, limitMin, limitMax, new LimitCalcPattern() {
			@Override
			public BigDecimal calc() {
				Optional<TradeItem> itemOpt = tradeItemList.stream().filter( item -> {
					return item.getTradeItemEnum() == TradeItemEnum.SUPER_SIX;
				}).findFirst();
				
				return getItem(itemOpt.orElse(null));
			}
		});
		
		isAllowBet(LimitItem.B27, limitMin, limitMax, new LimitCalcPattern() {
			@Override
			public BigDecimal calc() {
				Optional<TradeItem> itemOpt = tradeItemList.stream().filter( item -> {
					return item.getTradeItemEnum() == TradeItemEnum.B27;
				}).findFirst();
				
				return getItem(itemOpt.orElse(null));
			}
		});
		
		LOCK_CURRENT_LOGIN.readLock().lock();
		try {
			result.forEach( (key,value) -> {
				if(totalResult.containsKey(key)) {
					totalResult.put(key, totalResult.get(key).add(value));
				} else {
					totalResult.put(key, value);
				}
			});
		} finally {
			LOCK_CURRENT_LOGIN.readLock().unlock();
		}
		
		LOCK_CURRENT_LOGIN2.readLock().lock();
		try {
			absoluteResult.forEach( (key,value) -> {
				if(absoluteTotalResult.containsKey(key)) {
					absoluteTotalResult.put(key, absoluteTotalResult.get(key).add(value.abs()));
				} else {
					absoluteTotalResult.put(key, value.abs());
				}
			});
		} finally {
			LOCK_CURRENT_LOGIN2.readLock().unlock();
		}
		
//		totalResult.forEach( (key, value) -> {
//			log.debug( "当前下注总计:" + key + ",投注额:" + value );
//		});
		
	}
	
	private void isAllowBet(LimitItem limitItem, BigDecimal limitMin, BigDecimal limitMax, LimitCalcPattern pattern) {
		BigDecimal betWinMoney = pattern.calc();
		
		BigDecimal finalMoney = totalResult.containsKey(limitItem) ? totalResult.get(limitItem) : new BigDecimal(0);
		finalMoney = finalMoney.add(betWinMoney).abs();
		
		if(finalMoney.compareTo(new BigDecimal(0)) > 0 && limitMin.compareTo(finalMoney) > 0) {
			throw SocketException.createError("bet.lower.min");
		}
		
		if(limitMax.compareTo(finalMoney) < 0) {
			throw SocketException.createError("bet.higher.max");
		}
		
		LOCK_CURRENT_LOGIN.writeLock().lock();
		try {
			result.put(limitItem, betWinMoney);
		} finally {
			LOCK_CURRENT_LOGIN.writeLock().unlock();
		}
	}
	
	private BigDecimal getItem(TradeItem item) {
		if(item == null) {
			return new BigDecimal(0);
		}
		return tradeItemMap.containsKey(item) ? tradeItemMap.get(item) : new BigDecimal(0);
	}
	
	/**
	 * 计算赢额
	 */
	private BigDecimal count(TradeItem item,BigDecimal betNumber) {
		BigDecimal rate = item.getRate().subtract(new BigDecimal(1));
		if(rate.compareTo(new BigDecimal(1)) < 0) {
			rate = new BigDecimal(1);
		}
		return betNumber.multiply(rate);
	}
	
}
