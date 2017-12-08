package com.gameportal.web.order.dao;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.web.order.model.CCGroupxAndUser;
import com.gameportal.web.user.dao.BaseIbatisDAO;


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
