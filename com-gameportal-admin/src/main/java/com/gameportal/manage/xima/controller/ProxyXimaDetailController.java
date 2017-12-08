package com.gameportal.manage.xima.controller;

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

import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.xima.model.ProxyXimaDetail;
import com.gameportal.manage.xima.service.IProxyXimaDetailService;

@Controller
@RequestMapping(value = "/manage/proxyximadetail")
public class ProxyXimaDetailController {

	private static final Logger logger = Logger
			.getLogger(ProxyXimaDetailController.class);

	@Resource(name = "proxyXimaDetailServiceImpl")
	private IProxyXimaDetailService proxyXimaDetailService = null;

	public ProxyXimaDetailController() {
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
		// opttype 操作类型 0自助洗码，1洗码清零，2强制洗码，3强制清零
		JSONObject opttypeMap = new JSONObject();
		opttypeMap.put("0", "自助洗码");
		opttypeMap.put("1", "洗码清零");
		opttypeMap.put("2", "强制洗码");
		request.setAttribute("opttypeMap", opttypeMap.toString());
		return "manage/xima/proxyximadetail";
	}

	@RequestMapping(value = "/queryProxyximadetail")
	public @ResponseBody
	Object queryProxyXimaDetail(
			@RequestParam(value = "uiid", required = false) Long uiid,
			@RequestParam(value = "optusername", required = false) String optusername,

			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (null != uiid) {
			params.put("uiid", uiid);
		}
		if (StringUtils.isNotBlank(optusername)) {
			params.put("optusername", optusername);
		}
		params.put("sortColumns", "opttime desc");
		Long count = proxyXimaDetailService.queryProxyXimaDetailCount(params);
		List<ProxyXimaDetail> list = proxyXimaDetailService
				.queryProxyXimaDetail(params, startNo, pageSize);
		return new GridPanel(count, list, true);
	}

	@RequestMapping("/del/{pxdid}")
	@ResponseBody
	public Object delProxyXimaDetail(@PathVariable Long pxdid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(pxdid))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			if (proxyXimaDetailService.deleteProxyXimaDetail(pxdid)) {
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
