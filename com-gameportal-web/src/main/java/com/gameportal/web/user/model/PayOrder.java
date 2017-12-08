package com.gameportal.web.user.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class PayOrder extends BaseEntity {

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
	public static final String ALIAS_PAYSTATUS = "支付状态（0支付中，其它已支付）（第三方支付专用字段）";
	public static final String ALIAS_STATUS = "状态 0 作废 1 发起（待审核） 2 处理中（审核中） 3 存取成功 4 存取失败";
	public static final String ALIAS_REMARKS = "备注";
	public static final String ALIAS_ORDERCONTENT = "订单明细";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_DATE = "更新时间";
	public static final String ALIAS_UACCOUNT = "会员账号";
	public static final String ALIAS_UREALNAME = "会员姓名";
	public static final String ALIAS_KFREMARKS = "客服备注";
	public static final String ALIAS_KFID = "客服ID";
	public static final String ALIAS_KFNAME = "客服名称";
	public static final String ALIAS_KFOPTTIME = "客服操作时间";
	public static final String ALIAS_CWREMARKS = "财务备注";
	public static final String ALIAS_CWID = "财务ID";
	public static final String ALIAS_CWNAME = "财务名称";
	public static final String ALIAS_CWOPTTIME = "财务操作时间";
	public static final String ALIAS_ORDERTYPE = "订单类型";

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
	private Date deposittime;
	private BigDecimal amount;
	private java.lang.Integer paystatus;
	private java.lang.Integer status;
	private java.lang.String remarks;
	private java.lang.String ordercontent;
	private Date create_date;
	private Date update_date;
	private java.lang.String uaccount;
	private java.lang.String urealname;
	private java.lang.String kfremarks;
	private java.lang.Long kfid;
	private java.lang.String kfname;
	private Date kfopttime;
	private java.lang.String cwremarks;
	private java.lang.Long cwid;
	private java.lang.String cwname;
	private Date cwopttime;
	private String startTime;
	private String endTime;
	private Integer ordertype = Integer.valueOf(0);
	
	/**
	 * 操作前余额
	 */
	private BigDecimal beforebalance = BigDecimal.ZERO; 
	
	/**
	 * 操作后余额
	 */
	private BigDecimal laterbalance = BigDecimal.ZERO;
	
	/**
	 * 活动编号
	 * 不为空表示用户参加了优惠活动
	 */
	private String hdnumber;
	
	/**
	 * 公司入款订单号
	 */
	private String ordernumber;
	
	/**
	 * 所属代理
	 */
	private String proxyname;
	
	// columns END

	public PayOrder() {
	}
	
	public String getOrdernumber() {
		return ordernumber;
	}

	public void setOrdernumber(String ordernumber) {
		this.ordernumber = ordernumber;
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

	public void setDeposittime(Date value) {
		this.deposittime = value;
	}

	public Date getDeposittime() {
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

	public Date getCreate_Date() {
		return create_date;
	}

	public void setCreate_Date(Date create_Date) {
		this.create_date = create_Date;
	}

	public Date getUpdate_Date() {
		return update_date;
	}

	public void setUpdate_Date(Date update_Date) {
		this.update_date = update_Date;
	}

	public void setUaccount(java.lang.String value) {
		this.uaccount = value;
	}

	public java.lang.String getUaccount() {
		return this.uaccount;
	}

	public void setUrealname(java.lang.String value) {
		this.urealname = value;
	}

	public java.lang.String getUrealname() {
		return this.urealname;
	}

	public void setKfremarks(java.lang.String value) {
		this.kfremarks = value;
	}

	public java.lang.String getKfremarks() {
		return this.kfremarks;
	}

	public void setKfid(java.lang.Long value) {
		this.kfid = value;
	}

	public java.lang.Long getKfid() {
		return this.kfid;
	}

	public void setKfname(java.lang.String value) {
		this.kfname = value;
	}

	public java.lang.String getKfname() {
		return this.kfname;
	}

	public void setKfopttime(Date value) {
		this.kfopttime = value;
	}

	public Date getKfopttime() {
		return this.kfopttime;
	}

	public void setCwremarks(java.lang.String value) {
		this.cwremarks = value;
	}

	public java.lang.String getCwremarks() {
		return this.cwremarks;
	}

	public void setCwid(java.lang.Long value) {
		this.cwid = value;
	}

	public java.lang.Long getCwid() {
		return this.cwid;
	}

	public void setCwname(java.lang.String value) {
		this.cwname = value;
	}

	public java.lang.String getCwname() {
		return this.cwname;
	}

	public void setCwopttime(Date value) {
		this.cwopttime = value;
	}

	public Date getCwopttime() {
		return this.cwopttime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(Integer ordertype) {
		this.ordertype = ordertype;
	}

	public BigDecimal getBeforebalance() {
		return beforebalance;
	}

	public void setBeforebalance(BigDecimal beforebalance) {
		this.beforebalance = beforebalance;
	}

	public BigDecimal getLaterbalance() {
		return laterbalance;
	}

	public void setLaterbalance(BigDecimal laterbalance) {
		this.laterbalance = laterbalance;
	}

	public String getHdnumber() {
		return hdnumber;
	}

	public void setHdnumber(String hdnumber) {
		this.hdnumber = hdnumber;
	}

    public String getProxyname() {
		return proxyname;
	}

	public void setProxyname(String proxyname) {
		this.proxyname = proxyname;
	}

	public String toString() {
		return new ToStringBuilder(this).append("Poid", getPoid()).append("Platformorders", getPlatformorders()).append("Uiid", getUiid())
				.append("Paytyple", getPaytyple()).append("Ppid", getPpid()).append("Paymethods", getPaymethods()).append("Bankname", getBankname())
				.append("Bankcard", getBankcard()).append("Openname", getOpenname()).append("Deposit", getDeposit())
				.append("Deposittime", getDeposittime()).append("Amount", getAmount()).append("Paystatus", getPaystatus())
				.append("Status", getStatus()).append("Remarks", getRemarks()).append("Ordercontent", getOrdercontent())
				.append("CreateDate", getCreate_Date()).append("UpdateDate", getUpdate_Date()).append("Uaccount", getUaccount())
				.append("Urealname", getUrealname()).append("Kfremarks", getKfremarks()).append("Kfid", getKfid()).append("Kfname", getKfname())
				.append("Kfopttime", getKfopttime()).append("Cwremarks", getCwremarks()).append("Cwid", getCwid()).append("Cwname", getCwname())
				.append("Cwopttime", getCwopttime()).toString();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getPoid()).append(getPlatformorders()).append(getUiid()).append(getPaytyple()).append(getPpid())
				.append(getPaymethods()).append(getBankname()).append(getBankcard()).append(getOpenname()).append(getDeposit())
				.append(getDeposittime()).append(getAmount()).append(getPaystatus()).append(getStatus()).append(getRemarks())
				.append(getOrdercontent()).append(getCreate_Date()).append(getUpdate_Date()).append(getUaccount()).append(getUrealname())
				.append(getKfremarks()).append(getKfid()).append(getKfname()).append(getKfopttime()).append(getCwremarks()).append(getCwid())
				.append(getCwname()).append(getCwopttime()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (obj instanceof PayOrder == false)
			return false;
		if (this == obj)
			return true;
		PayOrder other = (PayOrder) obj;
		return new EqualsBuilder().append(getPoid(), other.getPoid()).append(getPlatformorders(), other.getPlatformorders())
				.append(getUiid(), other.getUiid()).append(getPaytyple(), other.getPaytyple()).append(getPpid(), other.getPpid())
				.append(getPaymethods(), other.getPaymethods()).append(getBankname(), other.getBankname()).append(getBankcard(), other.getBankcard())
				.append(getOpenname(), other.getOpenname()).append(getDeposit(), other.getDeposit()).append(getDeposittime(), other.getDeposittime())
				.append(getAmount(), other.getAmount()).append(getPaystatus(), other.getPaystatus()).append(getStatus(), other.getStatus())
				.append(getRemarks(), other.getRemarks()).append(getOrdercontent(), other.getOrdercontent())
				.append(getCreate_Date(), other.getCreate_Date()).append(getUpdate_Date(), other.getUpdate_Date())
				.append(getUaccount(), other.getUaccount()).append(getUrealname(), other.getUrealname()).append(getKfremarks(), other.getKfremarks())
				.append(getKfid(), other.getKfid()).append(getKfname(), other.getKfname()).append(getKfopttime(), other.getKfopttime())
				.append(getCwremarks(), other.getCwremarks()).append(getCwid(), other.getCwid()).append(getCwname(), other.getCwname())
				.append(getCwopttime(), other.getCwopttime()).isEquals();
	}

	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return this.poid;
	}
}
