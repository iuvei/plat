package com.gameportal.web.home.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.web.adver.service.IAdverService;
import com.gameportal.web.agent.service.IProxyWebSitePvService;
import com.gameportal.web.game.service.IGameTransferService;
import com.gameportal.web.redis.service.IRedisService;
import com.gameportal.web.user.model.AccountMoney;
import com.gameportal.web.user.model.PayOrder;
import com.gameportal.web.user.model.PayOrderLog;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.user.model.UserSign;
import com.gameportal.web.user.service.IReportQueryService;
import com.gameportal.web.user.service.IUserInfoService;
import com.gameportal.web.user.service.IUserPropertyService;
import com.gameportal.web.util.CheckMobile;
import com.gameportal.web.util.CookieUtil;
import com.gameportal.web.util.DateUtil;
import com.gameportal.web.util.GetProperty;
import com.gameportal.web.util.IdGenerator;
import com.gameportal.web.util.JsonUtils;
import com.gameportal.web.util.WebConst;

import net.sf.json.JSONObject;

@Controller
public class HomeController{
    @Resource(name = "redisServiceImpl")
    private IRedisService iRedisService = null;
    @Resource(name = "userInfoServiceImpl")
    private IUserInfoService userInfoService = null;
    @Resource(name="adverServiceImpl")
    private IAdverService adverService;
    @Resource(name="proxyWebSitePvService")
    private IProxyWebSitePvService proxyWebSitePvService;
    @Resource(name="reportQueryServiceImpl")
    private IReportQueryService reportQueryService;
    @Resource(name = "gameTransferServiceImpl")
    private IGameTransferService gameTransferService;
    @Resource(name="userPropertyService")
    private IUserPropertyService userPropertyService;
 

    private static final Logger logger = Logger.getLogger(HomeController.class);

    private static final BigDecimal WIN_AMOUNT = new BigDecimal(2000);//中奖排行榜最低中奖金额
    private static final int LIMIT_NUM = 20;//中奖排行榜共显示条数

    public HomeController() {
        super();
    }

    @RequestMapping(value = "/index")
    public String index(String adLocation,HttpServletRequest request, HttpServletResponse response) {
        // 加载首页广告
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("status", 1);
        request.setAttribute("adverList",adverService.queryAllAdver(paramMap));
        /*****************加载中奖喜讯内容****************/
        ArrayList<String> winRecords_result = iRedisService.getRedisResult("winning_top_data", ArrayList.class);
        if(winRecords_result == null || winRecords_result.size() <= 0){
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("winAmount", WIN_AMOUNT);
            params.put("limitNum", LIMIT_NUM);
            winRecords_result = (ArrayList<String>) userInfoService.selWinningTopBetLog(params);
        }
        request.setAttribute("winRecords", winRecords_result);
        
        request.setAttribute("vuid", CookieUtil.getOrCreateVuid(request, response));
        
        String userAgent =request.getHeader( "USER-AGENT" ).toLowerCase();
    	if(CheckMobile.check(userAgent)){
    		return "/mobile/main";
    	}
        return "/home/index";
    }

    @RequestMapping(value = "/register")
    public String register(
            @RequestParam(value = "c", required = false) String channel,
            @RequestParam(value = "param", required = false) String code,
            HttpServletRequest request,
            HttpServletResponse response) {
        String vuid = CookieUtil.getOrCreateVuid(request, response);
        if(null != code && !"".equals(code)){
            if(WebConst.isInteger(code) && WebConst.isNonnegative(code)){
                UserInfo proxyUser = userInfoService.findProxyInfoId(Long.valueOf(code), 0);
                if(null == proxyUser){
                    code = "0";
                }
            }else{
                code = "0";
            }
        }
        request.setAttribute("channel", channel);
        request.setAttribute("proxyid", code);
        return "/register/index";
    }

    @RequestMapping(value = "/qipaiGame")
    public String qipaiGame(HttpServletRequest request,
            HttpServletResponse response) {
        String vuid = CookieUtil.getOrCreateVuid(request, response);
        return "/home/qipaiGame";
    }

