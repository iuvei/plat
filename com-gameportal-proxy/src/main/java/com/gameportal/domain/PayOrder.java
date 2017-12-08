package com.gameportal.domain;

/**
 * 订单实体
 * @author leron
 *
 */
public class PayOrder extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6043341922733016775L;

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
	private Double amount;
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
	//columns END
	
	//other columns
	private String paystatusname; //第三方状态
	private String statusname;    //当前状态
	private String paymethodsname;//付款方式
	private String ordertypename; //订单类型名称 
	public java.lang.String getPoid() {
		return poid;
	}
	public void setPoid(java.lang.String poid) {
		this.poid = poid;
	}
	public java.lang.String getPlatformorders() {
		return platformorders;
	}
	public void setPlatformorders(java.lang.String platformorders) {
		this.platformorders = platformorders;
	}
	public java.lang.Long getUiid() {
		return uiid;
	}
	public void setUiid(java.lang.Long uiid) {
		this.uiid = uiid;
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
	public java.lang.Long getPpid() {
		return ppid;
	}
	public void setPpid(java.lang.Long ppid) {
		this.ppid = ppid;
	}
	public java.lang.Integer getPaymethods() {
		return paymethods;
	}
	public void setPaymethods(java.lang.Integer paymethods) {
		this.paymethods = paymethods;
	}
	public java.lang.String getBankname() {
		return bankname;
	}
	public void setBankname(java.lang.String bankname) {
		this.bankname = bankname;
	}
	public java.lang.String getBankcard() {
		return bankcard;
	}
	public void setBankcard(java.lang.String bankcard) {
		this.bankcard = bankcard;
	}
	public java.lang.String getOpenname() {
		return openname;
	}
	public void setOpenname(java.lang.String openname) {
		this.openname = openname;
	}
	public java.lang.String getDeposit() {
		return deposit;
	}
	public void setDeposit(java.lang.String deposit) {
		this.deposit = deposit;
	}
	public String getDeposittime() {
		return deposittime;
	}
	public void setDeposittime(String deposittime) {
		this.deposittime = deposittime;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public java.lang.Integer getPaystatus() {
		return paystatus;
	}
	public void setPaystatus(java.lang.Integer paystatus) {
		this.paystatus = paystatus;
	}
	public java.lang.Integer getStatus() {
		return status;
	}
	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}
	public java.lang.String getRemarks() {
		return remarks;
	}
	public void setRemarks(java.lang.String remarks) {
		this.remarks = remarks;
	}
	public java.lang.String getOrdercontent() {
		return ordercontent;
	}
	public void setOrdercontent(java.lang.String ordercontent) {
		this.ordercontent = ordercontent;
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
	public String getHdnumber() {
		return hdnumber;
	}
	public void setHdnumber(String hdnumber) {
		this.hdnumber = hdnumber;
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
	
}
