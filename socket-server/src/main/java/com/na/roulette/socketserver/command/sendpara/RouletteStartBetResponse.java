package com.na.roulette.socketserver.command.sendpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.sendpara.CommandResponse;


/**
 * 下注响应参数
 * 
 * @author alan
 * @date 2017年5月2日 下午12:29:51
 */
public class RouletteStartBetResponse extends CommandResponse {
	
	
	/**
	 * 
	 */
    @JSONField(name = "tid")
    private Integer tableId;
    /**
     * 下注倒计时
     */
    @JSONField(name = "cd")
    private Integer countDown;
    
    /**
     * 桌子展示状态
     */
    private String showTableStatus;
    
    
	public Integer getTableId() {
		return tableId;
	}
	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}
	public Integer getCountDown() {
		return countDown;
	}
	public void setCountDown(Integer countDown) {
		this.countDown = countDown;
	}
	public String getShowTableStatus() {
		return showTableStatus;
	}
	public void setShowTableStatus(String showTableStatus) {
		this.showTableStatus = showTableStatus;
	}
    
}
