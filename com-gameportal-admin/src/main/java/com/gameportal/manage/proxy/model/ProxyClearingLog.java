package com.gameportal.manage.proxy.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * 代理结算记录
 * @author Administrator
 *
 */
public class ProxyClearingLog extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4357424183822303967L;
	
	/**
	 * 记录ID
	 */
	private Integer clearingid;
	
	/**
	 * 对应用户ID
	 */
	private Integer uiid;
	
	/**
	 * 结算金额=实际盈亏*结算比例
	 */
	private String clearingAmount;
	
	/**
	 * 结算比例
	 */
	private String clearingScale;
	
	/**
	 * 盈亏
	 */
	private String finalamountTotal;
	
	/**
	 * 总有效投注
	 */
	private String validBetAmountTotal;
	
	/**
	 * 下线会员本月总洗码
	 */
	private String ximaAmount;
	
	/**
	 * 下线会员本月总优惠
	 */
	private String preferentialTotal;
	
	/**
	 * 实际盈亏=结算总盈亏-(本月总洗码+本月总优惠)
	 */
	private String realPL;
	
	/**
	 * 结算类型
	 * 0：输值结算按月
	 * 1：按月洗码
	 * 2：按天洗码
	 */
	private int clearingType;
	
	/**
	 * 入账状态
	 * 0：未入账
	 * 1：已入账
	 * 2:记录，用于累计代理盈亏
	 * 3:撤除记录
	 * 4:未分配，表现给代理洗码后，需要代理分配给下线
	 * 5:已分配，可以入账
	 * 6:洗码记录
	 */
	private int clearingStatus;
	
	/**
	 * 结算日期
	 */
	private String clearingTime;
	
	/**
	 * 结算注单开始日期
	 */
	private String clearingStartTime;
	
	/**
	 * 结算注单结束日期
	 */
	private String clearingEndTime;
	
	/**
	 * 备注
	 */
	private String clearingRemark;
	
	/**
	 * 入账操作人
	 */
	private String upuser;
	
	/**
	 * 入账时间
	 */
	private String uptime;
	
	/**
	 * 入账客户端IP
	 */
	private String upclient;
	
	//附加属性
	/**
	 * 代理账号
	 */
	private String account;
	
	/**
	 * 代理姓名
	 */
	private String uname;
	
	
	public Integer getClearingid() {
		return clearingid;
	}

	public void setClearingid(Integer clearingid) {
		this.clearingid = clearingid;
	}

	public Integer getUiid() {
		return uiid;
	}

	public void setUiid(Integer uiid) {
		this.uiid = uiid;
	}

	public String getClearingAmount() {
		return clearingAmount;
	}

	public void setClearingAmount(String clearingAmount) {
		this.clearingAmount = clearingAmount;
	}

	public String getClearingScale() {
		return clearingScale;
	}

	public void setClearingScale(String clearingScale) {
		this.clearingScale = clearingScale;
	}

	public String getFinalamountTotal() {
		return finalamountTotal;
	}

	public void setFinalamountTotal(String finalamountTotal) {
		this.finalamountTotal = finalamountTotal;
	}

	public String getValidBetAmountTotal() {
		return validBetAmountTotal;
	}

	public void setValidBetAmountTotal(String validBetAmountTotal) {
		this.validBetAmountTotal = validBetAmountTotal;
	}

	public String getXimaAmount() {
		return ximaAmount;
	}

	public void setXimaAmount(String ximaAmount) {
		this.ximaAmount = ximaAmount;
	}

	public String getPreferentialTotal() {
		return preferentialTotal;
	}

	public void setPreferentialTotal(String preferentialTotal) {
		this.preferentialTotal = preferentialTotal;
	}

	public String getRealPL() {
		return realPL;
	}

	public void setRealPL(String realPL) {
		this.realPL = realPL;
	}
	
	

	public String getUpuser() {
		return upuser;
	}

	public void setUpuser(String upuser) {
		this.upuser = upuser;
	}

	public String getUptime() {
		return uptime;
	}

	public void setUptime(String uptime) {
		this.uptime = uptime;
	}

	public String getUpclient() {
		return upclient;
	}

	public void setUpclient(String upclient) {
		this.upclient = upclient;
	}

	public int getClearingType() {
		return clearingType;
	}

	public void setClearingType(int clearingType) {
		this.clearingType = clearingType;
	}

	public int getClearingStatus() {
		return clearingStatus;
	}

	public void setClearingStatus(int clearingStatus) {
		this.clearingStatus = clearingStatus;
	}

	public String getClearingTime() {
		return clearingTime;
	}

	public void setClearingTime(String clearingTime) {
		this.clearingTime = clearingTime;
	}

	public String getClearingStartTime() {
		return clearingStartTime;
	}

	public void setClearingStartTime(String clearingStartTime) {
		this.clearingStartTime = clearingStartTime;
	}

	public String getClearingEndTime() {
		return clearingEndTime;
	}

	public void setClearingEndTime(String clearingEndTime) {
		this.clearingEndTime = clearingEndTime;
	}

	public String getClearingRemark() {
		return clearingRemark;
	}

	public void setClearingRemark(String clearingRemark) {
		this.clearingRemark = clearingRemark;
	}


	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getUname() {
		return uname;
	}



	public void setUname(String uname) {
		this.uname = uname;
	}



	@Override
	public String toString() {
		return "ProxyClearingLog [clearingid=" + clearingid + ", uiid=" + uiid
				+ ", clearingAmount=" + clearingAmount + ", clearingScale="
				+ clearingScale + ", finalamountTotal=" + finalamountTotal
				+ ", validBetAmountTotal=" + validBetAmountTotal
				+ ", ximaAmount=" + ximaAmount + ", preferentialTotal="
				+ preferentialTotal + ", realPL=" + realPL + ", clearingType="
				+ clearingType + ", clearingStatus=" + clearingStatus
				+ ", clearingTime=" + clearingTime + ", clearingStartTime="
				+ clearingStartTime + ", clearingEndTime=" + clearingEndTime
				+ ", clearingRemark=" + clearingRemark + "]";
	}



	@Override
	public Serializable getID() {
		
		return this.getClearingid();
	}

}
