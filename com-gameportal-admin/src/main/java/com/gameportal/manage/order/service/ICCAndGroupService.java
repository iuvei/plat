package com.gameportal.manage.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gameportal.manage.order.model.CCAndGroup;

public abstract interface ICCAndGroupService {

	boolean saveOrUpdateCCAndGroup(CCAndGroup cCAndGroup);

	boolean deleteCCAndGroup(Long id);

	Long queryCCAndGroupCount(Map<String, Object> params);

	List<CCAndGroup> queryCCAndGroup(Map<String, Object> params, Integer startNo, Integer pageSize);

	boolean saveOrUpdateCCAndGroup(Long ccgid, ArrayList<Long> ccIdList);
	
}
