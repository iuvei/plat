package com.gameportal.manage.system.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.Tree;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.system.service.ISecurityService;
import com.gameportal.manage.util.CookieUtil;
import com.gameportal.manage.util.WebConstants;

@Controller
@RequestMapping(value = "/manage")
public class IndexManageController {
	@Resource(name = "securityServiceImpl")
	private ISecurityService iSecurityService = null;
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService = null;
	private static final Logger logger = Logger
			.getLogger(IndexManageController.class);

	public IndexManageController() {
		// TODO Auto-generated constructor stub
	}

	@RequestMapping(value = "/index")
	public String getValidateCode(HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		return "manage/index/home";
	}

	@RequestMapping("/treeMenu")
	public @ResponseBody
	Object treeMenu(HttpServletRequest request, HttpServletResponse response) {
		try {
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(
					key, c);
			Tree tree = iSecurityService.getChildrenNodes(systemUser
					.getUserId());
			return tree.getChildren();// 返回根菜单下面的子菜单
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

}
