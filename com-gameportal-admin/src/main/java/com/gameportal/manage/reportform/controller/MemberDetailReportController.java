package com.gameportal.manage.reportform.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.manage.member.model.GameTransfer;
import com.gameportal.manage.reportform.service.IMemberDetailReportService;
import com.gameportal.manage.pay.model.PayOrder;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.xima.model.MemberXimaMain;

/**
 * 会员明细报表控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/manage/memberDetailReport")
public class MemberDetailReportController {
	
	@Resource(name="memberDetailReportService")
	private IMemberDetailReportService memberDetailReportService;
	
	public static final Logger logger = Logger
			.getLogger(MemberDetailReportController.class);

	public MemberDetailReportController(){
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
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("", "全部");
		jsonObj.put("0", "作废");
		jsonObj.put("1", "发起");
		jsonObj.put("2", "处理中");
		jsonObj.put("3", "存取成功");
		jsonObj.put("4", "存取失败");
		request.setAttribute("paystatus", jsonObj.toString());
		return "manage/reportform/memberdetail";
	}
	
	
	/**
	 * 钱包余额
	 * @param account  	   用户账户
	 * @return
	 */
	@RequestMapping(value = "/queryAccountMoney")
	@ResponseBody
	public Object queryAccountMoney(
			@RequestParam(value = "account", required = false) String account){
		
		JSONObject result = new JSONObject();
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("account", account);
		Map<String,Object>  resultMap=memberDetailReportService.getAccountMoney(map);
		if(resultMap==null){
			resultMap=new HashMap<String, Object>();
		}
		if(resultMap.get("totalamount")==null||resultMap.get("totalamount").equals("")){
			resultMap.put("totalamount", 0);
		}
		result.put("accountMoney", resultMap);
		return result;
	}
	
	/**
	 * 存款列表
	 * @param account  会员账号
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @param orderstate 订单状态
	 * @param poid       订单号
	 * @param paytype    订单类型
	 * @param startNo    当前页
	 * @param pageSize   每页行数
	 * @return
	 */
	@RequestMapping(value = "/queryPayMoneyResult")
	@ResponseBody
	public Object queryPayMoneyResult(
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "orderstate", required = false) String orderstate,  
			@RequestParam(value = "poid", required = false) String poid,   
			@RequestParam(value = "ordertype", required = false) String ordertype,   
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize){
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("account", account);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("orderstate", orderstate);
		map.put("poid", poid);
		map.put("ordertype", ordertype);
		map.put("paytype", 0);
		List<PayOrder> list=memberDetailReportService.getPayMoneyList(map, startNo, pageSize);
		Long count=memberDetailReportService.getPayMoneyCount(map);
		return new GridPanel(count, list, true);
	}
	
	/**
	 * 取款列表
	 * @param account  会员账号
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @param orderstate 订单状态
	 * @param poid       订单号
	 * @param paytype    订单类型
	 * @param startNo    当前页
	 * @param pageSize   每页行数
	 * @return
	 */
	@RequestMapping(value = "/queryPickMoneyResult")
	@ResponseBody
	public Object queryPickMoneyResult(
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "orderstate", required = false) String orderstate,  
			@RequestParam(value = "poid", required = false) String poid,   
			@RequestParam(value = "ordertype", required = false) String ordertype,   
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize){
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("account", account);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("orderstate", orderstate);
		map.put("poid", poid);
		map.put("ordertype", ordertype);
		map.put("paytype", 1);
		List<PayOrder> list=memberDetailReportService.getPayMoneyList(map, startNo, pageSize);
		Long count=memberDetailReportService.getPayMoneyCount(map);
		return new GridPanel(count, list, true);
	}
	
	/**
	 * 优惠列表
	 * @param account  会员账号
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @param orderstate 订单状态
	 * @param poid       订单号
	 * @param paytype    订单类型
	 * @param startNo    当前页
	 * @param pageSize   每页行数
	 * @return
	 */
	@RequestMapping(value = "/queryCouponMoneyResult")
	@ResponseBody
	public Object queryCouponMoneyResult(
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "orderstate", required = false) String orderstate,  
			@RequestParam(value = "poid", required = false) String poid,   
			@RequestParam(value = "ordertype", required = false) String ordertype,   
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize){
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("account", account);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("orderstate", orderstate);
		map.put("poid", poid);
		map.put("ordertype", ordertype);
		map.put("paytype", 2);
		map.put("coupon", true); //优惠
		List<PayOrder> list=memberDetailReportService.getPayMoneyList(map, startNo, pageSize);
		Long count=memberDetailReportService.getPayMoneyCount(map);
		return new GridPanel(count, list, true);
	}
	
	/**
	 * 转账列表
	 * @param account 用户账号
	 * @param startNo 当前页
	 * @param pageSize 每页行数
	 * @return
	 */
	@RequestMapping(value = "/queryTransferResult")
	@ResponseBody
	public Object queryTransferResult(
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize){
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("account", account);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		List<GameTransfer> list=memberDetailReportService.getTransferList(map, startNo, pageSize);
		Long count=memberDetailReportService.getTransferCount(map);
		return new GridPanel(count, list, true);
	}
	
	/**
	 * 会员洗码列表
	 * @param account 用户账号
	 * @param startNo 当前页
	 * @param pageSize 每页行数
	 * @return
	 */
	@RequestMapping(value = "/queryMemberXimaResult")
	@ResponseBody
	public Object queryMemberXimaResult(
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize){
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("account", account);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		List<MemberXimaMain> list=memberDetailReportService.getMemberXimaList(map, startNo, pageSize);
		Long count=memberDetailReportService.getMemberXimaCount(map);
		return new GridPanel(count, list, true);
	}
	
}
