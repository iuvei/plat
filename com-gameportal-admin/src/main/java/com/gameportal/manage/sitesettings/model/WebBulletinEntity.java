package com.gameportal.manage.sitesettings.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * 门户公告实体
 * @author Administrator
 *
 */
public class WebBulletinEntity extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7280602048731072260L;
	
	/**
	 * 主键
	 */
	private Long bid;
	
	/**
	 * 标题
	 */
	private String btitle;
	
	/**
	 * 内容
	 */
	private String bcontext;
	
	/**
	 * 状态
	 * 0：隐藏
	 * 1：显示
	 */
	private String status;
	
	/**
	 * 公告位置预留
	 */
	private String blocation;
	
	/**
	 * 编辑时间
	 */
	private String edittime;

	public Long getBid() {
		return bid;
	}

	public void setBid(Long bid) {
		this.bid = bid;
	}

	public String getBtitle() {
		return btitle;
	}

	public void setBtitle(String btitle) {
		this.btitle = btitle;
	}

	public String getBcontext() {
		return bcontext;
	}

	public void setBcontext(String bcontext) {
		this.bcontext = bcontext;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBlocation() {
		return blocation;
	}

	public void setBlocation(String blocation) {
		this.blocation = blocation;
	}

	public String getEdittime() {
		return edittime;
	}

	public void setEdittime(String edittime) {
		this.edittime = edittime;
	}

	@Override
	public Serializable getID() {
		return this.getBid();
	}

}
