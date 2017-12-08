package com.gameportal.manage.reportform.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gameportal.manage.reportform.model.ReportForm;
import com.gameportal.manage.system.dao.BaseIbatisDAO;
import com.gameportal.manage.user.model.UserInfo;

@Component("reportFormDao")
@SuppressWarnings("all")
public class ReportFormDao extends BaseIbatisDAO {

	@Override
	public Class getEntityClass() {
		return ReportForm.class;
	}
	
	/**
	 * 统计报表集合
	 * @param params 参数
	 * @return
	 */
	public List<ReportForm> getReportResult(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".selectReportResult", params);
	}
	
	/**
	 * 查询历史报表
	 * @param params
	 * @return
	 */
	public List<ReportForm> getHistoryReportResult(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".selectHistoryReportResult", params);
	}
	
	/**
	 * 统计报表总数
	 * @param params 参数
	 * @return
	 */
	public Long getReportCount(Map<String, Object> params){
		return (Long) super.getSqlMapClientTemplate().queryForObject(getSimpleName()+".selectReportCount", params);
	}
	
	/**
	 * 查询首次充值金额
	 * @param params 日期 (YY-MM-DD)
	 * @return
	 */
	public String getFirstPayMoney(Map<String, Object> params){
		return (String) super.getSqlMapClientTemplate().queryForObject(getSimpleName()+".selectFirstPayMoney",params);
	}
	
	/**
	 * 查询活跃人数
	 * @param params 日期 (YY-MM-DD)
	 * @return
	 */
	public long selectActiveCount(Map<String, Object> params){
		return (long) super.getSqlMapClientTemplate().queryForObject(getSimpleName()+".selectActiveCount",params);
	}
	
	/**
	 * 查询正常登录人数
	 * @param params
	 * @return
	 */
	public long selectLoginNumberCount(Map<String,Object> params){
		return (long) super.getSqlMapClientTemplate().queryForObject(getSimpleName()+".selectLoginNumberCount",params);
	}
	
	/**
	 * 查询投注总额
	 * @param params 日期 (YY-MM-DD)
	 * @return
	 */
	public String getRealBetMoney(Map<String, Object> params){
		return (String) super.getSqlMapClientTemplate().queryForObject(getSimpleName()+".selectRealBetMoney",params);
	}
	
	/**
	 * 统计注册人数集合
	 * @param params 参数
	 * @return
	 */
	public List<Map<String,Object>> getRegisterNumberResult(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".selectRegisterNumberByNowDay", params);
	}
	
	/**
	 * 统计注册人数总数
	 * @param params 参数
	 * @return
	 */
	public Long getRegisterCount(Map<String, Object> params){
		return (Long) super.getSqlMapClientTemplate().queryForObject(getSimpleName()+".selectRegisterNumberCountByNowDay", params);
	}
	
	
	/**
	 * 统计首冲人数集合
	 * @param params 参数
	 * @return
	 */
	public List<Map<String,Object>> getFirstPayNumberResult(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".selectFirstPayNumberByNowDay", params);
	}
	
	/**
	 * 统计首冲人数总数
	 * @param params 参数
	 * @return
	 */
	public Long getFirstPayCount(Map<String, Object> params){
		return (Long) super.getSqlMapClientTemplate().queryForObject(getSimpleName()+".selectFirstPayNumberCountByNowDay", params);
	}
	
	
	/**
	 * 实际投注额集合
	 * @param params 参数
	 * @return
	 */
	public List<Map<String,Object>> getBetMoneyResult(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".selectBetMoneyByNowDay", params);
	}
	
	/**
	 * 实际投注额总数
	 * @param params 参数
	 * @return
	 */
	public Long getBetMoneyCount(Map<String, Object> params){
		return (Long) super.getSqlMapClientTemplate().queryForObject(getSimpleName()+".selectBetMoneyCountByNowDay", params);
	}
	
	
	/**
	 * 会员/代理优惠集合
	 * @param params 参数
	 * @return
	 */
	public List<Map<String,Object>> getMemberCouponResult(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".selectMemberCouponByNowDay", params);
	}
	
	/**
	 * 会员/代理优惠总数
	 * @param params 参数
	 * @return
	 */
	public Long getMemberCouponCount(Map<String, Object> params){
		return (Long) super.getSqlMapClientTemplate().queryForObject(getSimpleName()+".selectMemberCouponCountByNowDay", params);
	}
	
	/**
	 * 存款/取款集合
	 * @param params 参数
	 * @return
	 */
	public List<Map<String,Object>> getPayMoneyResult(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".selectPayMoneyByNowDay", params);
	}
	
	/**
	 * 存款/取款优惠总数
	 * @param params 参数
	 * @return
	 */
	public Long getPayMoneyCount(Map<String, Object> params){
		return (Long) super.getSqlMapClientTemplate().queryForObject(getSimpleName()+".selectPayMoneyCountByNowDay", params);
	}

	/**
	 * 从平台投注统计表中查询选择日期的投注统计
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getPlatformBetStats(Map<String, Object> params) {
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".getPlatformBetStats", params);
	}
	
	/**
	 * 充值、提款、优惠统计
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getwinorlessReport(Map<String, Object> params) {
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".getwinorlessReport", params);
	}
}
