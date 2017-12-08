package com.gameportal.manage.proxy.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.proxy.model.ProxyClearingLog;
import com.gameportal.manage.proxy.service.IProxyClearingService;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.util.ClientIP;
import com.gameportal.manage.util.CookieUtil;
import com.gameportal.manage.util.DateUtil2;
import com.gameportal.manage.util.WebConstants;

/**
 * 结算记录控制器
 * @author Administrator
 *
 */

@Controller
@RequestMapping(value = "/manage/proxyclearinglog")
public class ProxyClearingLogController {
	
	public static final Logger logger = Logger.getLogger(ProxyClearingLogController.class);

	@Resource(name="proxyClearingService")
	private IProxyClearingService proxyClearingService;
	
	@Resource(name = "redisServiceImpl")
	private IRedisService redisService=null;
	
	@RequestMapping(value = "/index")
	public String index(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		return "manage/proxy/proxyclearinglog";
	}
	
	/**
	 * 查询所有洗码日志记录
	 * @param id
	 * @param account
	 * @param name
	 * @param thisPage
	 * @param pageSize
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryClearinglog")
	public @ResponseBody
	Object queryProxyInfo(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "start", required = false) Integer thisPage,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		if(!StringUtils.isBlank(account)){
			params.put("account", account);
		}
		params.put("sortColumns", "g.clearingTime DESC");
		Long count = proxyClearingService.count(params);
		List<ProxyClearingLog> list = proxyClearingService.getList(params, thisPage, pageSize);
		return new GridPanel(count, list, true);
	}
	
	@RequestMapping(value = "/save")
	public @ResponseBody
	Object save(
			@RequestParam(value = "clearingid", required = false) String clearingid,
			@RequestParam(value = "uiid", required = false) String uiid,
			@RequestParam(value = "clearingAmount", required = false) String clearingAmount,
			@RequestParam(value = "rAmount", required = false) String rAmount,
			@RequestParam(value = "rRemark", required = false) String rRemark,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) redisService.getRedisResult(key, c);
			Map<String, Object> params = new HashMap<String, Object>();
			if(!StringUtils.isBlank(clearingid)){
				params.put("clearingid", clearingid);
			}
			if(!StringUtils.isBlank(uiid)){
				params.put("uiid", uiid);
			}
			if(!StringUtils.isBlank(rAmount)){
				params.put("rAmount", rAmount);
			}
			if(!StringUtils.isBlank(rRemark)){
				params.put("rRemark", rRemark);
			}
			ProxyClearingLog entity = proxyClearingService.getObject(params);
			if(null == entity){
				return new ExtReturn(false, "您要入账的数据不存在，请刷新后重试。");
			}
			if(Double.valueOf(rAmount) > Double.valueOf(entity.getClearingAmount())){
				return new ExtReturn(false, "入账失败：您要入账的金额大于结算金额。");
			}
			params.put("upusers", systemUser.getAccount());
			params.put("clientIP", ClientIP.getInstance().getIpAddr(request));
			proxyClearingService.recorded(entity, params);
			return new ExtReturn(true, "成功入账【"+rAmount+"】元。");
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
}
