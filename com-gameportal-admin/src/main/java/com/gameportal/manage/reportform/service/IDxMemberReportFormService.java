package com.gameportal.manage.reportform.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.reportform.model.DxMemberReportForm;

/**
 * 电销客户分析表
 * @author Administrator
 *
 */
public interface IDxMemberReportFormService {
	
	/**
	 * 查询电销分析数据
	 * @param params
	 * @param startNo
	 * @param pageSize
	 * @return
	 */
	public List<DxMemberReportForm> getDxMemberReportList(Map<String, Object> params,Integer startNo,Integer pageSize);
	
	/**
	 * 查询电销分析总计
	 * @param params
	 * @return
	 */
	public Long getDxMemberReportCount(Map<String, Object> params);
}
