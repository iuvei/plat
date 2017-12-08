package com.gameportal.manage.proxy.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.manage.member.model.MemberInfo;
import com.gameportal.manage.member.service.IMemberInfoService;
import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.proxy.model.ProxySet;
import com.gameportal.manage.proxy.service.IProxySetService;
import com.gameportal.manage.proxydomain.model.ProxyDomian;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.util.ClientIP;
import com.gameportal.manage.util.CookieUtil;
import com.gameportal.manage.util.DateUtil2;
import com.gameportal.manage.util.WebConstants;

@Controller
@RequestMapping(value = "/manage/proxyset")
public class ProxySetController {
	
	public static final Logger logger = Logger.getLogger(ProxySetController.class);

	@Resource(name = "proxySetService")
	private IProxySetService proxySetService;
	
	@Resource(name = "memberInfoServiceImpl")
	private IMemberInfoService memberInfoService = null;
	
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
		return "manage/proxy/proxyset";
	}
	
	@RequestMapping(value = "/queryProxySet")
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
		Long count = proxySetService.count(params);
		List<ProxySet> list = proxySetService.getList(params, thisPage, pageSize);
		return new GridPanel(count, list, true);
	}
	
	@RequestMapping(value = "/save")
	public @ResponseBody
	Object save(
			@RequestParam(value = "pyid", required = false) String pyid,
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "returnscale", required = false) String returnscale,
			@RequestParam(value = "isximaflag", required = false) Integer isximaflag,
			@RequestParam(value = "clearingtype", required = false) Integer clearingtype,
			@RequestParam(value = "ximascale", required = false) String ximascale,
			HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + WebConstants.FRAMEWORK_USER;
		Class<Object> c = null;
		SystemUser systemUser = (SystemUser) redisService.getRedisResult(key, c);
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isBlank(account)){
			return new ExtReturn(false, "请输入代理账号。");
		}
		params.put("account", account);
		params.put("accounttype", 0);//查询不等于0
		MemberInfo proxyInfo = memberInfoService.qeuryMemberInfo(params);
		if(null == proxyInfo){
			return new ExtReturn(false, "代理账号【"+account+"】不存在，请检查后重新输入！");
		}
		ProxySet upObject = proxySetService.queryObject(params);
		if(StringUtils.isBlank(pyid)){
			if(upObject != null){
				return new ExtReturn(false, "代理账号【"+account+"】占成设置已存在，不能重复设置！");
			}
			ProxySet addObject = new ProxySet();
			addObject.setUiid(proxyInfo.getUiid().intValue());
			addObject.setReturnscale(returnscale);
			addObject.setXimascale(ximascale);
			addObject.setIsximaflag(isximaflag);
			addObject.setClearingtype(clearingtype);
			addObject.setCreatetime(DateUtil2.format2(new Date()));
			addObject.setUptime(DateUtil2.format2(new Date()));
			addObject.setUpuser(systemUser.getAccount());
			addObject.setStatus(1);
			addObject.setUpclientip(ClientIP.getInstance().getIpAddr(request));
			if(proxySetService.save(addObject)){
				return new ExtReturn(true, "成功添加代理占成。");
			}else{
				return new ExtReturn(false, "设置代理占成失败。");
			}
		}else{
			params.clear();
			params.put("pyid", pyid);
			if(upObject == null){
				return new ExtReturn(false, "您编辑的代理占成数据不存在，请刷新后重试！");
			}
			upObject.setReturnscale(returnscale);
			upObject.setXimascale(ximascale);
			upObject.setCreatetime(DateUtil2.format2(new Date()));
			upObject.setIsximaflag(isximaflag);
			upObject.setClearingtype(clearingtype);
			upObject.setUptime(DateUtil2.format2(new Date()));
			upObject.setUpuser(systemUser.getAccount());
			upObject.setUpclientip(ClientIP.getInstance().getIpAddr(request));
			if(proxySetService.update(upObject)){
				return new ExtReturn(true, "成功修改代理占成。");
			}else{
				return new ExtReturn(false, "修改代理占成失败。");
			}
		}
	}
	
	@RequestMapping("/bindproxyset/{id}/{status}")
	@ResponseBody
	public Object bindomain(@PathVariable Long id,@PathVariable Integer status) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(id))) {
				return new ExtReturn(false, "域名主键不能为空！");
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pyid", id);
			ProxySet entity = proxySetService.queryObject(params);
			if(null == entity || "".equals(entity)){
				return new ExtReturn(false, "您要修改的代理占成数据不存在，请刷新后重试！");
			}
			if(status==1){
				entity.setStatus(0);
			}else{
				entity.setStatus(1);
			}
			if (proxySetService.update(entity)) {
				return new ExtReturn(true, "修改代理占成数据状态成功！");
			} else {
				return new ExtReturn(false, "修改代理占成数据状态失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping("/delproxyset/{id}")
	@ResponseBody
	public Object delProxyInfo(@PathVariable Long id) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(id))) {
				return new ExtReturn(false, "代理主键不能为空！");
			}
			if (proxySetService.delete(id)) {
				return new ExtReturn(true, "删除代理占成数据成功！");
			} else {
				return new ExtReturn(false, "删除代理占成数据失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
}
