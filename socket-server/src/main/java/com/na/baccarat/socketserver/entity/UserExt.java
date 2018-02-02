package com.na.baccarat.socketserver.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author
 * @time 2015-07-21 18:13:40
 */
public class UserExt implements Serializable{
	private static final Long serialVersionUID = 1L;

	private Long uid;
	private String email;
	private String passwordQuestion;
	private String passwordAnswer;
	/**
	 * 总输赢总和，可以为负数
	 */
	private BigDecimal sumWinLost;
	/**
	 * 总下注金额
	 */
	//Long-BigDecimal	
	private BigDecimal totalBetMoney;
	/**
	 * 单注最小下注限制
	 */
	private Long limitOneBetMin;
	/**
	 * 
	 */
	private BigDecimal returnBackMoney;
	/**
	 * 占成比 40%
	 */
	private BigDecimal oPercentage;
	/**
	 * 洗码比
	 */
	private BigDecimal sPercentage;
	/**
	 * 占成比 40%
	 */
	private BigDecimal oPercentage1;
	/**
	 * 洗码比
	 */
	private BigDecimal sPercentage1;
	/**
	 * 占成比 40%
	 */
	private BigDecimal oPercentage2;
	/**
	 * 洗码比
	 */
	private BigDecimal sPercentage2;
	/**
	 * 占成比 40%
	 */
	private BigDecimal oPercentage3;
	/**
	 * 洗码比
	 */
	private BigDecimal sPercentage3;
	/**
	 * 占成比 40%
	 */
	private BigDecimal oPercentage4;
	/**
	 * 洗码比
	 */
	private BigDecimal sPercentage4;
	/**
	 * 占成比 40%
	 */
	private BigDecimal oPercentage5;
	/**
	 * 洗码比
	 */
	private BigDecimal sPercentage5;
	/**
	 * 占成比 40%
	 */
	private BigDecimal oPercentage6;
	/**
	 * 洗码比
	 */
	private BigDecimal sPercentage6;

	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 过期时间，这个有用吗？？0 不过期
	 */
	private Long expireDate;
	/**
	 * 限红,最高下注
	 */
	private Long limitRed;
	/**
	 * 他的注码，可以多个 1,2,3 对应cid
	 */
	private String chips;

	/**
	 * 空所有，否则固定被访问桌子ID
	 */
	private String accessTable;

	private Long maxLimit;
	private Long maxLimit1;
	private Long maxLimit2;
	private Long maxLimit3;
	private Long maxLimit4;
	private Long maxLimit5;
	private Long maxLimit6;

	//是否是包台用户
	private boolean iscontract;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordQuestion() {
		return passwordQuestion;
	}

	public void setPasswordQuestion(String passwordQuestion) {
		this.passwordQuestion = passwordQuestion;
	}

	public String getPasswordAnswer() {
		return passwordAnswer;
	}

	public void setPasswordAnswer(String passwordAnswer) {
		this.passwordAnswer = passwordAnswer;
	}

	public BigDecimal getSumWinLost() {
		return sumWinLost;
	}

	public void setSumWinLost(BigDecimal sumWinLost) {
		this.sumWinLost = sumWinLost;
	}

	public BigDecimal getTotalBetMoney() {
		return totalBetMoney;
	}

	public void setTotalBetMoney(BigDecimal totalBetMoney) {
		this.totalBetMoney = totalBetMoney;
	}

	public Long getLimitOneBetMin() {
		return limitOneBetMin;
	}

	public void setLimitOneBetMin(Long limitOneBetMin) {
		this.limitOneBetMin = limitOneBetMin;
	}

	public BigDecimal getReturnBackMoney() {
		return returnBackMoney;
	}

	public void setReturnBackMoney(BigDecimal returnBackMoney) {
		this.returnBackMoney = returnBackMoney;
	}

	public BigDecimal getoPercentage() {
		return oPercentage;
	}

	public void setoPercentage(BigDecimal oPercentage) {
		this.oPercentage = oPercentage;
	}

	public BigDecimal getsPercentage() {
		return sPercentage;
	}

	public void setsPercentage(BigDecimal sPercentage) {
		this.sPercentage = sPercentage;
	}

	public BigDecimal getoPercentage1() {
		return oPercentage1;
	}

