package com.gameportal.web.mail;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.gameportal.web.mail.model.MailSenderInfo;
import com.gameportal.web.user.model.UserInfo;

public interface IEmailService {

	void addEmailList(UserInfo userInfo,String email,String urlPrefix,Properties prop) throws Exception;
	
	void update(MailSenderInfo mail);
	
	List<MailSenderInfo> getMailSenderInfo(Map values);
	
	public MailSenderInfo getObject(Map<String, Object> params);
}
