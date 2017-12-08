package com.gameportal.manage.pay.controller;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.manage.listener.SystemFieldsCache;
import com.gameportal.manage.member.model.UserManager;
import com.gameportal.manage.member.model.XimaFlag;
import com.gameportal.manage.member.service.IUserManagerService;
import com.gameportal.manage.member.service.IXimaFlagService;
import com.gameportal.manage.order.model.CompanyCard;
import com.gameportal.manage.order.service.ICompanyCardService;
import com.gameportal.manage.pay.model.Activity;
import com.gameportal.manage.pay.model.ActivityFlag;
import com.gameportal.manage.pay.model.PayOrder;
import com.gameportal.manage.pay.model.PayOrderLog;
import com.gameportal.manage.pay.model.api.GstWithdrawal;
import com.gameportal.manage.pay.service.IActivityService;
import com.gameportal.manage.pay.service.IPayOrderService;
import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.system.model.SystemRole;
import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.system.service.ISystemService;
import com.gameportal.manage.user.model.AccountMoney;
import com.gameportal.manage.user.model.CardPackage;
import com.gameportal.manage.user.model.UserInfo;
import com.gameportal.manage.user.model.WithdrawalcountLog;
import com.gameportal.manage.user.service.IAccountMoneyService;
import com.gameportal.manage.user.service.IUserInfoService;
import com.gameportal.manage.util.CookieUtil;
import com.gameportal.manage.util.DateUtil2;
import com.gameportal.manage.util.ExcelUtil;
import com.gameportal.manage.util.IdGenerator;
import com.gameportal.manage.util.WebConstants;
import com.gameportal.portal.util.DateUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/manage")
public class PayOrderController {

	@Resource(name = "ximaFlagService")
	private IXimaFlagService ximaFlagService;
	@Resource(name = "systemServiceImpl")
	private ISystemService iSystemService = null;
	@Resource(name = "companyCardServiceImpl")
	private ICompanyCardService companyCardService = null;
	@Resource(name = "payOrderServiceImpl")
	private IPayOrderService payOrderService = null;
	@Resource(name = "userInfoServiceImpl")
	private IUserInfoService userInfoService = null;
	@Resource(name = "accountMoneyServiceImpl")
	private IAccountMoneyService accountMoneyService = null;
	@Resource(name = "systemServiceImpl")
	private ISystemService systemService = null;
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService = null;
	@Resource(name = "activityService")
	private IActivityService activityService;
	@Resource(name = "userManagerService")
	private IUserManagerService userManagerService;

	private static final Logger logger = Logger.getLogger(PayOrderController.class);

	public PayOrderController() {
		super();
		// TODO Auto-generated constructor stub
	}

	// ------------------------------------------------------------------------------------
	// 存款订单（公司入款）
	// ------------------------------------------------------------------------------------

	@RequestMapping(value = "/depositorder/index")
	public String index(@RequestParam(value = "id", required = false) String id, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
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
		// 公司银行卡
		Map<String, Object> params = new HashMap<String, Object>();
		// params.put("status", 0); // '银行卡状态 0未锁定 1锁定',
		params.put("sortColumns", "ccid asc");
		List<CompanyCard> ccList = companyCardService.queryCompanyCard(params, null, null);
		if (null != ccList && ccList.size() > 0) {
			JSONObject ccnoMap = new JSONObject();
			for (CompanyCard cc : ccList) {
				ccnoMap.put(cc.getCcno() + "::" + cc.getBankname() + "::" + cc.getOpenbank() + "::" + cc.getCcholder(),
						"[" + cc.getBankname() + "] " + cc.getCcno());
			}
			request.setAttribute("ccnoMap", ccnoMap.toString());
		}

		// 是否为客服、账务
		{
			int isKF = 0; // 0否，1是
			int isCW = 0; // 0否，1是
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(key, c);
			if (null != systemUser) {
				List<SystemRole> roleList = systemService.queryRoleByUserId(systemUser.getUserId());
				for (SystemRole sr : roleList) {
					if (-1 != sr.getRoleName().indexOf("客服")) {
						isKF = 1;
					} else if (-1 != sr.getRoleName().indexOf("财务")) {
						isCW = 1;
					} else if (-1 != sr.getRoleName().indexOf("管理员")) {
						isKF = 1;
						isCW = 1;
					}
				}
			}
			request.setAttribute("isKF", isKF);
			request.setAttribute("isCW", isCW);
		}
		/* 加载优惠活动代码 */
		params.clear();
		params.put("status", 1);
		List<Activity> alist = activityService.getList(params);
		Map<String, Object> activityList = new HashMap<String, Object>();
		activityList.put("", "不申请优惠");
		if (null != alist && alist.size() > 0) {
			for (Activity obj : alist) {
				activityList.put(obj.getHdnumber(), obj.getHdtext());
			}
		}
		request.setAttribute("yhcode", JSONObject.fromObject(activityList).toString());
		return "manage/payorder/depositorder";
	}

