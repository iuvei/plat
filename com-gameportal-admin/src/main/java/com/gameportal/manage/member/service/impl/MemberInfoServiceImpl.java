package com.gameportal.manage.member.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.gameportal.manage.member.dao.MemberInfoDao;
import com.gameportal.manage.member.model.GameTransfer;
import com.gameportal.manage.member.model.MemberInfo;
import com.gameportal.manage.member.model.MemberInfoReport;
import com.gameportal.manage.member.model.MemberInfoReportTotal;
import com.gameportal.manage.member.model.MemberInfoVo;
import com.gameportal.manage.member.model.UserLoginLog;
import com.gameportal.manage.member.service.IMemberInfoService;
import com.gameportal.manage.pay.model.PayOrder;
import com.gameportal.manage.pay.model.PayOrderLog;
import com.gameportal.manage.user.dao.AccountMoneyDao;
import com.gameportal.manage.user.model.AccountMoney;

@SuppressWarnings("all")
@Service
public class MemberInfoServiceImpl implements IMemberInfoService {

	@Resource(name = "memberInfoDao")
	private MemberInfoDao memberInfoDao = null;
	
	@Resource(name = "accountMoneyDao")
	private AccountMoneyDao accountMoneyDao = null;
	
	@Override
	public MemberInfo findMemberInfoId(Long uiid) {
		return (MemberInfo) memberInfoDao.findById(uiid);
		
	}
	
	@Override
	public boolean modifyMemberInfo(MemberInfo memberInfo) throws Exception {
		return memberInfoDao.saveOrUpdate(memberInfo);
		
	}

	@Override
	public MemberInfo qeuryMemberInfo(Map paramMap) {
		List<MemberInfo> list = memberInfoDao.queryForPager(paramMap,0, 1);
		if(null != list && list.size() > 0){
			return list.get(0);
		}
		return null;
	}


	@Override
	public boolean deleteMemberInfo(Long uiid) {
		return	memberInfoDao.delete(uiid);
		
	}

	@Override
	public MemberInfo saveUserInfo(MemberInfo memberInfo) throws Exception {
		
		memberInfo= (MemberInfo) memberInfoDao.save(memberInfo);
		return StringUtils.isNotBlank(ObjectUtils.toString(memberInfo.getUiid())) ? memberInfo : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MemberInfo> queryMemberInfo(Map paramMap, Integer startNo,
			Integer pagaSize) {
	
		List<MemberInfo> list = memberInfoDao.queryForPager(paramMap,
				startNo, pagaSize);
		return list;
	}

	@Override
	public Long queryMemberInfoCount(Map map) {
		return memberInfoDao.getRecordCount(map);
	}
	@Override
	public boolean isExistByAcc(String account) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("account", account);
		return memberInfoDao.queryForObject(memberInfoDao.getSelectQuery(), map)==null?false:true;
		
	}

	@Override
	public List<MemberInfoVo> queryMemberInfoVo(Map map, Integer startNo,
			Integer pageSize) {
		map.put("limit", true);
		map.put("thisPage", startNo);
		map.put("pageSize", pageSize);
		List<MemberInfoVo> list= memberInfoDao.getSqlMapClientTemplate().queryForList(MemberInfo.class.getSimpleName()+"."+"pageSelectAgent", map);				
		return list;
	}

	@Override
	public Long queryMemberInfoCountVo(Map map) {
		return (Long)memberInfoDao.queryForObject(MemberInfo.class.getSimpleName()+"."+"countVo", map);
	}
	
	@Override
	public Long queryFkMemberCount(Map<String, Object> params) {
		return (Long)memberInfoDao.queryForObject(MemberInfo.class.getSimpleName()+"."+"fkMemberCount", params);
	}
	
	@Override
	public List<MemberInfoVo> queryFkMemberResult(Map<String, Object> params,
			Integer startNo, Integer pageSize) {
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		List<MemberInfoVo> list= memberInfoDao.getSqlMapClientTemplate().queryForList(MemberInfo.class.getSimpleName()+"."+"pageSelectFkMemberInfo", params);				
		return list;
	}

