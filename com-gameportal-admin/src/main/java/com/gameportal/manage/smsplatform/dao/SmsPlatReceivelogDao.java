package com.gameportal.manage.smsplatform.dao;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.smsplatform.model.SmsPlatReceivelog;
import com.gameportal.manage.system.dao.BaseIbatisDAO;

@Component
public class SmsPlatReceivelogDao extends BaseIbatisDAO {

	public Class<SmsPlatReceivelog> getEntityClass() {
		return SmsPlatReceivelog.class;
	}

	public boolean saveOrUpdate(SmsPlatReceivelog entity) {
		if(entity.getSprid() == null) 
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else 
			return update(entity);
	}

}
