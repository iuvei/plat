package com.na.baccarat.socketserver.command.sendpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.sendpara.IResponse;

/**
 * Created by Administrator on 2017/4/28 0028.
 */
public class ShuffleResponse implements IResponse {
	
	
    @JSONField(name = "tableId")
    private Integer tableId;
    
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
	public String getShowTableStatus() {
		return showTableStatus;
	}
	public void setShowTableStatus(String showTableStatus) {
		this.showTableStatus = showTableStatus;
	}
    

}