package com.gameportal.web.user.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.web.activity.model.ActivityFlag;
import com.gameportal.web.activity.service.IActivityFlagService;
import com.gameportal.web.game.handler.IGameServiceHandler;
import com.gameportal.web.game.model.GamePlatform;
import com.gameportal.web.game.model.GameTransfer;
import com.gameportal.web.game.model.PlatformBetStats;
import com.gameportal.web.game.service.IGamePlatformService;
import com.gameportal.web.game.service.IGameTransferService;
import com.gameportal.web.mail.IEmailService;
import com.gameportal.web.mail.model.MailSenderInfo;
import com.gameportal.web.redis.service.IRedisService;
import com.gameportal.web.user.model.AccountMoney;
import com.gameportal.web.user.model.Activity;
import com.gameportal.web.user.model.CardPackage;
import com.gameportal.web.user.model.PayOrder;
import com.gameportal.web.user.model.PayOrderLog;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.user.model.XimaFlag;
import com.gameportal.web.user.service.IReportQueryService;
import com.gameportal.web.user.service.IUserInfoService;
import com.gameportal.web.user.service.IUserPropertyService;
import com.gameportal.web.user.service.IXimaFlagService;
import com.gameportal.web.util.CookieUtil;
import com.gameportal.web.util.DateUtil;
import com.gameportal.web.util.GetProperty;
import com.gameportal.web.util.IdGenerator;
import com.gameportal.web.util.PropertiesUtils;
import com.gameportal.web.util.WebConst;
import com.gameportal.web.util.WebConstants;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/manage")
public class IndexManageController{
	@Resource(name = "userInfoServiceImpl")
	private IUserInfoService userInfoService = null;
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService = null;
	@Resource(name = "activityFlagService")
	private IActivityFlagService activityFlagService;
	@Resource(name = "emailService")
	private IEmailService emailService;
	@Resource(name = "userPropertyService")
	private IUserPropertyService userPropertyService;
	@Resource(name = "gameTransferServiceImpl")
	private IGameTransferService gameTransferService;
	@Resource(name = "ximaFlagService")
	private IXimaFlagService ximaFlagService;
	@Resource(name = "reportQueryServiceImpl")
	private IReportQueryService reportQueryService;
	@Resource(name = "gamePlatformHandlerMap")
	private Map<String, IGameServiceHandler> gamePlatformHandlerMap;
	@Resource(name = "gamePlatformServiceImpl")
	private IGamePlatformService gamePlatformService;
	//@Resource(name = "mgGameAPI")
	//private MgGameAPI mgGameAPI;

	private static final Logger logger = Logger.getLogger(IndexManageController.class);

