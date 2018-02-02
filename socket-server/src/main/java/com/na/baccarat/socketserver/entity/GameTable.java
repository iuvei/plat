package com.na.baccarat.socketserver.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.baccarat.socketserver.command.requestpara.Card;
import com.na.baccarat.socketserver.common.enums.GameTableInstantStateEnum;
import com.na.baccarat.socketserver.common.enums.GameTableTypeEnum;
import com.na.baccarat.socketserver.common.enums.RoundStatusEnum;
import com.na.baccarat.socketserver.common.enums.TradeItemEnum;
import com.na.baccarat.socketserver.common.limitrule.CheckLimitRule;
import com.na.baccarat.socketserver.config.BaccaratConfig;
import com.na.user.socketserver.entity.GameTablePO;

/**
 * 游戏桌子
 */
public class GameTable implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Logger log = LoggerFactory.getLogger(GameTable.class);
	
	private GameTablePO gameTablePO;
	
	/**
	 * 桌子即时状态。
	 */
	@JSONField(name = "stu")
	private Integer instantState = GameTableInstantStateEnum.INACTIVE.get();
	
	/**
	 * 靴信息
	 */
	private Round round;

	/**
	 * 游戏桌路子
	 */
	private List<Round> rounds;
	
	/**
	 * 荷官
	 */
	private User dealer;
	
	/**
	 * 荷官主管
	 */
	private User checkers;
	
	/**
	 * 靴拓展信息
	 */
	private RoundExt roundExt;
	
	/**
	 * 靴启动的开始时间(需要数据库去查询)
	 */
	private Date bootStartTime;
	
	/**
	 * 玩法列表
	 */
	private List<Play> playList;
	
	/**
	 * 卡片信息
	 * 0-5  表示 闲前2张 庄前2张  闲第3张  庄第3张
	 */
	private Card[] cards;
	
	/**
	 * 更换荷官次数  还靴清空
	 */
	private int changeDealerNum = 0;
	
	/**
	 * 当前桌子庄和闲
	 */
	private Map<TradeItemEnum,Set<User>> bankPlayerMap = new ConcurrentHashMap<>();

	//投注倒计时
	private AtomicLong coundDown = new AtomicLong(-11);
	
	//倒计时当前时间
	private Date coundDownDate;
	
	//倒计时
	private Integer countDownSeconds;

	private GameTableInstantStateEnum oldStatus;
	
	//分位咪牌倒计时60秒
	private AtomicLong miCardDownSeat = new AtomicLong(-1l);
	
	private CheckLimitRule limitRule;

	public void setCards(Card[] cards) {
		this.cards = cards;
	}

	public Card[] getCards() {
		return cards;
	}

	public GameTableInstantStateEnum getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(GameTableInstantStateEnum oldStatus) {
		this.oldStatus = oldStatus;
	}

	/**
	 * 设置卡片
	 * @param type
	 * @param num
	 * @param index
	 */
	public void setCard(String type, int num, int index) {
		if (this.cards == null) {
			cards = new Card[BaccaratConfig.maxCardNum];
		}
		this.cards[index] = new Card(type, num);
		exportCard(index);
	}

	public void removeCard(int index){
		if(this.cards!=null && index<this.cards.length){
			this.cards[index] = null;
			clean(index);
		}
	}
	
	/**
	 * 索引从0开始
	 * 
	 * @param index
	 */
	public void exportCard(int index) {
		Card c = this.cards[index];
		if (c != null) {
			switch (index) {
				case 0:
					roundExt.getRoundExtPO().setPlayerCard1Mode(c.getCardType());
					roundExt.getRoundExtPO().setPlayerCard1Number(c.getCardNum());
					break;
				case 1:
					roundExt.getRoundExtPO().setPlayerCard2Mode(c.getCardType());
					roundExt.getRoundExtPO().setPlayerCard2Number(c.getCardNum());
					break;
				case 2:
					roundExt.getRoundExtPO().setBankCard1Mode(c.getCardType());
					roundExt.getRoundExtPO().setBankCard1Number(c.getCardNum());
					break;
				case 3:
					roundExt.getRoundExtPO().setBankCard2Mode(c.getCardType());
					roundExt.getRoundExtPO().setBankCard2Number(c.getCardNum());
					break;
				case 4:
					roundExt.getRoundExtPO().setPlayerCard3Mode(c.getCardType());
					roundExt.getRoundExtPO().setPlayerCard3Number(c.getCardNum());
					break;
				case 5:
					roundExt.getRoundExtPO().setBankCard3Mode(c.getCardType());
					roundExt.getRoundExtPO().setBankCard3Number(c.getCardNum());
					break;
			}
		}
	}
	/**
	 * 清除指定索引的牌。
	 *
	 * @param index
	 */
	private void clean(int index) {
		Card c = this.cards[index];
			switch (index) {
				case 0:
					roundExt.getRoundExtPO().setPlayerCard1Mode(null);
					roundExt.getRoundExtPO().setPlayerCard1Number(null);
					break;
				case 1:
					roundExt.getRoundExtPO().setPlayerCard2Mode(null);
					roundExt.getRoundExtPO().setPlayerCard2Number(null);
					break;
				case 2:
					roundExt.getRoundExtPO().setBankCard1Mode(null);
					roundExt.getRoundExtPO().setBankCard1Number(null);
					break;
				case 3:
					roundExt.getRoundExtPO().setBankCard2Mode(null);
					roundExt.getRoundExtPO().setBankCard2Number(null);
					break;
				case 4:
					roundExt.getRoundExtPO().setPlayerCard3Mode(null);
					roundExt.getRoundExtPO().setPlayerCard3Number(null);
					break;
				case 5:
					roundExt.getRoundExtPO().setBankCard3Mode(null);
					roundExt.getRoundExtPO().setBankCard3Number(null);
					break;
			}
	}

	public User getDealer() {
		return dealer;
	}

	public void setDealer(User dealer) {
		this.dealer = dealer;
	}



	public Round getRound() {
		return round;
	}

	public void setRound(Round round) {
		this.round = round;
	}

	public RoundExt getRoundExt() {
		return roundExt;
	}

	public void setRoundExt(RoundExt roundExt) {
		this.roundExt = roundExt;
	}

	public List<Round> getRounds() {
		return rounds;
	}

	public void setRounds(List<Round> rounds) {
		this.rounds = rounds;
	}

	public User getCheckers() {
		return checkers;
	}

	public void setCheckers(User checkers) {
		this.checkers = checkers;
	}

	public List<Play> getPlayList() {
		return playList;
	}

	public void setPlayList(List<Play> playList) {
		this.playList = playList;
	}
	
	public Date getBootStartTime() {
		return bootStartTime;
	}

	public void setBootStartTime(Date bootStartTime) {
		this.bootStartTime = bootStartTime;
	}

	public int getChangeDealerNum() {
		return changeDealerNum;
	}

	public void setChangeDealerNum(int changeDealerNum) {
		this.changeDealerNum = changeDealerNum;
	}


	public AtomicLong getCoundDown() {
		return coundDown;
	}

	public void setCoundDown(AtomicLong coundDown) {
		this.coundDown = coundDown;
	}


	public Map<TradeItemEnum, Set<User>> getBankPlayerMap() {
		return bankPlayerMap;
	}
	
	public Set<User> getBankPlayerMap(TradeItemEnum tradeItemEnum) {
		if(bankPlayerMap == null ||tradeItemEnum == null){
			return null;
		}
		return bankPlayerMap.get(tradeItemEnum);
	}

	public void setBankPlayerMap(Map<TradeItemEnum, Set<User>> bankPlayerMap) {
		this.bankPlayerMap = bankPlayerMap;
	}

	public AtomicLong getMiCardDownSeat() {
		return miCardDownSeat;
	}



	public void setMiCardDownSeat(AtomicLong miCardDownSeat) {
		this.miCardDownSeat = miCardDownSeat;
	}
	


	public Date getCoundDownDate() {
		return coundDownDate;
	}

	public void setCoundDownDate(Date coundDownDate) {
		this.coundDownDate = coundDownDate;
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

	@JSONField(deserialize=false,serialize = false)
	public GameTableInstantStateEnum getInstantStateEnum() {
		if(instantState==null)return null;
		return GameTableInstantStateEnum.get(instantState);
	}

	public void setInstantStateEnum(GameTableInstantStateEnum instantStateEnum) {
		log.debug("【桌子】: " + this.getGameTablePO().getId() + ",瞬时状态: " + this.instantState +",修改为: " + instantStateEnum);
		this.instantState = instantStateEnum.get();
	}

	@JSONField(deserialize=false,serialize = false)
	public Integer getInstantState() {
		return instantState;
	}

	public void setInstantState(Integer instantState) {
		this.instantState = instantState;
	}

	public GameTablePO getGameTablePO() {
		return gameTablePO;
	}

	public void setGameTablePO(GameTablePO gameTablePO) {
		this.gameTablePO = gameTablePO;
	}

	public CheckLimitRule getLimitRule() {
		return limitRule;
	}

	public void setLimitRule(CheckLimitRule limitRule) {
		this.limitRule = limitRule;
	}
	
	public String getShowStatus() {
		GameTableTypeEnum gameTableTypeEnum = this.getGameTablePO().getTypeEnum();
		RoundStatusEnum roundStatusEnum = this.round.getRoundPO().getStatusEnum();
		if(gameTableTypeEnum == GameTableTypeEnum.MI_BEING
				|| gameTableTypeEnum == GameTableTypeEnum.MI_NORMAL ) {
			if(roundStatusEnum == RoundStatusEnum.IDLE
					|| roundStatusEnum == RoundStatusEnum.SHUFFLE) {
				return "洗牌中";
			} else if(roundStatusEnum == RoundStatusEnum.NEWGAME
					|| roundStatusEnum == RoundStatusEnum.BETTING) {
				return "下注中";
			} else if(roundStatusEnum == RoundStatusEnum.AWAITING_RESULT) {
				return "咪牌中";
			} else if(roundStatusEnum == RoundStatusEnum.FINISH) {
				return "结算中";
			} else if (roundStatusEnum == RoundStatusEnum.PAUSE) {
				return "暂停中";
			}
		} else {
			if(roundStatusEnum == RoundStatusEnum.IDLE
					|| roundStatusEnum == RoundStatusEnum.SHUFFLE) {
				return "洗牌中";
			} else if(roundStatusEnum == RoundStatusEnum.NEWGAME
					|| roundStatusEnum == RoundStatusEnum.BETTING) {
				return "下注中";
			} else if(roundStatusEnum == RoundStatusEnum.AWAITING_RESULT
					|| roundStatusEnum == RoundStatusEnum.FINISH) {
				return "结算中";
			} else if (roundStatusEnum == RoundStatusEnum.PAUSE) {
				return "暂停中";
			}
		}
		return "空闲中";
	}

	public Integer getCountDownSeconds() {
		return countDownSeconds;
	}

	public void setCountDownSeconds(Integer countDownSeconds) {
		this.countDownSeconds = countDownSeconds;
	}

}
