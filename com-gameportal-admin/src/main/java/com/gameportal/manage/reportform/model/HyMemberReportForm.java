package com.gameportal.manage.reportform.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * 活跃会员统计报表
 * @author Administrator
 *
 */
public class HyMemberReportForm extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5838494071678475400L;

	/**
	 * 日期
	 */
	private String time;
	
	/**
	 * 登录人数
	 */
	private String logincount;
	
	/**
	 * 充值人数
	 */
	private String paycount;
	
	
	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public String getLogincount() {
		return logincount;
	}


	public void setLogincount(String logincount) {
		this.logincount = logincount;
	}


	public String getPaycount() {
		return paycount;
	}


	public void setPaycount(String paycount) {
		this.paycount = paycount;
	}


	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return null;
	}

}
