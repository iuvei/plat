package com.na.test.batchbet.para;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.test.batchbet.common.CommandResponse;


/**
 * 下注响应参数
 * 
 * @author alan
 * @date 2017年5月2日 下午12:29:51
 */
public class StartBetResponse {
	
	
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
    
}
