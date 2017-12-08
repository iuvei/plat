package com.gameportal.manage.reportform.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.proxy.controller.WebProxyApplyController;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.reportform.model.BetReportForm;
import com.gameportal.manage.reportform.model.FavorableReportForm;
import com.gameportal.manage.reportform.model.HyMemberReportForm;
import com.gameportal.manage.reportform.model.PayReportForm;
import com.gameportal.manage.reportform.model.PlatMoneyLog;
import com.gameportal.manage.reportform.model.PlatformReportForm;
import com.gameportal.manage.reportform.model.RegisterReportForm;
import com.gameportal.manage.reportform.model.WithdrawalReportForm;
import com.gameportal.manage.reportform.service.ITotalReportFormService;
import com.gameportal.manage.reportform.service.PlatMoneyLogService;
import com.gameportal.manage.util.DateUtil2;

/**
 * 总报表数据控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/manage/reportFormTotal")
public class ReportFormTotalController {
	
	private static final Logger logger = Logger
			.getLogger(WebProxyApplyController.class);

	@Resource(name = "redisServiceImpl")
	private IRedisService redisService=null;
	
	@Resource(name = "totalReportFormService")
	private ITotalReportFormService totalReportFormService;
	
	@Resource(name="platMoneyLogService")
	private PlatMoneyLogService platMoneyLogService;
	
	/**
	 * 注册统计报表
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/regindex")
	public String regindex(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		return "manage/reportform/totalreportform";
	}
	
	@RequestMapping(value = "/queryRegReport")
	@ResponseBody
	public Object queryRegReport(
			@RequestParam(value = "starttime", required = false) String starttime,
			@RequestParam(value = "endtime", required = false) String endtime,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			String date = DateUtil2.format(new Date());
			Map<String, Object> params = new HashMap<String, Object>();
			if(StringUtils.isNotBlank(starttime)){
				params.put("starttime", starttime);
			}else{
				params.put("starttime", DateUtil2.getFirstDay(date));//本月第一天
			}
			if(StringUtils.isNotBlank(endtime)){
				params.put("endtime", endtime);
			}else{
				params.put("endtime", date);//本月第一天
			}
			List<RegisterReportForm> list = totalReportFormService.getRegister(params);
			long totalCount = 0L;
			JSONObject result = new JSONObject();
			if(null != list && list.size() > 0){
				JSONArray jsonArray = new JSONArray();
				for(RegisterReportForm object : list){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("time", object.getTimes());
					jsonObj.put("count", object.getCounts());
					jsonArray.add(jsonObj);
					totalCount+=object.getCounts();
				}
				result.put("data", jsonArray);
				result.put("totalCount", totalCount);
				result.put("starttime", params.get("starttime"));
				result.put("endtime", params.get("endtime"));
			}
			return result;
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	/**
	 * 投注总数据
	 * @param starttime
	 * @param endtime
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryBetReport")
	@ResponseBody
	public Object queryBetReport(
			@RequestParam(value = "starttime", required = false) String starttime,
			@RequestParam(value = "endtime", required = false) String endtime,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			String s = " 00:00:00";
			String e = " 23:59:59";
			String date = DateUtil2.format(new Date());
			Map<String, Object> params = new HashMap<String, Object>();
			if(StringUtils.isNotBlank(starttime)){
				params.put("starttime", starttime + s);
			}else{
				params.put("starttime", DateUtil2.getFirstDay(date) + s);//本月第一天
			}
			if(StringUtils.isNotBlank(endtime)){
				params.put("endtime", endtime + e);
			}else{
				params.put("endtime", date + e);//本月第一天
			}
			List<BetReportForm> list = totalReportFormService.getTotalBetList(params);
			JSONObject result = new JSONObject();
			if(null != list && list.size() > 0){
				BigDecimal totalBet = new BigDecimal(0d);
				JSONArray jsonArray = new JSONArray();
				for(BetReportForm object : list){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("time", object.getTimes());
					jsonObj.put("betcount", object.getBetmoney());
					if(Double.valueOf(object.getBetmoney()) > 1000000){//大于100万
						jsonObj.put("color", "#FF0000");
					}else{
						jsonObj.put("color", "");
					}
					jsonArray.add(jsonObj);
					totalBet = totalBet.add(new BigDecimal(Double.valueOf(object.getBetmoney())));//计算总投注
				}
				result.put("data", jsonArray);
				result.put("totalBet",totalBet.doubleValue());
				result.put("starttime", params.get("starttime"));
				result.put("endtime", params.get("endtime"));
			}
			return result;
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	/**
	 * 统计充值
	 * @param starttime
	 * @param endtime
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryPayReport")
	@ResponseBody
	public Object queryPayReport(
			@RequestParam(value = "starttime", required = false) String starttime,
			@RequestParam(value = "endtime", required = false) String endtime,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			String s = " 00:00:00";
			String e = " 23:59:59";
			String date = DateUtil2.format(new Date());
			Map<String, Object> params = new HashMap<String, Object>();
			if(StringUtils.isNotBlank(starttime)){
				params.put("starttime", starttime + s);
			}else{
				params.put("starttime", DateUtil2.getFirstDay(date) + s);//本月第一天
			}
			if(StringUtils.isNotBlank(endtime)){
				params.put("endtime", endtime + e);
			}else{
				params.put("endtime", date + e);//本月第一天
			}
			params.put("paytyple", 0);
			params.put("status", 3);
			List<PayReportForm> list = totalReportFormService.getPayReportList(params);
			JSONObject result = new JSONObject();
			if(null != list && list.size() > 0){
				BigDecimal totalPay = new BigDecimal(0d);
				JSONArray jsonArray = new JSONArray();
				for(PayReportForm object : list){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("time", object.getTimes());
					jsonObj.put("paysum", object.getPaymoney());
					if(Double.valueOf(object.getPaymoney()) > 100000){//大于10万
						jsonObj.put("color", "#FF0000");
					}else{
						jsonObj.put("color", "");
					}
					jsonArray.add(jsonObj);
					totalPay = totalPay.add(new BigDecimal(Double.valueOf(object.getPaymoney())));//计算总投注
				}
				result.put("data", jsonArray);
				result.put("totalPay",totalPay.doubleValue());
				result.put("starttime", params.get("starttime"));
				result.put("endtime", params.get("endtime"));
			}
			return result;
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	/**
	 * 统计提款
	 * @param starttime
	 * @param endtime
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryWithdrawalReport")
	@ResponseBody
	public Object queryWithdrawalReport(
			@RequestParam(value = "starttime", required = false) String starttime,
			@RequestParam(value = "endtime", required = false) String endtime,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			String s = " 00:00:00";
			String e = " 23:59:59";
			String date = DateUtil2.format(new Date());
			Map<String, Object> params = new HashMap<String, Object>();
			if(StringUtils.isNotBlank(starttime)){
				params.put("starttime", starttime + s);
			}else{
				params.put("starttime", DateUtil2.getFirstDay(date) + s);//本月第一天
			}
			if(StringUtils.isNotBlank(endtime)){
				params.put("endtime", endtime + e);
			}else{
				params.put("endtime", date + e);//本月第一天
			}
			params.put("paytyple", 1);
			params.put("status", 3);
			List<WithdrawalReportForm> list = totalReportFormService.getWithdrawalReportList(params);
			JSONObject result = new JSONObject();
			if(null != list && list.size() > 0){
				BigDecimal totalTikuan = new BigDecimal(0d);
				JSONArray jsonArray = new JSONArray();
				for(WithdrawalReportForm object : list){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("time", object.getTimes());
					jsonObj.put("withdrawalsum", object.getWithdrawalTotal());
					if(Double.valueOf(object.getWithdrawalTotal()) > 100000){//大于10万
						jsonObj.put("color", "#FF0000");
					}else{
						jsonObj.put("color", "");
					}
					jsonArray.add(jsonObj);
					totalTikuan = totalTikuan.add(new BigDecimal(Double.valueOf(object.getWithdrawalTotal())));//计算总投注
				}
				result.put("data", jsonArray);
				result.put("totalTikuan",totalTikuan.doubleValue());
				result.put("starttime", params.get("starttime"));
				result.put("endtime", params.get("endtime"));
			}
			return result;
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	/**
	 * 统计优惠金额
	 * @param starttime
	 * @param endtime
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryFavorableReport")
	@ResponseBody
	public Object queryFavorableReport(
			@RequestParam(value = "starttime", required = false) String starttime,
			@RequestParam(value = "endtime", required = false) String endtime,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			String s = " 00:00:00";
			String e = " 23:59:59";
			String date = DateUtil2.format(new Date());
			Map<String, Object> params = new HashMap<String, Object>();
			if(StringUtils.isNotBlank(starttime)){
				params.put("starttime", starttime + s);
			}else{
				params.put("starttime", DateUtil2.getFirstDay(date) + s);//本月第一天
			}
			if(StringUtils.isNotBlank(endtime)){
				params.put("endtime", endtime + e);
			}else{
				params.put("endtime", date + e);//本月第一天
			}
			params.put("ordertype", "('1','2','4','5','6')");
			params.put("paytyple", 2);
			params.put("status", 3);
			List<FavorableReportForm> list = totalReportFormService.getFavorableReportList(params);
			JSONObject result = new JSONObject();
			if(null != list && list.size() > 0){
				BigDecimal totalFavorab = new BigDecimal(0d);
				JSONArray jsonArray = new JSONArray();
				for(FavorableReportForm object : list){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("time", object.getTimes());
					jsonObj.put("favorablesum", object.getFavorabTotal());
					if(Double.valueOf(object.getFavorabTotal()) > 10000){//大于10万
						jsonObj.put("color", "#FF0000");
					}else{
						jsonObj.put("color", "");
					}
					jsonArray.add(jsonObj);
					totalFavorab = totalFavorab.add(new BigDecimal(Double.valueOf(object.getFavorabTotal())));//计算总投注
				}
				result.put("data", jsonArray);
				result.put("totalFavorab",totalFavorab.doubleValue());
				result.put("starttime", params.get("starttime"));
				result.put("endtime", params.get("endtime"));
			}
			return result;
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	/**
	 * 统计优惠金额
	 * @param starttime
	 * @param endtime
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryXimaReport")
	@ResponseBody
	public Object queryXimaReport(
			@RequestParam(value = "starttime", required = false) String starttime,
			@RequestParam(value = "endtime", required = false) String endtime,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			String s = " 00:00:00";
			String e = " 23:59:59";
			String date = DateUtil2.format(new Date());
			Map<String, Object> params = new HashMap<String, Object>();
			if(StringUtils.isNotBlank(starttime)){
				params.put("starttime", starttime);
			}else{
				params.put("starttime", DateUtil2.getFirstDay(date));//本月第一天
			}
			if(StringUtils.isNotBlank(endtime)){
				params.put("endtime", endtime);
			}else{
				params.put("endtime", date);//本月第一天
			}
			String responseS = params.get("starttime").toString();
			String responseE = params.get("endtime").toString();
			int poorDay=0;//相差日期
			DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			Date d1 = f.parse(params.get("starttime").toString());//将字符串的开始时间转换成 Date类型
			Date d2 = f.parse(params.get("endtime").toString());//将字符串的结束时间转换成 Date类型
			Calendar c = Calendar.getInstance();
			c.setTime(d1);
			poorDay=DateUtil2.diffDate(d2,d1);
			List<FavorableReportForm> resultList = new ArrayList<FavorableReportForm>();
			for(int i = 0;i<=poorDay;i++){
				if(i==0){
					c.add(Calendar.DAY_OF_WEEK, 0);
	            }else{
	            	c.add(Calendar.DAY_OF_WEEK, 1);
	            }
				String time = DateUtil2.format(c.getTime());
				params.clear();
				params.put("starttime", time + s);
				params.put("endtime", time + e);
				FavorableReportForm favorableReportForm = totalReportFormService.getXimaReportObject(params);
				if(favorableReportForm != null && Double.valueOf(favorableReportForm.getFavorabTotal()) > 0){
					favorableReportForm.setTime(time);
					resultList.add(favorableReportForm);
				}
			}
			JSONObject result = new JSONObject();
			if(null != resultList && resultList.size() > 0){
				BigDecimal totalFavorab = new BigDecimal(0d);
				JSONArray jsonArray = new JSONArray();
				for(FavorableReportForm object : resultList){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("time", object.getTimes());
					jsonObj.put("ximasum", object.getFavorabTotal());
					if(Double.valueOf(object.getFavorabTotal()) > 10000){//大于10万
						jsonObj.put("color", "#FF0000");
					}else{
						jsonObj.put("color", "");
					}
					jsonArray.add(jsonObj);
					totalFavorab = totalFavorab.add(new BigDecimal(Double.valueOf(object.getFavorabTotal())));//计算总投注
				}
				result.put("data", jsonArray);
				result.put("totalXima",com.gameportal.manage.util.StringUtils.convertNumber(totalFavorab.doubleValue()));
				result.put("starttime", responseS);
				result.put("endtime", responseE);
			}
			return result;
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	/**
	 * 统计活跃会员数据
	 * @param starttime
	 * @param endtime
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryHymemberReport")
	@ResponseBody
	public Object queryHymemberReport(
			@RequestParam(value = "starttime", required = false) String starttime,
			@RequestParam(value = "endtime", required = false) String endtime,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			String s = " 00:00:00";
			String e = " 23:59:59";
			String date = DateUtil2.format(new Date());
			Map<String, Object> params = new HashMap<String, Object>();
			if(StringUtils.isNotBlank(starttime)){
				params.put("starttime", starttime);
			}else{
				params.put("starttime", DateUtil2.getFirstDay(date));//本月第一天
			}
			if(StringUtils.isNotBlank(endtime)){
				params.put("endtime", endtime);
			}else{
				params.put("endtime", date);//本月第一天
			}
			int poorDay=0;//相差日期
			DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			Date d1 = f.parse(params.get("starttime").toString());//将字符串的开始时间转换成 Date类型
			Date d2 = f.parse(params.get("endtime").toString());//将字符串的结束时间转换成 Date类型
			Calendar c = Calendar.getInstance();
			c.setTime(d1);
			poorDay=DateUtil2.diffDate(d2,d1);
			List<HyMemberReportForm> resultList = new ArrayList<HyMemberReportForm>();
			StringBuffer hymember = new StringBuffer();
			hymember.append("<chart>");
			hymember.append("<series>");
			for(int i = 0;i<=poorDay;i++){
				if(i==0){
					c.add(Calendar.DAY_OF_WEEK, 0);
	            }else{
	            	c.add(Calendar.DAY_OF_WEEK, 1);
	            }
				String time = DateUtil2.format(c.getTime());
				params.clear();
				params.put("starttime", time + s);
				params.put("endtime", time + e);
				HyMemberReportForm hymemberObject = totalReportFormService.getHymember(params);
				hymember.append("<value xid=\"").append(i).append("\">"+time+"</value>");
				if(hymemberObject != null){
					
					hymemberObject.setTime(time);
					resultList.add(hymemberObject);
				}
			}
			hymember.append("</series>");
			hymember.append("<graphs>");
			hymember.append("<graph title=\"登录会员\" fill_alpha=\"30\" line_width=\"2\" bullet=\"round\" hidden=\"false\" color=\"#FF8080\">");
			int size = 0;
			for(HyMemberReportForm loginObj : resultList){
				hymember.append("<value xid=\"").append(size).append("\">").append(loginObj.getLogincount()).append("</value>");
				size++;
			}
			hymember.append("</graph>");
			hymember.append("<graph title=\"充值会员\" fill_alpha=\"30\" line_width=\"2\" bullet=\"round\" hidden=\"false\" color=\"#009900\">");
			size = 0;
			for(HyMemberReportForm payObj : resultList){
				hymember.append("<value xid=\"").append(size).append("\">").append(payObj.getPaycount()).append("</value>");
				size++;
			}
			hymember.append("</graph>");
			hymember.append("</graphs>");
			hymember.append("</chart>");
			return hymember.toString();
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	/**
	 * 统计平台总金额
	 * @param starttime
	 * @param endtime
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryPlatformReport")
	@ResponseBody
	public Object queryPlatformReport(
			@RequestParam(value = "starttime", required = false) String starttime,
			@RequestParam(value = "endtime", required = false) String endtime,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			Long count=platMoneyLogService.queryPlatMoneyLogCount(params);
			List<PlatMoneyLog> list = platMoneyLogService.querPlatMoneyLog(params, startNo, pageSize);
			return new GridPanel(count, list, true);
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
}
