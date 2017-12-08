package com.gameportal.manage.proxy.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

public class ProxyClearingLogTotal extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5070083272089047115L;

	/**
	 * 结算金额
	 */
	private java.lang.String clearingAmounts;
	
	/**
	 * 盈利
	 */
	private java.lang.String finalamountTotals;
	
	/**
	 * 投注总额
	 */
	private java.lang.String validBetAmountTotals;
	
	/**
	 * 洗码总额
	 */
	private java.lang.String ximaAmounts;
	
	/**
	 * 优惠总额
	 */
	private java.lang.String preferentialTotals;
	
	/**
	 * 实际盈亏
	 */
	private java.lang.String realPLs;
	
	public ProxyClearingLogTotal() {
	}
	
	@Override
	public Serializable getID() {
		return null;
	}

	public java.lang.String getClearingAmounts() {
		return clearingAmounts;
	}

	public void setClearingAmounts(java.lang.String clearingAmounts) {
		this.clearingAmounts = clearingAmounts;
	}

	public java.lang.String getFinalamountTotals() {
		return finalamountTotals;
	}

	public void setFinalamountTotals(java.lang.String finalamountTotals) {
		this.finalamountTotals = finalamountTotals;
	}

	public java.lang.String getValidBetAmountTotals() {
		return validBetAmountTotals;
	}

	public void setValidBetAmountTotals(java.lang.String validBetAmountTotals) {
		this.validBetAmountTotals = validBetAmountTotals;
	}

	public java.lang.String getXimaAmounts() {
		return ximaAmounts;
	}

	public void setXimaAmounts(java.lang.String ximaAmounts) {
		this.ximaAmounts = ximaAmounts;
	}

	public java.lang.String getPreferentialTotals() {
		return preferentialTotals;
	}

	public void setPreferentialTotals(java.lang.String preferentialTotals) {
		this.preferentialTotals = preferentialTotals;
	}

	public java.lang.String getRealPLs() {
		return realPLs;
	}

	public void setRealPLs(java.lang.String realPLs) {
		this.realPLs = realPLs;
	}
	
}
