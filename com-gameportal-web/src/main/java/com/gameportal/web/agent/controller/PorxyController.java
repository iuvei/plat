package com.gameportal.web.agent.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.web.agent.model.ProxyApply;
import com.gameportal.web.agent.service.IProxyApplyService;
import com.gameportal.web.util.WebConst;

/**
 * 代理控制器。
 * 
 * @author sum
 *
 */
@Controller
@RequestMapping("/proxy")
public class PorxyController {
	private static final Logger logger = Logger.getLogger(PorxyController.class);

	@Resource(name = "proxyApplyServiceImpl")
	private IProxyApplyService proxyApplyService;

	public PorxyController() {
		super();
	}

	@RequestMapping("/register")
	@ResponseBody
	public String registerWebProxy(ProxyApply proxy, HttpServletRequest request, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			String cacheCode = (String) request.getSession().getAttribute(WebConst.AGENT_TOKEN_CODE);
			if (StringUtils.isEmpty(proxy.getCode()) || !proxy.getCode().equals(cacheCode)) {
				result.put("code", "9");
				result.put("info", "验证码错误！");
				return result.toString();
			}
			proxy.setApplySource(request.getHeader("Referer"));
			proxyApplyService.saveWebProxy(proxy);
			result.put("code", "0");
		} catch (Exception e) {
			logger.error("代理申请出现异常。", e);
			result.put("code", "9");
			result.put("info", "网络异常，请稍后重试。");
		}
		return result.toString();
	}
}
