package com.gameportal.manage.proxy.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.manage.betlog.model.BetClearing;
import com.gameportal.manage.betlog.model.BetLogTotal;
import com.gameportal.manage.betlog.service.IBetLogService;
import com.gameportal.manage.listener.SystemFieldsCache;
import com.gameportal.manage.member.model.MemberInfo;
import com.gameportal.manage.member.service.IMemberInfoService;
import com.gameportal.manage.pay.model.PayOrder;
import com.gameportal.manage.pay.service.IPayOrderService;
import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.proxy.model.ProxyInfo;
import com.gameportal.manage.proxy.model.ProxyReportEntity;
import com.gameportal.manage.proxy.service.IProxyClearingService;
import com.gameportal.manage.proxy.service.IProxyInfoService;
import com.gameportal.manage.proxy.service.IProxySetService;
import com.gameportal.manage.proxydomain.model.ProxyDomian;
import com.gameportal.manage.proxydomain.service.IProxyDomianService;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.system.model.SystemRole;
import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.system.service.ISystemService;
import com.gameportal.manage.userproxy.service.IProxyManageService;
import com.gameportal.manage.util.CookieUtil;
import com.gameportal.manage.util.DateUtil2;
import com.gameportal.manage.util.PropertyContext;
import com.gameportal.manage.util.WebConstants;

import net.sf.json.JSONObject;

/**
 * @ClassName: ProxyInfoController
 * @Description: TODO(代理控制类)
 * @author cheny
 * @date 2015-4-10 下午2:51:41
 */
@Controller
@RequestMapping(value = "/manage/proxy")
public class ProxyInfoController {
	
	@Resource(name = "systemServiceImpl")
	private ISystemService systemService = null;
	
	@Resource(name = "proxyInfoServiceImpl")
	private IProxyInfoService iProxyInfoService = null;
	
	@Resource(name = "proxyDomianService")
	private IProxyDomianService proxyDomianService;
	
	@Resource(name = "memberInfoServiceImpl")
	private IMemberInfoService memberInfoService = null;
	
	@Resource(name = "proxyManageService")
	private IProxyManageService proxyManageService;
	
	@Resource(name = "payOrderServiceImpl")
	private IPayOrderService payOrderService;
	
	@Resource(name = "proxySetService")
	private IProxySetService proxySetService;
	
	@Resource(name = "betLogServiceImpl")
	private IBetLogService betLogService = null;
	
	@Resource(name="proxyClearingService")
	private IProxyClearingService proxyClearingService;
	
	@Resource(name = "redisServiceImpl")
	private IRedisService redisService = null;
	
	private static Properties prop = PropertyContext
			.PropertyContextFactory("jdbc.properties").getPropertie();
	
	public static final Logger logger = Logger
			.getLogger(ProxyInfoController.class);

	public ProxyInfoController() {
		super();
	}

	@RequestMapping(value = "/index")
	public String index(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		JSONObject map = new JSONObject();
		map.put("", "全部");
		map.put("1", "公司入款");
		map.put("2", "在线支付");
		request.setAttribute("paymethodsMap", map.toString());
		JSONObject map2 = new JSONObject();
		map2.put("", "全部");
		map2.put("1", "充值");
		map2.put("2", "赠送");
		request.setAttribute("deposittypeMap", map2.toString());
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + WebConstants.FRAMEWORK_USER;
		Class<Object> c = null;
		SystemUser systemUser = (SystemUser) redisService.getRedisResult(key, c);
		String isManage ="0";;
		if (null != systemUser) {
			List<SystemRole> roleList = systemService.queryRoleByUserId(systemUser.getUserId());
			for (SystemRole sr : roleList) {
				if (-1 != sr.getRoleName().indexOf("合作伙伴")) {
					isManage = "1";
					break;
				}
			}
		}
		request.setAttribute("isManage", isManage);
		return "manage/proxy/proxyInfo";
	}

