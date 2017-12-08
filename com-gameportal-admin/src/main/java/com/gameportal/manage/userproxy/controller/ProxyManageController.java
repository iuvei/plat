package com.gameportal.manage.userproxy.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.manage.betlog.model.BetLog;
import com.gameportal.manage.betlog.model.BetLogTotal;
import com.gameportal.manage.betlog.service.IBetLogService;
import com.gameportal.manage.member.model.MemberInfo;
import com.gameportal.manage.member.model.MemberInfoReport;
import com.gameportal.manage.member.model.MemberInfoReportTotal;
import com.gameportal.manage.member.model.UserXimaSet;
import com.gameportal.manage.member.service.IMemberInfoService;
import com.gameportal.manage.member.service.IUserXimaSetService;
import com.gameportal.manage.pay.model.PayOrder;
import com.gameportal.manage.pay.service.IPayOrderService;
import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.proxy.model.ProxyClearingLog;
import com.gameportal.manage.proxy.model.ProxyClearingLogTotal;
import com.gameportal.manage.proxy.model.ProxySet;
import com.gameportal.manage.proxy.model.ProxyUserXimaLog;
import com.gameportal.manage.proxy.service.IProxyClearingService;
import com.gameportal.manage.proxy.service.IProxySetService;
import com.gameportal.manage.proxy.service.IProxyUserXimaService;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.userproxy.service.IProxyManageService;
import com.gameportal.manage.util.CookieUtil;
import com.gameportal.manage.util.DateUtil2;
import com.gameportal.manage.util.WebConstants;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/proxymanage/m")
public class ProxyManageController {

	
	private static final Logger logger = Logger.getLogger(ProxyIndexManageController.class);
	
	@Resource(name = "betLogServiceImpl")
	private IBetLogService betLogService = null;
	
	@Resource(name = "proxyManageService")
	private IProxyManageService proxyManageService;

	@Resource(name="proxyClearingService")
	private IProxyClearingService proxyClearingService;
	
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService = null;
	
	@Resource(name = "payOrderServiceImpl")
	private IPayOrderService payOrderService;
	
	@Resource(name = "userXimaSetService")
	private IUserXimaSetService userXimaSetService;
	
	@Resource(name = "memberInfoServiceImpl")
	private IMemberInfoService memberInfoService = null;
	
	@Resource(name = "proxySetService")
	private IProxySetService proxySetService;
	
	@Resource(name="proxyUserXimaService")
	private IProxyUserXimaService proxyUserXimaService;
	
	@RequestMapping(value = "/downuserindex")
	public String indexcount(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		String today = DateUtil2.convert2Str(new Date(), "yyyy-MM-dd")+" 23:59:59";
		request.setAttribute("today", today);
		return "manage/userproxy/downuser";
	}
	
