package com.na.manager.service;

import java.util.List;

import com.na.manager.bean.AgentWinLostReportRequest;
import com.na.manager.bean.vo.AgentWinLostVO;
import com.na.manager.entity.LiveUser;

/**
 * @author andy
 * @date 2017年6月24日 上午10:35:24
 * 
 */
public interface IAgentWinLostService {

	List<AgentWinLostVO> statisAgentTeam(AgentWinLostReportRequest agentReportRequest);

	List<AgentWinLostVO> statisDirectProxyAndMember(AgentWinLostReportRequest agentReportRequest,LiveUser liveUser);

	List<AgentWinLostVO> listDirectdMember(AgentWinLostReportRequest agentReportRequest);
}
