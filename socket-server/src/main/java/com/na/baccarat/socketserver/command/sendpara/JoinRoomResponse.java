package com.na.baccarat.socketserver.command.sendpara;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.baccarat.socketserver.common.enums.BetOrderSourceEnum;
import com.na.user.socketserver.command.sendpara.IResponse;
import com.na.user.socketserver.common.enums.VirtualGameTableType;

/**
 * 加入房间返回参数
 * 
 * @author alan
 * @date 2017年5月1日 下午12:26:08
 */
public class JoinRoomResponse implements IResponse,Serializable {
	/**
	 * 返回的用户信息
	 */
	public class UserInfo implements Serializable {
		/**
	     * 用户ID
	     */
	    @JSONField(name = "userId")
	    public Long userId;
	    /**
	     * 座位号
	     */
	    @JSONField(name = "seat")
	    public Integer seat;
	    /**
	     * 国籍代码
	     */
	    @JSONField(name = "countryCode")
	    public String countryCode;
	    /**
	     * 用户昵称
	     */
	    @JSONField(name = "nickName")
	    public String nickName;
	    /**
	     * 用户余额
	     */
	    @JSONField(name = "balance")
	    public Double balance;
	    
	    @JSONField(name = "userPicture")
	    public String userPicture;
    }
	
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
		/**
		 * 卡片信息
		 */
	    @JSONField(name = "cardList")
	    public List<CardInfo> cardList;
	}
	/**
	 * 卡片信息
	 */
	public class CardInfo {
		/**
	     * 花色
	     */
	    @JSONField(name = "type")
	    public String type;
		/**
	     * 点数
	     */
	    @JSONField(name = "number")
	    public Integer number;
		/**
	     * 赔率
	     */
	    @JSONField(name = "index")
	    public Integer index;
	}
	/**
	 * 用户下注信息
	 */
	public class UserBetInfo implements Serializable {
		/**
	     * 用户Id
	     */
	    @JSONField(name = "userId")
		public Long userId;
	    /**
	     * 桌子Id(多台使用)
	     */
	    @JSONField(name = "tableId")
		public Integer tableId;
	    /**
	     * 下注详情
	     */
	    @JSONField(name = "betList")
	    public List<UserBetDetailInfo> betList;
	}
	/**
	 * 用户下注信息
	 */
	public class UserBetDetailInfo implements Serializable {
		/**
	     * 交易项key
	     */
	    @JSONField(name = "item")
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
    
    /**
     * 限红ID
     */
    private Integer chipId;
    
    /**
     * 实体桌ID
     */
    @JSONField(name = "tid")
    private Integer tableId;
    /**
     * 9,多台,
	 * 1,咪牌普通桌,
	 * 2,不咪牌普通桌,
	 * 3,咪牌竞咪桌",
	 * 0,未定义",
     * 实体桌类型    @see com.na.baccarat.socketserver.common.enums.GameTableTypeEnum
     */
    @JSONField(name = "tType")
    private Integer tableType;
    /**
     * 虚拟桌ID
     */
    @JSONField(name = "vtid")
    private Integer virtualTableId;
    /**
     * 虚拟桌类型  1为普通  2为代理VIP
     */
    @JSONField(name = "vtType")
    private Integer virtualTableType;
    /**
     * 当前桌用户
     */
    @JSONField(name = "seatList")
    private List<UserInfo> seatList;
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
     * 座位
     */
    @JSONField(name = "seatNum")
    private Integer seatNum;
    /**
     * 是否快速换桌
     */
    @JSONField(name = "isQuickChange")
    private Boolean isQuickChange;
      
    /**
     * 加入座位类型(默认为1)
     * 1为进座位   2为旁注  3为多台
     */
    private int source;

    /**
     * 用户下注数量
     */
    private List<UserBetInfo> userBetList;
    
    
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public void setSourceEnum(BetOrderSourceEnum source) {
		this.source = source.get();
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public List<UserInfo> getSeatList() {
		return seatList;
	}
	public void setSeatList(List<UserInfo> seatList) {
		this.seatList = seatList;
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
	public Integer getSeatNum() {
		return seatNum;
	}
	public void setSeatNum(Integer seatNum) {
		this.seatNum = seatNum;
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
	public Integer getVirtualTableId() {
		return virtualTableId;
	}
	public void setVirtualTableId(Integer virtualTableId) {
		this.virtualTableId = virtualTableId;
	}
	public Boolean isQuickChange() {
		return isQuickChange;
	}
	public void setQuickChange(Boolean isQuickChange) {
		this.isQuickChange = isQuickChange;
	}
	public Integer getChipId() {
		return chipId;
	}
	public void setChipId(Integer chipId) {
		this.chipId = chipId;
	}
	public Integer getVirtualTableType() {
		return virtualTableType;
	}
	@JSONField(serialize = false,deserialize = false)
	public VirtualGameTableType getVirtualTableTypeEnum() {
		if(virtualTableType == null) {
			return null;
		}
		return VirtualGameTableType.get(virtualTableType);
	}
	public void setVirtualTableType(Integer virtualTableType) {
		this.virtualTableType = virtualTableType;
	}
	public void setVirtualTableTypeEnum(VirtualGameTableType virtualTableType) {
		this.virtualTableType = virtualTableType.get();
	}
	public Integer getTableType() {
		return tableType;
	}
	public void setTableType(Integer tableType) {
		this.tableType = tableType;
	}
    
}
