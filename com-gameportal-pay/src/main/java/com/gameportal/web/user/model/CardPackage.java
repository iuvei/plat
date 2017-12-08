package com.gameportal.web.user.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.pay.model.BaseEntity;

public class CardPackage extends BaseEntity {

	// alias
	public static final String TABLE_ALIAS = "CardPackage";
	public static final String ALIAS_CPID = "银行卡ID";
	public static final String ALIAS_UIID = "帐号ID";
	public static final String ALIAS_BANKNAME = "银行名称";
	public static final String ALIAS_PROVINCE = "开户省";
	public static final String ALIAS_CITY = "开户市";
	public static final String ALIAS_OPENINGBANK = "开户银行";
	public static final String ALIAS_ACCOUNTNAME = "开户姓名";
	public static final String ALIAS_CARDNUMBER = "卡号";
	public static final String ALIAS_STATUS = "状态 0 禁用 1启用";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_DATE = "更新时间";

	// columns START
	private java.lang.Long cpid;
	private java.lang.Long uiid;
	private java.lang.String bankname;
	private java.lang.String province;
	private java.lang.String city;
	private java.lang.String openingbank;
	private java.lang.String accountname;
	private java.lang.String cardnumber;
	private java.lang.Integer status;
	private java.util.Date createDate;
	private java.util.Date updateDate;

	// columns END

	public CardPackage() {
	}

	public CardPackage(java.lang.Long cpid) {
		this.cpid = cpid;
	}

	public void setCpid(java.lang.Long value) {
		this.cpid = value;
	}

	public java.lang.Long getCpid() {
		return this.cpid;
	}

	public void setUiid(java.lang.Long value) {
		this.uiid = value;
	}

	public java.lang.Long getUiid() {
		return this.uiid;
	}

	public void setBankname(java.lang.String value) {
		this.bankname = value;
	}

	public java.lang.String getBankname() {
		return this.bankname;
	}

	public void setProvince(java.lang.String value) {
		this.province = value;
	}

	public java.lang.String getProvince() {
		return this.province;
	}

	public void setCity(java.lang.String value) {
		this.city = value;
	}

	public java.lang.String getCity() {
		return this.city;
	}

	public void setOpeningbank(java.lang.String value) {
		this.openingbank = value;
	}

	public java.lang.String getOpeningbank() {
		return this.openingbank;
	}

	public void setAccountname(java.lang.String value) {
		this.accountname = value;
	}

	public java.lang.String getAccountname() {
		return this.accountname;
	}

	public void setCardnumber(java.lang.String value) {
		this.cardnumber = value;
	}

	public java.lang.String getCardnumber() {
		return this.cardnumber;
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

	public String toString() {
		return new ToStringBuilder(this).append("Cpid", getCpid())
				.append("Uiid", getUiid()).append("Bankname", getBankname())
				.append("Province", getProvince()).append("City", getCity())
				.append("Openingbank", getOpeningbank())
				.append("Accountname", getAccountname())
				.append("Cardnumber", getCardnumber())
				.append("Status", getStatus())
				.append("CreateDate", getCreateDate())
				.append("UpdateDate", getUpdateDate()).toString();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getCpid()).append(getUiid())
				.append(getBankname()).append(getProvince()).append(getCity())
				.append(getOpeningbank()).append(getAccountname())
				.append(getCardnumber()).append(getStatus())
				.append(getCreateDate()).append(getUpdateDate()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (obj instanceof CardPackage == false)
			return false;
		if (this == obj)
			return true;
		CardPackage other = (CardPackage) obj;
		return new EqualsBuilder().append(getCpid(), other.getCpid())
				.append(getUiid(), other.getUiid())
				.append(getBankname(), other.getBankname())
				.append(getProvince(), other.getProvince())
				.append(getCity(), other.getCity())
				.append(getOpeningbank(), other.getOpeningbank())
				.append(getAccountname(), other.getAccountname())
				.append(getCardnumber(), other.getCardnumber())
				.append(getStatus(), other.getStatus())
				.append(getCreateDate(), other.getCreateDate())
				.append(getUpdateDate(), other.getUpdateDate()).isEquals();
	}

	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return this.cpid;
	}
}
