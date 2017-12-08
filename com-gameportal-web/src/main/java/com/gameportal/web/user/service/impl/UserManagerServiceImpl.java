package com.gameportal.web.user.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gameportal.web.user.dao.UserManagerDao;
import com.gameportal.web.user.model.UserManager;
import com.gameportal.web.user.service.IUserManagerService;

@Service("userManagerService")
public class UserManagerServiceImpl implements IUserManagerService{

	@Resource(name = "userManagerDao")
	private UserManagerDao userManagerDao;
	@Override
	public boolean insert(UserManager entity) {
		return userManagerDao.insert(entity);
	}

}
