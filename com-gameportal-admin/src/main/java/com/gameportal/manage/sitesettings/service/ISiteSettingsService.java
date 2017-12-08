package com.gameportal.manage.sitesettings.service;

import java.util.List;

import com.gameportal.manage.sitesettings.model.SiteSettings;

/**
 * @ClassName: ISiteSettingsService
 * @Description: TODO(游戏平台接口)
 * @author shejia@gz-mstc.com
 * @date 2014-4-10 下午4:39:05
 */
public abstract interface ISiteSettingsService {
	public abstract SiteSettings querySiteSettingsById(Long ssid);
	
	public abstract List<SiteSettings> querySiteSettings(Long ssid,
			String sitename, Integer startNo, Integer pageSize);
	
	public abstract List<SiteSettings> querySiteSettings(Long ssid,
			String sitename, String siteurl, String spreadkey, 
			Integer lockedcount, Integer isclosed , Integer isregister,
			Integer islogin, Integer isrecharge , Integer isdraw,
			Integer lowestdraw, Integer highestdraw , Integer startNo, Integer pageSize);
	
	public abstract Long querySiteSettingsCount(Long ssid,
			String sitename);
	
	public abstract Long querySiteSettingsCount(Long ssid,
			String sitename, String siteurl, String spreadkey, 
			Integer lockedcount, Integer isclosed , Integer isregister,
			Integer islogin, Integer isrecharge , Integer isdraw,
			Integer lowestdraw, Integer highestdraw);
	
	public abstract SiteSettings saveSiteSettings(SiteSettings siteSettings)
			throws Exception;

	public abstract boolean saveOrUpdateSiteSettings(SiteSettings siteSettings)
			throws Exception;

	public abstract boolean deleteSiteSettings(Long ssid) throws Exception;
	
}
