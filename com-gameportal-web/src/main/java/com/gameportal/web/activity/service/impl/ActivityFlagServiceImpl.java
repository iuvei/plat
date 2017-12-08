package com.gameportal.web.activity.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gameportal.web.activity.dao.ActivityFlagDao;
import com.gameportal.web.activity.model.ActivityFlag;
import com.gameportal.web.activity.service.IActivityFlagService;
import com.gameportal.web.util.DateUtil;

@Service("activityFlagService")
public class ActivityFlagServiceImpl implements IActivityFlagService {

	@Resource(name = "activityFlagDao")
	private ActivityFlagDao activityFlagDao;

	@Override
	public ActivityFlag save(ActivityFlag activityFlag) {
		return (ActivityFlag)activityFlagDao.save(activityFlag);
	}

	@Override
	public List<ActivityFlag> getActivityFlags() {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type", "1");
		param.put("flagtime", DateUtil.getStrByDate(new Date(), "yyyy-MM-dd"));
		String hms = DateUtil.getStrByDate(new Date(), "HH:mm:ss");
		if(hms.compareTo("11:59:59")>0 && hms.compareTo("23:59:59")<0){
			param.put("hms", "23:59:59");
		}else{
			param.put("hms", "11:59:59");
		}
		return activityFlagDao.getActivityFlags(param);
	}

	@Override
	public boolean update(ActivityFlag activityFlag) {
		return activityFlagDao.update(activityFlag);
	}
	
	@Override
	public List<ActivityFlag> queryAll(Map<String, Object> params) {
		return activityFlagDao.getActivityFlags(params);
	}
}