	@Override
	public AccountMoney getAccountMoneyObj(Long uiid, Integer status) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uiid", uiid);
		if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
			map.put("status", status);
		}
		AccountMoney accountMoney = (AccountMoney) accountMoneyDao
				.queryForObject(accountMoneyDao.getSelectQuery(), map);
		return StringUtils.isNotBlank(ObjectUtils.toString(accountMoney)) ? accountMoney
				: null;
	}

	@Override
	public boolean updateAccountMoneyObj(AccountMoney accountMoney) {
		return accountMoneyDao.update(accountMoney);
	}

	@Override
	public Long queryMemberInfoReportCount(Map map) {
		return memberInfoDao.getRecordCount(map);
	}

	@Override
	public List<MemberInfoReport> queryMemberReport(Map map, Integer startNo,
			Integer pagaSize) {
		map.put("limit", true);
		map.put("thisPage", startNo);
		map.put("pageSize", pagaSize);
		List<MemberInfoReport> list= memberInfoDao.getSqlMapClientTemplate().queryForList(MemberInfo.class.getSimpleName()+".pageSelectWin", map);				
		return list;
	}
	
	@Override
	public MemberInfoReportTotal queryMemberReportTotal(Map map) {
		return (MemberInfoReportTotal) memberInfoDao.getSqlMapClientTemplate().queryForObject(MemberInfo.class.getSimpleName()+".pageSelectWinTotal", map);				
	}

	@Override
	public Long getCountLog(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return memberInfoDao.getCountLog(params); 
	}

	@Override
	public List<UserLoginLog> getListLog(Map<String, Object> params,
			int thisPage, int pageSize) {
		params.put("limit", true);
		params.put("thisPage", thisPage);
		params.put("pageSize", pageSize);
		return memberInfoDao.getListLog(params, thisPage, pageSize);
	}
	
	@Override
	public List<MemberInfo> queryMemberInfoByAccount(String account) {
		return memberInfoDao.getMemberInfoByAccount(account);
	}

	@Override
	public Long queryBirthDayCount(Map<String, Object> params) {
		return memberInfoDao.selectBirthDayCount(params);
	}
	
	@Override
	public List<MemberInfo> queryBirthDayResult(Map<String, Object> params,Integer startNo, Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		return memberInfoDao.selectBirthDayList(params);
	}
	
	@Override
	public MemberInfoVo queryFkMemberDetail(Map<String, Object> params) {
		List<MemberInfoVo> list= memberInfoDao.getSqlMapClientTemplate().queryForList(MemberInfo.class.getSimpleName()+"."+"fkMemberDetail", params);				
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public Long queryPayOrderLogCount(Map<String, Object> params) {
		return (Long)memberInfoDao.queryForObject(MemberInfo.class.getSimpleName()+"."+"fkPayOrderLogCount", params);
	}
	
	@Override
	public List<PayOrderLog> queryPayOrderLogResult(Map<String, Object> params,
			Integer startNo, Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		List<PayOrderLog> list= memberInfoDao.getSqlMapClientTemplate().queryForList(MemberInfo.class.getSimpleName()+".fkPayOrderLogResult", params);				
		return list;
	}
	
	@Override
	public List<GameTransfer> queryTransferResult(Map<String, Object> params,
			Integer startNo, Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		List<GameTransfer> list= memberInfoDao.getSqlMapClientTemplate().queryForList(MemberInfo.class.getSimpleName()+".fkTransferResult", params);				
		return list;
	}
	
	@Override
	public List<UserLoginLog> queryUserLoginLogResult(
			Map<String, Object> params, Integer startNo, Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		List<UserLoginLog> list= memberInfoDao.getSqlMapClientTemplate().queryForList(MemberInfo.class.getSimpleName()+".fkLoginLogResult", params);				
		return list;
	}
	
	@Override
	public List<PayOrder> queryPayOrderResult(Map<String, Object> params,
			Integer startNo, Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		List<PayOrder> list= memberInfoDao.getSqlMapClientTemplate().queryForList(MemberInfo.class.getSimpleName()+".fkPayOrderResult", params);				
		return list;
	}
}
