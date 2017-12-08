package com.gameportal.manage.payplatform.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.manage.system.model.BaseEntity;

public class PayPlatform extends BaseEntity {
	
	//alias
	public static final String TABLE_ALIAS = "PayPlatform";
	public static final String ALIAS_PPID = "支付平台信息ID";
	public static final String ALIAS_PNAME = "支付平台名称";
	public static final String ALIAS_DOMAINNAME = "平台域名";
	public static final String ALIAS_PLATFORMKEY = "平台KEY值";
	public static final String ALIAS_CIPHERCODE = "ciphercode";
	public static final String ALIAS_RETURN_URL = "商户回调地址";
	public static final String ALIAS_NOTICE_URL = "商户通知地址";
	public static final String ALIAS_STATUS = "状态 0 关闭 1开启";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_DATE = "更新时间";
	
	
	//columns START
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
	private String channelType;
	
	private BigDecimal fee;
	//columns END

	public PayPlatform(){
	}

	public PayPlatform(
		java.lang.Long ppid
	){
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

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("Ppid",getPpid())
			.append("Pname",getPname())
			.append("Domainname",getDomainname())
			.append("Platformkey",getPlatformkey())
			.append("Ciphercode",getCiphercode())
			.append("ReturnUrl",getReturnUrl())
			.append("NoticeUrl",getNoticeUrl())
			.append("Status",getStatus())
			.append("CreateDate",getCreateDate())
			.append("UpdateDate",getUpdateDate())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getPpid())
			.append(getPname())
			.append(getDomainname())
			.append(getPlatformkey())
			.append(getCiphercode())
			.append(getReturnUrl())
			.append(getNoticeUrl())
			.append(getStatus())
			.append(getCreateDate())
			.append(getUpdateDate())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof PayPlatform == false) return false;
		if(this == obj) return true;
		PayPlatform other = (PayPlatform)obj;
		return new EqualsBuilder()
			.append(getPpid(),other.getPpid())
			.append(getPname(),other.getPname())
			.append(getDomainname(),other.getDomainname())
			.append(getPlatformkey(),other.getPlatformkey())
			.append(getCiphercode(),other.getCiphercode())
			.append(getReturnUrl(),other.getReturnUrl())
			.append(getNoticeUrl(),other.getNoticeUrl())
			.append(getStatus(),other.getStatus())
			.append(getCreateDate(),other.getCreateDate())
			.append(getUpdateDate(),other.getUpdateDate())
			.isEquals();
	}
	



	@Override
	public Serializable getID() {

		return  this.ppid;
	}
}