	@RequestMapping(value = "/depositorder/queryDepositorder")
	public @ResponseBody Object queryDepositorder(@RequestParam(value = "uaccount", required = false) String uaccount,
			@RequestParam(value = "bankcard", required = false) String bankcard,
			@RequestParam(value = "bankname", required = false) String bankname,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "amount", required = false) Integer amount,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + WebConstants.FRAMEWORK_USER;
		Class<Object> c = null;
		SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(key, c);
		boolean manage = false;
		;
		if (null != systemUser) {
			List<SystemRole> roleList = systemService.queryRoleByUserId(systemUser.getUserId());
			for (SystemRole sr : roleList) {
				if (-1 != sr.getRoleName().indexOf("合作伙伴")) {
					manage = true;
					break;
				}
			}
		}
		if (manage) {
			JSONObject jsonMap = JSONObject.fromObject(SystemFieldsCache.fields.get("proxy.ids"));
			String s = jsonMap.toString();
			s = s.substring(2, s.indexOf(":") - 1);
			params.put("manageIds", s);
		}
		if (StringUtils.isNotBlank(uaccount)) {
			params.put("uaccount", uaccount);
		}
		if (StringUtils.isNotBlank(bankcard)) {
			params.put("bankcard", bankcard);
		}
		if (StringUtils.isNotBlank(bankname)) {
			params.put("bankname", bankname);
		}
		if (null != amount) {
			params.put("amount", amount);
		}
		if (null != status) {
			params.put("status", status);
		}
		if (null != startDate) {
			params.put("startDate", startDate);
		}
		if (null != endDate) {
			params.put("endDate", endDate);
		}
		params.put("paytyple", 0);
		params.put("paymethods", 0);
		params.put("sortColumns", "deposittime desc");
		Long count = payOrderService.queryPayOrderCount(params);
		List<PayOrder> list = payOrderService.queryPayOrder(params, startNo, pageSize);
		return new GridPanel(count, list, true);
	}

	/**
	 * 存款支订单报表导出
	 * 
	 * @param request
	 *            请求对象
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @param response
	 *            响应对象
	 * @throws Exception
	 */
	@RequestMapping(value = "/depositorder/toDownloadReport")
	@ResponseBody
	public void depositorderToDownloadData(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "uaccount", required = false) String uaccount,
			@RequestParam(value = "bankcard", required = false) String bankcard,
			@RequestParam(value = "bankname", required = false) String bankname,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "amount", required = false) Integer amount,
			@RequestParam(value = "status", required = false) Integer status) throws Exception {
		OutputStream fOut = null;
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if (StringUtils.isNotBlank(uaccount)) {
				params.put("uaccount", uaccount);
			}
			if (StringUtils.isNotBlank(bankcard)) {
				params.put("bankcard", bankcard);
			}
			if (StringUtils.isNotBlank(bankname)) {
				params.put("bankname", bankname);
			}
			if (null != amount) {
				params.put("amount", amount);
			}
			if (null != status) {
				params.put("status", status);
			}
			if (null != startDate) {
				params.put("startDate", startDate);
			}
			if (null != endDate) {
				params.put("endDate", endDate);
			}
			params.put("paytyple", 0);
			params.put("paymethods", 0);
			params.put("sortColumns", "deposittime desc");
			// --------------------
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");// 设置类型
			String fileName = "depositorder.xls";
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			// 数据源
			List<PayOrder> payOrderList = payOrderService.queryPayOrder(params);
			for (PayOrder payOrder : payOrderList) {
				if (null != payOrder.getStatus()) { // 当前状态
					if (payOrder.getStatus() == 0) {
						payOrder.setStatusname("作废");
					} else if (payOrder.getStatus() == 1) {
						payOrder.setStatusname("发起");
					} else if (payOrder.getStatus() == 2) {
						payOrder.setStatusname("处理中");
					} else if (payOrder.getStatus() == 3) {
						payOrder.setStatusname("成功");
					} else if (payOrder.getStatus() == 4) {
						payOrder.setStatusname("失败");
					}
				}
				if (null != payOrder.getPaymethods()) { // 付款方式
					if (payOrder.getPaymethods() == 0) {
						payOrder.setPaymethodsname("公司入款");
					} else if (payOrder.getPaymethods() == 1) {
						payOrder.setPaymethodsname("第三方支付");
					}
				}
			}
			// excel表头
			LinkedHashMap<String, String> viewMap = new LinkedHashMap<String, String>();
			viewMap.put("poid", "存款订单ID");
			viewMap.put("statusname", "当前状态");
			viewMap.put("uaccount", "会员账号");
			viewMap.put("urealname", "会员名称");
			viewMap.put("bankname", "银行名称");
			viewMap.put("bankcard", "存入银行卡");
			viewMap.put("deposit", "卡户行名称");
			viewMap.put("openname", "开户人");
			viewMap.put("amount", "存款金额(元)");
			viewMap.put("paymethodsname", "存款方式");
			viewMap.put("deposittime", "存款时间");
			viewMap.put("kfremarks", "客服备注");
			viewMap.put("kfname", "操作客服");
			viewMap.put("kfopttime", "客服操作时间");
			viewMap.put("cwremarks", "财务备注");
			viewMap.put("cwname", "操作财务");
			viewMap.put("cwopttime", "财务操作时间");
			Workbook workBook = ExcelUtil.generateSingleWorkBook(fileName.toString(), payOrderList, viewMap);
			// 以流的形式下载文件=
			fOut = response.getOutputStream();
			workBook.write(fOut);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fOut != null) {
				fOut.flush();
				fOut.close();
			}
		}
	}

	@RequestMapping(value = "/depositorder/save")
	@ResponseBody
	public Object save(@ModelAttribute PayOrder payOrder,
			@RequestParam(value = "deposittimenew", required = false) String deposittimenew, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String date = DateUtil2.format2(new Date());
			if (!StringUtils.isNotBlank(ObjectUtils.toString(payOrder))) {
				return new ExtReturn(false, "存款订单不能为空！");
			}
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(key, c);

			if (StringUtils.isNotBlank(ObjectUtils.toString(payOrder.getPoid()))) {
				PayOrder old = payOrderService.queryById(payOrder.getPoid());
				// 正常修改
				if (null != old) {
					if (1 == old.getStatus()) {
						return new ExtReturn(false, "状态为“待财务审核”的存款订单不能再修改！");
					} else if (3 == old.getStatus()) {
						return new ExtReturn(false, "状态为“存款成功”的存款订单不能再修改！");
					}
					payOrder.setCwid(old.getCwid());
					payOrder.setCwname(old.getCwname());
					payOrder.setCwopttime(old.getCwopttime());
					payOrder.setCwremarks(old.getCwremarks());
				}
				// 状态为“存款失败”和“作废”的存款订单，在修改后，由客服重新提交给账务审核
				payOrder.setKfid(systemUser.getUserId());
				payOrder.setKfname(systemUser.getAccount());
				payOrder.setKfopttime(date);
				payOrder.setStatus(1);
				if (payOrderService.updatePayOrder(payOrder)) {
					return new ExtReturn(true, "提交成功！");
				} else {
					return new ExtReturn(false, "提交失败！");
				}
			} else {
				// 客服新建存款订单
				// 会员账号检验
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("account", payOrder.getUaccount());
				List<UserInfo> userList = userInfoService.queryUserinfo(params, null, null);
				if (null == userList || userList.size() == 0) {
					return new ExtReturn(false, "不存在[" + payOrder.getUaccount() + "]对应会员，请填写有效的会员账号！");
				}
				UserInfo user = userList.get(0);
				payOrder.setUiid(user.getUiid());
				payOrder.setUrealname(user.getUname());
				// 卡号检验
				params = new HashMap<String, Object>();
				// params.put("status", 0); // '银行卡状态 0未锁定 1锁定',
				params.put("sortColumns", "updatetime asc");
				List<CompanyCard> ccList = companyCardService.queryCompanyCard(params, null, null);
				if (null != ccList && ccList.size() > 0) {
					Map<String, CompanyCard> ccMap = new HashMap<String, CompanyCard>();
					for (CompanyCard cc : ccList) {
						ccMap.put(cc.getCcno(), cc);
					}
					if (false == ccMap.containsKey(payOrder.getBankcard())) {
						return new ExtReturn(false, "不存在[" + payOrder.getBankcard() + "]对应有效公司银行卡，请刷新页面后再操作！");
					}
				} else {
					return new ExtReturn(false, "不存在[" + payOrder.getBankcard() + "]对应有效公司银行卡，请刷新页面后再操作！");
				}

				payOrder.setPoid(IdGenerator.genOrdId16("IN"));
				payOrder.setPlatformorders(IdGenerator.genOrdId16(""));
				payOrder.setPaytyple(0); // 0存款，1提款，2赠送，3扣款
				payOrder.setKfopttime(date);
				payOrder.setKfid(systemUser.getUserId());
				payOrder.setKfname(systemUser.getAccount());
				payOrder.setStatus(1);
				payOrder.setCreateDate(date);
				if (StringUtils.isNotEmpty(user.getPuiid() + "") && user.getPuiid() > 0) {
					user = userInfoService.queryById(user.getPuiid());
					if (user != null) {
						payOrder.setProxyname(user.getAccount());
					}
				}
				payOrder.setDeposittime(DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT));
				if (payOrderService.savePayOrder(payOrder)) {
					return new ExtReturn(true, "提交成功！");
				} else {
					return new ExtReturn(false, "提交失败！");
				}
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping("/depositorder/del/{poid}")
	@ResponseBody
	public Object del(@PathVariable String poid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(poid))) {
				return new ExtReturn(false, "存款订单主键不能为空！");
			}
			PayOrder old = payOrderService.queryById(poid);
			if (null != old) {
				if (1 == old.getStatus()) {
					return new ExtReturn(false, "不能删除状态为“待客服核实”的存款订单！");
				} else if (2 == old.getStatus()) {
					return new ExtReturn(false, "不能删除状态为“待财务审核”的存款订单！");
				} else if (3 == old.getStatus()) {
					return new ExtReturn(false, "不能删除状态为“存款成功”的存款订单！");
				}
			}
			if (payOrderService.deletePayOrder(poid)) {
				return new ExtReturn(true, "删除成功！");
			} else {
				return new ExtReturn(false, "删除失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping("/depositorder/auditKf/{result}")
	@ResponseBody
	public Object auditKf(@PathVariable Integer result, @ModelAttribute PayOrder payOrder, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String date = DateUtil2.format2(new Date());
			if (!StringUtils.isNotBlank(ObjectUtils.toString(payOrder))) {
				return new ExtReturn(false, "存款订单主键不能为空！");
			}
			if (null == result) {
				return new ExtReturn(false, "审核结果不能为空！");
			}
			PayOrder old = payOrderService.queryById(payOrder.getPoid());
			if (null != old) {
				if (2 != old.getStatus()) {
					return new ExtReturn(false, "只能审核状态为“待财务审核”的存款订单！");
				}
			}
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(key, c);
			// 客服核实
			old.setKfremarks(payOrder.getKfremarks());
			old.setKfopttime(date);
			old.setKfid(systemUser.getUserId());
			old.setKfname(systemUser.getAccount());
			old.setStatus(result);
			old.setUpdateDate(date);

			if (payOrderService.updatePayOrder(payOrder)) {
				return new ExtReturn(true, "提交成功！");
			} else {
				return new ExtReturn(false, "提交失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping("/depositorder/auditCw/{result}")
	@ResponseBody
	public synchronized Object auditCw(@PathVariable Integer result, @ModelAttribute PayOrder payOrder,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(payOrder))) {
				return new ExtReturn(false, "加款订单主键不能为空！");
			}
			if (null == result) {
				return new ExtReturn(false, "审核结果不能为空！");
			}
			PayOrder old = payOrderService.queryById(payOrder.getPoid());
			if (null != old) {
				if (1 != old.getStatus()) {
					return new ExtReturn(false, "只能审核状态为“待财务审核”的存款订单！");
				}
			}
			String date = DateUtil2.format2(new Date());
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(key, c);
			// 账务审核
			old.setCwremarks(payOrder.getCwremarks());
			old.setCwopttime(date);
			old.setCwid(systemUser.getUserId());
			old.setCwname(systemUser.getAccount());
			old.setStatus(result);
			old.setUpdateDate(date);

			if (payOrderService.isMoneyLocked(old.getUiid())) {
				return new ExtReturn(false, "该会员已被锁定，请确认未锁定后再试！");
			}
			BigDecimal amount = old.getAmount();
			boolean flag = false;
			try {
				flag = payOrderService.audit(old);
			} catch (Exception ex) {
				flag = false;
			}
			if (flag) {
				if (result == 3) {// 审核通过的时候
					if (payOrder.getOrdertype() == 7) { // 救援金
						PayOrderLog log = new PayOrderLog();
						log.setUiid(payOrder.getUiid());
						log.setOrderid(payOrder.getPoid());
						log.setAmount(payOrder.getAmount());
						log.setType(2);
						log.setWalletlog(payOrder.getBeforebalance() + ">>>" + payOrder.getLaterbalance());
						log.setRemark(payOrder.getCwremarks());
						log.setCreatetime(payOrder.getUpdateDate());
						payOrderService.insertPayLog(log);
					}
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("uiid", old.getUiid());
					AccountMoney accountMoney = accountMoneyService.getMoneyInfo(params);
					BigDecimal laterbalance = accountMoney.getTotalamount();
					// 充值0.5%手续费
					if (old.getPaymethods() == 0 && old.getStatus() == 3) {
						if (StringUtils.isNotEmpty(old.getBankname()) && (old.getBankname().indexOf("微信") == -1
								&& old.getOpenname().indexOf("微信") == -1 && old.getBankname().indexOf("支付宝") == -1
								&& old.getOpenname().indexOf("支付宝") == -1)) {
							BigDecimal sxf = old.getAmount().multiply(new BigDecimal(0.005)).setScale(2,
									BigDecimal.ROUND_HALF_UP);
							date = DateUtil2.format2(new Date());
							PayOrder payOrders = new PayOrder();
							payOrders.setPoid(IdGenerator.genOrdId16("IN"));
							payOrders.setUiid(old.getUiid());
							payOrders.setPaytyple(2);
							payOrders.setOrdertype(5);
							payOrders.setPaymethods(0);
							payOrders.setDeposittime(date);
							payOrders.setAmount(sxf);
							payOrders.setStatus(3);
							payOrders.setCwremarks("优惠:充值0.5%手续费");
							payOrders.setCwopttime(date);
							payOrders.setCwid(systemUser.getUserId());
							payOrders.setCwname(systemUser.getAccount());
							payOrders.setUpdateDate(date);
							payOrders.setUaccount(old.getUaccount());
							payOrders.setUrealname(old.getUrealname());

							laterbalance = accountMoney.getTotalamount().add(sxf);
							payOrders.setBeforebalance(accountMoney.getTotalamount() + "");
							payOrders.setLaterbalance(laterbalance + "");
							if (payOrderService.savePayOrder(payOrders)) {
								accountMoney.setTotalamount(sxf);
								accountMoneyService.updateTotalamount(accountMoney);
							}

							// 新增帐变记录。
							PayOrderLog log = new PayOrderLog();
							log.setUiid(payOrders.getUiid());
							log.setOrderid(payOrders.getPoid());
							log.setAmount(payOrders.getAmount());
							log.setType(2);
							log.setWalletlog(payOrders.getBeforebalance() + ">>>" + payOrders.getLaterbalance());
							log.setRemark("充值0.5%手续费");
							log.setCreatetime(payOrders.getUpdateDate());
							payOrderService.insertPayLog(log);
						}
					}
					/**
					 * 订单类型 1:优惠金额 2:活动彩金(需要根据此字段验证用户是否已领取彩金) 4:首存优惠 5:充值05%手续费
					 */
					Activity activity = null;
					/**
					 * 是否可洗码 0：不给洗码 1：给洗码
					 */
					int isxima = 1;
					// 校验用户参加了优惠活动
					if (null != old.getHdnumber() && !"".equals(old.getHdnumber())) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("hdnumber", old.getHdnumber());
						activity = payOrderService.getObject(map);
						// 校验活动存在并且为正常开放状态并且参加此活动后任然可以洗码
						// 条件成立设置用户为可洗码
						if (null != activity && activity.getStatus() == 1) {
							if (activity.getIsxima() == 1) {
								isxima = 1;
							} else {
								isxima = 0;
							}
						}
					}
					BigDecimal activityMoney = BigDecimal.ZERO;// 活动金额
					/**
					 * 活动金额类型 0：正常订单 1:优惠金额 2：活动彩金 3：洗码金额 4：首存优惠
					 */
					int activityType = 0;
					PayOrder hdRecord = null;
					if (null != activity) {// 添加活动金额
						// 充值金额大于或等于活动的最小金额
						if (amount.intValue() >= Double.valueOf(activity.getMinmoney())) {
							if ("1".equals(activity.getHdtype())) {// 首存、2、3、4、5存活动
								activityMoney = new BigDecimal(activity.getRewmoney());
								activityType = 1;
							}
							if ("2".equals(activity.getHdtype())) {// 次次存活动
								activityMoney = amount.multiply(new BigDecimal(activity.getHdscale()));
								// 验证优惠的金额是否大于活动最大优惠金额
								if (activityMoney.setScale(2).doubleValue() > Double.valueOf(activity.getMaxmoney())) {
									// 将活动金额赠送为最大金额
									activityMoney = new BigDecimal(activity.getMaxmoney());
								}
								activityType = 1;
							}
							if ("3".equals(activity.getHdtype())) {// 首存二存
								activityMoney = amount.multiply(new BigDecimal(activity.getHdscale()));
								// 验证优惠的金额是否大于活动最大优惠金额
								if (activityMoney.setScale(2).doubleValue() > Double.valueOf(activity.getMaxmoney())) {
									// 将活动金额赠送为最大金额
									activityMoney = new BigDecimal(activity.getMaxmoney());
								}
								activityType = 1;
							}

							if ("5".equals(activity.getHdtype()) || "10".equals(activity.getHdtype())) {// 笔笔存、真人存送活动
								activityMoney = amount.multiply(new BigDecimal(activity.getHdscale()));
								// 验证优惠的金额是否大于活动最大优惠金额
								if (activityMoney.setScale(2).doubleValue() > Double.valueOf(activity.getMaxmoney())) {
									// 将活动金额赠送为最大金额
									activityMoney = new BigDecimal(activity.getMaxmoney());
								}
								activityType = 1;
							}
							if ("8".equals(activity.getHdtype()) || "9".equals(activity.getHdtype())) {// 存200送50、存500送100
								activityMoney = new BigDecimal(activity.getMaxmoney());
								activityType = 1;
							}
						}
						// 添加一条加款订单用于记录用户参加活动赠送
						if (activityMoney.doubleValue() > 0) {
							date = DateUtil2.format2(new Date());
							hdRecord = old;
							String serialID = IdGenerator.genOrdId16("REWARD");
							hdRecord.setPoid(serialID);
							hdRecord.setPaytyple(2);
							hdRecord.setDeposittime(date);
							hdRecord.setAmount(activityMoney);
							hdRecord.setPaystatus(0);
							hdRecord.setStatus(3);
							hdRecord.setOrdertype(activityType);
							hdRecord.setCreateDate(date);
							hdRecord.setUpdateDate(date);
							hdRecord.setKfremarks(
									activity.getHdtext() + "->充值金额：" + amount + "->优惠金额:" + activityMoney);
							hdRecord.setCwremarks(
									activity.getHdtext() + "->充值金额：" + amount + "->优惠金额:" + activityMoney);
							hdRecord.setBeforebalance(laterbalance + "");
							BigDecimal laterbalance2 = laterbalance.add(activityMoney);
							hdRecord.setLaterbalance(String.valueOf(laterbalance2));
							payOrderService.savePayOrder(hdRecord);
							Map<String, Object> maps = new HashMap<String, Object>();
							maps.put("uiid", old.getUiid());
							AccountMoney accountMoney2 = accountMoneyService.getMoneyInfo(maps);
							accountMoney2.setTotalamount(activityMoney.setScale(2, BigDecimal.ROUND_HALF_UP));
							accountMoneyService.updateTotalamount(accountMoney2);

							ActivityFlag activityFlag = new ActivityFlag();
							activityFlag.setType(Integer.valueOf(activity.getHdtype()));
							activityFlag.setFlagtime(DateUtil.getStrByDate(new Date(), "yyyy-MM-dd"));
							activityFlag.setHms(DateUtil.getStrByDate(new Date(), "HH:mm:ss"));
							activityFlag.setUiid(old.getUiid());
							activityFlag.setAcid(activity.getAid());
							activityFlag.setAcgroup(activity.getAcgroup());
							payOrderService.saveActivityFlag(activityFlag);

							// 新增帐变记录。
							PayOrderLog log = new PayOrderLog();
							log.setUiid(hdRecord.getUiid());
							log.setOrderid(hdRecord.getPoid());
							log.setAmount(hdRecord.getAmount());
							log.setType(2);
							log.setWalletlog(hdRecord.getBeforebalance() + ">>>" + hdRecord.getLaterbalance());
							log.setRemark(hdRecord.getCwremarks());
							log.setCreatetime(hdRecord.getUpdateDate());
							payOrderService.insertPayLog(log);
						}
					}
					/* 设置用户是否可洗码 */
					XimaFlag ximaflag = ximaFlagService.getNewestXimaFlag(old.getUiid());
					if (ximaflag == null || ximaflag.getIsxima() != isxima) {
						ximaflag = new XimaFlag();
						ximaflag.setFlaguiid(old.getUiid());
						ximaflag.setFlagaccount(old.getUaccount());
						ximaflag.setIsxima(isxima);
						ximaflag.setUpdatetime(DateUtil2.format2(new Date()));
						if (hdRecord != null) {
							ximaflag.setRemark(hdRecord.getCwremarks());
						} else {
							ximaflag.setRemark("未参加任何活动");
						}
						ximaFlagService.save(ximaflag);
					}
					// 将用户设置为维护客户
					Map<String, Object> mp = new HashMap<String, Object>();
					mp.put("uiid", payOrder.getUiid());
					UserManager u = userManagerService.getUManager(mp);
					if (null != u) {
						if (u.getClienttype() != 1) {
							u.setClienttype(1);
							userManagerService.update(u);
						}
					}
				}
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
	 * 根据当前已有抽奖次数和充值金额计算出需添加的抽奖次数
	 * 
	 * @param currTimes
	 *            当前次数
	 * @param amount
	 *            充值金额
	 * @return
	 */
	private Integer getTimesByAmount(int currTimes, BigDecimal amount) {
		if (currTimes == 0) {// 今日无抽奖次数
			int a = amount.divide(new BigDecimal(200)).intValue();
			if (a <= 2) {
				return a;
			} else {
				int b = amount.subtract(new BigDecimal(400)).divide(new BigDecimal(500)).intValue();
				if (b == 0) {
					return 2;
				} else if (b < 8) {
					return 2 + b;
				} else {
					return 10;
				}
			}
		} else if (currTimes == 1) {
			int a = amount.divide(new BigDecimal(200)).intValue();
			if (a <= 1) {
				return a;
			} else {
				int b = amount.subtract(new BigDecimal(200)).divide(new BigDecimal(500)).intValue();
				if (b == 0) {
					return 1;
				} else if (b < 8) {
					return 1 + b;
				} else {
					return 9;
				}
			}
		} else {
			int b = amount.divide(new BigDecimal(500)).intValue();
			if (b + currTimes > 10) {
				return 10 - currTimes;
			}
			return b;
		}
	}

	// ------------------------------------------------------------------------------------
	// 在线支付：存款订单（第三方支付）
	// ------------------------------------------------------------------------------------

	@RequestMapping(value = "/depositorderOL/indexOL")
	public String indexOL(@RequestParam(value = "id", required = false) String id, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		// status 状态 0 作废 1 发起（待财务审核） 2 处理中（待客服核实） 3 存取成功 4 存取失败
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
		// 公司银行卡
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", 0); // '银行卡状态 0未锁定 1锁定',
		params.put("sortColumns", "ccid asc");
		List<CompanyCard> ccList = companyCardService.queryCompanyCard(params, null, null);
		if (null != ccList && ccList.size() > 0) {
			JSONObject ccnoMap = new JSONObject();
			for (CompanyCard cc : ccList) {
				ccnoMap.put(cc.getCcno() + "::" + cc.getBankname() + "::" + cc.getOpenbank() + "::" + cc.getCcholder(),
						"[" + cc.getBankname() + "] " + cc.getCcno());
			}
			request.setAttribute("ccnoMap", ccnoMap.toString());
		}
		// 是否为客服、账务
		{
			int isKF = 0; // 0否，1是
			int isCW = 0; // 0否，1是
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(key, c);
			if (null != systemUser) {
				List<SystemRole> roleList = systemService.queryRoleByUserId(systemUser.getUserId());
				for (SystemRole sr : roleList) {
					if (-1 != sr.getRoleName().indexOf("客服")) {
						isKF = 1;
					} else if (-1 != sr.getRoleName().indexOf("财务")) {
						isCW = 1;
					} else if (-1 != sr.getRoleName().indexOf("管理员")) {
						isKF = 1;
						isCW = 1;
					}
				}
			}
			request.setAttribute("isKF", isKF);
			request.setAttribute("isCW", isCW);
		}
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("", "全部");
		jsonObj.put("1", "提交充值");
		jsonObj.put("3", "充值成功");
		jsonObj.put("4", "充值失败");
		request.setAttribute("paystatus", jsonObj.toString());

		jsonObj = new JSONObject();
		jsonObj.put("", "全部");
		jsonObj.put("MBO", "摩宝微信");
		jsonObj.put("TWO", "通汇扫码");
		jsonObj.put("BFO", "宝付支付");
		jsonObj.put("THO", "通汇支付");
		jsonObj.put("JFP", "捷付WAP");
		jsonObj.put("FBO", "捷付支付宝");

		request.setAttribute("platMap", jsonObj.toString());
		return "manage/payorder/depositorderOL";
	}

	@RequestMapping(value = "/depositorderOL/queryDepositorderOL")
	public @ResponseBody Object queryDepositorderOL(@RequestParam(value = "uaccount", required = false) String uaccount,
			@RequestParam(value = "bankcard", required = false) String bankcard,
			@RequestParam(value = "bankname", required = false) String bankname,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "amount", required = false) Integer amount,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "platformorders", required = false) String platformorders,
			@RequestParam(value = "platname", required = false) String platname,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + WebConstants.FRAMEWORK_USER;
		Class<Object> c = null;
		SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(key, c);
		boolean manage = false;
		;
		if (null != systemUser) {
			List<SystemRole> roleList = systemService.queryRoleByUserId(systemUser.getUserId());
			for (SystemRole sr : roleList) {
				if (-1 != sr.getRoleName().indexOf("合作伙伴")) {
					manage = true;
					break;
				}
			}
		}
		if (manage) {
			JSONObject jsonMap = JSONObject.fromObject(SystemFieldsCache.fields.get("proxy.ids"));
			String s = jsonMap.toString();
			s = s.substring(2, s.indexOf(":") - 1);
			params.put("manageIds", s);
		}
		if (StringUtils.isNotBlank(uaccount)) {
			params.put("uaccount", uaccount);
		}
		if (StringUtils.isNotBlank(bankcard)) {
			params.put("bankcard", bankcard);
		}
		if (StringUtils.isNotBlank(bankname)) {
			params.put("bankname", bankname);
		}
		if (null != amount) {
			params.put("amount", amount);
		}
		if (null != status) {
			params.put("status", status);
		}
		if (null != startDate) {
			params.put("startDate", startDate);
		}
		if (null != endDate) {
			params.put("endDate", endDate);
		}
		if (StringUtils.isNotEmpty(platformorders)) {
			params.put("platformorders", platformorders);
		}
		if (StringUtils.isNotEmpty(platname)) {
			params.put("platname", platname);
		}
		params.put("paytyple", 0);
		params.put("paymethods", 1);
		params.put("sortColumns", "deposittime desc");
		Long count = payOrderService.queryPayOrderCount(params);
		List<PayOrder> list = payOrderService.queryPayOrder(params, startNo, pageSize);
		return new GridPanel(count, list, true);
	}

	/**
	 * 补单
	 * 
	 * @param poid
	 *            订单ID
	 * @return
	 */
	@RequestMapping("/depositorderOL/makeOrder/{poid}")
	@ResponseBody
	public Object makeOrder(@PathVariable String poid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(poid))) {
				return new ExtReturn(false, "存款订单主键不能为空！");
			}
			PayOrder old = payOrderService.queryById(poid);
			if (null != old) {
				if (3 == old.getStatus()) {
					return new ExtReturn(false, "当前订单状态已为“存款成功”！");
				}
			}
			if (payOrderService.makePayOrder(poid)) {
				XimaFlag ximaflag = ximaFlagService.getNewestXimaFlag(old.getUiid());
				int isXima = 1;
				if (StringUtils.isNotEmpty(old.getHdnumber())
						&& (StringUtils.isNotEmpty(old.getCwremarks()) && old.getCwremarks().indexOf("次次") == -1)) {
					isXima = 0;
				}
				if (ximaflag == null || ximaflag.getIsxima() != isXima) {
					ximaflag = new XimaFlag();
					ximaflag.setFlaguiid(old.getUiid());
					ximaflag.setFlagaccount(old.getUaccount());
					ximaflag.setIsxima(isXima);
					ximaflag.setRemark(old.getCwremarks());
					ximaFlagService.save(ximaflag);
				}
				return new ExtReturn(true, "补单成功！");
			} else {
				return new ExtReturn(false, "补单失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 在线支付报表导出
	 * 
	 * @param request
	 *            请求对象
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @param response
	 *            响应对象
	 * @throws Exception
	 */
	@RequestMapping(value = "/depositorderOL/toDownloadReport")
	@ResponseBody
	public void depositorderOLToDownloadData(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "uaccount", required = false) String uaccount,
			@RequestParam(value = "bankcard", required = false) String bankcard,
			@RequestParam(value = "bankname", required = false) String bankname,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "platname", required = false) String platname,
			@RequestParam(value = "platformorders", required = false) String platformorders,
			@RequestParam(value = "amount", required = false) Integer amount,
			@RequestParam(value = "status", required = false) Integer status) throws Exception {
		OutputStream fOut = null;
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if (StringUtils.isNotBlank(uaccount)) {
				params.put("uaccount", uaccount);
			}
			if (StringUtils.isNotBlank(bankcard)) {
				params.put("bankcard", bankcard);
			}
			if (StringUtils.isNotBlank(bankname)) {
				params.put("bankname", bankname);
			}
			if (null != amount) {
				params.put("amount", amount);
			}
			if (null != status) {
				params.put("status", status);
			}
			if (null != startDate) {
				params.put("startDate", startDate);
			}
			if (null != endDate) {
				params.put("endDate", endDate);
			}
			if (StringUtils.isNotEmpty(platformorders)) {
				params.put("platformorders", platformorders);
			}
			if (StringUtils.isNotEmpty(platname)) {
				params.put("platname", platname);
			}
			params.put("paytyple", 0);
			params.put("paymethods", 1);
			params.put("sortColumns", "deposittime desc");
			// --------------------
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");// 设置类型
			String fileName = "depositorderOL.xls";
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			// 数据源
			List<PayOrder> payOrderList = payOrderService.queryPayOrder(params);
			for (PayOrder payOrder : payOrderList) {
				if (null != payOrder.getPaystatus()) { // 第三方状态
					if (payOrder.getPaystatus() == 0) {
						payOrder.setPaystatusname("已接受");
					} else if (payOrder.getPaystatus() == 1) {
						payOrder.setPaystatusname("处理中");
					} else if (payOrder.getPaystatus() == 2) {
						payOrder.setPaystatusname("处理成功");
					} else if (payOrder.getPaystatus() == 3) {
						payOrder.setPaystatusname("处理失败");
					}
				}
				if (null != payOrder.getStatus()) { // 当前状态
					if (payOrder.getStatus() == 0) {
						payOrder.setStatusname("作废");
					} else if (payOrder.getStatus() == 1) {
						payOrder.setStatusname("发起");
					} else if (payOrder.getStatus() == 2) {
						payOrder.setStatusname("处理中");
					} else if (payOrder.getStatus() == 3) {
						payOrder.setStatusname("成功");
					} else if (payOrder.getStatus() == 4) {
						payOrder.setStatusname("失败");
					}
				}
				if (null != payOrder.getPaymethods()) { // 付款方式
					if (payOrder.getPaymethods() == 0) {
						payOrder.setPaymethodsname("公司入款");
					} else if (payOrder.getPaymethods() == 1) {
						payOrder.setPaymethodsname("第三方支付");
					}
				}
			}
			// excel表头
			LinkedHashMap<String, String> viewMap = new LinkedHashMap<String, String>();
			viewMap.put("poid", "存款订单ID");
			viewMap.put("platformorders", "交易流水号");
			viewMap.put("paystatusname", "第三方状态");
			viewMap.put("statusname", "当前状态");
			viewMap.put("uaccount", "会员账号");
			viewMap.put("urealname", "会员姓名");
			viewMap.put("amount", "存款金额(元)");
			viewMap.put("paymethodsname", "存款方式");
			viewMap.put("deposittime", "存款时间");
			viewMap.put("createDate", "创建时间");
			viewMap.put("updateDate", "更新时间");
			Workbook workBook = ExcelUtil.generateSingleWorkBook(fileName.toString(), payOrderList, viewMap);
			// 以流的形式下载文件=
			fOut = response.getOutputStream();
			workBook.write(fOut);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fOut != null) {
				fOut.flush();
				fOut.close();
			}
		}
	}

	// ------------------------------------------------------------------------------------
	// 提款订单
	// ------------------------------------------------------------------------------------
	@RequestMapping(value = "/pickuporder/index")
	public String indexPickup(@RequestParam(value = "id", required = false) String id, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		// status 状态 0 作废 1 发起（待财务审核） 2 处理中 3 存取成功 4 存取失败
		JSONObject map = new JSONObject();
		map.put("", "全部");
		map.put("0", "作废");
		map.put("1", "发起");
		map.put("2", "待财务审核");
		map.put("3", "成功");
		map.put("4", "失败");
		request.setAttribute("statusMap", map.toString());
		// 存提款方式 0公司入款，1第三方支付
		JSONObject paymethodsMap = new JSONObject();
		paymethodsMap.put("0", "公司入款");
		paymethodsMap.put("1", "第三方支付");
		request.setAttribute("paymethodsMap", paymethodsMap.toString());
		// 是否为客服、账务
		{
			int isKF = 0; // 0否，1是
			int isCW = 0; // 0否，1是
			int isFK = 0; // 0否，1是
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(key, c);
			if (null != systemUser) {
				List<SystemRole> roleList = systemService.queryRoleByUserId(systemUser.getUserId());
				for (SystemRole sr : roleList) {
					if (-1 != sr.getRoleName().indexOf("客服")) {
						isKF = 1;
					} else if (-1 != sr.getRoleName().indexOf("财务")) {
						isCW = 1;
						isFK = 1;
					} else if (-1 != sr.getRoleName().indexOf("管理员")) {
						isKF = 1;
						isCW = 1;
						isFK = 1;
					} else if (-1 != sr.getRoleName().indexOf("风控")) {
						isFK = 1;
						isCW = 1;
					}
				}
			}
			request.setAttribute("isKF", isKF);
			request.setAttribute("isCW", isCW);
			request.setAttribute("isFK", isFK);
		}
		return "manage/payorder/pickuporder";
	}

	/**
	 * 提款订单报表导出
	 * 
	 * @param request
	 *            请求对象
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @param response
	 *            响应对象
	 * @throws Exception
	 */
	@RequestMapping(value = "/pickuporder/toDownloadReport")
	@ResponseBody
	public void pickuporderToDownloadData(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "uaccount", required = false) String uaccount,
			@RequestParam(value = "bankcard", required = false) String bankcard,
			@RequestParam(value = "bankname", required = false) String bankname,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "amount", required = false) Integer amount,
			@RequestParam(value = "status", required = false) Integer status) throws Exception {
		OutputStream fOut = null;
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if (StringUtils.isNotBlank(uaccount)) {
				params.put("uaccount", uaccount);
			}
			if (StringUtils.isNotBlank(bankcard)) {
				params.put("bankcard", bankcard);
			}
			if (StringUtils.isNotBlank(bankname)) {
				params.put("bankname", bankname);
			}
			if (null != amount) {
				params.put("amount", amount);
			}
			if (null != status) {
				params.put("status", status);
			}
			if (null != startDate) {
				params.put("startDate", startDate);
			}
			if (null != endDate) {
				params.put("endDate", endDate);
			}
			params.put("paytyple", 1);
			params.put("sortColumns", "deposittime desc");
			// ----------------------------------------------------
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");// 设置类型
			String fileName = "pickuporder.xls";
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			// 数据源
			List<PayOrder> payOrderList = payOrderService.queryPayOrder(params);
			for (PayOrder payOrder : payOrderList) {
				if (null != payOrder.getPaystatus()) { // 第三方状态
					if (payOrder.getPaystatus() == 0) {
						payOrder.setPaystatusname("已接受");
					} else if (payOrder.getPaystatus() == 1) {
						payOrder.setPaystatusname("处理中");
					} else if (payOrder.getPaystatus() == 2) {
						payOrder.setPaystatusname("处理成功");
					} else if (payOrder.getPaystatus() == 3) {
						payOrder.setPaystatusname("处理失败");
					}
				}
				if (null != payOrder.getStatus()) { // 当前状态
					if (payOrder.getStatus() == 0) {
						payOrder.setStatusname("作废");
					} else if (payOrder.getStatus() == 1) {
						payOrder.setStatusname("发起");
					} else if (payOrder.getStatus() == 2) {
						payOrder.setStatusname("待财务审核");
					} else if (payOrder.getStatus() == 3) {
						payOrder.setStatusname("成功");
					} else if (payOrder.getStatus() == 4) {
						payOrder.setStatusname("失败");
					}
				}
				if (null != payOrder.getPaymethods()) { // 付款方式
					if (payOrder.getPaymethods() == 0) {
						payOrder.setPaymethodsname("公司入款");
					} else if (payOrder.getPaymethods() == 1) {
						payOrder.setPaymethodsname("第三方支付");
					}
				}
			}
			// excel表头
			LinkedHashMap<String, String> viewMap = new LinkedHashMap<String, String>();
			viewMap.put("poid", "提款订单ID");
			viewMap.put("statusname", "当前状态");
			viewMap.put("uaccount", "会员账号");
			viewMap.put("urealname", "会员名称");
			viewMap.put("proxyname", "所属代理");
			viewMap.put("bankname", "银行名称");
			viewMap.put("bankcard", "存入银行卡");
			viewMap.put("deposit", "卡户行名称");
			viewMap.put("openname", "开户人");
			viewMap.put("amount", "提款金额(元)");
			viewMap.put("paymethodsname", "提款方式");
			viewMap.put("deposittime", "存款时间");
			viewMap.put("kfremarks", "风控备注");
			viewMap.put("kfname", "操作风控");
			viewMap.put("kfopttime", "风控操作时间");
			viewMap.put("cwremarks", "财务备注");
			viewMap.put("cwname", "操作财务");
			viewMap.put("cwopttime", "财务操作时间");
			Workbook workBook = ExcelUtil.generateSingleWorkBook(fileName.toString(), payOrderList, viewMap);
			// 以流的形式下载文件=
			fOut = response.getOutputStream();
			workBook.write(fOut);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fOut != null) {
				fOut.flush();
				fOut.close();
			}
		}
	}

	@RequestMapping(value = "/pickuporder/queryPickuporder")
	public @ResponseBody Object queryPickuporder(@RequestParam(value = "uaccount", required = false) String uaccount,
			@RequestParam(value = "bankcard", required = false) String bankcard,
			@RequestParam(value = "bankname", required = false) String bankname,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "amount", required = false) Integer amount,
			@RequestParam(value = "status", required = false) Integer status,

	@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + WebConstants.FRAMEWORK_USER;
		Class<Object> c = null;
		SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(key, c);
		boolean manage = false;
		;
		if (null != systemUser) {
			List<SystemRole> roleList = systemService.queryRoleByUserId(systemUser.getUserId());
			for (SystemRole sr : roleList) {
				if (-1 != sr.getRoleName().indexOf("合作伙伴")) {
					manage = true;
					break;
				}
			}
		}
		if (manage) {
			JSONObject jsonMap = JSONObject.fromObject(SystemFieldsCache.fields.get("proxy.ids"));
			String s = jsonMap.toString();
			s = s.substring(2, s.indexOf(":") - 1);
			params.put("manageIds", s);
		}
		if (StringUtils.isNotBlank(uaccount)) {
			params.put("uaccount", uaccount);
		}
		if (StringUtils.isNotBlank(bankcard)) {
			params.put("bankcard", bankcard);
		}
		if (StringUtils.isNotBlank(bankname)) {
			params.put("bankname", bankname);
		}
		if (null != amount) {
			params.put("amount", amount);
		}
		if (null != status) {
			params.put("status", status);
		}
		if (null != startDate) {
			params.put("startDate", startDate);
		}
		if (null != endDate) {
			params.put("endDate", endDate);
		}
		params.put("paytyple", 1);
		params.put("sortColumns", "deposittime desc");
		Long count = payOrderService.queryPayOrderCount(params);
		List<PayOrder> list = payOrderService.queryPayOrder(params, startNo, pageSize);
		return new GridPanel(count, list, true);
	}

	@RequestMapping(value = "/pickuporder/save")
	@ResponseBody
	public Object savePickup(@ModelAttribute PayOrder payOrder, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String date = DateUtil2.format2(new Date());
			if (!StringUtils.isNotBlank(ObjectUtils.toString(payOrder))) {
				return new ExtReturn(false, "提款订单不能为空！");
			}
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(key, c);

			if (StringUtils.isNotBlank(ObjectUtils.toString(payOrder.getPoid()))) {
				PayOrder old = payOrderService.queryById(payOrder.getPoid());
				// 正常修改
				if (null != old) {
					if (1 == old.getStatus()) {
						return new ExtReturn(false, "状态为“待财务审核”的提款订单不能再修改！");
					} else if (3 == old.getStatus()) {
						return new ExtReturn(false, "状态为“提款成功”的提款订单不能再修改！");
					}
					payOrder.setCwid(old.getCwid());
					payOrder.setCwname(old.getCwname());
					payOrder.setCwopttime(old.getCwopttime());
					payOrder.setCwremarks(old.getCwremarks());
				}
				// 状态为“存款失败”和“作废”的存款订单，在修改后，由客服重新提交给账务审核
				payOrder.setKfid(systemUser.getUserId());
				payOrder.setKfname(systemUser.getAccount());
				payOrder.setKfopttime(date);
				payOrder.setStatus(1);

				if (payOrderService.updatePayOrder(payOrder)) {
					return new ExtReturn(true, "提交成功！");
				} else {
					return new ExtReturn(false, "提交失败！");
				}
			} else {
				// 客服新建提款订单
				// 会员账号检验
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("account", payOrder.getUaccount());
				List<UserInfo> userList = userInfoService.queryUserinfo(params, null, null);
				if (null == userList || userList.size() == 0) {
					return new ExtReturn(false, "不存在[" + payOrder.getUaccount() + "]对应会员，请填写有效的会员账号！");
				}
				payOrder.setUiid(userList.get(0).getUiid());
				payOrder.setUrealname(userList.get(0).getUname());
				// 卡号检验
				CardPackage cp = userInfoService.queryCardPackage(payOrder.getUiid(), 1);
				if (null == cp) {
					return new ExtReturn(false, "该会员[" + payOrder.getUaccount() + "]还没绑定银行卡，请确认绑定后后再操作！");
				}
				payOrder.setBankcard(cp.getCardnumber());
				payOrder.setBankname(cp.getBankname());
				payOrder.setDeposit(cp.getOpeningbank());
				payOrder.setOpenname(cp.getAccountname());

				payOrder.setPoid(IdGenerator.genOrdId16("OUT"));
				payOrder.setPlatformorders(IdGenerator.genOrdId16(""));
				payOrder.setPaytyple(1); // 0存款，1提款，2赠送，3扣款
				payOrder.setKfopttime(date);
				payOrder.setKfid(systemUser.getUserId());
				payOrder.setKfname(systemUser.getAccount());
				payOrder.setStatus(1);
				payOrder.setCreateDate(date);
				payOrder.setDeposittime(date);
				String success = payOrderService.savePickupOrder(payOrder);
				if (!StringUtils.isNotBlank(success)) {
					return new ExtReturn(false, "提款订单提交失败。");
				}
				if (success.equals("-1")) {
					return new ExtReturn(false, "非提款订单。");
				}
				if (success.equals("-2")) {
					return new ExtReturn(false, "会员钱包余额不足。");
				}
				return new ExtReturn(true, "提交成功待财务审核。");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping("/pickuporder/del/{poid}")
	@ResponseBody
	public Object delPickup(@PathVariable String poid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(poid))) {
				return new ExtReturn(false, "提款订单主键不能为空！");
			}
			PayOrder old = payOrderService.queryById(poid);
			if (null != old) {
				if (1 == old.getStatus()) {
					return new ExtReturn(false, "不能删除状态为“待客服核实”的提款订单！");
				} else if (2 == old.getStatus()) {
					return new ExtReturn(false, "不能删除状态为“待财务审核”的提款订单！");
				} else if (3 == old.getStatus()) {
					return new ExtReturn(false, "不能删除状态为“提款成功”的提款订单！");
				}
			}
			if (payOrderService.deletePayOrder(poid)) {
				return new ExtReturn(true, "删除成功！");
			} else {
				return new ExtReturn(false, "删除失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping("/pickuporder/auditKf/{result}")
	@ResponseBody
	public Object auditKfPickup(@PathVariable Integer result, @ModelAttribute PayOrder payOrder,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(payOrder))) {
				return new ExtReturn(false, "提款订单主键不能为空！");
			}
			if (null == result) {
				return new ExtReturn(false, "审核结果不能为空！");
			}
			PayOrder old = payOrderService.queryById(payOrder.getPoid());
			if (null != old) {
				if (2 != old.getStatus()) {
					return new ExtReturn(false, "只能审核状态为“待财务审核”的提款订单！");
				}
			}
			String date = DateUtil2.format2(new Date());
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(key, c);
			// 客服核实
			old.setKfremarks(payOrder.getKfremarks());
			old.setKfopttime(date);
			old.setKfid(systemUser.getUserId());
			old.setKfname(systemUser.getAccount());
			old.setStatus(result);
			old.setUpdateDate(date);

			if (payOrderService.updatePayOrder(payOrder)) {
				return new ExtReturn(true, "提交成功！");
			} else {
				return new ExtReturn(false, "提交失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping("/pickuporder/auditFK/{result}")
	@ResponseBody
	public Object auditFKPickup(@PathVariable Integer result, @ModelAttribute PayOrder payOrder,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(payOrder))) {
				return new ExtReturn(false, "提款订单主键不能为空！");
			}
			if (null == result) {
				return new ExtReturn(false, "审核结果不能为空！");
			}
			PayOrder old = payOrderService.queryById(payOrder.getPoid());
			if (null != old) {
				if (1 != old.getStatus()) {
					return new ExtReturn(false, "只能审核状态为“发起”的提款订单！");
				}
			}
			String date = DateUtil2.format2(new Date());
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(key, c);
			// 账务审核
			old.setKfremarks(payOrder.getKfremarks());
			old.setKfopttime(date);
			old.setKfid(systemUser.getUserId());
			old.setKfname(systemUser.getAccount());
			old.setStatus(result);
			old.setUpdateDate(date);
			boolean flag = true;
			if (result == 4) {// 提款审核失败
				WithdrawalcountLog w = new WithdrawalcountLog();
				w.setUiid(old.getUiid());
				w.setDaytime(DateUtil2.format(new Date()));
				w.setWithdrawalcount(1);
				payOrderService.updateWithdrawalcount(w);
				try {
					flag = payOrderService.audit(old);
				} catch (Exception ex) {
					flag = false;
				}
			}
			payOrderService.updatePayOrder(old);
			if (flag) {
				return new ExtReturn(true, "操作成功！");
			} else {
				return new ExtReturn(true, "操作失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping("/pickuporder/auditCw/{result}")
	@ResponseBody
	public Object auditCwPickup(@PathVariable Integer result, @ModelAttribute PayOrder payOrder,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(payOrder))) {
				return new ExtReturn(false, "提款订单主键不能为空！");
			}
			if (null == result) {
				return new ExtReturn(false, "审核结果不能为空！");
			}
			PayOrder old = payOrderService.queryById(payOrder.getPoid());
			if (null != old) {
				if (2 != old.getStatus()) {
					return new ExtReturn(false, "只能审核状态为“待财务审核”的提款订单！");
				}
			}
			String date = DateUtil2.format2(new Date());
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(key, c);
			// 账务审核
			if (result == 4) {
				old.setCwremarks(payOrder.getCwremarks());
			} else {
				old.setCwremarks("已支付");
			}
			old.setCwopttime(date);
			old.setCwid(systemUser.getUserId());
			old.setCwname(systemUser.getAccount());
			old.setStatus(result);
			old.setUpdateDate(date);

			boolean flag = false;
			try {
				flag = payOrderService.audit(old);
			} catch (Exception ex) {
				flag = false;
			}
			if (flag) {
				return new ExtReturn(true, "操作成功！");
			} else {
				return new ExtReturn(true, "操作失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 订单详情展示
	 */
	@RequestMapping(value = "/pickuporder/auditCk/{poid}")
	public String showOrderDetail(@PathVariable String poid, HttpServletRequest request, HttpServletResponse response) {
		PayOrder payOrder = payOrderService.queryById(poid);
		request.setAttribute("payOrder", payOrder);
		return "manage/payorder/pickupDetail";
	}

	/**
	 * 会员出款
	 * 
	 * @param poid
	 * @param vcode
	 * @return
	 */
	@RequestMapping("/pickuporder/customerWithdrawl")
	@ResponseBody
	public String customerWithdrawl(@RequestParam(value = "poid", required = false) String poid,
			@RequestParam(value = "vcode", required = false) String vcode, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		json.put("result", true);
		try {
			JSONObject jsonMap = JSONObject.fromObject(SystemFieldsCache.fields.get("anma"));
			String s = jsonMap.toString();
			s = s.substring(2, s.indexOf(":") - 1);
			if (!s.equals(vcode)) {
				json.put("result", false);
				json.put("msg", "出款口令错误！");
				return json.toString();
			}
			PayOrder payOrder = payOrderService.queryById(poid);
			if (payOrder == null) {
				json.put("result", false);
				json.put("msg", "订单不存在！");
				return json.toString();
			}
			if (payOrder.getStatus() == 3) {
				json.put("result", false);
				json.put("msg", "该订单状态已经是成功！");
				return json.toString();
			}
			GstWithdrawal th = new GstWithdrawal();
			th.setAmount(payOrder.getAmount().toString());
			th.setBankAccount(payOrder.getOpenname().trim());
			th.setBankCardNo(payOrder.getBankcard().trim());
			th.setBankCode(payOrder.getBankname().trim());
			th.setOrderNo(payOrder.getPoid().substring(3));
			String result = th.remit();
			JSONObject data = JSONObject.fromObject(result);
			if (data.getBoolean("is_success")) {
				String date = DateUtil2.format2(new Date());
				String vuid = CookieUtil.getOrCreateVuid(request, response);
				String key = vuid + WebConstants.FRAMEWORK_USER;
				Class<Object> c = null;
				SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(key, c);
				// 账务审核
				payOrder.setCwremarks("自动出款成功");
				payOrder.setCwopttime(date);
				payOrder.setCwid(systemUser.getUserId());
				payOrder.setCwname(systemUser.getAccount());
				payOrder.setStatus(3);
				payOrder.setUpdateDate(date);
				boolean flag = false;
				try {
					flag = payOrderService.audit(payOrder);
				} catch (Exception ex) {
					flag = false;
				}
				if (flag) {
					json.put("msg", "出款成功！");
					return json.toString();
				} else {
					json.put("result", false);
					json.put("msg", "第三方出款成功，修改钱宝订单状态失败！");
					return json.toString();
				}
			} else {
				json.put("result", false);
				json.put("msg", "调用接口出款失败！");
			}
		} catch (Exception e) {
			logger.error("出款异常。", e);
			json.put("result", false);
			json.put("msg", "出款失败！");
		}
		return json.toString();
	}

	/**
	 * 查询余额
	 * 
	 * @param poid
	 * @param vcode
	 * @return
	 */
	@RequestMapping("/pickuporder/getWithdrawlBalance")
	@ResponseBody
	public String getWithdrawlBalance(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		json.put("result", true);
		try {
			String result = new GstWithdrawal().queryBlance();
			json.put("balance", JSONObject.fromObject(result).getString("money"));
		} catch (Exception e) {
			logger.error("查询第三方余额异常。", e);
			json.put("result", false);
			json.put("msg", "网络异常，请稍后重试！");
			json.put("balance", 0);
		}
		return json.toString();
	}

	// ------------------------------------------------------------------------------------
	// 余额变更记录
	// ------------------------------------------------------------------------------------

	@RequestMapping(value = "/rporder/queryRporder")
	public @ResponseBody Object queryRporder(@RequestParam(value = "uiid", required = false) Long uiid,
			@RequestParam(value = "uaccount", required = false) String uaccount,
			@RequestParam(value = "bankcard", required = false) String bankcard,
			@RequestParam(value = "bankname", required = false) String bankname,
			@RequestParam(value = "amount", required = false) Integer amount,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "ordertype", required = false) String ordertype,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (null != uiid) {
			params.put("uiid", uiid);
		}
		if (StringUtils.isNotBlank(uaccount)) {
			params.put("uaccount", uaccount);
		}
		if (StringUtils.isNotBlank(bankcard)) {
			params.put("bankcard", bankcard);
		}
		if (StringUtils.isNotBlank(bankname)) {
			params.put("bankname", bankname);
		}
		if (null != amount) {
			params.put("amount", amount);
		}
		if (null != status) {
			params.put("status", status);
		}
		if (null != ordertype) {
			params.put("ordertype", ordertype);
		}
		params.put("sortColumns", "deposittime desc");
		Long count = payOrderService.queryPayOrderRPCount(params);
		List<PayOrder> list = payOrderService.queryPayOrderRP(params, startNo == null ? 0 : startNo,
				pageSize == null ? 30 : pageSize);
		return new GridPanel(count, list, true);
	}

	// ------------------------------------------------------------------------------------
	// 赠送订单
	// ------------------------------------------------------------------------------------
	@RequestMapping(value = "/rewordorder/index")
	public String indexReword(@RequestParam(value = "id", required = false) String id, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		// status 状态 0 作废 1 发起（待财务审核） 2 处理中 3 存取成功 4 存取失败
		JSONObject map = new JSONObject();
		map.put("", "全部");
		map.put("0", "作废");
		map.put("1", "发起");
		map.put("2", "处理中");
		map.put("3", "成功");
		map.put("4", "失败");
		request.setAttribute("statusMap", map.toString());

		JSONObject jsonMap = JSONObject.fromObject(SystemFieldsCache.fields.get("ordertype"));
		jsonMap.put("", "全部");
		request.setAttribute("ordertypeMap", jsonMap.toString());
		// 存提款方式 0公司入款，1第三方支付
		JSONObject paymethodsMap = new JSONObject();
		paymethodsMap.put("0", "公司入款");
		paymethodsMap.put("1", "第三方支付");
		request.setAttribute("paymethodsMap", paymethodsMap.toString());
		// 是否为客服、账务
		{
			int isKF = 0; // 0否，1是
			int isCW = 0; // 0否，1是
			int isFK = 0; // 0否，1是
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(key, c);
			if (null != systemUser) {
				List<SystemRole> roleList = systemService.queryRoleByUserId(systemUser.getUserId());
				for (SystemRole sr : roleList) {
					if (-1 != sr.getRoleName().indexOf("客服")) {
						isKF = 1;
					} else if (-1 != sr.getRoleName().indexOf("财务")) {
						isCW = 1;
					} else if (-1 != sr.getRoleName().indexOf("管理员")) {
						isKF = 1;
						isCW = 1;
						isFK = 1;
					} else if (-1 != sr.getRoleName().indexOf("风控")) {
						isFK = 1;
					}
				}
			}
			request.setAttribute("isKF", isKF);
			request.setAttribute("isCW", isCW);
			request.setAttribute("isFK", isFK);
		}
		return "manage/payorder/rewordorder";
	}

	/**
	 * 加款订单报表导出
	 * 
	 * @param request
	 *            请求对象
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @param response
	 *            响应对象
	 * @throws Exception
	 */
	@RequestMapping(value = "/rewordorder/toDownloadReport")
	@ResponseBody
	public void rewordorderToDownloadData(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "uaccount", required = false) String uaccount,
			@RequestParam(value = "bankcard", required = false) String bankcard,
			@RequestParam(value = "bankname", required = false) String bankname,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "amount", required = false) Integer amount,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "orderType", required = false) String orderType) throws Exception {
		OutputStream fOut = null;
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if (StringUtils.isNotBlank(uaccount)) {
				params.put("uaccount", uaccount);
			}
			if (StringUtils.isNotBlank(bankcard)) {
				params.put("bankcard", bankcard);
			}
			if (StringUtils.isNotBlank(bankname)) {
				params.put("bankname", bankname);
			}
			if (null != amount) {
				params.put("amount", amount);
			}
			if (null != status) {
				params.put("status", status);
			}
			if (null != startDate) {
				params.put("startDate", startDate);
			}
			if (null != endDate) {
				params.put("endDate", endDate);
			}
			if (StringUtils.isNotEmpty(orderType)) {
				params.put("ordertype", orderType);
			}
			params.put("paytyple", 2);
			params.put("sortColumns", "deposittime desc");
			// ----------------------------------------------------
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");// 设置类型
			String fileName = "rewordorder.xls";
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			// 数据源
			List<PayOrder> payOrderList = payOrderService.queryPayOrder(params);
			// Map<String, String> map = iSystemService.qeuryAllFields();
			Map<String, String> map = SystemFieldsCache.fields;
			for (PayOrder payOrder : payOrderList) {
				if (null != payOrder.getOrdertype()) { // 订单类型
					String ordertypeList = map.get("ordertype");
					String[] ordertypeLists = ordertypeList.substring(1, ordertypeList.length() - 1).split(",");
					for (String string : ordertypeLists) {
						String[] ordername = string.split(":");
						if (payOrder.getOrdertype().toString()
								.equals((ordername[0].substring(1, ordername[0].length() - 1)))) {
							payOrder.setOrdertypename(ordername[1].substring(1, ordername[1].length() - 1));
						}
					}
				}
				if (null != payOrder.getStatus()) { // 当前状态
					if (payOrder.getStatus() == 0) {
						payOrder.setStatusname("作废");
					} else if (payOrder.getStatus() == 1) {
						payOrder.setStatusname("发起");
					} else if (payOrder.getStatus() == 2) {
						payOrder.setStatusname("处理中");
					} else if (payOrder.getStatus() == 3) {
						payOrder.setStatusname("成功");
					} else if (payOrder.getStatus() == 4) {
						payOrder.setStatusname("失败");
					}
				}
			}
			// excel表头
			LinkedHashMap<String, String> viewMap = new LinkedHashMap<String, String>();
			viewMap.put("poid", "赠送订单ID");
			viewMap.put("ordertypename", "订单类型");
			viewMap.put("amount", "赠送金额(元)");
			viewMap.put("statusname", "当前状态");
			viewMap.put("uaccount", "会员账号");
			viewMap.put("urealname", "会员姓名");
			viewMap.put("deposittime", "赠送时间");
			viewMap.put("kfremarks", "客服备注");
			viewMap.put("kfname", "操作客服");
			viewMap.put("kfopttime", "客服操作时间");
			viewMap.put("cwremarks", "财务备注");
			viewMap.put("cwname", "操作财务");
			viewMap.put("cwopttime", "财务操作时间");
			Workbook workBook = ExcelUtil.generateSingleWorkBook(fileName.toString(), payOrderList, viewMap);
			// 以流的形式下载文件=
			fOut = response.getOutputStream();
			workBook.write(fOut);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fOut != null) {
				fOut.flush();
				fOut.close();
			}
		}

	}

	@RequestMapping(value = "/rewordorder/queryRewordorder")
	public @ResponseBody Object queryRewordorder(@RequestParam(value = "uaccount", required = false) String uaccount,
			@RequestParam(value = "bankcard", required = false) String bankcard,
			@RequestParam(value = "bankname", required = false) String bankname,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "amount", required = false) Integer amount,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "orderType", required = false) String orderType,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + WebConstants.FRAMEWORK_USER;
		Class<Object> c = null;
		SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(key, c);
		boolean manage = false;
		;
		if (null != systemUser) {
			List<SystemRole> roleList = systemService.queryRoleByUserId(systemUser.getUserId());
			for (SystemRole sr : roleList) {
				if (-1 != sr.getRoleName().indexOf("合作伙伴")) {
					manage = true;
					break;
				}
			}
		}
		if (manage) {
			JSONObject jsonMap = JSONObject.fromObject(SystemFieldsCache.fields.get("proxy.ids"));
			String s = jsonMap.toString();
			s = s.substring(2, s.indexOf(":") - 1);
			params.put("manageIds", s);
		}
		if (StringUtils.isNotBlank(uaccount)) {
			params.put("uaccount", uaccount);
		}
		if (StringUtils.isNotBlank(bankcard)) {
			params.put("bankcard", bankcard);
		}
		if (StringUtils.isNotBlank(bankname)) {
			params.put("bankname", bankname);
		}
		if (null != amount) {
			params.put("amount", amount);
		}
		if (null != status) {
			params.put("status", status);
		}
		if (null != startDate) {
			params.put("startDate", startDate);
		}
		if (null != endDate) {
			params.put("endDate", endDate);
		}
		if (StringUtils.isNotEmpty(orderType)) {
			params.put("ordertype", orderType);
		}
		params.put("paytyple", 2);
		params.put("sortColumns", "deposittime desc");
		Long count = payOrderService.queryPayOrderCount(params);
		List<PayOrder> list = payOrderService.queryPayOrder(params, startNo, pageSize);
		return new GridPanel(count, list, true);
	}

	@RequestMapping("/rewordorder/auditCw/{result}")
	@ResponseBody
	public Object auditCwReword(@PathVariable Integer result, @ModelAttribute PayOrder payOrder,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(payOrder))) {
				return new ExtReturn(false, "赠送订单主键不能为空！");
			}
			if (null == result) {
				return new ExtReturn(false, "审核结果不能为空！");
			}
			PayOrder old = payOrderService.queryById(payOrder.getPoid());
			if (null != old) {
				if (1 != old.getStatus()) {
					return new ExtReturn(false, "只能审核状态为“待财务审核”的赠送订单！");
				}
			}

			if (old.getOrdertype() == 10 && result == 3) { // 每日首存60%优惠
				Map<String, Object> map = new HashMap<>();
				map.put("uiid", old.getUiid());
				map.put("acid", 7);
				map.put("flagtime", DateUtil.FormatDate(new Date()));
				ActivityFlag activityFlag = activityService.getActivityFlag(map);
				if (activityFlag != null) {
					return new ExtReturn(false, "今日首存60%已参加，不能重复参加！");
				}
				// 新增
				activityFlag = new ActivityFlag();
				activityFlag.setType(3);
				activityFlag.setFlagtime(DateUtil.getStrByDate(new Date(), "yyyy-MM-dd"));
				activityFlag.setHms(DateUtil.getStrByDate(new Date(), "HH:mm:ss"));
				activityFlag.setUiid(old.getUiid());
				activityFlag.setAcid(7);
				activityFlag.setAcgroup("1");
				payOrderService.saveActivityFlag(activityFlag);
			}

			if (old.getOrdertype() == 4 && result == 3) { // 首存100%优惠
				Map<String, Object> map = new HashMap<>();
				map.put("uiid", old.getUiid());
				map.put("acid", 3);
				ActivityFlag activityFlag = activityService.getActivityFlag(map);
				if (activityFlag != null) {
					return new ExtReturn(false, "首存100%已参加，不能重复参加！");
				}
				// 新增
				activityFlag = new ActivityFlag();
				activityFlag.setType(3);
				activityFlag.setFlagtime(DateUtil.getStrByDate(new Date(), "yyyy-MM-dd"));
				activityFlag.setHms(DateUtil.getStrByDate(new Date(), "HH:mm:ss"));
				activityFlag.setUiid(old.getUiid());
				activityFlag.setAcid(3);
				activityFlag.setAcgroup("1");
				payOrderService.saveActivityFlag(activityFlag);
			}

			String date = DateUtil2.format2(new Date());
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(key, c);
			// 账务审核
			old.setCwremarks(payOrder.getCwremarks());
			old.setCwopttime(date);
			old.setCwid(systemUser.getUserId());
			old.setCwname(systemUser.getAccount());
			old.setStatus(result);
			old.setUpdateDate(date);

			boolean flag = false;
			try {
				flag = payOrderService.audit(old);
			} catch (Exception ex) {
				flag = false;
			}
			if (flag && result == 3) {
				/**
				 * 订单类型 1:优惠金额 2:8-88元免费彩金 3:洗码金额 4:首存100%优惠 6:存50送58 7:救援金优惠
				 * 8:可洗码优惠 9:送100送88 10:日首存60% 11:存200送50 12:存500送100
				 */
				if (old.getOrdertype() == 1 || old.getOrdertype() == 2 || old.getOrdertype() == 4
						|| old.getOrdertype() == 6 || old.getOrdertype() == 7 || old.getOrdertype() == 9
						|| old.getOrdertype() == 10 || old.getOrdertype() == 11 || old.getOrdertype() == 12) {
					XimaFlag ximaflag = ximaFlagService.getNewestXimaFlag(old.getUiid());
					if (StringUtils.isNotEmpty(old.getCwremarks()) && old.getCwremarks().indexOf("次次") != -1) {
						if (ximaflag == null || ximaflag.getIsxima() != 1) {
							ximaflag = new XimaFlag();
							ximaflag.setFlaguiid(old.getUiid());
							ximaflag.setFlagaccount(old.getUaccount());
							ximaflag.setIsxima(1);
							ximaflag.setRemark(old.getCwremarks());
							ximaFlagService.save(ximaflag);
						}
					} else {
						if (ximaflag == null || ximaflag.getIsxima() != 0) {
							ximaflag = new XimaFlag();
							ximaflag.setFlaguiid(old.getUiid());
							ximaflag.setFlagaccount(old.getUaccount());
							ximaflag.setIsxima(0);
							ximaflag.setRemark(old.getCwremarks());
							ximaFlagService.save(ximaflag);
						}
					}
				} else if (old.getOrdertype() == 8 || old.getOrdertype() == 0 || old.getOrdertype() == 3) { // 正常订单、可洗码优惠、洗码
					XimaFlag ximaflag = ximaFlagService.getNewestXimaFlag(old.getUiid());
					if (ximaflag == null || ximaflag.getIsxima() != 1) {
						ximaflag = new XimaFlag();
						ximaflag.setFlaguiid(old.getUiid());
						ximaflag.setFlagaccount(old.getUaccount());
						ximaflag.setIsxima(1);
						ximaflag.setRemark(old.getCwremarks());
						ximaFlagService.save(ximaflag);
					}
				}
				return new ExtReturn(true, "操作成功！");
			} else {
				return new ExtReturn(true, "操作失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	// ------------------------------------------------------------------------------------
	// 扣款订单
	// ------------------------------------------------------------------------------------

	@RequestMapping(value = "/punishorder/index")
	public String indexPunish(@RequestParam(value = "id", required = false) String id, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		// status 状态 0 作废 1 发起（待财务审核） 2 处理中 3 存取成功 4 存取失败
		JSONObject map = new JSONObject();
		map.put("0", "作废");
		map.put("1", "待财务审核");
		map.put("3", "成功");
		map.put("4", "失败");
		request.setAttribute("statusMap", map.toString());
		// 存提款方式 0公司入款，1第三方支付
		JSONObject paymethodsMap = new JSONObject();
		paymethodsMap.put("0", "公司入款");
		paymethodsMap.put("1", "第三方支付");
		request.setAttribute("paymethodsMap", paymethodsMap.toString());
		// 是否为客服、账务
		{
			int isKF = 0; // 0否，1是
			int isCW = 0; // 0否，1是
			int isFK = 0; // 0否，1是
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(key, c);
			if (null != systemUser) {
				List<SystemRole> roleList = systemService.queryRoleByUserId(systemUser.getUserId());
				for (SystemRole sr : roleList) {
					if (-1 != sr.getRoleName().indexOf("客服")) {
						isKF = 1;
					} else if (-1 != sr.getRoleName().indexOf("财务")) {
						isCW = 1;
					} else if (-1 != sr.getRoleName().indexOf("管理员")) {
						isKF = 1;
						isCW = 1;
						isFK = 1;
					} else if (-1 != sr.getRoleName().indexOf("风控")) {
						isFK = 1;
					}
				}
			}
			request.setAttribute("isKF", isKF);
			request.setAttribute("isCW", isCW);
			request.setAttribute("isFK", isFK);
		}
		return "manage/payorder/punishorder";
	}

	@RequestMapping(value = "/punishorder/queryPunishorder")
	public @ResponseBody Object queryPunishorder(@RequestParam(value = "uaccount", required = false) String uaccount,
			@RequestParam(value = "bankcard", required = false) String bankcard,
			@RequestParam(value = "bankname", required = false) String bankname,
			@RequestParam(value = "amount", required = false) Integer amount,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + WebConstants.FRAMEWORK_USER;
		Class<Object> c = null;
		SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(key, c);
		boolean manage = false;
		;
		if (null != systemUser) {
			List<SystemRole> roleList = systemService.queryRoleByUserId(systemUser.getUserId());
			for (SystemRole sr : roleList) {
				if (-1 != sr.getRoleName().indexOf("合作伙伴")) {
					manage = true;
					break;
				}
			}
		}
		if (manage) {
			JSONObject jsonMap = JSONObject.fromObject(SystemFieldsCache.fields.get("proxy.ids"));
			String s = jsonMap.toString();
			s = s.substring(2, s.indexOf(":") - 1);
			params.put("manageIds", s);
		}
		if (StringUtils.isNotBlank(uaccount)) {
			params.put("uaccount", uaccount);
		}
		if (StringUtils.isNotBlank(bankcard)) {
			params.put("bankcard", bankcard);
		}
		if (StringUtils.isNotBlank(bankname)) {
			params.put("bankname", bankname);
		}
		if (null != amount) {
			params.put("amount", amount);
		}
		if (null != status) {
			params.put("status", status);
		}
		params.put("paytyple", 3);
		params.put("sortColumns", "deposittime desc");
		Long count = payOrderService.queryPayOrderCount(params);
		List<PayOrder> list = payOrderService.queryPayOrder(params, startNo, pageSize);
		return new GridPanel(count, list, true);
	}

	@RequestMapping("/punishorder/auditCw/{result}")
	@ResponseBody
	public Object auditCwPunish(@PathVariable Integer result, @ModelAttribute PayOrder payOrder,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(payOrder))) {
				return new ExtReturn(false, "扣款订单主键不能为空！");
			}
			if (null == result) {
				return new ExtReturn(false, "审核结果不能为空！");
			}
			PayOrder old = payOrderService.queryById(payOrder.getPoid());
			if (null != old) {
				if (1 != old.getStatus()) {
					return new ExtReturn(false, "只能审核状态为“待财务审核”的扣款订单！");
				}
			}
			String date = DateUtil2.format2(new Date());
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(key, c);
			// 账务审核
			old.setCwremarks(payOrder.getCwremarks());
			old.setCwopttime(date);
			old.setCwid(systemUser.getUserId());
			old.setCwname(systemUser.getAccount());
			old.setStatus(result);
			old.setUpdateDate(date);

			boolean flag = false;
			try {
				flag = payOrderService.audit(old);
			} catch (Exception ex) {
				flag = false;
			}
			if (flag) {
				return new ExtReturn(true, "操作成功！");
			} else {
				return new ExtReturn(true, "操作失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
}
