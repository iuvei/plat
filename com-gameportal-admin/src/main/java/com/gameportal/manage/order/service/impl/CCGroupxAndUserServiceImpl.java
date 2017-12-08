package com.gameportal.manage.order.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.manage.order.dao.CCGroupxAndUserDao;
import com.gameportal.manage.order.model.CCGroupxAndUser;
import com.gameportal.manage.order.service.ICCGroupxAndUserService;

@Service("cCGroupxAndUserServiceImpl")
public class CCGroupxAndUserServiceImpl implements ICCGroupxAndUserService {
	
	private static final Logger logger = Logger
			.getLogger(CCGroupxAndUserServiceImpl.class);
	
	@Resource
	private CCGroupxAndUserDao cCGroupxAndUserDao;
	
	public CCGroupxAndUserServiceImpl() {
		super();
	}
	
	@Override
	public boolean saveOrUpdateCCGroupxAndUser(CCGroupxAndUser card) {
		return cCGroupxAndUserDao.saveOrUpdate(card);
	}

	@Override
	public boolean deleteCCGroupxAndUser(Long id) {
		return cCGroupxAndUserDao.delete(id);
	}

	@Override
	public Long queryCCGroupxAndUserCount(Map<String, Object> params) {
		return cCGroupxAndUserDao.getRecordCount(params);
	}

	@Override
	public List<CCGroupxAndUser> queryCCGroupxAndUser(Map<String, Object> params, Integer startNo,
			Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		return cCGroupxAndUserDao.queryForPager(params, startNo, pageSize);
	}
}
