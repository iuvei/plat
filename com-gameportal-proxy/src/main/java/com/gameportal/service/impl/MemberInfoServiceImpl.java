package com.gameportal.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gameportal.domain.AccountMoney;
import com.gameportal.domain.BetLog;
import com.gameportal.domain.BetLogTotal;
import com.gameportal.domain.DownUserReportTotal;
import com.gameportal.domain.MemberInfo;
import com.gameportal.domain.MemberInfoReport;
import com.gameportal.domain.Page;
import com.gameportal.domain.ProxyDomain;
import com.gameportal.domain.ProxySet;
import com.gameportal.domain.ProxyUserXimaLog;
import com.gameportal.domain.UserXimaSet;
import com.gameportal.persistence.MemberInfoMapper;
import com.gameportal.service.BaseService;
import com.gameportal.service.IMemberInfoService;

/**
 * 会员信息业务实现类
 * @author leron
 *
 */
@SuppressWarnings("all")
@Service("memberInfoServiceImpl")
public class MemberInfoServiceImpl extends BaseService implements IMemberInfoService {
	
	@Autowired
	private MemberInfoMapper memberInfoMapper;
	
	@Override
	public MemberInfo queryMemberInfo(Map<String, Object> params) {
		try {
			List<MemberInfo> list=memberInfoMapper.selectMemberInfo(params);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		} catch (Exception e) {
			logger.error("查询会员信息错误：", e);
		}
		return null;
	}
	
	@Override
	public Long queryDownCount(Map<String, Object> params) {
		return memberInfoMapper.selectDownCount(params);
	}
	
	@Override
	public ProxyDomain queryProxyUrl(Map<String, Object> pamras) {
		try {
			List<ProxyDomain> list=memberInfoMapper.selectProxyUrl(pamras);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		} catch (Exception e) {
			logger.error("查询代理推广地址错误：", e);
		}
		return null;
	}
	
	@Override
	public String queryProxyLoss(Map<String, Object> params) {
		return memberInfoMapper.getProxyLoss(params);
	}
	
	@Override
	public String queryProxyPreferential(Map<String, Object> params) {
		return memberInfoMapper.getProxyPreferential(params);
	}
	
	@Override
	public ProxySet queryScaleResult(Map<String, Object> params) {
		try {
			List<ProxySet> list=memberInfoMapper.getScaleResult(params);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		} catch (Exception e) {
			logger.error("查询代理占成错误：", e);
		}
		return null;
	}
	
	@Override
	public List<MemberInfoReport> queryMemberInfoReport(Page page) {
		return memberInfoMapper.findlistPageMonthReport(page);
	}
	
	@Override
	public DownUserReportTotal queryMemberInfoReportTotal(
			Map<String, Object> params) {
		return memberInfoMapper.downUserMonthReportTotal(params);
	}
	
	@Override
	public List<BetLogTotal> queryDownUser(Page page) {
		return memberInfoMapper.findlistPageDownUser(page);
	}
	
	@Override
	public List<BetLog> queryDownUserBetLog(Page page) {
		return memberInfoMapper.findlistPageDownUserDetail(page);
	}
	
	@Override
	public List<ProxyUserXimaLog> queryDownUserXimaLog(Page page) {
		return memberInfoMapper.findlistPageMemberXimaLog(page);
	}
	
	@Override
	public UserXimaSet queryUserXimaSet(Map<String, Object> params) {
		return memberInfoMapper.findUserXimaSet(params);
	}
	
	@Override
	public boolean updateUserXimaSet(UserXimaSet userXimaSet)  throws Exception{
		int count= memberInfoMapper.updateXimaScale(userXimaSet);
		if(count>0){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean insertUserXimaSet(UserXimaSet userXimaSet) {
		int count= memberInfoMapper.insertUserXimaSet(userXimaSet);
		if(count>0){
			return true;
		}
		return false;
	}

	@Override
	public MemberInfoReport querySingleWinorLess(Map<String, Object> map) {
		return memberInfoMapper.querySingleWinorLess(map);
	}
	
	@Override
	public BetLogTotal userBetDailyStats(Map<String, Object> params) {
		return memberInfoMapper.userBetDailyStats(params);
	}

	@Override
	public List<MemberInfo> getPlayerByDate(Map<String, Object> params) {
		return memberInfoMapper.getPlayerByDate(params);
	}

	@Override
	public List<MemberInfo> getTransByDate(Map<String, Object> params) {
		return memberInfoMapper.getTransByDate(params);
	}

	@Override
	public DownUserReportTotal queryUserDailyReport(Map<String, Object> params) {
		return memberInfoMapper.queryUserDailyReport(params);
	}

	@Override
	public List<MemberInfo> findlistPageDownProxy(Page page) {
		return memberInfoMapper.findlistPageDownProxy(page);
	}
}
