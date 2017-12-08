package com.gameportal.web.user.model;

import java.io.Serializable;

/**
 * 会员签到。
 * 
 * @author add by sunshine 2016.07.22
 *
 */
public class UserSign extends BaseEntity {
	private static final long serialVersionUID = 1L;

	private long sid;

	private long uiid;

	private String account;

	private long puid;

	private String createtime;

	private Integer status;

	public long getSid() {
		return sid;
	}

	public void setSid(long sid) {
		this.sid = sid;
	}

	public long getUiid() {
		return uiid;
	}

	public void setUiid(long uiid) {
		this.uiid = uiid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public long getPuid() {
		return puid;
	}

	public void setPuid(long puid) {
		this.puid = puid;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public Serializable getID() {
		return this.sid;
	}
}
