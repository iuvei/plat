package com.gameportal.manage.pay.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gameportal.manage.pay.dao.ActivityDao;
import com.gameportal.manage.pay.model.Activity;
import com.gameportal.manage.pay.model.ActivityFlag;
import com.gameportal.manage.pay.service.IActivityService;

/**
 * 充值活动
 * @author Administrator
 *
 */
@Service("activityService")
public class ActivityServiceImpl implements IActivityService{

	@Resource(name = "activityDao")
	private ActivityDao activityDao;
	@Override
	public List<Activity> getList(Map<String, Object> params, int thisPage,
			int pageSize) {
		params.put("limit", true);
		params.put("thisPage", thisPage);
		params.put("pageSize", pageSize);
		
		return activityDao.getList(params);
	}

	@Override
	public List<Activity> getList(Map<String, Object> params) {
		return activityDao.getList(params);
	}

	@Override
	public Long getCount(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return activityDao.getRecordCount(params);
	}

	@Override
	public boolean save(Activity entity) {
		return activityDao.save(entity);
	}

	@Override
	public boolean update(Activity entity) {
		return activityDao.update(entity);
	}

	@Override
	public ActivityFlag getActivityFlag(Map<String, Object> params) {
		return activityDao.getActivityFlag(params);
	}

	@Override
	public boolean saveActivityFlag(ActivityFlag activityFlag) {
		return activityDao.saveActivityFlag(activityFlag);
	}
}
