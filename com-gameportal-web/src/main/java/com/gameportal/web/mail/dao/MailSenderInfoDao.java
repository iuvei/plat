package com.gameportal.web.mail.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gameportal.web.mail.model.MailSenderInfo;
import com.gameportal.web.user.dao.BaseIbatisDAO;

@Repository
public class MailSenderInfoDao extends BaseIbatisDAO {

	@Override
	public Class<MailSenderInfo> getEntityClass() {
		return MailSenderInfo.class;
	}
	
	public List<MailSenderInfo> getMailSenderInfo(Map<String,Object> params){
		return super.queryForList(params);
	}
	
	public MailSenderInfo getObject(Map<String, Object> params){
		List<MailSenderInfo> list = this.getMailSenderInfo(params);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
}
