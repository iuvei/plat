package com.gameportal.manage.mail.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.mail.model.EmailLog;
import com.gameportal.manage.system.dao.BaseIbatisDAO;
@SuppressWarnings("unchecked")
@Component("emailLogDao")
public class EmailLogDao extends BaseIbatisDAO{

	@Override
	public Class<EmailLog> getEntityClass() {
		return EmailLog.class;
	}
	
	public boolean save(EmailLog entity) {
		return StringUtils.isNotBlank(ObjectUtils.toString(super.save(entity))) ? true: false;
	}
	
	public boolean update(EmailLog entity){
		return super.update(entity);
	}
	
	public List<EmailLog> getlList(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}
	
	public EmailLog getObject(Map<String, Object> params){
		List<EmailLog> list = this.getlList(params);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

}
