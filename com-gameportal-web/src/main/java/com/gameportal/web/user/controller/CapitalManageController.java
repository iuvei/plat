package com.gameportal.web.user.controller;

import java.math.BigDecimal;
import java.net.MalformedURLException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.web.activity.model.ActivityFlag;
import com.gameportal.web.activity.service.IActivityFlagService;
import com.gameportal.web.game.handler.IGameServiceHandler;
import com.gameportal.web.game.model.GameAccount;
import com.gameportal.web.game.model.GamePlatform;
import com.gameportal.web.game.model.GameTransfer;
import com.gameportal.web.game.service.IGamePlatformService;
import com.gameportal.web.game.service.IGameTransferService;
import com.gameportal.web.game.util.AGINOfficialCode;
import com.gameportal.web.game.util.ApiException;
import com.gameportal.web.order.model.CompanyCard;
import com.gameportal.web.order.service.ICompanyCardService;
import com.gameportal.web.payPlat.model.PayPlatform;
import com.gameportal.web.payPlat.service.IPayPlatformService;
import com.gameportal.web.redis.service.IRedisService;
import com.gameportal.web.sms.model.SmsPlatAccount;
import com.gameportal.web.sms.model.SmsSendLog;
import com.gameportal.web.sms.service.ISmsLogService;
import com.gameportal.web.sms.service.ISmsService;
import com.gameportal.web.user.model.AccountMoney;
import com.gameportal.web.user.model.Activity;
import com.gameportal.web.user.model.CardPackage;
import com.gameportal.web.user.model.PayOrder;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.user.service.IReportQueryService;
import com.gameportal.web.user.service.IUserInfoService;
import com.gameportal.web.user.service.IUserPropertyService;
import com.gameportal.web.util.Blowfish;
import com.gameportal.web.util.CookieUtil;
import com.gameportal.web.util.DateUtil;
import com.gameportal.web.util.HttpsUtil;
import com.gameportal.web.util.IdGenerator;
import com.gameportal.web.util.JsonUtils;
import com.gameportal.web.util.RandomUtil;
import com.gameportal.web.util.WebConst;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/manage/capital")
public class CapitalManageController{
	@Resource(name = "userInfoServiceImpl")
	private IUserInfoService userInfoService = null;
	@Resource(name = "userPropertyService")
	private IUserPropertyService userPropertyService;
	@Resource(name = "gameTransferServiceImpl")
	private IGameTransferService gameTransferService = null;
	@Resource(name = "companyCardServiceImpl")
	private ICompanyCardService companyCardService = null;
	@Resource(name = "activityFlagService")
	private IActivityFlagService activityFlagService = null;
	@Resource(name = "smsService")
	private ISmsService smsService;
	@Resource(name = "smsLogService")
	private ISmsLogService smsLogService;
	@Resource(name = "payPlatformService")
	private IPayPlatformService PayPlatformService;
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService;
	//@Resource(name = "mgGameAPI")
	//private MgGameAPI mgGameAPI;
	@Resource(name = "reportQueryServiceImpl")
	private IReportQueryService reportQueryService;

	@Resource(name = "gamePlatformHandlerMap")
	private Map<String, IGameServiceHandler> gamePlatformHandlerMap = null;
	@Resource(name = "gamePlatformServiceImpl")
	private IGamePlatformService gamePlatformService = null;
	private static final Logger logger = Logger.getLogger(CapitalManageController.class);

	public CapitalManageController() {
		super();
	}

