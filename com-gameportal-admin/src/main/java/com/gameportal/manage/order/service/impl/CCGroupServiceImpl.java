package com.gameportal.manage.order.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.manage.order.dao.CCGroupDao;
import com.gameportal.manage.order.model.CCGroup;
import com.gameportal.manage.order.service.ICCGroupService;

@Service("cCGroupServiceImpl")
public class CCGroupServiceImpl implements ICCGroupService {

	private static final Logger logger = Logger
			.getLogger(CCGroupServiceImpl.class);

	@Resource
	private CCGroupDao cCGroupDao;

	public CCGroupServiceImpl() {
		super();
	}

	@Override
	public boolean saveOrUpdateCCGroup(CCGroup card) {
		return cCGroupDao.saveOrUpdate(card);
	}

	@Override
	public boolean deleteCCGroup(Long id) {
		return cCGroupDao.delete(id);
	}

	@Override
	public Long queryCCGroupCount(Map<String, Object> params) {
		return cCGroupDao.getRecordCount(params);
	}

	@Override
	public List<CCGroup> queryCCGroup(Map<String, Object> params,
			Integer startNo, Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		return cCGroupDao.getList(params);
	}

	@Override
	public CCGroup queryById(Long ccgid) {
		return (CCGroup) cCGroupDao.findById(ccgid);
	}
}
