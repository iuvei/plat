package com.gameportal.manage.reportform.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.gameportal.manage.member.model.GameTransfer;
import com.gameportal.manage.reportform.dao.MemberDetailReportDao;
import com.gameportal.manage.reportform.service.IMemberDetailReportService;
import com.gameportal.manage.pay.model.PayOrder;
import com.gameportal.manage.xima.model.MemberXimaMain;

@Service("memberDetailReportService")
public class MemberDetailReportServiceImpl implements IMemberDetailReportService {

	@Resource(name = "memberDetailReportDao")
	private MemberDetailReportDao memberDetailReportDao = null;
	
	@Override
	public Map<String,Object> getAccountMoney(Map<String, Object> map) {
		return memberDetailReportDao.getAccountMoney(map);
	}
	
	@Override
	public List<PayOrder> getPayMoneyList(Map<String, Object> map, Integer startNo, Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		map.put("limit", true);
		map.put("thisPage", startNo);
		map.put("pageSize", pageSize);
		return memberDetailReportDao.getPayMoneyList(map);
	}
	
	@Override
	public Long getPayMoneyCount(Map<String, Object> map) {
		return memberDetailReportDao.getPayMoneyCount(map);
	}
	
	@Override
	public List<GameTransfer> getTransferList(Map<String, Object> map, Integer startNo, Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		map.put("limit", true);
		map.put("thisPage", startNo);
		map.put("pageSize", pageSize);
		return memberDetailReportDao.getTransferList(map);
	}
	
	@Override
	public Long getTransferCount(Map<String, Object> map) {
		return memberDetailReportDao.getTransferCount(map);
	}
	
	@Override
	public List<MemberXimaMain> getMemberXimaList(Map<String, Object> map, Integer startNo, Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		map.put("limit", true);
		map.put("thisPage", startNo);
		map.put("pageSize", pageSize);
		return memberDetailReportDao.getMemberXimaList(map);
	}
	
	@Override
	public Long getMemberXimaCount(Map<String, Object> map) {
		return memberDetailReportDao.getMemberXimaCount(map);
	}
}