	public void setoPercentage1(BigDecimal oPercentage1) {
		this.oPercentage1 = oPercentage1;
	}

	public BigDecimal getsPercentage1() {
		return sPercentage1;
	}

	public void setsPercentage1(BigDecimal sPercentage1) {
		this.sPercentage1 = sPercentage1;
	}

	public BigDecimal getoPercentage2() {
		return oPercentage2;
	}

	public void setoPercentage2(BigDecimal oPercentage2) {
		this.oPercentage2 = oPercentage2;
	}

	public BigDecimal getsPercentage2() {
		return sPercentage2;
	}

	public void setsPercentage2(BigDecimal sPercentage2) {
		this.sPercentage2 = sPercentage2;
	}

	public BigDecimal getoPercentage3() {
		return oPercentage3;
	}

	public void setoPercentage3(BigDecimal oPercentage3) {
		this.oPercentage3 = oPercentage3;
	}

	public BigDecimal getsPercentage3() {
		return sPercentage3;
	}

	public void setsPercentage3(BigDecimal sPercentage3) {
		this.sPercentage3 = sPercentage3;
	}

	public BigDecimal getoPercentage4() {
		return oPercentage4;
	}

	public void setoPercentage4(BigDecimal oPercentage4) {
		this.oPercentage4 = oPercentage4;
	}

	public BigDecimal getsPercentage4() {
		return sPercentage4;
	}

	public void setsPercentage4(BigDecimal sPercentage4) {
		this.sPercentage4 = sPercentage4;
	}

	public BigDecimal getoPercentage5() {
		return oPercentage5;
	}

	public void setoPercentage5(BigDecimal oPercentage5) {
		this.oPercentage5 = oPercentage5;
	}

	public BigDecimal getsPercentage5() {
		return sPercentage5;
	}

	public void setsPercentage5(BigDecimal sPercentage5) {
		this.sPercentage5 = sPercentage5;
	}

	public BigDecimal getoPercentage6() {
		return oPercentage6;
	}

	public void setoPercentage6(BigDecimal oPercentage6) {
		this.oPercentage6 = oPercentage6;
	}

	public BigDecimal getsPercentage6() {
		return sPercentage6;
	}

	public void setsPercentage6(BigDecimal sPercentage6) {
		this.sPercentage6 = sPercentage6;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Long expireDate) {
		this.expireDate = expireDate;
	}

	public Long getLimitRed() {
		return limitRed;
	}

	public void setLimitRed(Long limitRed) {
		this.limitRed = limitRed;
	}

	public String getChips() {
		return chips;
	}

	public void setChips(String chips) {
		this.chips = chips;
	}

	public String getAccessTable() {
		return accessTable;
	}

	public void setAccessTable(String accessTable) {
		this.accessTable = accessTable;
	}

	public Long getMaxLimit() {
		return maxLimit;
	}

	public void setMaxLimit(Long maxLimit) {
		this.maxLimit = maxLimit;
	}

	public Long getMaxLimit1() {
		return maxLimit1;
	}

	public void setMaxLimit1(Long maxLimit1) {
		this.maxLimit1 = maxLimit1;
	}

	public Long getMaxLimit2() {
		return maxLimit2;
	}

	public void setMaxLimit2(Long maxLimit2) {
		this.maxLimit2 = maxLimit2;
	}

	public Long getMaxLimit3() {
		return maxLimit3;
	}

	public void setMaxLimit3(Long maxLimit3) {
		this.maxLimit3 = maxLimit3;
	}

	public Long getMaxLimit4() {
		return maxLimit4;
	}

	public void setMaxLimit4(Long maxLimit4) {
		this.maxLimit4 = maxLimit4;
	}

	public Long getMaxLimit5() {
		return maxLimit5;
	}

	public void setMaxLimit5(Long maxLimit5) {
		this.maxLimit5 = maxLimit5;
	}

	public Long getMaxLimit6() {
		return maxLimit6;
	}

	public void setMaxLimit6(Long maxLimit6) {
		this.maxLimit6 = maxLimit6;
	}


	public boolean isIscontract() {
		return iscontract;
	}

	public void setIscontract(boolean iscontract) {
		this.iscontract = iscontract;
	}
}