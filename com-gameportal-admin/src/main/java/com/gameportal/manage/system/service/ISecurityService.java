package com.gameportal.manage.system.service;

import com.gameportal.manage.pojo.Tree;

/**
 * @ClassName: ISecurityService
 * @Description: TODO(权限接口)
 * @author shejia@gz-mstc.com
 * @date 2014-4-10 上午10:44:56
 */
public abstract interface ISecurityService {

	/**
	 * @Title: getChildrenNodes
	 * @Description: TODO(按用户查询权限的树)
	 * @param userId
	 * @return
	 * @return Tree 返回类型
	 * @throws
	 */
	public Tree getChildrenNodes(Long userId);

	public Object getChildrenNodes(Long userId, Long pid);
}
