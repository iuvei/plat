package com.na.manager.bean;
/**
 * 游戏桌输赢
 * @author andy
 * @date 2017年6月23日 上午10:08:14
 * 
 */
public class TableWinLostReportRequest  extends PageCondition{
	private String startTime;
	private String endTime;
	private String tableId;
	private String bootsId;
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getTableId() {
		return tableId;
	}
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	public String getBootsId() {
		return bootsId;
	}
	public void setBootsId(String bootsId) {
		this.bootsId = bootsId;
	}
}
