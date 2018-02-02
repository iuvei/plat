package com.na.roulette.socketserver.command.sendpara;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.sendpara.CommandResponse;

/**
 * 投注成功响应。
 * Created by sunny on 2017/5/3 0003.
 */
public class RouletteLeaveTableResponse extends CommandResponse {
	
	@JSONField(name = "loginName")
    private String loginName;
    @JSONField(name = "userType")
    private Integer userType;
    @JSONField(name = "loginId")
    private Long loginId;
    @JSONField(name = "rountId")
    private Integer rountId;
    @JSONField(name = "seat")
    private Integer seat;
    /**
     * 是否系统强制退出
     */
    @JSONField(name = "isForce")
    private boolean isForce;
    
    
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
	public Integer getRountId() {
		return rountId;
	}
	public void setRountId(Integer rountId) {
		this.rountId = rountId;
	}
	public Integer getSeat() {
		return seat;
	}
	public void setSeat(Integer seat) {
		this.seat = seat;
	}
	public boolean isForce() {
		return isForce;
	}
	public void setForce(boolean isForce) {
		this.isForce = isForce;
	}
    
    
    
}
