package com.gameportal.manage.xima.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.xima.model.MemberXimaSet;

public abstract interface IMemberXimaSetService {

	boolean saveOrUpdateMemberXimaSet(MemberXimaSet memberXimaSet);

	boolean deleteMemberXimaSet(Long id);

	Long queryMemberXimaSetCount(Map<String, Object> params);

	List<MemberXimaSet> queryMemberXimaSet(Map<String, Object> params, Integer startNo, Integer pageSize);
	
}
