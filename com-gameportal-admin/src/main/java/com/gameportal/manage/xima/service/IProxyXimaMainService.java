package com.gameportal.manage.xima.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.xima.model.ProxyXimaMain;

public abstract interface IProxyXimaMainService {

	boolean saveOrUpdateProxyXimaMain(ProxyXimaMain proxyXimaMain);

	boolean deleteProxyXimaMain(Long id);

	Long queryProxyXimaMainCount(Map<String, Object> params);

	List<ProxyXimaMain> queryProxyXimaMain(Map<String, Object> params, Integer startNo, Integer pageSize);

	boolean clearXima(ProxyXimaMain pxm, Date ymdstartDate, Date ymdendDate,
			SystemUser systemUser);

	boolean forceXima(ProxyXimaMain pxm, Date ymdstartDate, Date ymdendDate,
			SystemUser systemUser);

	boolean normalXima(ProxyXimaMain pxm, Date ymdstartDate, Date ymdendDate);
}
