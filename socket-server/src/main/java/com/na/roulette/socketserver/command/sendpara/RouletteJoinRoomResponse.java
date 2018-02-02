package com.na.roulette.socketserver.command.sendpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.sendpara.CommandResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 * 加入房间返回参数
 * 
 * @author alan
 * @date 2017年5月1日 下午12:26:08
 */
public class RouletteJoinRoomResponse extends CommandResponse {
	
	/**
	 * 玩法信息
	 */
	public class PlayInfo {
		
		/**
	     * 玩法Id
	     */
	    public Integer id;
	    /**
	     * 玩法名称
	     */
	    public String name;
		/**
	     * 交易项列表
	     */
	    @JSONField(name = "tradeList")
	    public List<TradeItemInfo> tradeList;
	}
	
	/**
	 * 交易项
	 */
	public class TradeItemInfo {
		/**
	     * 交易项ID
	     */
	    @JSONField(name = "id")
	    public Integer id;
		/**
	     * 交易项对应投注区标识
	     */
	    @JSONField(name = "key")
	    public String key;
		/**
	     * 名称
	     */
	    @JSONField(name = "name")
	    public String name;
		/**
	     * 赔率
	     */
	    @JSONField(name = "rate")
	    public Double rate;
	    /**
	     * 数字
	     */
	    public String number;
	}
	
	/**
	 * 路子信息
	 */
	public class RoundInfo {
		/**
	     * 结果
	     */
	    @JSONField(name = "result")
	    public String result;
	}
	/**
	 * 用户下注信息
	 */
	public class UserBetInfo {
		/**
	     * 交易项key
	     */
	    @JSONField(name = "tradeId")
	    public Integer tradeId;
		/**
	     * 下注数量
	     */
	    @JSONField(name = "number")
	    public BigDecimal number;
	}
	
	/**
     * 昵称
     */
    @JSONField(name = "nickName")
    private String nickName;

	private Integer chipId;
    
    /**
     * 实体桌ID
     */
    @JSONField(name = "tid")
    private Integer tableId;
    /**
     * 当前桌玩法列表
     */
    @JSONField(name = "playList")
    private List<PlayInfo> playList;
    
    /**
     * 当前桌详细路子信息
     */
    @JSONField(name = "roundList")
    private List<RoundInfo> roundList;
    /**
     * 当前局状态
     */
    @JSONField(name = "roundStatus")
    private Integer roundStatus;
    /**
     * 交易项
     */
    @JSONField(name = "tradeItemList")
    private List<TradeItemInfo> tradeItemList;
    /**
     * 倒计时
     */
    @JSONField(name = "cd")
    private Integer countDown;
    /**
     * 用户下注数量
     */
    private List<UserBetInfo> userBetList;
    /**
     * 倒计时
     */
    @JSONField(name = "isQuickChange")
    private boolean isQuickChange;

	public Integer getChipId() {
		return chipId;
	}

	public void setChipId(Integer chipId) {
		this.chipId = chipId;
	}

	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public List<PlayInfo> getPlayList() {
		return playList;
	}
	public void setPlayList(List<PlayInfo> playList) {
		this.playList = playList;
	}
	public List<RoundInfo> getRoundList() {
		return roundList;
	}
	public void setRoundList(List<RoundInfo> roundList) {
		this.roundList = roundList;
	}
	public Integer getRoundStatus() {
		return roundStatus;
	}
	public void setRoundStatus(Integer roundStatus) {
		this.roundStatus = roundStatus;
	}
	public List<TradeItemInfo> getTradeItemList() {
		return tradeItemList;
	}
	public void setTradeItemList(List<TradeItemInfo> tradeItemList) {
		this.tradeItemList = tradeItemList;
	}
	public Integer getCountDown() {
		return countDown;
	}
	public void setCountDown(Integer countDown) {
		this.countDown = countDown;
	}
	public List<UserBetInfo> getUserBetList() {
		return userBetList;
	}
	public void setUserBetList(List<UserBetInfo> userBetList) {
		this.userBetList = userBetList;
	}
	public Integer getTableId() {
		return tableId;
	}
	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}
	public boolean isQuickChange() {
		return isQuickChange;
	}
	public void setQuickChange(boolean isQuickChange) {
		this.isQuickChange = isQuickChange;
	}
    
}
