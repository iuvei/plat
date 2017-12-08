package com.gameportal.manage.xima.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.manage.system.model.BaseEntity;

public class ProxyXimaSet extends BaseEntity {
	
	//alias
	public static final String TABLE_ALIAS = "ProxyXimaSet";
	public static final String ALIAS_PXSID = "主键ID";
	public static final String ALIAS_UIID = "代理ID";
	public static final String ALIAS_ACCOUNT = "代理账号";
	public static final String ALIAS_NAME = "代理名字";
	public static final String ALIAS_SCALE = "返水的比例值（是个小数）";
	public static final String ALIAS_UPDATEUSERID = "更新者ID";
	public static final String ALIAS_UPDATETIME = "更新时间";
	public static final String ALIAS_UPDATEUSERNAME = "更新者名称";
	
	
	//columns START
	private java.lang.Long pxsid;
	private java.lang.Long uiid;
	private java.lang.String account;
	private java.lang.String name;
	private Double scale;
	private java.lang.Long updateuserid;
	private java.sql.Timestamp updatetime;
	private java.lang.String updateusername;
	//columns END

	public ProxyXimaSet(){
	}

	public ProxyXimaSet(
		java.lang.Long pxsid
	){
		this.pxsid = pxsid;
	}

	
	public void setPxsid(java.lang.Long value) {
		this.pxsid = value;
	}
	
	public java.lang.Long getPxsid() {
		return this.pxsid;
	}
	
	public void setUiid(java.lang.Long value) {
		this.uiid = value;
	}
	
	public java.lang.Long getUiid() {
		return this.uiid;
	}
	
	public void setAccount(java.lang.String value) {
		this.account = value;
	}
	
	public java.lang.String getAccount() {
		return this.account;
	}
	
	public void setName(java.lang.String value) {
		this.name = value;
	}
	
	public java.lang.String getName() {
		return this.name;
	}
	
	public void setScale(Double value) {
		this.scale = value;
	}
	
	public Double getScale() {
		return this.scale;
	}
	
	public void setUpdateuserid(java.lang.Long value) {
		this.updateuserid = value;
	}
	
	public java.lang.Long getUpdateuserid() {
		return this.updateuserid;
	}
	
	public void setUpdatetime(java.sql.Timestamp value) {
		this.updatetime = value;
	}
	
	public java.sql.Timestamp getUpdatetime() {
		return this.updatetime;
	}
	
	public void setUpdateusername(java.lang.String value) {
		this.updateusername = value;
	}
	
	public java.lang.String getUpdateusername() {
		return this.updateusername;
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("Pxsid",getPxsid())
			.append("Uiid",getUiid())
			.append("Account",getAccount())
			.append("Name",getName())
			.append("Scale",getScale())
			.append("Updateuserid",getUpdateuserid())
			.append("Updatetime",getUpdatetime())
			.append("Updateusername",getUpdateusername())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getPxsid())
			.append(getUiid())
			.append(getAccount())
			.append(getName())
			.append(getScale())
			.append(getUpdateuserid())
			.append(getUpdatetime())
			.append(getUpdateusername())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof ProxyXimaSet == false) return false;
		if(this == obj) return true;
		ProxyXimaSet other = (ProxyXimaSet)obj;
		return new EqualsBuilder()
			.append(getPxsid(),other.getPxsid())
			.append(getUiid(),other.getUiid())
			.append(getAccount(),other.getAccount())
			.append(getName(),other.getName())
			.append(getScale(),other.getScale())
			.append(getUpdateuserid(),other.getUpdateuserid())
			.append(getUpdatetime(),other.getUpdatetime())
			.append(getUpdateusername(),other.getUpdateusername())
			.isEquals();
	}
	



	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return  this.pxsid;
	}
}

