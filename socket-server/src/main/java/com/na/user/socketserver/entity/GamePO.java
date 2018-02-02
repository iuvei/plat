package com.na.user.socketserver.entity;

import java.io.Serializable;
import java.util.Map;

import com.na.user.socketserver.annotation.I18NField;
import com.na.user.socketserver.common.enums.GameEnum;

/**
 * 游戏实体类，记录游戏基本信息
 */
public class GamePO implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 游戏ID
	 */
	private Integer id;
	/**
	 * 名称
	 */
	@I18NField
	private String name;

	/**
	 * 0 表示正常
	 */
	private Integer status;
	/**
	 * 0 真人类游戏
	 */
	private Integer type;
	
	/**
	 * 游戏代码
	 */
	private String gameCode;
	
	/**
	 * 游戏配置
	 */
	private Map<String,String> gameConfig;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Map<String, String> getGameConfig() {
		return gameConfig;
	}

	public void setGameConfig(Map<String, String> gameConfig) {
		this.gameConfig = gameConfig;
	}

	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}
	
	public GameEnum getGameEnum() {
		return GameEnum.get(gameCode);
	}
	
	public void setGameEnum(GameEnum gameEnum) {
		gameCode = gameEnum.get();
	}
}
