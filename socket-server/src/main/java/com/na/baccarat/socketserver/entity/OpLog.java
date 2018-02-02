package com.na.baccarat.socketserver.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author java
 * @time 2015-07-21 18:05:58
 */
public class OpLog implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 自动增长
	 */
	private Long id;
	/**
	 * 用户ID
	 */
	private Long uid;
	/**
	 * 登录类型
	 */
	private Integer loginType;
	/**
	 * IP地址
	 */
	private String ipAddr;
	/**
	 * 操作时间
	 */
	private Date loginDate;
	/**
	 * 操作事件
	 */
	private String action;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Integer getLoginType() {
		return loginType;
	}

	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}