package com.gameportal.manage.xima.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.manage.xima.dao.MemberXimaSetDao;
import com.gameportal.manage.xima.model.MemberXimaSet;
import com.gameportal.manage.xima.service.IMemberXimaSetService;

@Service("memberXimaSetServiceImpl")
public class MemberXimaSetServiceImpl implements IMemberXimaSetService {
	
	private static final Logger logger = Logger
			.getLogger(MemberXimaSetServiceImpl.class);
	
	@Resource
	private MemberXimaSetDao memberXimaSetDao;
	
	public MemberXimaSetServiceImpl() {
		super();
	}
	
	@Override
	public boolean saveOrUpdateMemberXimaSet(MemberXimaSet card) {
		return memberXimaSetDao.saveOrUpdate(card);
	}

	@Override
	public boolean deleteMemberXimaSet(Long id) {
		return memberXimaSetDao.delete(id);
	}

	@Override
	public Long queryMemberXimaSetCount(Map<String, Object> params) {
		return memberXimaSetDao.getRecordCount(params);
	}

	@Override
	public List<MemberXimaSet> queryMemberXimaSet(Map<String, Object> params, Integer startNo,
			Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		return memberXimaSetDao.getList(params);
	}
}
