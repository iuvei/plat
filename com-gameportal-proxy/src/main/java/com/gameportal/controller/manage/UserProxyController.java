package com.gameportal.controller.manage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.gameportal.controller.BaseAction;
import com.gameportal.controller.ResponseContext;
import com.gameportal.domain.AccountMoney;
import com.gameportal.domain.BetLog;
import com.gameportal.domain.BetLogTotal;
import com.gameportal.domain.DownUserReportTotal;
import com.gameportal.domain.GameMoney;
import com.gameportal.domain.GamePlatform;
import com.gameportal.domain.MemberInfo;
import com.gameportal.domain.MemberInfoReport;
import com.gameportal.domain.Page;
import com.gameportal.domain.PageData;
import com.gameportal.domain.PayOrder;
import com.gameportal.domain.ProxyClearingLog;
import com.gameportal.domain.ProxyReportEntity;
import com.gameportal.domain.ProxySet;
import com.gameportal.domain.ProxyTransferLog;
import com.gameportal.domain.ProxyUserXimaLog;
import com.gameportal.domain.UserInfo;
import com.gameportal.domain.UserXimaSet;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.service.IAccountMoneyService;
import com.gameportal.service.IGamePlatformService;
import com.gameportal.service.IGameServiceHandler;
import com.gameportal.service.IMemberInfoService;
import com.gameportal.service.IPayOrderService;
import com.gameportal.service.IProxyClearingLogService;
import com.gameportal.service.IProxyReportEntityService;
import com.gameportal.service.IProxyTransferLogservice;
import com.gameportal.util.CookieUtil;
import com.gameportal.util.DateUtil2;
import com.gameportal.util.WebConstants;

import net.sf.json.JSONObject;

/**
 * 代理后台
 * @author leron
 *
 */
@Controller
@RequestMapping(value="/userproxy")
public class UserProxyController extends BaseAction{
	
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService = null;
	
	@Resource(name = "memberInfoServiceImpl")
	private IMemberInfoService memberInfoService = null;
	
	@Resource(name = "payOrderServiceImpl")
	private IPayOrderService payOrderServiceImpl=null;
	
	@Resource(name = "proxyClearingLogService")
	private IProxyClearingLogService proxyClearingLogService=null;
	
	@Resource(name = "iGamePlatformService")
	private IGamePlatformService iGamePlatformService = null;
	
	@Resource(name = "gamePlatformHandlerMap")
	private Map<String, IGameServiceHandler> gamePlatformHandlerMap = null;
	
	@Resource(name="proxyReportEntityService")
	private IProxyReportEntityService proxyReportEntityService;
	
	@Resource(name="accountMoneyServiceImpl")
	private IAccountMoneyService accountMoneyService;
	
	@Resource(name="proxyTransferLogservice")
	private IProxyTransferLogservice proxyTransferLogservice;
	
