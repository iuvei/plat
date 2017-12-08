package com.gameportal.web.user.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.web.game.model.GamePlatform;
import com.gameportal.web.game.service.IGamePlatformService;
import com.gameportal.web.redis.service.IRedisService;
import com.gameportal.web.user.model.BetLogTotal;
import com.gameportal.web.user.model.MemberXimaDetail;
import com.gameportal.web.user.model.MemberXimaMain;
import com.gameportal.web.user.model.PayOrder;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.user.service.IMemberXimaDetailService;
import com.gameportal.web.user.service.IMemberXimaMainService;
import com.gameportal.web.user.service.IReportQueryService;
import com.gameportal.web.util.CookieUtil;
import com.gameportal.web.util.DateUtil2;
import com.gameportal.web.util.PageTool;

import net.sf.json.JSONObject;

/**
 * 自助洗码
 * @author leron
 *
 */
@Controller
@RequestMapping(value = "/manage/helpxima")
public class SelfhelpXimaCenterController{
	private static final Logger logger = Logger
			.getLogger(SelfhelpXimaCenterController.class);
	
	@Resource(name = "memberXimaMainServiceImpl")
	private IMemberXimaMainService memberXimaMainServiceImpl;
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService = null;
	@Resource(name = "memberXimaDetailServiceImpl")
	private IMemberXimaDetailService memberXimaDetailServiceImpl;
	@Resource(name = "gamePlatformServiceImpl")
	private IGamePlatformService gamePlatformService = null;
	@Resource(name="reportQueryServiceImpl")
	private IReportQueryService reportQueryService;
	
	private static final int outinRecordPageSize = 10;

