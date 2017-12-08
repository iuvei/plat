package com.gameportal.pay.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.pay.model.Activity;
import com.gameportal.pay.model.ActivityFlag;

/**
 * 活动
 * @author Administrator
 *
 */
@Component("activityDao")
public class ActivityDao extends BaseIbatisDAO{

	@Override
	public Class getEntityClass() {
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

	public boolean saveActivityFlag(ActivityFlag activityFlag){
		return StringUtils.isNotBlank(ObjectUtils.toString(getSqlMapClientTemplate().insert(getSimpleName()+".insertActivityFlag", activityFlag))) ? true
				: false;
	}
	
	public List<ActivityFlag> queryActivityFlag(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".queryActivityFlag", params);
	}
}
