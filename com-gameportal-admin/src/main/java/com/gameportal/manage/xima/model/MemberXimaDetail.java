package com.gameportal.manage.xima.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.manage.system.model.BaseEntity;
import com.gameportal.manage.util.DateUtil;

public class MemberXimaDetail extends BaseEntity {
	
	//alias
	public static final String TABLE_ALIAS = "MemberXimaDetail";
	public static final String ALIAS_MXDID = "主键ID";
	public static final String ALIAS_GPID = "游戏平台ID";
	public static final String ALIAS_UIID = "会员ID";
	public static final String ALIAS_AMOUNT = "本次返水所得";
	public static final String ALIAS_YMDSTART = "返水开始日期";
	public static final String ALIAS_YMDEND = "返水结束日期";
	public static final String ALIAS_OPTUIID = "操作用户ID";
	public static final String ALIAS_OPTUSERNAME = "操作用户名称";
	public static final String ALIAS_OPTTYPE = "操作类型 0自助洗码，1洗码清零，2强制洗码";
	public static final String ALIAS_OPTTIME = "操作时间";
	public static final String ALIAS_PARAMLOG = "本次洗码参数日志";
	
	
	//columns START
	private java.lang.Long mxdid;
	private java.lang.Long gpid;
	private java.lang.Long uiid;
	private java.lang.String amount;
	private java.lang.String ymdstart;
	private java.lang.String ymdend;
	private java.lang.Long optuiid;
	private java.lang.String optusername;
	private java.lang.Integer opttype;
	private java.lang.String opttime;
	private java.lang.String paramlog;
	//columns END
	//传值用 START
	private String opttimeStr;

//	public String getOpttimeStr() {
//		if (null != this.opttime) {
//			this.opttimeStr = DateUtil.getStrYMDHMSByDate(new java.util.Date(
//					this.opttime.getTime()));
//		}
//		return opttimeStr;
//	}
//
//	public void setOpttimeStr(String opttimeStr) {
//		if (null == this.opttime) {
//			this.opttime = new java.sql.Timestamp(DateUtil.getDateByStr(
//					this.opttimeStr).getTime());
//		}
//		this.opttimeStr = opttimeStr;
//	}

	//传值用 END
	

	public MemberXimaDetail(){
	}

	
	public MemberXimaDetail(
		java.lang.Long mxdid
	){
		this.mxdid = mxdid;
	}

	
	public void setMxdid(java.lang.Long value) {
		this.mxdid = value;
	}
	
	public java.lang.Long getMxdid() {
		return this.mxdid;
	}
	
	public void setGpid(java.lang.Long value) {
		this.gpid = value;
	}
	
	public java.lang.Long getGpid() {
		return this.gpid;
	}
	
	public void setUiid(java.lang.Long value) {
		this.uiid = value;
	}
	
	public java.lang.Long getUiid() {
		return this.uiid;
	}
	
	
	
	public java.lang.String getAmount() {
		return amount;
	}


	public void setAmount(java.lang.String amount) {
		this.amount = amount;
	}


	public java.lang.String getYmdstart() {
		return ymdstart;
	}

	public void setYmdstart(java.lang.String ymdstart) {
		this.ymdstart = ymdstart;
	}

	public java.lang.String getYmdend() {
		return ymdend;
	}

	public void setYmdend(java.lang.String ymdend) {
		this.ymdend = ymdend;
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

	public void setParamlog(java.lang.String value) {
		this.paramlog = value;
	}
	
	public java.lang.String getParamlog() {
		return this.paramlog;
	}
	
	

	public java.lang.String getOpttime() {
		return opttime;
	}


	public void setOpttime(java.lang.String opttime) {
		this.opttime = opttime;
	}


	public String getOpttimeStr() {
		return opttimeStr;
	}


	public void setOpttimeStr(String opttimeStr) {
		this.opttimeStr = opttimeStr;
	}


	public String toString() {
		return new ToStringBuilder(this)
			.append("Mxdid",getMxdid())
			.append("Gpid",getGpid())
			.append("Uiid",getUiid())
			.append("Amount",getAmount())
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
			.append(getMxdid())
			.append(getGpid())
			.append(getUiid())
			.append(getAmount())
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
		if(obj instanceof MemberXimaDetail == false) return false;
		if(this == obj) return true;
		MemberXimaDetail other = (MemberXimaDetail)obj;
		return new EqualsBuilder()
			.append(getMxdid(),other.getMxdid())
			.append(getGpid(),other.getGpid())
			.append(getUiid(),other.getUiid())
			.append(getAmount(),other.getAmount())
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
		return  this.mxdid;
	}
}

