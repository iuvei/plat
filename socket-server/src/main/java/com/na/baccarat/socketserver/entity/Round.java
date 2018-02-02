package com.na.baccarat.socketserver.entity;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.na.baccarat.socketserver.common.enums.RoundStatusEnum;
import com.na.user.socketserver.entity.RoundPO;

/**
 * @author java
 * @time 2015-07-21 18:05:58
 */
public class Round implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private RoundPO roundPO;

	private RoundStatusEnum oldStatus;
	
	/**
	 * 记录每局的下注信息，结算时，清空。
	 */
	private List<UserBet> userBetedInfos = new CopyOnWriteArrayList<>();

	public RoundStatusEnum getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(RoundStatusEnum oldStatus) {
		this.oldStatus = oldStatus;
	}

	public Round() {
	}

	public Round(RoundPO roundPO) {
		this.roundPO = roundPO;
	}

	public RoundPO getRoundPO() {
		return roundPO;
	}

	public void setRoundPO(RoundPO roundPO) {
		this.roundPO = roundPO;
	}

	public List<UserBet> getUserBetedInfos() {
		return userBetedInfos;
	}

	public void setUserBetedInfos(List<UserBet> userBetedInfos) {
		this.userBetedInfos = userBetedInfos;
	}
	
	public void addBeted(UserBet userBet) {
		if(this.userBetedInfos == null) {
			return ;
		}
		this.userBetedInfos.add(userBet);
	}
	
}