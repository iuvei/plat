package com.gameportal.manage.betlog.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * 电子游戏列表
 * @author Administrator
 *
 */
public class SlotGameBetLog extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3244218294747297448L;
	
	/**
	 * 注单编号
	 */
	private String betno;
	
	/**
	 * 投注账号
	 */
	private String account;
	
	/**
	 * 平台编号
	 * AG,MG,PT
	 */
	private String platformcode;
	
	/**
	 * 游戏CODE
	 */
	private String gamecode;
	
	/**
	 * 游戏名称
	 */
	private String gamename;
	
	/**
	 * 
	 */
	private String gameCategory;
	
	/**
	 * 投注时间
	 */
	private String betdate;
	
	/**
	 * 投注金额
	 */
	private java.math.BigDecimal betamount;
	
	/**
	 * 派彩金额
	 */
	private java.math.BigDecimal profitamount;
	
	/**
	 * 输赢金额
	 */
	private java.math.BigDecimal finalamount;
	
	/**
	 * 客户端设备
	 */
	private String origin;
	
	/**
	 * 有效投注额
	 */
	private java.math.BigDecimal validBetAmount;
	
	/**
	 * 客户端IP
	 */
	private String clientIP;
	
	private java.math.BigDecimal beforeCerdit;
	
	/**
	 * 注单表示：默认0
	 * 0：正式注单
	 * 1：免费游戏注单
	 */
	private int flag;

	public String getBetno() {
		return betno;
	}

	public void setBetno(String betno) {
		this.betno = betno;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPlatformcode() {
		return platformcode;
	}

	public void setPlatformcode(String platformcode) {
		this.platformcode = platformcode;
	}

	public String getGamecode() {
		return gamecode;
	}

	public void setGamecode(String gamecode) {
		this.gamecode = gamecode;
	}

	public String getGamename() {
		return gamename;
	}

	public void setGamename(String gamename) {
		this.gamename = gamename;
	}

	public String getGameCategory() {
		return gameCategory;
	}

	public void setGameCategory(String gameCategory) {
		this.gameCategory = gameCategory;
	}

	public String getBetdate() {
		return betdate;
	}

	public void setBetdate(String betdate) {
		this.betdate = betdate;
	}

	

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
	

	public java.math.BigDecimal getBetamount() {
		return betamount;
	}

	public void setBetamount(java.math.BigDecimal betamount) {
		this.betamount = betamount;
	}

	public java.math.BigDecimal getProfitamount() {
		return profitamount;
	}

	public void setProfitamount(java.math.BigDecimal profitamount) {
		this.profitamount = profitamount;
	}

	public java.math.BigDecimal getFinalamount() {
		return finalamount;
	}

	public void setFinalamount(java.math.BigDecimal finalamount) {
		this.finalamount = finalamount;
	}

	public java.math.BigDecimal getValidBetAmount() {
		return validBetAmount;
	}

	public void setValidBetAmount(java.math.BigDecimal validBetAmount) {
		this.validBetAmount = validBetAmount;
	}

	public String getClientIP() {
		return clientIP;
	}

	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}
	

	public java.math.BigDecimal getBeforeCerdit() {
		return beforeCerdit;
	}

	public void setBeforeCerdit(java.math.BigDecimal beforeCerdit) {
		this.beforeCerdit = beforeCerdit;
	}
	

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "SlotGameBetLog [betno=" + betno + ", account=" + account
				+ ", platformcode=" + platformcode + ", gamecode=" + gamecode
				+ ", gamename=" + gamename + ", gameCategory=" + gameCategory
				+ ", betdate=" + betdate + ", betamount=" + betamount
				+ ", profitamount=" + profitamount + ", finalamount="
				+ finalamount + ", origin=" + origin + ", validBetAmount="
				+ validBetAmount + ", clientIP=" + clientIP + "]";
	}

	@Override
	public Serializable getID() {
		return null;
	}

}
