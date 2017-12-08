package com.gameportal.web.adver.model;

import java.io.Serializable;
import java.util.Date;

import com.gameportal.web.user.model.BaseEntity;

/**
 * 公告实体类。
 * 
 * @author sum
 *
 */
public class Bulletin extends BaseEntity {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_ALIAS = "Bulletin";
	public static final String ALIAS_ID = "公告编号";
	public static final String ALIAS_TITLE = "公告标题";
	public static final String ALIAS_CONTENT = "公告内容";
	public static final String ALIAS_STATUS = "状态0：隐藏 1：显示";
	public static final String ALIAS_LOCALTION = "公告位置";
	public static final String ALIAS_EDITTIME = "发布时间";

	private long id;
	private String title;
	private String content;
	private Integer status;
	private String location;
	private Date editTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getEditTime() {
		return editTime;
	}

	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}

	@Override
	public Serializable getID() {
		return this.id;
	}
}
