package com.gameportal.manage.member.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.manage.api.sa.KickUserResponse;
import com.gameportal.manage.exception.AGINOfficialCode;
import com.gameportal.manage.exception.ApiException;
import com.gameportal.manage.fcrecord.model.FcRecord;
import com.gameportal.manage.fcrecord.service.IFcRecordService;
import com.gameportal.manage.gameplatform.model.GamePlatform;
import com.gameportal.manage.gameplatform.service.IGamePlatformService;
import com.gameportal.manage.gameplatform.service.IGameServiceHandler;
import com.gameportal.manage.listener.SystemFieldsCache;
import com.gameportal.manage.member.model.GameMoney;
import com.gameportal.manage.member.model.GameTransfer;
import com.gameportal.manage.member.model.GameTransferLog;
import com.gameportal.manage.member.model.MemberInfo;
import com.gameportal.manage.member.model.MemberInfoReport;
import com.gameportal.manage.member.model.MemberInfoVo;
import com.gameportal.manage.member.model.MemberUpgradeLog;
import com.gameportal.manage.member.model.UserInfoRemark;
import com.gameportal.manage.member.model.UserLoginLog;
import com.gameportal.manage.member.model.UserManager;
import com.gameportal.manage.member.service.IGameTransferLogService;
import com.gameportal.manage.member.service.IGameTransferService;
import com.gameportal.manage.member.service.IMemberInfoService;
import com.gameportal.manage.member.service.IMemberUpgradeLogService;
import com.gameportal.manage.member.service.IUserManagerService;
import com.gameportal.manage.pay.model.PayOrder;
import com.gameportal.manage.pay.model.PayOrderLog;
import com.gameportal.manage.pay.service.IActivityService;
import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.system.model.SystemRole;
import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.system.service.ISystemService;
import com.gameportal.manage.user.model.AccountMoney;
import com.gameportal.manage.user.model.UserInfo;
import com.gameportal.manage.user.service.IAccountMoneyService;
import com.gameportal.manage.user.service.IUserInfoService;
import com.gameportal.manage.util.Blowfish;
import com.gameportal.manage.util.CookieUtil;
import com.gameportal.manage.util.DateUtil;
import com.gameportal.manage.util.DateUtil2;
import com.gameportal.manage.util.HttpUtil;
import com.gameportal.manage.util.IdGenerator;
import com.gameportal.manage.util.PropertyContext;
import com.gameportal.manage.util.RandomUtil;
import com.gameportal.manage.util.WebConstants;
import com.gameportal.manage.xima.model.MemberXimaMain;

import net.sf.json.JSONObject;

