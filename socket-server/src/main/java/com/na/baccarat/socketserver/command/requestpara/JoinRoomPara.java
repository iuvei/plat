package com.na.baccarat.socketserver.command.requestpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.baccarat.socketserver.common.enums.BetOrderSourceEnum;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;

/**
 * 加入房间请求参数
 * 
 * @author alan
 * @date 2017年4月29日 下午4:37:36
 */
public class JoinRoomPara extends CommandReqestPara {
	
	/**
     * 实体桌ID
     */
    @JSONField(name = "tid")
    private Integer tableId;
    
    /**
     * 虚拟桌ID
     */
    @JSONField(name = "vtid")
    private Integer virtualTableId;
    
    /**
     * 限红ID
     */
    private Integer chipId;
    
    /**
     * 加入座位类型(默认为1)
     * 1为进座位   2为旁注  3为多台
     */
    private Integer source;
    
    /**
     * 加入房间密码
     */
    @JSONField(name = "password")
    private String password;
    
    /**
     * 是否快速换桌
     */
    @JSONField(serialize=false)
    private boolean isQuickChangeRoom;

    
	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

	public Integer getVirtualTableId() {
		return virtualTableId;
	}

	public void setVirtualTableId(Integer virtualTableId) {
		this.virtualTableId = virtualTableId;
	}

	public Integer getChipId() {
		return chipId;
	}

	public void setChipId(Integer chipId) {
		this.chipId = chipId;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}
	
	public BetOrderSourceEnum getSourceEnum() {
		if(this.source != null) {
			return BetOrderSourceEnum.get(this.source);
		}
		return null;
	}

	public boolean isQuickChangeRoom() {
		return isQuickChangeRoom;
	}

	public void setQuickChangeRoom(boolean isQuickChangRoom) {
		this.isQuickChangeRoom = isQuickChangRoom;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
    
    
}
