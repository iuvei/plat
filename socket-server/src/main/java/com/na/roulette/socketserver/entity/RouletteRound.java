package com.na.roulette.socketserver.entity;

import com.na.baccarat.socketserver.common.enums.RoundStatusEnum;
import com.na.user.socketserver.entity.RoundPO;

import java.io.Serializable;

/**
 * @author java
 * @time 2015-07-21 18:05:58
 */
public class RouletteRound implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private RoundPO roundPO;

	private RoundStatusEnum oldStatus;

	public RoundStatusEnum getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(RoundStatusEnum oldStatus) {
		this.oldStatus = oldStatus;
	}
	public RouletteRound() {
	}

	public RouletteRound(RoundPO roundPO) {
		this.roundPO = roundPO;
	}

	public RoundPO getRoundPO() {
		return roundPO;
	}

	public void setRoundPO(RoundPO roundPO) {
		this.roundPO = roundPO;
	}
	
}
