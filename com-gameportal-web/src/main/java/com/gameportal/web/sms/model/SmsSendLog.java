package com.gameportal.web.sms.model;

import java.io.Serializable;
import java.util.Date;

import com.gameportal.web.user.model.BaseEntity;

/**
 * 短信发送日志。
 * 
 * @author sum
 *
 */
@SuppressWarnings("all")
public class SmsSendLog extends BaseEntity {
	private final static String TABLE_ALIAS = "SmsSendLog";
	private final static String ALIAS_SPSID = "日志ID";
	private final static String ALIAS_SPAID = "短信平台ID";
	private final static String ALIAS_USERNAME = "用户名";
	private final static String ALIAS_MOBILE = "手机号码";
	private final static String ALIAS_CONTENT = "发送内容";
	private final static String ALIAS_SENDTIME = "发送时间";
	private final static String ALIAS_TYPE = "类型 1：提款 2：找回密码";

	private Long spsid;
	private Long spaid;
	private String username;
	private String mobile;
	private String content;
	private Date sendtime = new Date();
	private Integer type;
	private String startTime;
	private String endTime;

	public Long getSpsid() {
		return spsid;
	}

	public void setSpsid(Long spsid) {
		this.spsid = spsid;
	}

	public Long getSpaid() {
		return spaid;
	}

	public void setSpaid(Long spaid) {
		this.spaid = spaid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getSendtime() {
		return sendtime;
	}

	public void setSendtime(Date sendtime) {
		this.sendtime = sendtime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Override
	public Serializable getID() {
		return this.getSpsid();
	}
}
