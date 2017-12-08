package com.gameportal.manage.xima.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.gameportal.manage.xima.model.MemberXimaDetail;
import com.gameportal.manage.xima.dao.MemberXimaDetailDao;
import com.gameportal.manage.xima.service.IMemberXimaDetailService;

@Service("memberXimaDetailServiceImpl")
public class MemberXimaDetailServiceImpl implements IMemberXimaDetailService {
	
	private static final Logger logger = Logger
			.getLogger(MemberXimaDetailServiceImpl.class);
	
	@Resource
	private MemberXimaDetailDao memberXimaDetailDao;
	
	public MemberXimaDetailServiceImpl() {
		super();
	}
	
	@Override
	public boolean saveOrUpdateMemberXimaDetail(MemberXimaDetail card) {
		return memberXimaDetailDao.saveOrUpdate(card);
	}

	@Override
	public boolean deleteMemberXimaDetail(Long id) {
		return memberXimaDetailDao.delete(id);
	}

	@Override
	public Long queryMemberXimaDetailCount(Map<String, Object> params) {
		return memberXimaDetailDao.getRecordCount(params);
	}

	@Override
	public List<MemberXimaDetail> queryMemberXimaDetail(Map<String, Object> params, Integer startNo,
			Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		return memberXimaDetailDao.queryForPager(params, startNo, pageSize);
	}
}
