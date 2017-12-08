package com.gameportal.manage.pay.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.pay.model.Activity;
import com.gameportal.manage.pay.model.ActivityFlag;
import com.gameportal.manage.system.dao.BaseIbatisDAO;

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
	
	public boolean save(Activity entity){
		return StringUtils.isNotBlank(ObjectUtils.toString(super.save(entity))) ? true
				: false;
	}
	
	/**
	 * 查询活动标示
	 * @param params
	 * @return
	 */
	public ActivityFlag getActivityFlag(Map<String, Object> params){
		return (ActivityFlag) getSqlMapClientTemplate().queryForObject(getSimpleName()+".getActivityFlag", params);
	}
	
	public boolean saveActivityFlag(ActivityFlag activityFlag){
		return StringUtils.isNotBlank(ObjectUtils.toString(getSqlMapClientTemplate().insert(getSimpleName()+".insertActivityFlag", activityFlag))) ? true
				: false;
		
	}
	

}
