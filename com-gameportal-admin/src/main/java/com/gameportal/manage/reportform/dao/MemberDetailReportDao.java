package com.gameportal.manage.reportform.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gameportal.manage.member.model.GameTransfer;
import com.gameportal.manage.pay.model.PayOrder;
import com.gameportal.manage.reportform.model.ReportForm;
import com.gameportal.manage.system.dao.BaseIbatisDAO;
import com.gameportal.manage.user.model.AccountMoney;
import com.gameportal.manage.xima.model.MemberXimaMain;

/**
 * 会员明细报表Dao
 * @author Administrator
 *
 */
@Component("memberDetailReportDao")
@SuppressWarnings("all")
public class MemberDetailReportDao extends BaseIbatisDAO {

	@Override
	public Class getEntityClass() {
		return null;
	}
	
	/**
	 * 钱包余额
	 * @param params 参数
	 * @return
	 */
	public Map<String,Object> getAccountMoney(Map<String, Object> params){
		return  (Map<String, Object>) super.getSqlMapClientTemplate().queryForObject("MemberDetailReport.selectAccountMoney", params);
	}
	
	/**
	 * 存款/取款/优惠列表集合 
	 * @param params 参数
	 * @return
	 */
	public List<PayOrder> getPayMoneyList(Map<String, Object> params){
		return  super.getSqlMapClientTemplate().queryForList("MemberDetailReport.selectPayMoneyList", params);
	}

	/**
	 * 存款/取款/优惠总计
	 * @param params 参数
	 * @return
	 */
	public Long getPayMoneyCount(Map<String, Object> params){
		return  (Long) super.getSqlMapClientTemplate().queryForObject("MemberDetailReport.selectPayMoneyCount", params);
	}
	
	/**
	 * 转账列表集合 
	 * @param params 参数
	 * @return
	 */
	public List<GameTransfer> getTransferList(Map<String, Object> params){
		return  super.getSqlMapClientTemplate().queryForList("MemberDetailReport.selectTransferList", params);
	}

	/**
	 * 转账总计
	 * @param params 参数
	 * @return
	 */
	public Long getTransferCount(Map<String, Object> params){
		return  (Long) super.getSqlMapClientTemplate().queryForObject("MemberDetailReport.selectTransferCount", params);
	}
	
	/**
	 * 会员洗码集合 
	 * @param params 参数
	 * @return
	 */
	public List<MemberXimaMain> getMemberXimaList(Map<String, Object> params){
		return  super.getSqlMapClientTemplate().queryForList("MemberDetailReport.selectMemberXimaList", params);
	}

	/**
	 * 会员洗码总计
	 * @param params 参数
	 * @return
	 */
	public Long getMemberXimaCount(Map<String, Object> params){
		return  (Long) super.getSqlMapClientTemplate().queryForObject("MemberDetailReport.selectMemberXimaCount", params);
	}
}
