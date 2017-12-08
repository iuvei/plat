package com.gameportal.mobile;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.web.activity.model.ActivityFlag;
import com.gameportal.web.activity.service.IActivityFlagService;
import com.gameportal.web.game.model.GameAccount;
import com.gameportal.web.game.model.GamePlatform;
import com.gameportal.web.game.service.IGamePlatformService;
import com.gameportal.web.game.service.IGameTransferService;
import com.gameportal.web.mail.IEmailService;
import com.gameportal.web.order.model.CompanyCard;
import com.gameportal.web.order.service.ICompanyCardService;
import com.gameportal.web.payPlat.model.PayPlatform;
import com.gameportal.web.payPlat.service.IPayPlatformService;
import com.gameportal.web.redis.service.IRedisService;
import com.gameportal.web.user.model.AccountMoney;
import com.gameportal.web.user.model.Activity;
import com.gameportal.web.user.model.CardPackage;
import com.gameportal.web.user.model.PayOrder;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.user.service.IReportQueryService;
import com.gameportal.web.user.service.IUserInfoService;
import com.gameportal.web.user.service.IUserPropertyService;
import com.gameportal.web.user.service.IXimaFlagService;
import com.gameportal.web.util.Blowfish;
import com.gameportal.web.util.CookieUtil;
import com.gameportal.web.util.DateUtil;
import com.gameportal.web.util.IdGenerator;
import com.gameportal.web.util.RandomUtil;
import com.gameportal.web.util.WebConst;

import net.sf.json.JSONObject;

/**
 * 手机版用户中信息
 * 
 * @author add by sum 2016.08.09
 *
 */
@Controller
@RequestMapping(value = "/mobile")
public class MobileMcontroller {
	private static final Logger logger = Logger.getLogger(MobileMcontroller.class);
	@Resource(name = "userInfoServiceImpl")
	private IUserInfoService userInfoService = null;
	@Resource(name = "gamePlatformServiceImpl")
	private IGamePlatformService gamePlatformService;
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
	@Resource(name = "companyCardServiceImpl")
	private ICompanyCardService companyCardService;
	@Resource(name = "payPlatformService")
	private IPayPlatformService PayPlatformService;

