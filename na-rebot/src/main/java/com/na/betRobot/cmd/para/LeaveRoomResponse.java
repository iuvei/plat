package com.na.betRobot.cmd.para;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 离开房间返回参数
 * 
 * @author alan
 * @date 2017年5月1日 下午12:26:08
 */
public class LeaveRoomResponse {
	
    @JSONField(name = "loginName")
    private String loginName;
    @JSONField(name = "userType")
    private Integer userType;
    @JSONField(name = "loginId")
    private Long loginId;
    @JSONField(name = "tableId")
    private Integer tableId;
    @JSONField(name = "rountId")
    private Long rountId;
    @JSONField(name = "seat")
    private Integer seat;
    @JSONField(name = "uid")
    private Long userId;
    /**
     * 荷官离开桌子时返回, 前端展示需要
     */
    @JSONField(name = "tableName")
    private String tableName;
    
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public Long getLoginId() {
		return loginId;
	}
	public void setLoginId(Long loginId) {
		this.loginId = loginId;
	}
	public Long getRountId() {
		return rountId;
	}
	public void setRountId(Long rountId) {
		this.rountId = rountId;
	}
	public Integer getSeat() {
		return seat;
	}
	public void setSeat(Integer seat) {
		this.seat = seat;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Integer getTableId() {
		return tableId;
	}
	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}
   
    
    
}
