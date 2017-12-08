package com.gameportal.manage.user.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.manage.member.service.IMemberInfoService;
import com.gameportal.manage.pay.model.PayOrder;
import com.gameportal.manage.pay.service.IPayOrderService;
import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.pojo.TreeProxyuser;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.smsplatform.model.SmsPlatAccount;
import com.gameportal.manage.smsplatform.model.SmsPlatSendlog;
import com.gameportal.manage.smsplatform.service.ISmsPlatInfoService;
import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.user.model.AccountMoney;
import com.gameportal.manage.user.model.UserInfo;
import com.gameportal.manage.user.service.IAccountMoneyService;
import com.gameportal.manage.user.service.IUserInfoService;
import com.gameportal.manage.util.Blowfish;
import com.gameportal.manage.util.CookieUtil;
import com.gameportal.manage.util.DateUtil;
import com.gameportal.manage.util.DateUtil2;
import com.gameportal.manage.util.IdGenerator;
import com.gameportal.manage.util.WebConstants;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/manage")
public class UserInfoManageController {

	@Resource(name = "userInfoServiceImpl")
	private IUserInfoService userInfoService = null;
	@Resource(name = "memberInfoServiceImpl")
	private IMemberInfoService memberInfoService = null;
	@Resource(name = "accountMoneyServiceImpl")
	private IAccountMoneyService accountMoneyService = null;
	@Resource(name = "payOrderServiceImpl")
	private IPayOrderService payOrderService = null;
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService = null;
	@Resource(name="smsPlatInfoServiceImpl")
	private ISmsPlatInfoService smsPlatInfoService;
	private static final Logger logger = Logger
			.getLogger(UserInfoManageController.class);

	public UserInfoManageController() {
		super();
		// TODO Auto-generated constructor stub
	}

	// *****************会员*************************
	@RequestMapping(value = "/member/index")
	public String index(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
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
		return "manage/user/member";
	}

	@RequestMapping(value = "/member/queryMember")
	public @ResponseBody
	Object queryMember(
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		Long count = userInfoService.queryMemberCount(account);
		List<UserInfo> list = userInfoService.queryMember(account, startNo,
				pageSize);
		return new GridPanel(count, list, true);
	}

