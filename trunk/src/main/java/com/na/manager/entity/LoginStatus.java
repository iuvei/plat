package com.na.manager.entity;

import java.io.Serializable;

/**
 * @author java
 * @time 2015-07-21 18:05:58
 */
public class LoginStatus implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 游戏主键。 用户ID + 登录类型 可以确定用户唯一。 用户多态登录
	 */
	private Long id;
	private Long uid;
	private Integer loginType;
	/**
	 * 游戏ID
	 */
	private Integer gid;
	
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
	public Integer getGid() {
		return gid;
	}
	public void setGid(Integer gid) {
		this.gid = gid;
	}

}