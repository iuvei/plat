package com.gameportal.manage.system.controller;

import java.net.URLEncoder;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.manage.listener.SystemFieldsCache;
import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.system.model.SystemUserLoginLog;
import com.gameportal.manage.system.service.ILoginService;
import com.gameportal.manage.system.service.ISystemService;
import com.gameportal.manage.system.service.ISystemUserLoginLogService;
import com.gameportal.manage.util.ClientIP;
import com.gameportal.manage.util.CookieUtil;
import com.gameportal.manage.util.DateConvertEditor;
import com.gameportal.manage.util.DateUtil2;
import com.gameportal.manage.util.ValidateCode;
import com.gameportal.manage.util.WebConstants;

import net.sf.json.JSONObject;

/**
 * 
 * @ClassName: IndexController
 * @Description: TODO(代码生成器-首页)
 * @author: 佘佳 -- shejia@gz-mstc.com
 * @date 2014-4-4 下午2:47:58
 * @Copyright © 2014 广州市品高软件开发有限公司.
 */
@Controller
public class LoginController {
	/** 限制时间 */
	@Value("${limit.millis:3600000}")
	private Long millis = null;
	@Resource(name = "loginServiceImpl")
	private ILoginService iLoginService = null;
	@Resource(name = "systemServiceImpl")
	private ISystemService iSystemService = null;
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService = null;
	@Resource(name = "systemUserLoginLogService")
	private ISystemUserLoginLogService systemUserLoginLogService;
	private static final Logger logger = Logger
			.getLogger(LoginController.class);

	public LoginController() {
		super();
		// TODO Auto-generated constructor stub
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new DateConvertEditor());
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		// Map<String, String> map = iSystemService.qeuryAllFields();
		request.setAttribute("fields", SystemFieldsCache.fields);
		return "login/index";
	}

	@RequestMapping(value = "/getValidateCode")
	public void getValidateCode(HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		String sRand = ValidateCode.getImg(request, response);
		if (sRand != null && !"".equals(sRand)) {
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_IMGCODE;
			if (vuid != null && !"".equals(vuid)) {
				if (iRedisService.keyExists(key)) {
					iRedisService.delete(key);
				}
				// 保存验证码10分钟
				iRedisService.setObjectFromRedis(key, sRand, 600);
			}
		}

	}

	/**
	 * 用户登录
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
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
			SystemUser systemUser = iLoginService.queryBySystemUser(account,
					password);
			
			if (!StringUtils.isNotBlank(ObjectUtils.toString(systemUser))) {
				return new ExtReturn(false, "用户名或者密码错误!");
			}
			if(systemUser.getBindstatus() == 1){
				String[] clientip = ClientIP.getInstance().getIpAddr(request).split(",");
				String[] bindIp = systemUser.getClientip().split(",");
				if(null != clientip && null != bindIp){
					boolean isLogin = false;
					for(String cip : clientip){
						for(String bip : bindIp){
							if(cip.equals(bip)){
								isLogin = true;
								break;
							}
						}
					}
					if(!isLogin){
						return new ExtReturn(false, "您的登录IP与绑定IP不符，不能登录。");
					}
				}
			}
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			CookieUtil.setCookie(request, response, "USERNAME",
					URLEncoder.encode(systemUser.getRealName(), "UTF-8"));
			
			iRedisService.addToRedis(key, systemUser,7200);//保持1小时
			
			SystemUserLoginLog syslog = new SystemUserLoginLog();
			syslog.setLoginaccount(systemUser.getAccount());
			syslog.setLoginname(systemUser.getTruename());
			syslog.setLoginrole(systemUser.getRealName());
			syslog.setLoginip(ClientIP.getInstance().getIpAddr(request));
			syslog.setLogintime(DateUtil2.format2(new Date()));
			syslog.setLoginsource(request.getHeader("User-Agent"));
			systemUserLoginLogService.insert(syslog);
			logger.info("{}登陆成功" + systemUser.getRealName());
			return new ExtReturn(true, "success");
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping(value = "/logout")
	private String logout(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + "FRAMEWORK_USER";
			Class<Object> c = null;
			if (iRedisService.delete(key)) {
				response.sendRedirect(request.getContextPath() + "/index.html");
			}
			return null;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}
}
