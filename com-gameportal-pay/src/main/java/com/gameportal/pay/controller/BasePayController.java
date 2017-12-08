package com.gameportal.pay.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;

import com.gameportal.pay.model.Activity;
import com.gameportal.pay.model.ActivityFlag;
import com.gameportal.pay.service.IPayPlatformService;
import com.gameportal.pay.util.DateUtil;
import com.gameportal.web.user.model.UserInfo;

@Controller
public class BasePayController {
	@Resource(name = "payPlatformServiceImpl")
	IPayPlatformService payPlatformService;

	public String webValidate(UserInfo userInfo, String hd, HttpServletRequest request) {
		List<ActivityFlag> listflag = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", 1);
		params.put("hdnumber", hd);
		List<Activity> activityList = payPlatformService.queryActivity(params);// 所选活动
		if (CollectionUtils.isEmpty(activityList)) {
			request.setAttribute("msg", "您选择的活动不存在或已经下线!");
			return "/payment/error";
		}
		if ("3".equals(hd) || "4".equals(hd) || "1".equals(hd) || "2".equals(hd)) { // 首存、次存
			Activity activity = activityList.get(0);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uiid", userInfo.getUiid());
			map.put("type", 3);
			map.put("acid", activity.getAid());
			listflag = payPlatformService.queryActivityFlag(map);// 当前用户参与过的活动
			if (CollectionUtils.isNotEmpty(listflag)) { // 如果参加过
				request.setAttribute("msg", "您已经参加过此活动,请选择其他优惠活动!");
				return "/payment/error";
			}
			if ("1".equals(hd) || "2".equals(hd) || "3".equals(hd)) { // 三选一
				map.clear();
				map.put("uiid", userInfo.getUiid());
				map.put("acids", "1,2,3");
				listflag = payPlatformService.queryActivityFlag(map);// 当前用户参与过的活动
				if (CollectionUtils.isNotEmpty(listflag) && listflag.size() >= 1) {
					request.setAttribute("msg", "首存100%,存100送88,送50送58三个活动,只能三选一");
					return "/payment/error";
				}
			}
		} else if ("7".equals(hd)) { // 日首存
			Activity activity = activityList.get(0);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uiid", userInfo.getUiid());
			map.put("type", 3);
			map.put("acid", activity.getAid());
			map.put("flagtime", DateUtil.getToday());
			listflag = payPlatformService.queryActivityFlag(map);// 当前用户今日参与过的活动
			if (CollectionUtils.isNotEmpty(listflag)) { // 如果参加过
				request.setAttribute("msg", "您今日已经参加过此活动,请选择其他优惠活动!");
				return "/payment/error";
			}
		} else if ("11".equals(hd) || "12".equals(hd) || "13".equals(hd)) { // 百家乐三选一
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uiid", userInfo.getUiid());
			map.put("acids", "11,12,13");
			listflag = payPlatformService.queryActivityFlag(map);// 当前用户参与过的活动
			if (CollectionUtils.isNotEmpty(listflag) && listflag.size() >= 1) {
				request.setAttribute("msg", "真人百家乐存送活动三个档次,只能三选一!");
				return "/payment/error";
			}
		} else if ("15".equals(hd) || "16".equals(hd)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uiid", userInfo.getUiid());
			map.put("acids", "15,16");
			map.put("flagtime", DateUtil.getToday());
			listflag = payPlatformService.queryActivityFlag(map);// 当前用户今日参与过的活动
			if (CollectionUtils.isNotEmpty(listflag)) { // 如果参加过
				request.setAttribute("msg", "您今日已经参加过此活动,请选择其他优惠活动!");
				return "/payment/error";
			}
		}
		return null;
	}

	public void wapValidate(UserInfo userInfo, String hd, String requestHost, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=GBK");
		response.setCharacterEncoding("GBK");
		PrintWriter writer = response.getWriter();
		List<ActivityFlag> listflag = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", 1);
		params.put("hdnumber", hd);
		List<Activity> activityList = payPlatformService.queryActivity(params);// 所选活动
		if (CollectionUtils.isEmpty(activityList)) {
			response.addHeader("refresh", "3;url=http://" + requestHost);
			writer.write("您选择的活动不存在或已经下线!返回首页...");
			writer.flush();
			response.flushBuffer();
			writer.close();
			return;
		}

		if ("3".equals(hd) || "4".equals(hd)) { // 首存、次存
			Activity activity = activityList.get(0);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uiid", userInfo.getUiid());
			map.put("type", 3);
			map.put("acid", activity.getAid());
			listflag = payPlatformService.queryActivityFlag(map);// 当前用户参与过的活动
			if (CollectionUtils.isNotEmpty(listflag)) { // 如果参加过
				response.addHeader("refresh", "3;url=http://" + requestHost);
				writer.write("您已经参加过此活动,请选择其他优惠活动!返回首页...");
				writer.flush();
				response.flushBuffer();
				writer.close();
				return;
			}
		} else if ("7".equals(hd)) { // 日首存
			Activity activity = activityList.get(0);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uiid", userInfo.getUiid());
			map.put("type", 3);
			map.put("acid", activity.getAid());
			map.put("flagtime", DateUtil.getToday());
			listflag = payPlatformService.queryActivityFlag(map);// 当前用户今日参与过的活动
			if (CollectionUtils.isNotEmpty(listflag)) { // 如果参加过
				response.addHeader("refresh", "3;url=http://" + requestHost);
				writer.write("您今日已经参加过此活动,请选择其他优惠活动!返回首页...");
				writer.flush();
				response.flushBuffer();
				writer.close();
				return;
			}
		} else if ("11".equals(hd) || "12".equals(hd) || "13".equals(hd)) { // 百家乐三选一
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uiid", userInfo.getUiid());
			map.put("acids", "11,12,13");
			listflag = payPlatformService.queryActivityFlag(map);// 当前用户参与过的活动
			if (CollectionUtils.isNotEmpty(listflag) && listflag.size() >= 1) {
				response.addHeader("refresh", "3;url=http://" + requestHost);
				writer.write("真人百家乐存送活动三个档次,只能三选一!返回首页...");
				writer.flush();
				response.flushBuffer();
				writer.close();
				return;
			}
		} else if ("15".equals(hd) || "16".equals(hd)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uiid", userInfo.getUiid());
			map.put("acids", "15,16");
			map.put("flagtime", DateUtil.getToday());
			listflag = payPlatformService.queryActivityFlag(map);// 当前用户今日参与过的活动
			if (CollectionUtils.isNotEmpty(listflag)) { // 如果参加过
				response.addHeader("refresh", "3;url=http://" + requestHost);
				writer.write("您今日已经参加过此活动,请选择其他优惠活动!返回首页...");
				writer.flush();
				response.flushBuffer();
				writer.close();
				return;
			}
		}
	}
}
