package com.na.roulette.socketserver.command.sendpara;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

public class RouletteTableStatusResponse {
	
	/**
	 * 局ID
	 */
	@JSONField
	private Long tableRid;
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
	 * 桌子台红
	 */
	private String tableChips;

	/**
	 * 倒计时
	 * @return
	 */
	@JSONField
	private Long tableTime;

	/**
	 * 桌子即时状态。
	 */
	@JSONField(name = "instantState")
	private Integer instantState;

	private String tableName;
	
	private List<String> results = new ArrayList<>(10);
	
	/**
     * 桌子展示状态
     */
    private String showTableStatus;
    
    /**
	 * 桌子状态
	 */
	@JSONField(name = "tableState")
	private Integer tableState;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<String> getResults() {
		return results;
	}
	public void setResults(List<String> results) {
		this.results = results;
	}
	public Long getTableTime() {
		return tableTime;
	}
	public void setTableTime(Long tableTime) {
		this.tableTime = tableTime;
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

	public String getShowTableStatus() {
		return showTableStatus;
	}

	public void setShowTableStatus(String showTableStatus) {
		this.showTableStatus = showTableStatus;
	}

	public Integer getTableState() {
		return tableState;
	}

	public void setTableState(Integer tableState) {
		this.tableState = tableState;
	}
	
	

}