	@RequestMapping(value = "/queryProxyInfo")
	public @ResponseBody
	Object queryProxyInfo(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "domain", required = false) String domain,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + WebConstants.FRAMEWORK_USER;
		Class<Object> c = null;
		SystemUser systemUser = (SystemUser) redisService.getRedisResult(key, c);
		boolean manage =false;;
		if (null != systemUser) {
			List<SystemRole> roleList = systemService.queryRoleByUserId(systemUser.getUserId());
			for (SystemRole sr : roleList) {
				if (-1 != sr.getRoleName().indexOf("合作伙伴")) {
					manage = true;
					break;
				}
			}
		}
		if(manage){
			JSONObject jsonMap = JSONObject.fromObject(SystemFieldsCache.fields.get("proxy.ids"));
			String s = jsonMap.toString();
			s = s.substring(2, s.indexOf(":") - 1);
			params.put("manageIds", s);
		}
		if(!StringUtils.isBlank(account)){
			params.put("account", account);
		}
		if(StringUtils.isNotBlank(domain)){
			params.put("domain", domain);
		}
		params.put("accounttype", 1);
		Long count = iProxyInfoService.queryProxyInfoCount(params);
		List<ProxyInfo> list = iProxyInfoService.queryProxyInfo(params,startNo, pageSize);
		List<ProxyInfo> resultList = list;
		if (StringUtils.isNotBlank(ObjectUtils.toString(systemUser))) {
			boolean isAdmin = false;
			if (null != systemUser) {
				List<SystemRole> roleList = systemService.queryRoleByUserId(systemUser.getUserId());
				for (SystemRole sr : roleList) {
					if (-1 != sr.getRoleName().indexOf("管理员") || -1 != sr.getRoleName().indexOf("代理")) {
						isAdmin = true;
						break;
					}
				}
			}
			if (!isAdmin) {
				resultList = new ArrayList<ProxyInfo>();
				for (ProxyInfo vo : list) {
					String email = vo.getEmail();
					if (null != email && !"".equals(email)) {
						vo.setEmail("****" + email.substring(4, email.length()));
					} else {
						vo.setEmail("**********");
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
					String QQ = vo.getQq();
					if (StringUtils.isNotBlank(QQ)) {
						vo.setQq("****" + QQ.substring(2, QQ.length()));
					} else {
						vo.setQq("********");
					}
					resultList.add(vo);
				}
			}
		}
		return new GridPanel(count, resultList, true);
	}
	
