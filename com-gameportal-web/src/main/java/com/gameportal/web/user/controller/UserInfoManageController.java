package com.gameportal.web.user.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.web.game.handler.IGameServiceHandler;
import com.gameportal.web.game.model.GamePlatform;
import com.gameportal.web.game.service.IGamePlatformService;
import com.gameportal.web.game.service.IGameTransferService;
import com.gameportal.web.redis.service.IRedisService;
import com.gameportal.web.sms.model.SmsPlatAccount;
import com.gameportal.web.sms.model.SmsSendLog;
import com.gameportal.web.sms.service.ISmsLogService;
import com.gameportal.web.sms.service.ISmsService;
import com.gameportal.web.user.model.CardPackage;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.user.service.IUserInfoService;
import com.gameportal.web.util.Blowfish;
import com.gameportal.web.util.CookieUtil;
import com.gameportal.web.util.DateUtil;
import com.gameportal.web.util.RandomUtil;
import com.gameportal.web.util.WebConst;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/manage/user")
public class UserInfoManageController{
	private static final Logger logger = Logger.getLogger(UserInfoManageController.class);
	@Resource(name = "userInfoServiceImpl")
	private IUserInfoService userInfoService = null;
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService = null;
	@Resource(name = "gamePlatformServiceImpl")
	private IGamePlatformService gamePlatformService;
	@Resource(name = "gameTransferServiceImpl")
	private IGameTransferService gameTransferService;
	@Resource(name = "smsService")
	private ISmsService smsService;
	@Resource(name = "smsLogService")
	private ISmsLogService smsLogService;
	@Resource(name = "gamePlatformHandlerMap")
	private Map<String, IGameServiceHandler> gamePlatformHandlerMap;
//	@Resource(name = "mgGameAPI")
//    private MgGameAPI mgGameAPI;

	public UserInfoManageController() {
		super();
	}

	@RequestMapping(value = "/changePwd")
	public String changePwd(HttpServletRequest request, HttpServletResponse response) {
		return "/manage/account/changePwd";
	}