	/**
	 * 我的结算记录
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/queryProxyClearing")
	public ModelAndView queryProxyClearing(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd =getPageData();
		try {
			String vuid = CookieUtil.getOrCreateVuid(getRequest(), ResponseContext.getResponse());
			String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
			Class<Object> c = null;
			MemberInfo info= (MemberInfo) iRedisService.getRedisResult(key, c);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("proxyuid", info.getUiid());
			String startDate = pd.getString("startDate");
			String endDate = pd.getString("endDate");
			if(StringUtils.isNotBlank(startDate)){
				params.put("startdate", startDate);
			}else{
				startDate =DateUtil2.getFirstDay(DateUtil2.format(new Date()));
				params.put("startdate", startDate);
			}
			if(StringUtils.isNotBlank(endDate)){
				params.put("enddate", endDate);
			}else{
				endDate = DateUtil2.format(new Date());
				params.put("enddate", endDate);
			}
			page.setPd(pd);
			ProxyReportEntity proxyEntity = proxyReportEntityService.getProxyFrom(params);
			pd.put("startdate", startDate);
			pd.put("enddate", endDate);
			mv.addObject("proxyobj", proxyEntity);
			mv.addObject("proxyuid", info.getUiid());
			mv.addObject("pd",pd);
			mv.addObject("page",page);
			mv.addObject("memberinfo",info);
		} catch (Exception e) {
			logger.error("获取代理报表异常："+pd.getString("id"), e);
		}
		mv.setViewName("system/admin/logger/proxyClearing");
		return mv;
	}
	
	
	
	/**
	 * 我的结算记录
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/proxyClearingDetail")
	public ModelAndView proxyClearingDetail(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd =getPageData();
		try {
			Map<String, Object> map =new HashMap<>();
			map.put("account", pd.get("account"));
			MemberInfo info= memberInfoService.queryMemberInfo(map);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("proxyuid", info.getUiid());
			String startDate = pd.getString("startDate");
			String endDate = pd.getString("endDate");
			if(StringUtils.isNotBlank(startDate)){
				params.put("startdate", startDate);
			}else{
				startDate =DateUtil2.getFirstDay(DateUtil2.format(new Date()));
				params.put("startdate", startDate);
			}
			if(StringUtils.isNotBlank(endDate)){
				params.put("enddate", endDate);
			}else{
				endDate = DateUtil2.format(new Date());
				params.put("enddate", endDate);
			}
			page.setPd(pd);
			ProxyReportEntity proxyEntity = proxyReportEntityService.getProxyFrom(params);
			pd.put("startdate", startDate);
			pd.put("enddate", endDate);
			mv.addObject("proxyobj", proxyEntity);
			mv.addObject("proxyuid", info.getUiid());
			mv.addObject("pd",pd);
			mv.addObject("page",page);
			mv.addObject("memberinfo",info);
		} catch (Exception e) {
			logger.error("获取代理报表异常："+pd.getString("id"), e);
		}
		mv.setViewName("system/admin/logger/proxyClearingDetail");
		return mv;
	}
	/**
	 * 我的结算记录
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/queryProxyClearingLog")
	public ModelAndView queryProxyClearingLog(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd =getPageData();
		String vuid = CookieUtil.getOrCreateVuid(getRequest(), ResponseContext.getResponse());
		String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
		Class<Object> c = null;
		MemberInfo info= (MemberInfo) iRedisService.getRedisResult(key, c);
		Map<String, Object> params=new HashMap<String, Object>();
		pd.put("uiid", info.getUiid());
		params.put("uiid", info.getUiid());
		pd.put("currentPage", pd.getString("currentPage"));
		String startDate =pd.getString("startDate");
		String endDate = pd.getString("endDate");
		if(StringUtils.isNotBlank(startDate)){
			if(StringUtils.isNotBlank(pd.getString("startsfm"))){
				pd.put("startsfm", pd.getString("startsfm"));
				params.put("startDate", startDate+" "+pd.getString("startsfm"));
			}else{
				pd.put("startsfm", "00:00:00");
				params.put("startDate", startDate+ " 00:00:00");
			}
		}else{
			startDate =DateUtil2.getFirstDay(DateUtil2.format(new Date()));
			pd.put("startsfm", "00:00:00");
			params.put("startDate", startDate+ " 00:00:00");
		}
		if(StringUtils.isNotBlank(endDate)){
			if(StringUtils.isNotBlank(pd.getString("endsfm"))){
				pd.put("endsfm", pd.getString("endsfm"));
				params.put("endDate", endDate+" "+pd.getString("endsfm"));
			}else{
				pd.put("endsfm", "23:59:59");
				params.put("endDate", endDate+ " 23:59:59");
			}
		}else{
			endDate = DateUtil2.format(new Date());
			pd.put("endsfm", "23:59:59");
			params.put("endDate", endDate+ " 23:59:59");
		}
		page.setPd(pd);
		List<ProxyClearingLog> proxyClearingLog = proxyClearingLogService.queryProxyClearingLog(page);
		double clearingAmount=0.00; //结算金额
		double finalamountTotal=0.00; //盈亏
		double validBetAmountTotal=0.00;//投注总额
		double ximaAmount=0.00;//洗码总额
		double preferentialTotal=0.00;//总优惠
		double realPL=0.00;//实际盈亏
		for (ProxyClearingLog proxyClearingLog2 : proxyClearingLog) {
			clearingAmount+=Double.valueOf(proxyClearingLog2.getClearingAmount());
			finalamountTotal+=Double.valueOf(proxyClearingLog2.getFinalamountTotal());
			validBetAmountTotal+=Double.valueOf(proxyClearingLog2.getValidBetAmountTotal());
			ximaAmount+=Double.valueOf(proxyClearingLog2.getXimaAmount());
			preferentialTotal+=Double.valueOf(proxyClearingLog2.getPreferentialTotal());
			realPL+=Double.valueOf(proxyClearingLog2.getRealPL());
		}
		ProxyClearingLog clearingLog=new ProxyClearingLog();
		clearingLog.setAccount("小计:");
		clearingLog.setClearingAmount(clearingAmount+"");
		clearingLog.setFinalamountTotal(finalamountTotal+"");
		clearingLog.setValidBetAmountTotal(validBetAmountTotal+"");
		clearingLog.setXimaAmount(ximaAmount+"");
		clearingLog.setPreferentialTotal(preferentialTotal+"");
		clearingLog.setRealPL(realPL+"");
		ProxyClearingLog clearingLogTotal=proxyClearingLogService.queryProxyClearingLogTotal(params);
		clearingLogTotal.setAccount("总计:");
		pd.put("startDate", startDate);
		pd.put("endDate", endDate);
		mv.addObject("pd",pd);
		mv.addObject("page",page);
		mv.addObject("proxyminTotal",clearingLog);
		mv.addObject("proxyMaxTotal",clearingLogTotal);
		mv.addObject("proxyList",proxyClearingLog);
		mv.addObject("memberinfo", getMemberInfo());
		mv.setViewName("system/admin/logger/proxyClearingLog");
		return mv;
	}
	
	/**
	 * 存款列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/queryDepositLog")
	public ModelAndView queryDepositLog(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd =getPageData();
		String vuid = CookieUtil.getOrCreateVuid(getRequest(), ResponseContext.getResponse());
		String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
		Class<Object> c = null;
		MemberInfo info= (MemberInfo) iRedisService.getRedisResult(key, c);
		Map<String, Object> params=new HashMap<String, Object>();
		pd.put("puiid", info.getUiid());
		params.put("puiid", info.getUiid());
		pd.put("currentPage", pd.getString("currentPage"));
		String startDate = pd.getString("startDate");
		String endDate =pd.getString("endDate");
		String date = DateUtil2.format(new Date());
		if(StringUtils.isNotBlank(startDate)){
			if(StringUtils.isNotBlank(pd.getString("startsfm"))){
				pd.put("startsfm", pd.getString("startsfm"));
				params.put("startDate", startDate+" "+pd.getString("startsfm"));
			}else{
				pd.put("startsfm", "00:00:00");
				params.put("startDate", startDate+ " 00:00:00");
			}
			pd.put("startDate", params.get("startDate"));
		}else{
			startDate = DateUtil2.getFirstDay(date);
			pd.put("startsfm", "00:00:00");
			params.put("startDate", startDate+ " 00:00:00");
			pd.put("startDate", params.get("startDate"));
		}
		if(StringUtils.isNotBlank(endDate)){
			if(StringUtils.isNotBlank(pd.getString("endsfm"))){
				pd.put("endsfm", pd.getString("endsfm"));
				params.put("endDate", endDate+" "+pd.getString("endsfm"));
			}else{
				pd.put("endsfm", "23:59:59");
				params.put("endDate", pd.getString("endDate")+ " 23:59:59");
			}
			pd.put("endDate", params.get("endDate"));
		}else{
			endDate = DateUtil2.getEndDay(date);
			params.put("endDate", endDate+ " 23:59:59");
			pd.put("endDate", params.get("endDate"));
			pd.put("endsfm", "23:59:59");
		}
		if(StringUtils.isNotBlank(pd.getString("account"))){
			pd.put("uaccount", pd.getString("account"));
			params.put("uaccount", pd.getString("account"));
		}
		if(StringUtils.isNotEmpty(pd.getString("paymethods"))){
			if("1".equals(pd.getString("paymethods"))){
				pd.put("paymethods", "0");
				params.put("paymethods", "0");
			}else if("2".equals(pd.getString("paymethods"))){
				pd.put("paymethods", "1");
				params.put("paymethods", "1");
			}
		}
		if(StringUtils.isEmpty(pd.getString("paytyple"))){
			pd.put("paytyple", "('0','2')");
			params.put("paytyple", "('0','2')");
		}else{
			if("1".equals(pd.getString("paytyple"))){
				pd.put("paytyple", "('0')");
				params.put("paytyple", "('0')");
				pd.put("paytypleSet", "0");
			}else if("2".equals(pd.getString("paytyple"))){
				pd.put("paytyple", "('2')");
				params.put("paytyple", "('2')");
				pd.put("paytypleSet", "2");
			}
		}
		pd.put("status", 3);
		params.put("status", 3);
		page.setPd(pd);
		List<PayOrder> orderList = payOrderServiceImpl.queryDepositOrderLog(page);
		double amount=0;//交易金额
		for (PayOrder payOrder : orderList) {
			amount+=payOrder.getAmount();
		}
		PayOrder payOrderMinTotal=new PayOrder();
		payOrderMinTotal.setAmount(amount);
		payOrderMinTotal.setUaccount("小计:");
		String amounts=payOrderServiceImpl.queryOrderLogTotal(params);
		PayOrder payOrderMaxTotal=new PayOrder();
		payOrderMaxTotal.setUaccount("总计:");
		payOrderMaxTotal.setAmount(Double.valueOf(amounts));
		pd.put("startDate", startDate);
		pd.put("endDate", endDate);
		mv.addObject("pd",pd);
		mv.addObject("page",page);
		mv.addObject("payOrderMinTotal",payOrderMinTotal);
		mv.addObject("payOrderMaxTotal",payOrderMaxTotal);
		mv.addObject("orderList",orderList);
		mv.addObject("memberinfo", getMemberInfo());
		mv.setViewName("system/admin/logger/queryDepositLog");
		return mv;
	}
	
	/**
	 * 提款列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/queryPayOrderLog")
	public ModelAndView queryPayOrderLog(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd =getPageData();
		String vuid = CookieUtil.getOrCreateVuid(getRequest(), ResponseContext.getResponse());
		String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
		Class<Object> c = null;
		MemberInfo info= (MemberInfo) iRedisService.getRedisResult(key, c);
		Map<String, Object> params=new HashMap<String, Object>();
		pd.put("puiid", info.getUiid());
		params.put("puiid", info.getUiid());
		pd.put("currentPage", pd.getString("currentPage"));
		if(!StringUtils.isBlank(pd.getString("startDate"))){
			if(StringUtils.isNotBlank(pd.getString("startsfm"))){
				pd.put("startDate", pd.getString("startDate")+" "+pd.getString("startsfm"));
				pd.put("startsfm", pd.getString("startsfm"));
				params.put("startDate", pd.getString("startDate"));
			}else{
				pd.put("startDate", pd.getString("startDate")+ " 00:00:00");
				pd.put("startsfm", "00:00:00");
				params.put("startDate", pd.getString("startDate"));
			}
		}
		if(!StringUtils.isBlank(pd.getString("endDate"))){
			if(StringUtils.isNotBlank(pd.getString("endsfm"))){
				pd.put("endDate", pd.getString("endDate")+" "+pd.getString("endsfm"));
				pd.put("endsfm", pd.getString("endsfm"));
				params.put("endDate", pd.getString("endDate"));
			}else{
				pd.put("endDate", pd.getString("endDate")+ " 23:59:59");
				pd.put("endsfm", "23:59:59");
				params.put("endDate", pd.getString("endDate"));
			}
		}
		if(!StringUtils.isBlank(pd.getString("account"))){
			pd.put("uaccount", pd.getString("account"));
			params.put("uaccount", pd.getString("account"));
		}
		pd.put("status", 3);
		params.put("status", 3);
		pd.put("paytyple", "('1')");
		params.put("paytyple", "('1')");
		page.setPd(pd);
		List<PayOrder> orderList = payOrderServiceImpl.queryDepositOrderLog(page);
		double amount=0;//交易金额
		for (PayOrder payOrder : orderList) {
			amount+=payOrder.getAmount();
		}
		PayOrder payOrderMinTotal=new PayOrder();
		payOrderMinTotal.setAmount(amount);
		payOrderMinTotal.setUaccount("小计:");
		String amounts=payOrderServiceImpl.queryOrderLogTotal(params);
		PayOrder payOrderMaxTotal=new PayOrder();
		payOrderMaxTotal.setUaccount("总计:");
		payOrderMaxTotal.setAmount(Double.valueOf(amounts));
		mv.addObject("pd",pd);
		mv.addObject("page",page);
		mv.addObject("payOrderMinTotal",payOrderMinTotal);
		mv.addObject("payOrderMaxTotal",payOrderMaxTotal);
		mv.addObject("orderList",orderList);
		mv.addObject("memberinfo", getMemberInfo());
		mv.setViewName("system/admin/logger/queryPayOrderLog");
		return mv;
	}
	
	/**
	 * 扣款列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/queryPunishOrderLog")
	public ModelAndView queryPunishOrderLog(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd =getPageData();
		String vuid = CookieUtil.getOrCreateVuid(getRequest(), ResponseContext.getResponse());
		String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
		Class<Object> c = null;
		MemberInfo info= (MemberInfo) iRedisService.getRedisResult(key, c);
		Map<String, Object> params=new HashMap<String, Object>();
		pd.put("puiid", info.getUiid());
		params.put("puiid", info.getUiid());
		pd.put("currentPage", pd.getString("currentPage"));
		if(!StringUtils.isBlank(pd.getString("startDate"))){
			if(StringUtils.isNotBlank(pd.getString("startsfm"))){
				pd.put("startDate", pd.getString("startDate")+" "+pd.getString("startsfm"));
				pd.put("startsfm", pd.getString("startsfm"));
				params.put("startDate", pd.getString("startDate"));
			}else{
				pd.put("startDate", pd.getString("startDate")+ " 00:00:00");
				pd.put("startsfm", "00:00:00");
				params.put("startDate", pd.getString("startDate"));
			}
		}
		if(!StringUtils.isBlank(pd.getString("endDate"))){
			if(StringUtils.isNotBlank(pd.getString("endsfm"))){
				pd.put("endDate", pd.getString("endDate")+" "+pd.getString("endsfm"));
				pd.put("endsfm", pd.getString("endsfm"));
				params.put("endDate", pd.getString("endDate"));
			}else{
				pd.put("endDate", pd.getString("endDate")+ " 23:59:59");
				pd.put("endsfm", "23:59:59");
				params.put("endDate", pd.getString("endDate"));
			}
		}
		if(!StringUtils.isBlank(pd.getString("account"))){
			pd.put("uaccount", pd.getString("account"));
			params.put("uaccount", pd.getString("account"));
		}
		pd.put("status", 3);
		params.put("status", 3);
		pd.put("paytyple", "('3')");
		params.put("paytyple", "('3')");
		page.setPd(pd);
		List<PayOrder> orderList = payOrderServiceImpl.queryDepositOrderLog(page);
		double amount=0;//交易金额
		for (PayOrder payOrder : orderList) {
			amount+=payOrder.getAmount();
		}
		PayOrder payOrderMinTotal=new PayOrder();
		payOrderMinTotal.setAmount(amount);
		payOrderMinTotal.setUaccount("小计:");
		String amounts=payOrderServiceImpl.queryOrderLogTotal(params);
		PayOrder payOrderMaxTotal=new PayOrder();
		payOrderMaxTotal.setUaccount("总计:");
		payOrderMaxTotal.setAmount(Double.valueOf(amounts));
		mv.addObject("pd",pd);
		mv.addObject("page",page);
		mv.addObject("payOrderMinTotal",payOrderMinTotal);
		mv.addObject("payOrderMaxTotal",payOrderMaxTotal);
		mv.addObject("orderList",orderList);
		mv.addObject("memberinfo", getMemberInfo());
		mv.setViewName("system/admin/logger/queryPunishOrderLog");
		return mv;
	}
	
	/**
	 * 下线本月报表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/queryProxyUserReport")
	public ModelAndView queryProxyUserReport(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd =getPageData();
		String vuid = CookieUtil.getOrCreateVuid(getRequest(), ResponseContext.getResponse());
		String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
		Class<Object> c = null;
		MemberInfo info= (MemberInfo) iRedisService.getRedisResult(key, c);
		Map<String, Object> params=new HashMap<String, Object>();
		pd.put("puiid", info.getUiid());
		params.put("puiid", info.getUiid());
		String date = DateUtil2.format(new Date());
		String startDate = pd.getString("startDate");
		String endDate =pd.getString("endDate");
		if(StringUtils.isNotBlank(pd.getString("account"))){
			pd.put("account", pd.getString("account"));
			params.put("account", pd.getString("account"));
		}
		if(StringUtils.isNotBlank(startDate)){
			params.put("startDate", pd.getString("startDate")+ " 00:00:00");
			pd.put("startDate", params.get("startDate"));
		}else{
			startDate =DateUtil2.getFirstDay(date);
			params.put("startDate", DateUtil2.getFirstDay(date) + " 00:00:00");
			pd.put("startDate", params.get("startDate"));
		}
		if(StringUtils.isNotBlank(endDate)){
			params.put("endDate", pd.getString("endDate")+ " 23:59:59");
			pd.put("endDate", params.get("endDate"));
		}else{
			endDate = DateUtil2.getEndDay(date);
			params.put("endDate", endDate + " 23:59:59");
			pd.put("endDate", params.get("endDate"));
		}
		pd.put("currentPage", pd.getString("currentPage"));
		page.setPd(pd);
		List<MemberInfoReport> memberInfoReportlist = memberInfoService.queryMemberInfoReport(page);
		double money=0.00; //钱包余额
		double depositTotal=0.00; //总存款
		double withdrawalTotal=0.00; //总提款
		double preferentialTotal=0.00; //总优惠
		double ximaTotal=0.00; //总洗码
		for (MemberInfoReport memberInfoReport : memberInfoReportlist) {
			money+=Double.valueOf(memberInfoReport.getMoney());
			depositTotal+=Double.valueOf(memberInfoReport.getDepositTotal());
			withdrawalTotal+=Double.valueOf(memberInfoReport.getWithdrawalTotal());
			preferentialTotal+=Double.valueOf(memberInfoReport.getPreferentialTotal());
			ximaTotal+=Double.valueOf(memberInfoReport.getXimaTotal());
		}
		MemberInfoReport memberInfoReportMinTotal=new MemberInfoReport();
		memberInfoReportMinTotal.setAccount("小计:");
		memberInfoReportMinTotal.setMoney(money+"");
		memberInfoReportMinTotal.setDepositTotal(depositTotal+"");
		memberInfoReportMinTotal.setWithdrawalTotal(withdrawalTotal+"");
		memberInfoReportMinTotal.setPreferentialTotal(preferentialTotal+"");
		memberInfoReportMinTotal.setXimaTotal(ximaTotal+"");
		DownUserReportTotal downuserTotal=memberInfoService.queryMemberInfoReportTotal(params);
		pd.put("startDate", startDate);
		pd.put("endDate", endDate);
		mv.addObject("pd",pd);
		mv.addObject("page",page);
		mv.addObject("downuserMinTotal",memberInfoReportMinTotal);
		mv.addObject("downuserMaxTotal",downuserTotal);
		mv.addObject("reportList",memberInfoReportlist);
		mv.addObject("memberinfo", getMemberInfo());
		mv.setViewName("system/admin/logger/memberInfoReport");
		return mv;
	}
	
	/**
	 * 下线本月报表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/querySingerUserReport")
	@ResponseBody
	public String querySingerUserReport(@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "type", required = false) String type, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONObject json = new JSONObject();
		String vuid = CookieUtil.getOrCreateVuid(getRequest(), ResponseContext.getResponse());
		String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
		Class<Object> c = null;
		MemberInfo info= (MemberInfo) iRedisService.getRedisResult(key, c);
		Map<String, Object> params=new HashMap<String, Object>();
		if(type.equals("0")){
			params.put("puiid", info.getUiid());
		}else{
			if(StringUtils.isNotBlank(account)){
				params.put("account", account);
			}
		}
		String date = DateUtil2.format(new Date());
		if(StringUtils.isNotBlank(startDate)){
			params.put("startDate", startDate+ " 00:00:00");
		}else{
			startDate =DateUtil2.getFirstDay(date);
			params.put("startDate", DateUtil2.getFirstDay(date) + " 00:00:00");
		}
		if(StringUtils.isNotBlank(endDate)){
			params.put("endDate", endDate+ " 23:59:59");
		}else{
			endDate = DateUtil2.getEndDay(date);
			params.put("endDate", endDate + " 23:59:59");
		}
		MemberInfoReport report = memberInfoService.querySingleWinorLess(params);
		json.put("account", account);
		json.put("success", true);
		json.put("winorless", report.getWinLossTotal());
		return json.toString();
	}
	
	
	/**
	 * 下线会员查询
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/queryDownUser")
	public ModelAndView queryDownUser(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd =getPageData();
		String vuid = CookieUtil.getOrCreateVuid(getRequest(), ResponseContext.getResponse());
		String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
		Class<Object> c = null;
		MemberInfo info= (MemberInfo) iRedisService.getRedisResult(key, c);
		pd.put("uiid", info.getUiid());
		pd.put("currentPage", pd.getString("currentPage"));
		String startDate = pd.getString("startDate");
		String endDate = pd.getString("endDate");
		String date = DateUtil2.format(new Date());
		if(StringUtils.isNotBlank(pd.getString("account"))){
			pd.put("account", pd.getString("account"));
		}
		if(StringUtils.isNotBlank(pd.getString("startDate"))){
			pd.put("startDate", pd.getString("startDate")+ " 00:00:00");
		}else{
			startDate = DateUtil2.getFirstDay(date);
			pd.put("startDate", DateUtil2.getFirstDay(date) + " 00:00:00");
		}
		if(StringUtils.isNotBlank(pd.getString("endDate"))){
			pd.put("endDate", pd.getString("endDate")+ " 23:59:59");
		}else{
			endDate = DateUtil2.getEndDay(date);
			pd.put("endDate", DateUtil2.getEndDay(date) + " 23:59:59");
		}
		page.setPd(pd);
		List<BetLogTotal> totalList = memberInfoService.queryDownUser(page);
		pd.put("startDate", startDate);
		pd.put("endDate", endDate);
		mv.addObject("pd",pd);
		mv.addObject("page",page);
		mv.addObject("totalList",totalList);
		mv.addObject("memberinfo", getMemberInfo());
		mv.setViewName("system/admin/logger/downUser");
		return mv;
	}
	
	
	/**
	 * 下线代理查询
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/queryDownProxy")
	public ModelAndView queryDownProxy(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd =getPageData();
		String vuid = CookieUtil.getOrCreateVuid(getRequest(), ResponseContext.getResponse());
		String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
		Class<Object> c = null;
		MemberInfo info= (MemberInfo) iRedisService.getRedisResult(key, c);
		pd.put("uiid", info.getUiid());
		pd.put("currentPage", pd.getString("currentPage"));
		if(StringUtils.isNotBlank(pd.getString("account"))){
			pd.put("account", pd.getString("account"));
		}
		page.setPd(pd);
		List<MemberInfo> totalList = memberInfoService.findlistPageDownProxy(page);
		mv.addObject("pd",pd);
		mv.addObject("page",page);
		mv.addObject("totalList",totalList);
		mv.addObject("memberinfo", getMemberInfo());
		mv.setViewName("system/admin/logger/downProxy");
		return mv;
	}
	
	/**
	 * 会员投注明细
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/queryDownUserDetail")
	public ModelAndView queryDownUserDetail(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd =getPageData();
		pd.put("currentPage", pd.getString("currentPage"));
		String date = DateUtil2.format(new Date());
		if(StringUtils.isNotBlank(pd.getString("account"))){
			pd.put("account", pd.getString("account"));
		}
		String startDate = pd.getString("startDate");
		String endDate = pd.getString("endDate");
		if(StringUtils.isNotBlank(startDate)){
			if(StringUtils.isNotBlank(pd.getString("startsfm"))){
				pd.put("startDate", startDate+" "+pd.getString("startsfm"));
				pd.put("startsfm", pd.getString("startsfm"));
			}else{
				pd.put("startDate", startDate+ " 00:00:00");
				pd.put("startsfm", "00:00:00");
			}
		}else{
			startDate = DateUtil2.getFirstDay(date);
			pd.put("startDate", DateUtil2.getFirstDay(date) + " 00:00:00");
			pd.put("startsfm", "00:00:00");
		}
		if(StringUtils.isNotBlank(endDate)){
			if(StringUtils.isNotBlank(pd.getString("endsfm"))){
				pd.put("endDate", endDate+" "+pd.getString("endsfm"));
				pd.put("endsfm", pd.getString("endsfm"));
			}else{
				pd.put("endDate", endDate+ " 23:59:59");
				pd.put("endsfm", "23:59:59");
			}
		}else{
			endDate = DateUtil2.getEndDay(date);
			pd.put("endDate", DateUtil2.getEndDay(date) + " 23:59:59");
			pd.put("endsfm", "23:59:59");
		}
		page.setPd(pd);
		List<BetLog> betList = memberInfoService.queryDownUserBetLog(page);
		pd.put("startDate", startDate);
		pd.put("endDate", endDate);
		mv.addObject("pd",pd);
		mv.addObject("page",page);
		mv.addObject("betList",betList);
		mv.addObject("memberinfo", getMemberInfo());
		mv.setViewName("system/admin/logger/downUserBetDetail");
		return mv;
	}
	
	/**
	 * 下线洗码明细
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/queryProxyUserXimaLog")
	public ModelAndView queryProxyUserXimaLog(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd =getPageData();
		String vuid = CookieUtil.getOrCreateVuid(getRequest(), ResponseContext.getResponse());
		String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
		Class<Object> c = null;
		MemberInfo info= (MemberInfo) iRedisService.getRedisResult(key, c);
		pd.put("puiid", info.getUiid());
		pd.put("currentPage", pd.getString("currentPage"));
		String startDate = pd.getString("startDate");
		String endDate = pd.getString("endDate");
		String date = DateUtil2.format(new Date());
		if(StringUtils.isNotBlank(startDate)){
			pd.put("startDate", startDate+ " 00:00:00");
		}else{
			startDate = DateUtil2.getFirstDay(date);
			pd.put("startDate", DateUtil2.getFirstDay(date) + " 00:00:00");
		}
		if(StringUtils.isNotBlank(endDate)){
			pd.put("endDate", endDate+ " 23:59:59");
		}else{
			endDate = DateUtil2.getEndDay(date);
			pd.put("endDate", DateUtil2.getEndDay(date) + " 23:59:59");
		}
		if(!StringUtils.isBlank(pd.getString("account"))){
			pd.put("account", pd.getString("account"));
		}
		page.setPd(pd);
		List<ProxyUserXimaLog> ximaList = memberInfoService.queryDownUserXimaLog(page);
		pd.put("startDate", startDate);
		pd.put("endDate", endDate);
		mv.addObject("pd",pd);
		mv.addObject("page",page);
		mv.addObject("ximaList",ximaList);
		mv.addObject("memberinfo", getMemberInfo());
		mv.setViewName("system/admin/logger/proxyUserXimaLog");
		return mv;
	}
	
	
	/**
	 * 自助洗码
	 * @param clearingid
	 * @param jsdate
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/ziZhuXima")
	@ResponseBody
	public synchronized Object ziZhuXima(@RequestParam(value = "clearingid", required = false) Long clearingid,
										@RequestParam(value = "jsdate", required = false) String jsdate,
			HttpServletRequest request, HttpServletResponse response){
		JSONObject json=new JSONObject();
		try{
			String vuid = CookieUtil.getOrCreateVuid(getRequest(), ResponseContext.getResponse());
			String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
			Class<Object> c = null;
			MemberInfo info= (MemberInfo) iRedisService.getRedisResult(key, c);
			Map<String, Object> params=new HashMap<String, Object>();
			params.put("clearingid", clearingid);
			if(StringUtils.isNotBlank(jsdate)){
				params.put("jsdate", jsdate.subSequence(0, 10));
			}
			params.put("puiid", info.getUiid());
			String code=proxyClearingLogService.saveXima(params);
			if(code.equals("0")){
				json.put("success", true);
				json.put("msg", "自助洗码成功，我们会尽快为您入账。");
				return json.toString();
			}else if(code.equals("400")){
				json.put("success", false);
				json.put("msg", "您要洗码的数据不存在或该数据不是可洗码状态，请刷新页面后重试。");
				return json.toString();
			}else if(code.equals("404")){
				json.put("success", false);
				json.put("msg", "没有找到可分配的下线用户。");
				return json.toString();
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			json.put("success", false);
			json.put("msg", e.getMessage());
			return json.toString();
		}
		return json.toString();
	}
	
	
	/**
	 * 设置洗码比例
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/setXimaScale")
	@ResponseBody
	public Object setXimaScale(@RequestParam(value = "uiid", required = false) Long uiid,
							   @RequestParam(value = "ximascale", required = false) String ximascale,
			HttpServletRequest request, HttpServletResponse response)throws Exception{
		JSONObject json=new JSONObject();
		try {
			if(uiid==null){
				json.put("success", false);
				json.put("msg", "请选择需要分配洗码比例的用户。");
				return json.toString();
			} 
			if(!StringUtils.isNotBlank(ximascale)){
				json.put("success", false);
				json.put("msg", "请输入您要分配给用户的洗码比例。");
				return json.toString();
			} 
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
			Class<Object> c = null;
			MemberInfo memberInfo = (MemberInfo) iRedisService.getRedisResult(key, c);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("uiid", memberInfo.getUiid());
			ProxySet proxyobj = memberInfoService.queryScaleResult(params);
			if(proxyobj == null){
				json.put("success", false);
				json.put("msg", "编辑用户洗码比例失败错误代码:500。");
				return json.toString();
			}
			if(Double.valueOf(ximascale) > Double.valueOf(proxyobj.getXimascale())){
				json.put("success", false);
				json.put("msg", "您当前洗码比例为："+proxyobj.getXimascale()+",分配给下线洗码比例不能大于您当前的洗码比例。");
				return json.toString();
			}
			params.clear();
			params.put("uiid", uiid);
			params.put("proxyid", memberInfo.getUiid());
			params.put("status", "1");
			UserXimaSet uxinaSet = memberInfoService.queryUserXimaSet(params);
			if(uxinaSet !=null){
				uxinaSet.setXimascale(ximascale);
				uxinaSet.setSettime(DateUtil2.format2(new Date()));
				if(memberInfoService.updateUserXimaSet(uxinaSet)){
					json.put("success", true);
					json.put("msg", "编辑用户洗码比例成功。");
					return json.toString();
				}else{
					json.put("success", false);
					json.put("msg", "编辑用户洗码比例失败。");
					return json.toString();
				}
			}else{
				uxinaSet = new UserXimaSet();
				uxinaSet.setUiid(uiid.intValue());
				uxinaSet.setStatus(1);
				uxinaSet.setProxyid(memberInfo.getUiid().intValue());
				uxinaSet.setXimascale(ximascale);
				uxinaSet.setSettime(DateUtil2.format2(new Date()));
				if(memberInfoService.insertUserXimaSet(uxinaSet)){
					json.put("success", true);
					json.put("msg", "保存用户洗码比例成功。");
					return json.toString();
				}else{
					json.put("success", false);
					json.put("msg", "保存用户洗码比例失败。");
					return json.toString();
				}
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			json.put("success", false);
			json.put("msg", e.getMessage());
			return json.toString();
		}
	}
	
	/**
	 * 查询用户游戏余额
	 * @param account
	 * @param apipassword
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryGameMoney")
	public @ResponseBody
	Object queryGameMoney(
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "apipassword", required = false) String apipassword,
			HttpServletRequest request, HttpServletResponse response) {
		    JSONObject json=new JSONObject();
			try{
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("status", 1);
				List<GamePlatform> list = iGamePlatformService.getGameList(params);
				List<GameMoney> resultList = new ArrayList<GameMoney>();
				if(null == list){
					json.put("success", false);
					json.put("data", "没有查询到平台信息。");
					return json.toString();
				}
				String strMoney = "";
				GameMoney gameMoney = null;
				UserInfo userInfo = new UserInfo();
				userInfo.setAccount(account);
				userInfo.setApipassword(apipassword);
				for(GamePlatform game : list){
					try {
						gameMoney = new GameMoney();
						strMoney = (String)gamePlatformHandlerMap.get(game.getGpname()).queryBalance(userInfo, game, null);
						if (null == strMoney || "".equals(strMoney)) {
							gameMoney.setMoney("0.00");
						} else {
							gameMoney.setMoney(strMoney);
						}
					} catch (Exception e) {
						logger.error(e.getMessage());
						gameMoney.setMoney("0.00");
					}
					gameMoney.setGpname(game.getGpname());
					resultList.add(gameMoney);
					strMoney = "";
				}
				json.put("success", true);
				json.put("data", resultList);
				return json.toString();
			}catch (Exception e) {
				e.printStackTrace();
				logger.error("Exception: ", e);
				json.put("success", false);
				json.put("data", "网络异常,请重试");
				return json.toString();
			}
			
		}
	
	/**
	 * 获取最新用户信息。
	 * @return
	 */
	private MemberInfo getMemberInfo(){
		String vuid = CookieUtil.getOrCreateVuid(getRequest(), ResponseContext.getResponse());
		String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
		Class<Object> c = null;
		MemberInfo info= (MemberInfo) iRedisService.getRedisResult(key, c);
		Map<String,  Object> params = new HashMap<String, Object>();
		params.put("uiid", info.getUiid());
		return memberInfoService.queryMemberInfo(params);
	}
	
	
	/**
	 * 进入转账页面。
	 * @return
	 */
	@RequestMapping(value="/membertransfer")
	public ModelAndView goTransfer(){
		MemberInfo member = getMemberInfo();
		Map<String, Object> map = new HashMap<>();
		map.put("uiid", member.getUiid());
		AccountMoney money =accountMoneyService.queryAccountMoney(map);
		map.put("accountMoney", money);
		ModelAndView mv = getModelAndView();
		mv.addObject("memberinfo", getMemberInfo());
		mv.addObject("accountMoney",money);
		mv.setViewName("system/admin/logger/downtransfer");
		return mv;
	}
	
