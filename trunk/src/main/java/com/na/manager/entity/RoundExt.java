package com.na.manager.entity;

import java.io.Serializable;


/**
 * 游戏专有扩展表
 * 
 * @author alan
 * @date 2017年4月28日 上午10:27:35
 */
public class RoundExt implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private Long roundId;
	/**
	 * 庄第一张牌类型
	 * H红桃  D方片  S黑桃 C梅花
	 */
	private String bankCard1Mode;
	/**
	 * 
	 */
	private Integer bankCard1Number;
	/**
	 * 
	 */
	private String bankCard2Mode;
	/**
	 * 
	 */
	private Integer bankCard2Number;
	/**
	 * 
	 */
	private String bankCard3Mode;
	/**
	 * 
	 */
	private Integer bankCard3Number;
	/**
	 * 闲第一张牌类型
	 */
	private String playerCard1Mode;
	/**
	 * 
	 */
	private Integer playerCard1Number;
	/**
	 * 
	 */
	private String playerCard2Mode;
	/**
	 * 
	 */
	private Integer playerCard2Number;
	/**
	 * 
	 */
	private String playerCard3Mode;
	/**
	 * 
	 */
	private Integer playerCard3Number;
	public String getBankCard1Mode() {
		return bankCard1Mode;
	}
	public void setBankCard1Mode(String bankCard1Mode) {
		this.bankCard1Mode = bankCard1Mode;
	}
	public String getBankCard2Mode() {
		return bankCard2Mode;
	}
	public void setBankCard2Mode(String bankCard2Mode) {
		this.bankCard2Mode = bankCard2Mode;
	}
	public String getBankCard3Mode() {
		return bankCard3Mode;
	}
	public void setBankCard3Mode(String bankCard3Mode) {
		this.bankCard3Mode = bankCard3Mode;
	}
	public String getPlayerCard1Mode() {
		return playerCard1Mode;
	}
	public void setPlayerCard1Mode(String playerCard1Mode) {
		this.playerCard1Mode = playerCard1Mode;
	}
	public String getPlayerCard2Mode() {
		return playerCard2Mode;
	}
	public void setPlayerCard2Mode(String playerCard2Mode) {
		this.playerCard2Mode = playerCard2Mode;
	}
	public String getPlayerCard3Mode() {
		return playerCard3Mode;
	}
	public void setPlayerCard3Mode(String playerCard3Mode) {
		this.playerCard3Mode = playerCard3Mode;
	}
	public Long getRoundId() {
		return roundId;
	}
	public void setRoundId(Long roundId) {
		this.roundId = roundId;
	}
	public Integer getBankCard1Number() {
		return bankCard1Number;
	}
	public void setBankCard1Number(Integer bankCard1Number) {
		this.bankCard1Number = bankCard1Number;
	}
	public Integer getBankCard2Number() {
		return bankCard2Number;
	}
	public void setBankCard2Number(Integer bankCard2Number) {
		this.bankCard2Number = bankCard2Number;
	}
	public Integer getBankCard3Number() {
		return bankCard3Number;
	}
	public void setBankCard3Number(Integer bankCard3Number) {
		this.bankCard3Number = bankCard3Number;
	}
	public Integer getPlayerCard1Number() {
		return playerCard1Number;
	}
	public void setPlayerCard1Number(Integer playerCard1Number) {
		this.playerCard1Number = playerCard1Number;
	}
	public Integer getPlayerCard2Number() {
		return playerCard2Number;
	}
	public void setPlayerCard2Number(Integer playerCard2Number) {
		this.playerCard2Number = playerCard2Number;
	}
	public Integer getPlayerCard3Number() {
		return playerCard3Number;
	}
	public void setPlayerCard3Number(Integer playerCard3Number) {
		this.playerCard3Number = playerCard3Number;
	}

	
}