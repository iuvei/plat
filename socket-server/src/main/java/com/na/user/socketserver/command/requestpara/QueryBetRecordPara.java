package com.na.user.socketserver.command.requestpara;


/**
 * 查询投注记录参数
 * 
 * @author alan
 * @date 2017年5月24日 上午11:50:40
 */
public class QueryBetRecordPara extends CommandReqestPara {

    private Integer size;
    
    private Integer rowsNumber;
    
    private String beginDate;
    
    private String endDate;

	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public Integer getRowsNumber() {
		return rowsNumber;
	}
	public void setRowsNumber(Integer rowsNumber) {
		this.rowsNumber = rowsNumber;
	}
    
}