    @RequestMapping(value = "/sportsEvent")
    public String sportsEvent(HttpServletRequest request,
            HttpServletResponse response) {
        String vuid = CookieUtil.getOrCreateVuid(request, response);
        return "/home/sportsEvent";
    }

    @RequestMapping(value = "/liveCasino")
    public String liveCasino(HttpServletRequest request,
            HttpServletResponse response) {
        return "/home/liveCasino";
    }

    @RequestMapping(value = "/electronicGame")
    public String electronicGame(HttpServletRequest request,
            HttpServletResponse response) {
        String vuid = CookieUtil.getOrCreateVuid(request, response);
        return "/home/electronicGame";
    }

    @RequestMapping(value = "/lottery")
    public String lottery(HttpServletRequest request,
            HttpServletResponse response) {
        String vuid = CookieUtil.getOrCreateVuid(request, response);
        return "/home/lottery";
    }

    @RequestMapping(value = "/coumonActivity")
    public String coumonActivity(HttpServletRequest request,
            HttpServletResponse response) {
        String vuid = CookieUtil.getOrCreateVuid(request, response);
        return "/home/coumonActivity";
    }

    @RequestMapping(value = "/integration/integrationRedirect")
    public String integrationRedirect(HttpServletRequest request, HttpServletResponse response) {
        return "/home/pt";
    }
    
    /**
     * 获取签到奖金
     * @param day
     * @return
     */
	public Double getRegReward(int day) {
		double reward = 0;
		if(day <=2){ // 1-2天
			reward=0.5;
		}else if(day ==3){
			reward =1; //天
		}else if(day>=4 && day<=5){  // 4-5天
			reward =1.5;
		}else if(day >=6 && day <=7 ){ // 6-7天
			reward =2;
		}
		return reward;
	}
    
