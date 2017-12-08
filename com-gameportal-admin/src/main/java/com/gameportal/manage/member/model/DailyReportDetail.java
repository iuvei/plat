package com.gameportal.manage.member.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.gameportal.manage.system.model.BaseEntity;

public class DailyReportDetail extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	private long staId;

	/**
	 * 创建日期
	 */
	private Date reporttime;

	/**
	 * 注册人数
	 */
	private long registerNumber;

	/**
	 * 首存人数
	 */
	private long firstPayNumber;

	/**
	 * 首存金额
	 */
	private BigDecimal firstPayTotalMoney;

	/**
	 * 登录人数
	 */
	private long loginCount;

	/**
	 * 活跃人数
	 */
	private long activeCount;
	/**
	 * 投注总额
	 */
	private BigDecimal realBetMoney;

	/**
	 * 会员优惠
	 */
	private BigDecimal memberCoupon;
	/**
	 * 代理优惠
	 */
	private BigDecimal proxyCoupon;
	/**
	 * 会员洗码
	 */
	private BigDecimal payOrderXimaMoney;
	/**
	 * 代理洗码
	 */
	private BigDecimal proxyXimaMoney;
	/**
	 * 代理结算
	 */
	private BigDecimal proxyClearMoney;

	/**
	 * 存款金额
	 */
	private BigDecimal payMoney;

	/**
	 * 存款人数
	 */
	private long payMoneyPerson;
	/**
	 * 存款笔数
	 */
	private long payMoneyCount;

	/**
	 * 提款金额
	 */
	private BigDecimal pickUpMoney;

	/**
	 * 提款人数
	 */
	private long pickUpMoneyPerson;
	/**
	 * 提款笔数
	 */
	private long pickUpMoneyCount;

	public long getStaId() {
		return staId;
	}

	public void setStaId(long staId) {
		this.staId = staId;
	}

	public Date getReporttime() {
		return reporttime;
	}

	public void setReporttime(Date reporttime) {
		this.reporttime = reporttime;
	}

	public long getRegisterNumber() {
		return registerNumber;
	}

	public void setRegisterNumber(long registerNumber) {
		this.registerNumber = registerNumber;
	}

	public long getFirstPayNumber() {
		return firstPayNumber;
	}

	public void setFirstPayNumber(long firstPayNumber) {
		this.firstPayNumber = firstPayNumber;
	}

	public BigDecimal getFirstPayTotalMoney() {
		return firstPayTotalMoney;
	}

	public void setFirstPayTotalMoney(BigDecimal firstPayTotalMoney) {
		this.firstPayTotalMoney = firstPayTotalMoney;
	}

	public long getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(long loginCount) {
		this.loginCount = loginCount;
	}

	public long getActiveCount() {
		return activeCount;
	}

	public void setActiveCount(long activeCount) {
		this.activeCount = activeCount;
	}

	public BigDecimal getRealBetMoney() {
		return realBetMoney;
	}

	public void setRealBetMoney(BigDecimal realBetMoney) {
		this.realBetMoney = realBetMoney;
	}

	public BigDecimal getMemberCoupon() {
		return memberCoupon;
	}

	public void setMemberCoupon(BigDecimal memberCoupon) {
		this.memberCoupon = memberCoupon;
	}

	public BigDecimal getProxyCoupon() {
		return proxyCoupon;
	}

	public void setProxyCoupon(BigDecimal proxyCoupon) {
		this.proxyCoupon = proxyCoupon;
	}

	public BigDecimal getPayOrderXimaMoney() {
		return payOrderXimaMoney;
	}

	public void setPayOrderXimaMoney(BigDecimal payOrderXimaMoney) {
		this.payOrderXimaMoney = payOrderXimaMoney;
	}

	public BigDecimal getProxyXimaMoney() {
		return proxyXimaMoney;
	}

	public void setProxyXimaMoney(BigDecimal proxyXimaMoney) {
		this.proxyXimaMoney = proxyXimaMoney;
	}

	public BigDecimal getProxyClearMoney() {
		return proxyClearMoney;
	}

	public void setProxyClearMoney(BigDecimal proxyClearMoney) {
		this.proxyClearMoney = proxyClearMoney;
	}

	public BigDecimal getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}

	public long getPayMoneyPerson() {
		return payMoneyPerson;
	}

	public void setPayMoneyPerson(long payMoneyPerson) {
		this.payMoneyPerson = payMoneyPerson;
	}

	public long getPayMoneyCount() {
		return payMoneyCount;
	}

	public void setPayMoneyCount(long payMoneyCount) {
		this.payMoneyCount = payMoneyCount;
	}

	public BigDecimal getPickUpMoney() {
		return pickUpMoney;
	}

	public void setPickUpMoney(BigDecimal pickUpMoney) {
		this.pickUpMoney = pickUpMoney;
	}

	public long getPickUpMoneyPerson() {
		return pickUpMoneyPerson;
	}

	public void setPickUpMoneyPerson(long pickUpMoneyPerson) {
		this.pickUpMoneyPerson = pickUpMoneyPerson;
	}

	public long getPickUpMoneyCount() {
		return pickUpMoneyCount;
	}

	public void setPickUpMoneyCount(long pickUpMoneyCount) {
		this.pickUpMoneyCount = pickUpMoneyCount;
	}
	
	@Override
	public Serializable getID() {
		return staId;
	}
}
