package com.na.baccarat.socketserver.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author java
 * @time 2015-07-21 18:05:58
 */
public class PhoneBetterUser implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 语音用户ID
	 */
	private Long phoneUserUid;
	/**
	 * 投注员ID
	 */
	private Long betterUserUid;
	/**
	 * 状态 0未关联 1已关联，2已结束
	 */
	private int status;
	/**
	 * 创建时间
	 */
	protected Date createDatetime;
	/**
	 * 创建人
	 */
	protected String createBy;

	public Long getPhoneUserUid() {
		return phoneUserUid;
	}

	public void setPhoneUserUid(Long phoneUserUid) {
		this.phoneUserUid = phoneUserUid;
	}

	public Long getBetterUserUid() {
		return betterUserUid;
	}

	public void setBetterUserUid(Long betterUserUid) {
		this.betterUserUid = betterUserUid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

}