	@RequestMapping(value = "/modifyPasswd")
	public @ResponseBody String modifyPasswd(@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "oldPwd", required = false) String oldPwd,
			@RequestParam(value = "newPwd", required = false) String newPwd,
			@RequestParam(value = "code", required = false) String vcode, HttpServletRequest request,
			HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		JSONObject json = new JSONObject();
		String token = (String) request.getSession().getAttribute(WebConst.AGENT_TOKEN_CODE);
		if (vcode == null || "".equals(vcode) || !vcode.equals(token)) {
			json.put("code", "9");
			json.put("info", "验证码错误！");
			return json.toString();
		}
		String key = vuid + "GAMEPORTAL_USER";
		try {
			Blowfish bf = new Blowfish(WebConst.DECRYPT_KEY);
			Class<Object> c = null;
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
				UserInfo userInfo2 = userInfoService.findUserInfoId(userInfo.getUiid());
				if (type == 1) {
					if (bf.decryptString(userInfo2.getPasswd()).equals(oldPwd)) {
						userInfo2.setPasswd(bf.encryptString(newPwd));
						userInfo2.setUpdateDate(new Date());
						boolean code = userInfoService.modifyUserInfo(userInfo2);
						if (code == true) {
							// 修改PT客户端的登陆密码
							GamePlatform gamePlatform = gamePlatformService.queryGamePlatform(WebConst.PT);
							if(!userInfo2.isNotExist(WebConst.PT)){
								IGameServiceHandler gameInstance = gamePlatformHandlerMap.get(WebConst.PT);
								String result = (String) gameInstance.edit(userInfo2, gamePlatform, null);
								if ("0".equals(result)) {
									logger.info("修改PT客户端登陆密码成功。");
								} else {
									logger.info("修改PT客户端登陆密码失败。");
								}
							}
							if(!userInfo2.isNotExist(WebConst.MG)){
								// 修改MG客户端密码
								gamePlatform = gamePlatformService.queryGamePlatform(WebConst.MG);
								IGameServiceHandler gameInstance = gamePlatformHandlerMap.get(WebConst.MG);
								if(gamePlatform !=null){
//									userInfo2.setPasswd(newPwd);
									String result = (String) gameInstance.edit(userInfo2, gamePlatform, null);
									logger.info("调用MG接口修改登陆密码返回结果:" +result);
									if ("0".equals(result)) {
										logger.info("修改MG客户端登陆密码成功。");
									} else {
										logger.info("修改MG客户端登陆密码失败。");
									}
								}
							}
							json.put("code", "1");
							json.put("info", "操作成功!");
						} else {
							json.put("code", "0");
							json.put("info", "操作失败，请稍后再试!");
						}
					} else {
						json.put("code", "0");
						json.put("info", "您输入的密码错误，请重新输入!");
					}
				} else if (type == 2) {
					if (null == userInfo2.getAtmpasswd() || "".equals(userInfo2.getAtmpasswd())) {
						json.put("code", "0");
						json.put("info", "您还没有设置取款密码，请先设置取款密码!");
						return json.toString();
					}
					if (bf.decryptString(userInfo2.getAtmpasswd()).equals(oldPwd)) {
						userInfo2.setAtmpasswd(bf.encryptString(newPwd));
						userInfo2.setUpdateDate(new Date());
						boolean code = userInfoService.modifyUserInfo(userInfo2);
						if (code == true) {
							json.put("code", "1");
							json.put("info", "操作成功!");
						} else {
							json.put("code", "0");
							json.put("info", "操作失败，请稍后再试!");
						}
					} else {
						json.put("code", "0");
						json.put("info", "您输入的提款密码错误，请重新输入!");
					}
				} else {
					json.put("code", "0");
					json.put("info", "非法操作，请联系客服!");
				}
			}
		} catch (Exception e) {
			json.put("code", "0");
			json.put("info", "网络异常，请稍后再试！");
		}
		return json.toString();
	}

	@RequestMapping(value = "/userBindBank")
	public String userBindBank(HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		// 查询会员最新信息。
		userInfo = userInfoService.findUserInfoId(userInfo.getUiid());
		if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
			Map<String, Object> params = new HashMap<>();
			params.put("accountname", userInfo.getUname());
			List<CardPackage> cardPackageList = userInfoService.queryUserInfoCardPackage(userInfo.getUiid(), 0, 0);
			CardPackage cardPackage = null;
			if (cardPackageList != null && cardPackageList.size() > 0) {
				cardPackage = cardPackageList.get(0);
			}
			request.setAttribute("cardPackage", cardPackage);
		}
		request.setAttribute("userInfo", userInfo);
		return "/manage/account/binding";
	}
	
	@RequestMapping(value = "/cancelWithdraw")
	@ResponseBody
	public String cancelWithdraw(@RequestParam(value = "poid", required = false) String poid,HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> params = new HashMap<>();
		params.put("poid", poid);
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		JSONObject json = new JSONObject();
		// 查询会员最新信息。
		if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
			params.put("uiid", userInfo.getUiid());
			try {
				userInfoService.cancelWithdraw(params);
				json.put("code", "0");
				json.put("info", "撤销提款操作成功!");
			} catch (Exception e) {
				logger.error("撤销提款操作失败。",e);
				json.put("code", "1");
				json.put("info", "撤销提款操作失败!");
			}
		}
		return json.toString();
	}
	
	@RequestMapping(value = "/verfityInfo")
	public String verfityInfo(HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		// 查询会员最新信息。
		userInfo = userInfoService.findUserInfoId(userInfo.getUiid());
		request.setAttribute("userInfo", userInfo);
		return "/manage/account/verfityInfo";
	}

	@RequestMapping(value = "/saveCardPackage")
	public @ResponseBody String saveCardPackage(@RequestParam(value = "bank", required = false) String bank,
			@RequestParam(value = "province", required = false) String province,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "bankDeposit", required = false) String bankDeposit,
			@RequestParam(value = "bankPerson", required = false) String bankPerson,
			@RequestParam(value = "cardNum", required = false) String cardNum,
			@RequestParam(value = "alipayname", required = false) String alipayname,
			@RequestParam(value = "alipay", required = false) String alipay,
			@RequestParam(value = "code", required = false) String vcode, HttpServletRequest request,
			HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		JSONObject json = new JSONObject();
		String uname = "";
		String token = (String) request.getSession().getAttribute(WebConst.AGENT_TOKEN_CODE);
		if (StringUtils.isEmpty(vcode)) {
			json.put("code", "9");
			json.put("info", "请输入验证码！");
			return json.toString();
		}
		if (StringUtils.isNotEmpty(alipay)) {
			token = (String) request.getSession().getAttribute(WebConst.TOKEN_CODE);
			uname = alipayname;
		} else {
			uname = bankPerson;
		}
		if (!token.equals(vcode)) {
			json.put("code", "9");
			json.put("info", "验证码输入错误！");
			return json.toString();
		}
		Map<String, Object> map = new HashMap<>();
		map.put("cardnumber", cardNum);
		List<CardPackage> cardPackages=userInfoService.queryCardPackage(map);
		if(CollectionUtils.isNotEmpty(cardPackages)){
			json.put("code", "9");
			json.put("info", "很抱歉，该卡号已被其他账号绑定！");
			return json.toString();
		}
		String key = vuid + "GAMEPORTAL_USER";
		try {
			Class<Object> c = null;
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			// 查询会员最新信息。
			userInfo = userInfoService.findUserInfoId(userInfo.getUiid());
			if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
				cardPackages=userInfoService.queryUserInfoCardPackage(userInfo.getUiid(), 0, 20);
				CardPackage cardPackage = null;
				if (CollectionUtils.isNotEmpty(cardPackages)) {
					cardPackage = cardPackages.get(0);
					cardPackage.setBankname(bank);
					cardPackage.setProvince(province);
					cardPackage.setCity(city);
					cardPackage.setOpeningbank(bankDeposit);
					cardPackage.setAccountname(bankPerson);
					cardPackage.setCardnumber(cardNum);
					cardPackage.setUpdateDate(new Date());
					cardPackage.setAlipayname(alipayname);
					cardPackage.setAlipay(alipay);
					uname = alipay;
					userInfoService.updateCardPackage(cardPackage);
				} else {
					cardPackage = new CardPackage();
					cardPackage.setUiid(userInfo.getUiid());
					cardPackage.setBankname(bank);
					cardPackage.setProvince(province);
					cardPackage.setCity(city);
					cardPackage.setOpeningbank(bankDeposit);
					cardPackage.setAccountname(bankPerson);
					cardPackage.setCardnumber(cardNum);
					cardPackage.setStatus(1);
					Date date = new Date();
					cardPackage.setCreateDate(date);
					cardPackage.setUpdateDate(date);
					cardPackage.setAlipayname(alipayname);
					cardPackage.setAlipay(alipay);
					cardPackage = userInfoService.saveCardPackage(cardPackage);
				}
				if (userInfo != null && StringUtils.isEmpty(userInfo.getUname())) {
					userInfo.setUname(uname);
					userInfoService.updateUserInfo(userInfo);
					iRedisService.addToRedis(key, userInfo);
				}
				if (StringUtils.isNotBlank(ObjectUtils.toString(cardPackage.getCpid()))) {
					json.put("code", "1");
					json.put("info", "操作成功!");
				} else {
					json.put("code", "0");
					json.put("info", "操作失败，请稍后再试!");
				}
			} else {
				json.put("code", "0");
				json.put("info", "非法操作，请联系客服!");
			}
		} catch (Exception e) {
			json.put("code", "0");
			json.put("info", "网络异常，请稍后再试！");
		}
		return json.toString();
	}

	/**
	 * 账号中心发送手机验证。
	 *
	 * @param phone
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/sendSmsCode")
	@ResponseBody
	public String sendSmsCode(String phone, HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		Class<Object> c = null;
		String key = vuid + "GAMEPORTAL_USER";
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		JSONObject json = new JSONObject();
		try {
			if (userInfo == null) {
				json.put("code", "0");
				json.put("info", "登录超时，请重新登录。");
			}
			if (StringUtils.isEmpty(phone)) {
				json.put("code", "0");
				json.put("info", "请输入您的手机号码。");
			} else {
				userInfo = userInfoService.queryUserInfo(userInfo.getAccount(), null, null, null);
				if (!userInfo.getPhone().equals(phone)) { // 验证时修改手机号码
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("phone", phone);
					param.put("neuiid", userInfo.getUiid());
					long count = userInfoService.getUserInfoCount(param);
					if (count > 0) {
						json.put("code", "0");
						json.put("info", "该手机号码已被其他会员绑定。");
						return json.toString();
					}
				}
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("type", 3);
				params.put("username", userInfo.getAccount().trim());
				params.put("startTime", DateUtil.getStrByDate(DateUtil.getAddHour(new Date(), -1), DateUtil.TIME_FORMAT));
				params.put("endTime", DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT));
				Long sendTimes = smsLogService.selectSmsLogCount(params);
				if (sendTimes >= 1L) {
					json.put("code", "9");
					json.put("info", "很抱歉，手机短信每小时只能发送一次，如需帮助请联系在线客服!");
					return json.toString();
				}
				if (userInfo.getPhonevalid() == 1) {
					json.put("code", "0");
					json.put("info", "该手机号码已经验证通过，不能重复验证。");
					return json.toString();
				}
				String vcode = new RandomUtil().getRandomNumber(6);
				String message = "尊敬的会员，账户中心" + DateUtil.getStrHMSByDate(new Date()) + "发送给您认证码为：" + vcode+",请及时输入验证码进行验证。";
				logger.info("短信发送内容：" + message);
				String code = smsService.sendSMS(phone, message, "0", "2");
				if (!"2000".equals(code)) {
					logger.info("账户中心发送短信验证码【" + vcode + "】失败");
					json.put("code", "0");
					json.put("info", "网络异常，请稍后重试。");
				} else {
					json.put("code", "1");
					if (iRedisService.keyExists(phone + "_" + userInfo.getAccount() + "_VCODE")) {
						iRedisService.delete(phone + "_" + userInfo.getAccount() + "_VCODE");
					}
					iRedisService.addToRedis(phone + "_" + userInfo.getAccount() + "_VCODE", vcode);
					// 记录发送短信日志
					SmsPlatAccount sms = smsService.getSmsPlatAccount();
					SmsSendLog log = new SmsSendLog();
					log.setSpaid(sms.getSpaid());
					log.setUsername(userInfo.getAccount());
					log.setMobile(userInfo.getPhone());
					log.setContent(message);
					log.setType(3); // 账户中心验证
					smsLogService.saveSmsPlat(log);
				}
			}
		} catch (Exception e) {
			logger.error("账户户中心--发送手机验证码异常。", e);
			json.put("code", "0");
			json.put("info", "网络异常，请稍后再试！");
		}
		return json.toString();
	}

	/**
	 * 账户中心验证手机号码。
	 *
	 * @param phone
	 * @param vcode
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/verificationPhone")
	@ResponseBody
	public String verificationPhone(String phone, String vcode, HttpServletRequest request,
			HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		Class<Object> c = null;
		String key = vuid + "GAMEPORTAL_USER";
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		JSONObject json = new JSONObject();
		try {
			if (userInfo == null) {
				json.put("code", "0");
				json.put("info", "登录超时，请重新登录。");
			}
			if (StringUtils.isEmpty(phone)) {
				json.put("code", "0");
				json.put("info", "请输入您的手机号码。");
			}
			// 防止用户获取验证码后修改手机号码
			if (!iRedisService.keyExists(phone + "_" + userInfo.getAccount() + "_VCODE")) {
				json.put("code", "0");
				json.put("info", "手机号码与验证码不匹配。");
				return json.toString();
			} else {
				String code = String
						.valueOf(iRedisService.getRedisResult(phone + "_" + userInfo.getAccount() + "_VCODE", c));
				// 获取最新用户信息
				userInfo = userInfoService.queryUserInfo(userInfo.getAccount(), null);

				if (!userInfo.getPhone().equals(phone)) { // 验证时修改手机号码
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("phone", phone);
					param.put("neuiid", userInfo.getUiid());
					long count = userInfoService.getUserInfoCount(param);
					if (count > 0) {
						json.put("code", "0");
						json.put("info", "该手机号码已被其他会员绑定。");
						return json.toString();
					}
				}
				// 防止用户重复认证
				if (userInfo.getPhonevalid() == 1) {
					json.put("code", "0");
					json.put("info", "该手机号码已经验证通过，不能重复验证。");
					return json.toString();
				}
				if (!code.equals(vcode)) {
					json.put("code", "0");
					json.put("info", "短信验证码输入错误。");
					return json.toString();
				} else {
					userInfo.setPhone(phone);
					userInfo.setPhonevalid(1);
					userInfoService.updateUserInfo(userInfo);
				}
				iRedisService.addToRedis(key, userInfo);
				json.put("code", "1");
				json.put("info", "手机号码认证成功。");
			}
		} catch (Exception e) {
			logger.info("手机号码认证失败。", e);
			json.put("code", "0");
			json.put("info", "手机号码认证失败。");
		}
		return json.toString();
	}
}
