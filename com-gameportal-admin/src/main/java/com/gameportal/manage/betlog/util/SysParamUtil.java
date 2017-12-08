package com.gameportal.manage.betlog.util;

import com.gameportal.manage.betlog.model.SysParam;
import com.gameportal.manage.betlog.service.ISysParamService;
import com.gameportal.manage.util.SpringUtil;

public class SysParamUtil {
  public static String getValue(String key){
	  ISysParamService sysParamService=(ISysParamService)SpringUtil.getBean("sysParamServiceImpl");
	  SysParam sys= sysParamService.findEntityByKey(key);
	  if (sys!=null){
		  return sys.getParamvalue();
	  }else{
		  return "";
	  }
	 
  }
  public static SysParam getEntity(String key){
	  ISysParamService sysParamService=(ISysParamService)SpringUtil.getBean("sysParamServiceImpl");
	  return sysParamService.findEntityByKey(key);
	 
	 
  }
}
