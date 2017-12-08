package com.gameportal.manage.pay.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.gameportal.manage.system.model.BaseEntity;
/**
 * 资金帐变表。
 * @author Administrator
 *
 */
public class PayOrderLog extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * ID
	 */
	private long coid;
	/**
	 * 用户ID
	 */
	private long uiid;

	/**
	 * 订单ID
	 */
	private String orderid;
	/**
	 * 类型
	 */
	private Integer type;
	
	/**
	 * 金额
	 */
	private BigDecimal amount;
	/**
	 * 钱包日志
	 */
	private String walletlog;
	/**
	 * 第三方日志
	 */
	private String gamelog;
	/**
	 * 审核描述
	 */
	private String remark;
	/**
	 * 创建时间（和原订单更新时间一致）
	 */
	private String createtime;

	public long getCoid() {
		return coid;
	}

	public void setCoid(long coid) {
		this.coid = coid;
	}

	public long getUiid() {
		return uiid;
	}

	public void setUiid(long uiid) {
		this.uiid = uiid;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getWalletlog() {
		return walletlog;
	}

	public void setWalletlog(String walletlog) {
		this.walletlog = walletlog;
	}

	public String getGamelog() {
		return gamelog;
	}

	public void setGamelog(String gamelog) {
		this.gamelog = gamelog;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public Serializable getID() {
		return this.coid;
	}
}
