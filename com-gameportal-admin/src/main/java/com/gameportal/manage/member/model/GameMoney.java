package com.gameportal.manage.member.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * 第三方游戏余额对象
 * @author Administrator
 *
 */
public class GameMoney extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6018431635798235L;
	
	/**
	 * 平台ID
	 */
	private Integer gpid;
	
	/**
	 * 平台名称
	 */
	private String gpname;
	
	/**
	 * 明天余额
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

	@Override
	public String toString() {
		return "GameMoney [gpid=" + gpid + ", gpname=" + gpname + ", money="
				+ money + "]";
	}

	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return this.getGpid();
	}

}
