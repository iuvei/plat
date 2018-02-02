package com.na.baccarat.socketserver.command.sendpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.sendpara.CommandResponse;

import java.util.List;

public class TableStatusResponse extends CommandResponse {
	
	/**
	 * 卡片信息
	 */
	public class CardInfo {
		 @JSONField(name = "type")
		 public String type;
		 
		 @JSONField(name = "number")
		 public Integer number;
		 
		 @JSONField(name = "index")
		 public Integer index;

		/**
		 * 是否咪过.只发给荷官，普通用户不发.
		 */
		@JSONField(name="os")
		public boolean openStatus;
	}
	
	
	/**
	 * 卡牌信息
	 */
	private List<CardInfo> cardList;
	/**
	 * 局ID
	 */
	@JSONField
	private Long tableRid;
	/**
	 * 局状态
	 */
	@JSONField
	private Integer tableStatus;
	/**
	 * 桌id
	 */
	@JSONField
	private Integer tableTid;
	/**
	 * 靴ID
	 */
	@JSONField
	private String tableBid;
	/**
	 * 局数
	 */
	@JSONField
	private Integer tableNum;
	/**
	 * 荷官名字
	 */
	@JSONField
	private String dealerName;
	/**
	 * 游戏id
	 */
	@JSONField
	private Integer tableGid;

	/**
	 * 桌子名字
	 */
	@JSONField
	private String tableName;

	/**
	 * 倒计时
	 * @return
	 */
	@JSONField
	private Integer timeLeft;

	/**
	 * 桌子即时状态。
	 */
	@JSONField(name = "instantState")
	private Integer instantState;
	
	/**
	 * 桌子台红
	 */
	private String tableChips;
	
	@JSONField(name = "rounds")
	private List<String> rds;
	//桌子人数
	private Integer playerNumber;
	
	/**
	 * 桌子类型
	 */
	@JSONField(name = "tableType")
	private Integer tableType;
	
	/**
     * 桌子展示状态
     */
    private String showTableStatus;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Integer getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(Integer playerNumber) {
		this.playerNumber = playerNumber;
	}

	public List<String> getRds() {
		return rds;
	}
	public void setRds(List<String> rds) {
		this.rds = rds;
	}
	public List<CardInfo> getCardList() {
		return cardList;
	}
	public void setCardList(List<CardInfo> cardList) {
		this.cardList = cardList;
	}

	public Integer getTimeLeft() {
		return timeLeft;
	}
	public void setTimeLeft(Integer timeLeft) {
		this.timeLeft = timeLeft;
	}
	public Long getTableRid() {
		return tableRid;
	}
	public void setTableRid(Long tableRid) {
		this.tableRid = tableRid;
	}
	public Integer getTableTid() {
		return tableTid;
	}
	public void setTableTid(Integer tableTid) {
		this.tableTid = tableTid;
	}
	public String getTableBid() {
		return tableBid;
	}
	public void setTableBid(String tableBid) {
		this.tableBid = tableBid;
	}
	public Integer getTableNum() {
		return tableNum;
	}
	public void setTableNum(Integer tableNum) {
		this.tableNum = tableNum;
	}
	public String getDealerName() {
		return dealerName;
	}
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	public Integer getTableGid() {
		return tableGid;
	}
	public void setTableGid(Integer tableGid) {
		this.tableGid = tableGid;
	}

	public Integer getInstantState() {
		return instantState;
	}

	public void setInstantState(Integer instantState) {
		this.instantState = instantState;
	}

	public String getTableChips() {
		return tableChips;
	}

	public void setTableChips(String tableChips) {
		this.tableChips = tableChips;
	}

	public Integer getTableStatus() {
		return tableStatus;
	}

	public void setTableStatus(Integer tableStatus) {
		this.tableStatus = tableStatus;
	}

	public Integer getTableType() {
		return tableType;
	}

	public void setTableType(Integer tableType) {
		this.tableType = tableType;
	}

	public String getShowTableStatus() {
		return showTableStatus;
	}

	public void setShowTableStatus(String showTableStatus) {
		this.showTableStatus = showTableStatus;
	}
	
}
