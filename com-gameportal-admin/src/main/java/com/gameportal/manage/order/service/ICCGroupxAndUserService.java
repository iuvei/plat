package com.gameportal.manage.order.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.order.model.CCGroupxAndUser;

public abstract interface ICCGroupxAndUserService {

	boolean saveOrUpdateCCGroupxAndUser(CCGroupxAndUser cCGroupxAndUser);

	boolean deleteCCGroupxAndUser(Long id);

	Long queryCCGroupxAndUserCount(Map<String, Object> params);

	List<CCGroupxAndUser> queryCCGroupxAndUser(Map<String, Object> params, Integer startNo, Integer pageSize);
	
}
