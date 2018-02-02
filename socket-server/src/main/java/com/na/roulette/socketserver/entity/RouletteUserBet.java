package com.na.roulette.socketserver.entity;

import java.math.BigDecimal;

/**
 * 用户投注
 * @author Administrator
 *
 */
public class RouletteUserBet {

	/**
	 * 用户ID
	 */
	private Long uid;
	/**
	 * 局ID
	 */
	private Long roundId;
	/**
	 * 交易项ID
	 */
	private Integer tradeId;
	/**
	 * 桌子Id
	 */
	private Integer tableId;
	/**
	 * 交易额度
	 */
	private BigDecimal amount;
	
	
	public Integer getTableId() {
		return tableId;
	}
	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	public Long getRoundId() {
		return roundId;
	}
	public void setRoundId(Long roundId) {
		this.roundId = roundId;
	}
	public Integer getTradeId() {
		return tradeId;
	}
	public void setTradeId(Integer tradeId) {
		this.tradeId = tradeId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	
	
}
