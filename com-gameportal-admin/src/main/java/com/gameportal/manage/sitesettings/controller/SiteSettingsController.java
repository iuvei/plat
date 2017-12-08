package com.gameportal.manage.sitesettings.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.manage.pay.model.Activity;
import com.gameportal.manage.pay.model.PayOrder;
import com.gameportal.manage.pay.service.IActivityService;
import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.sitesettings.model.SiteSettings;
import com.gameportal.manage.sitesettings.service.ISiteSettingsService;
import com.gameportal.manage.util.DateUtil2;

/**
 * @ClassName: SiteSettingsController
 * @Description: TODO(网站设置控制类)
 * @author shejia@gz-mstc.com
 * @date 2014-4-10 下午2:51:41
 */
@Controller
@RequestMapping(value = "/manage/sitesettings")
public class SiteSettingsController {
	@Resource(name = "siteSettingsServiceImpl")
	private ISiteSettingsService iSiteSettingsService = null;
	@Resource(name = "activityService")
	private IActivityService activityService;
	public static final Logger logger = Logger
			.getLogger(SiteSettingsController.class);

	public SiteSettingsController() {
		super();
		// TODO Auto-generated constructor stub
	}
	@RequestMapping(value = "/index")
	public String index(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		return "manage/sitesettings/siteSettings";
	}

	@RequestMapping(value = "/querySiteSettings")
	public @ResponseBody
	Object querySiteSettings(
			@RequestParam(value = "ssid", required = false) Long ssid,
			@RequestParam(value = "pname", required = false) String pname,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		Long count = iSiteSettingsService.querySiteSettingsCount(ssid, pname);
		List<SiteSettings> list = iSiteSettingsService.querySiteSettings(ssid, pname, startNo, pageSize);
		return new GridPanel(count, list, true);
	}
	
	@RequestMapping(value = "/saveSiteSettings")
	@ResponseBody
	public Object saveSiteSettings(@ModelAttribute SiteSettings siteSettings,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Date date = new Date();
			if (!StringUtils.isNotBlank(ObjectUtils.toString(siteSettings))) {
				return new ExtReturn(false, "网站信息不能为空！");
			}
			if (!StringUtils.isNotBlank(siteSettings.getSitename())) {
				return new ExtReturn(false, "网站名称不能为空！");
			}
			
			if (StringUtils.isNotBlank(ObjectUtils.toString(siteSettings
					.getSsid()))) {
				siteSettings.setUpdateDate(date);
			} else {
				siteSettings.setCreateDate(date);
				siteSettings.setUpdateDate(date);
			}
			if (iSiteSettingsService.saveOrUpdateSiteSettings(siteSettings)) {
				return new ExtReturn(true, "保存成功！");
			} else {
				return new ExtReturn(false, "保存失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping("/delSiteSettings/{ssid}")
	@ResponseBody
	public Object delSiteSettings(@PathVariable Long ssid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(ssid))) {
				return new ExtReturn(false, "网站主键不能为空！");
			}
			if (iSiteSettingsService.deleteSiteSettings(ssid)) {
				return new ExtReturn(true, "删除成功！");
			} else {
				return new ExtReturn(false, "删除失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	/*优惠活动设置*/
	@RequestMapping(value = "/yhindex")
	public String yhindex(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		return "manage/sitesettings/activity";
	}
	
	@RequestMapping(value = "/querysiteactivity")
	public @ResponseBody
	Object querysiteactivity(
			@RequestParam(value = "hdnumber", required = false) String hdnumber,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(StringUtils.isNotBlank(hdnumber)){
				params.put("hdnumber", hdnumber);
			}
			Long count = activityService.getCount(params);
			List<Activity> list = activityService.getList(params, startNo, pageSize);
			return new GridPanel(count, list, true);
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping(value = "/save")
	@ResponseBody
	public Object save(@ModelAttribute Activity activity,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			if (StringUtils.isNotBlank(ObjectUtils.toString(activity.getAid()))) {
				//更新
				boolean success = activityService.update(activity);
				if(success){
					return new ExtReturn(true, "更新成功！");
				}else{
					return new ExtReturn(false, "更新失败！");
				}
			}else{
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("hdnumber", activity.getHdnumber());
				Long count = activityService.getCount(params);
				if(count > 0){
					return new ExtReturn(false, "优惠代码已存在，请更换优惠代码后重新操作。");
				}
				boolean success = activityService.save(activity);
				if(success){
					return new ExtReturn(true, "保存成功！");
				}else{
					return new ExtReturn(false, "保存失败！");
				}
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
}
