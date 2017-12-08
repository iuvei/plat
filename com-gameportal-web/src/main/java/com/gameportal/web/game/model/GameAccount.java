package com.gameportal.web.game.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.web.user.model.BaseEntity;

public class GameAccount extends BaseEntity {

	// alias
	public static final String TABLE_ALIAS = "GameAccount";
	public static final String ALIAS_GAID = "游戏帐号ID";
	public static final String ALIAS_UIID = "用户ID";
	public static final String ALIAS_GPID = "平台类型";
	public static final String ALIAS_UNAME = "uname";
	public static final String ALIAS_MONEY = "平台金钱";
	public static final String ALIAS_STATUS = "状态";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_DATE = "更新时间";

	// columns START
	private java.lang.Long gaid;
	private java.lang.Long uiid;
	private java.lang.Long gpid;
	private java.lang.String uname;
	private java.lang.Integer money;
	private java.lang.Integer status;
	private java.util.Date createDate;
	private java.util.Date updateDate;
	
	//游戏全名
	private String fullname;

	// columns END

	public GameAccount() {
	}

	public GameAccount(java.lang.Long gaid) {
		this.gaid = gaid;
	}

	public void setGaid(java.lang.Long value) {
		this.gaid = value;
	}

	public java.lang.Long getGaid() {
		return this.gaid;
	}

	public void setUiid(java.lang.Long value) {
		this.uiid = value;
	}

	public java.lang.Long getUiid() {
		return this.uiid;
	}

	public void setGpid(java.lang.Long value) {
		this.gpid = value;
	}

	public java.lang.Long getGpid() {
		return this.gpid;
	}

	public void setUname(java.lang.String value) {
		this.uname = value;
	}

	public java.lang.String getUname() {
		return this.uname;
	}

	public void setMoney(java.lang.Integer value) {
		this.money = value;
	}

	public java.lang.Integer getMoney() {
		return this.money;
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

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String toString() {
		return new ToStringBuilder(this).append("Gaid", getGaid())
				.append("Uiid", getUiid()).append("Gpid", getGpid())
				.append("Uname", getUname()).append("Money", getMoney())
				.append("Status", getStatus())
				.append("CreateDate", getCreateDate())
				.append("UpdateDate", getUpdateDate()).toString();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getGaid()).append(getUiid())
				.append(getGpid()).append(getUname()).append(getMoney())
				.append(getStatus()).append(getCreateDate())
				.append(getUpdateDate()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (obj instanceof GameAccount == false)
			return false;
		if (this == obj)
			return true;
		GameAccount other = (GameAccount) obj;
		return new EqualsBuilder().append(getGaid(), other.getGaid())
				.append(getUiid(), other.getUiid())
				.append(getGpid(), other.getGpid())
				.append(getUname(), other.getUname())
				.append(getMoney(), other.getMoney())
				.append(getStatus(), other.getStatus())
				.append(getCreateDate(), other.getCreateDate())
				.append(getUpdateDate(), other.getUpdateDate()).isEquals();
	}

	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return this.gaid;
	}
}
