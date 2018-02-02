package com.na.gate.entity;

import java.util.Date;

import com.na.gate.util.DateUtil;

/**
 * @author Andy
 * @version 创建时间：2017年12月11日 下午7:27:26
 */
public class AccountSyncRecord {
	private Long userId;
	private Long id;
	private String syncTime=DateUtil.date2Str(new Date());
	
	public AccountSyncRecord(){}
	
	public AccountSyncRecord(Long userId, Long id, String syncTime) {
		super();
		this.userId = userId;
		this.id = id;
		this.syncTime = syncTime;
	}

	public AccountSyncRecord(Long userId, Long id) {
		super();
		this.userId = userId;
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(String syncTime) {
		this.syncTime = syncTime;
	}
}
