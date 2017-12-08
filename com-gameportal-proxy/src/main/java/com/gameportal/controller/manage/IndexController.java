package com.gameportal.controller.manage;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gameportal.comms.Tools;
import com.gameportal.comms.WebConst;
import com.gameportal.controller.BaseAction;
import com.gameportal.controller.ResponseContext;
import com.gameportal.domain.AccountMoney;
import com.gameportal.domain.MemberInfo;
import com.gameportal.domain.ProxyDomain;
import com.gameportal.domain.ProxySet;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.service.IAccountMoneyService;
import com.gameportal.service.IMemberInfoService;
import com.gameportal.service.IPayOrderService;
import com.gameportal.service.IProxyWebSitePvService;
import com.gameportal.util.CookieUtil;
import com.gameportal.util.DateUtil2;
import com.gameportal.util.PropertyContext;
import com.gameportal.util.WebConstants;

/**
 * 
 * @author leron
 *
 */
@Controller
@RequestMapping(value="/manage")
public class IndexController extends BaseAction{
	
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService = null;
	
	@Resource(name = "memberInfoServiceImpl")
	private IMemberInfoService memberInfoService = null;
	
	@Resource(name = "accountMoneyServiceImpl")
	private IAccountMoneyService accountMoneyServiceImpl=null;
	
	@Resource(name="proxyWebSitePvService")
	private IProxyWebSitePvService proxyWebSitePvService;
	
	@Resource(name = "payOrderServiceImpl")
	private IPayOrderService payOrderServiceImpl;
	
	private static Properties prop = PropertyContext
			.PropertyContextFactory("jdbc.properties").getPropertie();
	
	/**
	 * 登录成功首页跳转
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/index")
	public ModelAndView index(HttpServletRequest request,
			HttpServletResponse response)throws Exception{
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/admin/home/index");
		mv.addObject("SYSNAME", Tools.readTxtFile(WebConst.SYSNAME)); //读取系统名称
		MemberInfo member = getMemberInfo();
		mv.addObject("memberinfo", member); //当前用户信息
		String vuid = CookieUtil.getOrCreateVuid(getRequest(), ResponseContext.getResponse());
		String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
		Class<Object> c = null;
		MemberInfo info= (MemberInfo) iRedisService.getRedisResult(key, c);
		if(info!=null){
			Map<String, Object> params=new HashMap<String, Object>();
			params.put("uiid", info.getUiid());
			AccountMoney accountMoney=accountMoneyServiceImpl.queryAccountMoney(params);
			mv.addObject("totolMoney", accountMoney.getTotalamount());//钱包余额
			Long count = memberInfoService.queryDownCount(params);
			mv.addObject("downUserCount", count); //下线人数
			params.put("proxyuid",info.getUiid());
			ProxyDomain proxyDomain=memberInfoService.queryProxyUrl(params);
			String proxyURL = prop.getProperty("defaul.proxy.domain")+info.getUiid();
			if(null != proxyDomain){
				proxyURL = proxyDomain.getProxyurl();
			}
			mv.addObject("proxyURL", proxyURL); //代理推广地址
			String startDate = DateUtil2.getFirstDay(DateUtil2.format2(new Date()));
			String endDate = DateUtil2.format2(new Date(), "yyyy-MM-dd");
//			params.put("startDate", startDate+" 00:00:00");
//			params.put("endDate", endDate+" 23:59:59");
//			String proxyloss=memberInfoService.queryProxyLoss(params);
//			if(StringUtils.isBlank(proxyloss)){
//				mv.addObject("proxyloss", "0.00");
//			}else{
//				String preferential = memberInfoService.queryProxyPreferential(params);//查询代理下线总优惠
//				double doublepreferential = 0.00;
//				if(StringUtils.isNotBlank(preferential)){
//					doublepreferential = Double.valueOf(preferential);
//				}
//				Map<String, Object> map = new HashMap<>();
//				params.put("paytyple", "('3')");
//				map.put("status", "3");
//				map.put("ordertype", "0");
//				map.put("puiid", member.getUiid());
//				map.put("startDate", startDate+" 00:00:00");
//				map.put("endDate", endDate+" 23:59:59");
//				String debit = payOrderServiceImpl.queryOrderLogTotal(map);
//				double loss = Double.valueOf(proxyloss);
//				loss = loss - doublepreferential;//代理盈亏减优惠
//				if(loss >= 0){
//					mv.addObject("proxyloss", "<font color=#00CC00>"+com.gameportal.util.StringUtils.convertNumber(loss)+"</font>");
//				}else{
//					mv.addObject("proxyloss", "<font color=red>"+com.gameportal.util.StringUtils.convertNumber(loss)+"</font>");
//				}
//			}
			params.clear();
			params.put("uiid", info.getUiid());
			ProxySet proxySet=memberInfoService.queryScaleResult(params);
			if(null != proxySet){
				mv.addObject("returnscale", proxySet.getReturnscale()); //占成
				mv.addObject("ximascale", proxySet.getXimascale());    //洗码占成
				mv.addObject("isximaFlag", proxySet.getIsximaflag());
				getSession().setAttribute("isximaFlag", proxySet.getIsximaflag());
			}else{
				mv.addObject("returnscale", "0.00");//占成
				mv.addObject("ximascale", "0.00"); //洗码占成
				mv.addObject("isximaFlag", 0);
				getSession().setAttribute("isximaFlag",0);
			}
			// 本日推广域名pv值
			params.clear();
			params.put("proxyid", info.getUiid());
			params.put("createDate", endDate);
			Long todayPv = proxyWebSitePvService.queryCount(params);
			mv.addObject("todayPv",todayPv);
			// 本月推广域名pv值
			params.remove("createDate");
			params.put("startDate", startDate);
			params.put("endDate", endDate);
			Long monthPv = proxyWebSitePvService.queryCount(params);
			mv.addObject("monthPv",monthPv);
			//本日注册量
			params.clear();
			params.put("uiid", info.getUiid());
			params.put("startDate", endDate);
			params.put("endDate", endDate);
			Long todayRegNum = memberInfoService.queryDownCount(params);
			mv.addObject("todayRegNum",todayRegNum);
			//本月注册量
			params.clear();
			params.put("uiid", info.getUiid());
			params.put("startDate", startDate);
			params.put("endDate", endDate);
			Long monthRegNum = memberInfoService.queryDownCount(params);
			mv.addObject("monthRegNum",monthRegNum);
			//本日存款量
			params.clear();
			params.put("puiid", info.getUiid());
			params.put("status", 3);
			params.put("paytyple", "('0')");
			params.put("startDate", endDate+" 00:00:00");
			params.put("endDate", endDate +" 23:59:59");
			String todayDeposit = payOrderServiceImpl.queryOrderLogTotal(params);
			mv.addObject("todayDeposit",todayDeposit);
			// 本月存款量
			params.put("startDate", startDate+" 00:00:00");
			params.put("endDate", endDate +" 23:59:59");
			String monthDeposit = payOrderServiceImpl.queryOrderLogTotal(params);
			mv.addObject("monthDeposit",monthDeposit);
		}
		return mv;
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
}
