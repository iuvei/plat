package com.gameportal.manage.reportform.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.gameportal.manage.reportform.dao.DxMemberReportFormDao;
import com.gameportal.manage.reportform.dao.MemberDetailReportDao;
import com.gameportal.manage.reportform.model.DxMemberReportForm;
import com.gameportal.manage.reportform.service.IDxMemberReportFormService;

@Service("dxMemberReportService")
public class DxMemberReportFormImplServiceImpl implements IDxMemberReportFormService {

	@Resource(name = "dxMemberReportDao")
	private DxMemberReportFormDao dxMemberReportDao = null;
	
	@Override
	public List<DxMemberReportForm> getDxMemberReportList(Map<String, Object> params, Integer startNo, Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		return dxMemberReportDao.getDxMemberReportList(params);
	}
	
	@Override
	public Long getDxMemberReportCount(Map<String, Object> params) {
		return dxMemberReportDao.getDxMemberReportCount(params);
	}
}