	/**
	 * 进入个人中心
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/accountmp", method = RequestMethod.GET)
	public String accountCenter(HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		userInfo = userInfoService.queryUserInfo(userInfo.getAccount(), null);
		if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
			AccountMoney accountMoney = userInfoService.getAccountMoneyObj(userInfo.getUiid(), 1);
			List<GameAccount> gameAccountList = null;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("status", 1);
			List<GamePlatform> listGame = gamePlatformService.queryGame(params);
			if (null != listGame && listGame.size() > 0) {
				gameAccountList = new ArrayList<GameAccount>();
				GameAccount gameAccount = null;
				for (GamePlatform game : listGame) {
					gameAccount = new GameAccount();
					gameAccount.setMoney(0);
					gameAccount.setFullname(game.getFullname());
					gameAccount.setUname(game.getGpname());
					gameAccountList.add(gameAccount);
				}
			}
			request.setAttribute("gameList", gameAccountList);
			request.setAttribute("accountMoney", accountMoney.getTotalamount().intValue());
		} else {
			return "/mobile/main";
		}
		request.setAttribute("userInfo", userInfo);
		return "/mobile/user/index";
	}
	
	/**
	 * 进入信息认证页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/verifymp")
	public String infoVerify(HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		userInfo = userInfoService.queryUserInfo(userInfo.getAccount(), null);
		request.setAttribute("verifyUser", userInfo);
		return "/mobile/user/verify";
	}

	/**
	 * 进入个人资料页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/personalmp", method = RequestMethod.GET)
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
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return "/mobile/user/personal";
	}
	
	/**
     * 修改用户信息
     * @param turename
     * @param identitycard
     * @param phone
     * @param email
     * @param birthday
     * @param qq
     * @param vcode
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/modifyUserInfo",method = RequestMethod.POST)
    public @ResponseBody
    String modifyUserInfo(
            @RequestParam(value = "truename", required = false) String turename,
            @RequestParam(value = "identitycard", required = false) String identitycard,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "birthday", required = false) String birthday,
            @RequestParam(value = "QQ", required = false) String qq,
            @RequestParam(value = "code", required = false) String vcode,
            HttpServletRequest request, HttpServletResponse response) {
        String vuid = CookieUtil.getOrCreateVuid(request, response);
        String key = vuid + "GAMEPORTAL_USER";
        String token = (String)request.getSession().getAttribute(WebConst.AGENT_TOKEN_CODE);
        JSONObject json = new JSONObject();
        if(vcode == null || "".equals(vcode) || !vcode.equals(token)){
            json.put("code", "9");
            json.put("info", "验证码错误！");
            return json.toString();
        }
        try {
            Class<Object> c = null;
            long count =0;
            UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
            if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
                UserInfo userMsg = userInfoService.queryUserInfo(userInfo.getAccount(), null);
                Map<String, Object> params = new HashMap<String, Object>();
                if((StringUtils.isNotEmpty(turename))){
                    userMsg.setUname(URLDecoder.decode(turename,"utf8"));
                }
                if(StringUtils.isNotEmpty(identitycard)){
                    params.clear();
                    params.put("identitycard", identitycard);
                    params.put("neuiid", userInfo.getUiid());
                    count = userInfoService.getUserInfoCount(params);
                    if(count >0){
                        json.put("code", "0");
                        json.put("info", "该身份证号码已被其他会员绑定，一个身份证号码只能绑定一个会员!");
                        return json.toString();
                    }
                    userMsg.setIdentitycard(identitycard);
                }
                if(StringUtils.isNotEmpty(phone)){
                	 params.clear();
                     params.put("phone", phone);
                     params.put("neuiid", userInfo.getUiid());
                     count = userInfoService.getUserInfoCount(params);
                     if(count >0){
                         json.put("code", "0");
                         json.put("info", "该手机号被其他会员绑定!");
                         return json.toString();
                     }
                }
                if(userMsg.getPhonevalid() ==0 && StringUtils.isNotBlank(phone)){
                    userMsg.setPhone(phone);
                }
                if(userMsg.getEmailvalid() ==0 && StringUtils.isNotBlank(email)){
                    userMsg.setEmail(email);
                }
                userMsg.setBirthday(birthday);
                if(StringUtils.isNotEmpty(qq)){
                    params.clear();
                    params.put("qq", qq);
                    params.put("neuiid", userInfo.getUiid());
                    count = userInfoService.getUserInfoCount(params);
                    if(count >0){
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
	 * 手机绑卡页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/bindingmp", method = RequestMethod.GET)
	public String binding(HttpServletRequest request, HttpServletResponse response) {
		return "/mobile/user/binding";
	}

	/**
	 * 手机端绑定银行卡页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/bindCardmp", method = RequestMethod.GET)
	public String bindCardmp(HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		// 查询会员最新信息。
		userInfo = userInfoService.findUserInfoId(userInfo.getUiid());
		List<CardPackage> cardPackageList = userInfoService.queryUserInfoCardPackage(userInfo.getUiid(), 0, 4);
		if(CollectionUtils.isNotEmpty(cardPackageList)){
			request.setAttribute("card", cardPackageList.get(0));
		}
		request.setAttribute("userInfo", userInfo);
		return "/mobile/user/bindCard";
	}

	/**
	 * 手机端绑支付宝页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/bindalipaymp", method = RequestMethod.GET)
	public String bindalipaymp(HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		// 查询会员最新信息。
		userInfo = userInfoService.findUserInfoId(userInfo.getUiid());
		if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
			List<CardPackage> cardPackageList = userInfoService.queryUserInfoCardPackage(userInfo.getUiid(), 0, 4);
			if (CollectionUtils.isNotEmpty(cardPackageList)) {
				if (StringUtils.isNotEmpty(cardPackageList.get(0).getAlipay())) {
					request.setAttribute("myAlipay", cardPackageList.get(0));
				}
			}
		}
		request.setAttribute("userInfo", userInfo);
		return "/mobile/user/bindalipay";
	}

	/**
	 * 银行卡绑定
	 * 
	 * @param bank
	 * @param province
	 * @param city
	 * @param bankDeposit
	 * @param bankPerson
	 * @param cardNum
	 * @param alipayname
	 * @param alipay
	 * @param vcode
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/saveCardPackage")
	public @ResponseBody String saveCardPackage(@RequestParam(value = "bank", required = false) String bank,
			@RequestParam(value = "province", required = false) String province,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "bankDeposit", required = false) String bankDeposit,
			@RequestParam(value = "bankPerson", required = false) String bankPerson,
			@RequestParam(value = "cardNum", required = false) String cardNum,
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
		uname = bankPerson;
		if (!token.equals(vcode)) {
			json.put("code", "9");
			json.put("info", "验证码输入错误！");
			return json.toString();
		}
		String key = vuid + "GAMEPORTAL_USER";
		try {
			Class<Object> c = null;
			CardPackage cardPackage = null;
			List<CardPackage> cardPackages = null;
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			// 查询会员最新信息。
			userInfo = userInfoService.findUserInfoId(userInfo.getUiid());
			if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
				Map<String, Object> map = new HashMap<>();
				if (StringUtils.isNotEmpty(cardNum)) {
					map.put("cardnumber", cardNum);
					cardPackages = userInfoService.queryCardPackage(map);
					if (CollectionUtils.isNotEmpty(cardPackages)) {
						json.put("code", "0");
						json.put("info", "很抱歉，该银行卡号已被绑定，不能重复绑定!");
						return json.toString();
					}
				}
				if (StringUtils.isNotEmpty(bank)) {
					bank = URLDecoder.decode(bank, "utf8");
				}
				if (StringUtils.isNotEmpty(province)) {
					province = URLDecoder.decode(province, "utf8");
				}
				if (StringUtils.isNotEmpty(city)) {
					city = URLDecoder.decode(city, "utf8");
				}
				if (StringUtils.isNotEmpty(bankDeposit)) {
					bankDeposit = URLDecoder.decode(bankDeposit, "utf8");
				}
				if (StringUtils.isNotEmpty(bankPerson)) {
					bankPerson = URLDecoder.decode(bankPerson, "utf8");
				}
				// 查询会员是否绑定会员卡。
				cardPackages = userInfoService.queryUserInfoCardPackage(userInfo.getUiid(), 0, 20);
				if (CollectionUtils.isNotEmpty(cardPackages)) {
					json.put("code", "0");
					json.put("info", "很抱歉，您已经绑定了银行卡，不能再次绑定!");
					return json.toString();
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
					cardPackage = userInfoService.saveCardPackage(cardPackage);
				}
				if (userInfo != null && StringUtils.isEmpty(userInfo.getUname())) {
					userInfo.setUname(URLDecoder.decode(uname, "utf8"));
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
	 * 手机版存款页面
	 * 
	 * @param adLocation
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/depositmp")
	public String live(HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		userInfo = userInfoService.findUserInfoId(userInfo.getUiid());
		iRedisService.addToRedis(key, userInfo);
		// companyCardMap<银行名称，银行卡对象>
		Map<String, CompanyCard> companyCardMap = new HashMap<String, CompanyCard>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", 0);
		params.put("grade", userInfo.getGrade());
		List<CompanyCard> ccList = companyCardService.queryCompanyCard(params, 0, 5);
		if (CollectionUtils.isNotEmpty(ccList)) {
			for (CompanyCard cc : ccList) {
				if (cc.getBankname().indexOf("支付宝") != -1) {
					request.setAttribute("apliyAccount", cc);
				} else if (cc.getBankname().indexOf("微信") != -1) {
					request.setAttribute("wechatAccount", cc);
				}else if(cc.getBankname().indexOf("企鹅") !=-1){
					request.setAttribute("qqAccount", cc);
				} else {
					companyCardMap.put(cc.getBankname(), cc);
				}
			}
		}
		if (null != companyCardMap && companyCardMap.size() > 0) {
			request.setAttribute("companyCardMap", companyCardMap);
		}
		
		params.clear();
		params.put("status", 1);
		params.put("ccholderlike", "微信");
		ccList = companyCardService.queryCompanyCard(params);
		if(CollectionUtils.isNotEmpty(ccList)){
			request.setAttribute("companyWX", ccList.get(0)); //wap端私人微信
		}
		
		params.clear();
		params.put("status", 1); //未锁定
		params.put("banknamelike", "支付宝转");
		ccList = companyCardService.queryCompanyCard(params);
		if(CollectionUtils.isNotEmpty(ccList)){
			request.setAttribute("companyAliapy", ccList.get(0)); //支付宝转银行卡
		}
		
		Map<String, Object> payParams = new HashMap<>();
		payParams.put("status", 1); // 状态 0 关闭 1开启
		payParams.put("sortColumns", "sequence asc");
		List<PayPlatform> platforms = PayPlatformService.getPayPlatform(payParams);
		
		List<PayPlatform> ailpayChannel = new ArrayList<>();
		List<PayPlatform> wechatChannel = new ArrayList<>();
		List<PayPlatform> onlineQQChannel = new ArrayList<>();
		
		if (null != platforms && platforms.size() > 0) {
			for (PayPlatform platform : platforms) {
				if (platform.getPpid() == 1 || platform.getPpid() == 24 || platform.getPpid() == 41 || platform.getPpid() == 45
						|| platform.getPpid() == 53 || platform.getPpid() == 56 || platform.getPpid() == 203 || platform.getPpid() == 210
						|| platform.getPpid() == 224 || platform.getPpid() == 226 || platform.getPpid() == 228 || platform.getPpid() == 234) { // 支付宝wap
					ailpayChannel.add(platform);
				}else if (platform.getPpid() == 20 || platform.getPpid() == 21 || platform.getPpid() == 40 || platform.getPpid() == 43
						|| platform.getPpid() == 52 || platform.getPpid() == 58 || platform.getPpid() == 202 || platform.getPpid() == 209
						|| platform.getPpid() == 212 || platform.getPpid() == 222 || platform.getPpid() == 236) { //微信wap
					wechatChannel.add(platform);
				}else if(platform.getPpid() ==206 || platform.getPpid() ==230 || platform.getPpid() ==232){
					onlineQQChannel.add(platform);
				}
			}
		}
		if(CollectionUtils.isNotEmpty(ailpayChannel)){
			request.setAttribute("ailpayChannel", ailpayChannel);
		}
		if(CollectionUtils.isNotEmpty(wechatChannel)){
			request.setAttribute("wechatChannel", wechatChannel);
		}
		if(CollectionUtils.isNotEmpty(onlineQQChannel)){
			request.setAttribute("onlineQQChannel", onlineQQChannel);
		}
		request.setAttribute("vuid",  vuid);
		request.setAttribute("activitylist", filterActivity(userInfo)); // 可参与的活动
		String orderNumber = new RandomUtil().getRandomNumber(6);
		request.setAttribute("orderNumber", orderNumber); // 存款订单号
		return "/mobile/finance/deposit";
	}


	/**
	 * 显示订单详情页面
	 * @param ccid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/showOrder", method = RequestMethod.GET)
	public String wechat(long ccid,HttpServletRequest request, HttpServletResponse response) {
		 CompanyCard card = companyCardService.getCompanyCard(ccid);
         request.setAttribute("card", card);
		return "/mobile/finance/bankin";
	}

	@RequestMapping(value = "/qrcode", method = RequestMethod.GET)
	public String qrcode(HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		String introUrl = "";
		if (StringUtils.isNotEmpty(request.getServerPort() + "")) {
			introUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + "/registermp.html?aff="
					+ userInfo.getUiid();
		} else {
			introUrl = "http://" + request.getServerName() + "/registermp.html?aff=" + userInfo.getUiid();
		}
		request.setAttribute("introUrl", introUrl);
		return "/mobile/user/qrcode";
	}

	/**
	 * 
	 * @param ccid
	 * @param code
	 * @param depositFigure
	 * @param ordernumber
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/finance/savedepositmp")
	@ResponseBody
	public String savedepositmp(@RequestParam long ccid, @RequestParam Integer depositFigure,
			@RequestParam String ordernumber, @RequestParam String nickNme, @RequestParam String hd,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		Map<String, Object> params = null;
		PayOrder payOrder = new PayOrder();
//		String token = (String) request.getSession().getAttribute(WebConst.AGENT_TOKEN_CODE);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		userInfo = userInfoService.findUserInfoId(userInfo.getUiid());
		try {
//			if ("1".equals(type)) {// 支付宝
//				token = (String) request.getSession().getAttribute(WebConst.QR_TOKEN_CODE);
//				payOrder.setKfremarks(URLDecoder.decode(alipaynick, "utf-8"));
//			} else if ("2".equals(type)) { // 微信
//				token = (String) request.getSession().getAttribute(WebConst.WX_TOKEN_CODE);
//			}
//			if (StringUtils.isEmpty(code) || !code.equals(token)) {
//				json.put("code", "9");
//				json.put("info", "验证码错误！");
//				return json.toString();
//			}
			payOrder.setKfremarks(URLDecoder.decode(nickNme, "utf-8"));

			if (hd != null && !"".equals(hd)) {

				params = new HashMap<String, Object>();
				params.put("status", 1);
				params.put("hdnumber", hd);
				// params.put("hdtype", 3);
				List<Activity> activityList = userInfoService.getList(params);// 所选活动
				if (CollectionUtils.isEmpty(activityList)) {
					json.put("code", "0");
					json.put("info", "您选择的活动不存在或已经下线!");
					return json.toString();
				}
				Activity activity = activityList.get(0);
				Map<String, Object> map = new HashMap<String, Object>();
				List<ActivityFlag> listflag = null;
				if ("3".equals(hd) || "4".equals(hd) || "1".equals(hd) || "2".equals(hd)) {// (首存、次存)
					map.put("uiid", userInfo.getUiid());
					map.put("type", activity.getHdtype());
					map.put("acid", activity.getAid());
					listflag = activityFlagService.queryAll(map);// 当前用户参与过的活动
					if (CollectionUtils.isNotEmpty(listflag)) { // 如果参加过
						json.put("code", "0");
						json.put("info", "您已经参加过此活动,请选择其他优惠活动!");
						return json.toString();
					}
					map.clear();
					map.put("uiid", userInfo.getUiid());
					map.put("acids", "1,2,3");
					List<ActivityFlag>listflags = activityFlagService.queryAll(map);
					if(CollectionUtils.isNotEmpty(listflags) && listflags.size()>=1){
						json.put("code", "0");
						json.put("info", "首存100%,存100送88,送50送58三个活动,只能三选一!");
						return json.toString();
					}
				}else if("11".equals(hd) || "12".equals(hd) || "13".equals(hd)){
					map.clear();
					map.put("uiid", userInfo.getUiid());
					map.put("acids", "11,12,13");
					listflag = activityFlagService.queryAll(map);
					if(CollectionUtils.isNotEmpty(listflag) && listflag.size()>=1){
						json.put("code", "0");
						json.put("info", "真人百家乐存送活动三个档次,只能三选一!");
						return json.toString();
					}
				} else if ("7".equals(hd)) { // 日首存
					map.clear();
					map.put("uiid", userInfo.getUiid());
					map.put("type", activity.getHdtype());
					map.put("acid", activity.getAid());
					map.put("flagtime", DateUtil.getToday());
					listflag = activityFlagService.queryAll(map);// 当前用户今日参与过的活动
					if (CollectionUtils.isNotEmpty(listflag)) { // 如果参加过
						json.put("code", "0");
						json.put("info", "您今日已经参加过此活动,请选择其他优惠活动!");
						return json.toString();
					}
				}
			}


			 CompanyCard card = companyCardService.getCompanyCard(ccid);
			 if (card == null) {
				 json.put("code", "8");
				 json.put("info", "该卡信息不存在！");
				 return json.toString();
			 }
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uiid", userInfo.getUiid());
			map.put("paytyple", 0);// 表示存款
			map.put("paymethods", 0);// 0公司入款，1第三方支付
			map.put("status", 1);// 状态 0 作废 1 发起（待审核） 2 处理中（审核中） 3 存取成功 4 存取失败
			Long count = userPropertyService.getPayOrder(map);
			if (count > 0) {
				json.put("code", "0");
				json.put("info", "您有正在审核的存款订单，请稍后再申请存款。");
				return json.toString();
			}

			Timestamp date = new Timestamp(new Date().getTime());
			payOrder.setPoid(IdGenerator.genOrdId16("OMP"));
			payOrder.setPlatformorders(IdGenerator.genOrdId16(""));
			payOrder.setUiid(userInfo.getUiid());
			payOrder.setUaccount(userInfo.getAccount());
			payOrder.setUrealname(userInfo.getUname());
			payOrder.setPaytyple(0); // 0存款，1提款，2赠送，3扣款
			payOrder.setPpid(-1L);
			payOrder.setPaymethods(0); // 0公司入款，1第三方支付
			payOrder.setDeposittime(new Date());
			 payOrder.setDeposit(card.getBankname());
			 payOrder.setBankcard(card.getCcno());
			 payOrder.setOpenname(card.getCcholder());
			 payOrder.setBankname(card.getBankname());
			payOrder.setAmount(new BigDecimal(depositFigure));
			payOrder.setPaystatus(0);
			payOrder.setStatus(1);
			payOrder.setCreate_Date(date);
			payOrder.setUpdate_Date(date);
			payOrder.setHdnumber(hd);
			payOrder.setOrdernumber(ordernumber);
			if(StringUtils.isNotEmpty(userInfo.getPuiid()+"") && userInfo.getPuiid()>0){
				UserInfo pUser = userInfoService.findUserInfoId(userInfo.getPuiid());
				if(pUser !=null){
					payOrder.setProxyname(pUser.getAccount());
				}
			}
			payOrder = gameTransferService.savePayOrder(payOrder);
			if (StringUtils.isNotBlank(ObjectUtils.toString(payOrder))) {
				json.put("code", "1");
				json.put("ordernumber", ordernumber);
				json.put("info", "您的存款申请提交成功，客服正在受理!");
			} else {
				json.put("code", "0");
				json.put("info", "您的公司入款提交失败，请联系客服!");
			}
		} catch (Exception e) {
			logger.error("存款异常。", e);
			json.put("code", "0");
			json.put("info", "网络异常，请稍后再试！");
		}
		return json.toString();
	}

	/**
	 * 手机版提款页面
	 * 
	 * @param adLocation
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/withdrawalmp")
	public String withdrawal(HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		request.setAttribute("vuid", vuid);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		// 获取最新用户信息
		userInfo = userInfoService.queryUserInfo(userInfo.getAccount(), null);
		request.setAttribute("userInfo", userInfo);
		List<CardPackage> cardPackageList = userInfoService.queryUserInfoCardPackage(userInfo.getUiid(), 0, 4);
		if (CollectionUtils.isNotEmpty(cardPackageList)) {
			request.setAttribute("cardPackages", cardPackageList);
			request.setAttribute("cardPackage", cardPackageList.get(0));
		}else{
			return "/mobile/user/bindCard"; 
		}
		return "/mobile/finance/withdrawal";
	}
	
	  /**
     * 设置取款密码。
     * @return
     */
    @RequestMapping(value = "/setAtmPwdp")
    @ResponseBody
    public String setAtmPwd(@RequestParam(value = "atmPwd", required = false) String atmPwd,
            @RequestParam(value = "vcode", required = false) String vcode, HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            String vuid = CookieUtil.getOrCreateVuid(request, response);
            String key = vuid + "GAMEPORTAL_USER";
            Class<Object> c = null;
            UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
            userInfo = userInfoService.findUserInfoId(userInfo.getUiid());

            String token = (String) request.getSession().getAttribute(WebConst.AGENT_TOKEN_CODE);
            if (StringUtils.isEmpty(vcode)) {
                json.put("code", "9");
                json.put("info", "请输入验证码。");
                return json.toString();
            }
            if(!vcode.equals(token)){
                json.put("code", "9");
                json.put("info", "验证码输入错误。");
                return json.toString();
            }
            Blowfish bf = new Blowfish(WebConst.DECRYPT_KEY);
            userInfo.setAtmpasswd(bf.encryptString(atmPwd));
            userInfo.setUpdateDate(new Date());
            if (userInfoService.modifyUserInfo(userInfo)) {
                iRedisService.addToRedis(key, userInfo);
                json.put("code", "1");
                json.put("info", "恭喜，您的提款密码设置成功。");
            } else {
                json.put("code", "9");
                json.put("info", "系统忙，请稍后重试。");
            }
        } catch (Exception e) {
            json.put("code", "9");
            json.put("info", "网络异常，请稍后再试！");
            logger.error("设置提款密码失败。", e);
        }
        return json.toString();
    }

	/**
	 * 手机提款页面
	 * 
	 * @param caseoutPwd
	 * @param caseoutFigure
	 * @param caseoutBankname
	 * @param caseoutBankcard
	 * @param caseoutBankperson
	 * @param caseoutBankfh
	 * @param alipayname
	 * @param alipay
	 * @param way
	 * @param vcode
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/finance/saveCaseout", method = RequestMethod.POST)
	public @ResponseBody String saveCaseout(@RequestParam(value = "caseoutPwd", required = false) String caseoutPwd,
			@RequestParam(value = "caseoutFigure", required = false) Integer caseoutFigure,
			@RequestParam(value = "code", required = false) String vcode, HttpServletRequest request,
			HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		UserInfo userInfo2 = userInfoService.findUserInfoId(userInfo.getUiid());
		JSONObject json = new JSONObject();
		String token = (String) request.getSession().getAttribute(WebConst.AGENT_TOKEN_CODE);
		if (StringUtils.isEmpty(vcode)) {
			json.put("code", "9");
			json.put("info", "请输入验证码。");
			return json.toString();
		}
		if (!token.equals(vcode)) {
			json.put("code", "9");
			json.put("info", "验证码输入错误。");
			return json.toString();
		}
		if (caseoutFigure < 100) {
			json.put("code", "10");
			json.put("info", "单笔提款不能少于100。");
			return json.toString();
		}
		try {
			request.setAttribute("vuid", vuid);
			Blowfish bf = new Blowfish(WebConst.DECRYPT_KEY);
			if (null == userInfo2.getAtmpasswd() || "".equals(userInfo2.getAtmpasswd())) {
				json.put("code", "0");
				json.put("info", "您还没有设置提款密码，请先设置提款密码!");
				return json.toString();
			}
			String cods = bf.decryptString(userInfo2.getAtmpasswd());
			if (!StringUtils.isNotBlank(userInfo2.getAtmpasswd()) || !cods.equals(caseoutPwd)) {
				json.put("code", "0");
				json.put("info", "您的提款密码不正确，请检查后重新输入!");
				return json.toString();
			}
			List<CardPackage> cardPackages = userInfoService.queryUserInfoCardPackage(userInfo.getUiid(), 0, 20);
			if (CollectionUtils.isEmpty(cardPackages)) {
				json.put("code", "0");
				json.put("info", "您还没有绑定银行卡，请先绑定银行卡!");
				return json.toString();
			}
			if("0".equals(userInfo2.getWithdrawlFlag())){ //关闭提款
				json.put("code", "0");
				json.put("info", "提款失败，请联系在线客服!");
				return json.toString();
			}
			CardPackage cardPackage = cardPackages.get(0);
			Timestamp date = new Timestamp(new Date().getTime());
			PayOrder payOrder = new PayOrder();
			payOrder.setPoid(IdGenerator.genOrdId16("TMG"));
			payOrder.setPlatformorders(IdGenerator.genOrdId16(""));
			payOrder.setUiid(userInfo.getUiid());
			payOrder.setUaccount(userInfo.getAccount());
			payOrder.setUrealname(userInfo.getUname());
			payOrder.setPaytyple(1);
			payOrder.setPpid(1L);
			payOrder.setPaymethods(0);
			payOrder.setDeposittime(date);
			payOrder.setBankcard(cardPackage.getCardnumber());
			payOrder.setOpenname(cardPackage.getAccountname());
			payOrder.setBankname(cardPackage.getBankname());
			payOrder.setDeposit(cardPackage.getOpeningbank());
			payOrder.setAmount(new BigDecimal(caseoutFigure));
			payOrder.setPaystatus(0);
			payOrder.setStatus(1);
			payOrder.setCreate_Date(date);
			payOrder.setUpdate_Date(date);
			String code = gameTransferService.modifyCaseout(payOrder, userInfo, caseoutFigure);
			if (StringUtils.isNotBlank(ObjectUtils.toString(code)) && "0000".equals(code)) {
				json.put("code", "1");
				json.put("info", "您的提款申请提交成功，客服正在受理!");
				if (userInfo2.getPhonevalid() == 0) {
					// 清空redis中发送的短信验证码
					iRedisService.delete(userInfo.getAccount() + "_VCODE");
					// 修改用户电话号码验证状态为有效
					userInfo2.setPhonevalid(1);
					userInfoService.updateUserInfo(userInfo2);
				}
			} else if (StringUtils.isNotBlank(ObjectUtils.toString(code)) && "-1".equals(code)) {
				json.put("code", "0");
				json.put("info", "您的账户余额不足，提款失败!");
			} else if(StringUtils.isNotBlank(ObjectUtils.toString(code)) && "-4".equals(code)){
				 json.put("code", "0");
				 json.put("info", "您好，您今日提款次数已达上限，请明天再申请提款。");
			} else if (StringUtils.isNotBlank(ObjectUtils.toString(code)) && "-5".equals(code)) {
				json.put("code", "0");
				json.put("info", "没有查询到提款限额，请联系在线客服。");
			} else if (StringUtils.isNotBlank(ObjectUtils.toString(code)) && "-6".equals(code)) {
				json.put("code", "0");
				json.put("info", "您的提款额度大于单笔提款限额，提款失败。");
			} else if (StringUtils.isNotBlank(ObjectUtils.toString(code)) && "-7".equals(code)) {
				json.put("code", "0");
				json.put("info", "您有提款申请正在审核，请等待审核通过后再次申请提款。");
			}
		} catch (Exception e) {
			logger.error("提款异常。", e);
			json.put("code", "0");
			json.put("info", "网络异常，请稍后再试！");
		}
		return json.toString();
	}

	private List<Activity> filterActivity(UserInfo userInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", 1);
		map.put("sortColumns", "aid DESC");
		List<Activity> alist = userInfoService.getList(map);// 所有活动

		// 排除首存优惠
		map.clear();
		map.put("uiid", userInfo.getUiid());
		map.put("type", 3);
		List<ActivityFlag> listflags = activityFlagService.queryAll(map);
		map.clear();
		map.put("status", 1);
		map.put("aid", 3);
		List<Activity> activity100 = userInfoService.getList(map);
		if (CollectionUtils.isNotEmpty(listflags)) {
			alist.removeAll(activity100);
		}
		// 排除100 送88 优惠
		map.clear();
		map.put("uiid", userInfo.getUiid());
		map.put("acid", 1);
		listflags = activityFlagService.queryAll(map);
		map.clear();
		map.put("status", 1);
		map.put("aid", 1);
		List<Activity> activity88 = userInfoService.getList(map);
		if (CollectionUtils.isNotEmpty(listflags)) {
			alist.removeAll(activity88);
		}
		// 排除存50 送58优惠
		map.clear();
		map.put("uiid", userInfo.getUiid());
		map.put("acid", 2);
		listflags = activityFlagService.queryAll(map);
		map.clear();
		map.put("status", 1);
		map.put("aid", 2);
		List<Activity> activity58 = userInfoService.getList(map);
		if (CollectionUtils.isNotEmpty(listflags)) {
			alist.removeAll(activity58);
		}
		if (CollectionUtils.isNotEmpty(listflags)) { // 如果参加过
			List<Activity> revomelists = new ArrayList<Activity>();
			for (Activity activity : alist) {
				for (ActivityFlag activityFlag : listflags) {
					if (activity != null && activityFlag != null) {
						if (activity.getAid().equals(activityFlag.getAcid())) {
							String hdNum = activity.getHdnumber();// 优惠代码
							if ("7".equals(hdNum)) {// 根据活动代码判断是否是每日首存优惠活动
								/***************
								 * 判断用户今天是否参与过每日首存 如果参与 则需要删除
								 ***********/
								// 参与活动的时间
								Date joinDate = activityFlag.getFlagtime();
								String todayStr = DateUtil.getStrByDate(new Date(), "yyyy-MM-dd");
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
								String joinDateStr = sdf.format(joinDate);
								if (joinDateStr != null && todayStr.equals(joinDateStr)) {// 已经参与
									revomelists.add(activity);
								}
							} else {
								revomelists.add(activity);
							}
						}
					}
				}
			}
			alist.removeAll(revomelists);// 删除参与过的活动(首存二存)
		}
		// 首存100%，送50送58，送100送88(三选一)
		map.clear();
		map.put("uiid", userInfo.getUiid());
		map.put("acids", "1,2,3");
		listflags = activityFlagService.queryAll(map);
		if (CollectionUtils.isNotEmpty(listflags) && listflags.size() >= 1) {
			alist.removeAll(activity100);
			alist.removeAll(activity88);
			alist.removeAll(activity58);
		}

		// 百家乐首存存送(三选一)
		map.clear();
		map.put("uiid", userInfo.getUiid());
		map.put("acids", "11,12,13");
		listflags = activityFlagService.queryAll(map);
		if (CollectionUtils.isNotEmpty(listflags) && listflags.size() >= 1) {
			map = new HashMap<String, Object>();
			map.put("status", 1);
			map.put("hdtype", 10);
			alist.removeAll(userInfoService.getList(map));// 所有真人存送活动
		}
		// 排除抢红包活动
		map.clear();
		map.put("status", 1);
		map.put("aid", 10);
		List<Activity> hbActivity = userInfoService.getList(map);
		if (CollectionUtils.isNotEmpty(hbActivity)) {
			alist.removeAll(hbActivity);
		}

		// 排除老虎机存1000送388
		map.clear();
		map.put("uiid", userInfo.getUiid());
		map.put("acid", 15);
		map.put("flagtime", DateUtil.getToday());
		listflags = activityFlagService.queryAll(map);
		if (CollectionUtils.isNotEmpty(listflags)) {
			map = new HashMap<String, Object>();
			map.put("status", 1);
			map.put("hdnumber", 15);
			alist.removeAll(userInfoService.getList(map)); // 移除老虎机存1000送388元

			map.put("hdnumber", 16);
			alist.removeAll(userInfoService.getList(map)); // 移除老虎机存2000送688元
		} else {
			// 排除老虎机存2000送688
			map.clear();
			map.put("uiid", userInfo.getUiid());
			map.put("acid", 16);
			map.put("flagtime", DateUtil.getToday());
			listflags = activityFlagService.queryAll(map);
			if (CollectionUtils.isNotEmpty(listflags)) {
				map = new HashMap<String, Object>();
				map.put("status", 1);
				map.put("hdnumber", 16);
				alist.removeAll(userInfoService.getList(map)); // 移除老虎机存2000送688元

				map.put("hdnumber", 15);
				alist.removeAll(userInfoService.getList(map)); // 移除老虎机存1000送388元
			}
		}
		
		//每日次存50%优惠
		map.put("uiid", userInfo.getUiid());
		map.put("acid", 17);
		map.put("flagtime", DateUtil.getToday());
		listflags = activityFlagService.queryAll(map);
		if(CollectionUtils.isNotEmpty(listflags)){
			map = new HashMap<String, Object>();
			map.put("status", 1);
			map.put("hdnumber", 17);
			alist.removeAll(userInfoService.getList(map)); //每日次存50%优惠
		}else{
			map = new HashMap<String, Object>();
			map.put("uiid", userInfo.getUiid());
			map.put("paytyple", 0);
			map.put("status", 3);
			map.put("exhdnumber","1,2,3");
			map.put("startDateStr", DateUtil.getToday());
			map.put("endDateStr", DateUtil.getToday());
			long count = reportQueryService.countPayOrder(map);
			if(count <1){
				map = new HashMap<String, Object>();
				map.put("status", 1);
				map.put("hdnumber", 17);
				alist.removeAll(userInfoService.getList(map)); //每日次存50%优惠
			}
		}
		// 排除存10元送18优惠
		map.clear();
		map.put("uiid", userInfo.getUiid());
		map.put("acid", 18);
		listflags = activityFlagService.queryAll(map);
		map.clear();
		map.put("status", 1);
		map.put("aid", 18);
		List<Activity> activity10 = userInfoService.getList(map);
		if (CollectionUtils.isNotEmpty(listflags)) {
			alist.removeAll(activity10);
		}
		return alist;
	}
}
