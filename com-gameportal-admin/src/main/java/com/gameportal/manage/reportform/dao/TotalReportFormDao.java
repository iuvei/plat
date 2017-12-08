package com.gameportal.manage.reportform.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gameportal.manage.reportform.model.BetReportForm;
import com.gameportal.manage.reportform.model.FavorableReportForm;
import com.gameportal.manage.reportform.model.HyMemberReportForm;
import com.gameportal.manage.reportform.model.PayReportForm;
import com.gameportal.manage.reportform.model.PlatformReportForm;
import com.gameportal.manage.reportform.model.RegisterReportForm;
import com.gameportal.manage.reportform.model.WithdrawalReportForm;
import com.gameportal.manage.system.dao.BaseIbatisDAO;

@Component("totalReportFormDao")
public class TotalReportFormDao extends BaseIbatisDAO{

	@Override
	public Class<?> getEntityClass() {
		return null;
	}
	
	/**
	 * 统计注册人数
	 * @param params:{starttime,endtime}
	 * @return
	 */
	public List<RegisterReportForm> getRegister(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList("TotalReportForm.getRegister", params);
	}
	
	/**
	 * 统计下注总额
	 * @param params
	 * @return
	 */
	public List<BetReportForm> getTotalBetList(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList("TotalReportForm.getTotalBetList", params);
	}
	
	/**
	 * 充值报表列表
	 * @param params
	 * @return
	 */
	public List<PayReportForm> getPayReportList(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList("TotalReportForm.getPayReportList", params);
	}
	
	/**
	 * 提款报表列表
	 * @param params
	 * @return
	 */
	public List<WithdrawalReportForm> getWithdrawalReportList(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList("TotalReportForm.getWithdrawalReportList", params);
	}
	
	/**
	 * 优惠金额统计查询
	 * @param params
	 * @return
	 */
	public List<FavorableReportForm> getFavorableReportList(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList("TotalReportForm.getFavorableReportList", params);
	}
	
	/**
	 * 洗码金额统计查询
	 * @param params
	 * @return
	 */
	public FavorableReportForm getXimaReportList(Map<String, Object> params){
		List<FavorableReportForm> list = super.getSqlMapClientTemplate().queryForList("TotalReportForm.getXimaReportList", params);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 平台总数据
	 * @param params
	 * @return
	 */
	public List<PlatformReportForm> getPlatformReport(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList("TotalReportForm.getPlatformReport", params);
	}
	
	/**
	 * 获取活跃会员数据
	 * @param params
	 * @return
	 */
	public HyMemberReportForm getHymember(Map<String, Object> params){
		return (HyMemberReportForm)super.getSqlMapClientTemplate().queryForObject("TotalReportForm.getHymember", params);
	}

}
