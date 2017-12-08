package com.gameportal.manage.reportform.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.gameportal.manage.betlog.dao.BetLogDao;
import com.gameportal.manage.member.model.DailyReportDetail;
import com.gameportal.manage.reportform.dao.ReportFormDao;
import com.gameportal.manage.reportform.model.ReportForm;
import com.gameportal.manage.reportform.service.IReportFormService;

@Service("reportFormService")
public class ReportFormServiceImpl implements IReportFormService {
	
	@Resource(name = "reportFormDao")
	private ReportFormDao reportFormDao = null;
	
	@Resource(name="betLogDao")
	private BetLogDao betLogDao;
	
	@Override
	public List<ReportForm> getReportResult(Map<String, Object> params,Integer startNo,Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		return reportFormDao.getReportResult(params);
	}
	
	@Override
	public Long getReportCount(Map<String, Object> params){
		return reportFormDao.getReportCount(params);
	}
	
	@Override
	public String getFirstPayMoney(String nowDate) {
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("nowDate", nowDate);
		return reportFormDao.getFirstPayMoney(params);
	}
	
	@Override
	public String getRealBetMoney(String nowDate) {
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("nowDate", nowDate);
		return reportFormDao.getRealBetMoney(params);
	}
	
	@Override
	public List<ReportForm> getReportResult(Map<String, Object> params) {
		return reportFormDao.getReportResult(params);
	}
	
	
	@Override
	public List<Map<String, Object>> getRegisterNumberResult(Map<String, Object> params,Integer startNo,Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		return reportFormDao.getRegisterNumberResult(params);
	}
	
	
	@Override
	public Long getRegisterNumberCount(Map<String, Object> params) {
		return reportFormDao.getRegisterCount(params);
	}
	
	
	
	@Override
	public List<Map<String, Object>> getFirstPayNumberResult(Map<String, Object> params, Integer startNo, Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		return reportFormDao.getFirstPayNumberResult(params);
	}
	
	
	@Override
	public Long getFirstPayNumberCount(Map<String, Object> params) {
		return reportFormDao.getFirstPayCount(params);
	}

	
	@Override
	public List<Map<String, Object>> getBetMoneyResult(Map<String, Object> params, Integer startNo, Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		return reportFormDao.getBetMoneyResult(params);
	}
	
	@Override
	public Long getBetMoneyCount(Map<String, Object> params) {
		return reportFormDao.getBetMoneyCount(params);
	}
	
	@Override
	public List<Map<String, Object>> getMemberCouponResult(Map<String, Object> params, Integer startNo, Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		return reportFormDao.getMemberCouponResult(params);
	}
	
	@Override
	public Long getMemberCouponCount(Map<String, Object> params) {
		return reportFormDao.getMemberCouponCount(params);
	}
	
	@Override
	public List<Map<String, Object>> getPayMoneyResult(Map<String, Object> params, Integer startNo, Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		return reportFormDao.getPayMoneyResult(params);
	}
	
	@Override
	public Long getPayMoneyCount(Map<String, Object> params) {
		return reportFormDao.getPayMoneyCount(params);
	}

	@Override
	public List<Map<String, Object>> getPlatformBetStats(Map<String, Object> params, Integer startNo, Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		return reportFormDao.getPlatformBetStats(params);
	}

	@Override
	public List<Map<String, Object>> getwinorlessReport(Map<String, Object> params, Integer startNo, Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 1000;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		return reportFormDao.getwinorlessReport(params);
	}

	@Override
	public Long selectActiveCount(Map<String, Object> params) {
		return reportFormDao.selectActiveCount(params);
	}

	@Override
	public Long selectLoginNumberCount(Map<String, Object> params) {
		return reportFormDao.selectLoginNumberCount(params);
	}

	@Override
	public void insertStatDailyReport(DailyReportDetail report) {
		betLogDao.insertStatDailyReport(report);
	}

	@Override
	public List<ReportForm> getHistoryReportResult(Map<String, Object> params) {
		return reportFormDao.getHistoryReportResult(params);
	}
}
