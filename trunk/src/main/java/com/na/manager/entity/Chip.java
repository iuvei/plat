package com.na.manager.entity;

import java.io.Serializable;

/**
 * 用户限红表
 * @author andy
 * @date 2017年6月22日 下午12:21:21
 * 
 */
public class Chip implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Double max;
	private Double min;

	/**
	 * 筹码列表，使用逗号分隔 5,10,50,100,500,500
	 */
	private String jtton;

	/**
	 * 币种类型
	 */
	private String type;

	private Integer gameId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public String getJtton() {
		return jtton;
	}

	public void setJtton(String jtton) {
		this.jtton = jtton;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getGameId() {
		return gameId;
	}

	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}
}
