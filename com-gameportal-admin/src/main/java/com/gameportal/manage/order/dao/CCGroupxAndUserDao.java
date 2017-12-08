package com.gameportal.manage.order.dao;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.order.model.CCGroupxAndUser;
import com.gameportal.manage.system.dao.BaseIbatisDAO;


@Component
public class CCGroupxAndUserDao extends BaseIbatisDAO{

	public Class<CCGroupxAndUser> getEntityClass() {
		return CCGroupxAndUser.class;
	}
	
	public boolean saveOrUpdate(CCGroupxAndUser entity) {
		if(entity.getId() == null) 
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else 
			return update(entity);
	}

}