	/**
	 * 会员存款记录
	 * @param id
	 * @param puiid
	 * @param uaccount
	 * @param paymethods
	 * @param startDate
	 * @param endDate
	 * @param thisPage
	 * @param pageSize
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryMemberDepositPayOrderlog")
	public @ResponseBody
	Object queryMemberDepositPayOrderlog(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "puiid", required = false) Long puiid,
			@RequestParam(value = "uaccount", required = false) String uaccount,
			@RequestParam(value = "paymethods", required = false) String paymethods,
			@RequestParam(value = "depositype", required = false) String depositype,
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
				if("1".equals(paymethods)){
					params.put("paymethods", "0");
				}else if("2".equals(paymethods)){
					params.put("paymethods", "1");
				}
			}
			if(StringUtils.isEmpty(depositype)){
				params.put("paytyple", "('0','2')");
			}
			if(StringUtils.isNotEmpty(depositype)){
				if("1".equals(depositype)){
					params.put("paytyple", "('0')");
				}else if("2".equals(depositype)){
					params.put("paytyple", "('2')");
				}
			}
			params.put("puiid", puiid);
			params.put("status", 3);
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
	
	
	/**
	 * 会员提款记录
	 * @param id
	 * @param uaccount
	 * @param startDate
	 * @param endDate
	 * @param thisPage
	 * @param pageSize
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryMemberPayOrderlog")
	public @ResponseBody
	Object queryMemberPayOrderlog(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "puiid", required = false) Long puiid,
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
			params.put("puiid", puiid);
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
	
	/**
	 * 会员扣款记录
	 * @param id
	 * @param uaccount
	 * @param startDate
	 * @param endDate
	 * @param thisPage
	 * @param pageSize
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryMemberPunishOrderlog")
	public @ResponseBody
	Object queryMemberPunishOrderlog(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "puiid", required = false) Long puiid,
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
			params.put("puiid", puiid);
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
	
	@RequestMapping(value = "/domainindex")
	public String domainindex(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		return "manage/proxy/proxydomainset";
	}
	
	/**
	 * 查询代理域名
	 * @param account
	 * @param startNo
	 * @param pageSize
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryProxyDomain")
	public @ResponseBody
	Object queryProxyDomain(
			@RequestParam(value = "proxyaccount", required = false) String proxyaccount,
			@RequestParam(value = "proxydomain", required = false) String proxydomain,
			@RequestParam(value = "start", required = false) Integer thisPage,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		if(!StringUtils.isBlank(proxyaccount)){
			params.put("account", proxyaccount);
		}
		if(!StringUtils.isBlank(proxydomain)){
			params.put("blurryURL", proxydomain);
		}
		Long count = proxyDomianService.getCount(params);
		List<ProxyDomian> list = proxyDomianService.getList(params,thisPage==null?0:thisPage, pageSize==null?30:pageSize);
		return new GridPanel(count, list, true);
	}
	
	@RequestMapping(value = "/saveProxyDomainInfo")
	@ResponseBody
	public Object saveProxyDomainInfo(@ModelAttribute ProxyDomian proxyDomian,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("account", proxyDomian.getProxyaccount());
			params.put("accounttype", 0);//查询不等于0
			MemberInfo proxyInfo = memberInfoService.qeuryMemberInfo(params);
			proxyDomian.setProxyurl(proxyDomian.getProxyurl().trim());//去空格
			if(null == proxyInfo || "".equals(proxyInfo)){
				return new ExtReturn(false, "代理账号【"+proxyDomian.getProxyaccount()+"】不存在，请检查后重新输入！");
			}
			if(null != proxyDomian.getUrlid() && !"".equals(proxyDomian.getUrlid())){
				params.clear();
				params.put("urlid", proxyDomian.getUrlid());
				//params.put("proxyuid", proxyInfo.getUiid());
				ProxyDomian g = proxyDomianService.query(params);
				if(g == null || "".equals(g)){
					return new ExtReturn(false, "您编辑的域名不存在，请刷新后重试。");
				}
				g = proxyDomian;
				g.setProxyuid(String.valueOf(proxyInfo.getUiid()));
				boolean up = proxyDomianService.saveOrUpdate(g);
				if(up){
					return new ExtReturn(true, "编辑成功。");
				}else{
					return new ExtReturn(false, "编辑失败。");
				
				}
			}else{
				params.clear();
				params.put("blurryURL", proxyDomian.getProxyurl());
				Long count = proxyDomianService.getCount(params);
				if(count > 0){
					return new ExtReturn(false, "您要绑定的域名已经被试用。");
				}
				proxyDomian.setStatus(1);
				proxyDomian.setProxyuid(String.valueOf(proxyInfo.getUiid()));
				boolean add = proxyDomianService.saveOrUpdate(proxyDomian);
				if(add){
					return new ExtReturn(true, "添加成功。");
				}else{
					return new ExtReturn(false, "添加失败。");
				}
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping("/deldomain/{id}")
	@ResponseBody
	public Object delDomain(@PathVariable Long id) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(id))) {
				return new ExtReturn(false, "域名主键不能为空！");
			}
			
			if (proxyDomianService.delete(id)) {
				return new ExtReturn(true, "删除成功！");
			} else {
				return new ExtReturn(false, "删除失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping("/bindomain/{id}/{status}")
	@ResponseBody
	public Object bindomain(@PathVariable Long id,@PathVariable Integer status) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(id))) {
				return new ExtReturn(false, "域名主键不能为空！");
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("urlid", id);
			ProxyDomian entity = proxyDomianService.query(params);
			if(null == entity || "".equals(entity)){
				return new ExtReturn(false, "您要修改的域名不存在，请刷新后重试！");
			}
			if(status==1){
				entity.setStatus(0);
			}else{
				entity.setStatus(1);
			}
			if (proxyDomianService.saveOrUpdate(entity)) {
				return new ExtReturn(true, "修改域名绑定状态成功！");
			} else {
				return new ExtReturn(false, "修改域名绑定状态失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	/**
	 * 查询代理下线会员数据
	 * @param startDate
	 * @param endDate
	 * @param account
	 * @param thisPage
	 * @param pageSize
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryDownuser")
	public @ResponseBody
	Object queryDownuser(
			@RequestParam(value = "uiid", required = false) String uiid,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "start", required = false) Integer thisPage,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String,Object> params=new HashMap<String,Object>();
			if(!StringUtils.isBlank(uiid)){
				params.put("uiid", uiid);
			}
			if(!StringUtils.isBlank(account)){
				params.put("account", account);
			}
			if(!StringUtils.isBlank(startDate)){
				params.put("startDate", startDate + " 00:00:00");
			}else{
				String startt =   DateUtil2.getFirstDay(DateUtil2.format(new Date()));
				params.put("startDate", startt+" 00:00:00");
			}
			if(!StringUtils.isBlank(endDate)){
				params.put("endDate", endDate + " 23:59:59");
			}else{
				String endt = DateUtil2.format(new Date());
				params.put("endDate", endt+" 23:59:59");
			}
			System.out.println("开始时间："+params.get("startDate"));
			System.out.println("结束时间："+params.get("endDate"));
			params.put("uiid", uiid);
			Long count = proxyManageService.selectProxyDownUserCount(params);
			List<BetLogTotal> list = proxyManageService.selectProxyDownUser(params, thisPage, pageSize);
			return new GridPanel(count, list, true);
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
		
	}
	
	/**
	 * 查询需要结算的代理
	 * @return
	 */
	@RequestMapping(value = "/queryClearing")
	public @ResponseBody
	Object queryClearing(
			@RequestParam(value = "uiid", required = false) String uiid,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "start", required = false) Integer thisPage,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String,Object> params=new HashMap<String,Object>();
			if(!StringUtils.isBlank(uiid)){
				params.put("uiid", uiid);
			}
			if(!StringUtils.isBlank(account)){
				params.put("account", account);
			}
			if(!StringUtils.isBlank(startDate)){
				params.put("startDate", DateUtil2.getFirstDay(startDate) + " 00:00:00");
			}else{
				String startt =   DateUtil2.getFirstDay(DateUtil2.format(new Date()));
				params.put("startDate", startt+" 00:00:00");
			}
			if(!StringUtils.isBlank(endDate)){
				params.put("endDate", DateUtil2.getEndDay(endDate) + " 23:59:59");
			}else{
				String endt = DateUtil2.format(new Date());
				params.put("endDate", endt+" 23:59:59");
			}
			params.put("month", DateUtil2.getYMd("M", params.get("startDate").toString()));
			params.put("year", DateUtil2.getYMd("Y", params.get("startDate").toString()));
			params.put("flag", "1");//查询可以结算的注单
			List<BetClearing> list = betLogService.getBetClearing(params);
			Integer count = (list !=null) ? list.size() : 0;
			return new GridPanel(count.longValue(), list, true);
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	
	
