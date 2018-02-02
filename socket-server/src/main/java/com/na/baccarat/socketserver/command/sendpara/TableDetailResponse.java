package com.na.baccarat.socketserver.command.sendpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.sendpara.CommandResponse;

public class TableDetailResponse extends CommandResponse{
	
	@JSONField(name = "tid")
	private Integer tableId;
	@JSONField(name = "tstu")
	private Integer tableStatus; 
	@JSONField(name = "tbet")
	private Long tableBetTime; 
	@JSONField(name = "rtab")
	private String roundIdTableId; 
	@JSONField(name = "tRes")
	private String tableResult;
	
	public Integer getTableId() {
		return tableId;
	}
	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}
	public Integer getTableStatus() {
		return tableStatus;
	}
	public void setTableStatus(Integer tableStatus) {
		this.tableStatus = tableStatus;
	}
	public Long getTableBetTime() {
		return tableBetTime;
	}
	public void setTableBetTime(Long tableBetTime) {
		this.tableBetTime = tableBetTime;
	}
	public String getRoundIdTableId() {
		return roundIdTableId;
	}
	public void setRoundIdTableId(String roundIdTableId) {
		this.roundIdTableId = roundIdTableId;
	}
	public String getTableResult() {
		return tableResult;
	}
	public void setTableResult(String tableResult) {
		this.tableResult = tableResult;
	} 
	
	
	
	

}
