package com.gameportal.manage.reportform.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gameportal.manage.reportform.dao.TotalReportFormDao;
import com.gameportal.manage.reportform.model.BetReportForm;
import com.gameportal.manage.reportform.model.FavorableReportForm;
import com.gameportal.manage.reportform.model.HyMemberReportForm;
import com.gameportal.manage.reportform.model.PayReportForm;
import com.gameportal.manage.reportform.model.PlatformReportForm;
import com.gameportal.manage.reportform.model.RegisterReportForm;
import com.gameportal.manage.reportform.model.WithdrawalReportForm;
import com.gameportal.manage.reportform.service.ITotalReportFormService;

@Service("totalReportFormService")
public class TotalReportFormServiceImpl implements ITotalReportFormService{

	@Resource(name = "totalReportFormDao")
	private TotalReportFormDao totalReportFormDao;
	@Override
	public List<RegisterReportForm> getRegister(Map<String, Object> params) {
		return totalReportFormDao.getRegister(params);
	}
	@Override
	public List<BetReportForm> getTotalBetList(Map<String, Object> params) {
		return totalReportFormDao.getTotalBetList(params);
	}
	
	/**
	 * 充值报表列表
	 * @param params
	 * @return
	 */
	public List<PayReportForm> getPayReportList(Map<String, Object> params){
		return totalReportFormDao.getPayReportList(params);
	}
	/**
	 * 提款报表列表
	 * @param params
	 * @return
	 */
	public List<WithdrawalReportForm> getWithdrawalReportList(Map<String, Object> params){
		return totalReportFormDao.getWithdrawalReportList(params);
	}
	
	/**
	 * 优惠金额统计查询
	 * @param params
	 * @return
	 */
	public List<FavorableReportForm> getFavorableReportList(Map<String, Object> params){
		return totalReportFormDao.getFavorableReportList(params);
	}
	
	/**
	 * 洗码金额统计查询
	 * @param params
	 * @return
	 */
	public FavorableReportForm getXimaReportObject(Map<String, Object> params){
		return totalReportFormDao.getXimaReportList(params);
	}
	
	/**
	 * 平台总数据
	 * @param params
	 * @return
	 */
	public List<PlatformReportForm> getPlatformReport(Map<String, Object> params){
		return totalReportFormDao.getPlatformReport(params);
	}
	
	/**
	 * 获取活跃会员
	 */
	@Override
	public HyMemberReportForm getHymember(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return totalReportFormDao.getHymember(params);
	}

}
