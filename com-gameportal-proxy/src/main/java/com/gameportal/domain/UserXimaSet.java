package com.gameportal.domain;

/**
 * 会员洗码设置
 * @author leron
 *
 */
public class UserXimaSet extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4636240365596179413L;

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
}
