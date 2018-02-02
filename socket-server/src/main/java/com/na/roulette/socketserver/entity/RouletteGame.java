package com.na.roulette.socketserver.entity;

import java.io.Serializable;

import com.na.user.socketserver.entity.GamePO;

/**
 * 游戏实体类，记录游戏基本信息
 */
public class RouletteGame implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private GamePO gamePO;
	
	public RouletteGame() {
	}

	public RouletteGame(GamePO gamePO) {
		this.gamePO = gamePO;
	}

	public GamePO getGamePO() {
		return gamePO;
	}

	public void setGamePO(GamePO gamePO) {
		this.gamePO = gamePO;
	}
	
	
	
}
