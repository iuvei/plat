package com.gameportal.web.payPlat.model;

import java.io.Serializable;

import com.gameportal.web.user.model.BaseEntity;

public class PayPlatform extends BaseEntity {
	private static final long serialVersionUID = 1L;
	// columns START
	private java.lang.Long ppid;
	private java.lang.String pname;
	private java.lang.String domainname;
	private java.lang.String platformkey;
	private java.lang.String ciphercode;
	private java.lang.String returnUrl;
	private java.lang.String noticeUrl;
	private java.lang.Integer status;
	private java.util.Date createDate;
	private java.util.Date updateDate;
	private Integer sequence; // 排序

	// columns END

	public PayPlatform() {
	}

	public PayPlatform(java.lang.Long ppid) {
		this.ppid = ppid;
	}

	public void setPpid(java.lang.Long value) {
		this.ppid = value;
	}

	public java.lang.Long getPpid() {
		return this.ppid;
	}

	public void setPname(java.lang.String value) {
		this.pname = value;
	}

	public java.lang.String getPname() {
		return this.pname;
	}

	public void setDomainname(java.lang.String value) {
		this.domainname = value;
	}

	public java.lang.String getDomainname() {
		return this.domainname;
	}

	public void setPlatformkey(java.lang.String value) {
		this.platformkey = value;
	}

	public java.lang.String getPlatformkey() {
		return this.platformkey;
	}

	public void setCiphercode(java.lang.String value) {
		this.ciphercode = value;
	}

	public java.lang.String getCiphercode() {
		return this.ciphercode;
	}

	public void setReturnUrl(java.lang.String value) {
		this.returnUrl = value;
	}

	public java.lang.String getReturnUrl() {
		return this.returnUrl;
	}

	public void setNoticeUrl(java.lang.String value) {
		this.noticeUrl = value;
	}

	public java.lang.String getNoticeUrl() {
		return this.noticeUrl;
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
	
	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return this.ppid;
	}
}
