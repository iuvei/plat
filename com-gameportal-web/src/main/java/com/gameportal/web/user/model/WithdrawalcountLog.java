package com.gameportal.web.user.model;

import java.io.Serializable;

/**
 * 用户提款次数记录
 * @author Administrator
 *
 */
public class WithdrawalcountLog extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2299913252396463537L;
	
	private Long wid;
	private Long uiid;
	private String daytime;
	private int withdrawalcount;
	
	public Long getWid() {
		return wid;
	}

	public void setWid(Long wid) {
		this.wid = wid;
	}

	public Long getUiid() {
		return uiid;
	}

	public void setUiid(Long uiid) {
		this.uiid = uiid;
	}

	public String getDaytime() {
		return daytime;
	}

	public void setDaytime(String daytime) {
		this.daytime = daytime;
	}

	public int getWithdrawalcount() {
		return withdrawalcount;
	}

	public void setWithdrawalcount(int withdrawalcount) {
		this.withdrawalcount = withdrawalcount;
	}

	@Override
	public String toString() {
		return "WithdrawalcountLog [wid=" + wid + ", uiid=" + uiid
				+ ", daytime=" + daytime + ", withdrawalcount="
				+ withdrawalcount + "]";
	}

	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return this.getWid();
	}

}
