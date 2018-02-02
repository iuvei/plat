package com.na.manager.entity;

import java.io.Serializable;

/**
 * 公告内容
 * 
 * @author Administrator
 *
 */
public class AnnounceContent implements Serializable {

	private static final long serialVersionUID = 993929611763894998L;

	private Long id;
	private String contentDesc;
	private String contentTitle;
	
	private String loginName;
	private Integer type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getContentDesc() {
		return contentDesc;
	}

	public void setContentDesc(String contentDesc) {
		this.contentDesc = contentDesc;
	}

	public String getContentTitle() {
		return contentTitle;
	}

	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
