package com.gameportal.manage.pay.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.pay.model.Activity;
import com.gameportal.manage.pay.model.ActivityFlag;

public interface IActivityService {

	/**
	 * 分页查询
	 * @param params
	 * @param thisPage
	 * @param pageSize
	 * @return
	 */
	public List<Activity> getList(Map<String, Object> params,int thisPage,int pageSize);
	
	/**
	 * 查询全部
	 * @param params
	 * @return
	 */
	public List<Activity> getList(Map<String, Object> params);
	
	/**
	 * 查询游戏参与记录表
	 * @param params
	 * @return
	 */
	public ActivityFlag getActivityFlag(Map<String,Object> params);
	
	/**
	 * 新增游戏参加记录
	 * @param activityFlag
	 * @return
	 */
	public boolean saveActivityFlag(ActivityFlag activityFlag);
	
	/**
	 * 统计
	 * @param params
	 * @return
	 */
	public Long getCount(Map<String, Object> params);
	
	public boolean save(Activity entity);
	
	public boolean update(Activity entity);
}
