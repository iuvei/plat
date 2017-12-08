package com.gameportal.manage.order.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.order.model.CCGroup;

public abstract interface ICCGroupService {

	boolean saveOrUpdateCCGroup(CCGroup cCGroup);

	boolean deleteCCGroup(Long id);

	Long queryCCGroupCount(Map<String, Object> params);

	List<CCGroup> queryCCGroup(Map<String, Object> params, Integer startNo, Integer pageSize);

	CCGroup queryById(Long ccgid);
	
}
