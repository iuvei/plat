package com.gameportal.domain;


/**
 * 游戏余额
 * @author leron
 *
 */
public class GameMoney extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3218891759110322753L;
	
	/**
	 * 平台ID
	 */
	private Integer gpid;
	
	/**
	 * 平台名称
	 */
	private String gpname;
	
	/**
	 * 平台余额
	 */
	private String money;

	public Integer getGpid() {
		return gpid;
	}

	public void setGpid(Integer gpid) {
		this.gpid = gpid;
	}

	public String getGpname() {
		return gpname;
	}

	public void setGpname(String gpname) {
		this.gpname = gpname;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}
}
