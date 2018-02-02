package com.na.roulette.socketserver.command.requestpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;

/**
 * 轮盘加入桌子请求参数
 * 
 * @author alan
 * @date 2017年5月12日 上午10:25:34
 */
public class JoinTablePara extends CommandReqestPara {
	
	/**
     * 桌子ID
     */
    @JSONField(name = "tid")
    private Integer tableId;
    
    /**
     * 限红ID
     */
    private Integer chipId;
    
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

	public Integer getChipId() {
		return chipId;
	}

	public void setChipId(Integer chipId) {
		this.chipId = chipId;
	}

	public boolean isQuickChangeRoom() {
		return isQuickChangeRoom;
	}

	public void setQuickChangeRoom(boolean isQuickChangeRoom) {
		this.isQuickChangeRoom = isQuickChangeRoom;
	}
    
	
}
