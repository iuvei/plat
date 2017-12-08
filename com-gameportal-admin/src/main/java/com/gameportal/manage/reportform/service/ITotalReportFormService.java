package com.gameportal.manage.reportform.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.reportform.model.BetReportForm;
import com.gameportal.manage.reportform.model.FavorableReportForm;
import com.gameportal.manage.reportform.model.HyMemberReportForm;
import com.gameportal.manage.reportform.model.PayReportForm;
import com.gameportal.manage.reportform.model.PlatformReportForm;
import com.gameportal.manage.reportform.model.RegisterReportForm;
import com.gameportal.manage.reportform.model.WithdrawalReportForm;

public interface ITotalReportFormService {

	/**
	 * 统计注册人数
	 * @param params:{starttime,endtime}
	 * @return
	 */
	public List<RegisterReportForm> getRegister(Map<String, Object> params);
	
	/**
	 * 统计下注总额
	 * @param params
	 * @return
	 */
	public List<BetReportForm> getTotalBetList(Map<String, Object> params);
	
	/**
	 * 充值报表列表
	 * @param params
	 * @return
	 */
	public List<PayReportForm> getPayReportList(Map<String, Object> params);
	
	/**
	 * 提款报表列表
	 * @param params
	 * @return
	 */
	public List<WithdrawalReportForm> getWithdrawalReportList(Map<String, Object> params);
	
	/**
	 * 优惠金额统计查询
	 * @param params
	 * @return
	 */
	public List<FavorableReportForm> getFavorableReportList(Map<String, Object> params);
	
	/**
	 * 洗码金额统计查询
	 * @param params
	 * @return
	 */
	public FavorableReportForm getXimaReportObject(Map<String, Object> params);
	
	/**
	 * 平台总数据
	 * @param params
	 * @return
	 */
	public List<PlatformReportForm> getPlatformReport(Map<String, Object> params);
	
	/**
	 * 获取活跃会员数
	 * @param params
	 * @return
	 */
	public HyMemberReportForm getHymember(Map<String, Object> params);
}
