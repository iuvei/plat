package com.gameportal.manage.xima.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.xima.model.MemberXimaDetail;

public abstract interface IMemberXimaDetailService {

	boolean saveOrUpdateMemberXimaDetail(MemberXimaDetail memberXimaDetail);

	boolean deleteMemberXimaDetail(Long id);

	Long queryMemberXimaDetailCount(Map<String, Object> params);

	List<MemberXimaDetail> queryMemberXimaDetail(Map<String, Object> params, Integer startNo, Integer pageSize);
	
}
