package com.gameportal.manage.system.model;

import java.io.Serializable;

/**
 * 系统用户登录日志
 * @author Administrator
 *
 */
public class SystemUserLoginLog extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1681837367830588239L;

	private Integer logid;
	
	/**
	 * 登录账号
	 */
	private String loginaccount;
	
	/**
	 * 登录姓名
	 */
	private String loginname;
	
	/**
	 * 登录角色
	 */
	private String loginrole;
	
	/**
	 * 登录IP
	 */
	private String loginip;
	
	/**
	 * 登录客户端MAC
	 */
	private String loginmac;
	
	/**
	 * 登录时间
	 */
	private String logintime;
	
	/**
	 * 登录设备
	 */
	private String loginsource;
	
	
	public Integer getLogid() {
		return logid;
	}


	public void setLogid(Integer logid) {
		this.logid = logid;
	}


	public String getLoginaccount() {
		return loginaccount;
	}


	public void setLoginaccount(String loginaccount) {
		this.loginaccount = loginaccount;
	}


	public String getLoginname() {
		return loginname;
	}


	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}


	public String getLoginrole() {
		return loginrole;
	}


	public void setLoginrole(String loginrole) {
		this.loginrole = loginrole;
	}


	public String getLoginip() {
		return loginip;
	}


	public void setLoginip(String loginip) {
		this.loginip = loginip;
	}


	public String getLoginmac() {
		return loginmac;
	}


	public void setLoginmac(String loginmac) {
		this.loginmac = loginmac;
	}


	public String getLogintime() {
		return logintime;
	}


	public void setLogintime(String logintime) {
		this.logintime = logintime;
	}


	public String getLoginsource() {
		return loginsource;
	}


	public void setLoginsource(String loginsource) {
		this.loginsource = loginsource;
	}


	@Override
	public String toString() {
		return "SystemUserLoginLog [logid=" + logid + ", loginaccount="
				+ loginaccount + ", loginname=" + loginname + ", loginrole="
				+ loginrole + ", loginip=" + loginip + ", loginmac=" + loginmac
				+ ", logintime=" + logintime + ", loginsource=" + loginsource
				+ "]";
	}


	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return this.getLogid();
	}

}