	/**
	 * 结算代理数据
	 * @param uiid
	 * @param startDate
	 * @param endDate
	 * @param account
	 * @param thisPage
	 * @param pageSize
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/clearing")
	public @ResponseBody
	Object clearing(
			@RequestParam(value = "uiid", required = false) String uiid,
			@RequestParam(value = "proxys", required = false) String proxys,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "start", required = false) Integer thisPage,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String,Object> params=new HashMap<String,Object>();
			if(!StringUtils.isBlank(uiid)){
				params.put("uiid", uiid);
			}
			if(!StringUtils.isBlank(account)){
				params.put("account", account);
			}
			if(!StringUtils.isBlank(startDate)){
				params.put("startDate", DateUtil2.getFirstDay(startDate) + " 00:00:00");
			}else{
				String startt =   DateUtil2.getFirstDay(DateUtil2.format(new Date()));
				params.put("startDate", startt+" 00:00:00");
			}
			if(!StringUtils.isBlank(endDate)){
				params.put("endDate", DateUtil2.getEndDay(endDate) + " 23:59:59");
			}else{
				String endt = DateUtil2.format(new Date());
				params.put("endDate", endt+" 23:59:59");
			}
			int today = DateUtil2.getYMd("D");//获取系统当前日
			int day = Integer.valueOf(prop.getProperty("proxy.clearing.day"));//获取指定结算日
			if(today != day){
				return new ExtReturn(false, "您好，代理结算是每月5号结算上月数据，现在还没有到结算日期。");
			}
			int toMonth = DateUtil2.getYMd("M");//获取系统当前月
			int toYear = DateUtil2.getYMd("Y");//获取系统当前年
			int selectMonth = DateUtil2.getYMd("M", startDate);//获取要结算的月份
			int selectYear = DateUtil2.getYMd("Y", startDate);//获取要结算的年份
			if(selectMonth >= toMonth && selectYear == toYear){//结算月份大于或者等于当前月不能结算
				return new ExtReturn(false, "您好，现在是"+toMonth+"月份，还不能结算本月数据。");
			}
			if(StringUtils.isBlank(proxys)){
				return new ExtReturn(false, "系统异常：结算参数为空。");
			}
			String[] p = proxys.split(",");
			if(p.length <=0){
				return new ExtReturn(false, "请至少选择一个代理进行结算佣金。");
			}
			System.out.println("结算开始时间："+params.get("startDate"));
			System.out.println("结算结束时间："+params.get("endDate"));
			params.put("clearingInfo", p);
			params.put("selectMonth", selectMonth);
			params.put("selectYear", selectYear);
			System.out.println("proxys->"+proxys);
			proxyClearingService.clearing2(params);
			return new ExtReturn(true, "结算成功。");
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	/**
	 * 会员转代理
	 * @param account  会员账号
	 * @return
	 */
	@RequestMapping(value = "/memberSetProxy")
	public @ResponseBody
	Object memberSetProxy(@RequestParam(value = "memberAccount", required = false) String account){
		try {
			boolean flag=memberInfoService.isExistByAcc(account);
			if(!flag){
				return new ExtReturn(true, "会员账号不存在!");
			}
			List<MemberInfo> memberlist=memberInfoService.queryMemberInfoByAccount(account);
			if(memberlist!=null&&memberlist.size()>0){
				MemberInfo memberinfo=memberlist.get(0);
				if(memberinfo.getStatus()==0){
					return new ExtReturn(true, "会员账号已被禁用!");
				}
				if(memberinfo.getAccounttype()==1){
					return new ExtReturn(true, "已是代理用户!");
				}
				memberinfo.setAccounttype(1);
				memberInfoService.modifyMemberInfo(memberinfo);
			}
			return new ExtReturn(true, "设置成功!");
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	/**
	 * 查询代理洗码查询
	 * @param uiid
	 * @param startDate
	 * @param endDate
	 * @param account
	 * @param thisPage
	 * @param pageSize
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryXima")
	public @ResponseBody
	Object queryXima(
			@RequestParam(value = "uiid", required = false) String uiid,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "start", required = false) Integer thisPage,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String,Object> params=new HashMap<String,Object>();
			String day = DateUtil2.format(new Date());
			if(!StringUtils.isBlank(uiid)){
				params.put("uiid", uiid);
			}
			if(!StringUtils.isBlank(account)){
				params.put("account", account);
			}
			if(!StringUtils.isBlank(startDate)){
				params.put("startDate", startDate + " 00:00:00");
				params.put("endDate", startDate+" 23:59:59");
				params.put("jstime", startDate);
			}else{
				params.put("startDate", day+" 00:00:00");
				params.put("endDate", day+" 23:59:59");
				params.put("jstime", day);
			}
			params.put("flag", "1");//查询可结算的注单数据
			List<BetClearing> list = betLogService.selectProxyXima(params);
			Integer count = (list !=null) ? list.size() : 0;
			return new GridPanel(count.longValue(), list, true);
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping(value = "/xima")
	public @ResponseBody
	Object xima(
			@RequestParam(value = "uiid", required = false) String uiid,
			@RequestParam(value = "proxys", required = false) String proxys,
			@RequestParam(value = "clearingType", required = false) String clearingType,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "start", required = false) Integer thisPage,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String,Object> params=new HashMap<String,Object>();
			String day = DateUtil2.format(new Date());
			if(!StringUtils.isBlank(uiid)){
				params.put("uiid", uiid);
			}
			if(!StringUtils.isBlank(clearingType)){
				params.put("clearingType", clearingType);
			}
			if(!StringUtils.isBlank(account)){
				params.put("account", account);
			}
			if(!StringUtils.isBlank(startDate)){
				params.put("startDate", startDate + " 00:00:00");
				params.put("endDate", startDate+" 23:59:59");
				params.put("jstime", startDate);
			}else{
				params.put("startDate", day+" 00:00:00");
				params.put("endDate", day+" 23:59:59");
				params.put("jstime", day);
			}
			
			if(startDate.equals(day)){
				return new ExtReturn(false, "您好，代理洗码只能选择洗前一天的。");
			}
			
			if(StringUtils.isBlank(proxys)){
				return new ExtReturn(false, "没有可结算的代理数据。");
			}
			String[] p = proxys.split(",");
			if(p.length <=0){
				return new ExtReturn(false, "请至少选择一个代理进行结算洗码。");
			}
			System.out.println("结算开始时间："+params.get("startDate"));
			System.out.println("结算结束时间："+params.get("endDate"));
			params.put("clearingInfo", p);
			proxyClearingService.xima(params);
			return new ExtReturn(true, "结算成功。");
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping(value = "/saveProxyInfo")
	@ResponseBody
	public Object saveProxyInfo(@ModelAttribute ProxyInfo proxyInfo,
			HttpServletRequest request, HttpServletResponse response) {
//		try {
//			Date date = new Date();
//			if (!StringUtils.isNotBlank(ObjectUtils.toString(proxyInfo))) {
//				return new ExtReturn(false, "代理平台不能为空！");
//			}
//			if (!StringUtils.isNotBlank(proxyInfo.getName())) {
//				return new ExtReturn(false, "代理名称不能为空！");
//			}
//
//			if (StringUtils.isNotBlank(ObjectUtils.toString(proxyInfo.getPyid()))) {
//				proxyInfo.setCreatetime(date);
//			} else {
//				proxyInfo.setIsmember(1); //1允许会员、
//				proxyInfo.setProxynumber(0);
//				proxyInfo.setMembernumber(0);
//				proxyInfo.setProxystatus(1);
//				proxyInfo.setCreatetime(date);
//				proxyInfo.setLasttime(date);
//			}
//			if (iProxyInfoService.saveOrUpdateProxyInfo(proxyInfo)) {
//				return new ExtReturn(true, "保存成功！");
//			} else {
//				return new ExtReturn(false, "保存失败！");
//			}
//		} catch (Exception e) {
//			logger.error("Exception: ", e);
//			return new ExceptionReturn(e);
//		}
		return null;
	}

	@RequestMapping("/delProxyInfo/{id}")
	@ResponseBody
	public Object delProxyInfo(@PathVariable Long id) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(id))) {
				return new ExtReturn(false, "代理主键不能为空！");
			}
			if (iProxyInfoService.deleteProxyInfo(id)) {
				return new ExtReturn(true, "删除成功！");
			} else {
				return new ExtReturn(false, "删除失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	/*==========================代理报表===============================*/
	@RequestMapping(value = "/proxyfrom")
	public String proxyfrom(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		return "manage/proxy/proxyreport";
	}
	/**
	 * 查询代理详细报表
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/proxyfrominfo")
	public String proxyfrominfo(
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "startdate", required = false) String startdate,
			@RequestParam(value = "enddate", required = false) String enddate,
			HttpServletRequest request, HttpServletResponse response){
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(StringUtils.isNotBlank(id)){
				params.put("proxyuid", id);
			}
			if(StringUtils.isNotBlank(startdate)){
				params.put("startdate", startdate);
				params.put("startDate", startdate);
			}else{
				params.put("startdate", DateUtil2.getFirstDay(DateUtil2.format(new Date())));
				params.put("startDate", DateUtil2.getFirstDay(DateUtil2.format(new Date())));
			}
			if(StringUtils.isNotBlank(enddate)){
				params.put("enddate", enddate);
				params.put("endDate", enddate);
			}else{
				params.put("enddate", DateUtil2.format(new Date()));
				params.put("endDate", DateUtil2.format(new Date()));
			}
			ProxyReportEntity proxyEntity = iProxyInfoService.getProxyFrom(params);
			request.setAttribute("proxyobj", proxyEntity);
			request.setAttribute("proxyuid", id);
			request.setAttribute("startdate", params.get("startdate").toString());
			request.setAttribute("enddate", params.get("enddate").toString());
		} catch (Exception e) {
			logger.error("获取代理报表异常："+id, e);
		}
		return "manage/proxy/proxyreportInfo";
	}

}
