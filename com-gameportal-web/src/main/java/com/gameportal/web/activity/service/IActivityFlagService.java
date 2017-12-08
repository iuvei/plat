package com.gameportal.web.activity.service;

import java.util.List;
import java.util.Map;

import com.gameportal.web.activity.model.ActivityFlag;

public interface IActivityFlagService {

	List<ActivityFlag> getActivityFlags();
	
	ActivityFlag save(ActivityFlag activityFlag);
	
	boolean update(ActivityFlag activityFlag);
	
	List<ActivityFlag> queryAll(Map<String, Object> params);
}
