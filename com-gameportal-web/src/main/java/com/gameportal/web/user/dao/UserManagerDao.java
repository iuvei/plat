package com.gameportal.web.user.dao;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.web.user.model.UserManager;

@Component("userManagerDao")
public class UserManagerDao extends BaseIbatisDAO{

	@Override
	public Class<UserManager> getEntityClass() {
		return UserManager.class;
	}
	
	public boolean insert(UserManager entity){
		return StringUtils.isNotBlank(ObjectUtils.toString(super.save(entity))) ? true: false;
	}

}
