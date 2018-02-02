package com.na.betRobot.service;

import java.util.List;

import com.na.betRobot.entity.LiveUser;


/**
 * Created by sunny on 2017/6/21 0021.
 */
public interface ILiveUserService {
	
	LiveUser findById(Long id);
	
	LiveUser findByLoginName(String loginName);
	
	List<LiveUser> findByParentId(Long parentId, Integer number);
	
}
