package com.gameportal.manage.gameplatform.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.manage.system.model.BaseEntity;

public class GamePlatform extends BaseEntity {
	
	//alias
	public static final String TABLE_ALIAS = "GamePlatform";
	public static final String ALIAS_GPID = "游戏平台ID";
	public static final String ALIAS_GPNAME = "游戏平台名称";
	public static final String ALIAS_DOMAINNAME = "游戏平台域名";
	public static final String ALIAS_DOMAINIP = "游戏平台IP";
	public static final String ALIAS_DESKEY = "游戏平台KEY";
	public static final String ALIAS_APIACCOUNT = "API帐号";
	public static final String ALIAS_CIPHERCODE = "游戏平台密码";
	public static final String ALIAS_STATUS = "状态 0 关闭 1开启";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_DATE = "更新时间";
	
	
	//columns START
	private java.lang.Long gpid;
	private java.lang.String gpname;
	private java.lang.String domainname;
	private java.lang.String domainip;
	private java.lang.String deskey;
	private java.lang.String apiaccount;
	private java.lang.String ciphercode;
	private java.lang.Integer status;
	private java.util.Date createDate;
	private java.util.Date updateDate;
	private String fullname;
	//columns END

	public GamePlatform(){
	}

	public GamePlatform(
		java.lang.Long gpid
	){
		this.gpid = gpid;
	}

	
	public void setGpid(java.lang.Long value) {
		this.gpid = value;
	}
	
	public java.lang.Long getGpid() {
		return this.gpid;
	}
	
	public void setGpname(java.lang.String value) {
		this.gpname = value;
	}
	
	public java.lang.String getGpname() {
		return this.gpname;
	}
	
	public void setDomainname(java.lang.String value) {
		this.domainname = value;
	}
	
	public java.lang.String getDomainname() {
		return this.domainname;
	}
	
	public void setDomainip(java.lang.String value) {
		this.domainip = value;
	}
	
	public java.lang.String getDomainip() {
		return this.domainip;
	}
	
	public void setDeskey(java.lang.String value) {
		this.deskey = value;
	}
	
	public java.lang.String getDeskey() {
		return this.deskey;
	}
	
	public void setApiaccount(java.lang.String value) {
		this.apiaccount = value;
	}
	
	public java.lang.String getApiaccount() {
		return this.apiaccount;
	}
	
	public void setCiphercode(java.lang.String value) {
		this.ciphercode = value;
	}
	
	public java.lang.String getCiphercode() {
		return this.ciphercode;
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
		return new ToStringBuilder(this)
			.append("Gpid",getGpid())
			.append("Gpname",getGpname())
			.append("Domainname",getDomainname())
			.append("Domainip",getDomainip())
			.append("Deskey",getDeskey())
			.append("Apiaccount",getApiaccount())
			.append("Ciphercode",getCiphercode())
			.append("Status",getStatus())
			.append("CreateDate",getCreateDate())
			.append("UpdateDate",getUpdateDate())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getGpid())
			.append(getGpname())
			.append(getDomainname())
			.append(getDomainip())
			.append(getDeskey())
			.append(getApiaccount())
			.append(getCiphercode())
			.append(getStatus())
			.append(getCreateDate())
			.append(getUpdateDate())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof GamePlatform == false) return false;
		if(this == obj) return true;
		GamePlatform other = (GamePlatform)obj;
		return new EqualsBuilder()
			.append(getGpid(),other.getGpid())
			.append(getGpname(),other.getGpname())
			.append(getDomainname(),other.getDomainname())
			.append(getDomainip(),other.getDomainip())
			.append(getDeskey(),other.getDeskey())
			.append(getApiaccount(),other.getApiaccount())
			.append(getCiphercode(),other.getCiphercode())
			.append(getStatus(),other.getStatus())
			.append(getCreateDate(),other.getCreateDate())
			.append(getUpdateDate(),other.getUpdateDate())
			.isEquals();
	}
	



	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return  this.gpid;
	}
}

