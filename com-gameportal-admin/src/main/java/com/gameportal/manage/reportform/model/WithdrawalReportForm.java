package com.gameportal.manage.reportform.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * 取款报表
 * @author Administrator
 *
 */
public class WithdrawalReportForm extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3806247096021657208L;
	
	/**
	 * 提款时间
	 */
	private String times;
	
	/**
	 * 提款金额
	 */
	private String withdrawalTotal;
	
	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getWithdrawalTotal() {
		return withdrawalTotal;
	}

	public void setWithdrawalTotal(String withdrawalTotal) {
		this.withdrawalTotal = withdrawalTotal;
	}
	

	@Override
	public String toString() {
		return "WithdrawalReportForm [times=" + times + ", withdrawalTotal="
				+ withdrawalTotal + "]";
	}

	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return null;
	}

}