	@RequestMapping(value = "/queryDownuser")
	public @ResponseBody
	Object queryDownuser(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "start", required = false) Integer thisPage,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String,Object> params=new HashMap<String,Object>();
			if(!StringUtils.isBlank(account)){
				params.put("account", account);
			}
			if(!StringUtils.isBlank(startDate)){
				params.put("startDate", startDate);
			}else{
				String startt =   DateUtil2.getFirstDay(DateUtil2.format(new Date()));
				params.put("startDate", startt+" 00:00:00");
			}
			if(!StringUtils.isBlank(endDate)){
				params.put("endDate",endDate);
			}else{
				String endt = DateUtil2.format(new Date());
				params.put("endDate", endt+" 23:59:59");
			}
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
			Class<Object> c = null;
			MemberInfo memberInfo = (MemberInfo) iRedisService.getRedisResult(key, c);
			if(null != memberInfo){
				params.put("uiid", memberInfo.getUiid());
			}
			Long count = proxyManageService.selectProxyDownUserCount(params);
			List<BetLogTotal> list = proxyManageService.selectProxyDownUser(params, thisPage, pageSize);
			return new GridPanel(count, list, true);
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
		
	}
	
	@RequestMapping(value = "/queryUserBet")
	public @ResponseBody
	Object queryUserBetLog(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "start", required = false) Integer thisPage,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String,Object> map=new HashMap<String,Object>();
		    if(!StringUtils.isBlank(account)){
		    	if(!StringUtils.isBlank(startDate)){
		    		map.put("startDate", startDate);
				}else{
					String startt =   DateUtil2.getFirstDay(DateUtil2.format(new Date()));
					map.put("startDate", startt+" 00:00:00");
				}
				if(!StringUtils.isBlank(endDate)){
					map.put("endDate", endDate);
				}else{
					String endt = DateUtil2.format(new Date());
					map.put("endDate", endt+" 23:59:59");
				}
			    map.put("account", account);
			    map.put("sortColumns", "betdate DESC");
			    Long count = betLogService.queryForCount(map);
				List<BetLog> list = betLogService.queryForList(map, thisPage, pageSize);
				return new GridPanel(count, list, true);
		    }
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
		return null;
	}
	
	/**-----------------------结算记录------------------**/
	
	@RequestMapping(value = "/clearingindex")
	public String clearingindex(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		return "manage/userproxy/proxyclearinglog";
	}
	
	/**
	 * 查询所有洗码日志记录
	 * @param id
	 * @param thisPage
	 * @param pageSize
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryClearinglog")
	public @ResponseBody
	Object queryProxyInfo(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "start", required = false) Integer thisPage,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(!StringUtils.isBlank(startDate)){
				params.put("startDate", startDate +" 00:00:00");
			}
			if(!StringUtils.isBlank(endDate)){
				params.put("endDate", endDate + "23:59:59");
			}
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
			Class<Object> c = null;
			MemberInfo memberInfo = (MemberInfo) iRedisService.getRedisResult(key, c);
			params.put("uiid", memberInfo.getUiid());
			params.put("sortColumns", "g.clearingTime DESC");
			Long count = proxyClearingService.count(params);
			List<ProxyClearingLog> list = proxyClearingService.getList(params, thisPage, pageSize);
			ProxyClearingLogTotal sumMap=proxyClearingService.clearMoneyTotal(params); //总计
			ProxyClearingLog proxylog=new ProxyClearingLog();
			proxylog.setAccount("总计:");
			proxylog.setClearingAmount(sumMap.getClearingAmounts());
			proxylog.setFinalamountTotal(sumMap.getFinalamountTotals());
			proxylog.setValidBetAmountTotal(sumMap.getValidBetAmountTotals());
			proxylog.setXimaAmount(sumMap.getXimaAmounts());
			proxylog.setPreferentialTotal(sumMap.getPreferentialTotals());
			proxylog.setRealPL(sumMap.getRealPLs());
			proxylog.setClearingStartTime("");
			proxylog.setClearingEndTime("");
			proxylog.setClearingType(-1);
			proxylog.setClearingStatus(-1);
			proxylog.setClearingTime("");
			list.add(proxylog);
			return new GridPanel(count, list, true);
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
		
	}
	
	/**---------提款记录---------**/
	
	@RequestMapping(value = "/payindex")
	public String payindex(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		return "manage/userproxy/payorderlog";
	}
	
	@RequestMapping(value = "/queryPayOrderlog")
	public @ResponseBody
	Object queryPayOrderlog(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "uaccount", required = false) String uaccount,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "start", required = false) Integer thisPage,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(!StringUtils.isBlank(startDate)){
				params.put("startDate", startDate +" 00:00:00");
			}
			if(!StringUtils.isBlank(endDate)){
				params.put("endDate", endDate + "23:59:59");
			}
			if(!StringUtils.isBlank(uaccount)){
				params.put("uaccount", uaccount);
			}
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
			Class<Object> c = null;
			MemberInfo memberInfo = (MemberInfo) iRedisService.getRedisResult(key, c);
			params.put("puiid", memberInfo.getUiid());
			params.put("status", 3);
			params.put("paytyple", "('1')");
			params.put("sortColumns", "p.deposittime DESC");
			Long count = payOrderService.selectProxyPayOrderLogCount(params);
			List<PayOrder> list = payOrderService.selectProxyPayOrderLog(params, thisPage, pageSize);
			String amounts=payOrderService.selectProxyPayOrderTotal(params);
			PayOrder pay=new PayOrder();
			pay.setUaccount("总计:");
			pay.setAmount(new BigDecimal(amounts).setScale(2, BigDecimal.ROUND_HALF_UP));
			pay.setPaymethods(-1);
			pay.setPaytyple(-1);
			pay.setStatus(-1);
			list.add(pay);
			return new GridPanel(count, list, true);
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	/*--存款*/
	
	@RequestMapping(value = "/payDepositindex")
	public String payDepositindex(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		JSONObject map = new JSONObject();
		map.put("", "全部");
		map.put("0", "公司入款");
		map.put("1", "在线支付");
		
		request.setAttribute("paymethodsMap", map.toString());
		return "manage/userproxy/payorderdepositlog";
	}
	
	@RequestMapping(value = "/queryDepositPayOrderlog")
	public @ResponseBody
	Object queryDepositPayOrderlog(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "uaccount", required = false) String uaccount,
			@RequestParam(value = "paymethods", required = false) String paymethods,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "start", required = false) Integer thisPage,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(!StringUtils.isBlank(startDate)){
				params.put("startDate", startDate +" 00:00:00");
			}
			if(!StringUtils.isBlank(endDate)){
				params.put("endDate", endDate + "23:59:59");
			}
			if(!StringUtils.isBlank(uaccount)){
				params.put("uaccount", uaccount);
			}
			if(StringUtils.isNotEmpty(paymethods)){
				params.put("paymethods", paymethods);
			}
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
			Class<Object> c = null;
			MemberInfo memberInfo = (MemberInfo) iRedisService.getRedisResult(key, c);
			params.put("puiid", memberInfo.getUiid());
			params.put("status", 3);
			params.put("paytyple", "('0','2')");
			params.put("sortColumns", "p.deposittime desc");
			Long count = payOrderService.selectProxyPayOrderLogCount(params);
			List<PayOrder> list = payOrderService.selectProxyPayOrderLog(params, thisPage, pageSize);
			String amounts=payOrderService.selectProxyPayOrderTotal(params);
			PayOrder pay=new PayOrder();
			pay.setUaccount("总计:");
			pay.setAmount(new BigDecimal(amounts).setScale(2, BigDecimal.ROUND_HALF_UP));
			pay.setPaymethods(-1);
			pay.setPaytyple(-1);
			pay.setStatus(-1);
			list.add(pay);
			return new GridPanel(count, list, true);
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	/**---------扣款记录---------**/
	
	@RequestMapping(value = "/punishindex")
	public String punishindex(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		return "manage/userproxy/punishorderlog";
	}
	
	@RequestMapping(value = "/queryPunishOrderlog")
	public @ResponseBody
	Object queryPunishOrderlog(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "uaccount", required = false) String uaccount,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "start", required = false) Integer thisPage,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(!StringUtils.isBlank(startDate)){
				params.put("startDate", startDate +" 00:00:00");
			}
			if(!StringUtils.isBlank(endDate)){
				params.put("endDate", endDate + "23:59:59");
			}
			if(!StringUtils.isBlank(uaccount)){
				params.put("uaccount", uaccount);
			}
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
			Class<Object> c = null;
			MemberInfo memberInfo = (MemberInfo) iRedisService.getRedisResult(key, c);
			params.put("puiid", memberInfo.getUiid());
			params.put("status", 3);
			params.put("paytyple", "('3')");
			params.put("sortColumns", "p.deposittime DESC");
			Long count = payOrderService.selectProxyPayOrderLogCount(params);
			List<PayOrder> list = payOrderService.selectProxyPayOrderLog(params, thisPage, pageSize);
			String amounts=payOrderService.selectProxyPayOrderTotal(params);
			PayOrder pay=new PayOrder();
			pay.setUaccount("总计:");
			pay.setAmount(new BigDecimal(amounts).setScale(2, BigDecimal.ROUND_HALF_UP));
			pay.setPaymethods(-1);
			pay.setPaytyple(-1);
			pay.setStatus(-1);
			list.add(pay);
			return new GridPanel(count, list, true);
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	
	@RequestMapping(value = "/setUserXima")
	public @ResponseBody
	Object setUserXima(
			@RequestParam(value = "uiid", required = false) Long uiid,
			@RequestParam(value = "ximascale", required = false) String ximascale,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			if(!StringUtils.isNotBlank(ximascale)){
				return new ExtReturn(false, "请输入您要分配给用户的洗码比例。");
			} 
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
			Class<Object> c = null;
			MemberInfo memberInfo = (MemberInfo) iRedisService.getRedisResult(key, c);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("uiid", memberInfo.getUiid());
			ProxySet proxyobj = proxySetService.queryObject(params);
			if(proxyobj == null){
				return new ExtReturn(false, "编辑用户洗码比例失败错误代码:500。");
			}
			if(Double.valueOf(ximascale) > Double.valueOf(proxyobj.getXimascale())){
				return new ExtReturn(false, "您当前洗码比例为："+proxyobj.getXimascale()+",分配给下线洗码比例不能大于您当前的洗码比例。");
			}
			params.clear();
			params.put("uiid", uiid);
			params.put("proxyid", memberInfo.getUiid());
			params.put("status", "1");
			UserXimaSet uxinaSet = userXimaSetService.getObject(params);
			if(uxinaSet !=null){
				uxinaSet.setXimascale(ximascale);
				uxinaSet.setSettime(DateUtil2.format2(new Date()));
				if(userXimaSetService.update(uxinaSet)){
					return new ExtReturn(true, "编辑用户洗码比例成功。");
				}else{
					return new ExtReturn(false, "编辑用户洗码比例失败。");
				}
			}else{
				uxinaSet = new UserXimaSet();
				uxinaSet.setUiid(uiid.intValue());
				uxinaSet.setStatus(1);
				uxinaSet.setProxyid(memberInfo.getUiid().intValue());
				uxinaSet.setXimascale(ximascale);
				uxinaSet.setSettime(DateUtil2.format2(new Date()));
				if(userXimaSetService.save(uxinaSet)){
					return new ExtReturn(true, "保存用户洗码比例成功。");
				}else{
					return new ExtReturn(false, "保存用户洗码比例失败。");
				}
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	/**
	 * 洗码结算
	 * @param uiid
	 * @param ximascale
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/ximajs")
	public @ResponseBody
	Object ximajs(
			@RequestParam(value = "clearingid", required = false) Long clearingid,
			@RequestParam(value = "jsdate", required = false) String jsdate,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("clearingid", clearingid);
			if(StringUtils.isNotBlank(jsdate)){
				params.put("jsdate", jsdate.subSequence(0, 10));
			}
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
			Class<Object> c = null;
			MemberInfo memberInfo = (MemberInfo) iRedisService.getRedisResult(key, c);
			params.put("puiid", memberInfo.getUiid());
			String code = proxyManageService.ximajs(params);
			if(code.equals("0")){
				return new ExtReturn(true, "自助洗码成功，我们会尽快为您入账。");
			}else if(code.equals("400")){
				return new ExtReturn(false, "您要洗码的数据不存在或该数据不是可洗码状态，请刷新页面后重试。");
			}else if(code.equals("404")){
				return new ExtReturn(false, "没有找到可分配的下线用户。");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
		return null;
	}
	
	@RequestMapping(value = "/xmindex")
	public String xmindex(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		return "manage/userproxy/proxyuserxmlog";
	}
	
	@RequestMapping(value = "/queryProxyuserxm")
	public @ResponseBody
	Object queryProxyuserxm(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "start", required = false) Integer thisPage,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(StringUtils.isNotBlank(account)){
				params.put("account", account);
			}
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
			Class<Object> c = null;
			MemberInfo memberInfo = (MemberInfo) iRedisService.getRedisResult(key, c);
			params.put("puiid", memberInfo.getUiid());
			params.put("sortColumns", "xm.ximatime DESC");
			Long count = proxyUserXimaService.getCount(params);
			List<ProxyUserXimaLog> list = proxyUserXimaService.getList(params, thisPage, pageSize);
			return new GridPanel(count, list, true);
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	/**
	 * 下线报表
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/reportindex")
	public String reportindex(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		return "manage/userproxy/reportuser";
	}
	
	@RequestMapping(value = "/queryReport")
	public @ResponseBody
	Object queryReport(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "start", required = false) Integer thisPage,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			String date = DateUtil2.format(new Date());
			if(StringUtils.isNotBlank(account)){
				params.put("account", account);
			}
			if(StringUtils.isNotBlank(startDate)){
				params.put("startDate", startDate);
			}else{
				params.put("startDate", DateUtil2.getFirstDay(date) + " 00:00:00");
			}
			if(StringUtils.isNotBlank(endDate)){
				params.put("endDate", endDate);
			}else{
				params.put("endDate", DateUtil2.getEndDay(date) + " 23:59:59");
			}
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
			Class<Object> c = null;
			MemberInfo memberInfo = (MemberInfo) iRedisService.getRedisResult(key, c);
			params.put("puiid", memberInfo.getUiid());
			params.put("sortColumns", "winLossTotal=0,winLossTotal desc");
			Long count = memberInfoService.queryMemberInfoReportCount(params);
			List<MemberInfoReport> list = memberInfoService.queryMemberReport(params, thisPage, pageSize);
			MemberInfoReportTotal memberTotal=memberInfoService.queryMemberReportTotal(params);
			MemberInfoReport memberInfoReport=new MemberInfoReport();
			memberInfoReport.setAccount("总计：");
			memberInfoReport.setMoney(memberTotal.getMoneyTotal());
			memberInfoReport.setDepositTotal(memberTotal.getDepositTotal());
			memberInfoReport.setWithdrawalTotal(memberTotal.getWithdrawalTotal());
			memberInfoReport.setPreferentialTotal(memberTotal.getPreferentialTotal());
			memberInfoReport.setXimaTotal(memberTotal.getXimaTotal());
			memberInfoReport.setWinLossTotal(memberTotal.getWinLossTotal());
			list.add(memberInfoReport);
			return new GridPanel(count, list, true);
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
}
