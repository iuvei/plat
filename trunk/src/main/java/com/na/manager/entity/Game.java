package com.na.manager.entity;

import java.io.Serializable;

import com.na.manager.common.annotation.I18NField;

/**
 * 游戏
 *
 */
public class Game implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	@I18NField
	private String name;

	/**
	 * 游戏类型 0真人类 1彩票类 2体育类 3电子类
	 */
	private String type;
	/**
	 * 0正常,1关闭
	 */
	private Integer status;
	private String gameCode;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}
}
