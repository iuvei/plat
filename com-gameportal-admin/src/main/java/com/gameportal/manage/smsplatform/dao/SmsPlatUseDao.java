package com.gameportal.manage.smsplatform.dao;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.smsplatform.model.SmsPlatUse;
import com.gameportal.manage.system.dao.BaseIbatisDAO;

@Component
public class SmsPlatUseDao extends BaseIbatisDAO {

	public Class<SmsPlatUse> getEntityClass() {
		return SmsPlatUse.class;
	}

	public boolean saveOrUpdate(SmsPlatUse entity) {
		if(entity.getSpuid() == null) 
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else 
			return update(entity);
	}

}
