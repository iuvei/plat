package com.na.baccarat.socketserver.command.sendpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.sendpara.IResponse;


/**
 * 下注响应参数
 * 
 * @author alan
 * @date 2017年5月2日 下午12:29:51
 */
public class StartBetResponse implements IResponse {
	
	
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

    private Integer tableStatus;
    
    /**
     * 桌子展示状态
     */
    private String showTableStatus;

	public Integer getTableStatus() {
		return tableStatus;
	}

	public void setTableStatus(Integer tableStatus) {
		this.tableStatus = tableStatus;
	}
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
