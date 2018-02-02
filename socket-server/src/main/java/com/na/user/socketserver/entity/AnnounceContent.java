package com.na.user.socketserver.entity;

/**
 * 公告内容
 * @author Administrator
 *
 */
public class AnnounceContent {

	private Long id;
	private String contentTitle;
	private String contentDesc;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContentTitle() {
		return contentTitle;
	}
	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}
	public String getContentDesc() {
		return contentDesc;
	}
	public void setContentDesc(String contentDesc) {
		this.contentDesc = contentDesc;
	}
	
	
	
}
