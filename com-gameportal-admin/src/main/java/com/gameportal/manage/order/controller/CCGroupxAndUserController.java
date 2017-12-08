package com.gameportal.manage.order.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.manage.order.model.CCGroupxAndUser;
import com.gameportal.manage.order.service.ICCGroupxAndUserService;
import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;


@Controller
@RequestMapping(value = "/manage/CCGroupxAndUser")
public class CCGroupxAndUserController {
	

	private static final Logger logger = Logger
			.getLogger(CCGroupxAndUserController.class);
	
	@Resource(name = "cCGroupxAndUserServiceImpl")
	private ICCGroupxAndUserService cCGroupxAndUserService = null;
	

	public CCGroupxAndUserController() {
		super();
	}
	

	@RequestMapping(value = "/index")
	public String index(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		// `status` int(2) default NULL COMMENT '状态 0未锁定 1锁定',
		JSONObject map = new JSONObject();
		map.put("0", "未锁定");
		map.put("1", "锁定");
		request.setAttribute("statusMap", map.toString());
		return "com.gameportal.manage.order/cCGroupxAndUser";
	}

	@RequestMapping(value = "/queryCCGroupxAndUser")
	public @ResponseBody
	Object queryCCGroupxAndUser(
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (null!=status) {
			params.put("status", status);
		}
		Long count = cCGroupxAndUserService.queryCCGroupxAndUserCount(params);
		List<CCGroupxAndUser> list = cCGroupxAndUserService.queryCCGroupxAndUser(params,
				startNo, pageSize);
		return new GridPanel(count, list, true);
	}

	@RequestMapping("/del/{id}")
	@ResponseBody
	public Object delCCGroupxAndUser(@PathVariable Long id) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(id))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			if (cCGroupxAndUserService.deleteCCGroupxAndUser(id)) {
				return new ExtReturn(true, "删除成功！");
			} else {
				return new ExtReturn(false, "删除失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	
}

