package com.gameportal.manage.marketing.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.manage.marketing.model.MarketAnalysis;
import com.gameportal.manage.marketing.model.MarketingChannel;
import com.gameportal.manage.marketing.service.MarketingChannelService;
import com.gameportal.manage.member.model.GameTransferLog;
import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.util.DateUtil2;
import com.gameportal.portal.util.DateUtil;

@Controller
@RequestMapping(value = "/manage/marketingchannel")
public class MarketingChannelController {
	
	public static final Logger logger = Logger
			.getLogger(MarketingChannelController.class);


	@Resource(name = "marketingChannelService")
	private MarketingChannelService marketingChannelService;
	
	@RequestMapping(value = "/index")
	public String index(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		return "manage/marketingchannel/channel";
	}
	
	@RequestMapping(value = "/queryMarketingChannel")
	public @ResponseBody
	Object queryMarketingChannel(
			@RequestParam(value = "channelname", required = false) String channelname,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String,Object> params=new HashMap<String,Object>();
			if(StringUtils.isNotBlank(channelname)){
				params.put("channelname", channelname);
			}
			Long count = marketingChannelService.getCount(params);
			params.put("sortColumns", " channeltime desc");
			List<MarketingChannel> list = marketingChannelService.getList(params, startNo==null?0:startNo, pageSize==null?30:pageSize);
			return new GridPanel(count, list, true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping(value = "/saveMarketingChannel")
	public @ResponseBody
	Object saveMarketingChannel(
			@ModelAttribute MarketingChannel marketingChannel) {
		try {
			marketingChannel.setChanneltime(DateUtil2.format2(new Date(), DateUtil2.DATE_PATTERN_S));
			String channelURL = "http://www.16898.com/?c="+marketingChannel.getChannelvalue();
			marketingChannel.setChannelurl(channelURL);
			if(!StringUtils.isNotBlank(ObjectUtils.toString(marketingChannel.getChannelid()))){
				if (marketingChannelService.save(marketingChannel)) {
					return new ExtReturn(true, "保存成功！");
				} else {
					return new ExtReturn(false, "保存失败！");
				}
			}else{
				if (marketingChannelService.update(marketingChannel)) {
					return new ExtReturn(true, "编辑成功！");
				} else {
					return new ExtReturn(false, "编辑失败！");
				}
			}
			
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	/**
	 * 市场分析页面跳转
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/indexma")
	public String indexma(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		return "manage/marketingchannel/marketanalysis";
	}
	
	/**
	 * 查询市场分析数据
	 * @param channel
	 * @param startdate
	 * @param enddate
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryMarketAnalysis")
	public @ResponseBody
	Object queryMarketAnalysis(
			@RequestParam(value = "channel", required = false) String channel,
			@RequestParam(value = "startdate", required = false) String startdate,
			@RequestParam(value = "enddate", required = false) String enddate,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(StringUtils.isNotBlank(startdate)){
				params.put("startdate", startdate);
			}else{
				params.put("startdate", DateUtil.getFirstDayStr(new Date()));
			}
			if(StringUtils.isNotBlank(enddate)){
				params.put("enddate", enddate);
			}else{
				params.put("enddate", DateUtil2.format2(new Date(), DateUtil2.DATE_PATTERN_D));
			}
			if(StringUtils.isNotBlank(channel)){
				params.put("channel", channel);
			}
			List<MarketAnalysis> list = marketingChannelService.findMarketAnalysis(params);
			return new GridPanel(0L, list, true);
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
}
