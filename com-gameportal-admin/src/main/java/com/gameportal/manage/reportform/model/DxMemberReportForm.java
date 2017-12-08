package com.gameportal.manage.reportform.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * 电销客户分析
 * @author Administrator
 *
 */
public class DxMemberReportForm extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4805958386101325575L;

	@Override
	public Serializable getID() {
		return null;
	}
	
	/**
	 * 会员账号
	 */
	private java.lang.String account;
	/**
	 * 会员名称
	 */
	private java.lang.String uname;
	
	/**
	 * 电销名称
	 */
	private java.lang.String truename;
	
	/**
	 * 存款金额
	 */
	private java.lang.String depositorderMoney;
	
	/**
	 * 提款金额
	 */
	private java.lang.String pickUpMoney;
	
	/**
	 * 输赢
	 */
	private java.lang.String finalMoney;
	
	/**
	 * 有效投注额
	 */
	private java.lang.String validBetMoney;

	public java.lang.String getDepositorderMoney() {
		return depositorderMoney;
	}

	public void setDepositorderMoney(java.lang.String depositorderMoney) {
		this.depositorderMoney = depositorderMoney;
	}

	public java.lang.String getPickUpMoney() {
		return pickUpMoney;
	}

	public void setPickUpMoney(java.lang.String pickUpMoney) {
		this.pickUpMoney = pickUpMoney;
	}

	public java.lang.String getFinalMoney() {
		return finalMoney;
	}

	public void setFinalMoney(java.lang.String finalMoney) {
		this.finalMoney = finalMoney;
	}

	public java.lang.String getAccount() {
		return account;
	}

	public void setAccount(java.lang.String account) {
		this.account = account;
	}

	public java.lang.String getUname() {
		return uname;
	}

	public void setUname(java.lang.String uname) {
		this.uname = uname;
	}

	public java.lang.String getTruename() {
		return truename;
	}

	public void setTruename(java.lang.String truename) {
		this.truename = truename;
	}

	public java.lang.String getValidBetMoney() {
		return validBetMoney;
	}

	public void setValidBetMoney(java.lang.String validBetMoney) {
		this.validBetMoney = validBetMoney;
	}
	
	
}
