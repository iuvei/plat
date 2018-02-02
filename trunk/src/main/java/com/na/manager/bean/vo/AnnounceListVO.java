package com.na.manager.bean.vo;

/**
 * @author andy
 * @date 2017年6月27日 上午10:21:04
 * 
 */
public class AnnounceListVO {
	private Long id;
	private Long announceId;
	private String createTime;
	private String userName;
	private String conentDesc;
	private String updateBy;
	private String updateTime;
	private Integer type;
	private String contentTitle;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAnnounceId() {
		return announceId;
	}

	public void setAnnounceId(Long announceId) {
		this.announceId = announceId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getConentDesc() {
		return conentDesc;
	}

	public void setConentDesc(String conentDesc) {
		this.conentDesc = conentDesc;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getContentTitle() {
		return contentTitle;
	}

	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}
}
