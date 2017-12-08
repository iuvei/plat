package com.gameportal.manage.mail.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gameportal.manage.mail.dao.EmailLogDao;
import com.gameportal.manage.mail.model.EmailLog;
import com.gameportal.manage.mail.service.IEmailLogService;

@Service("emailLogService")
public class EmailLogServiceImpl implements IEmailLogService{
	
	@Resource(name = "emailLogDao")
	private EmailLogDao emailLogDao;

	@Override
	public boolean save(EmailLog entity) {
		// TODO Auto-generated method stub
		return emailLogDao.save(entity);
	}

	@Override
	public boolean update(EmailLog entity) {
		// TODO Auto-generated method stub
		return emailLogDao.update(entity);
	}

	@Override
	public Long getCount(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return emailLogDao.getRecordCount(params);
	}

	@Override
	public List<EmailLog> getlList(Map<String, Object> params,int thisPage,int pageSize) {
		params.put("limit", true);
		params.put("thisPage", thisPage);
		params.put("pageSize", pageSize);
		return emailLogDao.getlList(params);
	}

	@Override
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		return emailLogDao.delete(id);
	}
	
	public EmailLog getObject(Map<String, Object> params){
		return emailLogDao.getObject(params);
	}

}
