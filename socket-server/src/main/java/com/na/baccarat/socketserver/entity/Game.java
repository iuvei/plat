package com.na.baccarat.socketserver.entity;

import java.io.Serializable;

import com.na.user.socketserver.entity.GamePO;

/**
 * 游戏实体类，记录游戏基本信息
 */
public class Game implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private GamePO gamePO;
	
	public Game() {
	}

	public Game(GamePO gamePO) {
		this.gamePO = gamePO;
	}

	public GamePO getGamePO() {
		return gamePO;
	}

	public void setGamePO(GamePO gamePO) {
		this.gamePO = gamePO;
	}
	
	
	
}