	/**
	 * 代理转账前校验
	 * @param account
	 * @param amount
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "validateLower")
	@ResponseBody
	public String validateLower(@RequestParam(value = "account",required =false) String account,@RequestParam(value="amount",required =false) Integer amount, HttpServletRequest request, HttpServletResponse response){
		JSONObject json = new JSONObject();
		MemberInfo memberInfo = getMemberInfo();
		Map<String, Object> map = new HashMap<>();
		map.put("account", account);
		MemberInfo lowInfo =memberInfoService.queryMemberInfo(map);
		if(lowInfo == null || !lowInfo.getPuiid().equals(memberInfo.getUiid())){
			json.put("type", "1");
			json.put("result", "0");
			json.put("msg", "该收款人不是您的直接下级！");
			return json.toString();
		}
		map.clear();
		map.put("uiid", memberInfo.getUiid());
		AccountMoney money =accountMoneyService.queryAccountMoney(map);
		if(money.getTotalamount().compareTo(new BigDecimal(amount)) <0){
			json.put("type", "2");
			json.put("result", "0");
			json.put("msg", "转账金额不能大于您的当前额度！");
			return json.toString();
		}
		json.put("result", "1");
		json.put("msg", money.getTotalamount().subtract(new BigDecimal(amount)));
		
		return json.toString();
	}
	
	/**
	 * 代理转账操作
	 * @param account
	 * @param amount
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/proxyTransfer")
	@ResponseBody
	public String proxyTransfer(@RequestParam (value = "account",required =false) String account,@RequestParam(value="amount",required =false) Integer amount, HttpServletRequest request, HttpServletResponse response){
		JSONObject json = new JSONObject();
		try {
			MemberInfo memberInfo = getMemberInfo();
			Map<String, Object> map = new HashMap<>();
			map.put("account", account);
			MemberInfo lowInfo =memberInfoService.queryMemberInfo(map);
			if(lowInfo == null || !lowInfo.getPuiid().equals(memberInfo.getUiid())){
				json.put("result", "0");
				json.put("msg", "转账操作失败，该收款人不是您的直接下级！");
				return json.toString();
			}
			map.clear();
			map.put("uiid", memberInfo.getUiid());
			AccountMoney money =accountMoneyService.queryAccountMoney(map);
			if(money.getTotalamount().compareTo(new BigDecimal(amount))<0){
				json.put("result", "0");
				json.put("msg", "转账操作失败，转账金额不能大于您的当前额度！");
				return json.toString();
			}
			accountMoneyService.updateProxyTransfer(memberInfo, lowInfo, amount);
			json.put("result", 1);
			json.put("msg", "恭喜，转账操作成功！");
		} catch (Exception e) {
			logger.error("代理转账异常。",e);
			json.put("result", "0");
			json.put("msg", "系统异常，请稍后重试。");
		}
		return json.toString();
	}
	
	/**
	 * 代理转账记录
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/downtransferlist")
	public ModelAndView queryProxyTransferLog(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd =getPageData();
		String vuid = CookieUtil.getOrCreateVuid(getRequest(), ResponseContext.getResponse());
		String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
		Class<Object> c = null;
		MemberInfo info= (MemberInfo) iRedisService.getRedisResult(key, c);
		pd.put("pid", info.getUiid());
		pd.put("currentPage", pd.getString("currentPage"));
		if(!StringUtils.isBlank(pd.getString("startDate"))){
			if(StringUtils.isNotBlank(pd.getString("startsfm"))){
				if(pd.getString("startDate").length()<19){
					pd.put("startDate", pd.getString("startDate")+" "+pd.getString("startsfm"));
				}else{
					pd.put("startDate", pd.getString("startDate"));
				}
				pd.put("startsfm", pd.getString("startsfm"));
			}else{
				pd.put("startDate", pd.getString("startDate")+ " 00:00:00");
				pd.put("startsfm", "00:00:00");
			}
		}
		if(!StringUtils.isBlank(pd.getString("endDate"))){
			if(StringUtils.isNotBlank(pd.getString("endsfm"))){
				if(pd.getString("endDate").length()<19){
					pd.put("endDate", pd.getString("endDate")+" "+pd.getString("endsfm"));
				}else{
					pd.put("endDate", pd.getString("endDate"));
				}
				pd.put("endsfm", pd.getString("endsfm"));
			}else{
				pd.put("endDate", pd.getString("endDate")+ " 23:59:59");
				pd.put("endsfm", "23:59:59");
			}
		}
		if(!StringUtils.isBlank(pd.getString("uaccount"))){
			pd.put("laccount", pd.getString("uaccount"));
		}
		page.setPd(pd);
		List<ProxyTransferLog> logList = proxyTransferLogservice.pageProxyTransferLog(page);
		if(StringUtils.isNotEmpty(pd.getString("startDate")) && pd.getString("startDate").length()>10){
			pd.put("startDate", pd.getString("startDate").substring(0, 10));
		}
		if(StringUtils.isNotEmpty(pd.getString("endDate")) && pd.getString("endDate").length()>10){
			pd.put("endDate", pd.getString("endDate").substring(0, 10));
		}
		mv.addObject("pd",pd);
		mv.addObject("page",page);
		mv.addObject("logs",logList);
		mv.setViewName("system/admin/logger/downtransferlist");
		return mv;
	}
}
