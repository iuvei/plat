package com.gameportal.manage.userproxy.controller;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.manage.listener.SystemFieldsCache;
import com.gameportal.manage.member.model.MemberInfo;
import com.gameportal.manage.member.service.IMemberInfoService;
import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.system.service.ISystemService;
import com.gameportal.manage.util.Blowfish;
import com.gameportal.manage.util.CookieUtil;
import com.gameportal.manage.util.DateConvertEditor;
import com.gameportal.manage.util.WebConstants;

@Controller
public class ProxyLoginController {
	
	private static final Logger logger = Logger.getLogger(ProxyLoginController.class);
	
	@Resource(name = "systemServiceImpl")
	private ISystemService iSystemService = null;
	
	@Resource(name = "memberInfoServiceImpl")
	private IMemberInfoService memberInfoService = null;
	
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService = null;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new DateConvertEditor());
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	
	@RequestMapping(value = "/proxy/index")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		// Map<String, String> map = iSystemService.qeuryAllFields();
		request.setAttribute("fields", SystemFieldsCache.fields);
		return "login/proxy/index";
	}
	
	@RequestMapping(value = "/proxy/login", method = RequestMethod.POST)
	public @ResponseBody
	Object login(@RequestParam String account, @RequestParam String password,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			if (StringUtils.isBlank(account)) {
				return JSONObject.fromObject(new ExtReturn(false, "帐号不能为空！"))
						.toString();
			}
			if (StringUtils.isBlank(password)) {
				return JSONObject.fromObject(new ExtReturn(false, "密码不能为空！"))
						.toString();
			}
			Map<String,  Object> params = new HashMap<String, Object>();
			params.put("account", account);
			MemberInfo member = memberInfoService.qeuryMemberInfo(params);
			if(null == member){
				return new ExtReturn(false, "用户账号不存在，请检查后重新输入。");
			}
			Blowfish bf = new Blowfish(WebConstants.BLOWFISH_CODE);
			if(!bf.decryptString(member.getPasswd()).equals(password)){
				return new ExtReturn(false, "用户账号或密码错误，请检查后重新输入。");
			}
			if(member.getStatus() == 0){
				return new ExtReturn(false, "您的账号已被禁用，如有疑问请联系客服。");
			}
			if(member.getAccounttype() == 0){
				return new ExtReturn(false, "用户账号或密码错误，请联系客服。");
			}
			
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
			CookieUtil.setCookie(request, response, "PROXY_USERNAME",
					URLEncoder.encode(member.getUname(), "UTF-8"));
			iRedisService.addToRedis(key, member);//保持1小时
			return new ExtReturn(true, "success");
		}catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping(value = "/proxy/logout")
	private String logout(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
			Class<Object> c = null;
			if (iRedisService.delete(key)) {
				response.sendRedirect(request.getContextPath() + "/proxy/index.html");
			}
			return null;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}
}
