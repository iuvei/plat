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

import com.gameportal.manage.order.model.CCAndGroup;
import com.gameportal.manage.order.service.ICCAndGroupService;
import com.gameportal.manage.order.service.ICCGroupService;
import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.system.service.ISystemService;


@Controller
@RequestMapping(value = "/manage/ccandgroup")
public class CCAndGroupController {
	

	private static final Logger logger = Logger
			.getLogger(CCAndGroupController.class);
	
	@Resource(name = "cCAndGroupServiceImpl")
	private ICCAndGroupService cCAndGroupService = null;
	@Resource(name = "cCGroupServiceImpl")
	private ICCGroupService cCGroupService = null;
	@Resource(name = "systemServiceImpl")
	private ISystemService systemService = null;
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService = null;
	

	public CCAndGroupController() {
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
		return "com.gameportal.manage.order/cCAndGroup";
	}

	@RequestMapping(value = "/queryCCAndGroup")
	public @ResponseBody
	Object queryCCAndGroup(
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (null!=status) {
			params.put("status", status);
		}
		Long count = cCAndGroupService.queryCCAndGroupCount(params);
		List<CCAndGroup> list = cCAndGroupService.queryCCAndGroup(params,
				startNo, pageSize);
		return new GridPanel(count, list, true);
	}

	@RequestMapping("/del/{id}")
	@ResponseBody
	public Object delCCAndGroup(@PathVariable Long id) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(id))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			if (cCAndGroupService.deleteCCAndGroup(id)) {
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