	public IndexManageController() {
		super();
	}

	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		return "/manage/index/home";
	}

	@RequestMapping("/accountCenter")
	public String accountCenter(HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		userInfo = userInfoService.queryUserInfo(userInfo.getAccount(), null);
		if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
			/**
			 * 计算用户安全指数百分比
			 */
			List<CardPackage> cardPackageList = userInfoService.queryUserInfoCardPackage(userInfo.getUiid(), 0, 0);
			CardPackage cardPackage = null;
			if (cardPackageList != null && cardPackageList.size() > 0) {
				cardPackage = cardPackageList.get(0);
			}
			request.setAttribute("cardPackage", cardPackage);
			int exponent = 100;
			if (null == cardPackage || "".equals(cardPackage)) {
				exponent = exponent - 40;
			}
			UserInfo userdateil = userInfoService.findUserInfoId(userInfo.getUiid());

			if (null == userdateil.getUname() || "".equals(userdateil.getUname())) {
				exponent = exponent - 10;
			}
			if (null == userdateil.getIdentitycard() || "".equals(userdateil.getIdentitycard())) {
				exponent = exponent - 10;
			}
			if (null == userdateil.getPhone() || "".equals(userdateil.getPhone())) {
				exponent = exponent - 10;
			}
			if (null == userdateil.getEmail() || "".equals(userdateil.getEmail())) {
				exponent = exponent - 10;
			}
			if (null == userdateil.getQq() || "".equals(userdateil.getQq())) {
				exponent = exponent - 10;
			}
			if (null == userdateil.getBirthday() || "".equals(userdateil.getBirthday())) {
				exponent = exponent - 10;
			}
			String udateil = null;
			if ((null != userdateil.getUname() && !"".equals(userdateil.getUname()))
					&& (null != userdateil.getIdentitycard() && !"".equals(userdateil.getIdentitycard()))
					&& (null != userdateil.getPhone() && !"".equals(userdateil.getPhone()))
					&& (null != userdateil.getEmail() && !"".equals(userdateil.getEmail()))
					&& (null != userdateil.getQq() && !"".equals(userdateil.getQq()))
					&& (null != userdateil.getBirthday() && !"".equals(userdateil.getBirthday()))) {
				udateil = "true";
			}
			request.setAttribute("udateil", udateil);
			request.setAttribute("exponent", exponent);
		}
		if (userInfo.getEmailvalid() != 1) {
			Properties prop = GetProperty.getProp("activity.properties");
			List<ActivityFlag> activityFlags = activityFlagService.getActivityFlags();
			int count = CollectionUtils.isEmpty(activityFlags) ? 0
					: activityFlags.get(0).getNumbers() + iRedisService.getKeys("*" + WebConst.E_MAIL_QUEUE).size();
			boolean activty_38 = Boolean.valueOf(prop.getProperty("activity.38").toString());// 获取活动状态
			request.setAttribute("activty_38", activty_38);
			int rewardCount = (Integer.valueOf(prop.getProperty("activity.day.reward.numbers")) - count) < 0 ? 0
					: (Integer.valueOf(prop.getProperty("activity.day.reward.numbers")) - count);
			request.setAttribute("places", rewardCount);
		}

		// 如若邮箱session已经过期，用户没有验证邮箱
		if (userInfo.getEmailvalid() == 2) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("uuid", userInfo.getUiid());
			params.put("status", 3); // 待验证
			MailSenderInfo logs = emailService.getObject(params);
			if (null == logs) {
				params.clear();
				params.put("uuid", userInfo.getUiid());
				List<MailSenderInfo> mails = emailService.getMailSenderInfo(params);
				if (!CollectionUtils.isEmpty(mails)) {
					// MailSenderInfo curMail = mails.get(0);
					// if(DateUtil.getBetweenMin(DateUtil.getDateByStr(curMail.getCreateDate()),new
					// Date()) > 120){
					// userInfo.setEmailvalid(0);
					// }
				} else {
					userInfo.setEmailvalid(0);
				}
			} else if (DateUtil.getBetweenMin(DateUtil.getDateByStr(logs.getCreateDate()), new Date()) > 120) {
				logs.setStatus(4);
				emailService.update(logs);
				userInfo.setEmailvalid(0);
				iRedisService.delete(WebConst.E_MAIL_PREFIX + userInfo.getUiid() + WebConst.E_MAIL_QUEUE);
			}
		}
		if(userInfo.getGrade() >1){
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("uiid", userInfo.getUiid());
			param.put("ordertype", 19); // 生日礼金
			param.put("status", 3);
			param.put("createDate", DateUtil.getStrByDate(DateUtil.getCurrYearFirst(), "yyyy-MM-dd")+ " 00:00:00");
			long birthday = userPropertyService.getPayOrder(param);
			request.setAttribute("birthdayFlag", birthday ==1?false:true);
			request.setAttribute("userInfo", userInfo);
		}
		request.setAttribute("userInfo", userInfo);
		return "/manage/account/accountCenter";
	}

	@RequestMapping("/refresh")
	@ResponseBody
	public String refeshData(HttpServletRequest request, HttpServletResponse response) {
		try {
			JSONObject json = new JSONObject();
			Properties prop = GetProperty.getProp("activity.properties");
			List<ActivityFlag> activityFlags = activityFlagService.getActivityFlags();
			int count = CollectionUtils.isEmpty(activityFlags) ? 0
					: activityFlags.get(0).getNumbers() + iRedisService.getKeys("*" + WebConst.E_MAIL_QUEUE).size();
			int rewardCount = (Integer.valueOf(prop.getProperty("activity.day.reward.numbers")) - count) < 0 ? 0
					: (Integer.valueOf(prop.getProperty("activity.day.reward.numbers")) - count);
			json.put("rewardCount", rewardCount);
			return json.toString();
		} catch (Exception e) {
			logger.error("刷新日注册名额异常。" + e);
		}
		return null;
	}

	/**
	 * 领取8-38活动彩金。
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/activityReward")
	@ResponseBody
	public String receiveActivityReward(HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		JSONObject json = new JSONObject();
		try {
			json.put("success", false);
			Properties prop = GetProperty.getProp("activity.properties");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("createDate", DateUtil.getStrByDate(new Date(), "yyyy-MM-dd")+" 00:00:00");
			param.put("endDate", DateUtil.getStrByDate(new Date(), "yyyy-MM-dd")+" 23:59:59");
			param.put("ordertype", 2);
			param.put("status", 3);
			long isValidate = userPropertyService.getPayOrder(param);
			if(Integer.valueOf(isValidate+"")>=Integer.valueOf(prop.getProperty("activity.day.reward.numbers"))){
				json.put("msg", "今日5000个免费彩金已送完，请明日0:00点以后再来领取!");
				return json.toString();
			}
			Class<Object> c = null;
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			if (userInfo != null) {
				userInfo = userInfoService.findUserInfoId(userInfo.getUiid());
			}
			// 完善用户信息
			if(StringUtils.isEmpty(userInfo.getUname())){
				json.put("msg", "您的用户信息还没有完善，请完善后再来领取!");
				return json.toString();
			}
			// 查询会员是否绑定会员卡。
			Map<String, Object> params = new HashMap<>();
			params.put("uiid", userInfo.getUiid());
			List<CardPackage> cardPackages = userInfoService.queryCardPackage(params);
			if(CollectionUtils.isEmpty(cardPackages)){
				json.put("msg", "很抱歉，您还没有绑定银行卡,不能领取礼金哦!");
				return json.toString();
			}
			params.clear();
			params.put("accountname", userInfo.getUname());
			cardPackages = userInfoService.queryCardPackage(params);
			if(CollectionUtils.isNotEmpty(cardPackages) && cardPackages.size()>1){
				json.put("msg", "您属于同姓名会员，随机彩金8-88元已经锁定，累计存款大于300元以上联系客服解锁彩金!");
				return json.toString();
			}
			if (userInfo.getPhonevalid() == 0) {
				json.put("msg", "很抱歉，您的手机号码还没有认证，不能领取礼金哦!");
				return json.toString();
			}
			if (userInfo.getEmailvalid() == 0){
				json.put("msg", "很抱歉，您的邮箱还没有认证，不能领取礼金哦!");
				return json.toString();
			}
			param = new HashMap<String, Object>();
			param.put("uiid", userInfo.getUiid());
			param.put("ordertype", 2); // 认证优惠
			param.put("status", 3);

			long withdrawalActivity = userPropertyService.getPayOrder(param);
			if (withdrawalActivity > 0) {
				json.put("msg", "很抱歉，您已经领取过活动礼金了哦，不能重复领取!");
				return json.toString();
			}
			long cardCount =userInfoService.getCardPackageCount(userInfo.getUiid());
			if(cardCount<1){
				json.put("msg", "很抱歉，您还没有绑定银行卡，不能领取彩金哦!");
				return json.toString();
			}
			if(userInfo.getRelaflag() ==1){
				json.put("msg", "很抱歉，系统检测到其他账号与您的帐号存在IP关联，不能领取彩金哦!");
				return json.toString();
			}
			/**
			param = new HashMap<String, Object>();
			param.put("uiid", userInfo.getUiid());
			param.put("paytyple", 1);
			param.put("status", 3);
			Properties actProp = GetProperty.getProp("activity.properties");
			String createDate = actProp.getProperty("activity.start.time");
			param.put("createDate", createDate);
			long withdrawalCount = userPropertyService.getPayOrder(param);
			if (withdrawalCount <= 0) {
				json.put("msg", "很抱歉，您在" + createDate + "之后未提过款，不能领取礼金哦!");
				return json.toString();
			}
			*/
			// 赠送首提8-38礼金
			Integer reward = 8;
			AccountMoney accountMoney = userInfoService.getAccountMoneyObj(userInfo.getUiid(), 1);
			BigDecimal beforebalance = accountMoney.getTotalamount();
			accountMoney.setTotalamount(accountMoney.getTotalamount().add(new BigDecimal(reward)));
			BigDecimal latrbalance = accountMoney.getTotalamount();
			userInfoService.updateAccountMoneyObj(accountMoney);
			Timestamp date = new Timestamp(new Date().getTime());
			PayOrder payOrder = new PayOrder();
			payOrder.setPoid(IdGenerator.genOrdId16("REWARD"));
			payOrder.setPlatformorders(IdGenerator.genOrdId16(""));
			payOrder.setUiid(userInfo.getUiid());
			payOrder.setUaccount(userInfo.getAccount());
			payOrder.setUrealname(userInfo.getUname());
			payOrder.setPaytyple(2); // 0存款，1提款，2赠送，3扣款
			payOrder.setPpid(-1L);
			payOrder.setPaymethods(0); // 0公司入款，1第三方支付
			payOrder.setDeposittime(date);
			payOrder.setAmount(new BigDecimal(reward));
			payOrder.setPaystatus(0);
			payOrder.setStatus(3);
			payOrder.setOrdertype(2); // 2 认证优惠
			payOrder.setCwremarks("优惠：手机邮箱认证赠送礼金.");
			payOrder.setKfremarks("优惠：手机邮箱认证赠送礼金.");
			payOrder.setCreate_Date(date);
			payOrder.setUpdate_Date(date);
			payOrder.setBeforebalance(beforebalance);
			payOrder.setLaterbalance(latrbalance);
			if(StringUtils.isNotEmpty(userInfo.getPuiid()+"") && userInfo.getPuiid()>0){
				UserInfo pUser = userInfoService.findUserInfoId(userInfo.getPuiid());
				if(pUser !=null){
					payOrder.setProxyname(pUser.getAccount());
				}
			}
			payOrder = gameTransferService.savePayOrder(payOrder);
			// 查询最新洗码状态
			XimaFlag ximaflag = ximaFlagService.getNewestXimaFlag(payOrder.getUiid());
			if(ximaflag == null || ximaflag.getIsxima() !=0){
				ximaflag = new XimaFlag();
				ximaflag.setFlaguiid(payOrder.getUiid());
				ximaflag.setFlagaccount(payOrder.getUaccount());
				ximaflag.setIsxima(0);
				ximaflag.setRemark("手机邮箱认证赠送礼金");
				ximaFlagService.save(ximaflag);
			}
			// 新增帐变记录。
			PayOrderLog log = new PayOrderLog();
			log.setUiid(payOrder.getUiid());
			log.setOrderid(payOrder.getPlatformorders());
			log.setAmount(payOrder.getAmount());
			log.setType(payOrder.getOrdertype());
			log.setWalletlog(beforebalance + ">>>" + latrbalance);
			log.setRemark("手机邮箱认证赠送礼金");
			log.setCreatetime(DateUtil.getStrByDate(payOrder.getUpdate_Date(), DateUtil.TIME_FORMAT));
			userPropertyService.insertPayLog(log);
			json.put("success", true);
			json.put("money", accountMoney.getTotalamount());
			json.put("msg", "恭喜您，领取礼金成功，祝君游戏愉快!");
			return json.toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return json.toString();
	}

	@RequestMapping(value = "/userdata")
	public String userdata(HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		try {
			Class<Object> c = null;
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
				UserInfo userMsg = userInfoService.queryUserInfo(userInfo.getAccount(), null);
				if (null != userMsg) {
					if (!StringUtils.isNotBlank(userMsg.getUname())) {
						userMsg.setUname(null);
					}
					if (!StringUtils.isNotBlank(userMsg.getIdentitycard())) {
						userMsg.setIdentitycard(null);
					}
					if (!StringUtils.isNotBlank(userMsg.getPhone())) {
						userMsg.setPhone(null);
					}
					request.setAttribute("userMsg", userMsg);
				}
			}else{
				return "home/index";
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return "/manage/account/personal";
	}

	@RequestMapping(value = "/modifyUserInfo", method = RequestMethod.POST)
	public @ResponseBody String modifyUserInfo(@RequestParam(value = "truename", required = false) String turename,
			@RequestParam(value = "identitycard", required = false) String identitycard,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "birthday", required = false) String birthday,
			@RequestParam(value = "QQ", required = false) String qq,
			@RequestParam(value = "code", required = false) String vcode, HttpServletRequest request,
			HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		String token = (String) request.getSession().getAttribute(WebConst.AGENT_TOKEN_CODE);
		JSONObject json = new JSONObject();
		if (vcode == null || "".equals(vcode) || !vcode.equals(token)) {
			json.put("code", "9");
			json.put("info", "验证码错误！");
			return json.toString();
		}
		try {
			Class<Object> c = null;
			long count = 0;
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
				UserInfo userMsg = userInfoService.queryUserInfo(userInfo.getAccount(), null);
				Map<String, Object> params = new HashMap<String, Object>();
				if ((null == userMsg.getUname() || "".equals(userMsg.getUname()) && StringUtils.isNotBlank(turename))) {
					// params.put("uname", turename);
					// params.put("neuiid", userInfo.getUiid());
					// count = userInfoService.getUserInfoCount(params);
					// if(count >0){
					// json.put("code", "0");
					// json.put("info", "该真实姓名已被其他会员绑定，一个真实姓名只能绑定一个会员!");
					// return json.toString();
					// }
					userMsg.setUname(turename);
				}
				if (StringUtils.isNotEmpty(identitycard)) {
					params.clear();
					params.put("identitycard", identitycard);
					params.put("neuiid", userInfo.getUiid());
					count = userInfoService.getUserInfoCount(params);
					if (count > 0) {
						json.put("code", "0");
						json.put("info", "该身份证号码已被其他会员绑定，一个身份证号码只能绑定一个会员!");
						return json.toString();
					}
					userMsg.setIdentitycard(identitycard);
				}
				if (StringUtils.isNotEmpty(phone)) {
					params.clear();
					params.put("phone", phone);
					params.put("neuiid", userInfo.getUiid());
					count = userInfoService.getUserInfoCount(params);
					if (count > 0) {
						json.put("code", "0");
						json.put("info", "该手机号码已被其他会员绑定!");
						return json.toString();
					}
				}
				if (userMsg.getPhonevalid() == 0 && StringUtils.isNotBlank(phone)) {
					userMsg.setPhone(phone);
				}
				if (userMsg.getEmailvalid() == 0 && StringUtils.isNotBlank(email)) {
					userMsg.setEmail(email);
				}
				userMsg.setBirthday(birthday);
				if (StringUtils.isNotEmpty(qq)) {
					params.clear();
					params.put("qq", qq);
					params.put("neuiid", userInfo.getUiid());
					count = userInfoService.getUserInfoCount(params);
					if (count > 0) {
						json.put("code", "0");
						json.put("info", "该QQ号码已被其他会员绑定，一个QQ号码只能绑定一个会员!");
						return json.toString();
					}
					userMsg.setQq(qq);
				}
				userMsg.setUpdateDate(new Date());
				boolean code = userInfoService.modifyUserInfo(userMsg);
				if (code == true) {
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
	 * 领取救援金。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{type}/getHelpMoney")
	public String getHelpMoney(@PathVariable String type, HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		userInfo = userInfoService.queryUserInfo(userInfo.getAccount(), null);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uiid", userInfo.getUiid());
		map.put("ordertype", 7); // 救援金
		map.put("status", 3); // 成功
		map.put("startTime", DateUtil.getYesterday(new Date(), 0) + " 00:00:00");
		map.put("endTime", DateUtil.getYesterday(new Date(), 0) + " 23:59:59");
		List<PayOrder> payOrders = reportQueryService.queryPayOrder(map);
		if (CollectionUtils.isNotEmpty(payOrders)) {
			request.setAttribute("isHelp", false);
		} else {
			request.setAttribute("isHelp", true);
		}
		request.setAttribute("userInfo", userInfo);
		request.setAttribute("type", type);
		return "/manage/index/getHelpMoney";
	}

	/**
	 * 首提优惠领取。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/firstWithdrawPrize")
	public String firstWithdrawPrize(HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		userInfo = userInfoService.queryUserInfo(userInfo.getAccount(), null);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("uiid", userInfo.getUiid());
		param.put("ordertype", 2);
		param.put("status", 3);
		long withdrawalFlag = userPropertyService.getPayOrder(param);
		request.setAttribute("withdrawalFlag", withdrawalFlag);
		request.setAttribute("userInfo", userInfo);
		return "/manage/index/firstWithdrawPrize";
	}

	/**
	 * 领取PT救援金。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getHelp")
	@ResponseBody
	public String getHelp(Integer type, HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		JSONObject json = new JSONObject();
		try {
			Class<Object> c = null;
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			userInfo = userInfoService.queryUserInfo(userInfo.getAccount(), null);
			Map<String, Object> map = new HashMap<String, Object>();
			AccountMoney accountMoney = userInfoService.getAccountMoneyObj(userInfo.getUiid(), 1);
			if (accountMoney.getTotalamount().intValue() > 5) {
				json.put("code", "0");
				json.put("msg", "您目前的钱包余额大于5元，不能申请救援金哦。");
				return json.toString();
			}
			map = new HashMap<String, Object>();
            map.put("uiid", userInfo.getUiid());
            map.put("paytyple", 1); // 提款
            map.put("status", "1,2");
            int sumAmount = (int) reportQueryService.sumPayOrder(map);
            if (sumAmount > 0) {
                json.put("code", "0");
                json.put("msg", "很抱歉，您有正在审核的提款记录，请稍后再发起该申请，如有疑问，请联系客服！");
                return json.toString();
            }
            map.clear();
			map.put("uiid", userInfo.getUiid());
			map.put("ordertype", 7); // 救援金
			map.put("status", 3);
			map.put("startDateStr", DateUtil.getToday());
			map.put("endDateStr", DateUtil.getToday());
			sumAmount = (int) reportQueryService.sumPayOrder(map);
			if (sumAmount >= 5888) { // 每日救援金上限5888
				json.put("code", "0");
				json.put("msg", "您今日领取救援金总额已到达上限5888，如有疑问，请联系客服。");
				return json.toString();
			}
			map.remove("status");
			map.put("startTime", DateUtil.getToday() + " 00:00:00");
			map.put("endTime", DateUtil.getToday() + " 23:59:59");
			map.put("sortColumns", "create_date desc");
			List<PayOrder> payOrders = reportQueryService.queryPayOrder(map);
			PayOrder payOrder = null;
			int helpAmount = 0;
			if (CollectionUtils.isNotEmpty(payOrders)) {
				for (PayOrder po : payOrders) {
					if (po.getStatus() == 1) {
						payOrder = po;
						break;
					}
					if (po.getStatus() == 3) {
						if (StringUtils.isNotEmpty(po.getRemarks())) {
							helpAmount += Integer.valueOf(po.getRemarks()); // 统计之前统计救援金的总金额。
						}
					}
				}
				if (payOrder != null) {
					if (payOrder.getStatus() == 1) {
						json.put("code", "0");
						json.put("msg", "您已发起申请救援金的请求，风控部门正在为您审核,请稍后。");
						return json.toString();
					}
				}
			}
			// 充值总额
			map = new HashMap<String, Object>();
			map.put("uiid", userInfo.getUiid());
			map.put("paytyple", 0);
			map.put("status", 3);
			map.put("exhdnumber","1,2,3,4,7,8,9,15,16");
			map.put("startDateStr", DateUtil.getToday());
			map.put("endDateStr", DateUtil.getToday());
			long desposit = reportQueryService.sumPayOrder(map);
			// 优惠金额
//			map.clear();
//			map.put("uiid", userInfo.getUiid());
//			map.put("paytyple", 2);
//			map.put("status", 3);
//			map.put("startDateStr", DateUtil.getToday());
//			map.put("endDateStr", DateUtil.getToday());
//			long preferential = reportQueryService.sumPayOrder(map);
			// 次次存20%排除
//			map.clear();
//			map.put("uiid", userInfo.getUiid());
//			map.put("paytyple", 0);
//			map.put("exhdnumber", '6');
//			map.put("status", 3);
//			map.put("startDateStr", DateUtil.getToday());
//			map.put("endDateStr", DateUtil.getToday());
//			long ccAmount = reportQueryService.sumPayOrder(map);
			// 提款总额
			map.remove("exhdnumber");
			map.put("paytyple", 1);
			long withdraw = reportQueryService.sumPayOrder(map);
			long yk = desposit - withdraw - helpAmount;
			if (yk < 200L) {
				json.put("code", "0");
				json.put("msg", "很抱歉，您今天负盈利值小余200元，不能申请救援金哦。");
				return json.toString();
			} else {
				int helpMoney = 0;
				PayOrder hdRecord = new PayOrder();
				if (type == 1) {
					helpMoney = Double.valueOf(yk * Double.valueOf(0.05)).intValue();
					if ((helpMoney + sumAmount) >= 5888) {
						helpMoney = 5888 - sumAmount;
					}
					hdRecord.setKfremarks(DateUtil.getMonth(new Date()) + "月" + (DateUtil.getDay(new Date())) + "日老虎机救援金"
							+ yk + "x5%=" + helpMoney + "元,10倍流水");
				} else if (type == 2) {
					helpMoney = Double.valueOf(yk * Double.valueOf(0.1)).intValue();
					if ((helpMoney + sumAmount) >= 5888) {
						helpMoney = 5888 - sumAmount;
					}
					hdRecord.setKfremarks(DateUtil.getMonth(new Date()) + "月" + (DateUtil.getDay(new Date())) + "日老虎机救援金"
							+ yk + "x10%=" + helpMoney + "元,12倍流水");
				} else {
					helpMoney = Double.valueOf(yk * Double.valueOf(0.2)).intValue();
					if ((helpMoney + sumAmount) >= 5888) {
						helpMoney = 5888 - sumAmount;
					}
					hdRecord.setKfremarks(DateUtil.getMonth(new Date()) + "月" + (DateUtil.getDay(new Date())) + "日老虎机救援金"
							+ yk + "x20%=" + helpMoney + "元,20倍流水");
				}
				String serialID = IdGenerator.genOrdId16("REWARD");
				hdRecord.setPoid(serialID);
				hdRecord.setUiid(userInfo.getUiid());
				hdRecord.setUaccount(userInfo.getAccount());
				hdRecord.setUrealname(userInfo.getUname());
				hdRecord.setPaytyple(2);
				hdRecord.setDeposittime(new Date());
				hdRecord.setAmount(new BigDecimal(helpMoney));
				hdRecord.setRemarks(String.valueOf(yk));
				hdRecord.setPaystatus(0);
				hdRecord.setStatus(1);
				hdRecord.setOrdertype(7);
				hdRecord.setCreate_Date(new Date());
				hdRecord.setUpdate_Date(new Date());
				BigDecimal beforebalance = accountMoney.getTotalamount();
				hdRecord.setBeforebalance(beforebalance);
				BigDecimal latrbalance = beforebalance.add(new BigDecimal(helpMoney));
				hdRecord.setLaterbalance(latrbalance);
				if(StringUtils.isNotEmpty(userInfo.getPuiid()+"") && userInfo.getPuiid()>0){
					UserInfo pUser = userInfoService.findUserInfoId(userInfo.getPuiid());
					if(pUser !=null){
						hdRecord.setProxyname(pUser.getAccount());
					}
				}
				gameTransferService.savePayOrder(hdRecord);
				// 更新洗码状态 -移到后台财务审核处更新。
				json.put("code", "1");
				json.put("msg", "恭喜，您领取救援金已申请成功，风控部门马上为您审核。");
				return json.toString();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			logger.error("会员领取救援金异常：", e);
			json.put("code", "0");
			json.put("msg", "系统忙，请稍后重试。");
		}
		return json.toString();
	}
	
	
	/**
	 * 领取live救援金。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getLiveHelp")
	@ResponseBody
	public String getLiveHelp(Integer type, HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		JSONObject json = new JSONObject();
		try {
			Class<Object> c = null;
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			userInfo = userInfoService.queryUserInfo(userInfo.getAccount(), null);
			Map<String, Object> map = new HashMap<String, Object>();
			AccountMoney accountMoney = userInfoService.getAccountMoneyObj(userInfo.getUiid(), 1);
			if (accountMoney.getTotalamount().intValue() > 5) {
				json.put("code", "0");
				json.put("msg", "您目前的钱包余额大于5元，不能申请救援金哦。");
				return json.toString();
			}
			map = new HashMap<String, Object>();
		    map.put("uiid", userInfo.getUiid());
            map.put("paytyple", 1); // 提款
            map.put("status", "1,2");
            int sumAmount = (int) reportQueryService.sumPayOrder(map);
            if (sumAmount > 0) {
                json.put("code", "0");
                json.put("msg", "很抱歉，您有正在审核的提款记录，请稍后再发起该申请，如有疑问，请联系客服！");
                return json.toString();
            }
            map.clear();
			map.put("uiid", userInfo.getUiid());
			map.put("ordertype", 7); // 救援金
			map.put("status", 3);
			map.put("startDateStr", DateUtil.getToday());
			map.put("endDateStr", DateUtil.getToday());
			sumAmount = (int) reportQueryService.sumPayOrder(map);
			if (sumAmount >= 5888) { // 每日救援金上限5888
				json.put("code", "0");
				json.put("msg", "您今日领取救援金总额已到达上限5888，如有疑问，请联系客服。");
				return json.toString();
			}
			map.remove("status");
			map.put("startTime", DateUtil.getToday() + " 00:00:00");
			map.put("endTime", DateUtil.getToday() + " 23:59:59");
			map.put("sortColumns", "create_date desc");
			List<PayOrder> payOrders = reportQueryService.queryPayOrder(map);
			PayOrder payOrder = null;
			int helpAmount = 0;
			if (CollectionUtils.isNotEmpty(payOrders)) {
				for (PayOrder po : payOrders) {
					if (po.getStatus() == 1) {
						payOrder = po;
						break;
					}
					if (po.getStatus() == 3) {
						if (StringUtils.isNotEmpty(po.getRemarks())) {
							helpAmount += Integer.valueOf(po.getRemarks()); // 统计之前统计救援金的总金额。
						}
					}
				}
				if (payOrder != null) {
					if (payOrder.getStatus() == 1) {
						json.put("code", "0");
						json.put("msg", "您已发起申请救援金的请求，风控部门正在为您审核,请稍后。");
						return json.toString();
					}
				}
			}
			// 充值总额
			map = new HashMap<String, Object>();
			map.put("uiid", userInfo.getUiid());
			map.put("paytyple", 0);
			map.put("status", 3);
			map.put("exhdnumber","1,2,3,4,7,8,9,11,12,13");
			map.put("startDateStr", DateUtil.getToday());
			map.put("endDateStr", DateUtil.getToday());
			long desposit = reportQueryService.sumPayOrder(map);
			
			// 提款总额
			map.remove("exhdnumber");
			map.put("paytyple", 1);
			long withdraw = reportQueryService.sumPayOrder(map);
			long yk = desposit - withdraw - helpAmount;
			if (yk < 500L) {
				json.put("code", "0");
				json.put("msg", "很抱歉，您今天负盈利值小余500元，不能申请救援金哦。");
				return json.toString();
			} else {
				if(yk>=500L && yk<9999L && type>1){
					json.put("code", "0");
					json.put("msg", "很抱歉，您今天负盈利值小余9999元，救援金比例只能选择3%哦。");
					return json.toString();
				}
				if(yk>=9999L && yk<99999L && type>2){
					json.put("code", "0");
					json.put("msg", "很抱歉，您今天负盈利值小余99999元，救援金比例只能选择4%哦。");
					return json.toString();
				}
				int helpMoney = 0;
				PayOrder hdRecord = new PayOrder();
				if (type == 1) {
					helpMoney = Double.valueOf(yk * Double.valueOf(0.03)).intValue();
					if ((helpMoney + sumAmount) >= 5888) {
						helpMoney = 5888 - sumAmount;
					}
					hdRecord.setKfremarks(DateUtil.getMonth(new Date()) + "月" + (DateUtil.getDay(new Date())) + "日百家乐救援金"
							+ yk + "x3%=" + helpMoney + "元,8倍流水");
				} else if (type == 2) {
					helpMoney = Double.valueOf(yk * Double.valueOf(0.04)).intValue();
					if ((helpMoney + sumAmount) >= 5888) {
						helpMoney = 5888 - sumAmount;
					}
					hdRecord.setKfremarks(DateUtil.getMonth(new Date()) + "月" + (DateUtil.getDay(new Date())) + "日百家乐救援金"
							+ yk + "x4%=" + helpMoney + "元,8倍流水");
				} else {
					helpMoney = Double.valueOf(yk * Double.valueOf(0.05)).intValue();
					if ((helpMoney + sumAmount) >= 5888) {
						helpMoney = 5888 - sumAmount;
					}
					hdRecord.setKfremarks(DateUtil.getMonth(new Date()) + "月" + (DateUtil.getDay(new Date())) + "日百家乐救援金"
							+ yk + "x5%=" + helpMoney + "元,8倍流水");
				}
				String serialID = IdGenerator.genOrdId16("REWARD");
				hdRecord.setPoid(serialID);
				hdRecord.setUiid(userInfo.getUiid());
				hdRecord.setUaccount(userInfo.getAccount());
				hdRecord.setUrealname(userInfo.getUname());
				hdRecord.setPaytyple(2);
				hdRecord.setDeposittime(new Date());
				hdRecord.setAmount(new BigDecimal(helpMoney));
				hdRecord.setRemarks(String.valueOf(yk));
				hdRecord.setPaystatus(0);
				hdRecord.setStatus(1);
				hdRecord.setOrdertype(7);
				hdRecord.setCreate_Date(new Date());
				hdRecord.setUpdate_Date(new Date());
				BigDecimal beforebalance = accountMoney.getTotalamount();
				hdRecord.setBeforebalance(beforebalance);
				BigDecimal latrbalance = beforebalance.add(new BigDecimal(helpMoney));
				hdRecord.setLaterbalance(latrbalance);
				if(StringUtils.isNotEmpty(userInfo.getPuiid()+"") && userInfo.getPuiid()>0){
					UserInfo pUser = userInfoService.findUserInfoId(userInfo.getPuiid());
					if(pUser !=null){
						hdRecord.setProxyname(pUser.getAccount());
					}
				}
				gameTransferService.savePayOrder(hdRecord);
				// 更新洗码状态 -移到后台财务审核处更新。
				json.put("code", "1");
				json.put("msg", "恭喜，您领取救援金已申请成功，风控部门马上为您审核。");
				return json.toString();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			logger.error("会员领取救援金异常：", e);
			json.put("code", "0");
			json.put("msg", "系统忙，请稍后重试。");
		}
		return json.toString();
	}
	
	/**
	 * 进入抢红包页面。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/gotoRedReward")
	public String gotoRedReward(HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		userInfo = userInfoService.queryUserInfo(userInfo.getAccount(), null);
		request.setAttribute("userInfo", userInfo);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", 1);
		map.put("aid", 10);
		List<Activity> hbActivity= userInfoService.getList(map);
		map.clear();
		map.put("uiid", userInfo.getUiid());
		map.put("ordertype", 13); // 抢红包
		map.put("status", 3); // 成功
		map.put("startTime", DateUtil.getToday() + " 00:00:00");
		map.put("endTime", DateUtil.getToday() + " 23:59:59");
		List<PayOrder> payOrders = reportQueryService.queryPayOrder(map);
		if (CollectionUtils.isNotEmpty(payOrders)) {
			request.setAttribute("isPanlic", false);
		}else{
			request.setAttribute("isPanlic", true);
		}
		if(CollectionUtils.isEmpty(hbActivity)){
			request.setAttribute("isClose", true);
			return "/manage/index/getRedReward";
		}else{
			Date openDate = DateUtil.getDateByStr(hbActivity.get(0).getUptime());
			request.setAttribute("startTime", hbActivity.get(0).getUptime());
			if(openDate.after(new Date())){
				request.setAttribute("isStart", false);
				return "/manage/index/getRedReward";
			}
			request.setAttribute("isStart", true);
			if (CollectionUtils.isNotEmpty(payOrders)) {
				return "/manage/index/getRedReward";
			} else {
				// 计算已领取人数
				map.clear();
				map.put("ordertype", 13); // 抢红包
				map.put("status", 3); // 成功
				map.put("startTime", hbActivity.get(0).getUptime());
				payOrders = reportQueryService.queryPayOrder(map);
				if(CollectionUtils.isNotEmpty(payOrders)){
					if(Double.valueOf(hbActivity.get(0).getMaxmoney()).intValue() == payOrders.size()){
						request.setAttribute("isFull", true);
					}else{
						int number = (Double.valueOf(hbActivity.get(0).getMaxmoney()).intValue()-payOrders.size())*9;
						request.setAttribute("number", number < 0 ? 0 : number);
					}
				}else{
					request.setAttribute("number", (Double.valueOf(hbActivity.get(0).getMaxmoney()).intValue())*9);
				}
			}
		}
		request.setAttribute("startTime", hbActivity.get(0).getUptime());
		return "/manage/index/getRedReward";
	}
	
	/**
	 * 刷新剩余名额
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/refreshRedRewardNumber")
	@ResponseBody
	public String refreshRedRewardNumber(HttpServletRequest request, HttpServletResponse response){
		JSONObject json = new JSONObject();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", 1);
			map.put("aid", 10);
			List<Activity> hbActivity= userInfoService.getList(map);
			map.put("ordertype", 13); // 抢红包
			map.put("status", 3); // 成功
			map.put("startTime", hbActivity.get(0).getUptime());
			List<PayOrder> payOrders = reportQueryService.queryPayOrder(map);
			json.put("code", 1);
			if(CollectionUtils.isEmpty(payOrders)){
				json.put("number", (Double.valueOf(hbActivity.get(0).getMaxmoney()).intValue())*9);
			}else{
				if(Double.valueOf(hbActivity.get(0).getMaxmoney()).intValue()<=payOrders.size()){
					json.put("number", 0);
				}else{
					int number = (Double.valueOf(hbActivity.get(0).getMaxmoney()).intValue()-payOrders.size())*9;
					json.put("number", number < 0 ? 0 : number);
				}
			}
		} catch (Exception e) {
			json.put("code", 0);
			json.put("msg", "网络异常，请稍后重试！");
		}
		return json.toString();
	}
	
	/**
	 * 领取存款红包。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getRedReward")
	@ResponseBody
	public String getRedReward(Integer type, HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		JSONObject json = new JSONObject();
		try {
			Class<Object> c = null;
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			userInfo = userInfoService.queryUserInfo(userInfo.getAccount(), null);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", 1);
			map.put("aid", 10);
			List<Activity> hbActivity= userInfoService.getList(map);
			if (CollectionUtils.isEmpty(hbActivity)) {
				json.put("code", "0");
				json.put("msg", "红包领取已结束，请下次再来抢吧，如有疑问，请联系客服。");
				return json.toString();
			}
			Date openDate = DateUtil.getDateByStr(hbActivity.get(0).getUptime());
			if(openDate.after(new Date())){
				json.put("code", "0");
				json.put("msg", "本轮红包领取尚未开始，请稍后，如有疑问，请联系客服。");
				return json.toString();
			}
			map.clear();
			map.put("ordertype", 13); // 抢红包
			map.put("status", 3); // 成功
			map.put("startTime", hbActivity.get(0).getUptime());
			List<PayOrder> payOrders = reportQueryService.queryPayOrder(map);
			if (CollectionUtils.isNotEmpty(payOrders) && payOrders.size()>=Double.valueOf(hbActivity.get(0).getMaxmoney()).intValue()) {
				json.put("code", "0");
				json.put("msg", "很抱歉，您的手太慢了哦，本轮红包已抢完。");
				return json.toString();
			}
			map.clear();
			map.put("uiid", userInfo.getUiid());
			map.put("ordertype", 13); // 抢红包
			map.put("status", 3); // 成功
			map.put("startTime", hbActivity.get(0).getUptime());
			map.put("endTime", DateUtil.getToday() + " 23:59:59");
			payOrders = reportQueryService.queryPayOrder(map);
			if (CollectionUtils.isNotEmpty(payOrders)) {
				json.put("code", "0");
				json.put("msg", "您今天已经领取过红包了，不能重复领取哦，如有疑问，请联系客服。");
				return json.toString();
			}
			// 充值总额
			map = new HashMap<String, Object>();
			map.put("uiid", userInfo.getUiid());
			map.put("paytyple", 0);
			map.put("status", 3);
			map.put("startDateStr", DateUtil.getStrByDate(DateUtil.addDay(new Date(), -4), "yyyy-MM-dd"));
			long desposit = reportQueryService.sumPayOrder(map);
			double minAmount = Double.valueOf(PropertiesUtils.loadProperties("activity.properties").getProperty("activity.hb.amount"));
			if(desposit<minAmount){
				json.put("code", "0");
				json.put("msg", "您的存款少于"+minAmount+"元，不能领取红包哦，如有疑问，请联系客服。");
				return json.toString();
			}
			AccountMoney am =userInfoService.getAccountMoneyObj(userInfo.getUiid(), 1);
			if(am !=null && am.getTotalamount().doubleValue()>2){
				json.put("code", "0");
				json.put("msg", "您的钱包余额大于2元，不能领取红包哦，如有疑问，请联系客服。");
				return json.toString();
			}
			map = new HashMap<String, Object>();
			map.put("uuid", userInfo.getUiid());
			map.put("status", 1);
			map.put("sortColumns", "create_date desc");
			List<GameTransfer> transfers =gameTransferService.selectTranferOrder(map);
			double otherBalance =0;
			if(CollectionUtils.isNotEmpty(transfers)){
				GameTransfer transfer = transfers.get(0);
				try {
					if(transfer.getTogpid() !=0){
						IGameServiceHandler gameHandler = gamePlatformHandlerMap.get(WebConst.getPlatName(transfer.getTogpid().intValue()));
						GamePlatform gamePlatform =null;
//						if(transfer.getTogpid() ==2){
//							JSONObject balanceJson=mgGameAPI.getAccountBalance(userInfo.getAccount());
//							if("0".equals(balanceJson.getString("code"))){
//								otherBalance = new BigDecimal(balanceJson.getString("balance")).doubleValue();
//							}
//						}else{
							gamePlatform = gamePlatformService.queryGamePlatform(WebConst.getPlatName(transfer.getTogpid().intValue()));
							otherBalance =Double.valueOf(String.valueOf(gameHandler.queryBalance(userInfo, gamePlatform, null)));
//						}
					}
				} catch (Exception e) {
					logger.error("抢红包查询第三方游戏余额异常。",e);
					otherBalance =0;
				}
			}
			
			if(am !=null && (am.getTotalamount().doubleValue()+otherBalance)>2){
				json.put("code", "0");
				json.put("msg", "您的钱包总余额大于2元，不能领取红包哦，如有疑问，请联系客服。");
				return json.toString();
			}
			PayOrder hdRecord = new PayOrder();
			String serialID = IdGenerator.genOrdId16("REWARD");
			hdRecord.setPoid(serialID);
			hdRecord.setUiid(userInfo.getUiid());
			hdRecord.setUaccount(userInfo.getAccount());
			hdRecord.setUrealname(userInfo.getUname());
			hdRecord.setPaytyple(2);
			hdRecord.setDeposittime(new Date());
			Activity activity = hbActivity.get(0);
			hdRecord.setAmount(new BigDecimal(activity.getRewmoney()));
			hdRecord.setKfremarks(activity.getHdtext());
			hdRecord.setCwremarks(activity.getHdtext());
			hdRecord.setPaystatus(0);
			hdRecord.setStatus(3);
			hdRecord.setOrdertype(13);
			hdRecord.setCreate_Date(new Date());
			hdRecord.setUpdate_Date(new Date());
			AccountMoney accountMoney = userInfoService.getAccountMoneyObj(userInfo.getUiid(), 1);
			BigDecimal beforebalance = accountMoney.getTotalamount();
			accountMoney.setTotalamount(beforebalance.add(hdRecord.getAmount()));
			BigDecimal latrbalance = accountMoney.getTotalamount();
			// 更新钱包余额
			userInfoService.updateAccountMoneyObj(accountMoney);
			
			hdRecord.setBeforebalance(beforebalance);
			hdRecord.setLaterbalance(latrbalance);
			if(StringUtils.isNotEmpty(userInfo.getPuiid()+"") && userInfo.getPuiid()>0){
				UserInfo pUser = userInfoService.findUserInfoId(userInfo.getPuiid());
				if(pUser !=null){
					hdRecord.setProxyname(pUser.getAccount());
				}
			}
			gameTransferService.savePayOrder(hdRecord);
			
			// 查询最新洗码状态
			XimaFlag ximaflag = ximaFlagService.getNewestXimaFlag(userInfo.getUiid());
			if(ximaflag == null || ximaflag.getIsxima() !=activity.getIsxima()){
				ximaflag = new XimaFlag();
				ximaflag.setFlaguiid(userInfo.getUiid());
				ximaflag.setFlagaccount(userInfo.getAccount());
				ximaflag.setIsxima(activity.getIsxima());
				ximaflag.setRemark(hdRecord.getCwremarks());
				ximaFlagService.save(ximaflag);
			}
			// 新增帐变记录。
			PayOrderLog log = new PayOrderLog();
			log.setUiid(hdRecord.getUiid());
			log.setOrderid(hdRecord.getPlatformorders());
			log.setAmount(hdRecord.getAmount());
			log.setType(2); //优惠赠送
			log.setWalletlog(beforebalance + ">>>" + latrbalance);
			log.setRemark(hdRecord.getCwremarks());
			log.setCreatetime(DateUtil.getStrByDate(hdRecord.getUpdate_Date(), DateUtil.TIME_FORMAT));
			userPropertyService.insertPayLog(log);
						
			json.put("code", "1");
			json.put("money", accountMoney.getTotalamount());
			json.put("msg", "恭喜，红包您已领取成功，祝您生活愉快。");
			return json.toString();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			logger.error("红包您已领取异常：", e);
			json.put("code", "0");
			json.put("msg", "系统忙，请稍后重试。");
		}
		return json.toString();
	}
	
	
	/**
	 * 进入每日签到彩金页面
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/signIn")
	public String signIn(HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("uiid", userInfo.getUiid());
		param.put("ordertype", 17); // 每日签到彩金
		param.put("status", 3);
		param.put("createDate", DateUtil.getToday() + " 00:00:00");
		long signReward = userPropertyService.getPayOrder(param);
		request.setAttribute("isSign", signReward > 0 ? true : false);
		return "/manage/index/signIn";
	}

	/**
	 * 积分兑换界面。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/points")
	public String goSlotsWeekRebate(HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		AccountMoney accountMoney = userInfoService.getAccountMoneyObj(userInfo.getUiid(), 1);
		request.setAttribute("integral", accountMoney.getIntegral());
		return "/manage/index/slotsWeekRebate";
	}
	
	/**
	 * 积分兑换。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getSlotsWeekRebate")
	@ResponseBody
	public String getSlotsWeekRebate(HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		JSONObject json = new JSONObject();
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		userInfo = userInfoService.queryUserInfo(userInfo.getAccount(), null);
		PayOrder hdRecord = new PayOrder();
		AccountMoney accountMoney = userInfoService.getAccountMoneyObj(userInfo.getUiid(), 1);
		int rate =WebConstants.integralMap.get(userInfo.getGrade());
		int reward =accountMoney.getIntegral()/rate;
		if(reward<=0){
			json.put("code", "0");
			json.put("msg", "很抱歉，目前您的积分不足，无法兑换哦。");
			return json.toString();
		}
		int afterIntegral = accountMoney.getIntegral()-reward*rate;
		try {
			String serialID = IdGenerator.genOrdId16("REWARD");
			hdRecord.setPoid(serialID);
			hdRecord.setUiid(userInfo.getUiid());
			hdRecord.setUaccount(userInfo.getAccount());
			hdRecord.setUrealname(userInfo.getUname());
			hdRecord.setPaytyple(2);
			hdRecord.setDeposittime(new Date());
			hdRecord.setStatus(3);
			hdRecord.setOrdertype(18); // 积分兑换
			hdRecord.setCreate_Date(new Date());
			hdRecord.setUpdate_Date(new Date());
			hdRecord.setPaystatus(0);
			BigDecimal beforebalance = accountMoney.getTotalamount();
			hdRecord.setBeforebalance(beforebalance);
			hdRecord.setAmount(new BigDecimal(reward));
			BigDecimal latrbalance = beforebalance.add(new BigDecimal(reward));
			hdRecord.setLaterbalance(latrbalance);
			hdRecord.setKfremarks("兑换前积分："+accountMoney.getIntegral()+"->兑换后积分："+afterIntegral);
			hdRecord.setCwremarks(hdRecord.getKfremarks());
			if(StringUtils.isNotEmpty(userInfo.getPuiid()+"") && userInfo.getPuiid()>0){
				UserInfo pUser = userInfoService.findUserInfoId(userInfo.getPuiid());
				if(pUser !=null){
					hdRecord.setProxyname(pUser.getAccount());
				}
			}
			gameTransferService.savePayOrder(hdRecord);
			accountMoney.setTotalamount(latrbalance);
			accountMoney.setIntegral(afterIntegral);
			
			// 新增帐变记录。
            PayOrderLog log = new PayOrderLog();
            log.setUiid(hdRecord.getUiid());
            log.setOrderid(hdRecord.getPlatformorders());
            log.setAmount(new BigDecimal(reward));
            log.setType(2);
            log.setWalletlog(beforebalance+">>>"+latrbalance);
            log.setRemark("积分兑换");
            log.setCreatetime(DateUtil.getStrByDate(hdRecord.getUpdate_Date(),DateUtil.TIME_FORMAT));
            userPropertyService.insertPayLog(log);
			
			userInfoService.updateAccountMoneyObj(accountMoney);
			json.put("code", "1");
			json.put("msg", "恭喜，积分兑换成功，请再接再厉哦。");
			return json.toString();
		} catch (Exception e) {
			logger.error("领取老虎机周返利异常。", e);
		}
		return json.toString();
	}
	
	/**
	 * 申请生日礼金
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getBirthDayMoney")
	@ResponseBody
	public String getBirthDayMoney(HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		JSONObject json = new JSONObject();
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		userInfo = userInfoService.queryUserInfo(userInfo.getAccount(), null);
		PayOrder hdRecord = new PayOrder();
		AccountMoney accountMoney = userInfoService.getAccountMoneyObj(userInfo.getUiid(), 1);
		if(userInfo.getGrade() ==1){
			json.put("code", "0");
			json.put("msg", "很抱歉，新会员不能申请生日礼金哦。");
			return json.toString();
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("uiid", userInfo.getUiid());
		param.put("ordertype", 19); // 生日礼金
		param.put("status", 3);
		param.put("createDate", DateUtil.getStrByDate(DateUtil.getCurrYearFirst(), "yyyy-MM-dd")+ " 00:00:00");
		long birthday = userPropertyService.getPayOrder(param);
		if(birthday>0){
			json.put("code", "0");
			json.put("msg", "很抱歉，您今年已经申请过生日礼金了，不能再次申请哦。");
			return json.toString();
		}
		param.clear();
		param.put("uiid", userInfo.getUiid());
		param.put("kfremarks", "生日");
		param.put("createDate", DateUtil.getStrByDate(DateUtil.getCurrYearFirst(), "yyyy-MM-dd")+ " 00:00:00");
		birthday = userPropertyService.getPayOrder(param);
		if(birthday>0){
			json.put("code", "0");
			json.put("msg", "很抱歉，您今年已经申请过生日礼金了，不能再次申请哦。");
			return json.toString();
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			if(StringUtils.isEmpty(userInfo.getBirthday())){
				json.put("code", "0");
				json.put("msg", "很抱歉，请完善资料信息。");
				return json.toString();
			}
			if(!DateUtil.getStrByDate(sdf.parse(userInfo.getBirthday()),"MM-dd").equals(DateUtil.getStrByDate(new Date(),"MM-dd"))){
				json.put("code", "0");
				json.put("msg", "很抱歉，今天不是您的生日，不能再次申请哦。");
				return json.toString();
			}
			int reward = WebConstants.birthdayMap.get(userInfo.getGrade());
			String serialID = IdGenerator.genOrdId16("REWARD");
			hdRecord.setPoid(serialID);
			hdRecord.setUiid(userInfo.getUiid());
			hdRecord.setUaccount(userInfo.getAccount());
			hdRecord.setUrealname(userInfo.getUname());
			hdRecord.setPaytyple(2);
			hdRecord.setDeposittime(new Date());
			hdRecord.setStatus(1);
			hdRecord.setOrdertype(19); // 生日礼金
			hdRecord.setCreate_Date(new Date());
			hdRecord.setUpdate_Date(new Date());
			hdRecord.setPaystatus(0);
			BigDecimal beforebalance = accountMoney.getTotalamount();
			hdRecord.setBeforebalance(beforebalance);
			hdRecord.setAmount(new BigDecimal(reward));
			BigDecimal latrbalance = beforebalance.add(new BigDecimal(reward));
			hdRecord.setLaterbalance(latrbalance);
			hdRecord.setKfremarks("当前等级："+WebConstants.levelMap.get(userInfo.getGrade())+" 生日礼金："+reward);
			hdRecord.setCwremarks(hdRecord.getKfremarks());
			if(StringUtils.isNotEmpty(userInfo.getPuiid()+"") && userInfo.getPuiid()>0){
				UserInfo pUser = userInfoService.findUserInfoId(userInfo.getPuiid());
				if(pUser !=null){
					hdRecord.setProxyname(pUser.getAccount());
				}
			}
			gameTransferService.savePayOrder(hdRecord);
//			accountMoney.setTotalamount(new BigDecimal(reward));
			
			// 新增帐变记录。
//            PayOrderLog log = new PayOrderLog();
//            log.setUiid(hdRecord.getUiid());
//            log.setOrderid(hdRecord.getPlatformorders());
//            log.setAmount(new BigDecimal(reward));
//            log.setType(2);
//            log.setWalletlog(beforebalance+">>>"+latrbalance);
//            log.setRemark("生日礼金");
//            log.setCreatetime(DateUtil.getStrByDate(hdRecord.getUpdate_Date(),DateUtil.TIME_FORMAT));
//            userPropertyService.insertPayLog(log);
//			
//			userInfoService.updateTotalamount(accountMoney);
			json.put("code", "1");
			json.put("msg", "恭喜，我们已收到您的生日礼金申请，风控人员将马上为您审核。");
			return json.toString();
		} catch (Exception e) {
			logger.error("领取生日礼金异常。", e);
		}
		return json.toString();
	}
	
	
	/**
	 * 会员晋级
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/upGrade")
	@ResponseBody
	public String upGrade(HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		JSONObject json = new JSONObject();
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		userInfo = userInfoService.queryUserInfo(userInfo.getAccount(), null);
		PayOrder hdRecord = new PayOrder();
		AccountMoney accountMoney = userInfoService.getAccountMoneyObj(userInfo.getUiid(), 1);
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("uiid", userInfo.getUiid());
		param.put("ordertype", 20); // 晋级礼金
		param.put("status", 3);
		param.put("createDate", DateUtil.getStrByDate(DateUtil.getFirstDay(new Date()), "yyyy-MM-dd")+ " 00:00:00");
		long birthday = userPropertyService.getPayOrder(param);
		if(birthday>0){
			json.put("code", "0");
			json.put("msg", "很抱歉，本月您已经晋级了，不能再次晋级。");
			return json.toString();
		}
		try {
			int reward = 0;
			boolean flag = false;
			param.clear();
			param.put("account", userInfo.getAccount());
			param.put("startDateStr", DateUtil.getStrByDate(DateUtil.getFirstDay(new Date()), "yyyy-MM-dd"));
			param.put("endDateStr", DateUtil.getStrByDate(new Date(), "yyyy-MM-dd"));
			List<PlatformBetStats> dataList =gamePlatformService.selectBetFromBetlogByUser(param);
			BigDecimal totalBet =BigDecimal.ZERO;
			Map<String, BigDecimal> betMap = new HashMap<>();
			if(userInfo.getGrade() ==1){
				for (PlatformBetStats bet : dataList) {
					totalBet= totalBet.add(bet.getValidBetAmount());
					betMap.put(bet.getPlatform(), bet.getValidBetAmount());
				}
				//总投注额大于30w，所有老虎机投注额10w，当月累计存款5w
				param.clear();
				param.put("uiid", userInfo.getUiid());
				param.put("paytyple", 0);
				param.put("status", 3);
				param.put("startDateStr", DateUtil.getStrByDate(DateUtil.getFirstDay(new Date()), "yyyy-MM-dd"));
				long desposit = reportQueryService.sumPayOrder(param);
				logger.info(userInfo.getAccount()+"申请晋级，"+betMap.toString()+"，本月总存款："+desposit);
				if(desposit >=50000){
					flag =true;
				}else if(totalBet.doubleValue() >=300000){
					flag =true;
				}else if((betMap.containsKey("PT") && betMap.get("PT").doubleValue()>=100000) && 
						(betMap.containsKey("MG") && betMap.get("MG").doubleValue()>=100000) &&
						(betMap.containsKey("SA") && betMap.get("SA").doubleValue()>=100000)){
					flag =true;
				}
			}else if(userInfo.getGrade() ==2){
				for (PlatformBetStats bet : dataList) {
					totalBet= totalBet.add(bet.getValidBetAmount());
					betMap.put(bet.getPlatform(), bet.getValidBetAmount());
				}
				//总投注额大于120w，所有老虎机投注额40w，当月累计存款20w
				param.clear();
				param.put("uiid", userInfo.getUiid());
				param.put("paytyple", 0);
				param.put("status", 3);
				param.put("startDateStr", DateUtil.getStrByDate(DateUtil.getFirstDay(new Date()), "yyyy-MM-dd"));
				long desposit = reportQueryService.sumPayOrder(param);
				logger.info(userInfo.getAccount()+"申请晋级，"+betMap.toString()+"，本月总存款："+desposit);
				if(desposit >=200000){
					flag =true;
				}else if(totalBet.doubleValue() >=1200000){
					flag =true;
				}else if((betMap.containsKey("PT") && betMap.get("PT").doubleValue()>=400000) && 
						(betMap.containsKey("MG") && betMap.get("MG").doubleValue()>=400000) &&
						(betMap.containsKey("SA") && betMap.get("SA").doubleValue()>=400000)){
					flag =true;
				}
			}else if(userInfo.getGrade() ==3){
				for (PlatformBetStats bet : dataList) {
					totalBet= totalBet.add(bet.getValidBetAmount());
					betMap.put(bet.getPlatform(), bet.getValidBetAmount());
				}
				//总投注额大于360w，所有老虎机投注额120w，当月累计存款60w
				param.clear();
				param.put("uiid", userInfo.getUiid());
				param.put("paytyple", 0);
				param.put("status", 3);
				param.put("startDateStr", DateUtil.getStrByDate(DateUtil.getFirstDay(new Date()), "yyyy-MM-dd"));
				long desposit = reportQueryService.sumPayOrder(param);
				logger.info(userInfo.getAccount()+"申请晋级，"+betMap.toString()+"，本月总存款："+desposit);
				if(desposit >=600000){
					flag =true;
				}else if(totalBet.doubleValue() >=3600000){
					flag =true;
				}else if((betMap.containsKey("PT") && betMap.get("PT").doubleValue()>=1200000) && 
						(betMap.containsKey("MG") && betMap.get("MG").doubleValue()>=1200000) &&
						(betMap.containsKey("SA") && betMap.get("SA").doubleValue()>=1200000)){
					flag =true;
				}
			}else if(userInfo.getGrade() ==4){
				for (PlatformBetStats bet : dataList) {
					totalBet= totalBet.add(bet.getValidBetAmount());
					betMap.put(bet.getPlatform(), bet.getValidBetAmount());
				}
				//总投注额大于600w，所有老虎机投注额200，当月累计存款100w
				param.clear();
				param.put("uiid", userInfo.getUiid());
				param.put("paytyple", 0);
				param.put("status", 3);
				param.put("startDateStr", DateUtil.getStrByDate(DateUtil.getFirstDay(new Date()), "yyyy-MM-dd"));
				long desposit = reportQueryService.sumPayOrder(param);
				logger.info(userInfo.getAccount()+"申请晋级，"+betMap.toString()+"，本月总存款："+desposit);
				if(desposit >=1000000){
					flag =true;
				}else if(totalBet.doubleValue() >=6000000){
					flag =true;
				}else if((betMap.containsKey("PT") && betMap.get("PT").doubleValue()>=2000000) && 
						(betMap.containsKey("MG") && betMap.get("MG").doubleValue()>=2000000) &&
						(betMap.containsKey("SA") && betMap.get("SA").doubleValue()>=2000000)){
					flag =true;
				}
			}else{
				json.put("code", "0");
				json.put("msg", "恭喜，您目前已拥有最高等级，不能再次升级，感谢您的光临。");
				return json.toString();
			}
			
			if(!flag){
				json.put("code", "0");
				json.put("msg", "很抱歉，本月截止今日您还不满足晋级条件，请继续努力哦。");
				return json.toString();
			}
			String serialID = IdGenerator.genOrdId16("REWARD");
			hdRecord.setPoid(serialID);
			hdRecord.setUiid(userInfo.getUiid());
			hdRecord.setUaccount(userInfo.getAccount());
			hdRecord.setUrealname(userInfo.getUname());
			hdRecord.setPaytyple(2);
			hdRecord.setDeposittime(new Date());
			hdRecord.setStatus(1);
			hdRecord.setOrdertype(20); // 晋级礼金
			hdRecord.setCreate_Date(new Date());
			hdRecord.setUpdate_Date(new Date());
			hdRecord.setPaystatus(0);
			BigDecimal beforebalance = accountMoney.getTotalamount();
			hdRecord.setBeforebalance(beforebalance);
			reward = WebConstants.upGradeMap.get(userInfo.getGrade());
			hdRecord.setAmount(new BigDecimal(reward));
			BigDecimal latrbalance = beforebalance.add(new BigDecimal(reward));
			hdRecord.setLaterbalance(latrbalance);
			hdRecord.setKfremarks("当前等级："+WebConstants.levelMap.get(userInfo.getGrade())+" 晋级礼金："+reward);
			hdRecord.setCwremarks(hdRecord.getKfremarks());
			if(StringUtils.isNotEmpty(userInfo.getPuiid()+"") && userInfo.getPuiid()>0){
				UserInfo pUser = userInfoService.findUserInfoId(userInfo.getPuiid());
				if(pUser !=null){
					hdRecord.setProxyname(pUser.getAccount());
				}
			}
			gameTransferService.savePayOrder(hdRecord);
//			accountMoney.setTotalamount(new BigDecimal(reward));
			
			// 新增帐变记录。
//            PayOrderLog log = new PayOrderLog();
//            log.setUiid(hdRecord.getUiid());
//            log.setOrderid(hdRecord.getPlatformorders());
//            log.setAmount(new BigDecimal(reward));
//            log.setType(2);
//            log.setWalletlog(beforebalance+">>>"+latrbalance);
//            log.setRemark(hdRecord.getKfremarks());
//            log.setCreatetime(DateUtil.getStrByDate(hdRecord.getUpdate_Date(),DateUtil.TIME_FORMAT));
//            userPropertyService.insertPayLog(log);
//            
//            userInfo.setGrade(userInfo.getGrade()+1);
//            userInfoService.updateUserInfo(userInfo);
//			
//			userInfoService.updateTotalamount(accountMoney);
			json.put("code", "1");
			json.put("msg", "恭喜，我们已收到您的晋级申请，工作人员将马上为您审核。");
			return json.toString();
		} catch (Exception e) {
			logger.error("领取晋级礼金异常。", e);
		}
		return json.toString();
	}
	
}
