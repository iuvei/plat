package com.gameportal.web.user.model;

import java.io.Serializable;

/**
 * 用户登录日志
 * @author Administrator
 *
 */
public class UserLoginLog extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7933484107024493363L;
	
	private Long logid;
	private Integer uiid;
	private String account;
	private String truename;
	private String logintime;
	private String loginip;
	private String loginsource;
	private String logindevice;
	private String iparea;
	
	public Long getLogid() {
		return logid;
	}

	public void setLogid(Long logid) {
		this.logid = logid;
	}

	public Integer getUiid() {
		return uiid;
	}

	public void setUiid(Integer uiid) {
		this.uiid = uiid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}

	public String getLogintime() {
		return logintime;
	}

	public void setLogintime(String logintime) {
		this.logintime = logintime;
	}

	public String getLoginip() {
		return loginip;
	}

	public void setLoginip(String loginip) {
		this.loginip = loginip;
	}

	public String getLoginsource() {
		return loginsource;
	}

	public void setLoginsource(String loginsource) {
		this.loginsource = loginsource;
	}
	

	public String getLogindevice() {
		return logindevice;
	}

	public void setLogindevice(String logindevice) {
		this.logindevice = logindevice;
	}
	
	public String getIparea() {
		return iparea;
	}

	public void setIparea(String iparea) {
		this.iparea = iparea;
	}

	@Override
	public String toString() {
		return "UserLoginLog [logid=" + logid + ", uiid=" + uiid + ", account="
				+ account + ", truename=" + truename + ", logintime="
				+ logintime + ", loginip=" + loginip + ", loginsource="
				+ loginsource + "]";
	}



	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return this.getLogid();
	}

}
