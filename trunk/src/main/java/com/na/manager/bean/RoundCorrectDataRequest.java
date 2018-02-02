package com.na.manager.bean;


public class RoundCorrectDataRequest {

	/**
	 * 局ID
	 */
	private Long rid;
	/**
	 * 游戏ID
	 */
	private Integer gameId;
	/**
	 * 桌ID
	 */
	private Integer gameTableId;
	/**
	 * 靴ID 20150720-1
	 */
	private String bootId;
	/**
	 * 局数
	 */
	private Integer roundNumber;
	/**
	 * 靴数
	 */
	private Integer bootNumber;
	/**
	 * 靴开始时间
	 */
	private String bootStarttime;
	/**
	 * 局开始时间
	 */
	private String startTime;
	
	private String endTime;
	/**
	 * 结果游戏结果 结果 0庄 1闲 2和 3庄庄对 4庄闲对 5和庄对 6和闲对 7闲庄对 8闲闲对 9庄庄对闲对 10和庄对闲对 11闲庄对闲对
	 */
	private String result;
	
	/**
	 * 百家乐 状态 0没有开始 1新一靴 3新一局 4开始投注 5截止投注 6结算完成 7CLOSE。可扩展
	 */
	private Integer status;
	
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
	
	
	
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Long getRid() {
		return rid;
	}
	public void setRid(Long rid) {
		this.rid = rid;
	}
	public Integer getGameId() {
		return gameId;
	}
	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}
	public Integer getGameTableId() {
		return gameTableId;
	}
	public void setGameTableId(Integer gameTableId) {
		this.gameTableId = gameTableId;
	}
	public String getBootId() {
		return bootId;
	}
	public void setBootId(String bootId) {
		this.bootId = bootId;
	}
	public Integer getRoundNumber() {
		return roundNumber;
	}
	public void setRoundNumber(Integer roundNumber) {
		this.roundNumber = roundNumber;
	}
	public Integer getBootNumber() {
		return bootNumber;
	}
	public void setBootNumber(Integer bootNumber) {
		this.bootNumber = bootNumber;
	}
	public String getBootStarttime() {
		return bootStarttime;
	}
	public void setBootStarttime(String bootStarttime) {
		this.bootStarttime = bootStarttime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getBankCard1Mode() {
		return bankCard1Mode;
	}
	public void setBankCard1Mode(String bankCard1Mode) {
		this.bankCard1Mode = bankCard1Mode;
	}
	public Integer getBankCard1Number() {
		return bankCard1Number;
	}
	public void setBankCard1Number(Integer bankCard1Number) {
		this.bankCard1Number = bankCard1Number;
	}
	public String getBankCard2Mode() {
		return bankCard2Mode;
	}
	public void setBankCard2Mode(String bankCard2Mode) {
		this.bankCard2Mode = bankCard2Mode;
	}
	public Integer getBankCard2Number() {
		return bankCard2Number;
	}
	public void setBankCard2Number(Integer bankCard2Number) {
		this.bankCard2Number = bankCard2Number;
	}
	public String getBankCard3Mode() {
		return bankCard3Mode;
	}
	public void setBankCard3Mode(String bankCard3Mode) {
		this.bankCard3Mode = bankCard3Mode;
	}
	public Integer getBankCard3Number() {
		return bankCard3Number;
	}
	public void setBankCard3Number(Integer bankCard3Number) {
		this.bankCard3Number = bankCard3Number;
	}
	public String getPlayerCard1Mode() {
		return playerCard1Mode;
	}
	public void setPlayerCard1Mode(String playerCard1Mode) {
		this.playerCard1Mode = playerCard1Mode;
	}
	public Integer getPlayerCard1Number() {
		return playerCard1Number;
	}
	public void setPlayerCard1Number(Integer playerCard1Number) {
		this.playerCard1Number = playerCard1Number;
	}
	public String getPlayerCard2Mode() {
		return playerCard2Mode;
	}
	public void setPlayerCard2Mode(String playerCard2Mode) {
		this.playerCard2Mode = playerCard2Mode;
	}
	public Integer getPlayerCard2Number() {
		return playerCard2Number;
	}
	public void setPlayerCard2Number(Integer playerCard2Number) {
		this.playerCard2Number = playerCard2Number;
	}
	public String getPlayerCard3Mode() {
		return playerCard3Mode;
	}
	public void setPlayerCard3Mode(String playerCard3Mode) {
		this.playerCard3Mode = playerCard3Mode;
	}
	public Integer getPlayerCard3Number() {
		return playerCard3Number;
	}
	public void setPlayerCard3Number(Integer playerCard3Number) {
		this.playerCard3Number = playerCard3Number;
	}
	
	
	
}
