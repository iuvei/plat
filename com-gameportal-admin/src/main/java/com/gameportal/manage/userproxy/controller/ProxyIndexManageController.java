package com.gameportal.manage.userproxy.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.manage.betlog.service.IBetLogService;
import com.gameportal.manage.member.model.MemberInfo;
import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.Tree;
import com.gameportal.manage.proxy.model.ProxySet;
import com.gameportal.manage.proxy.service.IProxySetService;
import com.gameportal.manage.proxydomain.model.ProxyDomian;
import com.gameportal.manage.proxydomain.service.IProxyDomianService;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.system.service.ISecurityService;
import com.gameportal.manage.user.model.AccountMoney;
import com.gameportal.manage.user.service.IAccountMoneyService;
import com.gameportal.manage.userproxy.service.IProxyManageService;
import com.gameportal.manage.util.CookieUtil;
import com.gameportal.manage.util.DateUtil;
import com.gameportal.manage.util.DateUtil2;
import com.gameportal.manage.util.PropertyContext;
import com.gameportal.manage.util.WebConstants;

/**
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/proxymanage")
public class ProxyIndexManageController {
	
	private static final Logger logger = Logger.getLogger(ProxyIndexManageController.class);
	
	@Resource(name = "securityServiceImpl")
	private ISecurityService iSecurityService = null;
	
	@Resource(name = "betLogServiceImpl")
	private IBetLogService betLogService = null;
	
	@Resource(name = "proxyManageService")
	private IProxyManageService proxyManageService;
	
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService = null;
	
	@Resource(name = "accountMoneyServiceImpl")
	private IAccountMoneyService accountMoneyService = null;
	
	@Resource(name = "proxyDomianService")
	private IProxyDomianService proxyDomianService;
	
	@Resource(name = "proxySetService")
	private IProxySetService proxySetService;
	
	private static Properties prop = PropertyContext
			.PropertyContextFactory("jdbc.properties").getPropertie();

	@RequestMapping(value = "/index")
	public String getValidateCode(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
		Class<Object> c = null;
		MemberInfo memberInfo = (MemberInfo) iRedisService.getRedisResult(key, c);
		Map<String, Object> params = new HashMap<String, Object>();
		if(null != memberInfo){
			params.put("uiid", memberInfo.getUiid());
			params.put("proxyuid", memberInfo.getUiid());
			AccountMoney accountMoney = accountMoneyService.getMoneyInfo(params);
			request.setAttribute("proxyWallet", accountMoney.getTotalamount());
			Long count = proxyManageService.selectProxyDownUserCount(params);
			request.setAttribute("downUserCount", count);
			ProxyDomian proxyDomian = proxyDomianService.query(params);
			String proxyURL = prop.getProperty("defaul.proxy.domain")+memberInfo.getUiid();
			if(null != proxyDomian){
				proxyURL = proxyDomian.getProxyurl();
			}
			request.setAttribute("proxydomian", proxyURL);
			
			String startDate = DateUtil2.getFirstDay(DateUtil2.format2(new Date()));
			String endDate =   DateUtil2.format2(new Date(), "yyyy-MM-dd");
			params.put("startDate", startDate+" 00:00:00");
			params.put("endDate", endDate+" 23:59:59");
			String profitandloss = betLogService.getProxyLoss(params);
			
			if(!StringUtils.isNotBlank(profitandloss)){
				request.setAttribute("profitandloss", "0.00");
			}else{
				String preferential = betLogService.getProxyPreferential(params);//查询代理下线总优惠
				double doublepreferential = 0.00;
				if(StringUtils.isNotBlank(preferential)){
					doublepreferential = Double.valueOf(preferential);
				}
				double loss = Double.valueOf(profitandloss);
				loss = loss - doublepreferential;//代理盈亏减优惠
				if(loss >= 0){
					
					request.setAttribute("profitandloss", "<font color=#00CC00>"+com.gameportal.manage.util.StringUtils.convertNumber(loss)+"</font>");
				}else{
					request.setAttribute("profitandloss", "<font color=red>"+com.gameportal.manage.util.StringUtils.convertNumber(loss)+"</font>");
				}
			}
			params.clear();
			params.put("uiid", memberInfo.getUiid());
			ProxySet proxyobj = proxySetService.queryObject(params);
			if(null != proxyobj){
				request.setAttribute("returnscale", proxyobj.getReturnscale());
				request.setAttribute("ximascale", proxyobj.getXimascale());
				request.setAttribute("isximaFlag", proxyobj.getIsximaflag());
			}else{
				request.setAttribute("returnscale", "0.00");
				request.setAttribute("ximascale", "0.00");
				request.setAttribute("isximaFlag", 0);
			}
		}
		return "manage/userproxy/index";
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(DateUtil2.format2(new Date(), "yyyy-MM-dd"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/treeMenu")
	public @ResponseBody
	Object treeMenu(HttpServletRequest request, HttpServletResponse response) {
		try {
//			String vuid = CookieUtil.getOrCreateVuid(request, response);
//			String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
//			Class<Object> c = null;
//			MemberInfo memberInfo = (MemberInfo) iRedisService.getRedisResult(key, c);
			Long userid =  Long.valueOf(prop.getProperty("proxy.id"));
			Tree tree = iSecurityService.getChildrenNodes(userid);
			return tree.getChildren();// 返回根菜单下面的子菜单
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
}
