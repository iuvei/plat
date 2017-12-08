package com.gameportal.web.user.dao;

import org.springframework.stereotype.Component;

import com.gameportal.pay.dao.BaseIbatisDAO;
import com.gameportal.web.user.model.UserInfo;

@Component
public class UserInfoDao extends BaseIbatisDAO {

	public Class getEntityClass() {
		return UserInfo.class;
	}

	public void saveOrUpdate(UserInfo entity) {
		if (entity.getUiid() == null)
			save(entity);
		else
			update(entity);
	}

}
