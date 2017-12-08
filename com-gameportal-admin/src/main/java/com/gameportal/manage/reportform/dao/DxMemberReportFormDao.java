package com.gameportal.manage.reportform.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gameportal.manage.reportform.model.DxMemberReportForm;
import com.gameportal.manage.system.dao.BaseIbatisDAO;

/**
 * 电销客户分析dao
 * @author Administrator
 *
 */
@Component("dxMemberReportDao")
@SuppressWarnings("all")
public class DxMemberReportFormDao extends BaseIbatisDAO {

	@Override
	public Class getEntityClass() {
		return DxMemberReportForm.class;
	}

	/**
	 * 电销客户分析列表
	 * @param params
	 * @return
	 */
	public List<DxMemberReportForm> getDxMemberReportList(Map<String, Object> params){
		return  super.getSqlMapClientTemplate().queryForList(getSimpleName()+".selectDxMemberList", params);
	}
	
	/**
	 * 电销客户分析总数
	 * @param params
	 * @return
	 */
	public Long getDxMemberReportCount(Map<String, Object> params){
		return  (Long) super.getSqlMapClientTemplate().queryForObject(getSimpleName()+".selectDxMemberCount", params);
	}
}
