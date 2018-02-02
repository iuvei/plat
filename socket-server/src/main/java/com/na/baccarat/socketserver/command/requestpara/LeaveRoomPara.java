package com.na.baccarat.socketserver.command.requestpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;

/**
 * 离开房间请求参数
 * 
 * @author alan
 * @date 2017年4月29日 下午4:37:36
 */
public class LeaveRoomPara extends CommandReqestPara {
	
	
	/**
     * 桌ID
     */
    @JSONField(name = "tid")
    private Integer tableId;
    
    /**
     * 虚拟桌ID
     */
    @JSONField(name = "vtid")
    private Integer virtualTableId;
    
    /**
     * 离开房间类型(默认为1)
     * 1为座位   2为旁注  3为多台
     */
    @JSONField(name = "type")
    private String type;
    
    /**
     * 是否快速换桌
     */
    @JSONField(serialize=false)
    private boolean isQuickChangeRoom;
    
    /**
     * 是否重连
     */
    @JSONField(serialize=false)
    private boolean isReconnect;
    
    /**
     * 是否系统强制退出房间
     */
    @JSONField(serialize=false)
    private boolean isForce;

	public Integer getVirtualTableId() {
		return virtualTableId;
	}

	public void setVirtualTableId(Integer virtualTableId) {
		this.virtualTableId = virtualTableId;
	}

	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
