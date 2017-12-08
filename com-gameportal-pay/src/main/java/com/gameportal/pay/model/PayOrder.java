package com.gameportal.pay.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class PayOrder extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8419707511082888706L;
	// alias
	public static final String TABLE_ALIAS = "PayOrder";
	public static final String ALIAS_POID = "订单ID";
	public static final String ALIAS_PLATFORMORDERS = "platformorders";
	public static final String ALIAS_UIID = "用户ID";
	public static final String ALIAS_PAYTYPLE = "收支类型 0 充值 1 提现";
	public static final String ALIAS_PPID = "支付平台信息ID";
	public static final String ALIAS_PAYMETHODS = "支付方式 0 ATM  1 网上支付";
	public static final String ALIAS_BANKNAME = "银行名称";
	public static final String ALIAS_BANKCARD = "银行卡号";
	public static final String ALIAS_OPENNAME = "开户姓名";
	public static final String ALIAS_DEPOSIT = "存款分行";
	public static final String ALIAS_DEPOSITTIME = "存款时间";
	public static final String ALIAS_AMOUNT = "金额";
	public static final String ALIAS_PAYSTATUS = "paystatus";
	public static final String ALIAS_STATUS = "状态 0 作废 1 发起 2 处理中 3 支付成功 4 支付失败";
	public static final String ALIAS_REMARKS = "备注";
	public static final String ALIAS_ORDERCONTENT = "订单明细";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_DATE = "更新时间";

	// columns START
	private java.lang.String poid;
	private java.lang.String platformorders;
	private java.lang.Long uiid;
	private java.lang.Integer paytyple;
	private java.lang.Long ppid;
	private java.lang.Integer paymethods;
	private java.lang.String bankname;
	private java.lang.String bankcard;
	private java.lang.String openname;
	private java.lang.String deposit;
	private java.lang.String uaccount;
	private java.lang.String urealname;
	private java.util.Date deposittime;
	private BigDecimal amount;
	private java.lang.Integer paystatus;
	private java.lang.Integer status;
	private java.lang.String remarks;
	private java.lang.String ordercontent;
	private java.util.Date createDate;
	private java.util.Date updateDate;
	private String kfremarks;
	private String cwremarks;
	
	/**
	 * 操作前余额
	 */
	private String beforebalance; 
	/**
	 * 操作后余额
	 */
	private String laterbalance;
	
	/**
	 * 订单类型
	 * 根据system_fields字典表延伸
	 */
	private java.lang.Integer ordertype = 0;
	
	/**
	 * 活动编号
	 * 不为空就表示用户选择参加了活动
	 * 对应a_activity表
	 */
	private String hdnumber;
	
	// 所属代理
	private String proxyName;
	
	// columns END

	public PayOrder() {
	}

	public PayOrder(java.lang.String poid) {
		this.poid = poid;
	}

	public void setPoid(java.lang.String value) {
		this.poid = value;
	}

	public java.lang.String getPoid() {
		return this.poid;
	}

	public void setPlatformorders(java.lang.String value) {
		this.platformorders = value;
	}

	public java.lang.String getPlatformorders() {
		return this.platformorders;
	}

	public void setUiid(java.lang.Long value) {
		this.uiid = value;
	}

	public java.lang.Long getUiid() {
		return this.uiid;
	}

	public void setPaytyple(java.lang.Integer value) {
		this.paytyple = value;
	}

	public java.lang.Integer getPaytyple() {
		return this.paytyple;
	}

	public void setPpid(java.lang.Long value) {
		this.ppid = value;
	}

	public java.lang.Long getPpid() {
		return this.ppid;
	}

	public void setPaymethods(java.lang.Integer value) {
		this.paymethods = value;
	}

	public java.lang.Integer getPaymethods() {
		return this.paymethods;
	}

	public void setBankname(java.lang.String value) {
		this.bankname = value;
	}

	public java.lang.String getBankname() {
		return this.bankname;
	}

	public void setBankcard(java.lang.String value) {
		this.bankcard = value;
	}

	public java.lang.String getBankcard() {
		return this.bankcard;
	}

	public void setOpenname(java.lang.String value) {
		this.openname = value;
	}

	public java.lang.String getOpenname() {
		return this.openname;
	}

	public void setDeposit(java.lang.String value) {
		this.deposit = value;
	}

	public java.lang.String getDeposit() {
		return this.deposit;
	}

	public void setDeposittime(java.util.Date value) {
		this.deposittime = value;
	}

	public java.util.Date getDeposittime() {
		return this.deposittime;
	}

	public void setAmount(BigDecimal value) {
		this.amount = value;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setPaystatus(java.lang.Integer value) {
		this.paystatus = value;
	}

	public java.lang.Integer getPaystatus() {
		return this.paystatus;
	}

	public void setStatus(java.lang.Integer value) {
		this.status = value;
	}

	public java.lang.Integer getStatus() {
		return this.status;
	}

	public void setRemarks(java.lang.String value) {
		this.remarks = value;
	}

	public java.lang.String getRemarks() {
		return this.remarks;
	}

	public void setOrdercontent(java.lang.String value) {
		this.ordercontent = value;
	}

	public java.lang.String getOrdercontent() {
		return this.ordercontent;
	}

	public void setCreateDate(java.util.Date value) {
		this.createDate = value;
	}

	public java.util.Date getCreateDate() {
		return this.createDate;
	}

	public void setUpdateDate(java.util.Date value) {
		this.updateDate = value;
	}

	public java.util.Date getUpdateDate() {
		return this.updateDate;
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
	
	public String getBeforebalance() {
		return beforebalance;
	}

	public void setBeforebalance(String beforebalance) {
		this.beforebalance = beforebalance;
	}

	public String getLaterbalance() {
		return laterbalance;
	}

	public void setLaterbalance(String laterbalance) {
		this.laterbalance = laterbalance;
	}

	public String getHdnumber() {
		return hdnumber;
	}

	public void setHdnumber(String hdnumber) {
		this.hdnumber = hdnumber;
	}
	

	public String getKfremarks() {
		return kfremarks;
	}

	public void setKfremarks(String kfremarks) {
		this.kfremarks = kfremarks;
	}

	public String getCwremarks() {
		return cwremarks;
	}

	public void setCwremarks(String cwremarks) {
		this.cwremarks = cwremarks;
	}

	public java.lang.Integer getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(java.lang.Integer ordertype) {
		this.ordertype = ordertype;
	}

	public String getProxyName() {
		return proxyName;
	}

	public void setProxyName(String proxyName) {
		this.proxyName = proxyName;
	}

	public String toString() {
		return new ToStringBuilder(this).append("Poid", getPoid())
				.append("Platformorders", getPlatformorders())
				.append("Uiid", getUiid()).append("Paytyple", getPaytyple())
				.append("Ppid", getPpid())
				.append("Paymethods", getPaymethods())
				.append("Bankname", getBankname())
				.append("Bankcard", getBankcard())
				.append("Openname", getOpenname())
				.append("Deposit", getDeposit())
				.append("Deposittime", getDeposittime())
				.append("Amount", getAmount())
				.append("Paystatus", getPaystatus())
				.append("Status", getStatus()).append("Remarks", getRemarks())
				.append("Ordercontent", getOrdercontent())
				.append("CreateDate", getCreateDate())
				.append("UpdateDate", getUpdateDate()).toString();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getPoid())
				.append(getPlatformorders()).append(getUiid())
				.append(getPaytyple()).append(getPpid())
				.append(getPaymethods()).append(getBankname())
				.append(getBankcard()).append(getOpenname())
				.append(getDeposit()).append(getDeposittime())
				.append(getAmount()).append(getPaystatus()).append(getStatus())
				.append(getRemarks()).append(getOrdercontent())
				.append(getCreateDate()).append(getUpdateDate()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (obj instanceof PayOrder == false)
			return false;
		if (this == obj)
			return true;
		PayOrder other = (PayOrder) obj;
		return new EqualsBuilder().append(getPoid(), other.getPoid())
				.append(getPlatformorders(), other.getPlatformorders())
				.append(getUiid(), other.getUiid())
				.append(getPaytyple(), other.getPaytyple())
				.append(getPpid(), other.getPpid())
				.append(getPaymethods(), other.getPaymethods())
				.append(getBankname(), other.getBankname())
				.append(getBankcard(), other.getBankcard())
				.append(getOpenname(), other.getOpenname())
				.append(getDeposit(), other.getDeposit())
				.append(getDeposittime(), other.getDeposittime())
				.append(getAmount(), other.getAmount())
				.append(getPaystatus(), other.getPaystatus())
				.append(getStatus(), other.getStatus())
				.append(getRemarks(), other.getRemarks())
				.append(getOrdercontent(), other.getOrdercontent())
				.append(getCreateDate(), other.getCreateDate())
				.append(getUpdateDate(), other.getUpdateDate()).isEquals();
	}

	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return this.poid;
	}
}
