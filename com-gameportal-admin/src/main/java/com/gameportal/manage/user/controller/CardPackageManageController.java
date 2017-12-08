package com.gameportal.manage.user.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.manage.member.model.MemberInfo;
import com.gameportal.manage.member.service.IMemberInfoService;
import com.gameportal.manage.order.model.CompanyCard;
import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.system.model.SystemRole;
import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.system.service.ISystemService;
import com.gameportal.manage.user.model.CardPackage;
import com.gameportal.manage.user.service.ICardPackageService;
import com.gameportal.manage.util.CookieUtil;
import com.gameportal.manage.util.WebConstants;


@Controller
@RequestMapping(value = "/manage/cardpackag")
public class CardPackageManageController {

	@Resource(name = "cardPackageServiceImpl")
	private ICardPackageService iCardPackageService = null;
	
	@Resource(name = "redisServiceImpl")
	private IRedisService redisService=null;
	
	@Resource(name = "systemServiceImpl")
	private ISystemService systemService = null;
	
	@Resource(name = "memberInfoServiceImpl")
	private IMemberInfoService memberInfoService = null;
	
	public CardPackageManageController() {
		super();
	}
	
	private static final Logger logger = Logger
			.getLogger(CardPackageManageController.class);
	
	@RequestMapping(value = "/index")
	public String index(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		// `status` int(2) default NULL COMMENT '状态 0未锁定 1锁定',
		JSONObject map = new JSONObject();
		map.put("0", "未锁定");
		map.put("1", "锁定");
		request.setAttribute("statusMap", map.toString());
		JSONObject bankNameMap = new JSONObject();
		bankNameMap.put("工商银行","工商银行");
		bankNameMap.put("建设银行","建设银行");
		bankNameMap.put("农业银行","农业银行");
		bankNameMap.put("中国银行","中国银行");
		bankNameMap.put("交通银行","交通银行");
		bankNameMap.put("平安银行","平安银行");
		bankNameMap.put("中信银行","中信银行");
		bankNameMap.put("兴业银行","兴业银行");
		bankNameMap.put("招商银行","招商银行");
		bankNameMap.put("浦发银行","浦发银行");
		bankNameMap.put("华夏银行","华夏银行");
		bankNameMap.put("广东发展银行","广东发展银行");
		bankNameMap.put("深圳发展银行","深圳发展银行");
		bankNameMap.put("中国光大银行","中国光大银行");
		bankNameMap.put("中国民生银行","中国民生银行");
		bankNameMap.put("南京银行","南京银行");
		bankNameMap.put("江苏银行","江苏银行");
		bankNameMap.put("中国邮政储蓄银行","中国邮政储蓄银行");
		bankNameMap.put("农村信用社","农村信用社");
		request.setAttribute("bankNameMap", bankNameMap.toString());
		//查询权限
		int isAdmin = 0; //是否是管理员登录0否1是
		int isFK =0;
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + WebConstants.FRAMEWORK_USER;
		Class<Object> c = null;
		SystemUser systemUser = (SystemUser) redisService.getRedisResult(
				key, c);
		if (null != systemUser) {
			List<SystemRole> roleList = systemService
					.queryRoleByUserId(systemUser.getUserId());
			for (SystemRole sr : roleList) {
				if(-1 != sr.getRoleName().indexOf("管理员")){
					isAdmin = 1;
					break;
				} else if (-1 != sr.getRoleName().indexOf("风控")) {
					isFK = 1;
				}
			}
		}
		request.setAttribute("isAdmin", isAdmin);
		request.setAttribute("isFK", isFK);
		return "manage/payorder/cardpackage";
	}
	
	
	@RequestMapping("/del/{cpid}")
	@ResponseBody
	public Object delUserInfo(@PathVariable Long cpid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(cpid))) {
				return new ExtReturn(false, "个人银行卡主键不能为空！");
			}
			if (iCardPackageService.deleteCardPackage(cpid)) {
				return new ExtReturn(true, "删除成功！");
			} else {
				return new ExtReturn(false, "删除失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping(value = "/queryCardPackage")
	public @ResponseBody
	Object queryMember(
			@RequestParam(value = "openingbank", required = false) String ccno,
			@RequestParam(value = "bankcard", required = false) String bankcard,
			@RequestParam(value = "uname", required = false) String uname,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(ccno)) {
			params.put("account", ccno);
		}
		if (StringUtils.isNotBlank(uname)) {
			params.put("accountname", uname);
		}
		if (StringUtils.isNotBlank(bankcard)) {
			params.put("cardnumber", bankcard);
		}
		params.put("sortColumns", "update_date desc");
		Long count = iCardPackageService.queryCardPackageCount(params);
		List<CardPackage> list = iCardPackageService.queryCardPackage(params,
				startNo, pageSize);
		return new GridPanel(count, list, true);
	}
	
	@RequestMapping("/enable/{cpid}")
	@ResponseBody
	public Object enableSmsPlatAccount(@PathVariable Long cpid, HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(cpid))) {
				return new ExtReturn(false, "公司银行卡主键不能为空！");
			}
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;

			if (iCardPackageService.updateStatus(cpid, 0)) {
				return new ExtReturn(true, "锁定成功！");
			} else {
				return new ExtReturn(false, "锁定失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	
	@RequestMapping("/disable/{cpid}")
	@ResponseBody
	public Object disableSmsPlatAccount(@PathVariable Long cpid, HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(cpid))) {
				return new ExtReturn(false, "银行卡主键不能为空！");
			}
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			if (iCardPackageService.updateStatus(cpid, 1)) {
				return new ExtReturn(true, "解锁成功！");
			} else {
				return new ExtReturn(false, "解锁失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping(value = "/save")
	@ResponseBody
	public Object save(@ModelAttribute CardPackage bankcard,
			HttpServletRequest request, HttpServletResponse response){
		try {
			Map<String,Object> map=new HashMap<String,Object>();
			if(StringUtils.isNotBlank(bankcard.getAccount())){
				map.put("account", bankcard.getAccount());
			}
			Timestamp date = new Timestamp(new Date().getTime());
			MemberInfo memberinfo = memberInfoService.qeuryMemberInfo(map);
			if(null == memberinfo || "".equals(memberinfo)){
				return new ExtReturn(false, "您输入的【"+bankcard.getAccount()+"】账号不存在。");
			}
			bankcard.setUiid(memberinfo.getUiid());
			if(null == bankcard.getCpid() || "".equals(bankcard.getCpid())){
				bankcard.setCreateDate(date);
				bankcard.setUpdateDate(date);
			}else{
				bankcard.setUpdateDate(date);
			}
			
			if(iCardPackageService.saveOrUpdateCardPackage(bankcard)){
				return new ExtReturn(true, "会员银行卡操作成功！");
			}else{
				return new ExtReturn(false, "会员银行卡操作失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
		
	}
	
}
