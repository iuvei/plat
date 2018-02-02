package com.na.manager.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.na.manager.bean.AgentWinLostReportRequest;
import com.na.manager.bean.vo.AgentWinLostVO;
import com.na.manager.dao.IAgentWinLostMapper;
import com.na.manager.entity.LiveUser;
import com.na.manager.enums.StatisType;
import com.na.manager.service.IAgentWinLostService;

/**
 * @author andy
 * @date 2017年6月24日 上午10:36:00
 * 
 */
@Service
public class AgentWinLostServiceImpl implements IAgentWinLostService {

	@Autowired
	private IAgentWinLostMapper agentWinLostMapper;

	@Override
	@Transactional(readOnly = true)
	public List<AgentWinLostVO> statisAgentTeam(AgentWinLostReportRequest agentReportRequest) {
		List<AgentWinLostVO> list = agentWinLostMapper.statisAgentTeam(agentReportRequest);
		list.forEach(item -> {
			item.setStatisType(StatisType.AGENT.get());
			item.setStatisTypeDesc(StatisType.TEAM.getText());
		});
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public List<AgentWinLostVO> statisDirectProxyAndMember(AgentWinLostReportRequest agentReportRequest,LiveUser liveUser) {
		// 查询直属代理列表
		List<AgentWinLostVO> list = agentWinLostMapper.statisDirectProxy(agentReportRequest);
		list.forEach(item->{
			item.setStatisType(StatisType.AGENT.get());
			item.setStatisTypeDesc(StatisType.AGENT.getText());
		});
		// 查询直属会员汇总
		AgentWinLostVO staDirectMember =agentWinLostMapper.statisDirectMember(agentReportRequest);
		if(staDirectMember !=null){
			staDirectMember.setStatisType(StatisType.MEMBERSUM.get());
			staDirectMember.setStatisTypeDesc(StatisType.MEMBERSUM.getText());
			staDirectMember.setAgentId(liveUser.getId());
			staDirectMember.setLoginName(liveUser.getLoginName());
			BigDecimal washBetting;
			if(staDirectMember.getWashBetting().compareTo(BigDecimal.ZERO) == 0) {
				washBetting = new BigDecimal(1);
			} else {
				washBetting = staDirectMember.getWashBetting();
			}
			staDirectMember.setWinloSepercentage((staDirectMember.getWinLostAmount().add(staDirectMember.getMemberWashAmount())).divide(washBetting,10,BigDecimal.ROUND_HALF_DOWN));
			list.add(staDirectMember);
		}
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public List<AgentWinLostVO> listDirectdMember(AgentWinLostReportRequest agentReportRequest) {
		List<AgentWinLostVO> memberList = agentWinLostMapper.listDirectdMember(agentReportRequest);
		memberList.forEach(item->{
			item.setStatisType(StatisType.MEMBER.get());
			item.setStatisTypeDesc(StatisType.MEMBER.getText());
		});
		return memberList;
	}

}
