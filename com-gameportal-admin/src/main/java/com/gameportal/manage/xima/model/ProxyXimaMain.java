package com.gameportal.manage.xima.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.manage.system.model.BaseEntity;

public class ProxyXimaMain extends BaseEntity {
	
	//alias
	public static final String TABLE_ALIAS = "ProxyXimaMain";
	public static final String ALIAS_PXMID = "主键ID";
	public static final String ALIAS_UIID = "代理ID";
	public static final String ALIAS_ACCOUNT = "代理账号";
	public static final String ALIAS_NAME = "代理名字";
	public static final String ALIAS_TOTAL = "返水所得总额";
	public static final String ALIAS_YMDSTART = "返水开始日期";
	public static final String ALIAS_YMDEND = "返水结束日期";
	public static final String ALIAS_UPDATETIME = "操作时间";
	public static final String ALIAS_LOCKED = "锁定状态 0未锁定，1锁定";
	
	
	//columns START
	private java.lang.Long pxmid;
	private java.lang.Long uiid;
	private java.lang.String account;
	private java.lang.String name;
	private java.lang.Long total;
	private java.sql.Date ymdstart;
	private java.sql.Date ymdend;
	private java.sql.Timestamp updatetime;
	private java.lang.Integer locked;
	//columns END

	public ProxyXimaMain(){
	}

	public ProxyXimaMain(
		java.lang.Long pxmid
	){
		this.pxmid = pxmid;
	}

	
	public void setPxmid(java.lang.Long value) {
		this.pxmid = value;
	}
	
	public java.lang.Long getPxmid() {
		return this.pxmid;
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
	
	public void setTotal(java.lang.Long value) {
		this.total = value;
	}
	
	public java.lang.Long getTotal() {
		return this.total;
	}
	
	public void setYmdstart(java.sql.Date value) {
		this.ymdstart = value;
	}
	
	public java.sql.Date getYmdstart() {
		return this.ymdstart;
	}
	
	public void setYmdend(java.sql.Date value) {
		this.ymdend = value;
	}
	
	public java.sql.Date getYmdend() {
		return this.ymdend;
	}
	
	public void setUpdatetime(java.sql.Timestamp value) {
		this.updatetime = value;
	}
	
	public java.sql.Timestamp getUpdatetime() {
		return this.updatetime;
	}
	
	public void setLocked(java.lang.Integer value) {
		this.locked = value;
	}
	
	public java.lang.Integer getLocked() {
		return this.locked;
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("Pxmid",getPxmid())
			.append("Uiid",getUiid())
			.append("Account",getAccount())
			.append("Name",getName())
			.append("Total",getTotal())
			.append("Ymdstart",getYmdstart())
			.append("Ymdend",getYmdend())
			.append("Updatetime",getUpdatetime())
			.append("Locked",getLocked())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getPxmid())
			.append(getUiid())
			.append(getAccount())
			.append(getName())
			.append(getTotal())
			.append(getYmdstart())
			.append(getYmdend())
			.append(getUpdatetime())
			.append(getLocked())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof ProxyXimaMain == false) return false;
		if(this == obj) return true;
		ProxyXimaMain other = (ProxyXimaMain)obj;
		return new EqualsBuilder()
			.append(getPxmid(),other.getPxmid())
			.append(getUiid(),other.getUiid())
			.append(getAccount(),other.getAccount())
			.append(getName(),other.getName())
			.append(getTotal(),other.getTotal())
			.append(getYmdstart(),other.getYmdstart())
			.append(getYmdend(),other.getYmdend())
			.append(getUpdatetime(),other.getUpdatetime())
			.append(getLocked(),other.getLocked())
			.isEquals();
	}
	



	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return  this.pxmid;
	}
}

