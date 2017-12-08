package com.gameportal.web.activity.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gameportal.web.activity.model.ActivityFlag;
import com.gameportal.web.user.dao.BaseIbatisDAO;

@Repository
public class ActivityFlagDao extends BaseIbatisDAO {

	@Override
	public Class<ActivityFlag> getEntityClass() {
		return ActivityFlag.class;
	}
	
	public List<ActivityFlag> getActivityFlags(Map<String, Object> param){
		return (List<ActivityFlag>) queryForList(param);
	}
}
