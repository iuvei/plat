package com.gameportal.manage.reportform.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * 投注报表
 * @author Administrator
 *
 */
public class BetReportForm extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4737190172127167453L;
	
	private String platformcode;
	private String times;
	private String betmoney;

	public String getPlatformcode() {
		return platformcode;
	}

	public void setPlatformcode(String platformcode) {
		this.platformcode = platformcode;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getBetmoney() {
		return betmoney;
	}

	public void setBetmoney(String betmoney) {
		this.betmoney = betmoney;
	}

	@Override
	public String toString() {
		return "BetReportForm [platformcode=" + platformcode + ", times="
				+ times + ", betmoney=" + betmoney + "]";
	}

	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
