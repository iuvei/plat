package com.gameportal.manage.sitesettings.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.manage.gameplatform.model.GamePlatform;
import com.gameportal.manage.sitesettings.dao.SiteSettingsDao;
import com.gameportal.manage.sitesettings.model.SiteSettings;
import com.gameportal.manage.sitesettings.service.ISiteSettingsService;

@Service
public class SiteSettingsServiceImpl implements ISiteSettingsService {
	@Resource(name = "siteSettingsDao")
	private SiteSettingsDao siteSettingsDao = null;
	private Logger logger = Logger.getLogger(SiteSettingsServiceImpl.class);// 日志对象

	public SiteSettingsServiceImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public SiteSettings querySiteSettingsById(Long ssid) {
		return (SiteSettings) siteSettingsDao.findById(ssid);
	}

	@Override
	public List<SiteSettings> querySiteSettings(Long ssid, String sitename,
			Integer startNo, Integer pageSize) {
		return querySiteSettings(ssid,sitename,null,null,null,null,null,null,
				null,null,null,null,startNo,pageSize);
	}

	@Override
	public List<SiteSettings> querySiteSettings(Long ssid, String sitename,
			String siteurl, String spreadkey, Integer lockedcount,
			Integer isclosed, Integer isregister, Integer islogin,
			Integer isrecharge, Integer isdraw, Integer lowestdraw,
			Integer highestdraw, Integer startNo, Integer pageSize) {
		Map params = new HashMap();
		if(StringUtils.isNotBlank(ObjectUtils.toString(ssid))){
			params.put("ssid", ssid);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(sitename))){
			params.put("sitename", sitename);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(siteurl))){
			params.put("siteurl", siteurl);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(spreadkey))){
			params.put("spreadkey", spreadkey);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(lockedcount))){
			params.put("lockedcount", lockedcount);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(isclosed))){
			params.put("isclosed", isclosed);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(isregister))){
			params.put("isregister", isregister);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(islogin))){
			params.put("islogin", islogin);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(isrecharge))){
			params.put("isrecharge", isrecharge);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(isdraw))){
			params.put("isdraw", isdraw);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(lowestdraw))){
			params.put("lowestdraw", lowestdraw);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(highestdraw))){
			params.put("highestdraw", highestdraw);
		}
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		return siteSettingsDao.getList(params);
	}

	@Override
	public Long querySiteSettingsCount(Long ssid, String sitename) {
		return querySiteSettingsCount(ssid,sitename,null,null,null,
				null,null,null,null,null,null,null);
	}

	@Override
	public Long querySiteSettingsCount(Long ssid, String sitename,
			String siteurl, String spreadkey, Integer lockedcount,
			Integer isclosed, Integer isregister, Integer islogin,
			Integer isrecharge, Integer isdraw, Integer lowestdraw,
			Integer highestdraw) {
		Map params = new HashMap();
		if(StringUtils.isNotBlank(ObjectUtils.toString(ssid))){
			params.put("ssid", ssid);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(sitename))){
			params.put("sitename", sitename);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(siteurl))){
			params.put("siteurl", siteurl);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(spreadkey))){
			params.put("spreadkey", spreadkey);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(lockedcount))){
			params.put("lockedcount", lockedcount);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(isclosed))){
			params.put("isclosed", isclosed);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(isregister))){
			params.put("isregister", isregister);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(islogin))){
			params.put("islogin", islogin);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(isrecharge))){
			params.put("isrecharge", isrecharge);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(isdraw))){
			params.put("isdraw", isdraw);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(lowestdraw))){
			params.put("lowestdraw", lowestdraw);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(highestdraw))){
			params.put("highestdraw", highestdraw);
		}
		return siteSettingsDao.getRecordCount(params);
	}

	@Override
	public SiteSettings saveSiteSettings(SiteSettings siteSettings)
			throws Exception {
		siteSettings = (SiteSettings) siteSettingsDao.save(siteSettings);
		return StringUtils.isNotBlank(ObjectUtils.toString(siteSettings
				.getSsid())) ? siteSettings : null;
	}

	@Override
	public boolean saveOrUpdateSiteSettings(SiteSettings siteSettings)
			throws Exception {
		return siteSettingsDao.saveOrUpdate(siteSettings);
	}

	@Override
	public boolean deleteSiteSettings(Long ssid) throws Exception {
		return siteSettingsDao.delete(ssid);
	}
}