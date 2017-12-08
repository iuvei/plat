package com.gameportal.web.game.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.web.user.model.BaseEntity;

public class GameTransfer extends BaseEntity {
	private static final long serialVersionUID = 1L;
	// alias
	public static final String TABLE_ALIAS = "GameTransfer";
	public static final String ALIAS_GTID = "游戏转帐记录ID";
	public static final String ALIAS_UUID = "用户ID";
	public static final String ALIAS_GPID = "转出平台ID";
	public static final String ALIAS_GAMENAME = "转出平台名字";
	public static final String ALIAS_TOGPID = "转入平台ID";
	public static final String ALIAS_TOGAMENAME = "togamename";
	public static final String ALIAS_AMOUNT = "金额";
	public static final String ALIAS_REMARK = "类型 0 转出 1转入";
	public static final String ALIAS_STATUS = "状态 0 转账中 1成功  1 失败";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_DATE = "更新时间";
	public static final String ALIAS_BALANCE = "余额";

	// columns START
	private java.lang.Long gtid;
	private java.lang.Long uuid;
	private java.lang.Long gpid;
	private java.lang.String gamename;
	private java.lang.Long togpid;
	private java.lang.String togamename;
	private BigDecimal amount;
	private java.lang.String remark;
	private java.lang.Integer status;//7：从第三方游戏厅转出，第三方已扣款，但是我方发送错误导致转账未成功。
	private java.util.Date createDate;
	private java.util.Date updateDate;
	private BigDecimal balance; // 转账后余额
	private BigDecimal origamount; //转账前余额
	
	private BigDecimal otherbefore; //转账前第三方余额
	private BigDecimal otherafter; //转账后第三方余额

	// columns END

	public GameTransfer() {
	}

	public GameTransfer(java.lang.Long gtid) {
		this.gtid = gtid;
	}

	public void setGtid(java.lang.Long value) {
		this.gtid = value;
	}

	public java.lang.Long getGtid() {
		return this.gtid;
	}

	public void setUuid(java.lang.Long value) {
		this.uuid = value;
	}

	public java.lang.Long getUuid() {
		return this.uuid;
	}

	public void setGpid(java.lang.Long value) {
		this.gpid = value;
	}

	public java.lang.Long getGpid() {
		return this.gpid;
	}

	public void setGamename(java.lang.String value) {
		this.gamename = value;
	}

	public java.lang.String getGamename() {
		return this.gamename;
	}

	public void setTogpid(java.lang.Long value) {
		this.togpid = value;
	}

	public java.lang.Long getTogpid() {
		return this.togpid;
	}

	public void setTogamename(java.lang.String value) {
		this.togamename = value;
	}

	public java.lang.String getTogamename() {
		return this.togamename;
	}

	public void setAmount(BigDecimal value) {
		this.amount = value;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setRemark(java.lang.String value) {
		this.remark = value;
	}

	public java.lang.String getRemark() {
		return this.remark;
	}

	public void setStatus(java.lang.Integer value) {
		this.status = value;
	}

	public java.lang.Integer getStatus() {
		return this.status;
	}

	public void setCreateDate(java.util.Date value) {
		this.createDate = value;
	}

	public java.util.Date getCreateDate() {
		return this.createDate;
	}

	public void setUpdateDate(java.util.Date value) {
		this.updateDate = value;
	}

	public java.util.Date getUpdateDate() {
		return this.updateDate;
	}
	

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	public BigDecimal getOrigamount() {
		return origamount;
	}

	public void setOrigamount(BigDecimal origamount) {
		this.origamount = origamount;
	}

	public BigDecimal getOtherbefore() {
		return otherbefore;
	}

	public void setOtherbefore(BigDecimal otherbefore) {
		this.otherbefore = otherbefore;
	}

	public BigDecimal getOtherafter() {
		return otherafter;
	}

	public void setOtherafter(BigDecimal otherafter) {
		this.otherafter = otherafter;
	}

	public String toString() {
		return new ToStringBuilder(this).append("Gtid", getGtid())
				.append("Uuid", getUuid()).append("Gpid", getGpid())
				.append("Gamename", getGamename())
				.append("Togpid", getTogpid())
				.append("Togamename", getTogamename())
				.append("Amount", getAmount()).append("Remark", getRemark())
				.append("Status", getStatus())
				.append("CreateDate", getCreateDate())
				.append("Balance",getBalance())
				.append("UpdateDate", getUpdateDate()).toString();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getGtid()).append(getUuid())
				.append(getGpid()).append(getGamename()).append(getTogpid())
				.append(getTogamename()).append(getAmount())
				.append(getRemark()).append(getStatus()).append(getBalance())
				.append(getCreateDate()).append(getUpdateDate()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (obj instanceof GameTransfer == false)
			return false;
		if (this == obj)
			return true;
		GameTransfer other = (GameTransfer) obj;
		return new EqualsBuilder().append(getGtid(), other.getGtid())
				.append(getUuid(), other.getUuid())
				.append(getGpid(), other.getGpid())
				.append(getGamename(), other.getGamename())
				.append(getTogpid(), other.getTogpid())
				.append(getTogamename(), other.getTogamename())
				.append(getAmount(), other.getAmount())
				.append(getRemark(), other.getRemark())
				.append(getStatus(), other.getStatus())
				.append(getCreateDate(), other.getCreateDate())
				.append(getBalance(), other.getBalance())
				.append(getUpdateDate(), other.getUpdateDate()).isEquals();
	}

	@Override
	public Serializable getID() {
		return this.gtid;
	}
}
