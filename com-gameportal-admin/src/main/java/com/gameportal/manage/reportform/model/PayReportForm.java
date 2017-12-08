package com.gameportal.manage.reportform.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;


/**
 * 充值统计
 * @author Administrator
 *
 */
public class PayReportForm extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8722919377842133951L;
	
	/**
	 * 日期
	 */
	private String times;
	
	/**
	 * 充值时间
	 */
	private String paymoney;
	
	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getPaymoney() {
		return paymoney;
	}

	public void setPaymoney(String paymoney) {
		this.paymoney = paymoney;
	}
	

	@Override
	public String toString() {
		return "PayReportForm [times=" + times + ", paymoney=" + paymoney + "]";
	}

	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return null;
	}
}
