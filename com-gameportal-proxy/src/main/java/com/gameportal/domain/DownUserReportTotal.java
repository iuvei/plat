package com.gameportal.domain;

/**
 * 下线会员本月报表总计
 * @author leron
 *
 */
public class DownUserReportTotal extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1843907110261598987L;
	
	/**
	 * 钱包余额
	 */
	private java.lang.String moneyTotal;
	
	/**
	 * 总存款
	 */
	private java.lang.String depositTotal;
	
	/**
	 * 总提款
	 */
	private java.lang.String withdrawalTotal;
	
	/**
	 * 总优惠
	 */
	private java.lang.String preferentialTotal;
	
	
	/**
	 * 总洗码
	 */
	private java.lang.String ximaTotal;
	
	/**
	 * 总输赢
	 */
	private java.lang.String winLossTotal;

	public java.lang.String getMoneyTotal() {
		return moneyTotal;
	}

	public void setMoneyTotal(java.lang.String moneyTotal) {
		this.moneyTotal = moneyTotal;
	}

	public java.lang.String getDepositTotal() {
		return depositTotal;
	}

	public void setDepositTotal(java.lang.String depositTotal) {
		this.depositTotal = depositTotal;
	}

	public java.lang.String getWithdrawalTotal() {
		return withdrawalTotal;
	}

	public void setWithdrawalTotal(java.lang.String withdrawalTotal) {
		this.withdrawalTotal = withdrawalTotal;
	}

	public java.lang.String getPreferentialTotal() {
		return preferentialTotal;
	}

	public void setPreferentialTotal(java.lang.String preferentialTotal) {
		this.preferentialTotal = preferentialTotal;
	}

	public java.lang.String getXimaTotal() {
		return ximaTotal;
	}

	public void setXimaTotal(java.lang.String ximaTotal) {
		this.ximaTotal = ximaTotal;
	}

	public java.lang.String getWinLossTotal() {
		return winLossTotal;
	}

	public void setWinLossTotal(java.lang.String winLossTotal) {
		this.winLossTotal = winLossTotal;
	}

}
