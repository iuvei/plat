package com.gameportal.manage.reportform.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.gameportal.manage.proxy.model.ProxyUserXimaLog;
import com.gameportal.manage.reportform.dao.OrderManageReportFormDao;
import com.gameportal.manage.reportform.model.OrderManageReportForm;
import com.gameportal.manage.reportform.service.IOrderManageReportFormService;
import com.gameportal.manage.xima.model.MemberXimaMain;

@Service("orderManageReportFormService")
public class OrderManageReportFormServiceImpl implements IOrderManageReportFormService {

	@Resource(name = "orderManageReportDao")
	private OrderManageReportFormDao orderManageReportDao = null;
	
	@Override
	public Map<String, Object> getAccountMoney(Map<String, Object> map) {
		return orderManageReportDao.getAccountMoney(map);
	}

	@Override
	public List<OrderManageReportForm> getOrderManageList(Map<String, Object> map, Integer startNo, Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		map.put("limit", true);
		map.put("thisPage", startNo);
		map.put("pageSize", pageSize);
		return orderManageReportDao.getOrderManageList(map);
	}
	
	@Override
	public Long getOrderManageCount(Map<String, Object> map) {
		return orderManageReportDao.getOrderManageCount(map);
	}
	
	@Override
	public List<OrderManageReportForm> getOrderManageListForReport(Map<String, Object> map) {
		return orderManageReportDao.getOrderManageList(map);
	}
	
	@Override
	public String getMemberXimaMainMoney(Map<String, Object> params) {
		return orderManageReportDao.getMemberXimaMainMoney(params);
	}
	
	@Override
	public String getProxyClearLogMoney(Map<String, Object> params) {
		return orderManageReportDao.getProxyClearLogMoney(params);
	}
	@Override
	public String getProxyUserXimaLogMoney(Map<String, Object> params) {
		return orderManageReportDao.getProxyUserXimaLogMoney(params);
	}
	
}
