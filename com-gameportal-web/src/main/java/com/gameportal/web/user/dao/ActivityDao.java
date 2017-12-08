package com.gameportal.web.user.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gameportal.web.user.model.Activity;

/**
 * 活动
 * @author Administrator
 *
 */
@Component("activityDao")
public class ActivityDao extends BaseIbatisDAO{

	@Override
	public Class<Activity> getEntityClass() {
		return Activity.class;
	}
	
	public List<Activity> getList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".getList", params);
	}
	
	public Activity getObject(Map<String, Object> params){
		List<Activity> list = this.getList(params);
		if(null != list && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

}
