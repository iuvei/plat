package com.na.betRobot.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.na.betRobot.dao.ILiveUserMapper;
import com.na.betRobot.entity.LiveUser;
import com.na.betRobot.service.ILiveUserService;

/**
 * Created by sunny on 2017/6/21 0021.
 */
@Service
public class LiveUserServiceImpl implements ILiveUserService {
	
	@Autowired
	private ILiveUserMapper liveUserMapper;

	@Override
	@Transactional(readOnly = true)
	public LiveUser findById(Long userId) {
		Assert.isTrue(userId != null, "request.param.not.null");
		return liveUserMapper.findById(userId);
	}

	@Override
	public LiveUser findByLoginName(String loginName) {
		Assert.isTrue(loginName != null, "request.param.not.null");
		return liveUserMapper.findByLoginName(loginName);
	}

	@Override
	public List<LiveUser> findByParentId(Long parentId, Integer number) {
		Assert.isTrue(parentId != null, "request.param.not.null");
		return liveUserMapper.findByParentId(parentId, number);
	}
	
}
