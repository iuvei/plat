package com.gameportal.web.home.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.web.game.handler.IGameServiceHandler;
import com.gameportal.web.game.model.GamePlatform;
import com.gameportal.web.game.service.IGamePlatformService;
import com.gameportal.web.sms.model.SmsPlatAccount;
import com.gameportal.web.sms.model.SmsSendLog;
import com.gameportal.web.sms.service.ISmsLogService;
import com.gameportal.web.sms.service.ISmsService;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.user.service.IUserInfoService;
import com.gameportal.web.util.Blowfish;
import com.gameportal.web.util.DateUtil;
import com.gameportal.web.util.RandomUtil;
import com.gameportal.web.util.WebConst;

import net.sf.json.JSONObject;

/**
 * 密码找回控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class ForgetpwdController{
	public static final Logger logger = Logger.getLogger(ForgetpwdController.class);
	@Resource(name = "userInfoServiceImpl")
	private IUserInfoService userInfoService;
	@Resource(name = "smsService")
	private ISmsService smsService;
	@Resource(name = "smsLogService")
	private ISmsLogService smsLogService;
	@Resource(name = "gamePlatformServiceImpl")
	private IGamePlatformService gamePlatformService;
	@Resource(name = "gamePlatformHandlerMap")
	private Map<String, IGameServiceHandler> gamePlatformHandlerMap;
	//@Resource(name = "mgGameAPI")
   // private MgGameAPI mgGameAPI;

	/**
	 * 密码找回页面跳转
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/forgetpwd")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		return "/forgetpwd/index";
	}

	/**
	 * 确认找回密码
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/resetLoginPwd")
	@ResponseBody
	public String confirm(UserInfo user, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String code = (String) request.getSession().getAttribute(WebConst.AGENT_TOKEN_CODE);
		if (!user.getKey().equals(code)) {
			json.put("code", "9");
			json.put("info", "验证码错误！");
			return json.toString();
		}
		try {
			UserInfo userInfo = userInfoService.queryUserInfo(user.getAccount(), null);
			if (userInfo == null) {
				json.put("code", "9");
				json.put("info", "账号不存在!");
				return json.toString();
			} else if (userInfo.getStatus() == 0) {
				json.put("code", "9");
				json.put("info", "您的帐号已经禁用，请稍后联系客服!");
				return json.toString();
			}
			if (!userInfo.getPhone().equals(user.getPhone())) {
				json.put("code", "9");
				json.put("info", "联系电话输入错误，如果您已忘记预留的电话号码，请稍后联系客服!");
				return json.toString();
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("type", 2);
			params.put("username", user.getAccount().trim());
			params.put("startTime", DateUtil.getStrByDate(DateUtil.getAddHour(new Date(), -1), DateUtil.TIME_FORMAT));
			params.put("endTime", DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT));
			Long sendTimes = smsLogService.selectSmsLogCount(params);
			if (sendTimes >= 1L) {
				json.put("code", "9");
				json.put("info", "很抱歉，手机短信每小时只能发送一次，如需帮助请联系在线客服!");
				return json.toString();
			}
			String newPwd = new RandomUtil().getRandomCode(6);
			Blowfish bf = new Blowfish(WebConst.DECRYPT_KEY);
			userInfo.setPasswd(bf.encryptString(newPwd));
			// 如果以上验证通过，则调用短信接口发送短信，告知用户新的登录密码。
			String message = "尊敬的会员，新的登陆密码为：" + newPwd + "，您可以前 账户管理-账户中心处修改密码哦，祝您游戏愉快，好运连连!";
			code = smsService.sendSMS(userInfo.getPhone(), message, DateUtil.getStrYMDHMSByDate(new Date()), "2");
			if (!"2000".equals(code)) {
				logger.info("找回密码，发送短信失败。");
				json.put("code", "9");
				json.put("info", "网络异常，请稍后重试。");
				return json.toString();
			}
			// 修改用户登录密码
			userInfoService.updateUserInfo(userInfo);
			// 记录发送短信日志
			SmsPlatAccount sms = smsService.getSmsPlatAccount();
			SmsSendLog log = new SmsSendLog();
			log.setSpaid(sms.getSpaid());
			log.setUsername(userInfo.getAccount());
			log.setMobile(userInfo.getPhone());
			log.setContent(message);
			log.setType(2); // 找回密码
			smsLogService.saveSmsPlat(log);
			logger.info("新的登陆密码为:" + newPwd);
			// 修改PT客户端的登陆密码
			GamePlatform gamePlatform = gamePlatformService.queryGamePlatform(WebConst.PT);
			IGameServiceHandler gameInstance = gamePlatformHandlerMap.get(WebConst.PT);
			if(gamePlatform !=null){
				String result = (String) gameInstance.edit(userInfo, gamePlatform, null);
				if (!"0".equals(result)) {
					logger.info("修改PT客户端登陆密码失败。");
				}
			}
			// 修改MG客户端密码
			gamePlatform = gamePlatformService.queryGamePlatform(WebConst.MG);
			gameInstance = gamePlatformHandlerMap.get(WebConst.MG);
			if(gamePlatform !=null){
				String result = (String) gameInstance.edit(userInfo, gamePlatform, null);
				if (!"0".equals(result)) {
					logger.info("修改MG客户端登陆密码失败。");
				}
			}
			json.put("code", 0);
		} catch (Exception e) {
			logger.error("找回密码出现异常。");
			json.put("code", "9");
			json.put("info", "网络异常，请稍后重试。");
			e.getStackTrace();
		}
		return json.toString();
	}
}
