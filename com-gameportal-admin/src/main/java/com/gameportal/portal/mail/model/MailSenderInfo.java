package com.gameportal.portal.mail.model;   
/**   
 *发送邮件需要使用的基本信息 
 *  
 */    
import java.io.Serializable;
import java.util.Date;
import java.util.Properties;

import com.gameportal.manage.system.model.BaseEntity;
import com.gameportal.portal.util.DateUtil;

public class MailSenderInfo extends BaseEntity{    
   
	private static final long serialVersionUID = -6677472184723981820L;
	private Long mailid;
	private Long uuid;
	// 发送邮件的服务器的IP和端口    
    private String mailServerHost;    
    private String mailServerPort = "25";    
    // 邮件发送者的地址    
    private String fromAddress;    
    // 邮件接收者的地址    
    private String toAddress;    
    // 登陆邮件发送服务器的用户名和密码    
    private String userName;    
    private String password;    
    // 是否需要身份验证    
    private boolean validate = false;    
    // 邮件主题    
    private String subject;    
    // 邮件的文本内容    
    private String content;    
    // 邮件附件的文件名    
    private String[] attachFileNames; 
    
    private String emailType = "false";
    
    private String rediskey;
    // 邮件创建时间
    private String createDate = DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT);
    // 发送时间
    private String sendDate;
    // 验证时间
    private String validTime;
    // 状态 0: 待发送 1：发送成功 2：发送失败
    private Integer status;
    /**   
      * 获得邮件会话属性   
      */
    public Properties getProperties(){    
      Properties p = new Properties();    
      p.put("mail.smtp.host", this.mailServerHost);    
      p.put("mail.smtp.port", this.mailServerPort);    
      p.put("mail.smtp.auth", validate ? "true" : "false");
      if(emailType.equals("true")){
    	  p.put("mail.smtp.starttls.enable", "true");  
      }
      return p;    
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
    public boolean isValidate() {    
      return validate;    
    }   
    public void setValidate(boolean validate) {    
      this.validate = validate;    
    }   
    public String[] getAttachFileNames() {    
      return attachFileNames;    
    }   
    public void setAttachFileNames(String[] fileNames) {    
      this.attachFileNames = fileNames;    
    }   
    public String getFromAddress() {    
      return fromAddress;    
    }    
    public void setFromAddress(String fromAddress) {    
      this.fromAddress = fromAddress;    
    }   
    public String getPassword() {    
      return password;    
    }   
    public void setPassword(String password) {    
      this.password = password;    
    }   
    public String getToAddress() {    
      return toAddress;    
    }    
    public void setToAddress(String toAddress) {    
      this.toAddress = toAddress;    
    }    
    public String getUserName() {    
      return userName;    
    }   
    public void setUserName(String userName) {    
      this.userName = userName;    
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
    public void setContent(String textContent) {    
      this.content = textContent;    
    }
	public String getEmailType() {
		return emailType;
	}
	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}
	public String getRediskey() {
		return rediskey;
	}
	public void setRediskey(String rediskey) {
		this.rediskey = rediskey;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
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
	public String getValidTime() {
		return validTime;
	}
	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}
	@Override
	public Serializable getID() {
		return this.mailid;
	}
	
}   
