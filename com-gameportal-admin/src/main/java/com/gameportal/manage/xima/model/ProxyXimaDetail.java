package com.gameportal.manage.xima.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.manage.system.model.BaseEntity;

public class ProxyXimaDetail extends BaseEntity {
	
	//alias
	public static final String TABLE_ALIAS = "ProxyXimaDetail";
	public static final String ALIAS_PXDID = "主键ID";
	public static final String ALIAS_UIID = "代理ID";
	public static final String ALIAS_AMOUNT = "本次返水所得";
	public static final String ALIAS_FREETIME = "解冻时间（未解冻时此值为空）";
	public static final String ALIAS_YMDSTART = "返水开始日期";
	public static final String ALIAS_YMDEND = "返水结束日期";
	public static final String ALIAS_OPTUIID = "操作用户ID";
	public static final String ALIAS_OPTUSERNAME = "操作用户名称";
	public static final String ALIAS_OPTTYPE = "操作类型 0自助洗码，1洗码清零，2强制洗码";
	public static final String ALIAS_OPTTIME = "操作时间";
	public static final String ALIAS_PARAMLOG = "本次洗码参数日志";
	
	
	//columns START
	private java.lang.Long pxdid;
	private java.lang.Long uiid;
	private java.lang.Long amount;
	private java.sql.Timestamp freetime;
	private java.sql.Date ymdstart;
	private java.sql.Date ymdend;
	private java.lang.Long optuiid;
	private java.lang.String optusername;
	private java.lang.Integer opttype;
	private java.sql.Timestamp opttime;
	private java.lang.String paramlog;
	//columns END

	public ProxyXimaDetail(){
	}

	public ProxyXimaDetail(
		java.lang.Long pxdid
	){
		this.pxdid = pxdid;
	}

	
	public void setPxdid(java.lang.Long value) {
		this.pxdid = value;
	}
	
	public java.lang.Long getPxdid() {
		return this.pxdid;
	}
	
	public void setUiid(java.lang.Long value) {
		this.uiid = value;
	}
	
	public java.lang.Long getUiid() {
		return this.uiid;
	}
	
	public void setAmount(java.lang.Long value) {
		this.amount = value;
	}
	
	public java.lang.Long getAmount() {
		return this.amount;
	}
	
	public void setFreetime(java.sql.Timestamp value) {
		this.freetime = value;
	}
	
	public java.sql.Timestamp getFreetime() {
		return this.freetime;
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
	
	public void setOptuiid(java.lang.Long value) {
		this.optuiid = value;
	}
	
	public java.lang.Long getOptuiid() {
		return this.optuiid;
	}
	
	public void setOptusername(java.lang.String value) {
		this.optusername = value;
	}
	
	public java.lang.String getOptusername() {
		return this.optusername;
	}
	
	public void setOpttype(java.lang.Integer value) {
		this.opttype = value;
	}
	
	public java.lang.Integer getOpttype() {
		return this.opttype;
	}
	
	public void setOpttime(java.sql.Timestamp value) {
		this.opttime = value;
	}
	
	public java.sql.Timestamp getOpttime() {
		return this.opttime;
	}
	
	public void setParamlog(java.lang.String value) {
		this.paramlog = value;
	}
	
	public java.lang.String getParamlog() {
		return this.paramlog;
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("Pxdid",getPxdid())
			.append("Uiid",getUiid())
			.append("Amount",getAmount())
			.append("Freetime",getFreetime())
			.append("Ymdstart",getYmdstart())
			.append("Ymdend",getYmdend())
			.append("Optuiid",getOptuiid())
			.append("Optusername",getOptusername())
			.append("Opttype",getOpttype())
			.append("Opttime",getOpttime())
			.append("Paramlog",getParamlog())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getPxdid())
			.append(getUiid())
			.append(getAmount())
			.append(getFreetime())
			.append(getYmdstart())
			.append(getYmdend())
			.append(getOptuiid())
			.append(getOptusername())
			.append(getOpttype())
			.append(getOpttime())
			.append(getParamlog())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof ProxyXimaDetail == false) return false;
		if(this == obj) return true;
		ProxyXimaDetail other = (ProxyXimaDetail)obj;
		return new EqualsBuilder()
			.append(getPxdid(),other.getPxdid())
			.append(getUiid(),other.getUiid())
			.append(getAmount(),other.getAmount())
			.append(getFreetime(),other.getFreetime())
			.append(getYmdstart(),other.getYmdstart())
			.append(getYmdend(),other.getYmdend())
			.append(getOptuiid(),other.getOptuiid())
			.append(getOptusername(),other.getOptusername())
			.append(getOpttype(),other.getOpttype())
			.append(getOpttime(),other.getOpttime())
			.append(getParamlog(),other.getParamlog())
			.isEquals();
	}
	



	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return  this.pxdid;
	}
}

