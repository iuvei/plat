package com.gameportal.manage.reportform.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * 订单管理统计
 * @author Administrator
 *
 */
public class OrderManageReportForm extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1213294042643989747L;

	@Override
	public Serializable getID() {
		return null;
	}
	
	/**
	 * 订单号
	 */
	private java.lang.String poid;
	/**
	 * 会员账号
	 */
	private java.lang.String uaccount;
	/**
	 * 会员名称
	 */
	private java.lang.String urealname;
	/**
	 * 交易流水号
	 */
	private java.lang.String platformorders;
	/**
	 * 0:存款
	 * 1：提款
	 * 2：赠送
	 * 3：扣款
	 */
	private java.lang.Integer paytyple;
	/**
	 * 订单类型
	 * 根据system_fields字典表延伸
	 */
	private java.lang.Integer ordertype;
	/**
	 * 0:公司入款
	 * 1：第三方支付
	 */
	private java.lang.Integer paymethods;
	/**
	 * 处理时间
	 */
	private String deposittime;
	/**
	 * 金额
	 */
	private java.lang.String amount;
	/**
	 * 第三方状态
	 */
	private java.lang.Integer paystatus;
	/**
	 * 0:作废 
	 * 1: 发起（待财务审核） 
	 * 2: 处理中（待客服核实） 
	 * 3: 存取成功 
	 * 4: 存取失败
	 */
	private java.lang.Integer status;
	/**
	 * 客服字段
	 */
	private java.lang.String kfremarks;
	private java.lang.Long kfid;
	private java.lang.String kfname;
	private String kfopttime;
	/**
	 * 财务字段
	 */
	private java.lang.String cwremarks;
	private java.lang.Long cwid;
	private java.lang.String cwname;
	private String cwopttime;
	/**
	 * 操作前用户钱包余额
	 */
	private java.lang.String beforebalance; 
	/**
	 * 操作后用户钱包余额
	 */
	private java.lang.String laterbalance;
	
	/**
	 * 官方洗码金额
	 */
	private java.lang.String memberximaMoney;
	
	/**
	 * 代理洗码金额
	 */
	private java.lang.String proxyclearMoney;
	/**
	 * 代理给下线洗码金额
	 */
	private java.lang.String proxyuserximaMoney;
	
	/**报表所需字段 START*/
	private java.lang.String paytyplename;
	private java.lang.String ordertypename;
	private java.lang.String paymethodsname;
	private java.lang.String statusname;
	/**报表所需字段END */
	
	public java.lang.String getPoid() {
		return poid;
	}
	public java.lang.String getPaytyplename() {
		return paytyplename;
	}
	public void setPaytyplename(java.lang.String paytyplename) {
		this.paytyplename = paytyplename;
	}
	public java.lang.String getOrdertypename() {
		return ordertypename;
	}
	public void setOrdertypename(java.lang.String ordertypename) {
		this.ordertypename = ordertypename;
	}
	public java.lang.String getPaymethodsname() {
		return paymethodsname;
	}
	public void setPaymethodsname(java.lang.String paymethodsname) {
		this.paymethodsname = paymethodsname;
	}
	public java.lang.String getStatusname() {
		return statusname;
	}
	public void setStatusname(java.lang.String statusname) {
		this.statusname = statusname;
	}
	public void setPoid(java.lang.String poid) {
		this.poid = poid;
	}
	public java.lang.String getUaccount() {
		return uaccount;
	}
	public void setUaccount(java.lang.String uaccount) {
		this.uaccount = uaccount;
	}
	public java.lang.String getUrealname() {
		return urealname;
	}
	public void setUrealname(java.lang.String urealname) {
		this.urealname = urealname;
	}
	public java.lang.String getPlatformorders() {
		return platformorders;
	}
	public void setPlatformorders(java.lang.String platformorders) {
		this.platformorders = platformorders;
	}
	public java.lang.Integer getPaytyple() {
		return paytyple;
	}
	public void setPaytyple(java.lang.Integer paytyple) {
		this.paytyple = paytyple;
	}
	public java.lang.Integer getOrdertype() {
		return ordertype;
	}
	public void setOrdertype(java.lang.Integer ordertype) {
		this.ordertype = ordertype;
	}
	public java.lang.Integer getPaymethods() {
		return paymethods;
	}
	public void setPaymethods(java.lang.Integer paymethods) {
		this.paymethods = paymethods;
	}
	public String getDeposittime() {
		return deposittime;
	}
	public void setDeposittime(String deposittime) {
		this.deposittime = deposittime;
	}
	public java.lang.String getAmount() {
		return amount;
	}
	public void setAmount(java.lang.String amount) {
		this.amount = amount;
	}
	public java.lang.Integer getStatus() {
		return status;
	}
	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}
	public java.lang.String getKfremarks() {
		return kfremarks;
	}
	public void setKfremarks(java.lang.String kfremarks) {
		this.kfremarks = kfremarks;
	}
	public java.lang.Long getKfid() {
		return kfid;
	}
	public void setKfid(java.lang.Long kfid) {
		this.kfid = kfid;
	}
	public java.lang.String getKfname() {
		return kfname;
	}
	public void setKfname(java.lang.String kfname) {
		this.kfname = kfname;
	}
	public String getKfopttime() {
		return kfopttime;
	}
	public void setKfopttime(String kfopttime) {
		this.kfopttime = kfopttime;
	}
	public java.lang.String getCwremarks() {
		return cwremarks;
	}
	public void setCwremarks(java.lang.String cwremarks) {
		this.cwremarks = cwremarks;
	}
	public java.lang.Long getCwid() {
		return cwid;
	}
	public void setCwid(java.lang.Long cwid) {
		this.cwid = cwid;
	}
	public java.lang.String getCwname() {
		return cwname;
	}
	public void setCwname(java.lang.String cwname) {
		this.cwname = cwname;
	}
	public String getCwopttime() {
		return cwopttime;
	}
	public void setCwopttime(String cwopttime) {
		this.cwopttime = cwopttime;
	}
	public java.lang.String getBeforebalance() {
		return beforebalance;
	}
	public void setBeforebalance(java.lang.String beforebalance) {
		this.beforebalance = beforebalance;
	}
	public java.lang.String getLaterbalance() {
		return laterbalance;
	}
	public void setLaterbalance(java.lang.String laterbalance) {
		this.laterbalance = laterbalance;
	}
	public java.lang.Integer getPaystatus() {
		return paystatus;
	}
	public void setPaystatus(java.lang.Integer paystatus) {
		this.paystatus = paystatus;
	}
	public java.lang.String getMemberximaMoney() {
		return memberximaMoney;
	}
	public void setMemberximaMoney(java.lang.String memberximaMoney) {
		this.memberximaMoney = memberximaMoney;
	}
	public java.lang.String getProxyclearMoney() {
		return proxyclearMoney;
	}
	public void setProxyclearMoney(java.lang.String proxyclearMoney) {
		this.proxyclearMoney = proxyclearMoney;
	}
	public java.lang.String getProxyuserximaMoney() {
		return proxyuserximaMoney;
	}
	public void setProxyuserximaMoney(java.lang.String proxyuserximaMoney) {
		this.proxyuserximaMoney = proxyuserximaMoney;
	}

}
