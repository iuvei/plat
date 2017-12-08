package com.gameportal.manage.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.manage.pojo.Tree;
import com.gameportal.manage.pojo.TreeMenu;
import com.gameportal.manage.system.dao.SystemModuleDao;
import com.gameportal.manage.system.model.SystemModule;
import com.gameportal.manage.system.service.ISecurityService;

/**
 * @ClassName: SecurityServiceImpl
 * @Description: TODO(权限接口实现类)
 * @author shejia@gz-mstc.com
 * @date 2014-4-10 下午12:52:09
 */
@Service
public class SecurityServiceImpl implements ISecurityService {
	@Resource(name = "systemModuleDao")
	private SystemModuleDao systemModuleDao = null;
	private static final Logger logger = Logger
			.getLogger(SecurityServiceImpl.class);

	public SecurityServiceImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public Tree getChildrenNodes(Long userId) {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		paramMap.put("isDisplay", 1);
		List<SystemModule> list = systemModuleDao.queryForPager(
				"SystemModule.treeMenuSelect", paramMap, 0, 0);
		TreeMenu menu = new TreeMenu(list);
		return menu.getTreeJson();
	}

	@Override
	public Object getChildrenNodes(Long userId, Long pid) {

		return null;
	}

}
