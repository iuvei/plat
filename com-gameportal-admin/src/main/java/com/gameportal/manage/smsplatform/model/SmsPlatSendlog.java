package com.gameportal.manage.smsplatform.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * 短信平台发送日志
 * @author Administrator
 *
 */
public class SmsPlatSendlog extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2566236328807278718L;

	/**
	 * 主键
	 */
	private Long spsid;

	/**
	 * 短信平台ID
	 */
	private Long spaid;
	
	/**
	 * 接收短信会员账号
	 */
	private String username;
	
	/**
	 * 手机号码
	 */
	private String mobile;
	
	/**
	 * 短信内容
	 */
	private String content;
	
	/**
	 * 用途
	 * 1:提款
	 * 2:找回密码
	 */
	private int type;
	
	/**
	 * 发送时间
	 */
	private String sendtime;
	
	/**
	 * 短信平台服务地址
	 */
	private String servername;
	

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getSendtime() {
		return sendtime;
	}

	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}


	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

	@Override
	public String toString() {
		return "SmsPlatSendlog [spsid=" + spsid + ", spaid=" + spaid
				+ ", username=" + username + ", mobile=" + mobile
				+ ", content=" + content + ", type=" + type + ", sendtime="
				+ sendtime + "]";
	}



	@Override
	public Serializable getID() {

		return this.spsid;
	}
}
