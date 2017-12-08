package com.gameportal.manage.proxy.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.gameportal.manage.proxy.model.ProxyUserXimaLog;
import com.gameportal.manage.proxy.service.IProxyUserXimaService;

@Controller
@RequestMapping(value = "/manage/proxyuserxm")
public class ProxyUserXimaController {

	public static final Logger logger = Logger.getLogger(ProxyUserXimaController.class);
	
	@Resource(name="proxyUserXimaService")
	private IProxyUserXimaService proxyUserXimaService;
	
	@RequestMapping(value = "/index")
	public String index(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		return "manage/proxy/proxyuserxmlog";
	}
	
	@RequestMapping(value = "/queryProxyuserxm")
	public @ResponseBody
	Object queryProxyuserxm(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "start", required = false) Integer thisPage,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(StringUtils.isNotBlank(account)){
				params.put("account", account);
			}
			params.put("sortColumns", "xm.ximatime DESC");
			Long count = proxyUserXimaService.getCount(params);
			List<ProxyUserXimaLog> list = proxyUserXimaService.getList(params, thisPage, pageSize);
			return new GridPanel(count, list, true);
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	/**
	 * 入账
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/save/{id}")
	public @ResponseBody
	Object save(@PathVariable Long id,HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("logid", id);
			ProxyUserXimaLog proxyUserXimaLog = proxyUserXimaService.getObject(params);
			if(null == proxyUserXimaLog){
				return new ExtReturn(false, "您要入账的数据不存在，请刷新后重试。");
			}
			proxyUserXimaService.rz(proxyUserXimaLog);
			return new ExtReturn(true, "成功入账。");
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	/**
	 * 入账
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/verify/{id}")
	public @ResponseBody
	Object verify(@PathVariable Long id,HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("logid", id);
			ProxyUserXimaLog proxyUserXimaLog = proxyUserXimaService.getObject(params);
			if(null == proxyUserXimaLog){
				return new ExtReturn(false, "您要入账的数据不存在，请刷新后重试。");
			}
			proxyUserXimaLog.setStatus(2);
			if(proxyUserXimaService.update(proxyUserXimaLog)){
				return new ExtReturn(true, "操作成功。");
			}else{
				return new ExtReturn(false, "操作失败。");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
}
