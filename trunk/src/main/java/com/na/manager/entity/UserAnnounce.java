package com.na.manager.entity;

import java.io.Serializable;

/**
 * 用户公告
 * 
 * @author Administrator
 *
 */
public class UserAnnounce extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String userId;
	private String userName;
	private Long contentId;
	/**
	 * 1为代理团队,2为代理直线
	 */
	private Integer type;
	
	private String announceDesc;
	private String announceTitle;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getAnnounceDesc() {
		return announceDesc;
	}

	public void setAnnounceDesc(String announceDesc) {
		this.announceDesc = announceDesc;
	}

	public String getAnnounceTitle() {
		return announceTitle;
	}

	public void setAnnounceTitle(String announceTitle) {
		this.announceTitle = announceTitle;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
