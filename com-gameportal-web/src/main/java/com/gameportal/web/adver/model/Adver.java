package com.gameportal.web.adver.model;

import java.io.Serializable;
import java.util.Date;

import com.gameportal.web.user.model.BaseEntity;

/**
 * 广告实体类。
 * 
 * @author sum
 *
 */
public class Adver extends BaseEntity {
	private static final long serialVersionUID = 1L;
	public static final String TABLE_ALIAS = "Adver";
	public static final String ALIAS_ID = "广告编号";
	public static final String ALIAS_TITLE = "广告标题";
	public static final String ALIAS_IMAGES = "图片文字";
	public static final String ALIAS_HREF = "链接地址";
	public static final String ALIAS_HEIGHT = "高度";
	public static final String ALIAS_WIDTH = "宽度";
	public static final String ALIAS_COLOR = "广告颜色值";
	public static final String ALIAS_STATUS = "状态0：隐藏 1：显示";
	public static final String ALIAS_SEQUENCE = "排序";
	public static final String ALIAS_LOCATION = "广告位置";
	public static final String ALIAS_EDITTIME = "发布时间";

	private long id;
	private String title;
	private String images;
	private String href;
	private String height;
	private String width;
	private String color;
	private Integer status;
	private Integer sequence;
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

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
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
		return this.getId();
	}
}
