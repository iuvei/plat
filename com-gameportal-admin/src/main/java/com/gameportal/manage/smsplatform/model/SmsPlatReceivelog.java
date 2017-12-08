package com.gameportal.manage.smsplatform.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.manage.system.model.BaseEntity;

public class SmsPlatReceivelog extends BaseEntity {

	// alias
	public static final String TABLE_ALIAS = "SmsPlatReceivelog";
	public static final String ALIAS_SPSID = "sprid";
	public static final String ALIAS_SPIID = "spiid";
	public static final String ALIAS_ACCOUNT = "account";
	public static final String ALIAS_MOBILE = "mobile";
	public static final String ALIAS_CONTENT = "content";
	public static final String ALIAS_SENDTIME = "sendtime";
	public static final String ALIAS_CREATETIME = "createtime";

	// columns START
	private java.lang.Long sprid;
	private java.lang.Long spiid;
	private java.lang.String account;
	private java.lang.String mobile;
	private java.lang.String content;
	private java.sql.Timestamp sendtime;
	private java.sql.Timestamp createtime;

	// columns END

	public SmsPlatReceivelog() {
	}

	public SmsPlatReceivelog(java.lang.Long sprid) {
		this.sprid = sprid;
	}

	public void setSprid(java.lang.Long value) {
		this.sprid = value;
	}

	public java.lang.Long getSprid() {
		return this.sprid;
	}

	public void setSpiid(java.lang.Long value) {
		this.spiid = value;
	}

	public java.lang.Long getSpiid() {
		return this.spiid;
	}

	public void setAccount(java.lang.String value) {
		this.account = value;
	}

	public java.lang.String getAccount() {
		return this.account;
	}

	public void setMobile(java.lang.String value) {
		this.mobile = value;
	}

	public java.lang.String getMobile() {
		return this.mobile;
	}

	public void setContent(java.lang.String value) {
		this.content = value;
	}

	public java.lang.String getContent() {
		return this.content;
	}

	public void setSendtime(java.sql.Timestamp value) {
		this.sendtime = value;
	}

	public java.sql.Timestamp getSendtime() {
		return this.sendtime;
	}

	public void setCreatetime(java.sql.Timestamp value) {
		this.createtime = value;
	}

	public java.sql.Timestamp getCreatetime() {
		return this.createtime;
	}

	public String toString() {
		return new ToStringBuilder(this).append("Sprid", getSprid())
				.append("Spiid", getSpiid()).append("Account", getAccount())
				.append("Mobile", getMobile()).append("Content", getContent())
				.append("Sendtime", getSendtime())
				.append("Createtime", getCreatetime()).toString();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getSprid()).append(getSpiid())
				.append(getAccount()).append(getMobile()).append(getContent())
				.append(getSendtime()).append(getCreatetime()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (obj instanceof SmsPlatReceivelog == false)
			return false;
		if (this == obj)
			return true;
		SmsPlatReceivelog other = (SmsPlatReceivelog) obj;
		return new EqualsBuilder().append(getSprid(), other.getSprid())
				.append(getSpiid(), other.getSpiid())
				.append(getAccount(), other.getAccount())
				.append(getMobile(), other.getMobile())
				.append(getContent(), other.getContent())
				.append(getSendtime(), other.getSendtime())
				.append(getCreatetime(), other.getCreatetime()).isEquals();
	}

	@Override
	public Serializable getID() {

		return this.sprid;
	}
}
