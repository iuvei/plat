package com.na.roulette.socketserver.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.baccarat.socketserver.common.enums.RoundStatusEnum;
import com.na.baccarat.socketserver.entity.Play;
import com.na.baccarat.socketserver.entity.TradeItem;
import com.na.roulette.socketserver.common.enums.RouletteGameTableInstantStateEnum;
import com.na.roulette.socketserver.common.limitrule.RouletteCheckLimitRule;
import com.na.user.socketserver.entity.GameTablePO;

/**
 * 轮盘桌子
 * @author Administrator
 *
 */
public class RouletteGameTable implements Serializable  {

	private static final long serialVersionUID = 638840964074924957L;
	
	public RouletteGameTable() {
	}
	private GameTablePO gameTablePO;

	/**
	 * 荷官
	 */
	private RouletteUser dealer;
	
	/**
	 * 荷官主管
	 */
	private RouletteUser checkers;

	/**
	 * 桌子即时状态。
	 */
	@JSONField(name = "stu")
	private Integer instantState = RouletteGameTableInstantStateEnum.INACTIVE.get();;
	
	
	//倒计时当前时间
	private Date coundDownDate;
	/**
	 * 局信息
	 */
	private RouletteRound round;
	
	/**
	 * 靴拓展信息
	 */
	private RouletteRoundExt roundExt;
	
	/**
	 * 所有局信息
	 */
	private List<RouletteRound> rounds = new ArrayList<>(10);
	
	/**
	 * 玩法列表
	 */
	private List<Play> playList;
	
	/**
	 * 当前桌用户
	 * key 为用户ID
	 */
	private Map<Long, RouletteUser> players = new ConcurrentHashMap<Long, RouletteUser>();

	private RouletteGameTableInstantStateEnum oldStatus;

	public RouletteGameTableInstantStateEnum getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(RouletteGameTableInstantStateEnum oldStatus) {
		this.oldStatus = oldStatus;
	}
	
	/**
	 * 记录虚桌每局每个交易项的下注总额，结算时，清空。
	 * key userId,  Map<key 交易项ID， val 交易金额。>
	 */
	private Map<Long,Map<Integer,BigDecimal>> tradeItemBetMoneyMap = new ConcurrentHashMap<>();
	
	private RouletteCheckLimitRule limitRule;
	
	public RouletteCheckLimitRule getLimitRule() {
		return limitRule;
	}

	public void setLimitRule(RouletteCheckLimitRule limitRule) {
		this.limitRule = limitRule;
	}

	public RouletteGameTable(GameTablePO gameTablePO) {
		this.gameTablePO = gameTablePO;
	}

	@JSONField(deserialize=false,serialize = false)
	public RouletteGameTableInstantStateEnum getInstantStateEnum() {
		if(instantState==null)return null;
		return RouletteGameTableInstantStateEnum.get(instantState);
	}

	public Date getCoundDownDate() {
		return coundDownDate;
	}

	public void setCoundDownDate(Date coundDownDate) {
		this.coundDownDate = coundDownDate;
	}

	public void setInstantStateEnum(RouletteGameTableInstantStateEnum instantStateEnum) {
		this.instantState = instantStateEnum.get();
	}

	public Integer getInstantState() {
		return instantState;
	}

	public void setInstantState(Integer instantState) {
		this.instantState = instantState;
	}

	public RouletteRound getRound() {
		return round;
	}

	public void setRound(RouletteRound round) {
		this.round = round;
	}

	public RouletteUser getDealer() {
		return dealer;
	}

	public void setDealer(RouletteUser dealer) {
		this.dealer = dealer;
	}

	public List<RouletteRound> getRounds() {
		return rounds;
	}

	public void setRounds(List<RouletteRound> rounds) {
		this.rounds = rounds;
	}

	public RouletteUser getCheckers() {
		return checkers;
	}

	public void setCheckers(RouletteUser checkers) {
		this.checkers = checkers;
	}

