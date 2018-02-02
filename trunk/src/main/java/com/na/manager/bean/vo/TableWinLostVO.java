package com.na.manager.bean.vo;

import java.math.BigDecimal;

import com.na.manager.common.annotation.I18NField;

/**
 * @author andy
 * @date 2017年6月23日 下午6:35:49
 * 
 */
public class TableWinLostVO {
	private String tableId;
	@I18NField
	private String tableName;
	private String bootsId;
	private BigDecimal betAmount;
	private BigDecimal settleAmount;
	private BigDecimal winLostAmount;
	public String getTableId() {
		return tableId;
	}
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getBootId() {
		return bootsId;
	}
	public void setBootId(String bootId) {
		this.bootsId = bootId;
	}
	public BigDecimal getBetAmount() {
		return betAmount;
	}
	public void setBetAmount(BigDecimal betAmount) {
		this.betAmount = betAmount;
	}
	public BigDecimal getSettleAmount() {
		return settleAmount;
	}
	public void setSettleAmount(BigDecimal settleAmount) {
		this.settleAmount = settleAmount;
	}
	public BigDecimal getWinLostAmount() {
		return winLostAmount;
	}
	public void setWinLostAmount(BigDecimal winLostAmount) {
		this.winLostAmount = winLostAmount;
	}
}
