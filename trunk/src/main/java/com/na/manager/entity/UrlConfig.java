package com.na.manager.entity;

import java.io.Serializable;

/**
 * 服务地址
 * 
 * @author Andy
 * @version 创建时间：2017年9月14日 上午11:40:29
 */
public class UrlConfig implements Serializable{
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private Long id;

	/**
	 * key
	 */
	private String key;

	/**
	 * 分组
	 */
	private String group;

	/**
	 * url地址
	 */
	private String url;

	/**
	 * 描述
	 */
	private String remark;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
