package com.gameportal.manage.smsplatform.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.manage.system.model.BaseEntity;

public class SmsPlatBlacklist extends BaseEntity {

	// alias
	public static final String TABLE_ALIAS = "SmsPlatBlacklist";
	public static final String ALIAS_SPBID = "spbid";
	public static final String ALIAS_SPIID = "spiid";
	public static final String ALIAS_MOBILE = "mobile";
	public static final String ALIAS_CREATETIME = "createtime";

	// columns START
	private java.lang.Long spbid;
	private java.lang.Long spiid;
	private java.lang.String mobile;
	private java.sql.Timestamp createtime;

	// columns END

	public SmsPlatBlacklist() {
	}

	public SmsPlatBlacklist(java.lang.Long spbid) {
		this.spbid = spbid;
	}

	public void setSpbid(java.lang.Long value) {
		this.spbid = value;
	}

	public java.lang.Long getSpbid() {
		return this.spbid;
	}

	public void setSpiid(java.lang.Long value) {
		this.spiid = value;
	}

	public java.lang.Long getSpiid() {
		return this.spiid;
	}

	public void setMobile(java.lang.String value) {
		this.mobile = value;
	}

	public java.lang.String getMobile() {
		return this.mobile;
	}

	public void setCreatetime(java.sql.Timestamp value) {
		this.createtime = value;
	}

	public java.sql.Timestamp getCreatetime() {
		return this.createtime;
	}

	public String toString() {
		return new ToStringBuilder(this).append("Spbid", getSpbid())
				.append("Spiid", getSpiid()).append("Mobile", getMobile())
				.append("Createtime", getCreatetime()).toString();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getSpbid()).append(getSpiid())
				.append(getMobile()).append(getCreatetime()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (obj instanceof SmsPlatBlacklist == false)
			return false;
		if (this == obj)
			return true;
		SmsPlatBlacklist other = (SmsPlatBlacklist) obj;
		return new EqualsBuilder().append(getSpbid(), other.getSpbid())
				.append(getSpiid(), other.getSpiid())
				.append(getMobile(), other.getMobile())
				.append(getCreatetime(), other.getCreatetime()).isEquals();
	}

	@Override
	public Serializable getID() {

		return this.spbid;
	}
}