	public RouletteRoundExt getRoundExt() {
		return roundExt;
	}

	public void setRoundExt(RouletteRoundExt roundExt) {
		this.roundExt = roundExt;
	}

	public GameTablePO getGameTablePO() {
		return gameTablePO;
	}

	public void setGameTablePO(GameTablePO gameTablePO) {
		this.gameTablePO = gameTablePO;
	}

	public Map<Long, RouletteUser> getPlayers() {
		return players;
	}

	public void setPlayers(Map<Long, RouletteUser> players) {
		this.players = players;
	}
	
	public void addPlayer(RouletteUser player) {
		this.players.put(player.getUserPO().getId(), player);
	}

	public List<Play> getPlayList() {
		return playList;
	}

	public void setPlayList(List<Play> playList) {
		this.playList = playList;
	}
	
	public void setTradeItemBetMoneyMap(Map<Long,Map<Integer,BigDecimal>> tradeItemBetMoneyMap) {
		this.tradeItemBetMoneyMap = tradeItemBetMoneyMap;
	}
	
	public Map<Integer,BigDecimal> getTradeItemBetMoneyMap(Long userId) {
		if(tradeItemBetMoneyMap.containsKey(userId)) {
			return tradeItemBetMoneyMap.get(userId);
		} else {
			Map<Integer,BigDecimal> newMap = new ConcurrentHashMap<>();
			tradeItemBetMoneyMap.put(userId, newMap);
			return newMap;
		}
	}

	public Map<Long,Map<Integer,BigDecimal>> getTradeItemBetMoneyMap() {
		return tradeItemBetMoneyMap;
	}
	
	public void setTradeItemBetMoney(Long userId, Integer tradeId, BigDecimal amount) {
		if(tradeItemBetMoneyMap.containsKey(userId)) {
			Map<Integer, BigDecimal> trade = tradeItemBetMoneyMap.get(userId);
			if(trade.containsKey(tradeId)) {
				trade.put(tradeId, amount.add(trade.get(tradeId)));
			} else {
				trade.put(tradeId, amount);
			}
		} else {
			Map<Integer,BigDecimal> newMap = new ConcurrentHashMap<>();
			newMap.put(tradeId, amount);
			tradeItemBetMoneyMap.put(userId, newMap);
		}
	}
	
	/**
	 * 返回指定交易项ID的交易项。
	 * @param tradeItemId
	 * @return
	 */
	@JSONField(deserialize=false,serialize = false)
	public TradeItem getTradeItemById(Integer tradeItemId){
		for(Play play : this.playList){
			Optional<TradeItem> opt = play.getTradeList().stream().filter(item->item.getId().equals(tradeItemId)).findFirst();
			if(opt.isPresent()){
				return opt.get();
			}
		}
		return null;
	}
	
	/**
	 * 返回指定类型和数字的交易项。
	 * @param tradeItemId
	 * @return
	 */
	@JSONField(deserialize=false,serialize = false)
	public TradeItem getTradeItemById(String key, String addition){
		for(Play play : this.playList){
			Optional<TradeItem> opt = play.getTradeList()
					.stream().filter( item -> {
						if(item.getKey().equals(key) && item.getAddition().equals(addition)) {
							return true;
						}
						return false;
					}).findFirst();
			if(opt.isPresent()){
				return opt.get();
			}
		}
		return null;
	}
	
	public String getShowStatus() {
		RoundStatusEnum roundStatusEnum = this.round.getRoundPO().getStatusEnum();
		if(roundStatusEnum == RoundStatusEnum.NEWGAME
				|| roundStatusEnum == RoundStatusEnum.BETTING) {
			return "下注中";
		} else if(roundStatusEnum == RoundStatusEnum.AWAITING_RESULT
				|| roundStatusEnum == RoundStatusEnum.FINISH) {
			return "结算中";
		} else if (roundStatusEnum == RoundStatusEnum.PAUSE) {
			return "暂停中";
		}
		return "空闲中";
	}
	
}
