package com.na.betRobot.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.na.betRobot.dao.IUserMapper;
import com.na.betRobot.service.IUserService;

/**
 * Created by sunny on 2017/4/27 0027.
 */
@Service
public class UserServiceImpl implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private IUserMapper userMapper;


}
