package com.gameportal.web.user.model;

import java.io.Serializable;

/**
 * 单个用户洗码比例设置
 * @author Administrator
 *
 */
public class UserXimaSet extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4507496330937903491L;
	
	private Integer xmid;
	/**
	 * 对应用户ID
	 */
	private int uiid;
	
	/**
	 * 对应代理ID
	 */
	private int proxyid;
	
	/**
	 * 洗码比例
	 */
	private String ximascale;
	private int status;
	private String settime;

	/**
	 * 代理账号
	 */
	private String account;
	
	/**
	 * 代理名字
	 */
	private String uname;
	
	public Integer getXmid() {
		return xmid;
	}


	public void setXmid(Integer xmid) {
		this.xmid = xmid;
	}


	public int getUiid() {
		return uiid;
	}


	public void setUiid(int uiid) {
		this.uiid = uiid;
	}


	public int getProxyid() {
		return proxyid;
	}


	public void setProxyid(int proxyid) {
		this.proxyid = proxyid;
	}


	public String getXimascale() {
		return ximascale;
	}


	public void setXimascale(String ximascale) {
		this.ximascale = ximascale;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	public String getSettime() {
		return settime;
	}


	public void setSettime(String settime) {
		this.settime = settime;
	}

	public String getAccount() {
		return account;
	}


	public void setAccount(String account) {
		this.account = account;
	}


	public String getUname() {
		return uname;
	}


	public void setUname(String uname) {
		this.uname = uname;
	}


	@Override
	public String toString() {
		return "UserXimaSet [xmid=" + xmid + ", uiid=" + uiid + ", proxyid="
				+ proxyid + ", ximascale=" + ximascale + ", status=" + status
				+ ", settime=" + settime + "]";
	}


	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return this.getXmid();
	}

}
