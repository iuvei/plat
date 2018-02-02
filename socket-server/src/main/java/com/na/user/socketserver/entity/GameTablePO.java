package com.na.user.socketserver.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.baccarat.socketserver.common.enums.GameTableStatusEnum;
import com.na.baccarat.socketserver.common.enums.GameTableTypeEnum;
import com.na.user.socketserver.annotation.I18NField;

/**
 * 游戏桌子
 */
public class GameTablePO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@JSONField(name = "tid")
	private Integer id;

	private Integer gameId;
	
	@I18NField()
	@JSONField(name = "tcn")
	private String name;
	/**
	 * 游戏状态 0正常1关闭
	 */
	private Integer status;
	/**是否多台 0 否 1 是*/
	private Integer isMany;
	/**
	 * 下注时间
	 */
	private Integer countDownSeconds;
	/**
	 * @see com.na.baccarat.socketserver.common.enums.GameTableTypeEnum
	 */
	@JSONField(name = "type")
	private Integer type;
	/**
	 * 咪牌时间
	 */
	private Integer miCountDownSeconds;
	/**
	 * 最小台红
	 */
	@JSONField(name = "min")
	private BigDecimal min;
	/**
	 * 最大台红
	 */
	@JSONField(name = "max")
	private BigDecimal max;
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
	public Integer getIsMany() {
		return isMany;
	}
	public void setIsMany(Integer isMany) {
		this.isMany = isMany;
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
	public BigDecimal getMin() {
		return min;
	}
	public void setMin(BigDecimal min) {
		this.min = min;
	}
	public BigDecimal getMax() {
		return max;
	}
	public void setMax(BigDecimal max) {
		this.max = max;
	}
	
	/**
	 * 判断是否为咪牌桌
	 * @return
	 */
	public boolean isMiTable() {
		if(this.type == null) {
			return false;
		}
		
		if(GameTableTypeEnum.get(this.type) == GameTableTypeEnum.MI_BEING ||
				GameTableTypeEnum.get(this.type) == GameTableTypeEnum.MI_NORMAL) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 判断是否为竞咪桌
	 * @return
	 */
	public boolean isBeingTable() {
		if(this.type == null) {
			return false;
		}
		
		if(GameTableTypeEnum.get(this.type) == GameTableTypeEnum.MI_BEING) {
			return true;
		} else {
			return false;
		}
	}
	
	public GameTableTypeEnum getTypeEnum(){
		return GameTableTypeEnum.get(type);
	}

	public GameTableStatusEnum getStatusEnum(){
		return GameTableStatusEnum.get(status);
	}
	public Integer getMiCountDownSeconds() {
		return miCountDownSeconds;
	}
	public void setMiCountDownSeconds(Integer miCountDownSeconds) {
		this.miCountDownSeconds = miCountDownSeconds;
	}

	

}
