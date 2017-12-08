package com.gameportal.manage.order.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.manage.order.dao.CCAndGroupDao;
import com.gameportal.manage.order.model.CCAndGroup;
import com.gameportal.manage.order.service.ICCAndGroupService;

@Service("cCAndGroupServiceImpl")
public class CCAndGroupServiceImpl implements ICCAndGroupService {
	
	private static final Logger logger = Logger
			.getLogger(CCAndGroupServiceImpl.class);
	
	@Resource
	private CCAndGroupDao cCAndGroupDao;
	
	public CCAndGroupServiceImpl() {
		super();
	}
	
	@Override
	public boolean saveOrUpdateCCAndGroup(CCAndGroup card) {
		return cCAndGroupDao.saveOrUpdate(card);
	}

	@Override
	public boolean deleteCCAndGroup(Long id) {
		return cCAndGroupDao.delete(id);
	}

	@Override
	public Long queryCCAndGroupCount(Map<String, Object> params) {
		return cCAndGroupDao.getRecordCount(params);
	}

	@Override
	public List<CCAndGroup> queryCCAndGroup(Map<String, Object> params, Integer startNo,
			Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		return cCAndGroupDao.queryForPager(params, startNo, pageSize);
	}

	@Override
	public boolean saveOrUpdateCCAndGroup(Long ccgid, ArrayList<Long> ccIdList) {
		// 删除原有分组银行卡关联记录
		cCAndGroupDao.deleteByCcgid(ccgid);
		// 新增现有分组银行卡关联记录
		if (null != ccIdList && ccIdList.size() > 0) {
			for (Long ccid : ccIdList) {
				CCAndGroup ccg = new CCAndGroup();
				ccg.setCcgid(ccgid);
				ccg.setCcid(ccid);
				cCAndGroupDao.save(ccg);
			}
			return true;
		}
		return false;
	}
}
