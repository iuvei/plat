package com.na.baccarat.socketserver.command.sendpara;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.sendpara.IResponse;

/**
 * 加入房间返回参数
 * 
 * @author alan
 * @date 2017年5月1日 下午12:26:08
 */
public class OtherJoinRoomResponse implements IResponse {
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
     * 当前桌用户
     */
    @JSONField(name = "seatList")
    private List<JoinRoomResponse.UserInfo> seatList;
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
	public List<JoinRoomResponse.UserInfo> getSeatList() {
		return seatList;
	}
	public void setSeatList(List<JoinRoomResponse.UserInfo> seatList) {
		this.seatList = seatList;
	}
	
    
	
    
}
