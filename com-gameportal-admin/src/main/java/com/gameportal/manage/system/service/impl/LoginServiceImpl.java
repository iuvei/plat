package com.gameportal.manage.system.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.manage.system.dao.SystemUserDao;
import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.system.service.ILoginService;
import com.gameportal.manage.util.MD5Util;

/**
 * @ClassName: LoginServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author shejia@gz-mstc.com
 * @date 2014-4-9 下午12:32:01
 */
@Service
public class LoginServiceImpl implements ILoginService {
	@Resource(name = "systemUserDao")
	private SystemUserDao systemUserDao = null;
	private static final Logger logger = Logger
			.getLogger(LoginServiceImpl.class);

	@Override
	public SystemUser queryBySystemUser(String account, String password) {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("account", account);
		paramMap.put("password", MD5Util.getMD5Encode(password));
		paramMap.put("status", 1);
		SystemUser systemUser = (SystemUser) systemUserDao.queryForObject(
				systemUserDao.getSelectQuery(), paramMap);
		return systemUser;
	}

}
