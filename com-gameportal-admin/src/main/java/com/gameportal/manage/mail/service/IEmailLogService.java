package com.gameportal.manage.mail.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.mail.model.EmailLog;

public interface IEmailLogService {

	public boolean save(EmailLog entity);
	
	public boolean update(EmailLog entity);
	
	public Long getCount(Map<String, Object> params);
	
	public List<EmailLog> getlList(Map<String, Object> params,int thisPage,int pageSize);
	
	public boolean delete(Long id);
	
	public EmailLog getObject(Map<String, Object> params);
	
}
