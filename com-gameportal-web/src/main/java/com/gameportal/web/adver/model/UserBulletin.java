package com.gameportal.web.adver.model;

import java.io.Serializable;

import com.gameportal.web.user.model.BaseEntity;

/**
 * 用户公告。
 * 
 * @author Administrator
 *
 */
public class UserBulletin extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private long rid;

	private String bid;

	private long userid;

	public long getRid() {
		return rid;
	}

	public void setRid(long rid) {
		this.rid = rid;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	@Override
	public Serializable getID() {
		return this.rid;
	}
}