	@RequestMapping(value = "/member/save")
	@ResponseBody
	public Object saveMember(
			@ModelAttribute UserInfo member,
			@RequestParam(value = "passwdConfirm", required = false) String alignPasswd,
			@RequestParam(value = "atmpasswdConfirm", required = false) String alignAtmpasswd,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Timestamp date = new Timestamp(new Date().getTime());
			if (!StringUtils.isNotBlank(ObjectUtils.toString(member))) {
				return new ExtReturn(false, "会员不能为空！");
			}
			// 检查两次登陆密码是否一致
			if (!StringUtils.equals(member.getPasswd(), alignPasswd)) {
				return new ExtReturn(false, "两次登陆密码不一致！");
			}
			// 检查两次提款密码是否一致
			if (!StringUtils.equals(member.getAtmpasswd(), alignAtmpasswd)) {
				return new ExtReturn(false, "两次提款密码不一致！");

			}
			if (!StringUtils.isBlank(member.getAccount())) {
				return new ExtReturn(false, "帐号不能为空！");
			}
			if (memberInfoService.isExistByAcc(member.getAccount())) {
				return new ExtReturn(false, "此帐号已存在，请选择其它帐号！");
			}
			member.setAccounttype(0); // 账号类型:0 普通用户 1代理用户
			if (StringUtils.isNotBlank(ObjectUtils.toString(member.getUiid()))) {
				member.setUpdateDate(date);
			} else {
				member.setGrade(1); // 会员初始等级为1
				Blowfish bf = new Blowfish(WebConstants.BLOWFISH_CODE);
				member.setPasswd(bf.encryptString(member.getPasswd()));
				member.setAtmpasswd(bf.encryptString(member.getAtmpasswd()));
				member.setCreateDate(date);
				member.setUpdateDate(date);
			}
			if (userInfoService.saveOrUpdateUserInfo(member)) {
				return new ExtReturn(true, "保存成功！");
			} else {
				return new ExtReturn(false, "保存失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping("/member/del/{uiid}")
	@ResponseBody
	public Object delUserInfo(@PathVariable Long uiid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(uiid))) {
				return new ExtReturn(false, "会员主键不能为空！");
			}
			if (userInfoService.deleteUserInfo(uiid)) {
				return new ExtReturn(true, "删除成功！");
			} else {
				return new ExtReturn(false, "删除失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping("/member/enable/{uiid}")
	@ResponseBody
	public Object enableSmsPlatAccount(@PathVariable Long uiid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(uiid))) {
				return new ExtReturn(false, "会员主键不能为空！");
			}
			if (userInfoService.updateStatus(uiid, 2)) {
				return new ExtReturn(true, "启用成功！");
			} else {
				return new ExtReturn(false, "启用失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping("/member/disable/{uiid}")
	@ResponseBody
	public Object disableSmsPlatAccount(@PathVariable Long uiid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(uiid))) {
				return new ExtReturn(false, "会员主键不能为空！");
			}
			if (userInfoService.updateStatus(uiid, 0)) {
				return new ExtReturn(true, "禁用成功！");
			} else {
				return new ExtReturn(false, "禁用失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	/**
	 * 打开周返水
	 * @param uiid
	 * @return
	 */
	@RequestMapping("/member/openWeekRake/{uiid}")
	@ResponseBody
	public Object openWeekRake(@PathVariable Long uiid) {
		try {
			UserInfo userInfo = userInfoService.queryById(uiid);
			userInfo.setWithdrawlFlag("1");
			if (userInfoService.saveOrUpdateUserInfo(userInfo)) {
				return new ExtReturn(true, "打开提款成功！");
			} else {
				return new ExtReturn(false, "打开提款失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	/**
	 * 关闭提款
	 * @param uiid
	 * @return
	 */
	@RequestMapping("/member/closeWeekRake/{uiid}")
	@ResponseBody
	public Object closeWeekRake(@PathVariable Long uiid) {
		try {
			UserInfo userInfo = userInfoService.queryById(uiid);
			userInfo.setWithdrawlFlag("0");
			if (userInfoService.saveOrUpdateUserInfo(userInfo)) {
				return new ExtReturn(true, "关闭提款成功！");
			} else {
				return new ExtReturn(false, "关闭提款失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping(value = "/member/resetPwd/{type}/{userId}")
	@ResponseBody
	public Object resetPwd(@PathVariable Integer type,
			@PathVariable Long userId, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(userId))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			if (userInfoService.resetPwd(type, userId)) {
				return new ExtReturn(true, "密码重置成功！");
			} else {
				return new ExtReturn(false, "密码重置失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	// *****************代理用户*************************
	@RequestMapping(value = "/userinfo/index")
	public String indexUserinfo(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
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
		JSONObject accounttypeMap = new JSONObject();
		accounttypeMap.put("0", "普通会员");
		accounttypeMap.put("1", "代理会员");
		request.setAttribute("accounttypeMap", accounttypeMap.toString());
		return "manage/user/userinfo";
	}

	@RequestMapping(value = "/userinfo/queryUserinfo")
	public @ResponseBody
	Object queryUserinfo(
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		Map params = new HashMap();
		if(StringUtils.isNotBlank(account)){
			params.put("account", account);
		}
		Long count = userInfoService.queryUserinfoCount(params);
		List<UserInfo> list = userInfoService.queryUserinfo(params, startNo,
				pageSize);
		return new GridPanel(count, list, true);
	}

	@RequestMapping(value = "/userinfo/queryAbove")
	public @ResponseBody
	Object queryAbove(HttpServletRequest request, HttpServletResponse response) {
		Map params = new HashMap();
		params.put("accounttype", 1); // '账号类型 0 普通用户 1代理用户'
		params.put("sortColumns", "uiid asc");
		List<UserInfo> list = userInfoService.queryUserinfo(params, null, null);
		if (null == list || 0 == list.size()) {
			return JSONArray.fromObject("[]");
		}
		TreeProxyuser menu = new TreeProxyuser(list);
		return JSONArray.fromObject(JSONArray
				.fromObject(menu.getAllChildTreeJson()).toString()
				.replaceAll("\"leaf", "\"checked\":false,\"leaf"));
	}
	
	@RequestMapping(value = "/userinfo/queryAboveById")
	public @ResponseBody
	Object queryAboveById(@RequestParam(value = "uiid", required = false) Long uiid,
			HttpServletRequest request, HttpServletResponse response) {
		if (null == uiid) {
			return JSONArray.fromObject("[]");
		}
		UserInfo userInfo = userInfoService.queryById(uiid);
		if (null == userInfo) {
			return JSONArray.fromObject("[]");
		}
		Map params = new HashMap();
		params.put("accounttype", 1); // '账号类型 0 普通用户 1代理用户'
		params.put("sortColumns", "uiid asc"); 
		List<UserInfo> list = userInfoService.queryUserinfo(params, null, null);
		if (null == list || 0 == list.size()) {
			return JSONArray.fromObject("[]");
		}
		TreeProxyuser menu = new TreeProxyuser(list, userInfo);
		JSONArray arr = JSONArray.fromObject(JSONArray
				.fromObject(menu.getParentTreeJson()).toString()
				.replaceAll("\"leaf", "\"checked\":false,\"leaf"));
		this.setChecked(arr, uiid.toString());
		return arr;
//		return JSONArray.fromObject(JSONArray
//				.fromObject(menu.getParentTreeJson()).toString()
//				.replaceAll("\"leaf", "\"checked\":false,\"leaf"));
	}
	
	private void setChecked(JSONArray arr, String uiid) {
		Iterator<JSONObject> it = arr.iterator();
		for (; it.hasNext();) {
			JSONObject jsonObj = it.next();
			if (null != jsonObj) {
				if (("_generate_" + uiid).equals(jsonObj.getString("id"))) {
					jsonObj.put("checked", true);
					return;
				} else {
					JSONArray arr2 = jsonObj.getJSONArray("children");
					if (null != arr2 && arr2.size() > 0) {
						setChecked(arr2, uiid);
					}
				}
			}
		}
	}

	@RequestMapping(value = "/userinfo/save")
	@ResponseBody
	public Object saveUserinfo(
			@ModelAttribute UserInfo userinfo,
			@RequestParam(value = "passwdConfirm", required = false) String alignPasswd,
			@RequestParam(value = "atmpasswdConfirm", required = false) String alignAtmpasswd,
			@RequestParam(value = "proxyIds", required = false) String proxyIds,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Timestamp date = new Timestamp(new Date().getTime());
			if (!StringUtils.isNotBlank(ObjectUtils.toString(userinfo))) {
				return new ExtReturn(false, "会员不能为空！");
			}
			if (StringUtils.isBlank(userinfo.getAccount())) {
				return new ExtReturn(false, "帐号不能为空！");
			}
			if (StringUtils
					.isNotBlank(ObjectUtils.toString(userinfo.getUiid()))) {
				UserInfo old = userInfoService.queryById(userinfo.getUiid());
				userinfo.setPasswd(old.getPasswd());
				userinfo.setAtmpasswd(old.getAtmpasswd());
				userinfo.setUpdateDate(date);
			} else {
				// 设置上线代理ID
				if (StringUtils.isNotBlank(proxyIds)) {
					String[] proxyIdArr = proxyIds.split(",");
					if (null != proxyIdArr && proxyIdArr.length > 0) {
						userinfo.setPuiid(Long.parseLong(proxyIdArr[0]));
					}
				}
				// 检查两次登陆密码是否一致
				if (!StringUtils.equals(userinfo.getPasswd(), alignPasswd)) {
					return new ExtReturn(false, "两次登陆密码不一致！");
				}
				// 检查两次提款密码是否一致
				if (!StringUtils
						.equals(userinfo.getAtmpasswd(), alignAtmpasswd)) {
					return new ExtReturn(false, "两次提款密码不一致！");

				}
				if (memberInfoService.isExistByAcc(userinfo.getAccount())) {
					return new ExtReturn(false, "此帐号已存在，请选择其它帐号！");
				}
				userinfo.setGrade(1); // 会员初始等级为1
				Blowfish bf = new Blowfish(WebConstants.BLOWFISH_CODE);
				userinfo.setPasswd(bf.encryptString(userinfo.getPasswd()));
				userinfo.setAtmpasswd(bf.encryptString(userinfo.getAtmpasswd()));
				userinfo.setCreateDate(date);
				userinfo.setUpdateDate(date);
				userinfo.setApipassword(bf.encryptString(userinfo.getPasswd()));
			}
			if (userInfoService.saveOrUpdateUserInfo(userinfo)) {
				return new ExtReturn(true, "保存成功！");
			} else {
				return new ExtReturn(false, "保存失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping("/userinfo/del/{uiid}")
	@ResponseBody
	public Object delUserinfo(@PathVariable Long uiid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(uiid))) {
				return new ExtReturn(false, "会员主键不能为空！");
			}
			if (userInfoService.deleteUserInfo(uiid)) {
				return new ExtReturn(true, "删除成功！");
			} else {
				return new ExtReturn(false, "删除失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping("/userinfo/enable/{uiid}")
	@ResponseBody
	public Object enableUserinfo(@PathVariable Long uiid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(uiid))) {
				return new ExtReturn(false, "会员主键不能为空！");
			}
			if (userInfoService.updateStatus(uiid, 2)) {
				return new ExtReturn(true, "启用成功！");
			} else {
				return new ExtReturn(false, "启用失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping("/userinfo/disable/{uiid}")
	@ResponseBody
	public Object disableUserinfo(@PathVariable Long uiid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(uiid))) {
				return new ExtReturn(false, "会员主键不能为空！");
			}
			if (userInfoService.updateStatus(uiid, 0)) {
				return new ExtReturn(true, "禁用成功！");
			} else {
				return new ExtReturn(false, "禁用失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping(value = "/userinfo/resetPwd/{type}/{userId}")
	@ResponseBody
	public Object resetUserinfoPwd(@PathVariable Integer type,
			@PathVariable Long userId, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(userId))) {
				return new ExtReturn(false, "会员主键不能为空！");
			}
			if (userInfoService.resetPwd(type, userId)) {
				return new ExtReturn(true, "密码重置成功！");
			} else {
				return new ExtReturn(false, "密码重置失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	
	// ------------------------------------------------------------------------------------
	// 会员钱包
	// ------------------------------------------------------------------------------------
	
	@RequestMapping(value = "/accountmoney/index")
	public String indexAM(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		// status 状态 0 关闭 1 启用
		JSONObject map = new JSONObject();
		map.put("0", "锁定");
		map.put("1", "未锁定");
		request.setAttribute("statusMap", map.toString());
		// paytyple 0存款，1提款，2赠送，3扣款
		JSONObject paytypleMap = new JSONObject();
		paytypleMap.put("0", "存款");
		paytypleMap.put("1", "提款");
		paytypleMap.put("2", "赠送");
		paytypleMap.put("3", "扣款");
		request.setAttribute("paytypleMap", paytypleMap.toString());
		return "manage/user/accountmoney";
	}

	@RequestMapping(value = "/accountmoney/queryAccountmoney")
	public @ResponseBody
	Object queryAccountmoney(
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "status", required = false) Integer status,

			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(account)) {
			params.put("account", account);
		}
		if (null != status) {
			params.put("status", status);
		}
		Long count = accountMoneyService.queryAccountMoneyCount(params);
		List<AccountMoney> list = accountMoneyService.queryAccountMoney(params,startNo==null?0:startNo, pageSize==null?30:pageSize);
		return new GridPanel(count, list, true);
	}

	@RequestMapping(value = "/accountmoney/changeamount/{paystyple}")
	@ResponseBody
	public Object changeAmount(
			@PathVariable int paystyple, 
			@RequestParam(value = "amid", required = false) Long amid,
			@RequestParam(value = "uiid", required = false) Long uiid,
			@RequestParam(value = "amount", required = false) Double amount,
			@RequestParam(value = "ordertype", required = false) Integer ordertype,
			@RequestParam(value = "kfremarks", required = false) String kfremarks,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			if (paystyple != 2 && paystyple != 3) { // 0存款，1提款，2赠送，3扣款
				return new ExtReturn(false, "paystyple值异常，请联系开发人员！");
			}
			if (!StringUtils.isNotBlank(ObjectUtils.toString(amid))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			if (null == amount || amount == 0) {
				return new ExtReturn(false, "金额不能为空或0");
			}
			if (StringUtils.isNotBlank(ObjectUtils.toString(amid))) {
				String vuid = CookieUtil.getOrCreateVuid(request, response);
				String key = vuid + WebConstants.FRAMEWORK_USER;
				Class<Object> c = null;
				SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(
						key, c);

				// 会员账号检验
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("uiid", uiid);
				List<UserInfo> userList = userInfoService.queryUserinfo(params,
						null, null);
				if (null == userList || userList.size() == 0) {
					return new ExtReturn(false, "该记录对应会员不存在，请刷新后再试！");
				}
				UserInfo user = userList.get(0);
				
				String now = DateUtil2.format2(new Date());
//				AccountMoney am= new AccountMoney();
//				am.setAmid(amid);
//				if (paystyple == 2) { // 0存款，1提款，2赠送，3扣款
//					am.setTotalamount(new BigDecimal(amount));
//				} else if (paystyple == 3) {
//					am.setTotalamount(new BigDecimal(0 - Math.abs(amount)));
//				}
//				am.setUpdateDate(new Date());
//				am.setUiid(user.getUiid());
				
				PayOrder payOrder = new PayOrder();
				if (paystyple == 2) { // 0存款，1提款，2赠送，3扣款
					payOrder.setPoid(IdGenerator.genOrdId16("REWARD"));
				} else if (paystyple == 3) {
					AccountMoney am = accountMoneyService.queryById(amid);
					am.setStatus(0);
					accountMoneyService.saveOrUpdateAccountMoney(am);
					payOrder.setPoid(IdGenerator.genOrdId16("PUNISH"));
				}
				payOrder.setPlatformorders("");
				payOrder.setPaytyple(paystyple);
				payOrder.setAmount(new BigDecimal(amount));
				payOrder.setKfid(systemUser.getUserId());
				payOrder.setKfopttime(now);
				payOrder.setKfname(systemUser.getRealName());
				payOrder.setKfremarks(kfremarks);
				payOrder.setUiid(user.getUiid());
				payOrder.setUaccount(user.getAccount());
				payOrder.setUrealname(user.getUname());
				payOrder.setOrdertype(ordertype);
				payOrder.setDeposittime(now);
				payOrder.setCreateDate(now);
				payOrder.setStatus(1); // status 状态 0 作废 1 发起（待财务审核） 2 处理中 3 存取成功 4 存取失败

				if (payOrderService.savePayOrder(payOrder)) {
					return new ExtReturn(true, "操作成功！");
				} else {
					return new ExtReturn(false, "操作失败！");
				}
			} else {
				return new ExtReturn(false, "该记录不存在，请刷新后再试！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
		
	}

	@RequestMapping("/accountmoney/lock/{status}/{amid}")
	@ResponseBody
	public Object del(@PathVariable Integer status, @PathVariable Long amid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(amid))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			if (accountMoneyService.updateStatus(amid, status)) {
				return new ExtReturn(true, "操作成功！");
			} else {
				return new ExtReturn(false, "操作失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	/**
	 * 手动发送短信。
	 * 
	 * @param uiid
	 * @return
	 */
	@RequestMapping("/member/sendSms/{uiid}")
	@ResponseBody
	public Object sendSms(@PathVariable long uiid, String smsInfo) {
		try {
			Map<String, Object> values = new HashMap<String, Object>();
			values.put("status", 1);
			List<SmsPlatAccount> smsPlatAccounts = smsPlatInfoService.getList(values);
			if (CollectionUtils.isEmpty(smsPlatAccounts)) {
				logger.info("请开启短信配置。");
				return new ExtReturn(false, "请开启短信配置！");
			}
			UserInfo userInfo = userInfoService.queryById(uiid);
			SmsPlatAccount sms = smsPlatAccounts.get(0);
			String code = smsPlatInfoService.sendSMS(userInfo.getPhone(), smsInfo, "2", sms);
			if (!"2000".equals(code)) {
				return new ExtReturn(false, "短信发送失败！");
			}
			// 记录发送短信日志
			SmsPlatSendlog log = new SmsPlatSendlog();
			log.setSpaid(sms.getSpaid());
			log.setUsername(userInfo.getAccount());
			log.setMobile(userInfo.getPhone());
			log.setContent(smsInfo);
			log.setType(4); // 电销发送
			log.setSendtime(DateUtil.getStrByDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
			smsPlatInfoService.saveSmsPlat(log);
			return new ExtReturn(true, "短信发送成功！");
		} catch (Exception e) {
			logger.error("发送短信失败：", e);
		}
		return null;
	}
	
	/**
	 * 手动发送匿名短信。
	 * 
	 * @param phone
	 *            手机号码
	 * @smsInfo 短信内容
	 * @return
	 */
	@RequestMapping("/member/sendAnoSms")
	@ResponseBody
	public Object sendAnoSms(@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "smsInfo", required = false) String smsInfo) {
		try {
			Map<String, Object> values = new HashMap<String, Object>();
			values.put("status", 1);
			List<SmsPlatAccount> smsPlatAccounts = smsPlatInfoService.getList(values);
			if (CollectionUtils.isEmpty(smsPlatAccounts)) {
				logger.info("请开启短信配置。");
				return new ExtReturn(false, "请开启短信配置！");
			}
			SmsPlatAccount sms = smsPlatAccounts.get(0);
			String code = smsPlatInfoService.sendSMS(phone, smsInfo, "2", sms);
			if (!"2000".equals(code)) {
				return new ExtReturn(false, "短信匿名发送失败！");
			}
			// 记录发送短信日志
			SmsPlatSendlog log = new SmsPlatSendlog();
			log.setSpaid(sms.getSpaid());
			log.setUsername(phone);
			log.setMobile(phone);
			log.setContent(smsInfo);
			log.setType(4); // 电销发送
			log.setSendtime(DateUtil.getStrByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			smsPlatInfoService.saveSmsPlat(log);
			return new ExtReturn(true, "短信匿名发送成功！");
		} catch (Exception e) {
			logger.error("发送匿名短信失败：", e);
		}
		return null;
	}
	
	/**
	 * 认证手机、邮箱
	 * @param status
	 * @param amid
	 * @return
	 */
	@RequestMapping("/member/authInfo/{type}/{uiid}")
	@ResponseBody
	public Object authInfo(@PathVariable String type, @PathVariable Long uiid) {
		try {
			UserInfo userInfo = userInfoService.queryById(uiid);
			if ("1".equals(type)) {
				if(userInfo.getPhonevalid() ==1){
					return new ExtReturn(true, "该会员手机号码已经认证通过,不能重复认证！");
				}else{
					userInfo.setPhonevalid(1);
					userInfoService.saveOrUpdateUserInfo(userInfo);
					return new ExtReturn(true, "该会员手机号码已经认证通过！");
				}
			}else if("2".equals(type)){
				if(userInfo.getEmailvalid() ==1){
					return new ExtReturn(true, "该会员邮箱已经认证通过,不能重复认证！");
				}else{
					userInfo.setEmailvalid(1);
					userInfoService.saveOrUpdateUserInfo(userInfo);
					return new ExtReturn(true, "该会员邮箱已经认证通过！");
				}
			}
			return new ExtReturn(true, "操作成功！");
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
}