	/**
	 * 查询会员洗码记录
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @param pageNo    分页
	 * @param request  
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryMemberXimaMain")
	public String queryMemberXimaMain(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "pageNo", required = false) Integer pageNo,
			HttpServletRequest request,HttpServletResponse response) {
		pageNo = StringUtils.isNotBlank(ObjectUtils.toString(pageNo)) ? pageNo : 1;
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		UserInfo userInfo = iRedisService.getRedisResult(vuid
				+ "GAMEPORTAL_USER", UserInfo.class);
		if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
			Map<String, Object> params=new HashMap<String, Object>();
			params.put("account", userInfo.getAccount());
			if (null != startDate) {
				params.put("ymdstart", startDate);
			}
			if (null != endDate) {
				params.put("ymdend", endDate);
			}
			params.put("sortColumns", "updatetime DESC");
			params.put("limitParams", (pageNo-1)*outinRecordPageSize+","+outinRecordPageSize);
			List<MemberXimaMain> memberXimaList=memberXimaMainServiceImpl.queryMemberXimaMainList(params);
			long count=memberXimaMainServiceImpl.queryMemberXimaMainCount(params);	
			long pageCount = PageTool.getPage(count, outinRecordPageSize);
			request.setAttribute("total", count);
			request.setAttribute("pageCount", pageCount);
			request.setAttribute("pageNo", pageNo);
			request.setAttribute("memberXimaList", memberXimaList);
			request.setAttribute("startDate", startDate);
			request.setAttribute("endDate", endDate);
		}
		return "/manage/helpcenter/memberXimaMain";
	}
	
	@RequestMapping(value = "/queryMemberXimaMainDetail")
	public String queryMemberXimaMainDetail(
			@RequestParam(value = "pageNo", required = false) Integer pageNo,
			HttpServletRequest request,HttpServletResponse response) {
		pageNo = StringUtils.isNotBlank(ObjectUtils.toString(pageNo)) ? pageNo : 1;
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		UserInfo userInfo = iRedisService.getRedisResult(vuid
				+ "GAMEPORTAL_USER", UserInfo.class);
		if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
			Map<String, Object> params=new HashMap<String, Object>();
			params.put("uiid", userInfo.getUiid());
			params.put("sortColumns", "opttime DESC");
			params.put("limitParams", (pageNo-1)*outinRecordPageSize+","+outinRecordPageSize);
			List<MemberXimaDetail> memberXimaList=memberXimaDetailServiceImpl.queryMemberXimaDetailList(params);
			long count=memberXimaDetailServiceImpl.queryMemberXimaDetailCount(params);
			long pageCount = PageTool.getPage(count, outinRecordPageSize);
			request.setAttribute("total", count);
			request.setAttribute("pageCount", pageCount);
			request.setAttribute("pageNo", pageNo);
			request.setAttribute("memberXimaDetail", memberXimaList);
		}
		return "/manage/helpcenter/memberXimaDetail";
	}
	
	/**
	 * PT可洗码数据
	 * @param startDate
	 * @param endDate
	 * @param gamecode
	 * @param gamename
	 * @param pageNo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryPTMemberXima")
	public String queryPTMemberXima(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "gamecode", required = false) String gamecode,
			@RequestParam(value = "platformcode", required = false) String gamename,
			@RequestParam(value = "pageNo", required = false) Integer pageNo,
			HttpServletRequest request,HttpServletResponse response) {
		pageNo = StringUtils.isNotBlank(ObjectUtils.toString(pageNo)) ? pageNo : 1;
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		UserInfo userInfo = iRedisService.getRedisResult(vuid
				+ "GAMEPORTAL_USER", UserInfo.class);
		if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
			Map<String,Object> map=new HashMap<String,Object>();
			 Date date = new Date();
			 String day = startDate;//yyyy-MM-dd
		   if(!StringUtils.isBlank(startDate)){
			   map.put("starttime", startDate +" 00:00:00");
			   map.put("endtime", startDate + " 23:59:59");
			   map.put("jstime", day);
		   }else{
			   map.put("starttime", DateUtil2.format(date) + " 00:00:00");
			   map.put("endtime", DateUtil2.format(date) + " 23:59:59");
			   map.put("jstime", DateUtil2.format(date));
		   }
		   if(!StringUtils.isBlank(gamecode)){
			   map.put("gamecode", gamecode);
		   }
		   if(!StringUtils.isBlank(gamename)){
			   map.put("platformcode", "("+gamename+")");
			   map.put("platname", "("+gamename+")");
		   }else{
			   map.put("platformcode", "('PT')");
			   map.put("platname", "('PT')");
		   }
		   map.put("account", userInfo.getAccount());  
		   map.put("groupColumns", "bet.account");
		   map.put("flag", "1");//只查询可以洗码的注单
		   map.put("limitParams", (pageNo-1)*outinRecordPageSize+","+outinRecordPageSize);
		   List<BetLogTotal> list= memberXimaMainServiceImpl.queryXimaBetTotal(map);
		   long count=memberXimaMainServiceImpl.queryXimaBetLogCount(map);
		   long pageCount = PageTool.getPage(count, outinRecordPageSize);
		   request.setAttribute("startDate", day);
		   request.setAttribute("total", count);
		   request.setAttribute("pageCount", pageCount);
		   request.setAttribute("pageNo", pageNo);
		   request.setAttribute("betLogTotal", list);
		}
		return "/manage/helpcenter/memberXimaPTClearing";
	}
	
	/**
	 * PT可洗码数据
	 * @param startDate
	 * @param endDate
	 * @param gamecode
	 * @param gamename
	 * @param pageNo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryAGMemberXima")
	public String queryAGMemberXima(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "gamecode", required = false) String gamecode,
			@RequestParam(value = "platformcode", required = false) String gamename,
			@RequestParam(value = "pageNo", required = false) Integer pageNo,
			HttpServletRequest request,HttpServletResponse response) {
		pageNo = StringUtils.isNotBlank(ObjectUtils.toString(pageNo)) ? pageNo : 1;
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		UserInfo userInfo = iRedisService.getRedisResult(vuid
				+ "GAMEPORTAL_USER", UserInfo.class);
		if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
			Map<String,Object> map=new HashMap<String,Object>();
			 Date date = new Date();
			 String day = startDate;//yyyy-MM-dd
		   if(!StringUtils.isBlank(startDate)){
			   map.put("starttime", startDate +" 00:00:00");
			   map.put("endtime", startDate + " 23:59:59");
			   map.put("jstime", day);
		   }else{
			   map.put("starttime", DateUtil2.format(date) + " 00:00:00");
			   map.put("endtime", DateUtil2.format(date) + " 23:59:59");
			   map.put("jstime", DateUtil2.format(date));
		   }
		   if(!StringUtils.isBlank(gamecode)){
			   map.put("gamecode", gamecode);
		   }
		   if(!StringUtils.isBlank(gamename)){
			   StringBuffer sb=new StringBuffer();
			   String [] gamenames=gamename.split(",");
			   for (String string : gamenames) {
				   sb.append("'"+string+"',");
			   }
			   map.put("platformcode", "("+sb.toString().substring(0,sb.toString().length()-1)+")");
			   map.put("platname", "("+sb.toString().substring(0,sb.toString().length()-1)+")");
			   map.put("platforms", gamename);
		   }else{
			   map.put("platformcode", "('AG','AGIN','BBIN','MG')");
			   map.put("platname", "('AG','AGIN','BBIN','MG')");
			   map.put("platforms", "AG,AGIN,BBIN,MG");
		   }
		   map.put("account", userInfo.getAccount());  
		   map.put("groupColumns", "bet.account,bet.platformcode,bet.gamecode");
		   map.put("flag", "1");//只查询可以洗码的注单
		   map.put("limitParams", (pageNo-1)*outinRecordPageSize+","+outinRecordPageSize);
		   List<BetLogTotal> list= memberXimaMainServiceImpl.queryXimaBetTotal(map);
		   long count=memberXimaMainServiceImpl.queryXimaBetLogCount(map);
		   long pageCount = PageTool.getPage(count, outinRecordPageSize);
		   Map<String, Object> params=new HashMap<String, Object>();
		   params.put("status", 1);
		   List<GamePlatform> gamePlatform=gamePlatformService.queryGame(params);
		   if (null != gamePlatform && gamePlatform.size() > 0) {
			   	String platforms=(String) map.get("platforms");
				String [] platformArr=platforms.split(",");
				StringBuffer pt = new StringBuffer();
				for (GamePlatform gp : gamePlatform) {
					if(!"PT".equals(gp.getGpname())){
						boolean flag=false;
						for (String string : platformArr) {
							if(string.equals(gp.getGpname())){
								flag=true;
							}
						}
						if(flag){
							pt.append("<input type='checkbox' checked name='clearnPlat' value='");
						}else{
							pt.append("<input type='checkbox' name='clearnPlat' value='");
						}
						pt.append(gp.getGpid()).append("#").append(gp.getGpname()).append("'/>");
						pt.append(gp.getGpname());
					}
				}
				request.setAttribute("gamePlatform", pt.toString());
			}
		   request.setAttribute("startDate", day);
		   request.setAttribute("total", count);
		   request.setAttribute("pageCount", pageCount);
		   request.setAttribute("pageNo", pageNo);
		   request.setAttribute("betLogTotal", list);
		}
		return "/manage/helpcenter/memberXimaAGClearing";
	}
	
	/**
	 * 结算洗码
	 * @return
	 */
	@RequestMapping(value = "/saveMemberXimaMain")
	@ResponseBody
	public Object saveMemberXimaMain(
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "gameplat", required = false) String gameplat,
			HttpServletRequest request, HttpServletResponse response){
		JSONObject json=new JSONObject();
		try {
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			UserInfo userInfo = iRedisService.getRedisResult(vuid
					+ "GAMEPORTAL_USER", UserInfo.class);
			if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
				String today = DateUtil2.format(new Date());
				String cleanStr="2015-12-20";
				int hour=DateUtil2.getHour(new Date());
				Date cleanDate=DateUtil2.convert2Date(cleanStr, "yyyy-MM-dd");//最早结算日期
				Date cleanDate2=DateUtil2.convert2Date(startTime, "yyyy-MM-dd");//选择的日期
				if(!StringUtils.isNotBlank(startTime)){
					json.put("code", "0");
					json.put("msg", "请选择要结算的日期!");
					return json.toString();
				}
				if(startTime.equals(today)){
					json.put("code", "0");
					json.put("msg", "您选择的结算日期为："+today+",不能结算当天数据,请明天结算今天数据。");
					return json.toString();
				}
				if(cleanDate.after(cleanDate2)){
					json.put("code", "0");
					json.put("msg", "请选择 2015年12月20日 之后的数据进行结算。");
					return json.toString();
				}
				if(hour<12){
					json.put("code", "0");
					json.put("msg", "请您在今天中午12点之后在进行结算。");
					return json.toString();
				}
				String dateStr ="2016-01-31"; // 2月1日前没有领取救援金，可以领取洗码
				if(startTime.compareTo(dateStr)<=0){
					String date=DateUtil2.convert2Str(DateUtil2.getAddDate(DateUtil2.convert2Date(startTime, "yyyy-MM-dd"), 1),"yyyy-MM-dd");
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("uiid", userInfo.getUiid());
					map.put("ordertype", 7); // 救援金
					map.put("status", 3);
					map.put("startTime", date+" 00:00:00");
					map.put("endTime", date+" 23:59:59");
					List<PayOrder> payOrders = reportQueryService.queryPayOrder(map);
					if(CollectionUtils.isNotEmpty(payOrders)){
						json.put("code", "0");
						json.put("msg", "您选择的日期 '"+startTime+"' 次日已经领取过救援金,无法给您结算洗码");
						return json.toString();
					}
				}
				Map<String, Object> params=new HashMap<String, Object>();
				params.put("account", userInfo.getAccount());
				String[] pcode = gameplat.split(",");
				StringBuffer sb = new StringBuffer();
				for(int i = 0;i < pcode.length ;i++){
					String[] k_v = pcode[i].split("#");
					sb.append("'").append(k_v[1]).append("'");
					if((i+1) < pcode.length){
						sb.append(",");
					}
				}
				params.put("optuiid", userInfo.getUiid());
				params.put("optuname", userInfo.getUname());
				params.put("ymdstart", startTime+" 00:00:00");
				params.put("ymdend", startTime+ " 23:59:59");
				params.put("jstimes", startTime);
				params.put("gameplat", gameplat);
				params.put("platformcodeparams", "("+sb.toString()+")");
				String result = memberXimaMainServiceImpl.saveMemberXima(params);
				if(StringUtils.isNotEmpty(result)){
					if("2000".equals(result)){
						json.put("code", "1");
						json.put("msg", "洗码成功,请注意您钱包余额变化!");
						return json.toString();
					}else if("2001".equals(result)){
						json.put("code", "0");
						json.put("msg", "请您选择游戏平台再洗码!");
						return json.toString();
					}else if("2002".equals(result)){
						json.put("code", "0");
						json.put("msg", "您在 '"+startTime+"' 这天没有投注记录!");
						return json.toString();
					}else if("2003".equals(result)){
						json.put("code", "0");
						json.put("msg", "您在 '"+startTime+"' 的洗码金额为0");
						return json.toString();
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			json.put("code", "0");
			json.put("msg", "网络异常!请重试");
			return json.toString();
		}
		return json.toString();
	}
	
}
