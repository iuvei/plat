package com.na.manager.bean.vo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author andy
 * @date 2017年6月23日 下午3:23:28
 * 
 */
public class LoginLogVO {
	private Long userId;
	private String loginName;
	private String nickName;
	private String parentPath;
	// 0 成功 1失败
	private Integer loginStatus;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date loginDate;
	private String ipAddr;
	// 1 系统用户 2 荷官用户 3 子账号 4 真人用户
	private Integer userType;
	// 1 代理 2 会员 3 总代理
	private Integer type;
	// 描述信息
	private String remark;
	// 1 WEB 2 IOS 3 ANDROID
	private Integer deviceType;
	// 设备信息
	private String deviceInfo;

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

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	public Integer getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(Integer loginStatus) {
		this.loginStatus = loginStatus;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
}
