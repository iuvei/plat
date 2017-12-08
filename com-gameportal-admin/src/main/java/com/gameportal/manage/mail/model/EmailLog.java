package com.gameportal.manage.mail.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * Email邮件记录
 * @author Administrator
 *
 */
public class EmailLog extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7711129283262712139L;
	
	private Long mailid;
	
	/**
	 * 邮件所属用户ID
	 */
	private Long uuid;
	
	/**
	 * 邮件服务器地址
	 */
	private String mailServerHost;
	
	/**
	 * 邮件端口
	 */
	private String mailServerPort;
	
	/**
	 * 发件人E-mail地址
	 */
	private String fromAddress;
	
	/**
	 * 收件人Email地址
	 */
	private String toAddress;
	
	/**
	 * 标题
	 */
	private String subject;
	
	/**
	 * 内容
	 */
	private String content;
	
	/**
	 * 邮件创建时间
	 */
	private String createDate;
	
	/**
	 * 邮件发送时间
	 */
	private String sendDate;
	
	/**
	 * 邮件状态
	 * 0: 待发送 
	 * 1：发送成功 
	 * 2：发送失败
	 */
	private Integer status;
	
	/**
	 * 验证时间
	 */
	private String validTime;
	
	
	public String getValidTime() {
		return validTime;
	}


	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}


	public Long getMailid() {
		return mailid;
	}


	public void setMailid(Long mailid) {
		this.mailid = mailid;
	}


	public Long getUuid() {
		return uuid;
	}


	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}


	public String getMailServerHost() {
		return mailServerHost;
	}


	public void setMailServerHost(String mailServerHost) {
		this.mailServerHost = mailServerHost;
	}


	public String getMailServerPort() {
		return mailServerPort;
	}


	public void setMailServerPort(String mailServerPort) {
		this.mailServerPort = mailServerPort;
	}


	public String getFromAddress() {
		return fromAddress;
	}


	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}


	public String getToAddress() {
		return toAddress;
	}


	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}


	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getCreateDate() {
		return createDate;
	}


	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}


	public String getSendDate() {
		return sendDate;
	}


	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "EmailLog [mailid=" + mailid + ", uuid=" + uuid
				+ ", mailServerHost=" + mailServerHost + ", mailServerPort="
				+ mailServerPort + ", fromAddress=" + fromAddress
				+ ", toAddress=" + toAddress + ", subject=" + subject
				+ ", content=" + content + ", createDate=" + createDate
				+ ", sendDate=" + sendDate + ", status=" + status + "]";
	}

	@Override
	public Serializable getID() {
		return this.getMailid();
	}
}
