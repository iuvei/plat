package com.na.roulette.socketserver.entity;


import java.io.Serializable;

import com.na.user.socketserver.entity.RoundExtPO;


/**
 * 游戏专有扩展表
 * 
 * @author alan
 * @date 2017年4月28日 上午10:27:35
 */
public class RouletteRoundExt implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private RoundExtPO roundExtPO;
	
	public RouletteRoundExt() {
	}

	public RouletteRoundExt(RoundExtPO roundExtPO) {
		this.roundExtPO = roundExtPO;
	}

	public RoundExtPO getRoundExtPO() {
		return roundExtPO;
	}

	public void setRoundExtPO(RoundExtPO roundExtPO) {
		this.roundExtPO = roundExtPO;
	}

	
	
	
}