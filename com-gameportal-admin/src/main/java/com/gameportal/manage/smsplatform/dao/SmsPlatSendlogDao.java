package com.gameportal.manage.smsplatform.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.smsplatform.model.SmsPlatReceivelog;
import com.gameportal.manage.smsplatform.model.SmsPlatSendlog;
import com.gameportal.manage.system.dao.BaseIbatisDAO;

@Component("smsPlatSendlogDao")
public class SmsPlatSendlogDao extends BaseIbatisDAO {

	public Class<SmsPlatSendlog> getEntityClass() {
		return SmsPlatSendlog.class;
	}

	public boolean saveOrUpdate(SmsPlatSendlog entity) {
		if(entity.getSpsid() == null) 
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else 
			return update(entity);
	}
	
	public List<SmsPlatSendlog> getList(Map<String, Object> params){
		return  getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}


	public boolean updateStatusAndSendTime(SmsPlatSendlog entity) {
		try {
			prepareObjectForSaveOrUpdate(entity);
			Object primaryKey = getSqlMapClientTemplate().update(
					this.getSimpleName() + ".updateStatusAndSendTime", entity);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
