package com.gameportal.manage.member.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.member.model.GameTransfer;
import com.gameportal.manage.member.model.MemberInfo;
import com.gameportal.manage.member.model.MemberInfoReport;
import com.gameportal.manage.member.model.MemberInfoReportTotal;
import com.gameportal.manage.member.model.MemberInfoVo;
import com.gameportal.manage.member.model.UserLoginLog;
import com.gameportal.manage.pay.model.PayOrder;
import com.gameportal.manage.pay.model.PayOrderLog;
import com.gameportal.manage.user.model.AccountMoney;


public interface IMemberInfoService {
	public abstract MemberInfo saveUserInfo(MemberInfo memberInfo) throws Exception;

	public abstract MemberInfo qeuryMemberInfo(Map paramMap);

	public abstract MemberInfo findMemberInfoId(Long uiid);
	
	

	public abstract boolean modifyMemberInfo(MemberInfo MemberInfo) throws Exception;
	
	public abstract boolean deleteMemberInfo(Long uiid) ;

	public abstract boolean isExistByAcc(String account);
	
	public abstract List<MemberInfo> queryMemberInfo(Map map,Integer startNo, Integer pagaSize);
	
	public abstract List<MemberInfoVo> queryMemberInfoVo(Map map,Integer startNo, Integer pagaSize);
	
	public abstract Long queryMemberInfoCount(Map map);
	
	public abstract Long queryMemberInfoCountVo(Map map);
	
	public abstract boolean updateAccountMoneyObj(AccountMoney accountMoney);
	
	public abstract AccountMoney getAccountMoneyObj(Long uiid, Integer status);
	
	public abstract List<MemberInfoReport> queryMemberReport(Map map,Integer startNo, Integer pagaSize);
	
	public abstract MemberInfoReportTotal queryMemberReportTotal(Map map);
	
	public abstract Long queryMemberInfoReportCount(Map map);
	
	public Long getCountLog(Map<String, Object> params);
	
	public List<UserLoginLog> getListLog(Map<String, Object> params,int thisPage,int pageSize);
	
	public List<MemberInfo>  queryMemberInfoByAccount(String account);
	
	public Long queryBirthDayCount(Map<String, Object> params);
	
	public List<MemberInfo> queryBirthDayResult(Map<String, Object> params,Integer startNo, Integer pageSize);
	
	public Long queryFkMemberCount(Map<String, Object> params);
	
	public List<MemberInfoVo> queryFkMemberResult(Map<String, Object> params,Integer startNo, Integer pageSize);
	
	public MemberInfoVo queryFkMemberDetail(Map<String, Object> params);
	
	/**
	 * 风控资金流水总数
	 * @param params
	 * @return
	 */
	public Long queryPayOrderLogCount(Map<String, Object> params);
	
	/**
	 * 风控资金流水列表
	 * @param params
	 * @param startNo
	 * @param pageSize
	 * @return
	 */
	public List<PayOrderLog> queryPayOrderLogResult(Map<String, Object> params,Integer startNo, Integer pageSize);
	
	/**
	 * 转账记录
	 * @param params
	 * @param startNo
	 * @param pageSize
	 * @return
	 */
	public List<GameTransfer> queryTransferResult(Map<String, Object> params,Integer startNo,Integer pageSize);
 	
	/**
	 * 会员登录日志记录
	 * @param params
	 * @param startNo
	 * @param pageSize
	 * @return
	 */
	public List<UserLoginLog> queryUserLoginLogResult(Map<String, Object> params,Integer startNo,Integer pageSize);

	/***
	 * 存款-提款-加款
	 * @param params
	 * @param startNo
	 * @param pageSize
	 * @return
	 */
	public List<PayOrder> queryPayOrderResult(Map<String, Object> params,Integer startNo,Integer pageSize);
}
