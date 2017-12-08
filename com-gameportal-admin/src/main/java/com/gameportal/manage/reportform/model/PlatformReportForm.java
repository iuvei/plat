package com.gameportal.manage.reportform.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * 平台报表
 * @author Administrator
 *
 */
public class PlatformReportForm extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7153061548109720430L;
	
	/**
	 * 平台总余额
	 */
	private String totalMoney;

	
	public String getTotalMoney() {
		return totalMoney;
	}


	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	@Override
	public String toString() {
		return "PlatformReportForm [totalMoney=" + totalMoney + "]";
	}


	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return null;
	}
}
