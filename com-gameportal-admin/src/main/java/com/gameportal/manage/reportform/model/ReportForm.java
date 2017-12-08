package com.gameportal.manage.reportform.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * 报表统计实体
 * @author Administrator
 *
 */
public class ReportForm extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -933154084073425253L;

	/**
	 * 报表时间
	 */
	private String reporttime;
	
	/**
	 * 注册人数
	 */
	private Integer registerNumber;
	
	/**
	 * 首充人数
	 */
	private Integer firstPayNumber;
	
	/**
	 * 首充总金额
	 */
	private String firstPayTotalMoney;
	
	/**
	 * 实际投注额
	 */
	private String realBetMoney;
	
	/**
	 * 平台输赢
	 */
	private String platformBunko;
	
	
	/**
	 * 会员优惠
	 */
	private String memberCoupon;
	
	
	/**
	 * 代理优惠
	 */
	private String proxyCoupon;
	
	
	/**
	 * 充值金额
	 */
	private String payMoney;
	
	/**
	 * 充值人数
	 */
	private Integer payMoneyPerson;
	
	/**
	 * 充值比数
	 */
	private Integer payMoneyCount;
	
	/**
	 * 提款金额
	 */
	private String pickUpMoney;
	
	/**
	 * 提款人数
	 */
	private Integer pickUpMoneyPerson;
	
	/**
	 * 提款比数
	 */
	private Integer pickUpMoneyCount;
	
	/**
	 * 会员洗码金额
	 */
	private String memberXimaMoney;
	
	/**
	 * 代理洗码金额
	 */
	private String proxyXimaMoney;
	
	/**
	 * 代理结算佣金金额
	 */
	private String proxyClearMoney;
	
	/**
	 * 手工洗码金额
	 */
	private String payOrderXimaMoney;
	
	/**
	 * 平台余额
	 */
	private String platformMoney;
	
	
	@Override
	public Serializable getID() {
		return null;
	}

	public Integer getRegisterNumber() {
		return registerNumber;
	}

	public void setRegisterNumber(Integer registerNumber) {
		this.registerNumber = registerNumber;
	}

	public Integer getFirstPayNumber() {
		return firstPayNumber;
	}

	public void setFirstPayNumber(Integer firstPayNumber) {
		this.firstPayNumber = firstPayNumber;
	}

	public String getFirstPayTotalMoney() {
		return firstPayTotalMoney;
	}

	public void setFirstPayTotalMoney(String firstPayTotalMoney) {
		this.firstPayTotalMoney = firstPayTotalMoney;
	}

	public String getRealBetMoney() {
		return realBetMoney;
	}

	public void setRealBetMoney(String realBetMoney) {
		this.realBetMoney = realBetMoney;
	}

	public String getPlatformBunko() {
		return platformBunko;
	}

	public void setPlatformBunko(String platformBunko) {
		this.platformBunko = platformBunko;
	}

	public String getMemberCoupon() {
		return memberCoupon;
	}

	public void setMemberCoupon(String memberCoupon) {
		this.memberCoupon = memberCoupon;
	}

	public String getProxyCoupon() {
		return proxyCoupon;
	}

	public void setProxyCoupon(String proxyCoupon) {
		this.proxyCoupon = proxyCoupon;
	}

	public String getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}

	public String getPickUpMoney() {
		return pickUpMoney;
	}

	public void setPickUpMoney(String pickUpMoney) {
		this.pickUpMoney = pickUpMoney;
	}

	public String getPlatformMoney() {
		return platformMoney;
	}

	public void setPlatformMoney(String platformMoney) {
		this.platformMoney = platformMoney;
	}

	public String getReporttime() {
		return reporttime;
	}

	public void setReporttime(String reporttime) {
		this.reporttime = reporttime;
	}

	public Integer getPayMoneyPerson() {
		return payMoneyPerson;
	}

	public void setPayMoneyPerson(Integer payMoneyPerson) {
		this.payMoneyPerson = payMoneyPerson;
	}

	public Integer getPayMoneyCount() {
		return payMoneyCount;
	}

	public void setPayMoneyCount(Integer payMoneyCount) {
		this.payMoneyCount = payMoneyCount;
	}

	public Integer getPickUpMoneyPerson() {
		return pickUpMoneyPerson;
	}

	public void setPickUpMoneyPerson(Integer pickUpMoneyPerson) {
		this.pickUpMoneyPerson = pickUpMoneyPerson;
	}

	public Integer getPickUpMoneyCount() {
		return pickUpMoneyCount;
	}

	public void setPickUpMoneyCount(Integer pickUpMoneyCount) {
		this.pickUpMoneyCount = pickUpMoneyCount;
	}

	public String getMemberXimaMoney() {
		return memberXimaMoney;
	}

	public void setMemberXimaMoney(String memberXimaMoney) {
		this.memberXimaMoney = memberXimaMoney;
	}

	public String getProxyXimaMoney() {
		return proxyXimaMoney;
	}

	public void setProxyXimaMoney(String proxyXimaMoney) {
		this.proxyXimaMoney = proxyXimaMoney;
	}

	public String getProxyClearMoney() {
		return proxyClearMoney;
	}

	public void setProxyClearMoney(String proxyClearMoney) {
		this.proxyClearMoney = proxyClearMoney;
	}

	public String getPayOrderXimaMoney() {
		return payOrderXimaMoney;
	}

	public void setPayOrderXimaMoney(String payOrderXimaMoney) {
		this.payOrderXimaMoney = payOrderXimaMoney;
	}
}
