package com.na.baccarat.socketserver.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.na.baccarat.socketserver.common.enums.BetOrderSourceEnum;

/**
 * 用户投注
 * @author Administrator
 *
 */
public class UserBet implements Serializable {

	/**
	 * 用户ID
	 */
	private Long userId;
	/**
	 * 桌子ID
	 */
	private Integer tableId;
	/**
	 * 虚拟桌ID
	 */
	private Integer virtualTableId;
	/**
	 * 交易项ID
	 */
	private Integer tradeId;
	/**
	 * 交易额度
	 */
	private BigDecimal amount;
	/**
	 * 投注来源
	 */
	private BetOrderSourceEnum source;
	
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	public BetOrderSourceEnum getSource() {
		return source;
	}
	public void setSource(BetOrderSourceEnum source) {
		this.source = source;
	}
	public Integer getVirtualTableId() {
		return virtualTableId;
	}
	public void setVirtualTableId(Integer virtualTableId) {
		this.virtualTableId = virtualTableId;
	}
	public Integer getTableId() {
		return tableId;
	}
	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}
	
}
