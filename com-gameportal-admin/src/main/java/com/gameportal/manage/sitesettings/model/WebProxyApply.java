package com.gameportal.manage.sitesettings.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * 代理申请实体
 * @author Administrator
 *
 */
public class WebProxyApply extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6873832147429866542L;

	/**
	 * 主键
	 */
	private Integer aid;
	
	/**
	 * 真实名字
	 */
	private String truename;
	
	/**
	 * QQ AND skype
	 */
	private String qqskype;
	
	/**
	 * E-mail
	 */
	private String aemail;
	
	/**
	 * 推广方式
	 */
	private String spreadfs;
	
	/**
	 * 申请来源
	 */
	private String applysource;
	
	/**
	 * 申请时间
	 */
	private String applytime;
	
	/**
	 * 状态
	 * (0：待审核 1:审核通过 2：审核不通过)
	 */
	private int status;
	
	/**
	 * 备注
	 */
	private String description;

	public Integer getAid() {
		return aid;
	}
	
	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}

	public String getQqskype() {
		return qqskype;
	}

	public void setQqskype(String qqskype) {
		this.qqskype = qqskype;
	}

	public String getAemail() {
		return aemail;
	}

	public void setAemail(String aemail) {
		this.aemail = aemail;
	}

	public String getSpreadfs() {
		return spreadfs;
	}

	public void setSpreadfs(String spreadfs) {
		this.spreadfs = spreadfs;
	}

	public String getApplysource() {
		return applysource;
	}

	public void setApplysource(String applysource) {
		this.applysource = applysource;
	}

	public String getApplytime() {
		return applytime;
	}

	public void setApplytime(String applytime) {
		this.applytime = applytime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	@Override
	public String toString() {
		return "WebProxyApply [aid=" + aid + ", truename=" + truename
				+ ", qqskype=" + qqskype + ", aemail=" + aemail + ", spreadfs="
				+ spreadfs + ", applysource=" + applysource + ", applytime="
				+ applytime + ", status=" + status + ", description="
				+ description + "]";
	}

	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return this.getAid();
	}

}