	@RequestMapping(value = "/userDeposit")
	public String userDeposit(HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		request.setAttribute("vuid", vuid);
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
		List<CompanyCard> ccList = companyCardService.queryCompanyCard(params, 0, 0);
		if (null != ccList && ccList.size() > 0) {
			for (CompanyCard cc : ccList) {
				companyCardMap.put(cc.getBankname(), cc);
				if ("支付宝".equals(cc.getBankname())) {
					request.setAttribute("apliyAccount", cc);
				}else if(cc.getBankname().indexOf("微信") !=-1){
					request.setAttribute("wechatAccount", cc);
				}else if(cc.getBankname().indexOf("企鹅") !=-1){
					request.setAttribute("qqAccount", cc);
				}
			}
		}
		if (null != companyCardMap && companyCardMap.size() > 0) {
			request.setAttribute("companyCardMap", companyCardMap);
		}
		
		params.clear();
		params.put("status", 1); //未锁定
		params.put("banknamelike", "支付宝转");
		ccList = companyCardService.queryCompanyCard(params);
		if(CollectionUtils.isNotEmpty(ccList)){
			request.setAttribute("companyAliapy", ccList.get(0)); //支付宝转银行卡
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", 1);
		map.put("sortColumns", "aid DESC");
		List<Activity> alist = userInfoService.getList(map);// 所有活动
		
		map.clear();
		map.put("status", "1");
		map.put("sortColumns", "sequence asc");
		List<PayPlatform> payPlatforms = PayPlatformService.getPayPlatform(map);
		List<PayPlatform> wechatPay = new ArrayList<>();
		List<PayPlatform> bankPay = new ArrayList<>();
		List<PayPlatform> alipayPay = new ArrayList<>();
		List<PayPlatform> dkPay = new ArrayList<>();
		List<PayPlatform> onlineQQPay = new ArrayList<>();
		for (PayPlatform plat : payPlatforms) {
			if (plat.getPpid() == 4 || plat.getPpid() == 6 || plat.getPpid() == 7 || plat.getPpid() == 46 || plat.getPpid() == 48 
					|| plat.getPpid() == 50 || plat.getPpid() == 57 || plat.getPpid() == 200 || plat.getPpid() == 207 
					|| plat.getPpid() == 211 || plat.getPpid() == 220 || plat.getPpid() == 235) {
				request.setAttribute("haswx", true); // 判断是否有微信
				request.removeAttribute("wechatAccount");  // 网页版使用第三方微信、手机版使用自己的微信
				wechatPay.add(plat);
				request.setAttribute("wxPlats", wechatPay);
			}else if(plat.getPpid() ==1 || plat.getPpid() ==2 || plat.getPpid() ==47 || plat.getPpid() == 49 || plat.getPpid() == 51
					|| plat.getPpid() == 55 || plat.getPpid() == 201 || plat.getPpid() == 208 || plat.getPpid() == 223 || plat.getPpid() == 225
					|| plat.getPpid() == 227 || plat.getPpid() == 233){
				request.removeAttribute("apliyAccount");  // 网页版使用第三方支付宝、手机版使用自己的支付宝
				request.setAttribute("hasalipay", true); // 判断是否有支付宝
				alipayPay.add(plat);
				request.setAttribute("onlineAlipays", alipayPay);
			}else if(plat.getPpid() ==11 || plat.getPpid() ==44 || plat.getPpid() ==54 || plat.getPpid() ==204 || plat.getPpid() ==213 || plat.getPpid() ==221){
				bankPay.add(plat);
			}else if(plat.getPpid() ==100){
				dkPay.add(plat);
				request.setAttribute("dxAlipays", dkPay);
			}else if(plat.getPpid() ==205 || plat.getPpid() ==229 || plat.getPpid() ==231){
				onlineQQPay.add(plat);
				request.removeAttribute("qqAccount");
				request.setAttribute("qqonlineAlipays", onlineQQPay);
				
			}
		}
		request.setAttribute("payPlats", bankPay);
		request.setAttribute("today", DateUtil.getStrYMDHMSByDate(new Date()));
		// 排除首存优惠
		map.clear();
		map.put("uiid", userInfo.getUiid());
		map.put("type", 3);
		List<ActivityFlag> listflags = activityFlagService.queryAll(map);
		map.clear();
		map.put("status", 1);
		map.put("aid", 3);
		List<Activity> activity100 =userInfoService.getList(map);
		if(CollectionUtils.isNotEmpty(listflags)){
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
		if(CollectionUtils.isNotEmpty(listflags)){
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
		List<Activity> activity58= userInfoService.getList(map);
		if(CollectionUtils.isNotEmpty(listflags)){
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
		if(CollectionUtils.isNotEmpty(listflags) && listflags.size()>=1){
			alist.removeAll(activity100);
			alist.removeAll(activity88);
			alist.removeAll(activity58);
		}
		
		// 百家乐首存存送(三选一)
		map.clear();
		map.put("uiid", userInfo.getUiid());
		map.put("acids", "11,12,13");
		listflags = activityFlagService.queryAll(map);
		if(CollectionUtils.isNotEmpty(listflags) && listflags.size()>=1){
			map = new HashMap<String, Object>();
			map.put("status", 1);
			map.put("hdtype", 10);
			alist.removeAll(userInfoService.getList(map));// 所有真人存送活动
		}
		// 排除抢红包活动
		map.clear();
		map.put("status", 1);
		map.put("aid", 10);
		List<Activity> hbActivity =userInfoService.getList(map);
		if(CollectionUtils.isNotEmpty(hbActivity)){
			alist.removeAll(hbActivity);
		}
		
		//排除老虎机存1000送388
		map.clear();
		map.put("uiid", userInfo.getUiid());
		map.put("acid", 15);
		map.put("flagtime", DateUtil.getToday());
		listflags = activityFlagService.queryAll(map);
		if(CollectionUtils.isNotEmpty(listflags)){
			map = new HashMap<String, Object>();
			map.put("status", 1);
			map.put("hdnumber", 15);
			alist.removeAll(userInfoService.getList(map)); //移除老虎机存1000送388元
			
			map.put("hdnumber", 16);
			alist.removeAll(userInfoService.getList(map)); //移除老虎机存2000送688元
		}else{
			//排除老虎机存2000送688
			map.clear();
			map.put("uiid", userInfo.getUiid());
			map.put("acid", 16);
			map.put("flagtime", DateUtil.getToday());
			listflags = activityFlagService.queryAll(map);
			if(CollectionUtils.isNotEmpty(listflags)){
				map = new HashMap<String, Object>();
				map.put("status", 1);
				map.put("hdnumber", 16);
				alist.removeAll(userInfoService.getList(map)); //移除老虎机存2000送688元
				
				map.put("hdnumber", 15);
				alist.removeAll(userInfoService.getList(map)); //移除老虎机存1000送388元
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
			
		request.setAttribute("activitylist", alist); // 可参与的活动
		String orderNumber = new RandomUtil().getRandomNumber(6);
		request.setAttribute("orderNumber", orderNumber); // 存款订单号

		return "/manage/capital/deposit";
	}

	/**
	 * 公司入款或支付宝存款处理
	 */
	@RequestMapping(value = "/saveDeposit")
	public @ResponseBody String saveDeposit(@RequestParam(value = "ccid", required = false) String ccid,
			@RequestParam(value = "cardNum", required = false) String cardNum,
			@RequestParam(value = "bankPerson", required = false) String bankPerson,
			@RequestParam(value = "depositBankname", required = false) String depositBankname,
			@RequestParam(value = "depositOpenbank", required = false) String depositOpenbank,
			@RequestParam(value = "hd", required = false) String hd,
			@RequestParam(value = "nickName", required = false) String nickName,
			@RequestParam(value = "ordernumber", required = false) String ordernumber,
			@RequestParam(value = "depositFigure", required = false) Integer depositFigure,
			@RequestParam(value = "code", required = false) String vcode, HttpServletRequest request,
			HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		JSONObject json = new JSONObject();
		Map<String, Object> params = null;
		String token = (String) request.getSession().getAttribute(WebConst.AGENT_TOKEN_CODE);
		if (StringUtils.isNotEmpty(ccid)) {// 支付宝支付
			token = (String) request.getSession().getAttribute(WebConst.QR_TOKEN_CODE);
		}
		request.setAttribute("vuid", vuid);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		if (StringUtils.isEmpty(vcode) || !vcode.equals(token)) {
			json.put("code", "9");
			json.put("info", "验证码错误！");
			return json.toString();
		}

		if (hd != null && !"".equals(hd)) {
			/**
			if (userInfo != null) {
				if (userInfo.getPhonevalid() != 1) {
					json.put("code", "0");
					json.put("info", "请您先认证手机号码再参与此活动!");
					return json.toString();
				}
				if (userInfo.getEmailvalid() != 1) {
					json.put("code", "0");
					json.put("info", "请您先认证邮箱账号再参与此活动!");
					return json.toString();
				}
			}
			*/

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

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uiid", userInfo.getUiid());
			map.put("paytyple", 0);// 表示存款
			map.put("paymethods", 0);// 0公司入款，1第三方支付
			map.put("status", 1);// 状态 0 作废 1 发起（待审核） 2 处理中（审核中） 3 存取成功 4 存取失败
			Long count = userPropertyService.getPayOrder(map);
			if (count > 0) {
				json.put("code", "0");
				json.put("info", "您有正在审核的存款订单，请等待审核通过后再申请存款。");
				return json.toString();
			}
			// 判断订单号是否存在，防止用户修改页面订单号
			map.clear();
			map.put("ordernumber", ordernumber);
			count = userPropertyService.getPayOrder(map);
			if (count > 0) {
				json.put("code", "0");
				json.put("info", "您填写的银行转账附言已存在。");
				return json.toString();
			}

			Timestamp date = new Timestamp(new Date().getTime());
			PayOrder payOrder = new PayOrder();
			payOrder.setPoid(IdGenerator.genOrdId16("OMG"));
			payOrder.setPlatformorders(IdGenerator.genOrdId16(""));
			payOrder.setUiid(userInfo.getUiid());
			payOrder.setUaccount(userInfo.getAccount());
			payOrder.setUrealname(userInfo.getUname());
			payOrder.setPaytyple(0); // 0存款，1提款，2赠送，3扣款
			payOrder.setPpid(-1L);
			payOrder.setPaymethods(0); // 0公司入款，1第三方支付
			payOrder.setDeposittime(new Date());
			payOrder.setKfremarks(nickName);
			if (StringUtils.isNotEmpty(ccid)) {// 支付宝支付
				CompanyCard card = companyCardService.getCompanyCard(Long.valueOf(ccid));
				payOrder.setDeposit(card.getBankname());
				payOrder.setBankcard(card.getCcno());
				payOrder.setOpenname(card.getCcholder());
				payOrder.setBankname(card.getBankname());
			} else {
				payOrder.setDeposit(depositOpenbank);
				payOrder.setBankcard(cardNum);
				payOrder.setOpenname(bankPerson);
				payOrder.setBankname(depositBankname);
			}
			payOrder.setAmount(new BigDecimal(depositFigure));
			payOrder.setPaystatus(0);
			payOrder.setStatus(1);
			// payOrder.setOrdercontent(orderDetails);
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
				json.put("info", "您的公司入款提交成功，客服正在受理!");
			} else {
				json.put("code", "0");
				json.put("info", "您的公司入款提交失败，请联系客服!");
			}
		} catch (Exception e) {
			logger.error("<|>saveDeposit<|>" + cardNum + "/" + bankPerson + "/"
					+ DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT) + "/" + depositBankname + "/"
					+ depositFigure + "<|>" + e + "<|><|><|><|>", e);
			json.put("code", "0");
			json.put("info", "网络异常，请稍后再试！");
		}
		return json.toString();
	}

	/**
	 * 第三方充值验证码。
	 * 
	 * @param otherCode
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/validateOtherCode")
	@ResponseBody
	public String saveDeposit(String otherCode, String type, HttpServletRequest request, HttpServletResponse response) {
		String token = (String) request.getSession().getAttribute(WebConst.TOKEN_CODE); // 第三方支付
		if ("2".equals(type)) {
			token = (String) request.getSession().getAttribute(WebConst.WX_TOKEN_CODE); // 微信支付
		}
		JSONObject json = new JSONObject();
		if (StringUtils.isEmpty(otherCode) || !otherCode.equals(token)) {
			json.put("code", 0);
			json.put("info", "验证码输入错误。");
		} else {
			json.put("code", 1);
		}
		return json.toString();
	}

	@RequestMapping(value = "/userCaseout")
	public String userCaseout(HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		request.setAttribute("vuid", vuid);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		// 获取最新用户信息
		userInfo = userInfoService.queryUserInfo(userInfo.getAccount(), null);
		CardPackage cardPackage = userInfoService.queryCardPackage(userInfo.getUiid(), 1);
		if (null != cardPackage) {
			request.setAttribute("cardPackage", cardPackage);
		}
		request.setAttribute("myBalance", userInfoService.getAccountMoney(userInfo.getUiid(), 1));
		
		request.setAttribute("userInfo", userInfo);
		return "/manage/capital/withdraw";
	}

	@RequestMapping("/getSmsCode")
	@ResponseBody
	public String getSmsCode(String phone, HttpServletRequest request, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		request.setAttribute("vuid", vuid);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		// 获取最新用户信息
		userInfo = userInfoService.queryUserInfo(userInfo.getAccount(), null);
		String vcode = new RandomUtil().getRandomNumber(6);
		String message = "尊敬的会员，您于" + DateUtil.getStrYMDHMSByDate(new Date()) + "提款认证码为：" + vcode
				+ "，请您及时输入认证码,如有疑问，请联系客服。";
		try {
			if (StringUtils.isEmpty(phone)) {
				phone = userInfo.getPhone();
			}
			Map<String, Object> param = new HashMap<String, Object>();
            param.put("phone", phone);
            param.put("neuiid", userInfo.getUiid());
            long count = userInfoService.getUserInfoCount(param);
            if (count > 0) {
                result.put("code", "9");
                result.put("info", "该手机号码已被其他会员绑定。");
                return result.toString();
            }
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("type", 1);
			params.put("username", userInfo.getAccount().trim());
			params.put("startTime", DateUtil.getStrByDate(DateUtil.getAddHour(new Date(), -1), DateUtil.TIME_FORMAT));
			params.put("endTime", DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT));
			Long sendTimes = smsLogService.selectSmsLogCount(params);
			if (sendTimes >= 1L) {
				result.put("code", "9");
				result.put("info", "很抱歉，手机短信每小时只能发送一次，如需帮助请联系在线客服!");
				return result.toString();
			}
			String code = smsService.sendSMS(phone, message, DateUtil.getStrYMDHMSByDate(new Date()), "2");
			if (!"2000".equals(code)) {
				logger.info("提款发送短信验证码【" + vcode + "】失败");
				result.put("code", "9");
				result.put("info", "网络异常，请稍后重试。");
			} else {
				result.put("code", "0");
				if ((StringUtils.isEmpty(userInfo.getPhone()) || !userInfo.getPhone().equals(phone))
						&& StringUtils.isNotEmpty(phone)) {
					userInfo.setPhone(phone);
					userInfoService.updateUserInfo(userInfo);
				}
				iRedisService.addToRedis(userInfo.getAccount() + "_VCODE", vcode);
				// 记录发送短信日志
				SmsPlatAccount sms = smsService.getSmsPlatAccount();
				SmsSendLog log = new SmsSendLog();
				log.setSpaid(sms.getSpaid());
				log.setUsername(userInfo.getAccount());
				log.setMobile(userInfo.getPhone());
				log.setContent(message);
				log.setType(1); // 提款
				smsLogService.saveSmsPlat(log);
			}
		} catch (Exception e) {
			logger.info(e);
		}
		return result.toString();
	}

	/**
	 * 设置取款密码。
	 * 
	 * @return
	 */
	@RequestMapping(value = "/setAtmPwd")
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

			String token = (String) request.getSession().getAttribute(WebConst.TOKEN_CODE);
			if (StringUtils.isEmpty(vcode)) {
				json.put("code", "9");
				json.put("info", "请输入验证码。");
				return json.toString();
			}
			if (!vcode.equals(token)) {
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

	@RequestMapping(value = "/saveCaseout")
	public @ResponseBody String saveCaseout(@RequestParam(value = "caseoutPwd", required = false) String caseoutPwd,
			@RequestParam(value = "caseoutFigure", required = false) Integer caseoutFigure,
			@RequestParam(value = "caseoutBankname", required = false) String caseoutBankname,
			@RequestParam(value = "caseoutBankcard", required = false) String caseoutBankcard,
			@RequestParam(value = "caseoutBankperson", required = false) String caseoutBankperson,
			@RequestParam(value = "caseoutBankfh", required = false) String caseoutBankfh,
			@RequestParam(value = "alipayname", required = false) String alipayname,
			@RequestParam(value = "alipay", required = false) String alipay,
			@RequestParam(value = "way", required = false) String way,
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
		if (userInfo2.getPhonevalid() == 0) {
			String code = String.valueOf(iRedisService.getRedisResult(userInfo.getAccount() + "_VCODE", c));
			if (!code.equals(vcode)) {
				json.put("code", "-1");
				json.put("info", "短信验证码输入错误。");
				return json.toString();
			}
		} else if (!token.equals(vcode)) {
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
				json.put("info", "您还没有绑定银行卡或支付宝，请前往账户中心处绑定!");
				return json.toString();
			}
			CardPackage cardPackage = cardPackages.get(0);
			if (StringUtils.isEmpty(cardPackage.getCardnumber()) && "1".equals(way)) {
				json.put("code", "0");
				json.put("info", "您还没有绑定银行卡，请提款到您的支付宝!");
				return json.toString();
			}
			if (StringUtils.isEmpty(cardPackage.getAlipay()) && "2".equals(way)) {
				json.put("code", "0");
				json.put("info", "您还没有绑定支付宝，请提款到您的银行卡!");
				return json.toString();
			}
			if("0".equals(userInfo2.getWithdrawlFlag())){ //关闭提款
				json.put("code", "0");
				json.put("info", "提款失败，请联系在线客服!");
				return json.toString();
			}
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
			if(StringUtils.isNotEmpty(userInfo2.getPuiid()+"") && userInfo2.getPuiid()>0){
				UserInfo pUser = userInfoService.findUserInfoId(userInfo2.getPuiid());
				if(pUser !=null){
					payOrder.setProxyname(pUser.getAccount());
				}
			}
			if ("2".equals(way)) { // 提款到支付宝
				payOrder.setBankcard(cardPackage.getAlipay());
				payOrder.setOpenname(cardPackage.getAlipayname());
				payOrder.setBankname("支付宝");
				payOrder.setDeposit("支付宝");
			} else {
				payOrder.setBankcard(caseoutBankcard);
				payOrder.setOpenname(caseoutBankperson);
				payOrder.setBankname(caseoutBankname);
				payOrder.setDeposit(caseoutBankfh);
			}
			payOrder.setAmount(new BigDecimal(caseoutFigure));
			payOrder.setPaystatus(0);
			payOrder.setStatus(1);
			// payOrder.setOrdercontent(orderDetails);
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
			logger.error("<|>saveCaseout<|>" + caseoutPwd + "/" + caseoutFigure + "/" + caseoutBankname + "/"
					+ caseoutBankcard + "/" + caseoutBankperson + "<|>" + e + "<|><|><|><|>", e);
			json.put("code", "0");
			json.put("info", "网络异常，请稍后再试！");
		}
		return json.toString();
	}

	@RequestMapping(value = "/userTransfer")
	public String userTransfer(HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		Class<Object> c = null;
		String key = vuid + "GAMEPORTAL_USER";
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		//查询最新信息
		userInfo = userInfoService.findUserInfoId(userInfo.getUiid());
		if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
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
            request.setAttribute("agBill", "J25_AG" + new RandomUtil().getRandomNumber(15));
            request.setAttribute("aginBill", "J25_AGIN" + new RandomUtil().getRandomNumber(14));
            request.setAttribute("bbinBill", "75"+IdGenerator.getRandomNumber(17));
            request.setAttribute("ptBill", new RandomUtil().getRandomCode(9));
//            request.setAttribute("mgBill", UUID.randomUUID().toString().toUpperCase());
            request.setAttribute("mgBill",new RandomUtil().getRandomCode(16));
            request.setAttribute("saBill", DateUtil.getStrByDate(new Date(), "yyyMMddHHmmss")+userInfo.getAccount().toLowerCase());
			request.setAttribute("gameAccountList", gameAccountList);
			request.setAttribute("listGame", listGame);
		}
		request.setAttribute("transferUser", userInfo);
		return "/manage/capital/transfer";
	}
	
	/**
	 * 我要贷款
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/loanmoney")
	public String loanmoney(HttpServletRequest request, HttpServletResponse response) {
		return "/manage/capital/loanmoney";
	}

	@RequestMapping(value = "/getGameBalance")
	@ResponseBody
	public String getGameBalance(HttpServletRequest request, HttpServletResponse response, String gpname) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", 1);
		params.put("gpname", gpname);
		String strMoney = "";
		List<GameAccount> gameAccountList = null;
		Class<Object> c = null;
		String key = vuid + "GAMEPORTAL_USER";
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		userInfo = userInfoService.findUserInfoId(userInfo.getUiid());
		if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
			List<GamePlatform> listGame = gamePlatformService.queryGame(params);
			if (null != listGame && listGame.size() > 0) {
				gameAccountList = new ArrayList<GameAccount>();
				GameAccount gameAccount = null;
				for (GamePlatform game : listGame) {
					try {
						gameAccount = new GameAccount();
						strMoney = gamePlatformHandlerMap.get(game.getGpname()).queryBalance(userInfo, game, null) + "";
						if (null == strMoney || "".equals(strMoney)) {
							gameAccount.setMoney(0);
						} else {
							double money = Double.valueOf(strMoney);
							gameAccount.setMoney((int) money);
						}
					} catch (Exception e) {
						logger.error(e.getMessage());
						gameAccount.setMoney(0);
					}
					gameAccount.setUname(game.getGpname());
					gameAccountList.add(gameAccount);
				}
			}
		}
		return JsonUtils.toJson(gameAccountList);
	}

	/**
	 * 查询MG账户余额
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getMGAccountBalance")
	@ResponseBody
	public String getMGAccountBalance(HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", 1);
		params.put("gpname", "MG");
		List<GameAccount> gameAccountList = new ArrayList<>();
		Class<Object> c = null;
		String key = vuid + "GAMEPORTAL_USER";
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		userInfo = userInfoService.queryUserInfo(userInfo.getAccount());
		GamePlatform gamePlatform = gamePlatformService.queryGamePlatform(WebConst.MG);
		IGameServiceHandler gameInstance = gamePlatformHandlerMap.get(WebConst.MG);
		if (userInfo.isNotExist(gamePlatform.getGpname() + gamePlatform.getGpid())) {
			String result =(String)gameInstance.createAccount(userInfo, gamePlatform, null);
			if(!"-1".equals(result)){
				// 添加创建第三方账号标识
				userInfo.updatePlats(gamePlatform.getGpname()+gamePlatform.getGpid());
				userInfo.setMgId(result);
				userInfoService.updateUserInfo(userInfo);
			}
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
			List<GamePlatform> listGame = gamePlatformService.queryGame(params);
			if (null != listGame && listGame.size() > 0) {
				gameAccountList = new ArrayList<GameAccount>();
				GameAccount gameAccount = null;
				GamePlatform game = listGame.get(listGame.size() - 1);
				BigDecimal balance = BigDecimal.ZERO;
//				JSONObject json =null;
				try {
					gameAccount = new GameAccount();
//					json = mgGameAPI.getAccountBalance(userInfo.getAccount());
//                    if("0".equals(json.getString("code"))){
//                    	balance = new BigDecimal(json.getString("balance"));
//                    }else{
//                    	logger.error(userInfo.getAccount()+"查询MG游戏余额异常:["+json.getString("code")+"]"+json.getString("msg"));
//	            		JsonNode node = mgGameAPI.getAccountDetails(userInfo.getAccount());
//	            		if("46".equals(node.get("ErrorCode").textValue())) { //Customer account does not exists
//	            			mgGameAPI.createPlayer(userInfo);
//	            			balance = BigDecimal.ZERO;
//	            		}else{
//	            			balance = new BigDecimal(node.get("Balance").textValue());
//	            		}
//                    }
					balance =new BigDecimal(gameInstance.queryBalance(userInfo, gamePlatform, null)+"");
				} catch (Exception e) {
					logger.error(userInfo.getAccount()+"调用getAccountBalance查询失败", e);
					try {
//						json = mgGameAPI.getAccountBalance(userInfo.getAccount());
//	                    if("0".equals(json.getString("code"))){
//	                    	balance = new BigDecimal(json.getString("balance"));
//	                    }
						balance =new BigDecimal(gameInstance.queryBalance(userInfo, gamePlatform, null)+"");
					} catch (Exception e3) {
						logger.error(userInfo.getAccount()+"调用queryBalance失败后，再调余额queryBalance查询失败", e3);
						balance = BigDecimal.ZERO;
					}
				}
				gameAccount.setMoney(balance.intValue());
				gameAccount.setUname(game.getGpname());
				gameAccountList.add(gameAccount);
			}
		}
		return JsonUtils.toJson(gameAccountList);
	}

	/**
	 * 查询SA账户余额
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getSAAccountBalance")
	@ResponseBody
	public String getSAccountBalance(HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", 1);
		params.put("gpname", "SA");
		List<GameAccount> gameAccountList = null;
		Class<Object> c = null;
		String key = vuid + "GAMEPORTAL_USER";
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		userInfo = userInfoService.queryUserInfo(userInfo.getAccount());
		if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
			List<GamePlatform> listGame = gamePlatformService.queryGame(params);
			if (CollectionUtils.isNotEmpty(listGame)) {
				gameAccountList = new ArrayList<GameAccount>();
				GameAccount gameAccount = null;
				GamePlatform game = listGame.get(listGame.size() - 1);
				try {
					gameAccount = new GameAccount();
					String balance = (String) gamePlatformHandlerMap.get(game.getGpname())
							.queryBalance(userInfo, listGame.get(0), null);
					gameAccount.setMoney(Double.valueOf(balance).intValue());
				} catch (Exception e) {
					logger.error(e.getMessage());
					gameAccount.setMoney(0);
				}
				gameAccount.setUname(game.getGpname());
				gameAccountList.add(gameAccount);
			}
		}
		return JsonUtils.toJson(gameAccountList);
	}

	@RequestMapping(value = "/gameTransfer")
	public @ResponseBody String gameTransfer(@RequestParam(value = "transferOut", required = false) String transferOut,
			@RequestParam(value = "transferIn", required = false) String transferIn,
			@RequestParam(value = "transferNum", required = false) Integer transferNum,
			@RequestParam(value = "code", required = false) String vcode,
			@RequestParam(value = "agBill", required = false) String agBill,
			@RequestParam(value = "aginBill", required = false) String aginBill,
            @RequestParam(value = "bbinBill", required = false) String bbinBill,
            @RequestParam(value = "ptBill", required = false) String ptBill,
            @RequestParam(value = "mgBill", required = false) String mgBill,
            @RequestParam(value = "saBill", required = false) String saBill,
			HttpServletRequest request,HttpServletResponse response) throws MalformedURLException {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		JSONObject json = new JSONObject();
		String token = (String) request.getSession().getAttribute(WebConst.AGENT_TOKEN_CODE);
		GameTransfer gameTransfer = null;
		AccountMoney accountMoney = null;
		IGameServiceHandler gameInstance = null;
		if (vcode == null || "".equals(vcode) || !vcode.equals(token)) {
			json.put("code", "9");
			json.put("info", "验证码错误！");
			return json.toString();
		}
		try {
			Class<Object> c = null;
			String key = vuid + "GAMEPORTAL_USER";
			String code = "";
			GamePlatform gamePlatform = null;
			String billno = "";
			String balance = "";
			boolean flag = false;
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			userInfo = userInfoService.findUserInfoId(userInfo.getUiid());
			if(WebConst.PT.equals(transferOut)){
				String userName = WebConst.PT_API_USERNAME_PREFIX + userInfo.getAccount().toUpperCase();
				String requestUrl= "https://kioskpublicapi.luckydragon88.com/player/online/playername/"+userName;
				gamePlatform = gamePlatformService.queryGamePlatform(transferOut);
				String result = HttpsUtil.processRequst(requestUrl, gamePlatform.getDeskey());
				logger.info("{" + userInfo.getAccount() + "}查询玩家是否在线返回结果:" + result);
				json = JSONObject.fromObject(result);
				if(json.get("error")!=null){
					json.put("code", "0");
					json.put("info", "网络异常，请稍后再试！");
					return json.toString();
				}
				json =JSONObject.fromObject(json.get("result"));
				if("1".equals(json.getString("result"))){
					logger.info("玩家"+userName+"转账在线，登出玩家。");
					flag = true;
//					json.put("code", "0");
//					json.put("info", "很抱歉，您目前已经登入PT游戏，不能转账，请退出PT游戏后重试！");
//					return json.toString();
				}
			}
			// 判断是否有"转账中"的转账记录
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("status", 0);
			params.put("uuid", userInfo.getUiid());
			GameTransfer transfer = gameTransferService.getGameTransfer(params);
			if (transfer != null) {
				json.put("code", "0");
				json.put("info", "您有正在处理的转账记录，不能重复提交!");
				return json.toString();
			} else {
				if ("AA".equals(transferOut)) {
					gamePlatform = gamePlatformService.queryGamePlatform(transferIn);
					gameInstance = gamePlatformHandlerMap.get(transferIn);
				} else {
					gamePlatform = gamePlatformService.queryGamePlatform(transferOut);
					gameInstance = gamePlatformHandlerMap.get(transferOut);
				}
				if (gamePlatform == null) {
					json.put("code", "0");
					json.put("info", "平台已关闭或维护中，如有疑问，请联系客服!");
					return json.toString();
				}
				accountMoney = userInfoService.getAccountMoneyObj(userInfo.getUiid(), 1);
				if (accountMoney == null) {
					json.put("code", "0");
					json.put("info", "您的资金已被锁定，请您稍后重试，如有疑问，请联系客服!");
					return json.toString();
				}
				if ("AA".equals(transferOut) && accountMoney.getTotalamount().intValue() < transferNum) {
					json.put("code", "0");
					json.put("info", "您的余额不足，请充值!");
					return json.toString();
				}
//				if("MG".equals(transferOut)||"MG".equals(transferIn)){
//					JSONObject balanceJson = mgGameAPI.getAccountBalance(userInfo.getAccount());
//					if(!"0".equals(balanceJson.getString("code"))){
//						logger.info("MG转账前查询余额异常："+balanceJson.getString("msg"));
//						for(int i=0;i<2;i++){
//			        		try {
//								Thread.sleep(3000);
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
//			        		balanceJson = mgGameAPI.getAccountBalance(userInfo.getAccount());
//			        		if(!"0".equals(balanceJson.getString("code"))){
//			        			balance = String.valueOf(new BigDecimal(balanceJson.getString("balance")));
//			        			break;
//			        		}else{
//			        			balance = String.valueOf(new BigDecimal(balanceJson.getString("balance")));
//			        		}
//			        	}
//					}else{
//						BigDecimal balanceNum = new BigDecimal(balanceJson.getString("balance"));
//						balance = String.valueOf(balanceNum);
//					}
//				}else {
					balance = (String) gameInstance.queryBalance(userInfo, gamePlatform, null);
//				}
				// 新增转账记录
				gameTransfer = new GameTransfer();
				if ("AA".equals(transferOut)) {
					gameTransfer.setGamename("我的钱包");
					gameTransfer.setTogamename(gamePlatform.getFullname());
				} else {
					double dmoney = Double.valueOf(balance);
					if ((int) dmoney < transferNum) {
						json.put("code", "0");
						json.put("info", "您的余额不足，请充值!");
						return json.toString();
					}
					gameTransfer.setGamename(gamePlatform.getFullname());
					gameTransfer.setTogamename("我的钱包");
				}
				billno = aginBill;
				if (WebConst.AG.equals(transferOut) || WebConst.AG.equals(transferIn)) {
					billno = agBill;
				}else if(WebConst.BBIN.equals(transferOut) || WebConst.BBIN.equals(transferIn)) {
					billno = bbinBill;
				} else if (WebConst.PT.equals(transferOut) || WebConst.PT.equals(transferIn)) {
					billno = ptBill;
					if(flag){
						if(!"0".equals((String)gameInstance.logoutGame(userInfo, gamePlatform, null))){
							logger.info("登出PT玩家"+userInfo.getAccount()+"失败！");
						}
					}
				} else if (WebConst.MG.equals(transferOut) || WebConst.MG.equals(transferIn)) {
					billno = mgBill;
				}else if(WebConst.SA.equals(transferOut) || WebConst.SA.equals(transferIn)){
					billno =saBill;
				}
				if(StringUtils.isEmpty(billno)){
					json.put("code", "0");
					json.put("info", "转账失败,参数非法,请刷新页面或清空浏览器缓存重试!");
					return json.toString();
				}
				// 判断是否有"转账中"的转账记录
				params.clear();
				params.put("status", 0);
				params.put("uuid", userInfo.getUiid());
				transfer = gameTransferService.getGameTransfer(params);
				if (transfer != null) {
					json.put("code", "0");
					json.put("info", "您有正在处理的转账记录，不能重复提交!");
					return json.toString();
				} 
				gameTransfer.setUuid(userInfo.getUiid());
				gameTransfer.setGpid(WebConst.getGameMap(transferOut));
				gameTransfer.setTogpid(WebConst.getGameMap(transferIn));
				gameTransfer.setOrigamount(accountMoney.getTotalamount());
				gameTransfer.setAmount(new BigDecimal(transferNum));
				gameTransfer.setOtherbefore(new BigDecimal(balance));
				gameTransfer.setStatus(0);
				gameTransfer.setRemark(billno);
				gameTransfer.setCreateDate(new Date());
				gameTransfer.setUpdateDate(new Date());
				gameTransfer = gameTransferService.saveGameTransfer(gameTransfer);
				// 转账需要的参数
				params.clear();
				params.put("transferIn", transferIn);
				params.put("transferOut", transferOut);
				params.put("gameInstance", gameInstance);
				params.put("userInfo", userInfo);
				params.put("transferNum", transferNum);
				params.put("billno", billno);
				params.put("gamePlatform", gamePlatform);
				params.put("gameTransfer", gameTransfer);
				if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
					if (WebConst.AG.equals(transferIn) || WebConst.AG.equals(transferOut)
							|| WebConst.AGIN.equals(transferIn) || WebConst.AGIN.equals(transferOut)) {
						code = gameTransferService.updateAGGameTransfer(params);
					}else if(WebConst.BBIN.equals(transferIn) || WebConst.BBIN.equals(transferOut)){
						code = gameTransferService.updateBBINGameTransfer(params);
					}else if (WebConst.MG.equals(transferIn) || WebConst.MG.equals(transferOut)) {
						code = gameTransferService.updateMGGameTransfer(params);
//						if("MG".equals(transferOut)){
//					        boolean result = mgGameAPI.withdrawal(userInfo, new BigDecimal(transferNum), gameTransfer,mgBill);
//					        if(!result){
//					        	for(int i=0;i<2;i++){
//					        		try {
//										Thread.sleep(3000);
//									} catch (InterruptedException e) {
//										e.printStackTrace();
//									}
//					        		result = mgGameAPI.withdrawal(userInfo, new BigDecimal(transferNum), gameTransfer,mgBill);
//					        		if(result){
//					        			break;
//					        		}
//					        	}
//					        }
//					        code = result ? "0000":"-1";
//		                }else if("MG".equals(transferIn)){
//		                    boolean result = mgGameAPI.deposit(userInfo, new BigDecimal(transferNum), gameTransfer,gamePlatform,mgBill);
//		                    if(!result){
//		                    	for(int i=0;i<2;i++){
//		                    		try {
//										Thread.sleep(3000);
//									} catch (InterruptedException e) {
//										e.printStackTrace();
//									}
//		                    		result = mgGameAPI.deposit(userInfo, new BigDecimal(transferNum), gameTransfer,gamePlatform,mgBill);
//					        		if(result){
//					        			break;
//					        		}
//					        	}
//					        }
//                            code = result ? "0000":"-1";
//                        }
					}else if(WebConst.SA.equals(transferIn) || WebConst.SA.equals(transferOut)){ 
						code = gameTransferService.updateSAGameTransfer(params); //SA电子游戏
					} else {
						code = gameTransferService.updatePTGameTransfer(params); // PT 电子游戏
					}
					if ("0000".equals(code)) {
						json.put("code", "1");
						json.put("info", "转帐成功!");
						return json.toString();
					} else if("99".equals(code)){
						json.put("code", "0");
						json.put("info", "很抱歉，您目前已经登入PT游戏，还不能转账，请退出PT游戏后重试！");
						gameTransfer.setStatus(2);
						gameTransfer.setUpdateDate(new Date());
						gameTransferService.updateGameTransfer(gameTransfer);
					}else {
						json.put("code", "0");
						json.put("info", "网络异常，请稍后再试！");
						gameTransfer.setStatus(2);
						gameTransfer.setUpdateDate(new Date());
						gameTransferService.updateGameTransfer(gameTransfer);
					}
				}
			}
		} catch (ApiException e) {
			logger.error("<|>gameTransfer<|>" + transferOut + "/" + transferIn + "/" + transferNum + "<|>" + e
					+ "<|><|><|><|>", e);
			if (StringUtils.isNotEmpty(e.getErrorCode())) {
				if ("error".equals(e.getErrorCode())) {
					json.put("code", "0");
					json.put("info", e.getMessage());
				} else {
					json.put("code", "0");
					json.put("info", AGINOfficialCode.getMsg(e.getErrorCode()));
				}
			} else {
				json.put("code", "0");
				json.put("info", e.getMessage());
			}
			gameTransfer.setStatus(2);
			gameTransfer.setUpdateDate(new Date());
			gameTransferService.updateGameTransfer(gameTransfer);
		}
		return json.toString();
	}

	@RequestMapping(value = "/qrcode")
	public String showQRCode(String ordernumber, String ccid, HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute("ordernumber", ordernumber);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ccid", ccid);
		List<CompanyCard> cards = companyCardService.queryCompanyCard(params, 0, 5);
		if (CollectionUtils.isNotEmpty(cards)) {
			request.setAttribute("aplipy", cards.get(0));
		}
		return "/manage/capital/qrcode";
	}
}