    /**
     * 领取联系签到彩金
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/getSignReward", method = RequestMethod.POST)
	@ResponseBody
	public String getSignReward(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {
			json.put("success", false);
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + "GAMEPORTAL_USER";
			Class<Object> c = null;
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			userInfo = userInfoService.findUserInfoId(userInfo.getUiid());
			Properties prop = GetProperty.getProp("activity.properties");
			Date startDate = DateUtil.getDateByStr(prop.getProperty("activitysign.start.time"));
			if(new Date().before(startDate)){
				json.put("msg", "很抱歉，该活动尚未开始，如有疑问请联系在线客服！");
				return json.toString();
			}
			Date endDate = DateUtil.getDateByStr(prop.getProperty("activitysign.end.time"));
			if (new Date().after(endDate)) {
				json.put("msg", "很抱歉，该活动已结束，如有疑问请联系在线客服！");
				return json.toString();
			}
			// 是否有签到
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("uiid", userInfo.getUiid());
			map.put("starttime", DateUtil.getToday());
			List<UserSign> todaySignList = userInfoService.queryUserSignList(map);
			if(CollectionUtils.isNotEmpty(todaySignList)){
				json.put("msg", "很抱歉，您今日已经签到成功，不能重复签到哦!");
				return json.toString();
			}
			// 存款订单
			map = new HashMap<String, Object>();
			map.put("uiid", userInfo.getUiid());
            map.put("paytyple", 0);
            map.put("status", 3);
            map.put("startDateStr", DateUtil.getToday());
            map.put("endDateStr", DateUtil.getToday());
            long desposit = reportQueryService.sumPayOrder(map);
			if (desposit <100) {
				json.put("msg", "很抱歉，您今日存款总额小于100元，不能进行签到哦!");
				return json.toString();
			}

			map = new HashMap<String, Object>();
			map.put("uiid", userInfo.getUiid());
//			map.put("starttime", DateUtil.FormatDate(DateUtil.getFirstDay(new Date())));
			map.put("starttime", DateUtil.getMondayOfThisWeek());
			map.put("status", 1);
			List<UserSign> signList = userInfoService.queryUserSignList(map);
			int signDay =0;
			if(CollectionUtils.isNotEmpty(signList)){
				map.put("starttime", DateUtil.getYesterday(new Date(), -1)); //判断昨天有没有签到
				List<UserSign> yesterdaySignList = userInfoService.queryUserSignList(map);
				if(CollectionUtils.isNotEmpty(yesterdaySignList)){
					signDay = signList.size();
				}else{
					// 标记之前签到为"无效"状态
					for (UserSign sign : signList) {
						sign.setStatus(0);
						userInfoService.updateUserSign(sign);
					}
					signDay=0;
				}
			}else{
				signDay=0;
			}
			double reward = getRegReward(signDay+1);
//			double reward = (signDay>7?1:0.5);
			map.put("starttime", DateUtil.FormatDate(DateUtil.getFirstDay(new Date())));
			signList =userInfoService.queryUserSignList(map);
			int yesterday= DateUtil.getLastDay(new Date()).getDate()-1; //当月最后一日
			if(signDay >0 && yesterday == (signList.size()+1)){
				reward = 28; //满勤奖
			}

			// 签到礼金
			AccountMoney accountMoney = userInfoService.getAccountMoneyObj(userInfo.getUiid(), 1);
			BigDecimal beforebalance = accountMoney.getTotalamount();
			accountMoney.setTotalamount(new BigDecimal(reward));
			BigDecimal latrbalance = beforebalance.add(new BigDecimal(reward));
			userInfoService.updateTotalamount(accountMoney);
			Timestamp date = new Timestamp(new Date().getTime());
			PayOrder payOrder = new PayOrder();
			payOrder.setPoid(IdGenerator.genOrdId16("REWARD"));
			payOrder.setPlatformorders(IdGenerator.genOrdId16(""));
			payOrder.setUiid(userInfo.getUiid());
			payOrder.setUaccount(userInfo.getAccount());
			payOrder.setUrealname(userInfo.getUname());
			payOrder.setPaytyple(2); // 0存款，1提款，2赠送，3扣款
			payOrder.setPpid(-1L);
			payOrder.setPaymethods(0); // 0公司入款，1第三方支付
			payOrder.setDeposittime(date);
			payOrder.setAmount(new BigDecimal(reward));
			payOrder.setPaystatus(0);
			payOrder.setStatus(3);
			payOrder.setOrdertype(17); // 签到送彩金
			payOrder.setCwremarks("今日签到彩金");
			payOrder.setKfremarks("今日签到彩金");
			payOrder.setCreate_Date(date);
			payOrder.setUpdate_Date(date);
			payOrder.setBeforebalance(beforebalance);
			payOrder.setLaterbalance(latrbalance);
			gameTransferService.savePayOrder(payOrder);

			// 新增帐变记录。
            PayOrderLog log = new PayOrderLog();
            log.setUiid(payOrder.getUiid());
            log.setOrderid(payOrder.getPlatformorders());
            log.setAmount(new BigDecimal((signDay>7?1:0.5)));
            log.setType(2);
            log.setWalletlog(beforebalance+">>>"+latrbalance);
            log.setRemark("今日签到彩金");
            log.setCreatetime(DateUtil.getStrByDate(payOrder.getUpdate_Date(),DateUtil.TIME_FORMAT));
            System.out.println("签到彩金数据："+JsonUtils.toJson(log));
            userPropertyService.insertPayLog(log);

            // 新增签到记录
            UserSign sign = new UserSign();
            sign.setUiid(userInfo.getUiid());
            sign.setAccount(userInfo.getAccount());
            sign.setPuid(userInfo.getPuiid());
            sign.setStatus(1);
            sign.setCreatetime(log.getCreatetime());
            userInfoService.insertUserSign(sign);

            json.put("success", true);
            json.put("day", signDay);
            json.put("msg", "恭喜您，今日签到成功，祝君游戏愉快!");
            return json.toString();
		} catch (Exception e) {
			logger.error("签到赠送彩金异常。", e);
			json.put("msg", "网络异常，请稍后重试!");
		}
		return json.toString();
	}
}
