package com.na.manager.entity;

import java.io.Serializable;

import com.na.manager.common.annotation.I18NField;

/**
 * 游戏桌子
 */
public class GameTable implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer gameId;
	@I18NField
	private String name;
	/**
	 * 游戏状态 0正常1关闭
	 */
	private Integer status;
	/**
	 * 下注时长 (0无限制)
	 */
	private Integer countDownSeconds;
	/**
	 * 0普通桌,1包桌
	 */
	private Integer type;

	/**
	 * 是否多台 0 否 1 是
	 */
	private Integer isMany = 1;

	/**
	 * 是否竞咪 0 否 1 是
	 */
	private Integer miCountdownSeconds = 0;

	/**
	 * 最小限红
	 */
	private Integer min = 0;

	/**
	 * 最大限红
	 */
	private Integer max = 10000000;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGameId() {
		return gameId;
	}

	public void setGameId(Integer gameId) {
		this.gameId = gameId;
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

	public Integer getCountDownSeconds() {
		return countDownSeconds;
	}

	public void setCountDownSeconds(Integer countDownSeconds) {
		this.countDownSeconds = countDownSeconds;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getIsMany() {
		return isMany;
	}

	public void setIsMany(Integer isMany) {
		this.isMany = isMany;
	}

	public Integer getMiCountdownSeconds() {
		return miCountdownSeconds;
	}

	public void setMiCountdownSeconds(Integer miCountdownSeconds) {
		this.miCountdownSeconds = miCountdownSeconds;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}
}
