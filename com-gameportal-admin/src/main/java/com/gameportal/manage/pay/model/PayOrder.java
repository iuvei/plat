package com.gameportal.manage.pay.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.gameportal.manage.system.model.BaseEntity;

public class PayOrder extends BaseEntity {
	
	//alias
	public static final String TABLE_ALIAS = "PayOrder";
	public static final String ALIAS_POID = "订单ID";
	public static final String ALIAS_PLATFORMORDERS = "platformorders";
	public static final String ALIAS_UIID = "用户ID";
	public static final String ALIAS_PAYTYPLE = "收支类型 0 充值 1 提现";
	public static final String ALIAS_PPID = "支付平台信息ID";
	public static final String ALIAS_PAYMETHODS = "支付方式 0 公司入款  1第三方支付";
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
	
	
	//columns START
	private java.lang.String poid;
	private java.lang.String platformorders;
	private java.lang.Long uiid;
	
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
	private java.lang.Integer ordertype = 0;
	
	private java.lang.Long ppid;
	
	/**
	 * 0:公司入款
	 * 1：第三方支付
	 */
	private java.lang.Integer paymethods;
	private java.lang.String bankname;
	private java.lang.String bankcard;
	private java.lang.String openname;
	private java.lang.String deposit;
	private String deposittime;
	private BigDecimal amount;
	private java.lang.Integer paystatus;
	
	/**
	 * 0:作废 
	 * 1: 发起（待财务审核） 
	 * 2: 处理中（待客服核实） 
	 * 3: 存取成功 
	 * 4: 存取失败
	 */
	private java.lang.Integer status;
	private java.lang.String remarks;
	private java.lang.String ordercontent;
	private String createDate;
	private String updateDate;
	private java.lang.String uaccount;
	private java.lang.String urealname;
	private java.lang.String kfremarks;
	private java.lang.Long kfid;
	private java.lang.String kfname;
	private String kfopttime;
	private java.lang.String cwremarks;
	private java.lang.Long cwid;
	private java.lang.String cwname;
	private String cwopttime;
	private java.lang.String beforebalance = "0.00"; 
	private java.lang.String laterbalance = "0.00";
	
	/**
	 * 活动编号
	 * 不为空表示用户参加了优惠活动
	 */
	private String hdnumber;
	
	/**
	 * 公司入款订单号
	 */
	private String ordernumber;
	//columns END
	
	//other columns
	private String paystatusname; //第三方状态
	private String statusname;    //当前状态
	private String paymethodsname;//付款方式
	private String ordertypename; //订单类型名称 
	private String proxyname; //所属代理
	
	public PayOrder(){
	}

	public PayOrder(
		java.lang.String poid
	){
		this.poid = poid;
	}

	public String getOrdernumber() {
		return ordernumber;
	}

	public void setOrdernumber(String ordernumber) {
		this.ordernumber = ordernumber;
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

	public String getCwopttime() {
		return cwopttime;
	}

	public void setCwopttime(String cwopttime) {
		this.cwopttime = cwopttime;
	}

	public java.lang.Integer getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(java.lang.Integer ordertype) {
		this.ordertype = ordertype;
	}
	
	public String getDeposittime() {
		return deposittime;
	}

	public void setDeposittime(String deposittime) {
		this.deposittime = deposittime;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getKfopttime() {
		return kfopttime;
	}

	public void setKfopttime(String kfopttime) {
		this.kfopttime = kfopttime;
	}
	
	public String getPaystatusname() {
		return paystatusname;
	}

	public void setPaystatusname(String paystatusname) {
		this.paystatusname = paystatusname;
	}

	public String getStatusname() {
		return statusname;
	}

	public void setStatusname(String statusname) {
		this.statusname = statusname;
	}

	public String getPaymethodsname() {
		return paymethodsname;
	}

	public void setPaymethodsname(String paymethodsname) {
		this.paymethodsname = paymethodsname;
	}
	
	public String getOrdertypename() {
		return ordertypename;
	}
	
	public void setOrdertypename(String ordertypename) {
	    this.ordertypename = ordertypename;
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

	@Override
	public String toString() {
		return "PayOrder [poid=" + poid + ", platformorders=" + platformorders
				+ ", uiid=" + uiid + ", paytyple=" + paytyple + ", ordertype="
				+ ordertype + ", ppid=" + ppid + ", paymethods=" + paymethods
				+ ", bankname=" + bankname + ", bankcard=" + bankcard
				+ ", openname=" + openname + ", deposit=" + deposit
				+ ", deposittime=" + deposittime + ", amount=" + amount
				+ ", paystatus=" + paystatus + ", status=" + status
				+ ", remarks=" + remarks + ", ordercontent=" + ordercontent
				+ ", createDate=" + createDate + ", updateDate=" + updateDate
				+ ", uaccount=" + uaccount + ", urealname=" + urealname
				+ ", kfremarks=" + kfremarks + ", kfid=" + kfid + ", kfname="
				+ kfname + ", kfopttime=" + kfopttime + ", cwremarks="
				+ cwremarks + ", cwid=" + cwid + ", cwname=" + cwname
				+ ", cwopttime=" + cwopttime + "]";
	}

	public int hashCode() {
		return new HashCodeBuilder()
			.append(getPoid())
			.append(getPlatformorders())
			.append(getUiid())
			.append(getPaytyple())
			.append(getPpid())
			.append(getPaymethods())
			.append(getBankname())
			.append(getBankcard())
			.append(getOpenname())
			.append(getDeposit())
			.append(getDeposittime())
			.append(getAmount())
			.append(getPaystatus())
			.append(getStatus())
			.append(getRemarks())
			.append(getOrdercontent())
			.append(getCreateDate())
			.append(getUpdateDate())
			.append(getUaccount())
			.append(getUrealname())
			.append(getKfremarks())
			.append(getKfid())
			.append(getKfname())
			.append(getKfopttime())
			.append(getCwremarks())
			.append(getCwid())
			.append(getCwname())
			.append(getCwopttime())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof PayOrder == false) return false;
		if(this == obj) return true;
		PayOrder other = (PayOrder)obj;
		return new EqualsBuilder()
			.append(getPoid(),other.getPoid())
			.append(getPlatformorders(),other.getPlatformorders())
			.append(getUiid(),other.getUiid())
			.append(getPaytyple(),other.getPaytyple())
			.append(getPpid(),other.getPpid())
			.append(getPaymethods(),other.getPaymethods())
			.append(getBankname(),other.getBankname())
			.append(getBankcard(),other.getBankcard())
			.append(getOpenname(),other.getOpenname())
			.append(getDeposit(),other.getDeposit())
			.append(getDeposittime(),other.getDeposittime())
			.append(getAmount(),other.getAmount())
			.append(getPaystatus(),other.getPaystatus())
			.append(getStatus(),other.getStatus())
			.append(getRemarks(),other.getRemarks())
			.append(getOrdercontent(),other.getOrdercontent())
			.append(getCreateDate(),other.getCreateDate())
			.append(getUpdateDate(),other.getUpdateDate())
			.append(getUaccount(),other.getUaccount())
			.append(getUrealname(),other.getUrealname())
			.append(getKfremarks(),other.getKfremarks())
			.append(getKfid(),other.getKfid())
			.append(getKfname(),other.getKfname())
			.append(getKfopttime(),other.getKfopttime())
			.append(getCwremarks(),other.getCwremarks())
			.append(getCwid(),other.getCwid())
			.append(getCwname(),other.getCwname())
			.append(getCwopttime(),other.getCwopttime())
			.isEquals();
	}
	



	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return  this.poid;
	}
}

