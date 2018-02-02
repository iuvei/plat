package com.na.roulette.socketserver.command.requestpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;

/**
 * 离开房间请求参数
 * 
 * @author alan
 * @date 2017年4月29日 下午4:37:36
 */
public class LeaveTablePara extends CommandReqestPara {
	
    @JSONField(name = "tid")
    private Integer tableId;
    
    @JSONField(serialize=false)
    private boolean isQuickChangeRoom;
    
    /**
     * 是否重连
     */
    @JSONField(serialize=false)
    private boolean isReconnect;
    /**
     * 是否系统强制退出
     */
    @JSONField(serialize=false)
    private boolean isForce;

	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

	public boolean isQuickChangeRoom() {
		return isQuickChangeRoom;
	}

	public void setQuickChangeRoom(boolean isQuickChangeRoom) {
		this.isQuickChangeRoom = isQuickChangeRoom;
	}

	public boolean isReconnect() {
		return isReconnect;
	}

	public void setReconnect(boolean isReconnect) {
		this.isReconnect = isReconnect;
	}

	public boolean isForce() {
		return isForce;
	}

	public void setForce(boolean isForce) {
		this.isForce = isForce;
	}
	
	
	
    
    
}
