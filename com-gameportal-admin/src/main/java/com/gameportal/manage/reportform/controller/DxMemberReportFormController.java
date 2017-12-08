package com.gameportal.manage.reportform.controller;

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

import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.reportform.model.DxMemberReportForm;
import com.gameportal.manage.reportform.service.IDxMemberReportFormService;
import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.util.CookieUtil;
import com.gameportal.manage.util.WebConstants;

/**
 * 电销客户分析表
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/manage/dxMemberReport")
public class DxMemberReportFormController {
	
	@Resource(name="dxMemberReportService")
	private IDxMemberReportFormService dxMemberReportService;
	
	@Resource(name = "redisServiceImpl")
	private IRedisService redisService=null;
	
	public static final Logger logger = Logger
			.getLogger(DxMemberReportFormController.class);

	public DxMemberReportFormController(){
		super();
	}
	
	
	/**
	 * 跳转页面
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/index")
	public String index(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		return "manage/reportform/dxmember";
	}
	
	/**
	 * 电销客户分析表
	 * @param account   会员账号
	 * @param uname     会员名称
	 * @param truename 	电销名称
	 * @param phone 	会员电话
	 * @param qq 		会员QQ
	 * @param email 	会员邮箱
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @param startNo   当前页数
	 * @param pageSize  每页行数
	 * @param request 
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryDxMemberReportResult")
	@ResponseBody
	public Object queryDxMemberReportResult(
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "uname", required = false) String uname,
			@RequestParam(value = "truename", required = false) String truename,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response){
		try{
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) redisService.getRedisResult(
					key, c);
			
			Map<String,Object> params=new HashMap<String,Object>();
			if(StringUtils.isNotBlank(account)){
				params.put("account", account);
			}
			if(!systemUser.getAccount().equals("admin")){ //如果不是admin
				params.put("belongid", systemUser.getUserId());  
			}
			if(StringUtils.isNotBlank(uname)){
				params.put("uname", uname);
			}
			if(StringUtils.isNotBlank(truename)){
				params.put("truename", truename);
			}
			if(StringUtils.isNotBlank(startDate)){
				params.put("startDate", startDate);
			}
			if(StringUtils.isNotBlank(endDate)){
				params.put("endDate", endDate);
			}
			Long count = dxMemberReportService.getDxMemberReportCount(params);
			params.put("sortColumns", " validBetMoney desc");
			List<DxMemberReportForm> list = dxMemberReportService.getDxMemberReportList(params, startNo==null?0:startNo, pageSize==null?30:pageSize);
			return new GridPanel(count, list, true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
		
	}
}
