package com.na.user.socketserver.entity;

/**
 * 用户公告
 * @author Administrator
 *
 */
public class UserAnnounce {

	private Long id;
	private String userId;
	private String userName;
	private Long contentId;
	private Integer type;
	private AnnounceContent announceContent;
	
	
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
	public AnnounceContent getAnnounceContent() {
		return announceContent;
	}
	public void setAnnounceContent(AnnounceContent announceContent) {
		this.announceContent = announceContent;
	}
	
	
}
