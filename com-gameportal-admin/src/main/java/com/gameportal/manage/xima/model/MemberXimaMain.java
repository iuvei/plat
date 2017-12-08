package com.gameportal.manage.xima.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.manage.system.model.BaseEntity;

public class MemberXimaMain extends BaseEntity {

	// alias
	public static final String TABLE_ALIAS = "MemberXimaMain";
	public static final String ALIAS_MXMID = "主键ID";
	public static final String ALIAS_GPID = "游戏平台ID";
	public static final String ALIAS_UIID = "会员ID";
	public static final String ALIAS_ACCOUNT = "会员账号";
	public static final String ALIAS_NAME = "会员名字";
	public static final String ALIAS_TOTAL = "返水所得总额";
	public static final String ALIAS_YMDSTART = "返水开始日期";
	public static final String ALIAS_YMDEND = "返水结束日期";
	public static final String ALIAS_UPDATETIME = "操作时间";
	public static final String ALIAS_LOCKED = "锁定状态 0未锁定，1锁定";

	// columns START
	private java.lang.Long mxmid;
	private java.lang.String gpid;
	private java.lang.Long uiid;
	private java.lang.String account;
	private java.lang.String name;
	private java.lang.String total;
	private java.lang.String ymdstart;
	private java.lang.String ymdend;
	private java.lang.String updatetime;
	
	/**
	 * 0:未入账
	 * 1：已入账
	 * 2：审核失败
	 */
	private java.lang.Integer locked;
	// columns END

	// 传值用 START
	private java.lang.String gpname;
	
	private String lockedname; //状态名称

	
	public String getLockedname() {
		return lockedname;
	}

	public void setLockedname(String lockedname) {
		this.lockedname = lockedname;
	}

	public java.lang.String getGpname() {
		return gpname;
	}

	public void setGpname(java.lang.String gpname) {
		this.gpname = gpname;
	}

	// 传值用 END

	public MemberXimaMain() {
	}

	public MemberXimaMain(java.lang.Long mxmid) {
		this.mxmid = mxmid;
	}

	public void setMxmid(java.lang.Long value) {
		this.mxmid = value;
	}

	public java.lang.Long getMxmid() {
		return this.mxmid;
	}

	public java.lang.String getGpid() {
		return gpid;
	}

	public void setGpid(java.lang.String gpid) {
		this.gpid = gpid;
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


	public java.lang.String getTotal() {
		return total;
	}

	public void setTotal(java.lang.String total) {
		this.total = total;
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


	public java.lang.String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(java.lang.String updatetime) {
		this.updatetime = updatetime;
	}

	public void setLocked(java.lang.Integer value) {
		this.locked = value;
	}

	public java.lang.Integer getLocked() {
		return this.locked;
	}

	public String toString() {
		return new ToStringBuilder(this).append("Mxmid", getMxmid())
				.append("Gpid", getGpid()).append("Uiid", getUiid())
				.append("Account", getAccount()).append("Name", getName())
				.append("Total", getTotal()).append("Ymdstart", getYmdstart())
				.append("Ymdend", getYmdend())
				.append("Updatetime", getUpdatetime())
				.append("Locked", getLocked()).toString();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getMxmid()).append(getGpid())
				.append(getUiid()).append(getAccount()).append(getName())
				.append(getTotal()).append(getYmdstart()).append(getYmdend())
				.append(getUpdatetime()).append(getLocked()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (obj instanceof MemberXimaMain == false)
			return false;
		if (this == obj)
			return true;
		MemberXimaMain other = (MemberXimaMain) obj;
		return new EqualsBuilder().append(getMxmid(), other.getMxmid())
				.append(getGpid(), other.getGpid())
				.append(getUiid(), other.getUiid())
				.append(getAccount(), other.getAccount())
				.append(getName(), other.getName())
				.append(getTotal(), other.getTotal())
				.append(getYmdstart(), other.getYmdstart())
				.append(getYmdend(), other.getYmdend())
				.append(getUpdatetime(), other.getUpdatetime())
				.append(getLocked(), other.getLocked()).isEquals();
	}

	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return this.mxmid;
	}
}
