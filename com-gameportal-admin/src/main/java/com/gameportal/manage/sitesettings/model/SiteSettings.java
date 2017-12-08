package com.gameportal.manage.sitesettings.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.manage.system.model.BaseEntity;

public class SiteSettings extends BaseEntity {
	
	//alias
	public static final String TABLE_ALIAS = "SiteSettings";
	public static final String ALIAS_SSID = "站点ID";
	public static final String ALIAS_SITENAME = "站点名称";
	public static final String ALIAS_SITEURL = "站点URL";
	public static final String ALIAS_SPREADKEY = "推广关键词";
	public static final String ALIAS_LOCKEDCOUNT = "登录密码超过多少次锁定";
	public static final String ALIAS_ISCLOSED = "是否关闭站点 0 关闭 1 开启";
	public static final String ALIAS_ISREGISTER = "是否关闭注册 0 关闭  1开启";
	public static final String ALIAS_ISLOGIN = "是否关闭登录 0 关闭 1 开启";
	public static final String ALIAS_ISRECHARGE = "是否关闭会员充值 0 关闭  1 开启";
	public static final String ALIAS_ISDRAW = "是否关闭会员提款 0 关闭  1 开启";
	public static final String ALIAS_LOWESTDRAW = "最低提款次数";
	public static final String ALIAS_HIGHESTDRAW = "最高提款次数";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_DATE = "更新时间";
	
	
	//columns START
	private java.lang.Long ssid;
	private java.lang.String sitename;
	private java.lang.String siteurl;
	private java.lang.String spreadkey;
	private java.lang.Integer lockedcount;
	private java.lang.Integer isclosed;
	private java.lang.Integer isregister;
	private java.lang.Integer islogin;
	private java.lang.Integer isrecharge;
	private java.lang.Integer isdraw;
	private java.lang.Integer lowestdraw;
	private java.lang.Integer highestdraw;
	private java.util.Date createDate;
	private java.util.Date updateDate;
	//columns END

	public SiteSettings(){
	}

	public SiteSettings(
		java.lang.Long ssid
	){
		this.ssid = ssid;
	}

	
	public void setSsid(java.lang.Long value) {
		this.ssid = value;
	}
	
	public java.lang.Long getSsid() {
		return this.ssid;
	}
	
	public void setSitename(java.lang.String value) {
		this.sitename = value;
	}
	
	public java.lang.String getSitename() {
		return this.sitename;
	}
	
	public void setSiteurl(java.lang.String value) {
		this.siteurl = value;
	}
	
	public java.lang.String getSiteurl() {
		return this.siteurl;
	}
	
	public void setSpreadkey(java.lang.String value) {
		this.spreadkey = value;
	}
	
	public java.lang.String getSpreadkey() {
		return this.spreadkey;
	}
	
	public void setLockedcount(java.lang.Integer value) {
		this.lockedcount = value;
	}
	
	public java.lang.Integer getLockedcount() {
		return this.lockedcount;
	}
	
	public void setIsclosed(java.lang.Integer value) {
		this.isclosed = value;
	}
	
	public java.lang.Integer getIsclosed() {
		return this.isclosed;
	}
	
	public void setIsregister(java.lang.Integer value) {
		this.isregister = value;
	}
	
	public java.lang.Integer getIsregister() {
		return this.isregister;
	}
	
	public void setIslogin(java.lang.Integer value) {
		this.islogin = value;
	}
	
	public java.lang.Integer getIslogin() {
		return this.islogin;
	}
	
	public void setIsrecharge(java.lang.Integer value) {
		this.isrecharge = value;
	}
	
	public java.lang.Integer getIsrecharge() {
		return this.isrecharge;
	}
	
	public void setIsdraw(java.lang.Integer value) {
		this.isdraw = value;
	}
	
	public java.lang.Integer getIsdraw() {
		return this.isdraw;
	}
	
	public void setLowestdraw(java.lang.Integer value) {
		this.lowestdraw = value;
	}
	
	public java.lang.Integer getLowestdraw() {
		return this.lowestdraw;
	}
	
	public void setHighestdraw(java.lang.Integer value) {
		this.highestdraw = value;
	}
	
	public java.lang.Integer getHighestdraw() {
		return this.highestdraw;
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
		return new ToStringBuilder(this)
			.append("Ssid",getSsid())
			.append("Sitename",getSitename())
			.append("Siteurl",getSiteurl())
			.append("Spreadkey",getSpreadkey())
			.append("Lockedcount",getLockedcount())
			.append("Isclosed",getIsclosed())
			.append("Isregister",getIsregister())
			.append("Islogin",getIslogin())
			.append("Isrecharge",getIsrecharge())
			.append("Isdraw",getIsdraw())
			.append("Lowestdraw",getLowestdraw())
			.append("Highestdraw",getHighestdraw())
			.append("CreateDate",getCreateDate())
			.append("UpdateDate",getUpdateDate())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getSsid())
			.append(getSitename())
			.append(getSiteurl())
			.append(getSpreadkey())
			.append(getLockedcount())
			.append(getIsclosed())
			.append(getIsregister())
			.append(getIslogin())
			.append(getIsrecharge())
			.append(getIsdraw())
			.append(getLowestdraw())
			.append(getHighestdraw())
			.append(getCreateDate())
			.append(getUpdateDate())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof SiteSettings == false) return false;
		if(this == obj) return true;
		SiteSettings other = (SiteSettings)obj;
		return new EqualsBuilder()
			.append(getSsid(),other.getSsid())
			.append(getSitename(),other.getSitename())
			.append(getSiteurl(),other.getSiteurl())
			.append(getSpreadkey(),other.getSpreadkey())
			.append(getLockedcount(),other.getLockedcount())
			.append(getIsclosed(),other.getIsclosed())
			.append(getIsregister(),other.getIsregister())
			.append(getIslogin(),other.getIslogin())
			.append(getIsrecharge(),other.getIsrecharge())
			.append(getIsdraw(),other.getIsdraw())
			.append(getLowestdraw(),other.getLowestdraw())
			.append(getHighestdraw(),other.getHighestdraw())
			.append(getCreateDate(),other.getCreateDate())
			.append(getUpdateDate(),other.getUpdateDate())
			.isEquals();
	}
	



	@Override
	public Serializable getID() {

		return  this.ssid;
	}
}

