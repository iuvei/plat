package com.gameportal.manage.system.service;

import com.gameportal.manage.system.model.SystemUser;

/**
 * @ClassName: ILoginService
 * @Description: TODO(系统登录服务接口)
 * @author shejia@gz-mstc.com
 * @date 2014-4-9 下午12:33:22
 */
public abstract interface ILoginService {

	/**
	 * @Title: queryBySystemUser
	 * @Description: TODO(用户帐号登录方法)
	 * @param account
	 *            帐号
	 * @param password
	 *            密码
	 * @return SystemUser 返回类型
	 * @throws
	 */
	public abstract SystemUser queryBySystemUser(String account, String password);
}