/**
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/manage/memberinfo")
public class MemberInfoController {
	@Resource(name = "memberInfoServiceImpl")
	private IMemberInfoService memberInfoService = null;

	@Resource(name = "userInfoServiceImpl")
	private IUserInfoService userInfoService = null;

	@Resource(name = "memberUpgradeLogServiceImpl")
	private IMemberUpgradeLogService memberUpgradeLogService = null;

	@Resource(name = "gameTransferServiceImpl")
	private IGameTransferService gameTransferService = null;

	@Resource(name = "gameTransferLogServiceImpl")
	private IGameTransferLogService gameTransferLogService = null;

	@Resource(name = "accountMoneyServiceImpl")
	private IAccountMoneyService accountMoneyService = null;

	@Resource(name = "redisServiceImpl")
	private IRedisService redisService = null;

	@Resource(name = "systemServiceImpl")
	private ISystemService systemService = null;

	@Resource(name = "gamePlatformServiceImpl")
	private IGamePlatformService iGamePlatformService = null;

	@Resource(name = "gamePlatformHandlerMap")
	private Map<String, IGameServiceHandler> gamePlatformHandlerMap = null;

	@Resource(name = "userManagerService")
	private IUserManagerService userManagerService;

	@Resource(name = "fcRecordService")
	private IFcRecordService fcRecordService;

	@Resource(name = "activityService")
	private IActivityService activityService;

	//@Resource(name = "mgGameAPI")
	//private MgGameAPI mgGameAPI = null;

	public static final Logger logger = Logger.getLogger(MemberInfoController.class);

	public MemberInfoController() {
		super();
	}

	@RequestMapping(value = "/index")
	public String index(@RequestParam(value = "id", required = false) String id, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		// 查询权限
		int isAdmin = 0; // 是否是管理员登录0否1是
		int isKF = 0; // 0否，1是
		int isDX = 0; // 0否，1是
		int isSC = 0; // 0否，1是
		int isFK =0;
		int isHZ =0;
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + WebConstants.FRAMEWORK_USER;
		Class<Object> c = null;
		SystemUser systemUser = (SystemUser) redisService.getRedisResult(key, c);
		if (null != systemUser) {
			List<SystemRole> roleList = systemService.queryRoleByUserId(systemUser.getUserId());
			for (SystemRole sr : roleList) {
				if (-1 != sr.getRoleName().indexOf("管理员")) {
					isAdmin = 1;
					break;
				} else if (-1 != sr.getRoleName().indexOf("客服")) {
					isKF = 1;
				} else if (-1 != sr.getRoleName().indexOf("电销")) {
					isDX = 1;
				} else if (-1 != sr.getRoleName().indexOf("市场专员")) {
					isSC = 1;
				} else if (-1 != sr.getRoleName().indexOf("风控")) {
					isFK = 1;
				} else if (-1 != sr.getRoleName().indexOf("合作伙伴")) {
					isHZ = 1;
				}
			}
		}
		JSONObject userType = new JSONObject();
		userType.put("", "全部");
		userType.put("0", "普通会员");
		userType.put("1", "代理会员");
		JSONObject typeflag = new JSONObject();
		typeflag.put("0", "线下会员");
		typeflag.put("1", "线上会员");
		JSONObject isPayMoney = new JSONObject();
		isPayMoney.put("", "全部");
		isPayMoney.put("1", "是");
		isPayMoney.put("2", "否");
		JSONObject isRelAccount = new JSONObject();
		isRelAccount.put("", "全部");
		isRelAccount.put("1", "已关联");
		isRelAccount.put("2", "未关联");

		JSONObject weekRake = new JSONObject();
		weekRake.put("", "全部");
		weekRake.put("1", "启用");
		weekRake.put("0", "禁用");
		request.setAttribute("isRelAccount", isRelAccount.toString());
		request.setAttribute("isPayMoney", isPayMoney.toString());
		request.setAttribute("userType", userType.toString());
		request.setAttribute("typeflag", typeflag.toString());
		request.setAttribute("weekRake", weekRake.toString());
		request.setAttribute("isAdmin", isAdmin);
		request.setAttribute("isKF", isKF);
		request.setAttribute("isDX", isDX);
		request.setAttribute("isSC", isSC);
		request.setAttribute("isFK", isFK);
		request.setAttribute("isHZ", isHZ);
		request.setAttribute("id", id);
		return "manage/member/memberInfo";
	}

	@RequestMapping(value = "/reportIndex")
	public String reportIndex(@RequestParam(value = "id", required = false) String id, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		JSONObject map = new JSONObject();
		map.put("1", "总存款降序");
		map.put("2", "总提款降序");

		request.setAttribute("winlossMap", map.toString());
		return "manage/member/memberRpt";
	}

	@RequestMapping(value = "/online")
	public String online(@RequestParam(value = "id", required = false) String id, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		// `status` int(2) default NULL COMMENT '状态 0 禁用 1 首次登录 2 正常登录 3 审核通过 ',
		JSONObject map = new JSONObject();
		map.put("0", "禁用");
		map.put("1", "首次登录");
		map.put("2", "正常登录");
		request.setAttribute("statusMap", map.toString());
		return "manage/user/online";
	}

	/**
	 * 查询在线会员
	 * 
	 * @param account
	 * @param uname
	 * @param startNo
	 * @param pageSize
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryOnline")
	public @ResponseBody Object queryOnline(@RequestParam(value = "account", required = false) String account,@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			List<String> redisList = redisService.getKeys("*GAMEPORTAL_USER");
			List<com.gameportal.web.user.model.UserInfo> list = null;
			long count = 0;
			if (null != redisList && redisList.size() > 0) {
				count = redisList.size();
				list = new ArrayList<com.gameportal.web.user.model.UserInfo>();
				for (String str : redisList) {
					Class<Object> c = null;
					com.gameportal.web.user.model.UserInfo systemUser = (com.gameportal.web.user.model.UserInfo) redisService
							.getRedisResult(str, c);
					if (StringUtils.isNotBlank(ObjectUtils.toString(systemUser))) {
						systemUser.setKey(str);
						if(StringUtils.isNotEmpty(account) && !systemUser.getAccount().equals(account)){
							continue;
						}
						list.add(systemUser);
					}
				}
			}
			return new GridPanel(count, list, true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}

	}
	
	@RequestMapping("/tiMemberInfo")
	@ResponseBody
	public Object tiMemberInfo(@RequestParam(value = "ukey", required = false) String ukey, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(ukey))) {
				return new ExtReturn(false, "会员key不存在");
			}
			if (redisService.keyExists(ukey)) {
				if (redisService.delete(ukey)) {
					return new ExtReturn(true, "踢出会员成功");
				} else {
					return new ExtReturn(false, "踢出会员失败");
				}
			} else {
				return new ExtReturn(true, "踢出会员成功");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping(value = "/queryMemberInfoRpt")
	public @ResponseBody Object queryMemberInfoRpt(@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "uname", required = false) String uname,
			@RequestParam(value = "quick", required = false) String quick,
			@RequestParam(value = "winloss", required = false) String winloss,
			@RequestParam(value = "starttime", required = false) String starttime,
			@RequestParam(value = "endtime", required = false) String endtime,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "stype", required = false) String stype,
			@RequestParam(value = "limit", required = false) Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String s = " 00:00:00";
			String e = " 23:59:59";
			String date = DateUtil2.format(new Date());
			Map<String, Object> map = new HashMap<String, Object>();
			// 1： 查询单个会员输赢。 2： 查询会员下线输赢。
			if ("2".equals(stype)) {
				List<MemberInfo> memberInfos = memberInfoService.queryMemberInfoByAccount(account);
				if (CollectionUtils.isNotEmpty(memberInfos)) {
					map.put("puiid", memberInfos.get(0).getUiid());
				} else {
					return new GridPanel(0L, new ArrayList<Object>(), true);
				}
			} else {
				if (StringUtils.isNotBlank(account)) {
					map.put("account", account);
				}
			}
			if (StringUtils.isNotBlank(uname)) {
				map.put("uname", uname);
			}
			if (StringUtils.isNotBlank(starttime)) {
				map.put("startDate", starttime);
			} else {
				map.put("startDate", DateUtil2.getFirstDay(date) + s);
			}
			if (StringUtils.isNotBlank(endtime)) {
				map.put("endDate", endtime);
			} else {
				map.put("endDate", date + e);
			}
			if (StringUtils.isNotBlank(quick)) {// 快捷查询参数
				if ("today".equals(quick)) {// 今天数据
					map.put("startDate", DateUtil2.format(new Date()) + s);
					map.put("endDate", DateUtil2.format(new Date()) + e);
				} else if ("yesterday".equals(quick)) {// 昨天数据
					map.put("startDate", DateUtil2.format(DateUtil2.calcYesterday()) + s);
					map.put("endDate", DateUtil2.format(DateUtil2.calcYesterday()) + e);
				} else if ("toM".equals(quick)) {// 本月
					map.put("startDate", DateUtil2.getFirstDay(date) + s);
					map.put("endDate", DateUtil2.getEndDay(date) + e);
				} else if ("toSZ".equals(quick)) {// 上周
					map.put("startDate", DateUtil2.format(DateUtil2.calcLastWeekBegin()) + s);
					map.put("endDate", DateUtil2.format(DateUtil2.calcLastWeekEnd()) + e);
				} else if ("toBZ".equals(quick)) {// 本周
					map.put("startDate", DateUtil2.format(DateUtil2.calcThisWeek()) + s);
					map.put("endDate", DateUtil2.format(new Date()) + e);
				}
			}
			logger.info("输赢查询开始时间：" + map.get("startDate"));
			logger.info("输赢查询结束时间：" + map.get("endDate"));
			if ("1".equals(winloss)) {
				map.put("sortColumns", " depositTotal desc");
			} else {
				map.put("sortColumns", " withdrawalTotal desc");
			}
			Long count = memberInfoService.queryMemberInfoReportCount(map);
			List<MemberInfoReport> list = memberInfoService.queryMemberReport(map, startNo == null ? 0 : startNo,
					pageSize == null ? 30 : pageSize);
			return new GridPanel(count, list, true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}

	}

	@RequestMapping(value = "/queryGameMoney")
	public @ResponseBody Object queryGameMoney(@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "apipassword", required = false) String apipassword,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("status", 1);
			List<GamePlatform> list = iGamePlatformService.getList(params);
			List<GameMoney> resultList = new ArrayList<GameMoney>();
			if (null == list) {
				return new ExtReturn(false, "没有查询到平台信息。");
			}
			String strMoney = "";
			GameMoney gameMoney = null;
			Map<String, Object> map = new HashMap<>();
			map.put("account", account);
			UserInfo userInfo = userInfoService.queryUserInfo2(map);
			for (GamePlatform game : list) {
				try {
					gameMoney = new GameMoney();
//					if(WebConstants.MG.equals(game.getGpname())){
//						BigDecimal balance = mgGameAPI.getAccountBalance(account);
//						if(balance ==null){
//							gameMoney.setMoney("0.00");
//						}else {
//							gameMoney.setMoney(String.valueOf(balance));
//						}
//					}else{
						strMoney = (String) gamePlatformHandlerMap.get(game.getGpname()).queryBalance(userInfo, game, null);
						if (null == strMoney || "".equals(strMoney)) {
							gameMoney.setMoney("0.00");
						} else {
							gameMoney.setMoney(strMoney);
						}
//					}
				} catch (Exception e) {
					logger.error(e.getMessage());
					gameMoney.setMoney("0.00");
				}
				gameMoney.setGpname(game.getGpname());
				resultList.add(gameMoney);
				strMoney = "";
			}
			return new GridPanel(Long.valueOf(resultList.size()), resultList, true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}

	}

	@RequestMapping(value = "/queryMemberInfo")
	public @ResponseBody Object queryMemberInfo(@ModelAttribute MemberInfoVo memberInfoVo,
			@RequestParam(value = "beginDate", required = false) String beginDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			if (StringUtils.isNotBlank(memberInfoVo.getAccount())) {
				map.put("account", memberInfoVo.getAccount());
			}
			if (StringUtils.isNotBlank(memberInfoVo.getUname())) {
				map.put("uname", memberInfoVo.getUname());
			}
			if (StringUtils.isNotBlank(memberInfoVo.getPhone())) {
				map.put("phone", memberInfoVo.getPhone());
			}
			if (StringUtils.isNotBlank(memberInfoVo.getQq())) {
				map.put("qq", memberInfoVo.getQq());
			}
			if (StringUtils.isNotBlank(memberInfoVo.getEmail())) {
				map.put("email", memberInfoVo.getEmail());
			}
			if (StringUtils.isNotBlank(memberInfoVo.getIdentitycard())) {
				map.put("identitycard", memberInfoVo.getIdentitycard());
			}
			if (memberInfoVo.getGrade() != null) {
				map.put("grade", memberInfoVo.getGrade());
			}
			if (memberInfoVo.getPuiid() != null) {
				map.put("puiid", memberInfoVo.getPuiid());
			}
			if (StringUtils.isNotEmpty(memberInfoVo.getWeekrake())) {
				map.put("withdrawlFlag", memberInfoVo.getWeekrake());
			}
			if (memberInfoVo.getAccounttype() != null) {
				map.put("accounttype", memberInfoVo.getAccounttype());
			}
			if (memberInfoVo.getRelaflag() != null) {
				if (memberInfoVo.getRelaflag() == 2) {
					map.put("norelaflag", 0);
				} else {
					map.put("relaflag", memberInfoVo.getRelaflag());
				}
			}
			if (memberInfoVo.getStatus() != null) {
				map.put("status", memberInfoVo.getStatus());
			}
			if (StringUtils.isNotBlank(memberInfoVo.getPuname())) {
				List<MemberInfo> memberInfos = memberInfoService.queryMemberInfoByAccount(memberInfoVo.getPuname());
				if (CollectionUtils.isEmpty(memberInfos)) {
					return new GridPanel(0L, new ArrayList<Object>(), true);
				}
				map.put("puiid", memberInfos.get(0).getUiid());
			}
			if (StringUtils.isNotBlank(beginDate)) {
				map.put("beginDate", beginDate);
			}
			if (StringUtils.isNotBlank(endDate)) {
				map.put("endDate", endDate);
			}
			if (StringUtils.isNotBlank(memberInfoVo.getRegip())) {
				map.put("regip", memberInfoVo.getRegip());
			}
			if (StringUtils.isNotBlank(memberInfoVo.getUrl())) {
				map.put("likeurl", memberInfoVo.getUrl());
			}
			if (StringUtils.isNotBlank(memberInfoVo.getPaymoney())) {
				if ("1".equals(memberInfoVo.getPaymoney())) {
					map.put("ispays", memberInfoVo.getPaymoney());
				} else if ("2".equals(memberInfoVo.getPaymoney())) {
					map.put("nopays", memberInfoVo.getPaymoney());
				}
			}
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) redisService.getRedisResult(key, c);
			List<SystemRole> roleList =null;
			boolean isManage = false;
			if (null != systemUser) {
				roleList = systemService.queryRoleByUserId(systemUser.getUserId());
				for (SystemRole sr : roleList) {
					if (-1 != sr.getRoleName().indexOf("合作伙伴")) {
						isManage = true;
						break;
					}
				}
			}
			if(isManage){
				JSONObject jsonMap = JSONObject.fromObject(SystemFieldsCache.fields.get("proxy.ids"));
				String s = jsonMap.toString();
				s = s.substring(2, s.indexOf(":") - 1);
				map.put("manageIds", s);
			}
			map.put("sortColumns", "create_date desc");
			Long count = memberInfoService.queryMemberInfoCountVo(map);

			List<MemberInfoVo> list = memberInfoService.queryMemberInfoVo(map, startNo == null ? 0 : startNo,
					pageSize == null ? 30 : pageSize);
			
			List<MemberInfoVo> resultList = list;
			if (StringUtils.isNotBlank(ObjectUtils.toString(systemUser))) {
				boolean isAdmin = false;
				if (null != systemUser) {
					for (SystemRole sr : roleList) {
						if (-1 != sr.getRoleName().indexOf("管理员")) {
							isAdmin = true;
							break;
						}
					}
				}
				if (!isAdmin) {
					resultList = new ArrayList<MemberInfoVo>();
					for (MemberInfoVo vo : list) { 
						if(StringUtils.isNotEmpty(vo.getPuiid()+"") && vo.getPuiid() !=41726){//zzb300
							String email = vo.getEmail();
							if (null != email && !"".equals(email)) {
								vo.setEmail("****" + email.substring(4, email.length()));
							} else {
								vo.setEmail("**********");
							}
							String QQ = vo.getQq();
							if (StringUtils.isNotBlank(QQ)) {
								vo.setQq("****" + QQ.substring(2, QQ.length()));
							} else {
								vo.setQq("********");
							}
	
							String phone = vo.getPhone();
							if (null != phone && !"".equals(phone)) {
								vo.setPhone(phone.substring(0, 3) + "****"
										+ phone.substring(phone.length() - 4, phone.length()));
							} else {
								vo.setPhone("**********");
							}
	
	//						String ID = vo.getIdentitycard();
	//						if (null != ID && !"".equals(ID)) {
	//							vo.setIdentitycard(ID.substring(0, ID.length() - 4) + "****");
	//						} else {
	//							vo.setIdentitycard("***********");
	//						}
						}
						resultList.add(vo);
					}
				}
			}
			return new GridPanel(count, resultList, true);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 查询当前生日人数
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryBirthDayCount")
	@ResponseBody
	public Object queryBirthDayCount(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		String date = DateUtil2.format2(new Date(), "yyyy-MM-dd");
		params.put("today", date);
		Long count = memberInfoService.queryBirthDayCount(params);
		return new ExtReturn(true, count);
	}

	@RequestMapping(value = "/queryBirthDayResult")
	public @ResponseBody Object queryBirthDayResult(@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		String date = DateUtil2.format2(new Date(), "yyyy-MM-dd");
		params.put("today", date);
		Long count = memberInfoService.queryBirthDayCount(params);
		List<MemberInfo> list = memberInfoService.queryBirthDayResult(params, startNo, pageSize);
		return new GridPanel(count, list, true);
	}

	@RequestMapping(value = "/saveMemberInfo")
	@ResponseBody
	public Object saveMemberInfo(@ModelAttribute MemberInfo memberInfoA,
			@RequestParam(value = "proxyaccount", required = false) String proxyaccount,
			@RequestParam(value = "alignPasswd", required = false) String alignPasswd,
			@RequestParam(value = "alignAtmpasswd", required = false) String alignAtmpasswd, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			MemberInfo memberInfoB = new MemberInfo();
			MemberInfo proxyInfo = null;
			Timestamp date = new Timestamp(new Date().getTime());
			if (null != proxyaccount && !"".equals(proxyaccount)) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("account", proxyaccount);
				params.put("accounttype", 0);// 查询不等于0
				proxyInfo = memberInfoService.qeuryMemberInfo(params);
				if (null == proxyInfo || "".equals(proxyInfo)) {
					return new ExtReturn(false, "代理账号【" + proxyaccount + "】不存在，请检查后重新输入！");
				}
			}
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) redisService.getRedisResult(key, c);
			if (StringUtils.isNotBlank(ObjectUtils.toString(memberInfoA.getUiid()))) {
				memberInfoB = memberInfoService.findMemberInfoId(memberInfoA.getUiid());
				if (memberInfoB == null) {
					return new ExtReturn(false, "更新失败，您选择的会员未查询到，请刷新后重试！");
				}
				if (proxyInfo != null) {
					memberInfoB.setPuiid(proxyInfo.getUiid());
				}
				//只有管理员才可以修改身份证号码
				if(!"admin".equals(systemUser.getAccount()) && !memberInfoB.getIdentitycard().equals(memberInfoA.getIdentitycard())){
					return new ExtReturn(false, "您没有修改身份证号码的权限，请联系管理员处理！");
				}
				
				if(!"admin".equals(systemUser.getAccount()) && !memberInfoB.getPhone().equals(memberInfoA.getPhone())){
					return new ExtReturn(false, "您没有修改电话号码的权限，请联系管理员处理！");
				}
				memberInfoB.setAccounttype(memberInfoA.getAccounttype());
				memberInfoB.setBirthday(memberInfoA.getBirthday());
				memberInfoB.setIdentitycard(memberInfoA.getIdentitycard());
				//手机、邮箱、qq带*不保存
				if(StringUtils.isNotEmpty(memberInfoA.getEmail()) && memberInfoA.getEmail().indexOf("*") ==-1){
					memberInfoB.setEmail(memberInfoA.getEmail());
				}
				memberInfoB.setGrade(memberInfoA.getGrade());
				if(StringUtils.isNotEmpty(memberInfoA.getPhone()) && memberInfoA.getPhone().indexOf("*") ==-1){
					memberInfoB.setPhone(memberInfoA.getPhone());
				}
				if(StringUtils.isNotEmpty(memberInfoA.getQq()) && memberInfoA.getQq().indexOf("*") ==-1){
					memberInfoB.setQq(memberInfoA.getQq());
				}
				memberInfoB.setTypeflag(memberInfoA.getTypeflag());
				memberInfoB.setUname(memberInfoA.getUname());
				memberInfoB.setUpdateDate(date);
				memberInfoB.setRegip(memberInfoA.getRegip());
				if (memberInfoService.modifyMemberInfo(memberInfoB)) {
					return new ExtReturn(true, "更新成功！");
				} else {
					return new ExtReturn(false, "更新失败！");
				}
			} else {
				// 检查两次登陆密码是否一致
				if (!StringUtils.equals(memberInfoA.getPasswd(), alignPasswd)) {
					return new ExtReturn(false, "两次登陆密码不一致！");
				}
				// 检查两次提款密码是否一致
				if (!StringUtils.equals(memberInfoA.getAtmpasswd(), alignAtmpasswd)) {
					return new ExtReturn(false, "两次提款密码不一致！");

				}
				if (null == memberInfoA.getAccount() || "".equals(memberInfoA.getAccount())) {
					return new ExtReturn(false, "帐号不能为空！");
				}
				if (memberInfoService.isExistByAcc(memberInfoA.getAccount())) {
					return new ExtReturn(false, "此帐号已存在，请选择其它帐号！");
				}

				Blowfish bf = new Blowfish(WebConstants.BLOWFISH_CODE);
				memberInfoB = memberInfoA;
				memberInfoB.setPasswd(bf.encryptString(alignPasswd));// 加密
				memberInfoB.setAtmpasswd(bf.encryptString(alignAtmpasswd));// 加密
				memberInfoB.setStatus(1);
				if (proxyInfo != null) {
					memberInfoB.setPuiid(proxyInfo.getUiid());
				}
				memberInfoB.setCreateDate(date);
				memberInfoB.setUpdateDate(date);
				memberInfoB.setApipassword(bf.encryptString(alignPasswd));
			}
			MemberInfo resuleMember = memberInfoService.saveUserInfo(memberInfoB);
			if (resuleMember != null) {
				AccountMoney accountMoney = new AccountMoney();
				accountMoney.setUiid(resuleMember.getUiid());
				accountMoney.setStatus(1);
				accountMoney.setTotalamount(BigDecimal.ZERO);
				accountMoney.setUpdateDate(date);
				accountMoney.setCreateDate(date);
				accountMoneyService.saveOrUpdateAccountMoney(accountMoney);
				return new ExtReturn(true, "保存成功！");
			} else {
				return new ExtReturn(false, "保存失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping("/delMemberInfo/{uiid}")
	@ResponseBody
	public Object delMemberInfo(@PathVariable Long uiid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(uiid))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			if (userManagerService.delete(uiid) && memberInfoService.deleteMemberInfo(uiid)) {
				return new ExtReturn(true, "删除成功！");
			} else {
				return new ExtReturn(false, "删除失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping(value = "/modifyPwd")
	@ResponseBody
	public Object modifyPwd(@RequestParam(value = "passwd", required = false) String passwd,
			@RequestParam(value = "okpasswd", required = false) String atmpasswd,
			@RequestParam(value = "isCheckPas", required = false) Boolean isCheckPas,
			@RequestParam(value = "isCheckAtmPas", required = false) Boolean isCheckAtmPas,
			@RequestParam(value = "uiid", required = true) Long uiid, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			MemberInfo memberInfo = memberInfoService.findMemberInfoId(uiid);
			if (memberInfo == null) {
				return new ExtReturn(false, "找不到会员资料！");

			}
			Blowfish bf = new Blowfish(WebConstants.BLOWFISH_CODE);
			if (null != isCheckPas && isCheckPas) {
				memberInfo.setPasswd(bf.encryptString(passwd));
			}
			if (null != isCheckAtmPas && isCheckAtmPas) {
				memberInfo.setAtmpasswd(bf.encryptString(atmpasswd));
			}
			if (memberInfoService.modifyMemberInfo(memberInfo)) {
				if (null != isCheckPas && isCheckPas) {
					// 修改PT客户端的登陆密码
					GamePlatform gamePlatform = iGamePlatformService.queryGamePlatformById(WebConstants.PT);
					if (gamePlatform != null) {
						IGameServiceHandler gameInstance = gamePlatformHandlerMap.get(WebConstants.PT);
						UserInfo userInfo = new UserInfo();
						userInfo.setAccount(memberInfo.getAccount());
						userInfo.setPasswd(memberInfo.getPasswd());
						String result = (String) gameInstance.edit(userInfo, gamePlatform, null);
						if (!"0".equals(result)) {
							logger.info("修改PT客户端登陆密码失败。");
						}
						
						gamePlatform = iGamePlatformService.queryGamePlatformById(WebConstants.MG);
						gameInstance = gamePlatformHandlerMap.get(WebConstants.MG);
//						memberInfo.setPasswd(passwd);
//						ResultMsg rm = mgGameAPI.editAccount(memberInfo);
//						logger.info("调用MG接口修改登陆密码返回结果:" + rm.toString());
//						String mgCode = rm.getCode();
//						if(mgCode == null && !"1".equals(mgCode)){
//							logger.info("修改MG客户端登陆密码失败！");
//						}
						result = (String) gameInstance.edit(userInfo, gamePlatform, null);
						if (!"0".equals(result)) {
							logger.info("修改MG客户端登陆密码失败。");
						}
					}
				}
				return new ExtReturn(true, "密码修改成功！");
			} else {
				return new ExtReturn(true, "密码修改失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}

	}

	@RequestMapping(value = "/setstatus")
	@ResponseBody
	public Object setstatus(@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "uiid", required = true) Long uiid,
			@RequestParam(value = "remark", required = false) String remark, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			MemberInfo memberInfo = memberInfoService.findMemberInfoId(uiid);
			if (memberInfo == null) {
				return new ExtReturn(false, "找不到会员资料！");

			}
			memberInfo.setStatus(Integer.parseInt(status));
			if ("0".equals(status)) {
				memberInfo.setRemark(remark); // 禁用操作
			} else {
				memberInfo.setRemark(null);
			}
			if (memberInfoService.modifyMemberInfo(memberInfo)) {
				return new ExtReturn(true, "设置成功！");
			} else {
				return new ExtReturn(true, "设置失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}

	}

	@RequestMapping(value = "/upgrade")
	@ResponseBody
	@Transactional
	public Object upgrade(@RequestParam(value = "oldgrade", required = true) int oldgrade,
			@RequestParam(value = "newgrade", required = true) int newgrade,
			@RequestParam(value = "reason", required = false) String reason,
			@RequestParam(value = "uiid", required = true) Long uiid, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			MemberInfo memberInfo = memberInfoService.findMemberInfoId(uiid);
			if (memberInfo == null) {
				return new ExtReturn(false, "找不到会员资料！");

			}
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) redisService.getRedisResult(key, c);

			Timestamp date = new Timestamp(new Date().getTime());
			MemberUpgradeLog memberUpgradeLog = new MemberUpgradeLog();

			memberUpgradeLog.setUid(uiid);
			memberUpgradeLog.setAccount(memberInfo.getAccount());
			memberUpgradeLog.setOldgrade((long) oldgrade);
			memberUpgradeLog.setNewgrade((long) newgrade);
			memberUpgradeLog.setReason(reason);
			memberUpgradeLog.setCreateDate(date);
			memberUpgradeLog.setCreateuserid(String.valueOf(systemUser.getUserId()));
			memberUpgradeLog.setCreateusername(systemUser.getRealName());
			memberUpgradeLogService.saveMemberUpgradeLog(memberUpgradeLog);

			memberInfo.setGrade(newgrade);
			if (memberInfoService.modifyMemberInfo(memberInfo)) {
				return new ExtReturn(true, "升级成功！");
			} else {
				return new ExtReturn(true, "升级失败！");
			}
			//
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}

	}

	/**
	 * 查询第三方游戏余额。
	 * 
	 * @param account
	 * @param gamePlat
	 * @return
	 */
	@RequestMapping(value = "/queryWalletMoney/{uiid}/{gamePlat}")
	@ResponseBody
	public Object queryWalletMoney(@PathVariable Long uiid, @PathVariable String gamePlat) {
		try {
			if ("AA".equals(gamePlat)) { // 查询钱包余额
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("uiid", uiid);
				AccountMoney accountMoney = accountMoneyService.getMoneyInfo(params);
				return new ExtReturn(true, accountMoney.getTotalamount());
			} else { // 查询第三方游戏余额
				GamePlatform gamePlatform = iGamePlatformService.queryGamePlatformById(gamePlat);
				IGameServiceHandler gameInstance = gamePlatformHandlerMap.get(gamePlat);
				String balance = "";
				UserInfo userInfo = userInfoService.queryById(uiid);
//				if(gamePlatform.getGpname().equals(WebConstants.MG)){
//					BigDecimal balanceNum = mgGameAPI.getAccountBalance(userInfo.getAccount());
//					balance = balanceNum == null ? "0" : String.valueOf(balanceNum);
//				}else{
					balance = (String) gameInstance.queryBalance(userInfo, gamePlatform, null);
//				}
				double dmoney = Double.valueOf(balance);
				int intmoney = (int) dmoney;
				return new ExtReturn(true, intmoney);
			}
		} catch (Exception e) {
			logger.error("查询第三方" + gamePlat + "游戏余额失败。", e);
			return new ExtReturn(false, "查询转出金额异常。");
		}
	}

	@RequestMapping(value = "/gameTransfer")
	public @ResponseBody Object gameTransfer(@RequestParam(value = "fromPlaform", required = false) String fromPlaform,
			@RequestParam(value = "toPlaform", required = false) String toPlaform,
			@RequestParam(value = "amount", required = false) Integer amount,
			@RequestParam(value = "uiid", required = true) Long uiid,
			@RequestParam(value = "remark", required = true) String remark, HttpServletRequest request,
			HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		GameTransfer gameTransfer = null;
		try {
			Class<Object> c = null;
			String code = "";
			GamePlatform gamePlatform = null;
			AccountMoney accountMoney = null;
			IGameServiceHandler gameInstance = null;
			String billno = "";
			String balance = "";
			String key = vuid + WebConstants.FRAMEWORK_USER;
			UserInfo userInfo = userInfoService.queryById(uiid);
			if (userInfo == null) {
				return new ExtReturn(false, "找不到会员资料！");
			}
			if (WebConstants.PT.equals(fromPlaform)) {
				String userName = WebConstants.PT_API_USERNAME_PREFIX + userInfo.getAccount().toUpperCase();
				String requestUrl = "https://kioskpublicapi.luckydragon88.com/player/online/playername/" + userName;
				gamePlatform = iGamePlatformService.queryGamePlatformById(fromPlaform);
				String result = com.gameportal.manage.util.HttpsUtil.processRequst(requestUrl,
						gamePlatform.getDeskey());
				logger.info("{" + userInfo.getAccount() + "}查询玩家是否在线返回结果:" + result);
				JSONObject json = JSONObject.fromObject(result);
				if (json.get("error") != null) {
					return new ExtReturn(false, "网络异常，请稍后再试！");
				}
				json = JSONObject.fromObject(json.get("result"));
				if ("1".equals(json.getString("result"))) {
					return new ExtReturn(false, "很抱歉，您目前已经登入PT游戏，还不能转账，请退出PT游戏后重试！");
				}
			}
			// 判断是否有"转账中"的转账记录
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("status", 0);
			params.put("uuid", userInfo.getUiid());
			GameTransfer transfer = gameTransferService.getGameTransfer(params);
			if (transfer != null) {
				return new ExtReturn(false, "您有正在处理的转账记录，不能重复提交！");
			} else {
				if ("AA".equals(fromPlaform)) {
					gamePlatform = iGamePlatformService.queryGamePlatformById(toPlaform);
					gameInstance = gamePlatformHandlerMap.get(toPlaform);
				} else {
					gamePlatform = iGamePlatformService.queryGamePlatformById(fromPlaform);
					gameInstance = gamePlatformHandlerMap.get(fromPlaform);
				}
				if (gamePlatform == null) {
					return new ExtReturn(false, "平台已关闭或维护中，如有疑问，请联系客服！");
				}
				accountMoney = memberInfoService.getAccountMoneyObj(userInfo.getUiid(), 1);
				if (accountMoney == null) {
					return new ExtReturn(false, "中心钱包被锁定，请解锁后重试！");
				}
//				if ("MG".equals(fromPlaform) || "MG".equals(toPlaform)) {
//					BigDecimal balanceNum = mgGameAPI.getAccountBalance(userInfo.getAccount());
//					balance = balanceNum == null ? "0" : String.valueOf(balanceNum);
//				} else {
					balance = (String) gameInstance.queryBalance(userInfo, gamePlatform, null);
//				}
				// 新增转账记录
				gameTransfer = new GameTransfer();
				if ("AA".equals(fromPlaform)) {
					if (accountMoney.getTotalamount().compareTo(new BigDecimal(amount)) == -1) {
						return new ExtReturn(false, "您的余额不足，请充值！");
					}
					gameTransfer.setGamename("我的钱包");
					gameTransfer.setTogamename(gamePlatform.getFullname());
				} else {
					double dmoney = Double.valueOf(balance);
					if ((int) dmoney < amount) {
						return new ExtReturn(false, "您的余额不足，请充值！");
					}
					gameTransfer.setGamename(gamePlatform.getFullname());
					gameTransfer.setTogamename("我的钱包");
				}
				billno = gamePlatform.getCiphercode() + new RandomUtil().getRandomNumber(15);
				if (WebConstants.BBIN.equals(fromPlaform) || WebConstants.BBIN.equals(toPlaform)) {
					billno = IdGenerator.getRandomNumber(17);// BBIN游戏厅订单编号
				} else if (WebConstants.PT.equals(fromPlaform) || WebConstants.PT.equals(toPlaform)) {
					billno = new RandomUtil().getRandomCode(9); // PT接口交易号
				} else if (WebConstants.SA.equals(fromPlaform) || WebConstants.SA.equals(toPlaform)) {
					billno = DateUtil.getStrByDate(new Date(), "yyyMMddHHmmss")+userInfo.getAccount(); // SA接口交易号
				} else if (WebConstants.MG.equals(fromPlaform) || WebConstants.MG.equals(toPlaform)) {
					billno = new RandomUtil().getRandomCode(15); // MG接口交易号
				}
				gameTransfer.setUuid(userInfo.getUiid());
				gameTransfer.setGpid(WebConstants.getGameMap(fromPlaform));
				gameTransfer.setTogpid(WebConstants.getGameMap(toPlaform));
				gameTransfer.setOrigamount(accountMoney.getTotalamount());
				gameTransfer.setAmount(amount);
				gameTransfer.setOtherbefore(new BigDecimal(balance).setScale(2, BigDecimal.ROUND_HALF_UP));
				gameTransfer.setStatus(0);
				gameTransfer.setRemark(billno);
				gameTransfer.setCreateDate(new Date());
				gameTransfer.setUpdateDate(new Date());
				gameTransfer = gameTransferService.saveGameTransfer(gameTransfer);
				// 转账需要的参数
				params.clear();
				params.put("transferIn", toPlaform);
				params.put("transferOut", fromPlaform);
				params.put("gameInstance", gameInstance);
				params.put("userInfo", userInfo);
				params.put("transferNum", amount);
				params.put("billno", billno);
				params.put("gamePlatform", gamePlatform);
				params.put("gameTransfer", gameTransfer);
				if (WebConstants.AG.equals(fromPlaform) || WebConstants.AGIN.equals(fromPlaform)
						|| WebConstants.AG.equals(toPlaform) || WebConstants.AGIN.equals(toPlaform)) {
					code = gameTransferService.agGameTransfer(params);
				} else if (WebConstants.BBIN.equals(fromPlaform) || WebConstants.BBIN.equals(toPlaform)) {
					code =gameTransferService.bbinGameTransfer(params);
				} else if (WebConstants.MG.equals(fromPlaform) || WebConstants.MG.equals(toPlaform)) {
//					if ("MG".equals(fromPlaform)) {
//						boolean result = mgGameAPI.withdrawal(userInfo, new BigDecimal(amount), gameTransfer);
//						code = result ? "0000" : "-1";
//					} else if ("MG".equals(toPlaform)) {
//						boolean result = mgGameAPI.deposit(userInfo, new BigDecimal(amount), gameTransfer);
//						code = result ? "0000" : "-1";
//					}
					code = gameTransferService.mgGameTransfer(params);
				}else if (WebConstants.SA.equals(fromPlaform) || WebConstants.SA.equals(toPlaform)) {
					code = gameTransferService.updateSAGameTransfer(params);
				}  else {
					code = gameTransferService.ptGameTransfer(params);
				}
				if ("0000".equals(code)) {
					SystemUser systemUser = (SystemUser) redisService.getRedisResult(key, c);
					Timestamp date = new Timestamp(new Date().getTime());
					GameTransferLog gameTransferLog = new GameTransferLog();
					gameTransferLog.setFormplat(fromPlaform.toString().equals("AA") ? "我的钱包" : fromPlaform.toString());
					gameTransferLog.setToplat(toPlaform.toString().equals("AA") ? "我的钱包" : toPlaform.toString());
					gameTransferLog.setUid(uiid);
					gameTransferLog.setAccount(userInfo.getAccount());
					gameTransferLog.setAmount(amount);
					gameTransferLog.setCreateDate(date);
					gameTransferLog.setCreateuserid(remark);
					gameTransferLog.setCreateusername(systemUser.getRealName());
					gameTransferLogService.saveGameTransferLog(gameTransferLog);
					return new ExtReturn(true, "转帐成功！");
				} else if ("99".equals(code)) {
					return new ExtReturn(false, "很抱歉，您目前已经登入PT游戏，还不能转账，请退出PT游戏后重试！");
				} else if ("1".equals(code)) {
					gameTransfer.setStatus(2);
					gameTransfer.setUpdateDate(new Date());
					gameTransferService.updateGameTransfer(gameTransfer);
					return new ExtReturn(false, "非法参数！");
				} else if ("2".equals(code)) {
					gameTransfer.setStatus(2);
					gameTransfer.setUpdateDate(new Date());
					gameTransferService.updateGameTransfer(gameTransfer);
					return new ExtReturn(false, "您的金额不足，请充值！");
				} else {
					gameTransfer.setStatus(2);
					gameTransfer.setUpdateDate(new Date());
					gameTransferService.updateGameTransfer(gameTransfer);
					return new ExtReturn(false, "网络异常，请稍后再试！");
				}
			}
		} catch (ApiException e) {
			logger.error(
					"<|>gameTransfer<|>" + fromPlaform + "/" + toPlaform + "/" + amount + "<|>" + e + "<|><|><|><|>",
					e);
			gameTransfer.setStatus(2);
			gameTransfer.setUpdateDate(new Date());
			gameTransferService.updateGameTransfer(gameTransfer);
			if (StringUtils.isNotEmpty(e.getErrorCode())) {
				if ("error".equals(e.getErrorCode())) {
					return new ExtReturn(false, e.getMessage());
				} else {
					return new ExtReturn(false, AGINOfficialCode.getMsg(e.getErrorCode()));
				}
			} else {
				return new ExtReturn(false, e.getMessage());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			gameTransfer.setStatus(2);
			gameTransfer.setUpdateDate(new Date());
			gameTransferService.updateGameTransfer(gameTransfer);
			return new ExtReturn(false, ex.getMessage());
		}
	}

	@RequestMapping(value = "/indexlog")
	public String indexlog(@RequestParam(value = "id", required = false) String id, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		return "manage/member/loginlog";
	}

	@RequestMapping(value = "/queryUserloginLog")
	public @ResponseBody Object queryUserloginLog(@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "loginip", required = false) String loginip,
			@RequestParam(value="iparea",required=false) String iparea,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if (StringUtils.isNotBlank(account)) {
				params.put("account", account);
			}
			if (StringUtils.isNotBlank(loginip)) {
				params.put("loginip", loginip);
			}
			if(StringUtils.isNotEmpty(iparea)){
				params.put("iparea", "菲律宾");
			}
			Long count = memberInfoService.getCountLog(params);
			params.put("sortColumns", " logintime desc");
			List<UserLoginLog> list = memberInfoService.getListLog(params, startNo == null ? 0 : startNo,
					pageSize == null ? 30 : pageSize);
			return new GridPanel(count, list, true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	////////////////////// 电销用户管理////////////////////////
	@RequestMapping(value = "/dx/index")
	public String dxindex(@RequestParam(value = "id", required = false) String id, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + WebConstants.FRAMEWORK_USER;
		Class<Object> c = null;
		SystemUser systemUser = (SystemUser) redisService.getRedisResult(key, c);
		if (systemUser.getAccount().equals("admin")) {
			request.setAttribute("isAdmin", 1);
		} else {
			request.setAttribute("isAdmin", 0);
		}
		List<Map<String, Object>> params = userManagerService.getDXList(new HashMap<String, Object>());

		Map<String, Object> alldx = new HashMap<String, Object>();
		for (Map<String, Object> map : params) {
			alldx.put(map.get("userId").toString(), map.get("truename"));
		}
		request.setAttribute("alldx", JSONObject.fromObject(alldx).toString());
		// status 状态 0 作废 1 发起（待财务审核） 2 处理中 3 存取成功 4 存取失败
		JSONObject map = new JSONObject();
		map.put("", "全部");
		map.put("0", "作废");
		map.put("1", "发起");
		map.put("2", "处理中");
		map.put("3", "成功");
		map.put("4", "失败");
		request.setAttribute("statusMap", map.toString());
		// 存提款方式 0公司入款，1第三方支付
		JSONObject paymethodsMap = new JSONObject();
		paymethodsMap.put("0", "公司入款");
		paymethodsMap.put("1", "第三方支付");
		request.setAttribute("paymethodsMap", paymethodsMap.toString());
		return "manage/member/usermanager";
	}

	@RequestMapping(value = "/dx/queryUser")
	public @ResponseBody Object dxqueryUser(@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "uname", required = false) String uname,
			@RequestParam(value = "truename", required = false) String truename,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "qq", required = false) String qq,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "paymoney", required = false) String paymoney,
			@RequestParam(value = "starttime", required = false) String starttime,
			@RequestParam(value = "endtime", required = false) String endtime,
			@RequestParam(value = "sequence", required = false) String sequence,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) redisService.getRedisResult(key, c);

			Map<String, Object> params = new HashMap<String, Object>();
			if (StringUtils.isNotBlank(account)) {
				params.put("account", account);
			}
			if (!systemUser.getAccount().equals("admin")) {
				params.put("belongid", systemUser.getUserId());
			}
			if (StringUtils.isNotBlank(uname)) {
				params.put("uname", uname);
			}
			if (StringUtils.isNotBlank(phone)) {
				params.put("phone", phone);
			}
			if (StringUtils.isNotBlank(email)) {
				params.put("email", email);
			}
			if (StringUtils.isNotBlank(qq)) {
				params.put("qq", qq);
			}
			if (StringUtils.isNotBlank(truename)) {
				params.put("truename", truename);
			}
			if (StringUtils.isNotBlank(paymoney)) {
				params.put("paytotal", Integer.valueOf(paymoney));
			}
			String sort = " u.update_date desc";
			if (StringUtils.isNotBlank(sequence)) {
				if ("regtime".equals(sequence)) {
					sort = " u.create_date desc";
				} else if ("logintime".equals(sequence)) {
					sort = " u.update_date desc";
				}
			}
			if (StringUtils.isNotBlank(starttime)) {
				if ("regtime".equals(sequence)) {
					params.put("starttime", " u.create_date >='" + starttime + "'");
				} else if ("logintime".equals(sequence)) {
					params.put("starttime", " u.update_date >='" + starttime + "'");
				}
			}
			if (StringUtils.isNotBlank(endtime)) {
				if ("regtime".equals(sequence)) {
					params.put("endtime", " u.create_date <='" + endtime + "'");
				} else if ("logintime".equals(sequence)) {
					params.put("endtime", " u.update_date <='" + endtime + "'");
				}
			}
			Long count = userManagerService.getCount(params);

			params.put("sortColumns", sort);
			List<UserManager> list = userManagerService.getList(params, startNo == null ? 0 : startNo,
					pageSize == null ? 30 : pageSize);
			List<UserManager> resultList = list;
			if (StringUtils.isNotBlank(ObjectUtils.toString(systemUser))) {
				boolean isAdmin = false;
				if (null != systemUser) {
					List<SystemRole> roleList = systemService.queryRoleByUserId(systemUser.getUserId());
					for (SystemRole sr : roleList) {
						if (-1 != sr.getRoleName().indexOf("管理员")) {
							isAdmin = true;
							break;
						}
					}
				}
				if (!isAdmin) {
					resultList = new ArrayList<UserManager>();
					for (UserManager vo : list) {
						String uemail = vo.getEmail();
						if (StringUtils.isNotEmpty(uemail)) {
							vo.setEmail("****" + uemail.substring(4, uemail.length()));
						} else {
							vo.setEmail("**********");
						}
						String QQ = vo.getQq();
						if (StringUtils.isNotBlank(QQ)) {
							vo.setQq("****" + QQ.substring(2, QQ.length()));
						} else {
							vo.setQq("********");
						}

						String uphone = vo.getPhone();
						if (StringUtils.isNotBlank(uphone)) {
							vo.setPhone(uphone.substring(0, 3) + "****"
									+ uphone.substring(uphone.length() - 4, uphone.length()));
						} else {
							vo.setPhone("**********");
						}

						String ID = vo.getIdentitycard();
						if (null != ID && !"".equals(ID)) {
							vo.setIdentitycard(ID.substring(0, ID.length() - 4) + "****");
						} else {
							vo.setIdentitycard("***********");
						}
						resultList.add(vo);
					}
				}
			}
			return new GridPanel(count, resultList, true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 电销拨打电话
	 * 
	 * @param extno
	 *            分机号
	 * @param phone
	 *            电话号码
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/dx/call/{extno}/{phone}")
	@ResponseBody
	public Object dxCall(@PathVariable String extno, @PathVariable String phone, HttpServletRequest request,
			HttpServletResponse response) {
		// 构造手机号码格式
		long l = Long.parseLong(phone);
		String p = "1" + String.valueOf(l);
		l = Long.parseLong(p) * 11 + 159753;
		if ("0".equals(extno)) { // 首存记录处传"0"
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) redisService.getRedisResult(key, c);
			extno = systemUser.getRemark();
			Pattern pattern = Pattern.compile("[0-9]*");
			if (!pattern.matcher(extno).matches()) {
				return new ExtReturn(false, "分机号码设置错误！");
			}
		}
		try {
			String url = "http://49.213.15.82/atstar/index.php/status-op?op=dialv2&ext_no=" + extno + "&dia_num=" + l;
			String result = new String(HttpUtil.doGet(url), "UTF-8");
			logger.info("呼叫中心返回:" + result);
		} catch (Exception e) {
			logger.error("调用呼叫中心系统异常：", e);
			return new ExtReturn(false, "调用呼叫中心系统异常！");
		}
		return null;
	}

	/**
	 * 客服拨打电话
	 * 
	 * @param extno
	 *            分机号
	 * @param uiid
	 *            用户ID
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/kf/call/{extno}/{uiid}")
	@ResponseBody
	public Object kfCall(@PathVariable String extno, @PathVariable Long uiid, HttpServletRequest request,
			HttpServletResponse response) {
		UserInfo userInfo = userInfoService.queryById(uiid);
		if (StringUtils.isEmpty(userInfo.getPhone())) {
			return new ExtReturn(false, "手机号码不能为空！");
		}
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + WebConstants.FRAMEWORK_USER;
		Class<Object> c = null;
		SystemUser systemUser = (SystemUser) redisService.getRedisResult(key, c);
		extno = systemUser.getRemark();
		Pattern pattern = Pattern.compile("[0-9]*");
		if (!pattern.matcher(extno).matches()) {
			return new ExtReturn(false, "分机号码设置错误！");
		}
		// 构造手机号码格式
		long l = Long.parseLong(userInfo.getPhone());
		String p = "1" + String.valueOf(l);
		l = Long.parseLong(p) * 11 + 159753;
		try {
			String url = "http://49.213.15.82/atstar/index.php/status-op?op=dialv2&ext_no=" + extno + "&dia_num=" + l;
			String result = new String(HttpUtil.doGet(url), "UTF-8");
			logger.info("呼叫中心返回:" + result);
		} catch (Exception e) {
			logger.error("调用呼叫中心系统异常：", e);
			return new ExtReturn(false, "调用呼叫中心系统异常！");
		}
		return null;
	}

	@RequestMapping(value = "/dx/save")
	public @ResponseBody Object dxsave(@RequestParam(value = "uiid", required = false) Integer uiid,
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "payertype", required = false) String payertype,
			@RequestParam(value = "belongid", required = false) Long belongid,
			@RequestParam(value = "remark", required = false) String remark, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if (!StringUtils.isNotBlank(ObjectUtils.toString(uiid))) {
				params.put("account", account);
				UserInfo userinfo = userInfoService.queryUserInfo2(params);
				if (null == userinfo) {
					return new ExtReturn(false, "用户【" + account + "】不存在，无法分配！");
				}
				Long count = userManagerService.getCount(params);
				if (count > 0) {
					return new ExtReturn(false, "用户【" + account + "】已经被分配给其他电销，无法重复分配！");
				}
				UserManager userManager = new UserManager();
				userManager.setUiid(userinfo.getUiid());
				userManager.setBelongid(belongid);
				userManager.setPayertype(payertype);
				boolean success = userManagerService.save(userManager);
				if (success) {
					return new ExtReturn(true, "分配用户成功！");
				} else {
					return new ExtReturn(false, "分配用户失败！");
				}
			} else {
				params.put("uiid", uiid);
				UserManager userManager = userManagerService.getObject(params);
				if (null == userManager) {
					return new ExtReturn(false, "您要备注的用户数据不存在，请刷新后重试。");
				}
				userManager.setRemark(remark);
				boolean success = userManagerService.update(userManager);
				if (success) {
					return new ExtReturn(true, "备注回访信息成功！");
				} else {
					return new ExtReturn(false, "备注回访信息失败！");
				}
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping("/dx/delete/{uiid}")
	@ResponseBody
	public Object dxdelete(@PathVariable Long uiid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(uiid))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			if (userManagerService.delete(uiid)) {
				return new ExtReturn(true, "删除成功！");
			} else {
				return new ExtReturn(false, "删除失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 电销模块 会员存款列表
	 * 
	 * @param account
	 *            会员账号
	 * @param startNo
	 *            当前页
	 * @param pageSize
	 *            每页行数
	 * @return
	 */
	@RequestMapping("/dx/queryDxDepositorder")
	@ResponseBody
	public Object queryDxDepositorder(@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize) {

		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(account)) {
			params.put("uaccount", account);
		}
		params.put("paytyple", "0");
		Long count = userManagerService.getPayOrderCount(params);
		List<PayOrder> list = userManagerService.getPayOrderList(params, startNo, pageSize);
		return new GridPanel(count, list, true);
	}

	/**
	 * 电销模块 会员取款列表
	 * 
	 * @param account
	 *            会员账号
	 * @param startNo
	 *            当前页
	 * @param pageSize
	 *            每页行数
	 * @return
	 */
	@RequestMapping("/dx/queryDxPickUporder")
	@ResponseBody
	public Object queryDxPickUporder(@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize) {

		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(account)) {
			params.put("uaccount", account);
		}
		params.put("paytyple", "1");
		Long count = userManagerService.getPayOrderCount(params);
		List<PayOrder> list = userManagerService.getPayOrderList(params, startNo, pageSize);
		return new GridPanel(count, list, true);
	}

	/**
	 * 电销模块 会员优惠列表
	 * 
	 * @param account
	 *            会员账号
	 * @param startNo
	 *            当前页
	 * @param pageSize
	 *            每页行数
	 * @return
	 */
	@RequestMapping("/dx/queryDxCouponorder")
	@ResponseBody
	public Object queryDxCouponorder(@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize) {

		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(account)) {
			params.put("uaccount", account);
		}
		params.put("paytyple", "2");
		params.put("coupon", true); // 优惠
		Long count = userManagerService.getPayOrderCount(params);
		List<PayOrder> list = userManagerService.getPayOrderList(params, startNo, pageSize);
		return new GridPanel(count, list, true);
	}

	/**
	 * 电销模块 会员洗码列表
	 * 
	 * @param account
	 *            会员账号
	 * @param startNo
	 *            当前页
	 * @param pageSize
	 *            每页行数
	 * @return
	 */
	@RequestMapping("/dx/queryDxMemberXimaMain")
	@ResponseBody
	public Object queryDxMemberXimaMain(@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize) {

		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(account)) {
			params.put("uaccount", account);
		}
		Long count = userManagerService.getXimaCount(params);
		List<MemberXimaMain> list = userManagerService.getXimaList(params, startNo, pageSize);
		return new GridPanel(count, list, true);
	}

	/**
	 * 进入钱包余额查询页面
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryMoneyIndex")
	public String queryMoneyIndex(@RequestParam(value = "id", required = false) String id, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		return "manage/member/queryMoney";
	}

	/**
	 * 钱包余额查询
	 * 
	 * @param account
	 *            会员账号
	 * @param startNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/queryMoney")
	@ResponseBody
	public Object queryMoney(@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(account)) {
			params.put("account", account);
		}
		params.put("orderField", "totalamount desc");
		Long count = accountMoneyService.queryAccountMoneyCount(params);
		List<AccountMoney> list = accountMoneyService.queryAccountMoney(params, startNo, pageSize);
		return new GridPanel(count, list, true);
	}

	/**
	 * 进入电销分机管理页面
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/dx/dxListIndex")
	public String dxListIndex(@RequestParam(value = "id", required = false) String id, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		return "manage/member/dxExtension";
	}

	/**
	 * 查询电销列表
	 * 
	 * @param account
	 * @param startNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/dxList")
	@ResponseBody
	public Object dxList(@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(account)) {
			params.put("account", account);
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		List<Map<String, Object>> list = userManagerService.getDXList(params);
		Long count = 0L;
		if (CollectionUtils.isNotEmpty(list)) {
			count = Long.valueOf(list.size());
		}
		return new GridPanel(count, list, true);
	}

	/**
	 * 设置电销分机号码
	 * 
	 * @param systemUser
	 * @return
	 */
	@RequestMapping("/saveExtension")
	@ResponseBody
	public Object saveExstension(SystemUser systemUser) {
		try {
			if (StringUtils.isEmpty(systemUser.getRemark())) {
				return new ExtReturn(false, "分机号码不能为空！");
			}
			if (userManagerService.saveOrUpdateSystemUser(systemUser)) {
				return new ExtReturn(true, "保存成功！");
			}
			return new ExtReturn(false, "保存失败！");
		} catch (Exception e) {
			logger.error("设置分机号码异常:" + e.getMessage());
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 进入每日首存页面
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/dx/dailyFcIndex")
	public String DailyFcIndex(@RequestParam(value = "id", required = false) String id, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		request.setAttribute("defaultDate", DateUtil.FormatDate(DateUtil.getYesterDay()));
		return "manage/member/DailyFcRecord";
	}

	/**
	 * 分页查询日首充记录。
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/dx/queryDailyFcIndex")
	@ResponseBody
	public Object dxListIndex(@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "pname", required = false) String pname,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		params.put("pname", pname);
		if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {
			params.put("startDate", DateUtil.FormatDate(DateUtil.getYesterDay()));
			params.put("endDate", DateUtil.FormatDate(DateUtil.getYesterDay()));
		} else {
			params.put("startDate", startDate);
			params.put("endDate", endDate);
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		params.put("sortColumns", "paytime desc");
		List<FcRecord> list = fcRecordService.pageFcVisitRecord(params);
		FcRecord sumFc = new FcRecord();
		sumFc.setAccount("总计：");
		sumFc.setMoney(String.valueOf(fcRecordService.sumFcMoney(params)));
		list.add(sumFc);
		Long count = fcRecordService.countFcRecord(params);
		return new GridPanel(count, list, true);
	}

	/**
	 * 更新首充记录。
	 * 
	 * @param rid
	 * @param remark
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "dx/updateDailyFcRecord")
	@ResponseBody
	public Object updateDailyFcRecord(@RequestParam(value = "rid", required = false) Long rid,
			@RequestParam(value = "remark", required = false) String remark, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) redisService.getRedisResult(key, c);
			FcRecord fcRecord = new FcRecord();
			fcRecord.setVisitorId(systemUser.getUserId());
			fcRecord.setVisitor(systemUser.getAccount());
			fcRecord.setVistortime(DateUtil.getStrByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			fcRecord.setRid(rid);
			fcRecord.setRemark(remark);
			fcRecordService.update(fcRecord);
			return new ExtReturn(true, "添加备注成功！");
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ExtReturn(false, "添加备注失败！");
		}
	}

	/**
	 * 风控会员资料
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/fkMemberindex")
	public String fkMemberindex(@RequestParam(value = "id", required = false) String id, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		return "manage/member/fkmemberInfo";
	}

	/**
	 * 查询风控数据
	 * 
	 * @param memberInfoVo
	 * @param startNo
	 * @param pageSize
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryFKMemberInfo")
	public @ResponseBody Object queryFKMemberInfo(@ModelAttribute MemberInfoVo memberInfoVo,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			if (StringUtils.isNotBlank(memberInfoVo.getAccount())) {
				map.put("account", memberInfoVo.getAccount());
			}
			map.put("sortColumns", "create_date desc");
			Long count = memberInfoService.queryFkMemberCount(map);
			List<MemberInfoVo> list = memberInfoService.queryFkMemberResult(map, startNo == null ? 0 : startNo,
					pageSize == null ? 30 : pageSize);
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) redisService.getRedisResult(key, c);
			if (StringUtils.isNotBlank(ObjectUtils.toString(systemUser))) {
				boolean isAdmin = false;
				if (null != systemUser) {
					List<SystemRole> roleList = systemService.queryRoleByUserId(systemUser.getUserId());
					for (SystemRole sr : roleList) {
						if (-1 != sr.getRoleName().indexOf("管理员")) {
							isAdmin = true;
							break;
						}
					}
				}
				if (!isAdmin) {
					for (MemberInfoVo vo : list) {
						if(StringUtils.isNotEmpty(vo.getPuiid()+"") && vo.getPuiid() !=41726){
							String email = vo.getEmail();
							if (null != email && !"".equals(email)) {
								vo.setEmail("****" + email.substring(4, email.length()));
							} else {
								vo.setEmail("**********");
							}
							String QQ = vo.getQq();
							if (StringUtils.isNotBlank(QQ)) {
								vo.setQq("****" + QQ.substring(2, QQ.length()));
							} else {
								vo.setQq("********");
							}
	
							String phone = vo.getPhone();
							if (null != phone && !"".equals(phone)) {
								vo.setPhone(phone.substring(0, 3) + "****"
										+ phone.substring(phone.length() - 4, phone.length()));
							} else {
								vo.setPhone("**********");
							}
	
							String ID = vo.getIdentitycard();
							if (null != ID && !"".equals(ID)) {
								vo.setIdentitycard(ID.substring(0, ID.length() - 4) + "****");
							} else {
								vo.setIdentitycard("***********");
							}
						}
					}
				}
			}
			return new GridPanel(count, list, true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 风控会员详情
	 * 
	 * @param uiid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryFkMemberDetail/{uiid}")
	public String queryFkMemberDetail(@PathVariable long uiid,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uiid", uiid);
			MemberInfoVo memberInfo = memberInfoService.queryFkMemberDetail(map); // 风控会员资料详情
			List<PayOrderLog> payOrderLogList = memberInfoService.queryPayOrderLogResult(map, startNo, pageSize); // 分控资金流水数据
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("uiid", uiid);
			params.put("paytyple", 0);
			List<PayOrder> payOrderList = memberInfoService.queryPayOrderResult(params, startNo, pageSize);// 存款
			params.clear();
			params.put("uiid", uiid);
			params.put("paytyple", 1);
			List<PayOrder> withdrawOrderList = memberInfoService.queryPayOrderResult(params, startNo, pageSize);// 提款
			params.clear();
			params.put("uiid", uiid);
			params.put("paytyple", "2,3");
			List<PayOrder> couponOrderList = memberInfoService.queryPayOrderResult(params, startNo, pageSize);// 加款
			List<GameTransfer> transferlist = memberInfoService.queryTransferResult(map, startNo, pageSize);// 转账
			List<UserLoginLog> loginLoglist = memberInfoService.queryUserLoginLogResult(map, startNo, pageSize);// 会员登录日志
			params.clear();
			params.put("status", 1);
			List<GamePlatform> gamePlatforms = iGamePlatformService.getList(params);
			params.clear();
			params.put("uiid", uiid);
			params.put("sortColumns", "createdate desc");
			List<UserInfoRemark> remarkList = userInfoService.getRemarkList(map, startNo, pageSize);
			request.setAttribute("gamePlatforms", gamePlatforms);
			request.setAttribute("fkMember", memberInfo);
			if(memberInfo.getAccount().startsWith("QB77")){
				request.setAttribute("qbh", true);
			}else{
				request.setAttribute("qbh", false);
			}
			request.setAttribute("payorderlog", payOrderLogList);
			request.setAttribute("payorderlist", payOrderList);
			request.setAttribute("withdrawOrderList", withdrawOrderList);
			request.setAttribute("couponorderList", couponOrderList);
			request.setAttribute("transferList", transferlist);
			request.setAttribute("loginLoglist", loginLoglist);
			request.setAttribute("remarkList", remarkList);
			Properties prop = PropertyContext.PropertyContextFactory("local.properties").getPropertie();
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			boolean flag =false;
			SystemUser systemUser = (SystemUser) redisService.getRedisResult(key, c);
			if (null != systemUser) {
				List<SystemRole> roleList = systemService.queryRoleByUserId(systemUser.getUserId());
				for (SystemRole sr : roleList) {
					if (-1 != sr.getRoleName().indexOf("管理员")) {
						flag =true;
					}
				}
			}
			if(flag){
				request.setAttribute("showFlag", true);
			}else{
				if(prop.get("proxy.hide").toString().indexOf(memberInfo.getPuiid()+"") !=-1){
					request.setAttribute("showFlag", false);
				}else{
					request.setAttribute("showFlag", true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
		}
		return "manage/member/fkinfo";
	}

	/**
	 * 查询用户单个游戏平台余额
	 * 
	 * @param uiid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryUserGameMoney/{gpname}/{uiid}")
	@ResponseBody
	public Object queryGameMoneyByUser(@PathVariable String gpname, @PathVariable long uiid, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("status", 1);
			params.put("gpname", gpname);
			GamePlatform pf = iGamePlatformService.getObject(params);
			UserInfo userInfo = userInfoService.queryById(uiid);
			String strMoney = "";
//			if(gpname.equals(WebConstants.MG)){
//				BigDecimal balanceNum = mgGameAPI.getAccountBalance(userInfo.getAccount());
//				strMoney = balanceNum == null ? "0" : String.valueOf(balanceNum);
//			}else{
				strMoney =(String) gamePlatformHandlerMap.get(pf.getGpname()).queryBalance(userInfo, pf, null);
//			}
			return new ExtReturn(true, strMoney);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
			return new ExtReturn(false, 0);
		}
	}

	/**
	 * 将用户登出PT游戏
	 * 
	 * @param uiid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/logoutGame/{gpname}/{uiid}")
	@ResponseBody
	public Object logoutGame(@PathVariable String gpname, @PathVariable long uiid, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("status", 1);
			params.put("gpname", gpname);
			GamePlatform pf = iGamePlatformService.getObject(params);
			UserInfo userInfo = userInfoService.queryById(uiid);
			String code ="0";
			if(WebConstants.PT.equals(gpname)){
				code = (String) gamePlatformHandlerMap.get(pf.getGpname()).logoutGame(userInfo, pf, null);
			}else if(WebConstants.SA.equals(gpname)){
				KickUserResponse userInfoResponse = (KickUserResponse) gamePlatformHandlerMap.get(pf.getGpname()).logoutGame(userInfo, pf, null);
				// 登出用户成功、用户不存在
				if (userInfoResponse != null && ("0".equals(userInfoResponse.getErrorMsgId()) || "116".equals(userInfoResponse.getErrorMsgId()))) {
					code ="0";
				}
			}
			if ("0".equals(code)) {
				return new ExtReturn(true, "登出用户[" + userInfo.getAccount() + "]成功。");
			}
			return new ExtReturn(false, "登出用户[" + userInfo.getAccount() + "]失败。");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
			return new ExtReturn(false, "网络异常，请稍后重试。");
		}
	}

	/**
	 * 资金流水记录
	 * 
	 * @param uiid
	 * @param startNo
	 * @param pageSize
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryFKPayOrderLog")
	public @ResponseBody Object queryFKPayOrderLog(@RequestParam(value = "uiid", required = false) Long uiid,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uiid", uiid);
			List<PayOrderLog> list = memberInfoService.queryPayOrderLogResult(map, startNo, pageSize);
			return new ExtReturn(true, list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 存款-提款-加款
	 * 
	 * @param uiid
	 * @param paytype
	 * @param startNo
	 * @param pageSize
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryFKPayOrderResult")
	public @ResponseBody Object queryFKPayOrderResult(@RequestParam(value = "uiid", required = false) Long uiid,
			@RequestParam(value = "paytype", required = false) Long paytype,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uiid", uiid);
			map.put("paytyple", paytype);
			List<PayOrder> list = memberInfoService.queryPayOrderResult(map, startNo, pageSize);
			return new ExtReturn(true, list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 转账记录
	 * 
	 * @param uiid
	 * @param startNo
	 * @param pageSize
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryFKTransferResult")
	public @ResponseBody Object queryFKTransferResult(@RequestParam(value = "uiid", required = false) Long uiid,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uiid", uiid);
			List<GameTransfer> list = memberInfoService.queryTransferResult(map, startNo, pageSize);
			return new ExtReturn(true, list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 会员登录日志
	 * 
	 * @param uiid
	 * @param startNo
	 * @param pageSize
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryFKLoginLogResult")
	public @ResponseBody Object queryFKLoginLogResult(@RequestParam(value = "uiid", required = false) Long uiid,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uiid", uiid);
			List<UserLoginLog> list = memberInfoService.queryUserLoginLogResult(map, startNo, pageSize);
			return new ExtReturn(true, list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping(value = "/fk/edit")
	public @ResponseBody Object fkEdit(@RequestParam(value = "uiid", required = false) Integer uiid,
			@RequestParam(value = "remark", required = false) String remark, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser)redisService.getRedisResult(key, c);
			MemberInfo member = memberInfoService.findMemberInfoId(Long.valueOf(uiid));
			member.setRemark(remark);
			memberInfoService.modifyMemberInfo(member);
			
			UserInfoRemark uremark = new UserInfoRemark();
			uremark.setUiid(member.getUiid());
			uremark.setOperator(systemUser.getAccount());
			uremark.setRemark(remark);
			uremark.setCreatedate(new Date());
			userInfoService.saveRemark(uremark);
			return new ExtReturn(true, "添加备注成功！");
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
}
