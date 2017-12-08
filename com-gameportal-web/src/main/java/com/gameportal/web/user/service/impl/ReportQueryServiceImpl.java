package com.gameportal.web.user.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.gameportal.web.user.dao.BetLogDao;
import com.gameportal.web.user.dao.PayOrderDao;
import com.gameportal.web.user.model.PayOrder;
import com.gameportal.web.user.service.IReportQueryService;

@Service("reportQueryServiceImpl")
public class ReportQueryServiceImpl implements IReportQueryService {

	@Resource(name = "payOrderDao")
	private PayOrderDao payOrderDao;
	
	@Resource(name = "betLogDao")
	private BetLogDao betLogDao;

	@Override
	public List<PayOrder> queryPayOrder(Long uiid, String startTime,
			String endTime, int paytyple, int status, int pageNo, int pageSize) {
		Map<String, Object> map = new HashMap<String, Object>(5);
		map.put("uiid", uiid);
		if (StringUtils.isNotBlank(ObjectUtils.toString(startTime))) {
			map.put("startTime", startTime+" 00:00:00");
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(endTime))) {
			map.put("endTime", endTime + " 23:59:59");
		}
		if (paytyple > -1) {
			map.put("paytyple", paytyple);
		}
		if (status > -1) {
			map.put("status", status);
		}
		map.put("sortColumns", "create_date desc");
		map.put("limitParams", (pageNo-1)*pageSize+","+pageSize);
		List<PayOrder> list = payOrderDao.getList(map);
		return list;
	}

	@Override
	public long queryPayOrderCount(Long uiid, String startTime, String endTime,
			int paytyple, int status) {
		Map<String, Object> map = new HashMap<String, Object>(5);
		map.put("uiid", uiid);
		if (StringUtils.isNotBlank(ObjectUtils.toString(startTime))) {
			map.put("startTime", startTime+" 00:00:00");
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(endTime))) {
			map.put("endTime", endTime + " 23:59:59");
		}
		if (paytyple > -1) {
			map.put("paytyple", paytyple);
		}
		if (status > -1) {
			map.put("status", status);
		}
		return payOrderDao.getRecordCount(map);
	}

	@Override
	public long sumPayOrder(Map<String, Object> params) {
		return payOrderDao.sumPayOrder(params);
	}

	@Override
	public List<PayOrder> queryPayOrder(Map<String, Object> params) {
		return payOrderDao.getList(params);
	}

	@Override
	public List<String> selectGameMemberByDate(Map<String, Object> params) {
		return betLogDao.selectGameMemberByDate(params);
	}

	@Override
	public List<String> selectGameMemberByWeek(Map<String, Object> params) {
		return betLogDao.selectGameMemberByWeek(params);
	}

	@Override
	public long countPayOrder(Map<String, Object> params) {
		return payOrderDao.countPayOrder(params);
	}
}
